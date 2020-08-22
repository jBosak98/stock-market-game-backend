package com.ktor.stock.market.game.jbosak.repository

import arrow.core.toOption
import com.ktor.stock.market.game.jbosak.model.Share
import com.ktor.stock.market.game.jbosak.model.Transaction
import com.ktor.stock.market.game.jbosak.model.TransactionType
import com.ktor.stock.market.game.jbosak.model.db.Transactions
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime

object TransactionRepository {
    fun insert(playerId:Int, companyId:Int, price:Float, quantity:Int, type:TransactionType) = transaction {
        Transactions.insert {
            it[Transactions.playerId] = playerId
            it[Transactions.companyId] = companyId
            it[Transactions.pricePerShare] = price
            it[Transactions.quantity] = quantity
            it[Transactions.createdAt] = DateTime.now()
            it[Transactions.type] = type
        }
    }
    private fun evaluateTransaction(transaction:Transaction,share: Share?): Share {
        if(share.toOption().isEmpty()) return Share(transaction.companyId,transaction.quantity)
        if(transaction.companyId != share!!.companyId) return share

        val amount = when (transaction.type) {
            TransactionType.PURCHASE -> share.amount + transaction.quantity
            TransactionType.DISPOSAL -> share.amount - transaction.quantity
        }
        return Share(transaction.companyId, amount)
    }
    fun getShares(playerId:Int) = transaction {
        Transactions
            .select { Transactions.playerId eq playerId }
            .map(ResultRow::toTransaction)
            .fold(emptyArray<Share>()) {shares,transaction ->
                val oldShares = shares.find { it.companyId == transaction.companyId }
                shares
                    .filter { it.companyId != transaction.companyId }
                    .toTypedArray() + evaluateTransaction(transaction, oldShares)
            }
    }
}