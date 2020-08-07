package com.ktor.stock.market.game.jbosak.service

import arrow.core.Validated
import com.intrinio.api.SecurityApi
import com.intrinio.invoker.ApiException
import com.intrinio.models.RealtimeStockPrice
import com.ktor.stock.market.game.jbosak.graphQL.ClientGraphQLException
import com.ktor.stock.market.game.jbosak.model.StockPrice
import com.ktor.stock.market.game.jbosak.repository.CompanyRepository
import com.ktor.stock.market.game.jbosak.repository.StockPriceRepository
import org.joda.time.DateTime
import org.threeten.bp.OffsetDateTime

private fun isUpdatedInLast30Minutes(date: DateTime): Boolean {
//    return date.isBefore(OffsetDateTime.now().minusMinutes(3000).toEpochSecond())
    return true
}

fun ensureCompanyExists(ticker:String): ClientGraphQLException? =
    if(CompanyRepository.findCompany(ticker = ticker).isEmpty())
        ClientGraphQLException("The security does not exist")
    else
        null



fun getRealTimeSecurityPrice(ticker: String): Validated<ClientGraphQLException, StockPrice>{
    ensureCompanyExists(ticker)?.let { return Validated.Invalid(it) }
    val price = StockPriceRepository.findPrice(ticker).firstOrNull()
        ?: refetch(ticker)
        ?: return Validated.Invalid(ClientGraphQLException("Price not found"))

    return if (isUpdatedInLast30Minutes(price.updatedOn)) Validated.Valid(price)
    else  Validated.Invalid(ClientGraphQLException("Price not found"))
}


private fun refetch(securityIdentifier: String): StockPrice? {
    val price = fetchRealTimePrice(securityIdentifier)
    price?.updatedOn?.isBefore(OffsetDateTime.now().minusMinutes(30))
    if (price != null)
        StockPriceRepository.insert(price)
    return StockPriceRepository.findPrice(securityIdentifier).firstOrNull()

}

private fun fetchRealTimePrice(securityIdentifier: String): RealtimeStockPrice? {
    val securityApi = SecurityApi()
    return try {
        val realTimeStockPrice = securityApi.getSecurityRealtimePrice(securityIdentifier, null)
        realTimeStockPrice
    } catch (e: ApiException) {
        null
    }
}