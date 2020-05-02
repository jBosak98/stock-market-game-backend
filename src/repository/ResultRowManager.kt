package com.ktor.stock.market.game.jbosak.repository

import com.ktor.stock.market.game.jbosak.model.User
import com.ktor.stock.market.game.jbosak.model.Users
import org.jetbrains.exposed.sql.ResultRow

fun ResultRow.toUser() = User(
    id = this[Users.id],
    email = this[Users.name],
    password = this[Users.password],
    token = ""

)