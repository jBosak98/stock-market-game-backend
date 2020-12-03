package com.ktor.stock.market.game.jbosak.model.db

import org.jetbrains.exposed.sql.Table

object Users : Table("Users") {
    val id = integer("id").primaryKey().autoIncrement()
    val name = text("name")
    val password = text("password")
}