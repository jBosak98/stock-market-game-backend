package com.ktor.stock.market.game.jbosak.model

import org.joda.time.DateTime

data class Candles(
    val openPrices:List<Float>?,
    val highPrices:List<Float>?,
    val lowPrices:List<Float>?,
    val closePrices:List<Float>?,
    val volumes:List<Float>?,
    val time:List<DateTime>?,
    val status:String?
)