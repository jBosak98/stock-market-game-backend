package com.ktor.stock.market.game.jbosak

import com.ktor.stock.market.game.jbosak.server.initDB
import com.ktor.stock.market.game.jbosak.server.initExternalApi
import com.ktor.stock.market.game.jbosak.server.setup
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.auth.Authentication
import io.ktor.features.CORS
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.features.ForwardedHeaderSupport
import io.ktor.gson.gson
import io.ktor.routing.Routing
import io.ktor.util.KtorExperimentalAPI


fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@KtorExperimentalAPI
@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    initDB()
    initExternalApi(finnhubKey)

    install(CORS) { setup() }

    install(DefaultHeaders)

    install(ForwardedHeaderSupport)
    install(Authentication) { setup() }

    install(ContentNegotiation) { gson() }

    install(Routing) { setup() }
}
@KtorExperimentalAPI
val Application.finnhubKey get() = environment.config.property("ktor.deployment.finnhubKey").getString()
