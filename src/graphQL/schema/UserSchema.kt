package com.ktor.stock.market.game.jbosak.graphQL.schema

import arrow.core.valueOr
import com.ktor.stock.market.game.jbosak.graphQL.ClientGraphQLException
import com.ktor.stock.market.game.jbosak.model.Context
import com.ktor.stock.market.game.jbosak.model.CredentialWrapper
import com.ktor.stock.market.game.jbosak.model.RegistrationWrapper
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

            val response = AuthService.register(user).valueOr { throw it }
            response
        })

fun TypeRuntimeWiring.Builder.userQueryResolvers(): TypeRuntimeWiring.Builder =
    this
        .dataFetcher("me") { env ->
            val authContext = env.getContext<Context>()

            authContext.user
        }
        .dataFetcher("login") { env ->
            val credentials = convertToObject(env.arguments, CredentialWrapper::class.java)!!.user
            val login = AuthService.login(credentials)
            login.valueOr { error -> throw error }
        }