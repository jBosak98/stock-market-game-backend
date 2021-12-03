package com.ktor.stock.market.game.jbosak.model

import com.google.gson.annotations.SerializedName

data class DataPredictionCandle(
    @SerializedName("openPrice") val openPrice:Float,
    @SerializedName("highPrice") val highPrice:Float,
    @SerializedName("lowPrice") val lowPrice:Float,
    @SerializedName("closePrice") val closePrice:Float,
    @SerializedName("volume") val volume:Float,
    @SerializedName("time") val time: String
)