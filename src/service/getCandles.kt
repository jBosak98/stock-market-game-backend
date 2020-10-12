package com.ktor.stock.market.game.jbosak.service

import com.finnhub.api.apis.DefaultApi
import com.ktor.stock.market.game.jbosak.model.CandlesResolution
import com.ktor.stock.market.game.jbosak.model.SingleCandle
import com.ktor.stock.market.game.jbosak.model.toCandle
import com.ktor.stock.market.game.jbosak.repository.CandleRepository
import com.ktor.stock.market.game.jbosak.utils.toUnixTime
import org.joda.time.DateTime
import org.joda.time.Days
import org.joda.time.Minutes
import org.joda.time.Months


fun getCandles(ticker:String): List<SingleCandle> {
    val res = CandlesResolution.ONE_MONTH
    val now = DateTime.now()
    val from = now.minusYears(1)
    val to = now

    val candles = CandleRepository.find(ticker, res, from, to)
    val isValid = when(res){
        CandlesResolution.ONE_MINUTE ->
            Minutes.minutesBetween(from, to).minutes == candles.size
        CandlesResolution.FIVE_MINUTES ->
            Minutes.minutesBetween(from, to).minutes == candles.size * 5
        CandlesResolution.FIFTEEN_MINUTES ->
            Minutes.minutesBetween(from, to).minutes == candles.size * 15
        CandlesResolution.THIRTY_MINUTES ->
            Minutes.minutesBetween(from, to).minutes == candles.size * 30
        CandlesResolution.ONE_DAY ->
            Days.daysBetween(from, to).days == candles.size
        CandlesResolution.ONE_WEEK ->
            Days.daysBetween(from, to).days == candles.size * 7
        CandlesResolution.ONE_MONTH ->
            Months.monthsBetween(from, to).months == candles.size
    }
    return if(isValid) candles
    else refetch(ticker, res, from, to)
}


private fun refetch(
    ticker:String,
    res:CandlesResolution,
    from:DateTime,
    to:DateTime
): List<SingleCandle> {
    val candles = DefaultApi()
        .stockCandles(
            symbol = ticker,
            resolution = res.resolution,
            from = from.toUnixTime(),
            to = to.toUnixTime(),
            adjusted = true.toString()
        ).toCandle()
    CandleRepository.insert(candles,ticker,res)
    return  CandleRepository.find(ticker, res,from)
}