package com.ktor.stock.market.game.jbosak.model.db

import org.jetbrains.exposed.sql.Table

object Companies: Table() {
    val id = integer("id").primaryKey().autoIncrement()
    val ticker = text("ticker").index(isUnique = true)
    val name = text("name")
    val lei = text("lei").nullable()
    val cik = text("cik")
    val externalId = (text("external_api_id").index(isUnique = true))
}