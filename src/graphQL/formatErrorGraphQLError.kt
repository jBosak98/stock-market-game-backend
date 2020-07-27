package com.ktor.stock.market.game.jbosak.graphQL

import graphql.ExceptionWhileDataFetching
import graphql.GraphQLError

val formatErrorGraphQLError: (GraphQLError.() -> Map<String, Any>) = {
    val clientMessage = if (this is ExceptionWhileDataFetching) {
        if (exception is ClientGraphQLException) exception.message
        else "Internal server error"
    } else {
        message
    }
    val result = toSpecification()
    result["message"] = clientMessage
    result
}