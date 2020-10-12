package com.ktor.stock.market.game.jbosak.graphQL.schema

import com.ktor.stock.market.game.jbosak.service.getCandles
import graphql.schema.AsyncDataFetcher.async
import graphql.schema.idl.TypeRuntimeWiring

fun getCandleSchema() =
    """
       type Candle {
            openPrice:Float!
            highPrice:Float!
            lowPrice:Float!
            closePrice:Float!
            volume:Float!
            time: String!
        }
    """

fun TypeRuntimeWiring.Builder.candleQueryResolvers() =
    this.dataFetcher("getCandles", async { env->
        val ticker = env.arguments["ticker"] as String
        val from = env.arguments["from"] as String
        val to = env.arguments["to"] as String
        val resolution = env.arguments["resolution"] as String
        getCandles(ticker)
    })

