package com.ktor.stock.market.game.jbosak.graphQL.schema

import arrow.core.getOrElse
import arrow.core.toOption
import arrow.core.valueOr
import com.ktor.stock.market.game.jbosak.graphQL.ClientGraphQLException
import com.ktor.stock.market.game.jbosak.graphQL.dataLoadersConfig.DataLoaderKey
import com.ktor.stock.market.game.jbosak.graphQL.dataLoadersConfig.dataloaderResolver
import com.ktor.stock.market.game.jbosak.graphQL.dataLoadersConfig.resolve
import com.ktor.stock.market.game.jbosak.model.*
import com.ktor.stock.market.game.jbosak.model.graphql.AssetsGraphQL
import com.ktor.stock.market.game.jbosak.model.graphql.CompanyGraphQL
import com.ktor.stock.market.game.jbosak.model.graphql.ShareGraphQL
import com.ktor.stock.market.game.jbosak.repository.PlayerRepository
import com.ktor.stock.market.game.jbosak.repository.UserRepository
import com.ktor.stock.market.game.jbosak.service.AuthService
import com.ktor.stock.market.game.jbosak.service.getCompany
import com.ktor.stock.market.game.jbosak.service.getQuote
import com.ktor.stock.market.game.jbosak.utils.convertToObject
import graphql.schema.AsyncDataFetcher.async
import graphql.schema.idl.TypeRuntimeWiring
import org.dataloader.BatchLoader
import org.dataloader.DataLoader
import java.util.concurrent.CompletableFuture


fun getUserSchema() =
    """
        type User {
            id: String!
            email: String!
            token: String
            assets: Assets
        }
        type Assets {
            money:Float
            accountValue:Float
            shares:[Share]
        }
        type Share {
            companyId:Int!
            company:Company
            amount:Int
        }
        
        input UserRegisterInput {
            email: String!
            password: String!
        }
        input UserLoginInput {
            email: String!
            password: String!
        }
    """

fun TypeRuntimeWiring.Builder.userMutationResolvers(): TypeRuntimeWiring.Builder =
    this
        .dataFetcher("register", async { env ->
            val user = convertToObject(env.arguments, RegistrationWrapper::class.java)!!.user
            if (UserRepository.doesUserExist(user.email))
                throw ClientGraphQLException("user exists")

            val createdUser = AuthService.register(user).valueOr { throw it }

            val assetsQL =
                dataloaderResolver(env)
                    .resolve<AssetsGraphQL>("assets")(createdUser.id)

            assetsQL?.let { createdUser.toUserGraphQL(it) }
        })

fun TypeRuntimeWiring.Builder.userQueryResolvers(): TypeRuntimeWiring.Builder =
    this
        .dataFetcher("me") { env ->
            val user = env
                .getContext<Context>()
                .user
                ?: throw ClientGraphQLException("Unauthorized")

            val assetsQL =
                dataloaderResolver(env)
                    .resolve<AssetsGraphQL>("assets")(user.id)

            assetsQL?.let { user.toUserGraphQL(it) }
        }
        .dataFetcher("login") { env ->
            val credentials = convertToObject(env.arguments, CredentialWrapper::class.java)!!.user
            val login = AuthService.login(credentials)

            val user = login.valueOr { error -> throw error }

            val assetsQL =
                dataloaderResolver(env)
                    .resolve<AssetsGraphQL>("assets")(user.id)

            assetsQL?.let { user.toUserGraphQL(it) }

        }

