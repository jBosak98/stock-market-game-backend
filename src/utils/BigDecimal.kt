package com.ktor.stock.market.game.jbosak.utils

import java.math.BigDecimal

fun BigDecimal?.toPriceInt() = this?.times(100.toBigDecimal())?.toInt()
