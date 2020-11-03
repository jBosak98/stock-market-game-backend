package com.ktor.stock.market.game.jbosak.server

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.ktor.stock.market.game.jbosak.model.User
import io.ktor.application.Application
import java.util.*

object JwtConfig {
    lateinit var secret:String
    private const val issuer = "ktor.io"
    private const val validityInMs = 36_000_00 * 10 // 10 hours
    private val algorithm = Algorithm.HMAC512(secret)

    val verifier: JWTVerifier =
        JWT
            .require(algorithm)
            .withIssuer(issuer)
            .build()

    private fun getExpirationTime() =
        Date(System.currentTimeMillis() + validityInMs)

    fun makeToken(user: User):String =
        JWT
            .create()
            .withSubject("auth")
            .withIssuer(issuer)
            .withClaim("id",user.id)
            .withExpiresAt(getExpirationTime())
            .sign(algorithm)
}