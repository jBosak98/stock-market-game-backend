package com.ktor.stock.market.game.jbosak.service

import arrow.core.toOption
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
        if(cacheInfo.isEmpty()){
            cache[key] = CandleInfo(key, mutableListOf(CandleIntervals(resolution,mutableListOf(interval))))
        }else {
            val candleInterval = cache[key]!!
                .candleIntervals
                .find { it.resolution == resolution }
            if(candleInterval.toOption().isEmpty())
                cache[key]!!.candleIntervals.add(CandleIntervals(resolution, mutableListOf(interval)))
            else
                candleInterval!!.intervals = mergeOverlappedIntervals(candleInterval.intervals + interval)

        }
    }
    private fun mergeOverlappedIntervals(intervals: List<Interval>): List<Interval> =
        intervals
            .sortedBy { it.start }
            .fold(Stack()){ acc: Stack<Interval>, interval ->
                if (acc.isEmpty()) acc.apply { push(interval) }
                else {
                    val top = acc.peek()
                    when {
                        top.end < interval.start ->
                            acc.apply { push(interval) }
                        top.end < interval.end -> acc.apply {
                            val newTop = Interval(top.start,interval.end)
                            pop()
                            push(newTop)
                        }
                        else -> acc
                    }
                }
            }

    override fun remove(key: String) = this.cache.remove(key)

    override fun get(key: String) = this.cache[key]

    override fun clear() = this.cache.clear()
}