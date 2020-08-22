package com.ktor.stock.market.game.jbosak.model.db

import com.ktor.stock.market.game.jbosak.model.TransactionType
import org.jetbrains.exposed.sql.Table


object Transactions: Table() {
    val id = integer("id").uniqueIndex().primaryKey().autoIncrement()
    val playerId = integer("player_id") references Players.id
    val companyId = integer("company_id") references Companies.id
    val pricePerShare = float("price_per_share")
    val quantity = integer("quantity")
    val createdAt = date("created_at")
    val type = enumeration("type", TransactionType::class)
}