package com.ktor.stock.market.game.jbosak.utils

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

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