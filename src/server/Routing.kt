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
    //        get("/json/jackson") {
//            call.respond(mapOf("hello" to "world"))
//        }
//
//        get("/users") {
//            call.respond(userController.getAll())
//        }
//
//        post("/users") {
//            val userDto = call.receive<UserDTO>()
//            userController.insert(userDto)
//            call.respond(HttpStatusCode.Created)
//        }
//
//        put("/users/{id}") {
//            val id: Int = call.parameters["id"] as Int
//            val userDTO = call.receive<UserDTO>()
//            userController.update(userDTO, id)
//            call.respond(HttpStatusCode.OK)
//        }
//
//        delete("/users/{id}") {
//            val id: Int = call.parameters["id"] as Int
//            userController.delete(id)
//            call.respond(HttpStatusCode.OK)
//        }
}