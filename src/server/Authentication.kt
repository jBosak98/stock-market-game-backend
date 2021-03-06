package com.ktor.stock.market.game.jbosak.server

import com.ktor.stock.market.game.jbosak.globalSecretKey
import com.ktor.stock.market.game.jbosak.repository.UserRepository
import io.ktor.auth.Authentication
import io.ktor.auth.jwt.jwt

fun Authentication.Configuration.setup() {
    JwtConfig.secret = globalSecretKey
    jwt {
        verifier(JwtConfig.verifier)
        realm = "ktor.io"
        validate {
            val user = it.payload
                .getClaim("id")
                .asInt()
                ?.let(UserRepository::findUserById)
                ?.let { user ->
                    val token = JwtConfig.makeToken(user)
                    user.copy(token = token)
                }
            user
        }
    }
}