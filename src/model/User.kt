package com.ktor.stock.market.game.jbosak.model

import java.security.Principal

data class User (
    val id:Int,
    val email:String,
    val password:String,
    val token: String
): Principal {
    override fun getName(): String = email
}

fun User.toUserDTO()
    = UserDTO(
        id = id,
        email = email,
        token = token
    )
