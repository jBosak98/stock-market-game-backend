package com.ktor.stock.market.game.jbosak.utils

import kotlin.math.roundToInt

fun Float.toPrice() = (this * 100)
    .roundToInt()
    .toFloat() / 100
