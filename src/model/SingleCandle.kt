package com.ktor.stock.market.game.jbosak.model

import org.joda.time.DateTime

data class SingleCandle(
    val openPrice:Float,
    val highPrice:Float,
    val lowPrice:Float,
    val closePrice:Float,
    val volume:Float,
    val time: DateTime
)