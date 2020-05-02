package com.ktor.stock.market.game.jbosak

import com.ktor.stock.market.game.jbosak.server.initDB
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


fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {

    initDB()

    install(CORS) { setup() }

    install(DefaultHeaders)

    install(ForwardedHeaderSupport)
    install(Authentication) { setup() }

    install(ContentNegotiation) { gson() }

    install(Routing) { setup() }
}

