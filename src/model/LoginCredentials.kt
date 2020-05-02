package com.ktor.stock.market.game.jbosak.model

data class LoginCredentials(
    val email: String,
    val password: String
)

class CredentialWrapper(val user: LoginCredentials)