package com.ktor.stock.market.game.jbosak.model.db

import org.jetbrains.exposed.sql.Table

object StockPrices: Table(){
    val id = integer("id").primaryKey().autoIncrement()
    val updatedOn = datetime("updated_on")
    val lastPrice = integer("last_price")
    val lastTime = datetime("last_time")
    val bidPrice = integer("bid_price")
    val askPrice =  integer("ask_price")
    val askSize = integer("ask_size")
    val openPrice =  integer("open_price").nullable()
    val highPrice =  integer("high_price").nullable()
    val lowPrice =  integer("low_price").nullable()
    val exchangeVolume =  integer("exchange_volume")
    val marketVolume =  integer("market_volume").nullable()
    val dataSource = text("source")
    val securityExternalId = (text("security_external_id") /*references Companies.externalId*/)
    val securityTicker = (text("security_ticker") references Companies.ticker)
}
