package com.ktor.stock.market.game.jbosak.repository

import com.ktor.stock.market.game.jbosak.model.TransactionType
import com.ktor.stock.market.game.jbosak.model.db.Transactions
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime

object TransactionRepository {
    fun insert(playerId:Int, companyId:Int, price:Int, quantity:Int, type:TransactionType) = transaction {
        Transactions.insert {
            it[Transactions.playerId] = playerId
            it[Transactions.companyId] = companyId
            it[Transactions.pricePerShare] = price
            it[Transactions.quantity] = quantity
            it[Transactions.createdAt] = DateTime.now()
            it[Transactions.type] = type
        }
    }
}