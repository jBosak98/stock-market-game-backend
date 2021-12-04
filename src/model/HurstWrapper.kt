package com.ktor.stock.market.game.jbosak.model

data class HurstWrapper(
    val timestamp:String?,
    val hurst: Hurst?,
    val predictions:List<Any>
)