package com.ktor.stock.market.game.jbosak.model.graphql

import org.joda.time.DateTime

data class TransactionGraphQL(
    val id: Int,
    val playerId: Int,
    val company: CompanyGraphQL?,
    val companyId: Int,
    val pricePerShare: Float,
    val quantity: Int,
    val createdAt: DateTime,
    val type: String
)

