package com.ktor.stock.market.game.jbosak.graphQL.schema

import arrow.core.getOrElse
import arrow.core.valueOr
import com.ktor.stock.market.game.jbosak.graphQL.ClientGraphQLException
import com.ktor.stock.market.game.jbosak.model.Context
import com.ktor.stock.market.game.jbosak.model.CredentialWrapper
import com.ktor.stock.market.game.jbosak.model.RegistrationWrapper
import com.ktor.stock.market.game.jbosak.model.toUserGraphQL
import com.ktor.stock.market.game.jbosak.repository.PlayerRepository
import com.ktor.stock.market.game.jbosak.repository.UserRepository
import com.ktor.stock.market.game.jbosak.service.AuthService
import com.ktor.stock.market.game.jbosak.utils.convertToObject
import graphql.schema.AsyncDataFetcher.async
import graphql.schema.idl.TypeRuntimeWiring


fun getUserSchema() =
    """
        type User {
            id: String!
            email: String!
            token: String
            assets: Assets
        }
        type Assets {
            money:Int
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
            val player = PlayerRepository
                .createUser(createdUser.id)
                ?: throw ClientGraphQLException("Player not found")

            createdUser.toUserGraphQL(player)
        })

fun TypeRuntimeWiring.Builder.userQueryResolvers(): TypeRuntimeWiring.Builder =
    this
        .dataFetcher("me") { env ->
            val user = env
                .getContext<Context>()
                .user
                ?: throw ClientGraphQLException("Unauthorized")

            val player = PlayerRepository
                .findPlayer(user.id)
                .getOrElse { PlayerRepository.createUser(user.id) }
                ?: throw ClientGraphQLException("Player not found")

            user.toUserGraphQL(player)
        }
        .dataFetcher("login") { env ->
            val credentials = convertToObject(env.arguments, CredentialWrapper::class.java)!!.user
            val login = AuthService.login(credentials)

            val user = login.valueOr { error -> throw error }
            val player = PlayerRepository
                .findPlayer(user.id)
                .getOrElse { throw ClientGraphQLException("Player not found") }
            user.toUserGraphQL(player)

        }