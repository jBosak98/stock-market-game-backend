package com.ktor.stock.market.game.jbosak.repository

import arrow.core.getOrElse
import arrow.core.toOption
import com.finnhub.api.models.Quote
import com.ktor.stock.market.game.jbosak.model.db.Quotes
import com.ktor.stock.market.game.jbosak.utils.getDailyChange
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.statements.InsertStatement
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime

object QuoteRepository {

    fun insert(quote: Quote, companyId:Int) = transaction {
        Quotes.insert(assignQuote(quote, companyId))
    }
    fun insert(quote: Quote, ticker:String) = transaction {
        val companyId = CompanyRepository
            .findCompany(ticker = ticker)
            .getOrElse { throw Exception() }
            .id
        insert(quote, companyId)
    }

    fun findQuote(ticker:String) = transaction {
        val companyId = CompanyRepository
            .findCompany(ticker = ticker)
            .getOrElse { throw Exception() }
            .id
        Quotes
            .select { Quotes.companyId eq companyId }
            .maxBy { it[Quotes.date] }
            .toOption()
            .map(ResultRow::toQuote)
    }

    private fun <T> assignQuote(quote: Quote, companyId: Int):T.(InsertStatement<Number>) -> Unit = {
        it[Quotes.companyId] = companyId
//        it[Quotes.openDayPrice] = quote.o
//        it[Quotes.highDayPrice] = quote.h
//        it[Quotes.lowDayPrice] = quote.l
        it[Quotes.currentPrice] = quote.c
//        it[Quotes.previousClosePrice] = quote.pc
        it[Quotes.dailyChange] = getDailyChange(quote)
        it[Quotes.dailyChangePercentage] = getDailyChange(quote)?.div(quote.pc!!)?.times(100)
        it[Quotes.date] = DateTime.now()
    }
}

