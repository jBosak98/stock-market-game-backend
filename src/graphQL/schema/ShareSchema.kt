package com.ktor.stock.market.game.jbosak.graphQL.schema

import arrow.core.getOrElse
import arrow.core.toOption
import com.ktor.stock.market.game.jbosak.graphQL.ClientGraphQLException
import com.ktor.stock.market.game.jbosak.graphQL.dataLoadersConfig.dataloaderResolver
import com.ktor.stock.market.game.jbosak.graphQL.dataLoadersConfig.resolve
import com.ktor.stock.market.game.jbosak.model.Context
import com.ktor.stock.market.game.jbosak.model.TransactionType
import com.ktor.stock.market.game.jbosak.model.graphql.BuyShareInput
import com.ktor.stock.market.game.jbosak.model.graphql.CompanyGraphQL
import com.ktor.stock.market.game.jbosak.model.graphql.ShareGraphQL
import com.ktor.stock.market.game.jbosak.model.toUserGraphQL
import com.ktor.stock.market.game.jbosak.repository.CompanyRepository
import com.ktor.stock.market.game.jbosak.repository.PlayerRepository
import com.ktor.stock.market.game.jbosak.repository.PlayerRepository.updatePlayerMoney
import com.ktor.stock.market.game.jbosak.repository.QuoteRepository
import com.ktor.stock.market.game.jbosak.repository.TransactionRepository
import com.ktor.stock.market.game.jbosak.utils.convertToObject
import com.ktor.stock.market.game.jbosak.utils.toPrice
import graphql.schema.AsyncDataFetcher.async
import graphql.schema.idl.TypeRuntimeWiring

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

        val price = QuoteRepository
            .findQuote(company.ticker)
            .getOrElse { throw ClientGraphQLException("Quote not found") }
            .currentPrice
            ?.toPrice()
            .toOption()
            .getOrElse { throw ClientGraphQLException("Price not found") }

        val updatedMoney = player.money - price * input.amount

        if(updatedMoney < 0) throw ClientGraphQLException("Player does not have enough money")

        updatePlayerMoney(player.id, updatedMoney)
        TransactionRepository.insert(
            playerId = player.id,
            companyId = company.id,
            price = price,
            quantity = input.amount,
            type = TransactionType.PURCHASE
        )
        val evalCompany = dataloaderResolver(env)
            .resolve<CompanyGraphQL>("company")
        val assets = player.assets.map {
            ShareGraphQL(it.companyId, evalCompany(it.companyId), it.amount)
        }
        user.toUserGraphQL(player, assets)
    })

