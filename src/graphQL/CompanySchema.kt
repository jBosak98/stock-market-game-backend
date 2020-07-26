package com.ktor.stock.market.game.jbosak.graphQL

import arrow.syntax.function.pipe
import com.ktor.stock.market.game.jbosak.model.ConnectionArguments
import com.ktor.stock.market.game.jbosak.repository.CompanyRepository.companiesSize
import com.ktor.stock.market.game.jbosak.service.getCompanies
import com.ktor.stock.market.game.jbosak.service.getCompany
import com.ktor.stock.market.game.jbosak.utils.convertToObject
import graphql.schema.AsyncDataFetcher.async
import graphql.schema.idl.TypeRuntimeWiring
import org.dataloader.BatchLoader
import org.dataloader.DataLoader
import java.util.concurrent.CompletableFuture

data class CompanyGraphQL(
    val id:Int,
    val ticker:String,
    val name:String,
    val lei:String?,
    val cik:String,
    val externalId:String,
    val stockPrice: Any?
)
fun getCompanySchema() =
    """
    type Company {
        id:Int!
        ticker:String
        name:String
        lei:String
        cik:String
        externalId:String
        stockPrice: StockPrice
    }
    
    type CompaniesConnection {
        totalCount:Int
        companies: [Company]!
    }
    """

fun TypeRuntimeWiring.Builder.companyQueryResolvers() =
    this.dataFetcher("companiesConnection", async { env ->
        val (skip, limit) = convertToObject(env.arguments, ConnectionArguments::class.java)!!
        val companies = getCompanies(skip,limit?: 10)

        val (companyResolver, companyKeyGenerator)
                = dataloaderResolver(env).getOrElse("company") { Pair(null, null) }
        val evalCompany = { ticker: String ->
            companyResolver?.run {
                val value = companyKeyGenerator?.invoke(ticker).pipe { this.load(it) }
                dispatch()
                value.get()
            }
        }
        object {
            val totalCount = companiesSize()
            val companies = companies.map {
                evalCompany(it.ticker)
            }
        }
    })

fun companyDataLoader(): DataLoader<DataLoaderKey<Any>, Any>? {
    val loaderById = BatchLoader<DataLoaderKey<Any>, Any> { keys ->
        CompletableFuture.supplyAsync {
            keys.map {
               val company = when (it.key) {
                    is String -> getCompany(ticker = it.key)
                    is Int -> getCompany(id = it.key)
                    else -> null
                }
                val (stockPriceResolver, stockPriceKeyGenerator)
                        = it.resolver.value.getOrElse("stockPrice") { Pair(null, null) }
                val stockPrice = stockPriceResolver?.run {
                    val value = stockPriceKeyGenerator?.invoke(company?.ticker).pipe { this.load(it) }
                    dispatch()
                    value.get()
                }

                CompanyGraphQL(
                    id = company!!.id,
                    ticker = company.ticker,
                    cik = company.cik,
                    externalId = company.externalId,
                    lei = company.lei,
                    name = company.name,
                    stockPrice = stockPrice
                )
            }
        }
    }
    return DataLoader.newDataLoader(loaderById)


}