package com.ktor.stock.market.game.jbosak.model.db

import org.jetbrains.exposed.sql.Table

object Players: Table(){
    val id = integer("id").uniqueIndex().primaryKey().autoIncrement()
    val money = integer("money")
    val userId = integer("user_id") references Users.id
    val startedAt = date("started_at")
    val removedAt = date("removed_at").nullable()
}