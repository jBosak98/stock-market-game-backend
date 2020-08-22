package com.ktor.stock.market.game.jbosak.model

import org.joda.time.DateTime

data class Player(
    val id:Int,
    val money:Float,
    val userId:Int,
    val startedAt:DateTime,
    val removedAt:DateTime?,
    val assets:List<Share>
)