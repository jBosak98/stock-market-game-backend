package com.ktor.stock.market.game.jbosak.utils

import arrow.core.Option
import arrow.core.getOrElse

fun <T> Option<T>.getNullableValue()
        = this.getOrElse { null }