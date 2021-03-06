package com.ktor.stock.market.game.jbosak.model.graphql


data class ShareGraphQL(
    val companyId:Int,
    val company:CompanyGraphQL?,
    val amount:Int,
    val totalGain:Float?,
    val totalGainPercentage:Float?
)