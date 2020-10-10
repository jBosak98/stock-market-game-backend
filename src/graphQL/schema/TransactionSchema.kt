package com.ktor.stock.market.game.jbosak.graphQL.schema

import arrow.core.getOrElse
import com.ktor.stock.market.game.jbosak.graphQL.ClientGraphQLException
import com.ktor.stock.market.game.jbosak.graphQL.dataLoadersConfig.dataloaderResolver
import com.ktor.stock.market.game.jbosak.graphQL.dataLoadersConfig.resolve
import com.ktor.stock.market.game.jbosak.model.Context
import com.ktor.stock.market.game.jbosak.model.graphql.CompanyGraphQL
import com.ktor.stock.market.game.jbosak.model.toGraphQL
import com.ktor.stock.market.game.jbosak.repository.PlayerRepository.findPlayer
import com.ktor.stock.market.game.jbosak.repository.TransactionRepository
import graphql.schema.idl.TypeRuntimeWiring

fun getTransactionSchema() =
    """
        type Transaction {
            id:Int!
            playerId: Int!
            companyId: Int!
            company: Company
            pricePerShare: Float
            quantity: Int
            createdAt: String
            type: String!
        }
    """

fun TypeRuntimeWiring.Builder.transactionQueryResolvers():TypeRuntimeWiring.Builder =
    this.dataFetcher("getTransactions"){ env ->
        val user = env
            .getContext<Context>()
            .user
            ?: throw ClientGraphQLException("Unauthorized")
        val player = findPlayer(user.id)
            .getOrElse { throw ClientGraphQLException("Player not found") }
        val resolvers =
            dataloaderResolver(env = env,methodName = "getTransactions")
        val evalCompany = resolvers.resolve<CompanyGraphQL>("company")

        val transactions =
            TransactionRepository
                .findPlayerTransactions(player.id)
                .map{ it.toGraphQL(evalCompany(it.companyId)) }

        transactions
    }