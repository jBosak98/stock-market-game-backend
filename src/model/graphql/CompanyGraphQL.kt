package com.ktor.stock.market.game.jbosak.model.graphql

data class CompanyGraphQL(
    val id: Int,
    val ticker: String,
    val name: String,
    val lei: String?,
    val cik: String,
    val externalId: String,
    val stockPrice: Any?
)