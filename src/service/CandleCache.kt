package com.ktor.stock.market.game.jbosak.service

import arrow.core.toOption
import com.google.common.collect.Range
import com.google.common.collect.RangeSet
import com.google.common.collect.TreeRangeSet
import com.ktor.stock.market.game.jbosak.model.CandleInfo
import com.ktor.stock.market.game.jbosak.model.CandleIntervals
import com.ktor.stock.market.game.jbosak.model.CandlesResolution
import com.ktor.stock.market.game.jbosak.utils.Cache

import org.joda.time.Interval
import java.util.*
import kotlin.collections.HashMap

object CandleCache : Cache<String, CandleInfo> {
    private val cache = HashMap<String, CandleInfo>()

    override val size: Int
        get() = cache.size

    override fun set(key: String, value: CandleInfo) {
        this.cache[key] = value
    }
    fun update(key:String, interval: Interval, resolution: CandlesResolution){
        val cacheInfo = cache[key].toOption()
        val intervals = Range.closed(interval.startMillis,interval.endMillis)
        val set = TreeRangeSet.create<Long>()
        set.add(intervals)
        if(cacheInfo.isEmpty()){
            cache[key] = CandleInfo(key, mutableListOf(CandleIntervals(resolution,set)))
        }else {
            val candleInterval = cache[key]!!
                .candleIntervals
                .find { it.resolution == resolution }
            if(candleInterval.toOption().isEmpty())
                cache[key]!!.candleIntervals.add(CandleIntervals(resolution, set))
            else
                candleInterval!!.intervals.add(intervals)

        }
    }

    override fun remove(key: String) = this.cache.remove(key)

    override fun get(key: String) = this.cache[key]

    override fun clear() = this.cache.clear()
}