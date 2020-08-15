package com.ktor.stock.market.game.jbosak.server

import arrow.core.toOption
import com.finnhub.api.infrastructure.ApiClient
import com.ktor.stock.market.game.jbosak.model.Company
import com.ktor.stock.market.game.jbosak.service.getCompanies
import com.ktor.stock.market.game.jbosak.service.refetchCompanyFinancials
import com.ktor.stock.market.game.jbosak.service.refetchCompanyProfile
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private fun refetchDelayed(companies:List<Company>, condition:(Company)-> Boolean, refetch:(Company)-> Unit){
    companies.mapIndexed {index, it ->
        if(condition(it))
            GlobalScope.launch {
                delay((index * 1000).toLong())
                refetch(it)
            }
    }

}
fun initExternalApi(finnhubKey:String)  {
    ApiClient.apiKey["token"] = finnhubKey

    val companies = getCompanies(0,15)
    refetchDelayed(
        companies = companies,
        condition = { it.financials.tenDayAverageTradingVolume.toOption().isEmpty() },
        refetch = { refetchCompanyFinancials(it.ticker) }
    )
    refetchDelayed(
        companies = companies,
        condition = { it.currency.toOption().isEmpty() },
        refetch = { refetchCompanyProfile(it.ticker) }
    )
}