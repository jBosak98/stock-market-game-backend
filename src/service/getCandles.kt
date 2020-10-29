package com.ktor.stock.market.game.jbosak.service


import arrow.core.Either
import arrow.core.Left
import arrow.core.Right
import arrow.core.getOrHandle
import arrow.syntax.function.pipe
import com.finnhub.api.apis.DefaultApi
import com.google.common.collect.Range
import com.google.common.collect.TreeRangeSet
import com.ktor.stock.market.game.jbosak.model.CandlesResolution
import com.ktor.stock.market.game.jbosak.model.SingleCandle
import com.ktor.stock.market.game.jbosak.model.toCandle
import com.ktor.stock.market.game.jbosak.repository.CandleRepository
import com.ktor.stock.market.game.jbosak.utils.roundDown
import com.ktor.stock.market.game.jbosak.utils.toUnixTime
import org.joda.time.DateTime
import org.joda.time.Duration
import org.joda.time.Interval
import org.joda.time.LocalDate
import kotlin.ranges.contains

private fun checkCache(neededRange:Range<Long>, ticker:String, res:CandlesResolution): Either<TreeRangeSet<Long>, Boolean> {
    val rangesToFetch = TreeRangeSet.create<Long>()
    rangesToFetch.add(neededRange)
    val rangeInCache = CandleCache[ticker]
        ?.candleIntervals
        ?.find { it.resolution == res }
        ?.intervals
    rangeInCache?.let { rangesToFetch.removeAll(it) }
    val isValid = rangesToFetch.isEmpty &&
            CandleCache[ticker]
                ?.candleIntervals
                ?.find { it.resolution == res }
                ?.intervals
                ?.encloses(neededRange)
            ?: false

    return if(isValid) Right(true)
    else Left(rangesToFetch)
}


fun getCandles(ticker:String, res:CandlesResolution, from:DateTime, to:DateTime?): List<SingleCandle> {
    val toDate = (to?: DateTime.now())
        .pipe { if(it.isAfterNow) DateTime.now() else it }
        .pipe { roundDateTimeByResolution(it, res) }

    val neededRange =
        Range.closed(
            normalizeFromTime(from, res).millis,
            normalizeToTime(toDate, res).millis
        )
    return checkCache(neededRange,ticker, res)
        .map { CandleRepository.find(ticker, res, from, toDate) }
        .getOrHandle {
            val ranges = it.asRanges()
            when (ranges?.size){
                0 -> CandleRepository.find(ticker, res, from, toDate)
                in 1..2 -> ranges?.map {
                    refetch(ticker, res, DateTime(it.lowerEndpoint()), DateTime(it.upperEndpoint()))
                }
                else ->refetch(ticker, res, from, toDate)
            }
            CandleRepository.find(ticker, res,from)
        }
}

private fun normalizeFromTime(from:DateTime, res:CandlesResolution) =
    when(res){
        CandlesResolution.ONE_MONTH -> DateTime((from.year().get() - 1).toString())
        CandlesResolution.ONE_WEEK -> DateTime((from.year().get()).toString())
        CandlesResolution.ONE_DAY -> DateTime((from.year().get()).toString())
        else -> LocalDate(from).toDateTimeAtStartOfDay()
    }
        .pipe { if(it.isAfterNow) DateTime.now() else it }


private fun normalizeToTime(to:DateTime, res:CandlesResolution) =
    when(res){
        CandlesResolution.ONE_MONTH -> DateTime((to.year().get() + 2).toString())
        CandlesResolution.ONE_WEEK -> DateTime((to.year().get() + 1).toString())
        CandlesResolution.ONE_DAY -> DateTime((to.year().get() + 1).toString())
        else -> LocalDate(to.plusDays(1)).toDateTimeAtStartOfDay()
    }
        .pipe { if(it.isAfterNow) DateTime.now() else it }
        .pipe { roundDateTimeByResolution(it,res) }

private fun roundDateTimeByResolution(dateTime: DateTime, res:CandlesResolution) =
    when(res){
        CandlesResolution.ONE_WEEK -> dateTime.roundDown(Duration.standardDays(7))
        CandlesResolution.ONE_DAY -> dateTime.roundDown(Duration.standardDays(1))
        CandlesResolution.ONE_MONTH -> dateTime.roundDown(Duration.standardDays(7*4))
        CandlesResolution.THIRTY_MINUTES -> dateTime.roundDown(Duration.standardMinutes(30))
        CandlesResolution.FIVE_MINUTES -> dateTime.roundDown(Duration.standardMinutes(5))
        CandlesResolution.FIFTEEN_MINUTES -> dateTime.roundDown(Duration.standardMinutes(15))
        CandlesResolution.ONE_MINUTE -> dateTime.roundDown(Duration.standardMinutes(1))
    }


private fun refetch(
    ticker:String,
    res:CandlesResolution,
    from:DateTime,
    to:DateTime
) {

    val candles = DefaultApi()
        .stockCandles(
            symbol = ticker,
            resolution = res.resolution,
            from = from.toUnixTime(),
            to = to.toUnixTime(),
            adjusted = true.toString()
        ).toCandle()
    val interval = Interval(from, to)
    CandleCache.update(ticker,interval,res)
    CandleRepository.upsert(candles,ticker,res)

}