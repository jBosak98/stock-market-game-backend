package com.ktor.stock.market.game.jbosak.repository


import com.ktor.stock.market.game.jbosak.model.Company
import com.ktor.stock.market.game.jbosak.model.StockPrice
import com.ktor.stock.market.game.jbosak.model.User
import com.ktor.stock.market.game.jbosak.model.db.Companies
import com.ktor.stock.market.game.jbosak.model.db.StockPrices
import com.ktor.stock.market.game.jbosak.model.db.Users
import org.jetbrains.exposed.sql.ResultRow

fun ResultRow.toUser() = User(
    id = this[Users.id],
    email = this[Users.name],
    password = this[Users.password],
    token = ""

)

fun ResultRow.toCompany() = Company(
    id = this[Companies.id],
    ticker = this[Companies.ticker],
    name = this[Companies.name],
    lei = this[Companies.lei],
    cik = this[Companies.cik],
    externalId = this[Companies.externalId]
)

fun ResultRow.toStockPrice() = StockPrice(
    id = this[StockPrices.id],
    updatedOn = this[StockPrices.updatedOn],
    lastPrice = this[StockPrices.lastPrice],
    lastTime = this[StockPrices.lastTime],
    bidPrice = this[StockPrices.bidPrice],
    askPrice = this[StockPrices.askPrice],
    askSize = this[StockPrices.askSize],
    openPrice = this[StockPrices.openPrice],
    highPrice = this[StockPrices.highPrice],
    lowPrice = this[StockPrices.lowPrice],
    exchangeVolume = this[StockPrices.exchangeVolume],
    marketVolume = this[StockPrices.marketVolume],
    dataSource = this[StockPrices.dataSource],
    securityExternalId = this[StockPrices.securityExternalId],
    securityTicker = this[StockPrices.securityTicker]
)