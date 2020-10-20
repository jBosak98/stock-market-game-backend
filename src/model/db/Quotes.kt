package com.ktor.stock.market.game.jbosak.model.db

import org.jetbrains.exposed.sql.Table

object Quotes: Table(){
    val id = integer("id").primaryKey().autoIncrement()
    val companyId = integer("company_id") references Companies.id
    val openDayPrice = float("open_day_price").nullable()
    val highDayPrice = float("high_day_price").nullable()
    val lowDayPrice = float("low_day_price").nullable()
    val currentPrice = float("current_price").nullable()
    val previousClosePrice = float("previous_close_price").nullable()
    val dailyChange = float("daily_change").nullable()
    val dailyChangePercentage = float("daily_change_percentage").nullable()
    val date = datetime("date")
}