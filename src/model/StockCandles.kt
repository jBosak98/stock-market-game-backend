package com.ktor.stock.market.game.jbosak.model

import com.finnhub.api.models.StockCandles
import org.joda.time.DateTime

fun StockCandles.toCandle() =
    CandlesContainer(
        openPrices = o,
        highPrices = h,
        lowPrices= l,
        closePrices = c,
        volumes = v,
        time = t?.map { DateTime(it*1000) },
        status = s
    )
