package com.ktor.stock.market.game.jbosak.model


import com.ktor.stock.market.game.jbosak.model.graphql.CompanyGraphQL
import com.ktor.stock.market.game.jbosak.model.graphql.StockPriceGraphQL
import org.joda.time.DateTime

data class StockPrice(
    val id: Int,
    val updatedOn: DateTime,
    val lastPrice: Int,
    val lastTime: DateTime,
    val bidPrice: Int,
    val askPrice: Int,
    val askSize: Int,
    val openPrice: Int?,
    val highPrice: Int?,
    val lowPrice: Int?,
    val exchangeVolume: Int,
    val marketVolume: Int?,
    val dataSource: String,
    val securityExternalId: String,
    val securityTicker: String
)

fun StockPrice.toGraphQL(company: CompanyGraphQL?) =
    StockPriceGraphQL(
        id = this.id,
        updatedOn = this.updatedOn,
        lastPrice = this.lastPrice,
        lastTime = this.lastTime,
        bidPrice = this.bidPrice,
        askPrice = this.askPrice,
        askSize = this.askSize,
        dataSource = this.dataSource,
        exchangeVolume = this.exchangeVolume,
        highPrice = this.highPrice,
        lowPrice = this.lowPrice,
        marketVolume = this.marketVolume,
        openPrice = this.openPrice,
        securityExternalId = this.securityExternalId,
        securityTicker = this.securityTicker,
        company = company
    )
