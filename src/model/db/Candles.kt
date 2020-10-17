package com.ktor.stock.market.game.jbosak.model.db

import com.ktor.stock.market.game.jbosak.utils.uniqueIndexR
import org.jetbrains.exposed.sql.Table

object Candles: Table(){
    val id = integer("id").uniqueIndex().primaryKey().autoIncrement()
    val companyId = integer("company_id") references Companies.id
    val openPrice = float("open_price").nullable()
    val highPrice = float("high_price").nullable()
    val lowPrice = float("low_price").nullable()
    val closePrice = float("close_price").nullable()
    val volume = float("volume").nullable()
    val time = datetime("timestamp").nullable()
    val resolution = text("resolution")
    init {
        uniqueIndexR("uniqueCandle", companyId, time, resolution)
    }
}