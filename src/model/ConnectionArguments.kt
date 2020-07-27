package com.ktor.stock.market.game.jbosak.model

data class ConnectionArguments<T>(
    val skip: Int,
    val limit: Int?,
    val filterArgs: T?
)