package com.ktor.stock.market.game.jbosak.service

import arrow.core.extensions.list.foldable.exists
import arrow.syntax.function.pipe
import com.finnhub.api.apis.DefaultApi
import com.ktor.stock.market.game.jbosak.model.CandlesResolution
import com.ktor.stock.market.game.jbosak.model.SingleCandle
import com.ktor.stock.market.game.jbosak.model.toCandle
import com.ktor.stock.market.game.jbosak.repository.CandleRepository
import com.ktor.stock.market.game.jbosak.utils.toUnixTime
import org.joda.time.DateTime
import org.joda.time.Interval
import org.joda.time.LocalDate


fun getCandles(ticker:String, res:CandlesResolution, from:DateTime, to:DateTime?): List<SingleCandle> {
    val toDate = to?: DateTime.now()
    val isValid = CandleCache[ticker]
        ?.candleIntervals
        ?.find { it.resolution == res }
        ?.intervals
        ?.exists { from.isAfter(it.start) && toDate.isBefore(it.end) }
        ?: false

    return if(isValid) CandleRepository.find(ticker, res, from, toDate)
    else refetch(ticker, res, from, toDate)
}


private fun refetch(
    ticker:String,
    res:CandlesResolution,
    from:DateTime,
    to:DateTime
): List<SingleCandle> {
    val normalizedFrom = when(res){
        CandlesResolution.ONE_MONTH -> DateTime((from.year().get() - 1).toString())
        CandlesResolution.ONE_WEEK -> DateTime((from.year().get()).toString())
        CandlesResolution.ONE_DAY -> DateTime((from.year().get()).toString())
        else -> LocalDate(from).toDateTimeAtStartOfDay()
    }
        .pipe { if(it.isAfterNow) DateTime.now() else it }
    val normalizedTo = when(res){
        CandlesResolution.ONE_MONTH -> DateTime((to.year().get() + 2).toString())
        CandlesResolution.ONE_WEEK -> DateTime((to.year().get() + 1).toString())
        CandlesResolution.ONE_DAY -> DateTime((to.year().get() + 1).toString())
        else -> LocalDate(to.plusDays(1)).toDateTimeAtStartOfDay()
    }
        .pipe { if(it.isAfterNow) DateTime.now() else it }

    val candles = DefaultApi()
        .stockCandles(
            symbol = ticker,
            resolution = res.resolution,
            from = normalizedFrom.toUnixTime(),
            to = normalizedTo.toUnixTime(),
            adjusted = true.toString()
        ).toCandle()
    val interval = Interval(normalizedFrom, normalizedTo)
    CandleCache.update(ticker,interval,res)
    CandleRepository.upsert(candles,ticker,res)
    return  CandleRepository.find(ticker, res,from)
}