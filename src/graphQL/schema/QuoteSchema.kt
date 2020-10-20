package com.ktor.stock.market.game.jbosak.graphQL.schema

import arrow.core.valueOr
import com.ktor.stock.market.game.jbosak.graphQL.dataLoadersConfig.DataLoaderKey
import com.ktor.stock.market.game.jbosak.service.getQuote
import graphql.schema.AsyncDataFetcher.async
import graphql.schema.idl.TypeRuntimeWiring
import org.dataloader.BatchLoader
import org.dataloader.DataLoader
import java.util.concurrent.CompletableFuture

fun getQuoteSchema() =
    """
       type Quote {
            id:Int!
            companyId:Int!
            openDayPrice:Float
            highDayPrice:Float
            lowDayPrice:Float
            currentPrice:Float
            previousClosePrice:Float
            dailyChange:Float
            dailyChangePercentage:Float
            date: String!
        }
    """

fun TypeRuntimeWiring.Builder.quoteQueryResolvers() =
    this.dataFetcher("getQuote", async { env ->
        val ticker = env.arguments["ticker"] as String
        val quote = getQuote(ticker).valueOr { throw it }
        quote
    })

fun quoteDataLoader(): DataLoader<DataLoaderKey<String>, Any>? {
    val loader = BatchLoader<DataLoaderKey<String>, Any> { keys ->
        CompletableFuture.supplyAsync {
            keys.map { (key) ->
                val quote = getQuote(key).valueOr {throw it }
                quote
            }
        }
    }
    return DataLoader.newDataLoader(loader)
}