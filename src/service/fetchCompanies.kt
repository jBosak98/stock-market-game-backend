package com.ktor.stock.market.game.jbosak.service

import com.intrinio.api.CompanyApi
import com.intrinio.invoker.ApiException
import com.intrinio.models.ApiResponseCompanies
import org.threeten.bp.LocalDate

fun fetchCompanies(
    latestFilingDate: LocalDate,
    sic: String? = null,
    template: String? = null,
    sector: String? = null,
    industryCategory: String? = null,
    industryGroup: String? = null,
    hasFundamentals: Boolean = true,
    hasStockProces: Boolean = true
): ApiResponseCompanies? {
    val api = CompanyApi()
    return try {
        val result = api.getAllCompanies(
            latestFilingDate,
            sic,
            template,
            sector,
            industryCategory,
            industryGroup,
            hasFundamentals,
            hasStockProces,
            100,
            null
        )
        result
    } catch (e: ApiException) {
        null
    }
}