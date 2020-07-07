package com.ktor.stock.market.game.jbosak.service

import arrow.core.extensions.list.alternative.orElse
import arrow.syntax.collections.collect
import com.ktor.stock.market.game.jbosak.model.Company
import com.ktor.stock.market.game.jbosak.repository.CompanyRepository
import org.threeten.bp.LocalDate

fun getCompanies(skip:Int, limit:Int): List<Company> {
    val dbCompanies = CompanyRepository.findCompanies(skip, limit)
    return  if(dbCompanies.isNotEmpty()) dbCompanies
            else refetch(skip, limit)
}

private fun refetch(skip:Int, limit:Int): List<Company> {
    val fetched = fetchCompanies(LocalDate.now().minusYears(1))
    if(fetched != null && fetched.companies.isNotEmpty())
        CompanyRepository.insertMany(fetched.companies)
    return CompanyRepository.findCompanies(skip, limit)
}