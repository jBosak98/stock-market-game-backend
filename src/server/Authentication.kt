package com.ktor.stock.market.game.jbosak.server

import com.ktor.stock.market.game.jbosak.repository.UserRepository
import com.ktor.stock.market.game.jbosak.secretKey
import io.ktor.auth.Authentication
import io.ktor.auth.jwt.jwt

fun Authentication.Configuration.setup(secretKey: String) {
    JwtConfig.secret = secretKey
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