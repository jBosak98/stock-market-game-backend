package com.ktor.stock.market.game.jbosak.repository

import arrow.core.getOrElse
import arrow.core.toOption
import com.ktor.stock.market.game.jbosak.model.Player
import com.ktor.stock.market.game.jbosak.model.db.Players
import com.ktor.stock.market.game.jbosak.utils.MONEY_TO_START
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import org.joda.time.DateTime

object PlayerRepository {
    private fun insert(userId:Int) = transaction {
        Players.insert {
            it[money] = MONEY_TO_START
            it[Players.userId] = userId
            it[startedAt] = DateTime.now()
        }
    }

    fun updatePlayerMoney(playerId:Int, money:Float) = transaction {
        Players.update(
            where = { Players.id eq playerId },
            limit = null,
            body = { it[Players.money] = money }
        )
    }
    fun createUser(userId: Int): Player? {
        insert(userId)
        return findPlayer(userId).getOrElse { null }
    }

    fun findPlayer(userId:Int) = transaction {
        Players
            .select { Players.userId eq userId }
            .singleOrNull()
            ?.let {
                val id = it[Players.id]
                val assets = TransactionRepository.getShares(id).toList()
                it.toPlayer(assets)
            }
            .toOption()
    }
}