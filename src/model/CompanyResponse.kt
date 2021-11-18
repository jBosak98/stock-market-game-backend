package com.ktor.stock.market.game.jbosak.model

data class CompanyResponse(
    val sector:String,
    val industry: String,
    val employees: Int,
    val business_summary: String,
    val city: String,
    val website: String,
    val logo_url: String
)