package com.ktor.stock.market.game.jbosak.route

import arrow.core.valueOr
import com.ktor.stock.market.game.jbosak.repository.UserRepository
import com.ktor.stock.market.game.jbosak.model.CredentialWrapper
import com.ktor.stock.market.game.jbosak.model.RegistrationWrapper
import com.ktor.stock.market.game.jbosak.service.AuthService
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.post
import io.ktor.routing.route

fun Route.authRoute() {

    route("/auth"){
        post ("/login"){
            val credential = call.receive<CredentialWrapper>().user
            val user = AuthService.login(credential)

            call.respond(HttpStatusCode.Created, user.valueOr { throw it })
        }
        post("/register") {
            val details = call.receive<RegistrationWrapper>().user
            if(UserRepository.doesUserExist(details.email))
                call.respond(HttpStatusCode.BadRequest)
            else {
                AuthService.register(details)
                call.respond(HttpStatusCode.Created)
            }

        }
    }
}