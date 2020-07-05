package com.ktor.stock.market.game.jbosak.server


import com.ktor.stock.market.game.jbosak.graphQL.formatErrorGraphQLError
import com.ktor.stock.market.game.jbosak.graphQL.getSchema
import com.ktor.stock.market.game.jbosak.model.Context
import com.ktor.stock.market.game.jbosak.model.User
import com.ktor.stock.market.game.jbosak.route.authRoute
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.auth.authentication
import io.ktor.http.ContentType
import io.ktor.response.respondText
import io.ktor.routing.Routing
import io.ktor.routing.get
import ktor.graphql.config
import ktor.graphql.graphQL

fun Routing.setup(){
    authRoute()
    authenticate(optional = true) {
        get("/") {
            call.respondText("HELLO WORLD!", contentType = ContentType.Text.Plain)
        }

        get("/hello") {
            call.respondText("HELLO WORLD!", contentType = ContentType.Text.Plain)
        }
        graphQL("/graphql", getSchema()) {
            config {
                context = getContext(call)
                graphiql = true
                formatError = formatErrorGraphQLError
            }
        }
    }
}

fun getContext(call: ApplicationCall): Context
        = Context(call.authentication.principal as User?)

