package com.ktor.stock.market.game.jbosak.server


import com.ktor.stock.market.game.jbosak.graphQL.dataLoadersConfig.generateDataLoaders
import com.ktor.stock.market.game.jbosak.graphQL.formatErrorGraphQLError
import com.ktor.stock.market.game.jbosak.graphQL.schema.getSchema
import com.ktor.stock.market.game.jbosak.model.Context
import com.ktor.stock.market.game.jbosak.model.User
import com.ktor.stock.market.game.jbosak.route.authRoute
import graphql.ExecutionInput
import graphql.GraphQL
import graphql.execution.AsyncExecutionStrategy
import graphql.execution.AsyncSerialExecutionStrategy
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.auth.authentication
import io.ktor.config.*
import io.ktor.http.ContentType
import io.ktor.response.respondText
import io.ktor.routing.Routing
import io.ktor.routing.get
import ktor.graphql.Config
import ktor.graphql.fromRequest
import ktor.graphql.graphQL
import rabbit

fun Routing.setup(config:ApplicationConfig) {
    authRoute()
    rabbit(config)
    authenticate(optional = true) {
        get("/") {
            call.respondText("HELLO WORLD!", contentType = ContentType.Text.Plain)
        }

        get("/hello") {
            call.respondText("HELLO WORLD!", contentType = ContentType.Text.Plain)
        }
        val graphql = GraphQL
            .newGraphQL(getSchema())
            .queryExecutionStrategy(AsyncExecutionStrategy())
            .mutationExecutionStrategy(AsyncSerialExecutionStrategy())
            .build()

        graphQL("/graphql", getSchema()) { request ->
            Config(
                formatError = formatErrorGraphQLError,
                showExplorer = true,
                executeRequest = {
                    val input = ExecutionInput
                        .newExecutionInput()
                        .fromRequest(request)
                        .dataLoaderRegistry(generateDataLoaders())
                        .context(getContext(call))
                    graphql.execute(input)
                }
            )

        }
    }
}



fun getContext(call: ApplicationCall): Context = Context(call.authentication.principal as User?)

