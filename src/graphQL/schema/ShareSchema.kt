package com.ktor.stock.market.game.jbosak.graphQL.schema

import arrow.core.getOrElse
import arrow.core.toOption
import com.ktor.stock.market.game.jbosak.graphQL.ClientGraphQLException
import com.ktor.stock.market.game.jbosak.model.Context
import com.ktor.stock.market.game.jbosak.model.TransactionType
import com.ktor.stock.market.game.jbosak.model.toUserGraphQL
import com.ktor.stock.market.game.jbosak.repository.CompanyRepository
import com.ktor.stock.market.game.jbosak.repository.PlayerRepository
import com.ktor.stock.market.game.jbosak.repository.QuoteRepository
import com.ktor.stock.market.game.jbosak.repository.TransactionRepository
import com.ktor.stock.market.game.jbosak.utils.convertToObject
import graphql.schema.AsyncDataFetcher.async
import graphql.schema.idl.TypeRuntimeWiring
import kotlin.math.roundToInt

fun getShareShema() =
    """
       input BuyShareInput {
            ticker:String!
            amount: Int!
        }
    """

fun TypeRuntimeWiring.Builder.shareMutationResolvers() =
    this.dataFetcher("buyShare", async { env ->
        println("inspect: ${env.arguments}")
        val input = convertToObject(env.arguments, BuyShareInput::class.java)!!
        val user = env
            .getContext<Context>()
            .user
            .toOption()
            .getOrElse { throw ClientGraphQLException("Not authorized") }
        val player = PlayerRepository
            .findPlayer(user.id)
            .getOrElse { throw ClientGraphQLException("Player not found") }

        val company = CompanyRepository
            .findCompany(ticker = input.ticker)
            .getOrElse { throw ClientGraphQLException("Wrong ticker") }
        val quote = QuoteRepository
            .findQuote(company.ticker)
            .getOrElse { throw ClientGraphQLException("Price not found") }
        val price = quote.currentPrice?.roundToInt()?:0 //TODO:change everthing to int
        //TODO - update amount of money
        TransactionRepository.insert(
            playerId = player.id,
            companyId = company.id,
            price = price,
            quantity = input.amount,
            type = TransactionType.PURCHASE
        )
        user.toUserGraphQL(player)
    })
data class BuyShareInput(val ticker:String, val amount:Int)
