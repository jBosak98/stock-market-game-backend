package com.ktor.stock.market.game.jbosak.model

import arrow.core.getOrElse
import arrow.core.toOption
import org.joda.time.DateTime

data class CandlesContainer(
    val openPrices:List<Float>?,
    val highPrices:List<Float>?,
    val lowPrices:List<Float>?,
    val closePrices:List<Float>?,
    val volumes:List<Float>?,
    val time:List<DateTime>?,
    val status:String?
)

fun CandlesContainer.toSingleCandles(): List<SingleCandle> {
    val size = this.openPrices?.size?: 0

    if(
        highPrices?.size?:-1 != size ||
        lowPrices?.size?:-1 != size ||
        closePrices?.size?:-1 != size ||
        volumes?.size?:-1 != size ||
        time?.size?:-1 != size
    ){
        return emptyList()
    }
    return closePrices
        ?.indices
        ?.map {
            SingleCandle(
                openPrice = this.openPrices!![it],
                highPrice = this.highPrices!![it],
                lowPrice = this.lowPrices!![it],
                closePrice = this.closePrices[it],
                volume = this.volumes!![it],
                time = this.time!![it]
            )
        }
        .toOption()
        .getOrElse { emptyList() }
}
