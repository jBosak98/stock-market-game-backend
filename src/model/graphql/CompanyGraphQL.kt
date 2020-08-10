package com.ktor.stock.market.game.jbosak.model.graphql

import com.ktor.stock.market.game.jbosak.model.CompanyFinancials
import com.ktor.stock.market.game.jbosak.model.Quote

data class CompanyGraphQL(
    val id: Int,
    val ticker: String,
    val quote: Quote?,
    val financials: CompanyFinancials?,
    val country: String?,
    val currency: String?,
    val exchange: String?,
    val ipo: String?,
    val name: String?,
    val phone: String?,
    val shareOutstanding: Float?,
    val weburl: String?,
    val logo: String?,
    val finnhubIndustry: String?

)