package com.ktor.stock.market.game.jbosak.repository


import com.ktor.stock.market.game.jbosak.model.Company
import com.ktor.stock.market.game.jbosak.model.User
import com.ktor.stock.market.game.jbosak.model.db.Companies
import com.ktor.stock.market.game.jbosak.model.db.Users
import org.jetbrains.exposed.sql.ResultRow

fun ResultRow.toUser() = User(
    id = this[Users.id],
    email = this[Users.name],
    password = this[Users.password],
    token = ""

)

fun ResultRow.toCompany() = Company(
    id = this[Companies.id],
    ticker = this[Companies.ticker],
    name = this[Companies.name],
    lei = this[Companies.lei],
    cik = this[Companies.cik],
    externalId = this[Companies.externalId]
)