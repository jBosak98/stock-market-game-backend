package service

import com.ktor.stock.market.game.jbosak.server.initExternalApi
import com.ktor.stock.market.game.jbosak.service.fetchSecurityStockPrices
import junit.framework.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.threeten.bp.LocalDate

internal class FetchSecurityStockPricesKtTest {

    @Before
    fun setUp(){
        initExternalApi()
    }

    @Test
    fun configureStockMarketApiTest() {
        val response =
            fetchSecurityStockPrices(
                "AAPL",
                LocalDate.of(2020,7,1),
                LocalDate.now(),
                "daily",
                100
            )
        response?.stockPrices
        println(response?.security)
        assertTrue(true)
    }


}