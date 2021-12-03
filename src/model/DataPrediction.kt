package com.ktor.stock.market.game.jbosak.model

data class DataPrediction(
    val ticker: String,
    val data: List<DataPredictionCandle>
)