package com.ktor.stock.market.game.jbosak.utils

import org.joda.time.DateTime
import org.joda.time.Duration
import org.joda.time.format.DateTimeFormat
import kotlin.math.roundToInt

fun DateTime.isInLast5Minutes() =
    DateTime
        .now()
        .minusMinutes(5)
        .let { fiveMinAgo -> this.isAfter(fiveMinAgo) }

fun DateTime.toUnixTime() = this.millis / 1000

fun isValidDateTime(time:String) = try {
    DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parseDateTime(time)
    true
}catch (e:Exception){
    false
}

fun DateTime.roundDown(d: Duration): DateTime =
    this.minus(this.millis - (this.millis.toDouble() / d.millis).roundToInt() * d.millis)
