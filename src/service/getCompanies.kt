package com.ktor.stock.market.game.jbosak.service

import arrow.core.getOrElse
import arrow.core.toOption
import com.finnhub.api.apis.DefaultApi
import com.ktor.stock.market.game.jbosak.model.Company
import com.ktor.stock.market.game.jbosak.model.getCompanyMetrics
import com.ktor.stock.market.game.jbosak.repository.CompanyRepository

//import com.intrinio.api.CompanyApi
//import com.intrinio.invoker.ApiException
//import com.intrinio.models.ApiResponseCompanies

//import org.threeten.bp.LocalDate

fun getCompanies(skip: Int, limit: Int): List<Company> {

    val companies = CompanyRepository.findCompanies(skip, limit)
    return if (companies.isNotEmpty()) companies
    else refetch(skip, limit)
}

fun getCompany(id: Int? = null, externalId: String? = null, ticker: String? = null): Company? =
    CompanyRepository
        .findCompany(id, externalId, ticker)
        .getOrElse {
            refetch(0, 1000)
            CompanyRepository.findCompany(id, externalId, ticker).orNull()
        }

val excludedCompanies = arrayOf("1337.HK")

private fun refetch(skip: Int, limit: Int): List<Company> {
    val companies = DefaultApi().companyPeers("AAPL")
        .filter { excludedCompanies.contains(it).not() }
    if (companies.isNotEmpty()) CompanyRepository.insertMany(companies)
    return CompanyRepository.findCompanies(skip, limit)
}

fun refetchCompanyFinancials(ticker:String): Company? {
    DefaultApi()
        .companyBasicFinancials(ticker, "all")
        .getCompanyMetrics()
        .toOption()
        .map {
            CompanyRepository.insertCompanyFinancials(ticker,it)
        }
    return getCompany(ticker = ticker)
}
fun refetchCompanyProfile(ticker:String):Company? {
    DefaultApi()
        .companyProfile2(symbol = ticker, isin = null, cusip = null)
        .let {
            CompanyRepository.insertCompanyProfile(ticker, it)
        }
    return getCompany(ticker = ticker)
}
//
//private fun fetchCompanies(
//    latestFilingDate: LocalDate,
//    sic: String? = null,
//    template: String? = null,
//    sector: String? = null,
//    industryCategory: String? = null,
//    industryGroup: String? = null,
//    hasFundamentals: Boolean = true,
//    hasStockProces: Boolean = true
//): ApiResponseCompanies? {
//    val api = CompanyApi()
//    return try {
//        val result = api.getAllCompanies(
//            latestFilingDate,
//            sic,
//            template,
//            sector,
//            industryCategory,
//            industryGroup,
//            hasFundamentals,
//            hasStockProces,
//            100,
//            null
//        )
//        result
//    } catch (e: ApiException) {
//        null
//    }
//}