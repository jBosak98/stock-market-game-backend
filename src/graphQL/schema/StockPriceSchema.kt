package com.ktor.stock.market.game.jbosak.graphQL.schema

import arrow.core.valueOr
import com.ktor.stock.market.game.jbosak.graphQL.dataLoadersConfig.DataLoaderKey
import com.ktor.stock.market.game.jbosak.graphQL.dataLoadersConfig.dataloaderResolver
import com.ktor.stock.market.game.jbosak.graphQL.dataLoadersConfig.resolve
import com.ktor.stock.market.game.jbosak.model.graphql.CompanyGraphQL
import com.ktor.stock.market.game.jbosak.model.graphql.toGraphQLStockPrice
import com.ktor.stock.market.game.jbosak.model.toGraphQL
import com.ktor.stock.market.game.jbosak.service.getRealTimeSecurityPrice
import graphql.schema.AsyncDataFetcher.async
import graphql.schema.idl.TypeRuntimeWiring
import org.dataloader.BatchLoader
import org.dataloader.DataLoader
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
            val resolvers =
                dataloaderResolver(env)
            val response = getRealTimeSecurityPrice(ticker).valueOr { throw it }
            val company = resolvers.resolve<CompanyGraphQL>("company")(ticker)

            listOf(response).map { stockPrice ->
                toGraphQLStockPrice(stockPrice, company)
            }
        })

fun stockPriceDataLoader(): DataLoader<DataLoaderKey<String>, Any>? {
    val loader = BatchLoader<DataLoaderKey<String>, Any> { keys ->
        CompletableFuture.supplyAsync {
            keys.map {
                val price = getRealTimeSecurityPrice(it.key).valueOr { error -> throw error }
                val company = it
                    .resolve<CompanyGraphQL, String>("company")(price.securityTicker)
                price.toGraphQL(company)
            }
        }
    }
    return DataLoader.newDataLoader(loader)
}