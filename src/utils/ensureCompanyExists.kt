package com.ktor.stock.market.game.jbosak.utils

import com.ktor.stock.market.game.jbosak.graphQL.ClientGraphQLException
import com.ktor.stock.market.game.jbosak.repository.CompanyRepository

fun ensureCompanyExists(ticker:String): ClientGraphQLException? =
    if(CompanyRepository.findCompany(ticker = ticker).isEmpty())
        ClientGraphQLException("The security does not exist")
    else
        null