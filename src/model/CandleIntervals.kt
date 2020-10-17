package com.ktor.stock.market.game.jbosak.model

import org.joda.time.Interval

data class CandleIntervals(
    val resolution: CandlesResolution,
    var intervals: List<Interval>
)