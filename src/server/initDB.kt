package com.ktor.stock.market.game.jbosak.server

import com.ktor.stock.market.game.jbosak.model.db.Companies
import com.ktor.stock.market.game.jbosak.model.db.Users
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

fun initDB() {
    val config = HikariConfig("/hikari.properties")
    config.schema = "public"
    val ds = HikariDataSource(config)
    Database.connect(ds)
    transaction {
        SchemaUtils.create(Users)
        SchemaUtils.create(Companies)
    }
}