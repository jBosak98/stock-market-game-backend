package com.ktor.stock.market.game.jbosak.graphQL.schema

import com.ktor.stock.market.game.jbosak.graphQL.dataLoadersConfig.DataLoaderKey
import com.ktor.stock.market.game.jbosak.graphQL.dataLoadersConfig.dataloaderResolver
import com.ktor.stock.market.game.jbosak.graphQL.dataLoadersConfig.resolve
import com.ktor.stock.market.game.jbosak.model.ConnectionArguments
import com.ktor.stock.market.game.jbosak.model.Quote
import com.ktor.stock.market.game.jbosak.model.graphql.CompaniesConnectionGraphQL
import com.ktor.stock.market.game.jbosak.model.graphql.CompanyGraphQL
import com.ktor.stock.market.game.jbosak.model.toGraphQL
import com.ktor.stock.market.game.jbosak.repository.CompanyRepository.companiesSize
import com.ktor.stock.market.game.jbosak.service.getCompanies
import com.ktor.stock.market.game.jbosak.service.getCompany
import com.ktor.stock.market.game.jbosak.utils.convertToObject
import graphql.schema.AsyncDataFetcher.async
import graphql.schema.idl.TypeRuntimeWiring
import org.dataloader.BatchLoader
import org.dataloader.DataLoader
import java.util.concurrent.CompletableFuture


fun getCompanySchema() =
    """
    type Company {
        id:Int!
        ticker:String!
        country: String
        currency: String
        exchange: String
        ipo: String
        name: String
        phone: String
        shareOutstanding: Float
        weburl: String
        logo: String
        finnhubIndustry: String
        financials: CompanyFinancials
        quote:Quote
    }
    type CompanyFinancials {
      tenDayAverageTradingVolume:Float
      thirteenWeekPriceReturnDaily:Float
      twentySixWeekPriceReturnDaily:Float
      threeMonthAverageTradingVolume:Float
      fiftyTwoWeekHigh:Float
      fiftyTwoWeekHighDate:String
      fiftyTwoWeekLow:Float
      fiftyTwoWeekLowDate:String
      fiftyTwoWeekPriceReturnDaily:Float
      fiveDayPriceReturnDaily:Float
      assetTurnoverAnnual:Float
      assetTurnoverTTM:Float
      beta:Float
      bookValuePerShareAnnual:Float
      bookValuePerShareQuarterly:Float
      bookValueShareGrowth5Y:Float
      capitalSpendingGrowth5Y:Float
      cashFlowPerShareAnnual:Float
      cashFlowPerShareTTM:Float
      cashPerSharePerShareAnnual:Float
      cashPerSharePerShareQuarterly:Float
      currentDividendYieldTTM:Float
      currentEvOverfreeCashFlowAnnual:Float
      currentEvOverfreeCashFlowTTM:Float
      currentRatioAnnual:Float
      currentRatioQuarterly:Float
      dividendGrowthRate5Y:Float
      dividendPerShare5Y:Float
      dividendPerShareAnnual:Float
      dividendYield5Y:Float
      dividendYieldIndicatedAnnual:Float
      dividendsPerShareTTM:Float
      ebitdPerShareTTM:Float
      ebitdaCagr5Y:Float
      ebitdaInterimCagr5Y:Float
      epsBasicExclExtraItemsAnnual:Float
      epsBasicExclExtraItemsTTM:Float
      epsExclExtraItemsAnnual:Float
      epsExclExtraItemsTTM:Float
      epsGrowth3Y:Float
      epsGrowth5Y:Float
      epsGrowthQuarterlyYoy:Float
      epsGrowthTTMYoy:Float
      epsInclExtraItemsAnnual:Float
      epsInclExtraItemsTTM:Float
      epsNormalizedAnnual:Float
      focfCagr5Y:Float
      freeCashFlowAnnual:Float
      freeCashFlowPerShareTTM:Float
      freeCashFlowTTM:Float
      freeOperatingCashFlowOverRevenue5Y:Float
      freeOperatingCashFlowOverRevenueTTM:Float
      grossMargin5Y:Float
      grossMarginAnnual:Float
      grossMarginTTM:Float
      inventoryTurnoverAnnual:Float
      inventoryTurnoverTTM:Float
      longTermDebtOverEquityAnnual:Float
      longTermDebtOverEquityQuarterly:Float
      marketCapitalization:Float
      monthToDatePriceReturnDaily:Float
      netDebtAnnual:Float
      netDebtInterim:Float
      netIncomeEmployeeAnnual:Float
      netIncomeEmployeeTTM:Float
      netInterestCoverageAnnual:Float
      netInterestCoverageTTM:Float
      netMarginGrowth5Y:Float
      netProfitMargin5Y:Float
      netProfitMarginAnnual:Float
      netProfitMarginTTM:Float
      operatingMargin5Y:Float
      operatingMarginAnnual:Float
      operatingMarginTTM:Float
      payoutRatioAnnual:Float
      payoutRatioTTM:Float
      pbAnnual:Float
      pbQuarterly:Float
      pcfShareTTM:Float
      peBasicExclExtraTTM:Float
      peExclExtraAnnual:Float
      peExclExtraHighTTM:Float
      peExclExtraTTM:Float
      peExclLowTTM:Float
      peInclExtraTTM:Float
      peNormalizedAnnual:Float
      pfcfShareAnnual:Float
      pfcfShareTTM:Float
      pretaxMargin5Y:Float
      pretaxMarginAnnual:Float
      pretaxMarginTTM:Float
      priceRelativeToSAndP50013Week:Float
      priceRelativeToSAndP50026Week:Float
      priceRelativeToSAndP5004Week:Float
      priceRelativeToSAndP50052Week:Float
      priceRelativeToSAndP500Yt:Float
      psAnnual:Float
      psTTM:Float
      ptbvAnnual:Float
      ptbvQuarterly:Float
      quickRatioAnnual:Float
      quickRatioQuarterly:Float
      receivablesTurnoverAnnual:Float
      receivablesTurnoverTTM:Float
      revenueEmployeeAnnual:Float
      revenueEmployeeTTM:Float
      revenueGrowth3Y:Float
      revenueGrowth5Y:Float
      revenueGrowthQuarterlyYoy:Float
      revenueGrowthTTMYoy:Float
      revenuePerShareAnnual:Float
      revenuePerShareTTM:Float
      revenueShareGrowth5Y:Float
      roaRfy:Float
      roaa5Y:Float
      roae5Y:Float
      roaeTTM:Float
      roeRfy:Float
      roeTTM:Float
      roi5Y:Float
      roiAnnual:Float
      roiTTM:Float
      tangibleBookValuePerShareAnnual:Float
      tangibleBookValuePerShareQuarterly:Float
      tbvCagr5Y:Float
      totalDebtOverTotalEquityAnnual:Float
      totalDebtOverTotalEquityQuarterly:Float
      totalDebtCagr5Y:Float
      yearToDatePriceReturnDaily:Float
    }
    
    type CompaniesConnection {
        totalCount:Int
        companies: [Company]!
    }
    """

fun TypeRuntimeWiring.Builder.companyQueryResolvers() =
    this.dataFetcher("companiesConnection", async { env ->
        val (skip, limit) = convertToObject(env.arguments, ConnectionArguments::class.java)!!
        val companies = getCompanies(skip, limit ?: 10)
        val resolvers =
            dataloaderResolver(env)
        val evalCompany = resolvers.resolve<CompanyGraphQL>("companies")
        CompaniesConnectionGraphQL(
            totalCount = companiesSize(),
            companies = companies.map { evalCompany(it.ticker) }
        )
    }).dataFetcher("getCompany", async { env ->
        val ticker = env.arguments["ticker"] as String
        val resolvers =
            dataloaderResolver(env = env,methodName = "getCompany")
        val evalCompany = resolvers.resolve<CompanyGraphQL>("getCompany")
        val company = evalCompany(ticker)
        company
    })

fun companyDataLoader(): DataLoader<DataLoaderKey<Any>, Any>? {
    val loaderById = BatchLoader<DataLoaderKey<Any>, Any> { keys ->
        CompletableFuture.supplyAsync {
            keys.map {
                val company = when (it.key) {
                    is String -> getCompany(ticker = it.key)
                    is Int -> getCompany(id = it.key)
                    else -> null
                } ?: return@map null

                val quote =
                    it.resolve<Quote, Any>("quote")(company.ticker)
                company.toGraphQL(quote)
            }
        }
    }
    return DataLoader.newDataLoader(loaderById)


}