package com.ktor.stock.market.game.jbosak.model.graphql

data class BuyShareInput(val ticker:String, val amount:Int, val price:Float?)
