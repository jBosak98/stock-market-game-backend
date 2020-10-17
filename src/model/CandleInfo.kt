package com.ktor.stock.market.game.jbosak.model


data class CandleInfo(
    val ticker:String,
    val candleIntervals: MutableList<CandleIntervals>
)