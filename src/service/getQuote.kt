package com.ktor.stock.market.game.jbosak.service

import arrow.core.Option
import arrow.core.Validated
import com.finnhub.api.apis.DefaultApi
import com.ktor.stock.market.game.jbosak.graphQL.ClientGraphQLException
import com.ktor.stock.market.game.jbosak.model.Quote
import com.ktor.stock.market.game.jbosak.repository.QuoteRepository
import com.ktor.stock.market.game.jbosak.utils.isInLast5Minutes


fun getQuote(ticker:String): Validated<ClientGraphQLException,Quote> {
    ensureCompanyExists(ticker)?.let { return Validated.Invalid(it) }
    val quote = QuoteRepository
        .findQuote(ticker).orNull()

    if(quote != null && quote.date.isInLast5Minutes())
        return Validated.Valid(quote)

    return Validated.fromOption(refetch(ticker)) {
        ClientGraphQLException("Cannot find the qoute")
    }


}

private fun refetch(ticker:String): Option<Quote> {
     DefaultApi()
        .quote(ticker)
        .let { QuoteRepository.insert(it, ticker) }
    return QuoteRepository.findQuote(ticker)
}