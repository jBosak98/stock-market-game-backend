package com.ktor.stock.market.game.jbosak.server

import com.ktor.stock.market.game.jbosak.JwtConfig
import com.ktor.stock.market.game.jbosak.UserRepository
import com.ktor.stock.market.game.jbosak.UserRepository.findUserById
import io.ktor.auth.Authentication
import io.ktor.auth.jwt.JWTPrincipal
import io.ktor.auth.jwt.jwt

fun Authentication.Configuration.setup(){

    jwt {
        verifier(JwtConfig.verifier)
        realm = "ktor.io"
        validate {
            val user = it.payload
                .getClaim("id")
                .asInt()
                ?.let(UserRepository::findUserById)

            if (user === null) null
            else JWTPrincipal(it.payload)
        }
    }
}