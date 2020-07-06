package com.ktor.stock.market.game.jbosak.graphQL

import arrow.core.valueOr
import com.ktor.stock.market.game.jbosak.repository.UserRepository
import com.ktor.stock.market.game.jbosak.model.Context
import com.ktor.stock.market.game.jbosak.model.CredentialWrapper
import com.ktor.stock.market.game.jbosak.model.RegistrationWrapper
import com.ktor.stock.market.game.jbosak.service.AuthService
import com.ktor.stock.market.game.jbosak.utils.convertToObject
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

fun TypeRuntimeWiring.Builder.userMutationResolvers() =
    this
        .dataFetcher("register") { env ->
            val user = convertToObject(env.arguments, RegistrationWrapper::class.java)!!.user
            if(UserRepository.doesUserExist(user.email))
                throw ClientGraphQLException("user exists")

            AuthService.register(user)
        }

fun TypeRuntimeWiring.Builder.userQueryResolvers() =
    this
        .dataFetcher("me") { env ->
            val authContext = env.getContext<Context>()

            authContext.user
        }
        .dataFetcher("login") { env ->
            val credentials = convertToObject(env.arguments, CredentialWrapper::class.java)!!.user
            val login = AuthService.login(credentials)
            login.valueOr {error -> throw error }
        }