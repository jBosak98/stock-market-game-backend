package com.ktor.stock.market.game.jbosak.repository

import arrow.core.Option
import arrow.core.extensions.list.functorFilter.filter
import arrow.core.toOption
import com.finnhub.api.models.CompanyProfile2
import com.ktor.stock.market.game.jbosak.model.Company
import com.ktor.stock.market.game.jbosak.model.CompanyFinancials
import com.ktor.stock.market.game.jbosak.model.db.Companies
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.InsertStatement
import org.jetbrains.exposed.sql.statements.UpdateStatement
import org.jetbrains.exposed.sql.transactions.transaction

object CompanyRepository {

    fun insert(company: String) = transaction {
        Companies.insert(assignCompany(company)) get Companies.id
    }
    fun insertCompanyFinancials(ticker: String, financials:CompanyFinancials) = transaction {
        Companies.update(
            where = { Companies.ticker eq ticker },
            limit = null,
            body = assignCompanyFinancials(financials)
        )
    }

    fun insertCompanyProfile(ticker: String, companyProfile: CompanyProfile2) = transaction{
        Companies.update(
            where = { Companies.ticker eq ticker },
            limit = null,
            body = assignCompanyProfile(companyProfile)
        )
    }

    fun insertMany(companiesNames: List<String>) = transaction {
        companiesNames
            .filter { findCompany(ticker = it).isEmpty() }
            .map { company ->
                Companies.insert(assignCompany(company)) get Companies.id
            }
    }

    fun findCompany(id: Int? = null, externalId: String? = null, ticker: String? = null) = transaction {
        val comparator: SqlExpressionBuilder.() -> Op<Boolean> =
            when {
                id != null -> {
                    { Companies.id eq id }
                }
                ticker != null -> {
                    { Companies.ticker eq ticker }
                }
                else -> return@transaction Option.empty<Company>()
            }
        Companies
            .select(comparator)
            .singleOrNull()
            ?.let(ResultRow::toCompany)
            .toOption()
    }


    fun companiesSize() = transaction { Companies.selectAll().count() }

    fun findCompanies(skip: Int, limit: Int) = transaction {
        Companies
            .selectAll()
            .limit(limit, skip)
            .map(ResultRow::toCompany)
    }

    private fun <T> assignCompanyProfile(companyProfile: CompanyProfile2): T.(UpdateStatement) -> Unit = {
        it[Companies.country] = companyProfile.country
        it[Companies.currency] = companyProfile.currency
        it[Companies.exchange] = companyProfile.exchange
        it[Companies.ipo] = companyProfile.ipo
        it[Companies.name] = companyProfile.name
        it[Companies.phone] = companyProfile.phone
        it[Companies.shareOutstanding] = companyProfile.shareOutstanding
        it[Companies.weburl] = companyProfile.weburl
        it[Companies.logo] = companyProfile.logo
        it[Companies.finnhubIndustry] = companyProfile.finnhubIndustry
    }
    private fun <T> assignCompany(companyName: String): T.(InsertStatement<Number>) -> Unit = {
        it[Companies.ticker] = companyName
    }
    private fun <T> assignCompanyFinancials(financials:CompanyFinancials): T.(UpdateStatement) -> Unit = {
        it[Companies.tenDayAverageTradingVolume] = financials.tenDayAverageTradingVolume
        it[Companies.thirteenWeekPriceReturnDaily] = financials.thirteenWeekPriceReturnDaily
        it[Companies.twentySixWeekPriceReturnDaily] = financials.twentySixWeekPriceReturnDaily
        it[Companies.threeMonthAverageTradingVolume] = financials.threeMonthAverageTradingVolume
        it[Companies.fiftyTwoWeekHigh] = financials.fiftyTwoWeekHigh
        it[Companies.fiftyTwoWeekHighDate] = financials.fiftyTwoWeekHighDate
        it[Companies.fiftyTwoWeekLow] = financials.fiftyTwoWeekLow
        it[Companies.fiftyTwoWeekLowDate] = financials.fiftyTwoWeekLowDate
        it[Companies.fiftyTwoWeekPriceReturnDaily] = financials.fiftyTwoWeekPriceReturnDaily
        it[Companies.fiveDayPriceReturnDaily] = financials.fiveDayPriceReturnDaily
        it[Companies.assetTurnoverAnnual] = financials.assetTurnoverAnnual
        it[Companies.assetTurnoverTTM] = financials.assetTurnoverTTM
        it[Companies.beta] = financials.beta
        it[Companies.bookValuePerShareAnnual] = financials.bookValuePerShareAnnual
        it[Companies.bookValuePerShareQuarterly] = financials.bookValuePerShareQuarterly
        it[Companies.bookValueShareGrowth5Y] = financials.bookValueShareGrowth5Y
        it[Companies.capitalSpendingGrowth5Y] = financials.capitalSpendingGrowth5Y
        it[Companies.cashFlowPerShareAnnual] = financials.cashFlowPerShareAnnual
        it[Companies.cashFlowPerShareTTM] = financials.cashFlowPerShareTTM
        it[Companies.cashPerSharePerShareAnnual] = financials.cashPerSharePerShareAnnual
        it[Companies.cashPerSharePerShareQuarterly] = financials.cashPerSharePerShareQuarterly
        it[Companies.currentDividendYieldTTM] = financials.currentDividendYieldTTM
        it[Companies.currentEvOverfreeCashFlowAnnual] = financials.currentEvOverfreeCashFlowAnnual
        it[Companies.currentEvOverfreeCashFlowTTM] = financials.currentEvOverfreeCashFlowTTM
        it[Companies.currentRatioAnnual] = financials.currentRatioAnnual
        it[Companies.currentRatioQuarterly] = financials.currentRatioQuarterly
        it[Companies.dividendGrowthRate5Y] = financials.dividendGrowthRate5Y
        it[Companies.dividendPerShare5Y] = financials.dividendPerShare5Y
        it[Companies.dividendPerShareAnnual] = financials.dividendPerShareAnnual
        it[Companies.dividendYield5Y] = financials.dividendYield5Y
        it[Companies.dividendYieldIndicatedAnnual] = financials.dividendYieldIndicatedAnnual
        it[Companies.dividendsPerShareTTM] = financials.dividendsPerShareTTM
        it[Companies.ebitdPerShareTTM] = financials.ebitdPerShareTTM
        it[Companies.ebitdaCagr5Y] = financials.ebitdaCagr5Y
        it[Companies.ebitdaInterimCagr5Y] = financials.ebitdaInterimCagr5Y
        it[Companies.epsBasicExclExtraItemsAnnual] = financials.epsBasicExclExtraItemsAnnual
        it[Companies.epsBasicExclExtraItemsTTM] = financials.epsBasicExclExtraItemsTTM
        it[Companies.epsExclExtraItemsAnnual] = financials.epsExclExtraItemsAnnual
        it[Companies.epsExclExtraItemsTTM] = financials.epsExclExtraItemsTTM
        it[Companies.epsGrowth3Y] = financials.epsGrowth3Y
        it[Companies.epsGrowth5Y] = financials.epsGrowth5Y
        it[Companies.epsGrowthQuarterlyYoy] = financials.epsGrowthQuarterlyYoy
        it[Companies.epsGrowthTTMYoy] = financials.epsGrowthTTMYoy
        it[Companies.epsInclExtraItemsAnnual] = financials.epsInclExtraItemsAnnual
        it[Companies.epsInclExtraItemsTTM] = financials.epsInclExtraItemsTTM
        it[Companies.epsNormalizedAnnual] = financials.epsNormalizedAnnual
        it[Companies.focfCagr5Y] = financials.focfCagr5Y
        it[Companies.freeCashFlowAnnual] = financials.freeCashFlowAnnual
        it[Companies.freeCashFlowPerShareTTM] = financials.freeCashFlowPerShareTTM
        it[Companies.freeCashFlowTTM] = financials.freeCashFlowTTM
        it[Companies.freeOperatingCashFlowOverRevenue5Y] = financials.freeOperatingCashFlowOverRevenue5Y
        it[Companies.freeOperatingCashFlowOverRevenueTTM] = financials.freeOperatingCashFlowOverRevenueTTM
        it[Companies.grossMargin5Y] = financials.grossMargin5Y
        it[Companies.grossMarginAnnual] = financials.grossMarginAnnual
        it[Companies.grossMarginTTM] = financials.grossMarginTTM
        it[Companies.inventoryTurnoverAnnual] = financials.inventoryTurnoverAnnual
        it[Companies.inventoryTurnoverTTM] = financials.inventoryTurnoverTTM
        it[Companies.longTermDebtOverEquityAnnual] = financials.longTermDebtOverEquityAnnual
        it[Companies.longTermDebtOverEquityQuarterly] = financials.longTermDebtOverEquityQuarterly
        it[Companies.marketCapitalization] = financials.marketCapitalization
        it[Companies.monthToDatePriceReturnDaily] = financials.monthToDatePriceReturnDaily
        it[Companies.netDebtAnnual] = financials.netDebtAnnual
        it[Companies.netDebtInterim] = financials.netDebtInterim
        it[Companies.netIncomeEmployeeAnnual] = financials.netIncomeEmployeeAnnual
        it[Companies.netIncomeEmployeeTTM] = financials.netIncomeEmployeeTTM
        it[Companies.netInterestCoverageAnnual] = financials.netInterestCoverageAnnual
        it[Companies.netInterestCoverageTTM] = financials.netInterestCoverageTTM
        it[Companies.netMarginGrowth5Y] = financials.netMarginGrowth5Y
        it[Companies.netProfitMargin5Y] = financials.netProfitMargin5Y
        it[Companies.netProfitMarginAnnual] = financials.netProfitMarginAnnual
        it[Companies.netProfitMarginTTM] = financials.netProfitMarginTTM
        it[Companies.operatingMargin5Y] = financials.operatingMargin5Y
        it[Companies.operatingMarginAnnual] = financials.operatingMarginAnnual
        it[Companies.operatingMarginTTM] = financials.operatingMarginTTM
        it[Companies.payoutRatioAnnual] = financials.payoutRatioAnnual
        it[Companies.payoutRatioTTM] = financials.payoutRatioTTM
        it[Companies.pbAnnual] = financials.pbAnnual
        it[Companies.pbQuarterly] = financials.pbQuarterly
        it[Companies.pcfShareTTM] = financials.pcfShareTTM
        it[Companies.peBasicExclExtraTTM] = financials.peBasicExclExtraTTM
        it[Companies.peExclExtraAnnual] = financials.peExclExtraAnnual
        it[Companies.peExclExtraHighTTM] = financials.peExclExtraHighTTM
        it[Companies.peExclExtraTTM] = financials.peExclExtraTTM
        it[Companies.peExclLowTTM] = financials.peExclLowTTM
        it[Companies.peInclExtraTTM] = financials.peInclExtraTTM
        it[Companies.peNormalizedAnnual] = financials.peNormalizedAnnual
        it[Companies.pfcfShareAnnual] = financials.pfcfShareAnnual
        it[Companies.pfcfShareTTM] = financials.pfcfShareTTM
        it[Companies.pretaxMargin5Y] = financials.pretaxMargin5Y
        it[Companies.pretaxMarginAnnual] = financials.pretaxMarginAnnual
        it[Companies.pretaxMarginTTM] = financials.pretaxMarginTTM
        it[Companies.priceRelativeToSAndP50013Week] = financials.priceRelativeToSAndP50013Week
        it[Companies.priceRelativeToSAndP50026Week] = financials.priceRelativeToSAndP50026Week
        it[Companies.priceRelativeToSAndP5004Week] = financials.priceRelativeToSAndP5004Week
        it[Companies.priceRelativeToSAndP50052Week] = financials.priceRelativeToSAndP50052Week
        it[Companies.priceRelativeToSAndP500Yt] = financials.priceRelativeToSAndP500Yt
        it[Companies.psAnnual] = financials.psAnnual
        it[Companies.psTTM] = financials.psTTM
        it[Companies.ptbvAnnual] = financials.ptbvAnnual
        it[Companies.ptbvQuarterly] = financials.ptbvQuarterly
        it[Companies.quickRatioAnnual] = financials.quickRatioAnnual
        it[Companies.quickRatioQuarterly] = financials.quickRatioQuarterly
        it[Companies.receivablesTurnoverAnnual] = financials.receivablesTurnoverAnnual
        it[Companies.receivablesTurnoverTTM] = financials.receivablesTurnoverTTM
        it[Companies.revenueEmployeeAnnual] = financials.revenueEmployeeAnnual
        it[Companies.revenueEmployeeTTM] = financials.revenueEmployeeTTM
        it[Companies.revenueGrowth3Y] = financials.revenueGrowth3Y
        it[Companies.revenueGrowth5Y] = financials.revenueGrowth5Y
        it[Companies.revenueGrowthQuarterlyYoy] = financials.revenueGrowthQuarterlyYoy
        it[Companies.revenueGrowthTTMYoy] = financials.revenueGrowthTTMYoy
        it[Companies.revenuePerShareAnnual] = financials.revenuePerShareAnnual
        it[Companies.revenuePerShareTTM] = financials.revenuePerShareTTM
        it[Companies.revenueShareGrowth5Y] = financials.revenueShareGrowth5Y
        it[Companies.roaRfy] = financials.roaRfy
        it[Companies.roaeTTM] = financials.roaeTTM
        it[Companies.roeRfy] = financials.roeRfy
        it[Companies.roaa5Y] = financials.roaa5Y
        it[Companies.roeTTM] = financials.roeTTM
        it[Companies.roi5Y] = financials.roi5Y
        it[Companies.roiAnnual] = financials.roiAnnual
        it[Companies.roiTTM] = financials.roiTTM
        it[Companies.tangibleBookValuePerShareAnnual] = financials.tangibleBookValuePerShareAnnual
        it[Companies.tangibleBookValuePerShareQuarterly] = financials.tangibleBookValuePerShareQuarterly
        it[Companies.tbvCagr5Y] = financials.tbvCagr5Y
        it[Companies.totalDebtOverTotalEquityAnnual] = financials.totalDebtOverTotalEquityAnnual
        it[Companies.totalDebtOverTotalEquityQuarterly] = financials.totalDebtOverTotalEquityQuarterly
        it[Companies.totalDebtCagr5Y] = financials.totalDebtCagr5Y
        it[Companies.yearToDatePriceReturnDaily] = financials.yearToDatePriceReturnDaily
    }

}