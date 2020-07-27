package com.ktor.stock.market.game.jbosak.service

import com.intrinio.api.SecurityApi
import com.intrinio.invoker.ApiException
import com.intrinio.models.ApiResponseSecurityStockPrices
import org.threeten.bp.LocalDate


fun fetchSecurityStockPrices(
    identifier: String,
    startDate: LocalDate,
    endDate: LocalDate,
    frequency: String,
    pageSize: Int
): ApiResponseSecurityStockPrices? {
    val securityApi = SecurityApi()
    val nextPage: String? = null
    return try {
        val result = securityApi.getSecurityStockPrices(
            identifier,
            startDate,
            endDate,
            frequency,
            pageSize,
            nextPage
        )
        result
    } catch (e: ApiException) {
        println("ERROR: ${e.message}")
        null
    }

}