package com.ktor.stock.market.game.jbosak.repository

import com.intrinio.models.CompanySummary
import com.ktor.stock.market.game.jbosak.model.db.Companies
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.statements.InsertStatement
import org.jetbrains.exposed.sql.transactions.transaction

object CompanyRepository {

    private fun <T>assignCompany(company:CompanySummary): T.(InsertStatement<Number>)-> Unit = {
        it[Companies.externalId] = company.id
        it[Companies.ticker] = company.ticker
        it[Companies.name] = company.name
        it[Companies.lei] = company.lei
        it[Companies.cik] = company.cik
    }

    fun insert(company:CompanySummary) = transaction {
        Companies.insert(assignCompany(company)) get Companies.id
    }
    fun insertMany(companies:List<CompanySummary>) = transaction {
        companies.map { company ->
            Companies.insert(assignCompany(company)) get Companies.id
        }
    }

    fun findCompany(id:Int? = null, externalId:String? = null, ticker: String? = null) = transaction {
        val comparator: SqlExpressionBuilder.()-> Op<Boolean> =
            when {
                id != null -> {{ Companies.id eq id }}
                externalId != null -> {{ Companies.externalId eq externalId }}
                ticker != null -> {{ Companies.ticker eq ticker }}
                else -> return@transaction null
            }
        Companies
            .select(comparator)
            .singleOrNull()
            ?.let(ResultRow::toCompany)
    }

    fun companiesSize() = transaction { Companies.selectAll().count() }

    fun findCompanies(skip:Int, limit:Int) = transaction {
        Companies
            .selectAll()
            .limit(limit, skip)
            .map(ResultRow::toCompany)
    }


    fun <T,V> twoOptionsComparator(opt1:T?,column1:Column<T>, opt2:V?, column2:Column<V>)
            : (() -> Op<Boolean>)? =
        when {
            opt1 != null -> {{ column1 eq opt1 }}
            opt2 != null -> {{ column2 eq opt2 }}
            else -> null
        }


}