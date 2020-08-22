package com.ktor.stock.market.game.jbosak.model

import com.ktor.stock.market.game.jbosak.model.graphql.AssetsGraphQL
import com.ktor.stock.market.game.jbosak.model.graphql.UserGraphQL
import com.ktor.stock.market.game.jbosak.server.JwtConfig
import io.ktor.auth.Principal

data class User(
    val id: Int,
    val email: String,
    val password: String,
    val token: String?
) : Principal

fun User.toUserDTO() = UserDTO(
    id = id,
    email = email,
    token = JwtConfig.makeToken(this)
)
fun User.toUserGraphQL(player: Player) = UserGraphQL(
    id = id,
    email = email,
    token = token,
    assets = AssetsGraphQL(player.money)
)