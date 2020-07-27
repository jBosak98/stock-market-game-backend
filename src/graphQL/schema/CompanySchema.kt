package com.ktor.stock.market.game.jbosak.graphQL.schema

import com.ktor.stock.market.game.jbosak.graphQL.dataLoadersConfig.DataLoaderKey
import com.ktor.stock.market.game.jbosak.graphQL.dataLoadersConfig.dataloaderResolver
import com.ktor.stock.market.game.jbosak.graphQL.dataLoadersConfig.resolve
import com.ktor.stock.market.game.jbosak.model.ConnectionArguments
import com.ktor.stock.market.game.jbosak.model.graphql.CompaniesConnectionGraphQL
import com.ktor.stock.market.game.jbosak.model.graphql.CompanyGraphQL
import com.ktor.stock.market.game.jbosak.model.graphql.StockPriceGraphQL
import com.ktor.stock.market.game.jbosak.model.toGraphQL
import com.ktor.stock.market.game.jbosak.repository.CompanyRepository.companiesSize
import com.ktor.stock.market.game.jbosak.service.getCompanies
import com.ktor.stock.market.game.jbosak.service.getCompany
import com.ktor.stock.market.game.jbosak.utils.convertToObject
import graphql.schema.AsyncDataFetcher.async
import graphql.schema.idl.TypeRuntimeWiring
import org.dataloader.BatchLoader
import org.dataloader.DataLoader
import java.util.concurrent.CompletableFuture


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
        val companies = getCompanies(skip, limit ?: 10)
        val resolvers =
            dataloaderResolver(env)
        val evalCompany = resolvers.resolve<CompanyGraphQL>("companies")

        CompaniesConnectionGraphQL(
            totalCount = companiesSize(),
            companies = companies.map { evalCompany(it.ticker) }
        )
    })

fun companyDataLoader(): DataLoader<DataLoaderKey<Any>, Any>? {
    val loaderById = BatchLoader<DataLoaderKey<Any>, Any> { keys ->
        CompletableFuture.supplyAsync {
            keys.map {
                val company = when (it.key) {
                    is String -> getCompany(ticker = it.key)
                    is Int -> getCompany(id = it.key)
                    else -> null
                } ?: return@map null

                val stockPrice =
                    it.resolve<StockPriceGraphQL, Any>("stockPrice")(company.ticker)
                company.toGraphQL(stockPrice)
            }
        }
    }
    return DataLoader.newDataLoader(loaderById)


}