package com.ktor.stock.market.game.jbosak.model

data class Company(
    val id:Int,
    val ticker:String,
    val name: String,
    val lei: String?,
    val cik:String,
    val externalId:String
)