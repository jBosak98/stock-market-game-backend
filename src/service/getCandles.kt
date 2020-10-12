package com.ktor.stock.market.game.jbosak.service

import com.finnhub.api.apis.DefaultApi
import com.ktor.stock.market.game.jbosak.model.Candles
import com.ktor.stock.market.game.jbosak.model.CandlesResolution
import com.ktor.stock.market.game.jbosak.model.toCandle
import com.ktor.stock.market.game.jbosak.utils.toUnixTime
import org.joda.time.DateTime



fun getCandles(ticker:String): Candles {
    //TODO:find in db
    //if not, then
    return refetch(ticker)
}


private fun refetch(ticker:String):Candles{
    val now = DateTime.now().minusYears(10)
    val candles = DefaultApi().stockCandles(
        symbol = ticker,
        resolution = CandlesResolution.ONE_MONTH.resolution,
        from = now.minusYears(1).toUnixTime(),
        to = now.toUnixTime(),
        adjusted = true.toString()
    )
    //TODO: insert to db
    //return db.find
    return candles.toCandle()
}