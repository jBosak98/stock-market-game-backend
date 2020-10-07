package com.ktor.stock.market.game.jbosak.graphQL.schema

import arrow.core.getOrElse
import com.ktor.stock.market.game.jbosak.graphQL.ClientGraphQLException
import com.ktor.stock.market.game.jbosak.model.Context
import com.ktor.stock.market.game.jbosak.repository.PlayerRepository.findPlayer
import com.ktor.stock.market.game.jbosak.repository.TransactionRepository
import graphql.schema.idl.TypeRuntimeWiring

fun getTransactionSchema() =
    """
        type Transaction {
            id:Int
            playerId: Int
            companyId: Int
            pricePerShare: Float
            quantity: Int
            createdAt: String
            type: String!
        }
    """

fun TypeRuntimeWiring.Builder.transactionQueryResolvers():TypeRuntimeWiring.Builder =
    this.dataFetcher("getTransactions"){env ->
        val user = env
            .getContext<Context>()
            .user
            ?: throw ClientGraphQLException("Unauthorized")
        val player = findPlayer(user.id)
            .getOrElse { throw ClientGraphQLException("Player not found") }

        val transactions =
            TransactionRepository.findPlayerTransactions(player.id)

        transactions
    }