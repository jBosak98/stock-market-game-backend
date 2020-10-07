package com.ktor.stock.market.game.jbosak.model

import com.ktor.stock.market.game.jbosak.model.graphql.TransactionGraphQL
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

fun Transaction.toGraphQL() =
    TransactionGraphQL(
        id = id,
        playerId = playerId,
        companyId = companyId,
        pricePerShare = pricePerShare,
        quantity = quantity,
        createdAt = createdAt,
        type = type.toString()
    )