package com.ktor.stock.market.game.jbosak.model.db

import org.jetbrains.exposed.sql.Table

object Players: Table(){
    val id = integer("id").uniqueIndex().primaryKey().autoIncrement()
    val money = float("money")
    val userId = integer("userId") references Users.id
    val startedAt = date("startedAt")
    val removedAt = date("removedAt").nullable()
}