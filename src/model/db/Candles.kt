package com.ktor.stock.market.game.jbosak.model.db

import com.ktor.stock.market.game.jbosak.utils.uniqueIndexR
import org.jetbrains.exposed.sql.Table

object Candles: Table("Candles"){
    val id = integer("id").uniqueIndex().primaryKey().autoIncrement()
    val companyId = integer("companyId") references Companies.id
    val openPrice = float("openPrice").nullable()
    val highPrice = float("highPrice").nullable()
    val lowPrice = float("lowPrice").nullable()
    val closePrice = float("closePrice").nullable()
    val volume = float("volume").nullable()
    val time = datetime("timestamp").nullable()
    val resolution = text("resolution")
    init {
        uniqueIndexR("uniqueCandle", companyId, time, resolution)
    }
}