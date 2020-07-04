package com.ktor.stock.market.game.jbosak.server


import com.ktor.stock.market.game.jbosak.route.authRoute
import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.http.ContentType
import io.ktor.response.respondText
import io.ktor.routing.Routing
import io.ktor.routing.get

fun Routing.setup(){
    authRoute()
    authenticate {
        get("/") {
            call.respondText("HELLO WORLD!", contentType = ContentType.Text.Plain)
        }
    }


    
}