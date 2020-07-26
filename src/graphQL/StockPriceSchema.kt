package com.ktor.stock.market.game.jbosak.graphQL

import arrow.syntax.function.pipe
import com.ktor.stock.market.game.jbosak.model.StockPrice
import com.ktor.stock.market.game.jbosak.service.getRealTimeSecurityPrice
import graphql.schema.AsyncDataFetcher.async
import graphql.schema.idl.TypeRuntimeWiring
import org.dataloader.BatchLoader
import org.dataloader.DataLoader
import org.joda.time.DateTime
import java.util.concurrent.CompletableFuture

fun getStockPriceSchema() =
    """
        type StockPrice {
            id:Int!
            updatedOn:String
            lastPrice:Int!
            lastTime:String!
            bidPrice:Int!
            askPrice:Int!
            askSize:Int!
            openPrice:Int
            highPrice:Int
            lowPrice:Int
            exchangeVolume:Int!
            marketVolume:Int
            dataSource: String!
            securityExternalId: String!
            securityTicker: String!
            company: Company!
        }

    """

fun TypeRuntimeWiring.Builder.stockPriceMutationResolvers() =
    this

fun TypeRuntimeWiring.Builder.stockPriceQueryResolvers(): TypeRuntimeWiring.Builder =
    this
        .dataFetcher("getPrices", async { env ->
            val ticker = env.arguments["ticker"] as String
            val resolvedDataLoaders = dataloaderResolver(env)
            val response = getRealTimeSecurityPrice(ticker)
            val (companyResolver, companyKeyGenerator)
                    = dataloaderResolver(env).getOrElse("company") { Pair(null, null) }
            val company = companyResolver?.run {
                val companyArg = companyKeyGenerator?.invoke(ticker)
                val value = load(companyArg)
                dispatch()
                value?.get()
            }


            listOf(response).map { stockPrice ->
                toGraphQLStockPrice(stockPrice!!, company)
            }
        })

private fun toGraphQLStockPrice(stockPrice: StockPrice, company: Any?) =
    StockPriceGraphQL(
        id = stockPrice.id,
        updatedOn = stockPrice.updatedOn,
        lastPrice = stockPrice.lastPrice,
        lastTime = stockPrice.lastTime,
        bidPrice = stockPrice.bidPrice,
        askPrice = stockPrice.askPrice,
        askSize = stockPrice.askSize,
        dataSource = stockPrice.dataSource,
        exchangeVolume = stockPrice.exchangeVolume,
        highPrice = stockPrice.highPrice,
        lowPrice = stockPrice.lowPrice,
        marketVolume = stockPrice.marketVolume,
        openPrice = stockPrice.openPrice,
        securityExternalId = stockPrice.securityExternalId,
        securityTicker = stockPrice.securityTicker,
        company = company
    )

data class StockPriceGraphQL(
    val id:Int,
    val updatedOn: DateTime,
    val lastPrice:Int,
    val lastTime:DateTime,
    val bidPrice:Int,
    val askPrice:Int,
    val askSize:Int,
    val openPrice:Int?,
    val highPrice:Int?,
    val lowPrice:Int?,
    val exchangeVolume:Int,
    val marketVolume:Int?,
    val dataSource:String,
    val securityExternalId:String,
    val securityTicker:String,
    val company:Any?
)


fun stockPriceDataLoader(): DataLoader<DataLoaderKey<String>, Any>? {
    val loader = BatchLoader<DataLoaderKey<String>, Any> { keys ->
        CompletableFuture.supplyAsync {
            keys.map {
                val price = getRealTimeSecurityPrice(it.key)
                val (companyResolver, companyKeyGenerator)
                        = it.resolver.value.getOrElse("company") { Pair(null, null) }
                val company = companyResolver?.run {
                    val value = companyKeyGenerator?.invoke(price?.securityTicker).pipe{ load(it) }
                    dispatch()
                    value.get()
                }
                toGraphQLStockPrice(price!!, company)
            }
        }
    }
    return DataLoader.newDataLoader(loader)
}