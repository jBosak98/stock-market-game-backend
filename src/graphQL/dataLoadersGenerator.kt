package com.ktor.stock.market.game.jbosak.graphQL

import com.ktor.stock.market.game.jbosak.model.Company
import com.ktor.stock.market.game.jbosak.model.StockPrice
import org.dataloader.DataLoaderRegistry

fun generateDataLoaders(): DataLoaderRegistry =
     DataLoaderRegistry()
         .register(Company::class.java.toString(),companyDataLoader())
         .register("company",companyDataLoader())
         .register("stockPrice", stockPriceDataLoader())
         .register(StockPrice::class.java.toString(), stockPriceDataLoader())
