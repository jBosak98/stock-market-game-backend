package com.ktor.stock.market.game.jbosak.model.graphql

data class AssetsGraphQL(val money:Float, val shares:List<ShareGraphQL>, val accountValue:Float)
