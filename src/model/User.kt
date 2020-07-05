package com.ktor.stock.market.game.jbosak.model

import com.ktor.stock.market.game.jbosak.JwtConfig
import io.ktor.auth.Principal

data class User (
    val id:Int,
    val email:String,
    val password:String,
    val token: String?
) : Principal

fun User.toUserDTO()
    = UserDTO(
        id = id,
        email = email,
        token = JwtConfig.makeToken(this)
    )
