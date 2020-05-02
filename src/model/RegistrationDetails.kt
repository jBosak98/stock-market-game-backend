package com.ktor.stock.market.game.jbosak.model

data class RegistrationDetails(
    val email: String,
    val password: String
)

class RegistrationWrapper(val user: RegistrationDetails)