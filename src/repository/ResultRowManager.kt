package com.ktor.stock.market.game.jbosak.repository


import com.ktor.stock.market.game.jbosak.model.*
import com.ktor.stock.market.game.jbosak.model.db.*
import org.jetbrains.exposed.sql.ResultRow

fun ResultRow.toSingleCandle() = SingleCandle(
    openPrice = this[Candles.openPrice]!!,
    highPrice = this[Candles.highPrice]!!,
    lowPrice = this[Candles.lowPrice]!!,
    closePrice = this[Candles.closePrice]!!,
    volume = this[Candles.volume]!!,
    time = this[Candles.time]!!
)


fun ResultRow.toUser() = User(
    id = this[Users.id],
    email = this[Users.name],
    password = this[Users.password],
    token = ""

)

fun ResultRow.toPlayer(assets:List<Share>) = Player(
    id = this[Players.id],
    money = this[Players.money],
    userId = this[Players.userId],
    startedAt = this[Players.startedAt],
    removedAt = this[Players.removedAt],
    assets = assets
)

fun ResultRow.toTransaction() = Transaction(
    id = this[Transactions.id],
    playerId = this[Transactions.playerId],
    companyId = this[Transactions.companyId],
    pricePerShare = this[Transactions.pricePerShare],
    quantity = this[Transactions.quantity],
    createdAt = this[Transactions.createdAt],
    type = this[Transactions.type]
)

fun ResultRow.toQuote() = Quote(
    id = this[Quotes.id],
    companyId = this[Quotes.companyId],
    openDayPrice = this[Quotes.openDayPrice],
    highDayPrice = this[Quotes.highDayPrice],
    lowDayPrice = this[Quotes.lowDayPrice],
    currentPrice = this[Quotes.currentPrice],
    previousClosePrice = this[Quotes.previousClosePrice],
    dailyChange = this[Quotes.dailyChange],
    dailyChangePercentage = this[Quotes.dailyChangePercentage],
    date = this[Quotes.date]
)

fun ResultRow.toCompany() = Company(
    id = this[Companies.id],
    ticker = this[Companies.ticker],
    country = this[Companies.country],
    currency = this[Companies.currency],
    exchange = this[Companies.exchange],
    ipo = this[Companies.ipo],
    name = this[Companies.name],
    phone = this[Companies.phone],
    shareOutstanding = this[Companies.shareOutstanding],
    weburl = this[Companies.weburl],
    logo = this[Companies.logo],
    finnhubIndustry = this[Companies.finnhubIndustry],
    financials = this.toCompanyFinancials()
)

fun ResultRow.toCompanyFinancials() = CompanyFinancials(
    tenDayAverageTradingVolume = this[Companies.tenDayAverageTradingVolume],
    thirteenWeekPriceReturnDaily = this[Companies.thirteenWeekPriceReturnDaily],
    twentySixWeekPriceReturnDaily = this[Companies.twentySixWeekPriceReturnDaily],
    threeMonthAverageTradingVolume = this[Companies.threeMonthAverageTradingVolume],
    fiftyTwoWeekHigh = this[Companies.fiftyTwoWeekHigh],
    fiftyTwoWeekHighDate = this[Companies.fiftyTwoWeekHighDate],
    fiftyTwoWeekLow = this[Companies.fiftyTwoWeekLow],
    fiftyTwoWeekLowDate = this[Companies.fiftyTwoWeekLowDate],
    fiftyTwoWeekPriceReturnDaily = this[Companies.fiftyTwoWeekPriceReturnDaily],
    fiveDayPriceReturnDaily = this[Companies.fiveDayPriceReturnDaily],
    assetTurnoverAnnual = this[Companies.assetTurnoverAnnual],
    assetTurnoverTTM = this[Companies.assetTurnoverTTM],
    beta = this[Companies.beta],
    bookValuePerShareAnnual = this[Companies.bookValuePerShareAnnual],
    bookValuePerShareQuarterly = this[Companies.bookValuePerShareQuarterly],
    bookValueShareGrowth5Y = this[Companies.bookValueShareGrowth5Y],
    capitalSpendingGrowth5Y = this[Companies.capitalSpendingGrowth5Y],
    cashFlowPerShareAnnual = this[Companies.cashFlowPerShareAnnual],
    cashFlowPerShareTTM = this[Companies.cashFlowPerShareTTM],
    cashPerSharePerShareAnnual = this[Companies.cashPerSharePerShareAnnual],
    cashPerSharePerShareQuarterly = this[Companies.cashPerSharePerShareQuarterly],
    currentDividendYieldTTM = this[Companies.currentDividendYieldTTM],
    currentEvOverfreeCashFlowAnnual = this[Companies.currentEvOverfreeCashFlowAnnual],
    currentEvOverfreeCashFlowTTM = this[Companies.currentEvOverfreeCashFlowTTM],
    currentRatioAnnual = this[Companies.currentRatioAnnual],
    currentRatioQuarterly = this[Companies.currentRatioQuarterly],
    dividendGrowthRate5Y = this[Companies.dividendGrowthRate5Y],
    dividendPerShare5Y = this[Companies.dividendPerShare5Y],
    dividendPerShareAnnual = this[Companies.dividendPerShareAnnual],
    dividendYield5Y = this[Companies.dividendYield5Y],
    dividendYieldIndicatedAnnual = this[Companies.dividendYieldIndicatedAnnual],
    dividendsPerShareTTM = this[Companies.dividendsPerShareTTM],
    ebitdPerShareTTM = this[Companies.ebitdPerShareTTM],
    ebitdaCagr5Y = this[Companies.ebitdaCagr5Y],
    ebitdaInterimCagr5Y = this[Companies.ebitdaInterimCagr5Y],
    epsBasicExclExtraItemsAnnual = this[Companies.epsBasicExclExtraItemsAnnual],
    epsBasicExclExtraItemsTTM = this[Companies.epsBasicExclExtraItemsTTM],
    epsExclExtraItemsAnnual = this[Companies.epsExclExtraItemsAnnual],
    epsExclExtraItemsTTM = this[Companies.epsExclExtraItemsTTM],
    epsGrowth3Y = this[Companies.epsGrowth3Y],
    epsGrowth5Y = this[Companies.epsGrowth5Y],
    epsGrowthQuarterlyYoy = this[Companies.epsGrowthQuarterlyYoy],
    epsGrowthTTMYoy = this[Companies.epsGrowthTTMYoy],
    epsInclExtraItemsAnnual = this[Companies.epsInclExtraItemsAnnual],
    epsInclExtraItemsTTM = this[Companies.epsInclExtraItemsTTM],
    epsNormalizedAnnual = this[Companies.epsNormalizedAnnual],
    focfCagr5Y = this[Companies.focfCagr5Y],
    freeCashFlowAnnual = this[Companies.freeCashFlowAnnual],
    freeCashFlowPerShareTTM = this[Companies.freeCashFlowPerShareTTM],
    freeCashFlowTTM = this[Companies.freeCashFlowTTM],
    freeOperatingCashFlowOverRevenue5Y = this[Companies.freeOperatingCashFlowOverRevenue5Y],
    freeOperatingCashFlowOverRevenueTTM = this[Companies.freeOperatingCashFlowOverRevenueTTM],
    grossMargin5Y = this[Companies.grossMargin5Y],
    grossMarginAnnual = this[Companies.grossMarginAnnual],
    grossMarginTTM = this[Companies.grossMarginTTM],
    inventoryTurnoverAnnual = this[Companies.inventoryTurnoverAnnual],
    inventoryTurnoverTTM = this[Companies.inventoryTurnoverTTM],
    longTermDebtOverEquityAnnual = this[Companies.longTermDebtOverEquityAnnual],
    longTermDebtOverEquityQuarterly = this[Companies.longTermDebtOverEquityQuarterly],
    marketCapitalization = this[Companies.marketCapitalization],
    monthToDatePriceReturnDaily = this[Companies.monthToDatePriceReturnDaily],
    netDebtAnnual = this[Companies.netDebtAnnual],
    netDebtInterim = this[Companies.netDebtInterim],
    netIncomeEmployeeAnnual = this[Companies.netIncomeEmployeeAnnual],
    netIncomeEmployeeTTM = this[Companies.netIncomeEmployeeTTM],
    netInterestCoverageAnnual = this[Companies.netInterestCoverageAnnual],
    netInterestCoverageTTM = this[Companies.netInterestCoverageTTM],
    netMarginGrowth5Y = this[Companies.netMarginGrowth5Y],
    netProfitMargin5Y = this[Companies.netProfitMargin5Y],
    netProfitMarginAnnual = this[Companies.netProfitMarginAnnual],
    netProfitMarginTTM = this[Companies.netProfitMarginTTM],
    operatingMargin5Y = this[Companies.operatingMargin5Y],
    operatingMarginAnnual = this[Companies.operatingMarginAnnual],
    operatingMarginTTM = this[Companies.operatingMarginTTM],
    payoutRatioAnnual = this[Companies.payoutRatioAnnual],
    payoutRatioTTM = this[Companies.payoutRatioTTM],
    pbAnnual = this[Companies.pbAnnual],
    pbQuarterly = this[Companies.pbQuarterly],
    pcfShareTTM = this[Companies.pcfShareTTM],
    peBasicExclExtraTTM = this[Companies.peBasicExclExtraTTM],
    peExclExtraAnnual = this[Companies.peExclExtraAnnual],
    peExclExtraHighTTM = this[Companies.peExclExtraHighTTM],
    peExclExtraTTM = this[Companies.peExclExtraTTM],
    peExclLowTTM = this[Companies.peExclLowTTM],
    peInclExtraTTM = this[Companies.peInclExtraTTM],
    peNormalizedAnnual = this[Companies.peNormalizedAnnual],
    pfcfShareAnnual = this[Companies.pfcfShareAnnual],
    pfcfShareTTM = this[Companies.pfcfShareTTM],
    pretaxMargin5Y = this[Companies.pretaxMargin5Y],
    pretaxMarginAnnual = this[Companies.pretaxMarginAnnual],
    pretaxMarginTTM = this[Companies.pretaxMarginTTM],
    priceRelativeToSAndP50013Week = this[Companies.priceRelativeToSAndP50013Week],
    priceRelativeToSAndP50026Week = this[Companies.priceRelativeToSAndP50026Week],
    priceRelativeToSAndP5004Week = this[Companies.priceRelativeToSAndP5004Week],
    priceRelativeToSAndP50052Week = this[Companies.priceRelativeToSAndP50052Week],
    priceRelativeToSAndP500Yt = this[Companies.priceRelativeToSAndP500Yt],
    psAnnual = this[Companies.psAnnual],
    psTTM = this[Companies.psTTM],
    ptbvAnnual = this[Companies.ptbvAnnual],
    ptbvQuarterly = this[Companies.ptbvQuarterly],
    quickRatioAnnual = this[Companies.quickRatioAnnual],
    quickRatioQuarterly = this[Companies.quickRatioQuarterly],
    receivablesTurnoverAnnual = this[Companies.receivablesTurnoverAnnual],
    receivablesTurnoverTTM = this[Companies.receivablesTurnoverTTM],
    revenueEmployeeAnnual = this[Companies.revenueEmployeeAnnual],
    revenueEmployeeTTM = this[Companies.revenueEmployeeTTM],
    revenueGrowth3Y = this[Companies.revenueGrowth3Y],
    revenueGrowth5Y = this[Companies.revenueGrowth5Y],
    revenueGrowthQuarterlyYoy = this[Companies.revenueGrowthQuarterlyYoy],
    revenueGrowthTTMYoy = this[Companies.revenueGrowthTTMYoy],
    revenuePerShareAnnual = this[Companies.revenuePerShareAnnual],
    revenuePerShareTTM = this[Companies.revenuePerShareTTM],
    revenueShareGrowth5Y = this[Companies.revenueShareGrowth5Y],
    roaRfy = this[Companies.roaRfy],
    roaa5Y = this[Companies.roaa5Y],
    roae5Y = this[Companies.roae5Y],
    roaeTTM = this[Companies.roaeTTM],
    roeRfy = this[Companies.roeRfy],
    roeTTM = this[Companies.roeTTM],
    roi5Y = this[Companies.roi5Y],
    roiAnnual = this[Companies.roiAnnual],
    roiTTM = this[Companies.roiTTM],
    tangibleBookValuePerShareAnnual = this[Companies.tangibleBookValuePerShareAnnual],
    tangibleBookValuePerShareQuarterly = this[Companies.tangibleBookValuePerShareQuarterly],
    tbvCagr5Y = this[Companies.tbvCagr5Y],
    totalDebtOverTotalEquityAnnual = this[Companies.totalDebtOverTotalEquityAnnual],
    totalDebtOverTotalEquityQuarterly = this[Companies.totalDebtOverTotalEquityQuarterly],
    totalDebtCagr5Y = this[Companies.totalDebtCagr5Y],
    yearToDatePriceReturnDaily = this[Companies.yearToDatePriceReturnDaily]
)
