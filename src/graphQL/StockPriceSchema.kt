package com.ktor.stock.market.game.jbosak.graphQL

import com.ktor.stock.market.game.jbosak.service.getRealTimeSecurityPrice
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
        }

    """

fun TypeRuntimeWiring.Builder.stockPriceMutationResolvers() =
    this

fun TypeRuntimeWiring.Builder.stockPriceQueryResolvers() =
    this.dataFetcher("getPrices"){ env ->
        val ticker = env.arguments["ticker"] as String
        val response = getRealTimeSecurityPrice(ticker)
        listOf(response)
    }