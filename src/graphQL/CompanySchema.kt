package com.ktor.stock.market.game.jbosak.graphQL

import arrow.core.getOrElse
import arrow.core.toOption
import arrow.syntax.function.pipe
import com.ktor.stock.market.game.jbosak.model.ConnectionArguments
import com.ktor.stock.market.game.jbosak.repository.CompanyRepository.companiesSize
import com.ktor.stock.market.game.jbosak.service.getCompanies
import com.ktor.stock.market.game.jbosak.service.getCompany
import com.ktor.stock.market.game.jbosak.utils.convertToObject
import graphql.schema.AsyncDataFetcher.async
import graphql.schema.DataFetchingEnvironment
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


fun getSelectedFieldsByKey(env: DataFetchingEnvironment, allSelectedField:List<String>?): Map<String, (String?) -> DataLoaderKey<String?>> {
    val selectedFields =allSelectedField.toOption().getOrElse { env
        .selectionSet
        .get()
        .keySet()
        .filterNotNull() }
    return selectedFields.map { keyPath ->
        val key = keyPath.split("/").last()
        val keySelectedField = selectedFields
            .map {field -> field.split("/") }
            .filter { selected -> selected.contains(key) }
            .map { field ->
                field.pipe {
                    it.subList(it.indexOf(key) + 1, it.size).joinToString("/")
                }

            };
            key to { arg:String? -> DataLoaderKey(arg, keySelectedField,env)}
    }.toMap()

}

fun TypeRuntimeWiring.Builder.companyQueryResolvers() =
    this.dataFetcher("companiesConnection", async { env ->
        val (skip, limit) = convertToObject(env.arguments, ConnectionArguments::class.java)!!
        val resolvedDataLoaders = dataloaderResolver(env)
        val companies = getCompanies(skip,limit?: 10)
        val allSelectedField = env
            .selectionSet
            .get()
            .keySet()
            .filterNotNull()
        val dataLoadersArgs = getSelectedFieldsByKey(env,allSelectedField)
        val stockPriceArg = dataLoadersArgs["stockPrice"]?.invoke(companies.firstOrNull()?.ticker)
        val stockPriceResolver = resolvedDataLoaders["stockPrice"]
        val stockPrice = stockPriceResolver?.load(stockPriceArg)
        stockPriceResolver?.dispatch()
        val stockPriceValue = stockPrice?.get()
        object {
            val totalCount = companiesSize()
            val companies = companies.map {
                CompanyGraphQL(
                    id = it.id,
                    ticker = it.ticker,
                    cik = it.cik,
                    externalId = it.externalId,
                    lei = it.lei,
                    name = it.name,
                    stockPrice = stockPriceValue
                )
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
                val resolvedDataLoaders = dataloaderResolver(it.env, it.selectedFields)
                val dataLoadersArgs = getSelectedFieldsByKey(it.env, it.selectedFields)
                val stockPriceArg = dataLoadersArgs["stockPrice"]?.invoke(company?.ticker)
                val stockPriceLoader = resolvedDataLoaders["stockPrice"]
                val stockPrice = stockPriceLoader?.load(stockPriceArg)
                stockPriceLoader?.dispatch()

                CompanyGraphQL(
                    id = company!!.id,
                    ticker = company.ticker,
                    cik = company.cik,
                    externalId = company.externalId,
                    lei = company.lei,
                    name = company.name,
                    stockPrice = stockPrice?.get()
                )
            }
        }
    }
    val companiesDataLoader = DataLoader.newDataLoader(loaderById)
    return companiesDataLoader

}