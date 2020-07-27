package com.ktor.stock.market.game.jbosak.model.graphql

import com.ktor.stock.market.game.jbosak.model.StockPrice
import org.joda.time.DateTime

data class StockPriceGraphQL(
    val id:Int,
    val updatedOn: DateTime,
    val lastPrice:Int,
    val lastTime: DateTime,
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

fun toGraphQLStockPrice(stockPrice: StockPrice, company: Any?) =
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