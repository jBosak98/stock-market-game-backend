package com.ktor.stock.market.game.jbosak.service

import com.intrinio.api.StockExchangeApi
import com.intrinio.invoker.ApiException
import com.intrinio.models.ApiResponseStockExchangeRealtimeStockPrices

//TODO: do something with these functions

private fun refetch(stockExchangeIdentifier:String){
//    val stockPrices = fetchStockPrices()
//    stockPrices?.stockExchange
}

fun fetchStockPrices(stockExchangeIdentifier:String,
                             source:String? = null,
                             pageSize:Int? = 100,
                             nextPage:String? = null): ApiResponseStockExchangeRealtimeStockPrices? {
    val api = StockExchangeApi()


    return try {
        val result
                = api.getStockExchangeRealtimePrices(
            stockExchangeIdentifier,
            source,
            pageSize,
            nextPage
        )
        result
    }catch (e:ApiException){
        println(e.message)
        null
    }

}