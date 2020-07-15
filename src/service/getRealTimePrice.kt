package com.ktor.stock.market.game.jbosak.service

import com.intrinio.api.SecurityApi
import com.intrinio.invoker.ApiException
import com.intrinio.models.RealtimeStockPrice
import com.ktor.stock.market.game.jbosak.model.StockPrice
import com.ktor.stock.market.game.jbosak.repository.StockPriceRepository
import org.joda.time.DateTime
import org.threeten.bp.OffsetDateTime

private fun isUpdatedInLast30Minutes(date:DateTime):Boolean{
    return date.isBefore(OffsetDateTime.now().minusMinutes(30).toEpochSecond())
}

fun getRealTimeSecurityPrice(ticker:String): StockPrice? {
    val price = StockPriceRepository.findPrice(ticker).firstOrNull()
    return  if( price?.updatedOn != null /*&& isUpdatedInLast30Minutes(price.updatedOn)*/) price
            else refetch(ticker)

}


private fun refetch(securityIdentifier:String): StockPrice? {
    val price = fetchRealTimePrice(securityIdentifier)
    price?.updatedOn?.isBefore(OffsetDateTime.now().minusMinutes(30))
    if(price != null)
        StockPriceRepository.insert(price)
    return StockPriceRepository.findPrice(securityIdentifier).firstOrNull()

}

private fun fetchRealTimePrice(securityIdentifier:String):RealtimeStockPrice?{
    val securityApi = SecurityApi()
    return try {
        val realTimeStockPrice
                = securityApi.getSecurityRealtimePrice(securityIdentifier, null)
        realTimeStockPrice
    }catch (e: ApiException){
        null
    }
}