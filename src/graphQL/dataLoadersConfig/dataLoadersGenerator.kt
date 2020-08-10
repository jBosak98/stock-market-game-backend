package com.ktor.stock.market.game.jbosak.graphQL.dataLoadersConfig

import com.ktor.stock.market.game.jbosak.graphQL.schema.companyDataLoader
import com.ktor.stock.market.game.jbosak.graphQL.schema.quoteDataLoader
import com.ktor.stock.market.game.jbosak.graphQL.schema.stockPriceDataLoader
import com.ktor.stock.market.game.jbosak.model.Company
import com.ktor.stock.market.game.jbosak.model.StockPrice
import org.dataloader.DataLoaderRegistry

fun generateDataLoaders(): DataLoaderRegistry =
    DataLoaderRegistry()
        .register(
            Company::class.java.toString(),
            companyDataLoader()
        )
        .register("company", companyDataLoader())
        .register("companies", companyDataLoader())
        .register(
            "stockPrice",
            stockPriceDataLoader()
        )
        .register(
            StockPrice::class.java.toString(),
            stockPriceDataLoader()
        ).register("quote", quoteDataLoader())
