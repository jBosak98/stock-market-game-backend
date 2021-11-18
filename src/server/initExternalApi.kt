package com.ktor.stock.market.game.jbosak.server

import arrow.core.toOption
import com.finnhub.api.infrastructure.ApiClient
import com.ktor.stock.market.game.jbosak.model.Company
import com.ktor.stock.market.game.jbosak.service.getCompanies
import com.ktor.stock.market.game.jbosak.service.refetchCompanyInfo
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private fun refetchDelayed(companies:List<Company>, condition:(Company)-> Boolean, refetch:suspend (Company)-> Unit){
    companies.mapIndexed {index, it ->
        if(condition(it))
            GlobalScope.launch {
                delay((index * 1000).toLong())
                refetch(it)
            }
    }
}
fun initExternalApi(finnhubKey:String, host: String, port:String)  {
    ApiClient.apiKey["token"] = finnhubKey

    val companies = getCompanies(0,150)

    refetchDelayed(
        companies = companies,
        condition = { it.businessSummary.toOption().isEmpty() },
        refetch = { refetchCompanyInfo(it.ticker, host, port) }
    )

}