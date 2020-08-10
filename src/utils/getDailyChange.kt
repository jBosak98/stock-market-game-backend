package com.ktor.stock.market.game.jbosak.utils

import arrow.core.toOption
import com.finnhub.api.models.Quote

fun getDailyChange(quote: Quote): Float? = when {
    quote.c.toOption().isEmpty() -> null
    quote.pc.toOption().isEmpty() -> null
    else -> (quote.c!! - quote.pc!!) / quote.pc!! * 100
}