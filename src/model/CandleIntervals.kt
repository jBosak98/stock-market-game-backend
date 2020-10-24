package com.ktor.stock.market.game.jbosak.model

import com.google.common.collect.RangeSet
import org.joda.time.DateTime
import org.joda.time.Interval

data class CandleIntervals(
    val resolution: CandlesResolution,
    var intervals: RangeSet<Long>
)