package com.ktor.stock.market.game.jbosak.utils

import org.joda.time.DateTime
import org.threeten.bp.OffsetDateTime

fun OffsetDateTime.toDateTime()
        = DateTime(year, monthValue, dayOfMonth, hour, minute)