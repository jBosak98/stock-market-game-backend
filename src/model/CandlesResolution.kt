package com.ktor.stock.market.game.jbosak.model

enum class CandlesResolution(val resolution: String){
    ONE_MINUTE("1"),
    FIVE_MINUTES("5"),
    FIFTEEN_MINUTES("15"),
    THIRTY_MINUTES("30"),
    ONE_DAY("D"),
    ONE_WEEK("W"),
    ONE_MONTH("M")
}