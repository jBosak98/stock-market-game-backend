package com.ktor.stock.market.game.jbosak.model

import org.joda.time.DateTime

data class Quote(
    val id: Int,
    val companyId: Int,
    val openDayPrice: Float?,
    val highDayPrice: Float?,
    val lowDayPrice: Float?,
    val currentPrice: Float?,
    val previousClosePrice: Float?,
    val dailyChange:Float?,
    val dailyChangePercentage:Float?,
    val date:DateTime
)