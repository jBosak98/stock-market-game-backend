package com.ktor.stock.market.game.jbosak.repository

import arrow.core.getOrElse
import arrow.core.toOption
import com.ktor.stock.market.game.jbosak.model.CandlesContainer
import com.ktor.stock.market.game.jbosak.model.CandlesResolution
import com.ktor.stock.market.game.jbosak.model.SingleCandle
import com.ktor.stock.market.game.jbosak.model.db.Candles
import com.ktor.stock.market.game.jbosak.model.toSingleCandles
import com.ktor.stock.market.game.jbosak.utils.uniqueIndexR
import com.ktor.stock.market.game.jbosak.utils.upsert
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.statements.InsertStatement
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime


object CandleRepository {
    fun upsert(candles: CandlesContainer, ticker:String, resolution:CandlesResolution) = transaction {
        val companyId = CompanyRepository
            .findCompany(ticker = ticker)
            .getOrElse { throw Exception("Company not found") }
            .id
        val conflictIndex = Candles.uniqueIndexR("uniqueCandle",
            Candles.companyId,
            Candles.time,
            Candles.resolution
        )
        candles
            .toSingleCandles()
            .map {
                Candles.upsert( conflictIndex = conflictIndex, body = assignCandle(it, resolution, companyId))
            }

    }

    fun find(ticker:String, resolution:CandlesResolution, from:DateTime, to:DateTime? = null) = transaction {
        val timeUpperLimit = to ?: DateTime.now()
        val companyId = CompanyRepository
            .findCompany(ticker = ticker)
            .getOrElse { throw Exception("Company not found") }
            .id
        Candles
            .select { (Candles.companyId eq companyId) and
                        (Candles.resolution eq resolution.resolution) and
                        (Candles.time greaterEq from) and (Candles.time.lessEq(timeUpperLimit))
            }
            .orderBy(Candles.time)
            .filter {
                it[Candles.openPrice].toOption().isDefined() &&
                it[Candles.highPrice].toOption().isDefined() &&
                it[Candles.lowPrice].toOption().isDefined() &&
                it[Candles.closePrice].toOption().isDefined() &&
                it[Candles.volume].toOption().isDefined() &&
                it[Candles.time].toOption().isDefined()
            }
            .map(ResultRow::toSingleCandle)


    }

    private fun <T> assignCandle(
        candle: SingleCandle,
        resolution:CandlesResolution,
        companyId:Int
    ): T.(InsertStatement<Number>) -> Unit = {
        it[Candles.openPrice] = candle.openPrice
        it[Candles.highPrice] = candle.highPrice
        it[Candles.lowPrice] = candle.lowPrice
        it[Candles.closePrice] = candle.closePrice
        it[Candles.volume] = candle.volume
        it[Candles.time] = candle.time
        it[Candles.resolution] = resolution.resolution
        it[Candles.companyId] = companyId
    }


}