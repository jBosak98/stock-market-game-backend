package com.ktor.stock.market.game.jbosak.model

import org.joda.time.DateTime

data class Transaction(
    val id:Int,
    val playerId:Int,
    val companyId:Int,
    val pricePerShare:Float,
    val quantity:Int,
    val createdAt:DateTime,
    val type:TransactionType
)