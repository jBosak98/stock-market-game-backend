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

fun candlesResolutionFrom(res: String) = when(res){
    "ONE_MINUTE" -> CandlesResolution.ONE_MINUTE
    "1" -> CandlesResolution.ONE_MINUTE
    "FIVE_MINUTES" -> CandlesResolution.FIVE_MINUTES
    "5" -> CandlesResolution.FIVE_MINUTES
    "FIFTEEN_MINUTES" -> CandlesResolution.FIFTEEN_MINUTES
    "15" -> CandlesResolution.FIFTEEN_MINUTES
    "THIRTY_MINUTES" -> CandlesResolution.THIRTY_MINUTES
    "30" -> CandlesResolution.THIRTY_MINUTES
    "ONE_DAY" -> CandlesResolution.ONE_DAY
    "D" -> CandlesResolution.ONE_DAY
    "ONE_WEEK" -> CandlesResolution.ONE_WEEK
    "W" -> CandlesResolution.ONE_WEEK
    "ONE_MONTH" -> CandlesResolution.ONE_MONTH
    "M" -> CandlesResolution.ONE_MONTH
    else -> CandlesResolution.ONE_DAY
}