package com.ktor.stock.market.game.jbosak.graphQL

import graphql.ExceptionWhileDataFetching
import graphql.GraphQLError
import java.util.concurrent.CompletionException

val formatErrorGraphQLError: (GraphQLError.() -> Map<String, Any>) = {
    val clientMessage =
        if (this is ExceptionWhileDataFetching) {
            when (exception) {
                is CompletionException -> exception::cause.get()?.message ?: exception.message
                is ClientGraphQLException -> exception.message
                else -> "Internal server error"
            }
    } else {
        message
    }
    val result = toSpecification()
    result["message"] = clientMessage
    result
}