package com.ktor.stock.market.game.jbosak.service

import arrow.core.getOrElse
import arrow.core.toOption
import com.finnhub.api.apis.DefaultApi
import com.google.gson.annotations.SerializedName
import com.ktor.stock.market.game.jbosak.model.Company
import com.ktor.stock.market.game.jbosak.model.TickerInfoResponse
import com.ktor.stock.market.game.jbosak.model.getCompanyMetrics
import com.ktor.stock.market.game.jbosak.repository.CompanyRepository
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.http.ContentType.Application.Json
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import io.ktor.client.features.json.*
import io.ktor.server.engine.*

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


suspend fun refetchCompanyInfo(ticker:String, domain:String, port:String): Company? {
    val url = "${domain}:${port}/info/ticker/${ticker.toLowerCase()}"

    val companyInfo = HttpClient { install(JsonFeature) }
        .use { it.get<TickerInfoResponse>(url) }
    CompanyRepository.insertComapnyInfo(ticker, companyInfo)
    return getCompany(ticker = ticker)
}