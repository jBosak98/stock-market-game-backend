package com.ktor.stock.market.game.jbosak.model

import org.joda.time.DateTime

data class StockPrice(
    val id: Int,
    val updatedOn:DateTime,
    val lastPrice:Int,
    val lastTime:DateTime,
    val bidPrice:Int,
    val askPrice:Int,
    val askSize:Int,
    val openPrice:Int?,
    val highPrice:Int?,
    val lowPrice: Int?,
    val exchangeVolume:Int,
    val marketVolume:Int?,
    val dataSource:String,
    val securityExternalId:String,
    val securityTicker:String
)