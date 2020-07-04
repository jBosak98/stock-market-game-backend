package com.ktor.stock.market.game.jbosak.graphQL

import com.google.gson.Gson
import com.ktor.stock.market.game.jbosak.UserRepository
import com.ktor.stock.market.game.jbosak.model.Context
import com.ktor.stock.market.game.jbosak.model.RegistrationWrapper
import com.ktor.stock.market.game.jbosak.service.AuthService
import graphql.schema.idl.TypeRuntimeWiring

fun <T> convertToObject(args:Map<String, Any>, type:Class<T>): T? {
    val gson = Gson()
    val jsonTree = gson.toJsonTree(args)
    return gson.fromJson(jsonTree, type)
}
fun getUserSchema() =
    """
        type User {
            id: String
            email: String
        }
        
        input UserRegisterInput {
            email: String!
            password: String!
        }
    """

fun TypeRuntimeWiring.Builder.userMutationResolvers() =
    this.dataFetcher("register") { env ->
        val user = convertToObject(env.arguments, RegistrationWrapper::class.java)!!.user
        if(UserRepository.doesUserExist(user.email))
            throw Exception("user exists")//TODO: make graphQL exception

        AuthService.register(user)

    }

fun TypeRuntimeWiring.Builder.userQueryResolvers() =
    this.dataFetcher("me") { env ->
        val authContext = env.getContext<Context>()
        authContext.user
    }