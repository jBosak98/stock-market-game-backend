package com.ktor.stock.market.game.jbosak.model.db

import org.jetbrains.exposed.sql.Table

object Quotes: Table("Quotes"){
    val id = integer("id").primaryKey().autoIncrement()
    val companyId = integer("companyId") references Companies.id
//    val openDayPrice = float("open_day_price").nullable()
//    val highDayPrice = float("high_day_price").nullable()
//    val lowDayPrice = float("low_day_price").nullable()
    val currentPrice = float("currentPrice").nullable()
//    val previousClosePrice = float("previous_close_price").nullable()
    val dailyChange = float("dailyChange").nullable()
    val dailyChangePercentage = float("dailyChangePercentage").nullable()
    val date = datetime("date")
}