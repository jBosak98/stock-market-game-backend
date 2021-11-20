package com.ktor.stock.market.game.jbosak.service


import arrow.syntax.function.pipe
import com.ktor.stock.market.game.jbosak.model.CandlesResolution
import com.ktor.stock.market.game.jbosak.model.SingleCandle
import com.ktor.stock.market.game.jbosak.repository.CandleRepository
import com.ktor.stock.market.game.jbosak.utils.roundDown
import org.joda.time.DateTime
import org.joda.time.Duration


fun getCandles(ticker:String, res:CandlesResolution, from:DateTime, to:DateTime?): List<SingleCandle> {
        val toDate = (to?: DateTime.now())
        .pipe { if(it.isAfterNow) DateTime.now() else it }
        .pipe { roundDateTimeByResolution(it, res) }
    return CandleRepository.find(ticker, res,from, toDate)
}

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