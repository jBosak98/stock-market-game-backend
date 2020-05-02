package com.ktor.stock.market.game.jbosak.route

import com.ktor.stock.market.game.jbosak.JwtConfig
import com.ktor.stock.market.game.jbosak.UserRepository
import com.ktor.stock.market.game.jbosak.model.UserDTO
import io.ktor.application.call
import io.ktor.auth.UserPasswordCredential
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.post
import io.ktor.routing.route

fun Route.authRoute() {

    route("/"){
        post ("login"){
            val credential = call.receive<UserPasswordCredential>()
            println(credential)
            val user = UserRepository.findUserByCredentials(credential)
            if(user === null) return@post  call.respond(HttpStatusCode.Unauthorized)
            val token = JwtConfig.makeToken(user)
            call.respond(HttpStatusCode.Created, token)
        }
        post("register") {
            val userDto = call.receive<UserDTO>()
            if(UserRepository.doesUserExist(userDto.email))
                call.respond(HttpStatusCode.BadRequest)
            else {
                UserRepository.insert(userDto)
                call.respond(HttpStatusCode.Created)
            }

        }
    }
}