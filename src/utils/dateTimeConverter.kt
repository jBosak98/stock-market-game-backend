package com.ktor.stock.market.game.jbosak.utils

import com.google.gson.JsonDeserializer
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormatter
import org.joda.time.format.ISODateTimeFormat
import java.util.*

val dateTimeConverter = JsonDeserializer<DateTime?> { json, typeOfT, context ->
    val fmt: DateTimeFormatter = ISODateTimeFormat.dateTimeNoMillis()
    try {
        fmt.parseDateTime(json!!.asString)
    } catch (e: IllegalArgumentException) {
        val date: Date? = context?.deserialize(json, Date::class.java)
        DateTime(date)
    }
}
