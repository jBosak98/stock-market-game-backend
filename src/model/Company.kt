package com.ktor.stock.market.game.jbosak.model

import com.ktor.stock.market.game.jbosak.model.graphql.CompanyGraphQL

data class Company(
    val id: Int,
    val ticker: String,
    val financials: CompanyFinancials,
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



fun Company.toGraphQL( quote: Quote?) =
    CompanyGraphQL(
        id = this.id,
        ticker = this.ticker,
        financials = this.financials,
        country = this.country,
        currency = this.currency,
        exchange = this.exchange,
        ipo = this.ipo,
        name = this.name,
        phone = this.phone,
        shareOutstanding = this.shareOutstanding,
        weburl = this.weburl,
        logo = this.logo,
        finnhubIndustry = this.finnhubIndustry,
        quote = quote
    )