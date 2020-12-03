package com.ktor.stock.market.game.jbosak.model.db

import com.ktor.stock.market.game.jbosak.model.TransactionType
import org.jetbrains.exposed.sql.Table


object Transactions: Table("Transactions") {
    val id = integer("id").uniqueIndex().primaryKey().autoIncrement()
    val playerId = integer("playerId") references Players.id
    val companyId = integer("companyId") references Companies.id
    val quoteId = integer("quoteId") references Quotes.id
//    val pricePerShare = float("price_per_share")
    val quantity = integer("quantity")
    val createdAt = datetime("createdAt")
    val type = enumeration("type", TransactionType::class)
}