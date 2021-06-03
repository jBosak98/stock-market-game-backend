package com.ktor.stock.market.game.jbosak.model.graphql

data class UserGraphQL(
    val id:Int,
    val email:String,
    val token:String?,
    val assets:AssetsGraphQL?
)
