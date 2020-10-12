package com.ktor.stock.market.game.jbosak.utils

import org.joda.time.DateTime

fun DateTime.isInLast5Minutes() =
    DateTime
        .now()
        .minusMinutes(5)
        .let { fiveMinAgo -> this.isAfter(fiveMinAgo) }

fun DateTime.toUnixTime() = this.millis / 1000