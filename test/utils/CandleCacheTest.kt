package utils

import com.ktor.stock.market.game.jbosak.model.CandlesResolution
import com.ktor.stock.market.game.jbosak.service.CandleCache
import org.joda.time.DateTime
import org.joda.time.Interval
import org.junit.jupiter.api.Test

internal class CandleCacheTest {

    @Test
    fun update() {
        CandleCache.update("AAPL",
            Interval(
                DateTime(2020,1,1,1,1)
                ,DateTime(2020,1,3,1,1)),
            CandlesResolution.THIRTY_MINUTES
        )
//        assert(CandleCache["AAPL"]!!.candleIntervals.size == 1)
//        assert(CandleCache["AAPL"]!!.candleIntervals[0].intervals.size == 1)
//        assert(CandleCache["AAPL"]!!.candleIntervals[0].intervals[0].start.toDateTime() == DateTime(2020,1,1,1,1))
//        assert(CandleCache["AAPL"]!!.candleIntervals[0].intervals[0].end.toDateTime() == DateTime(2020,1,3,1,1))

        CandleCache.update("AAPL",
            Interval(
                DateTime(2020,1,2,1,1),
                DateTime(2020,1,5,1,1)),
            CandlesResolution.THIRTY_MINUTES
        )
//        assert(CandleCache["AAPL"]!!.candleIntervals.size == 1)
//        assert(CandleCache["AAPL"]!!.candleIntervals[0].intervals.size == 1)
//        assert(CandleCache["AAPL"]!!.candleIntervals[0].intervals[0].start.toDateTime() == DateTime(2020,1,1,1,1))
//        assert(CandleCache["AAPL"]!!.candleIntervals[0].intervals[0].end.toDateTime() == DateTime(2020,1,5,1,1))
//

        CandleCache.update("AAPL",
            Interval(
                DateTime(2020,1,7,1,1),
                DateTime(2020,1,8,1,1)),
            CandlesResolution.THIRTY_MINUTES
        )
//        assert(CandleCache["AAPL"]!!.candleIntervals.size == 1)
//        assert(CandleCache["AAPL"]!!.candleIntervals[0].intervals.size == 2)
//        assert(CandleCache["AAPL"]!!.candleIntervals[0].intervals[0].start.toDateTime() == DateTime(2020,1,1,1,1))
//        assert(CandleCache["AAPL"]!!.candleIntervals[0].intervals[0].end.toDateTime() == DateTime(2020,1,5,1,1))

        CandleCache.update("AAPL",
            Interval(
                DateTime(2020,1,4,1,1),
                DateTime(2020,1,7,1,1)),
            CandlesResolution.THIRTY_MINUTES
        )

//        assert(CandleCache["AAPL"]!!.candleIntervals.size == 1)
//        assert(CandleCache["AAPL"]!!.candleIntervals[0].intervals.size == 1)
//        assert(CandleCache["AAPL"]!!.candleIntervals[0].intervals[0].start.toDateTime() == DateTime(2020,1,1,1,1))
//        assert(CandleCache["AAPL"]!!.candleIntervals[0].intervals[0].end.toDateTime() == DateTime(2020,1,8,1,1))
    }
}