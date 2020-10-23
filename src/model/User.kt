package com.ktor.stock.market.game.jbosak.model

import com.ktor.stock.market.game.jbosak.model.graphql.AssetsGraphQL
import com.ktor.stock.market.game.jbosak.model.graphql.ShareGraphQL
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
fun User.toUserGraphQL(assets:AssetsGraphQL) = UserGraphQL(
    id = id,
    email = email,
    token = token,
    assets = assets
)