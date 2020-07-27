package com.ktor.stock.market.game.jbosak.service

import arrow.core.getOrElse
import com.intrinio.api.CompanyApi
import com.intrinio.invoker.ApiException
import com.intrinio.models.ApiResponseCompanies
import com.ktor.stock.market.game.jbosak.model.Company
import com.ktor.stock.market.game.jbosak.repository.CompanyRepository
import org.threeten.bp.LocalDate

fun getCompanies(skip: Int, limit: Int): List<Company> {
    val companies = CompanyRepository.findCompanies(skip, limit)
    return if (companies.isNotEmpty()) companies
    else refetch(skip, limit)
}

fun getCompany(id: Int? = null, externalId: String? = null, ticker: String? = null): Company? =
    CompanyRepository.findCompany(id, externalId, ticker).getOrElse {
        refetch(0, 1000)
        CompanyRepository.findCompany(id, externalId, ticker).orNull()
    }


private fun refetch(skip: Int, limit: Int): List<Company> {
    val yearAgoDate = LocalDate.now().minusYears(1)
    val fetched = fetchCompanies(yearAgoDate)
    if (fetched != null) CompanyRepository.insertMany(fetched.companies)
    return CompanyRepository.findCompanies(skip, limit)
}

private fun fetchCompanies(
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