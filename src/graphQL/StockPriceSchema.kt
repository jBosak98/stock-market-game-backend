package com.ktor.stock.market.game.jbosak.graphQL

import com.ktor.stock.market.game.jbosak.service.getRealTimeSecurityPrice
import graphql.schema.AsyncDataFetcher.async
import graphql.schema.idl.TypeRuntimeWiring

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
            listOf(response).map { stockPrice ->
                object {
                    val id = stockPrice!!.id
                    val updatedOn = stockPrice!!.updatedOn
                    val lastPrice = stockPrice!!.lastPrice
                    val lastTime = stockPrice!!.lastTime
                    val bidPrice = stockPrice!!.bidPrice
                    val askPrice = stockPrice!!.askPrice
                    val askSize = stockPrice!!.askSize
                    val openPrice = stockPrice!!.openPrice
                    val highPrice = stockPrice!!.highPrice
                    val lowPrice = stockPrice!!.lowPrice
                    val exchangeVolume = stockPrice!!.exchangeVolume
                    val marketVolume = stockPrice!!.marketVolume
                    val dataSource = stockPrice!!.dataSource
                    val securityExternalId = stockPrice!!.securityExternalId
                    val securityTicker = stockPrice!!.securityTicker
                    val company = resolvedDataLoaders["company"]?.load(ticker)
                }
            }
        })

