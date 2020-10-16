package com.ktor.stock.market.game.jbosak.graphQL.schema

import com.ktor.stock.market.game.jbosak.model.candlesResolutionFrom
import com.ktor.stock.market.game.jbosak.service.getCandles
import com.ktor.stock.market.game.jbosak.utils.isValidDateTime
import graphql.schema.AsyncDataFetcher.async
import graphql.schema.idl.TypeRuntimeWiring
import org.joda.time.DateTime

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
        val fromString = env.arguments["from"] as String
        val from =
            if (isValidDateTime(fromString)) DateTime.parse(fromString)
            else DateTime.now().minusYears(2)
        val to =
            if (isValidDateTime(fromString))  DateTime.parse(fromString)
            else DateTime.now()

        val resolution = env.arguments["resolution"] as String
        val res = candlesResolutionFrom(resolution)
        getCandles(ticker,res,from, to)
    })

