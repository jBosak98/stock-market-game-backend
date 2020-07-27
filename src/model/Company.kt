package com.ktor.stock.market.game.jbosak.model

import com.ktor.stock.market.game.jbosak.model.graphql.CompanyGraphQL
import com.ktor.stock.market.game.jbosak.model.graphql.StockPriceGraphQL

data class Company(
    val id:Int,
    val ticker:String,
    val name: String,
    val lei: String?,
    val cik:String,
    val externalId:String
)

fun Company.toGraphQL(stockPrice:StockPriceGraphQL?) =
    CompanyGraphQL(
        id = this.id,
        ticker = this.ticker,
        cik = this.cik,
        externalId = this.externalId,
        lei = this.lei,
        name = this.name,
        stockPrice = stockPrice
    )