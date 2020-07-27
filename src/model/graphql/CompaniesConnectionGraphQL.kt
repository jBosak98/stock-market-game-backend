package com.ktor.stock.market.game.jbosak.model.graphql

data class CompaniesConnectionGraphQL(
    override val totalCount: Int,
    val companies: List<CompanyGraphQL?>
) : ConnectionGraph