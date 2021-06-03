package com.ktor.stock.market.game.jbosak.graphQL.schema

import arrow.core.getOrElse
import arrow.core.toOption
import com.ktor.stock.market.game.jbosak.graphQL.ClientGraphQLException
import com.ktor.stock.market.game.jbosak.graphQL.dataLoadersConfig.dataloaderResolver
import com.ktor.stock.market.game.jbosak.graphQL.dataLoadersConfig.resolve
import com.ktor.stock.market.game.jbosak.model.Context
import com.ktor.stock.market.game.jbosak.model.TransactionType
import com.ktor.stock.market.game.jbosak.model.graphql.AssetsGraphQL
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
            price: Float
        }
    """

fun TypeRuntimeWiring.Builder.shareMutationResolvers(): TypeRuntimeWiring.Builder =
    this.dataFetcher("buyShare", async { env ->
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

        val findPrice = {
            QuoteRepository
                .findQuote(company.ticker)
                .getOrElse { throw ClientGraphQLException("Quote not found") }
                .currentPrice
                .toOption()
                .getOrElse { throw ClientGraphQLException("Price not found") }
                .toPrice()
        }
        val price = input
            .price
            .toOption()
            .getOrElse(findPrice)


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

        val assetsQL =
            dataloaderResolver(env)
                .resolve<AssetsGraphQL>("assets")(user.id)

        assetsQL?.let { user.toUserGraphQL(it) }
    })
        .dataFetcher("sellShare", async {env ->
            val input = convertToObject(env.arguments, BuyShareInput::class.java)!!
            val user = env
                .getContext<Context>()
                .user
                .toOption()
                .getOrElse { throw ClientGraphQLException("Not authorized") }

            val company = CompanyRepository
                .findCompany(ticker = input.ticker)
                .getOrElse { throw ClientGraphQLException("Wrong ticker") }

            val player = PlayerRepository
                .findPlayer(user.id)
                .getOrElse { throw ClientGraphQLException("Player not found") }


            val findPrice = {
                QuoteRepository
                    .findQuote(company.ticker)
                    .getOrElse { throw ClientGraphQLException("Quote not found") }
                    .currentPrice
                    .toOption()
                    .getOrElse { throw ClientGraphQLException("Price not found") }
                    .toPrice()
            }
            val price = input
                .price
                .toOption()
                .getOrElse(findPrice)


            val updatedMoney = player.money + price * input.amount
            updatePlayerMoney(player.id, updatedMoney)
            TransactionRepository.insert(
                playerId = player.id,
                companyId = company.id,
                price = price,
                quantity = input.amount,
                type = TransactionType.DISPOSAL
            )

            val assetsQL =
                dataloaderResolver(env)
                    .resolve<AssetsGraphQL>("assets")(user.id)


            assetsQL?.let { user.toUserGraphQL(it) }
        })

