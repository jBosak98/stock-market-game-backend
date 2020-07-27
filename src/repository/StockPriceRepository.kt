package com.ktor.stock.market.game.jbosak.repository

import com.intrinio.models.RealtimeStockPrice
import com.ktor.stock.market.game.jbosak.model.db.StockPrices
import com.ktor.stock.market.game.jbosak.utils.toDateTime
import com.ktor.stock.market.game.jbosak.utils.toPriceInt
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

object StockPriceRepository {

    fun insert(price: RealtimeStockPrice) = transaction {
        StockPrices.insert {
            it[updatedOn] = price.updatedOn.toDateTime()
            it[lastPrice] = price.lastPrice.toPriceInt()!!
            it[lastTime] = price.lastTime.toDateTime()
            it[bidPrice] = 0//price.bidPrice.toPriceInt()!!TODO: should be nullable
            it[askPrice] = price.askPrice.toPriceInt()!!
            it[askSize] = price.askSize.toPriceInt()!!
            it[openPrice] = price.openPrice.toPriceInt()
            it[highPrice] = price.highPrice.toPriceInt()
            it[lowPrice] = price.lowPrice.toPriceInt()
            it[exchangeVolume] = price.exchangeVolume.toPriceInt()!!
            it[marketVolume] = price.marketVolume.toPriceInt()
            it[dataSource] = price.source
            it[securityExternalId] = price.security.id
            it[securityTicker] = price.security.ticker
        }

    }

    fun findPrice(securityIdentifier: String) = transaction {
        StockPrices
            .select { StockPrices.securityTicker eq securityIdentifier }
            .map(ResultRow::toStockPrice)
    }

}
