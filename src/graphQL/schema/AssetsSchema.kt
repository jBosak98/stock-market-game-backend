package com.ktor.stock.market.game.jbosak.graphQL.schema

import arrow.core.getOrElse
import arrow.core.toOption
import arrow.core.valueOr
import com.ktor.stock.market.game.jbosak.graphQL.ClientGraphQLException
import com.ktor.stock.market.game.jbosak.graphQL.dataLoadersConfig.DataLoaderKey
import com.ktor.stock.market.game.jbosak.graphQL.dataLoadersConfig.resolve
import com.ktor.stock.market.game.jbosak.model.graphql.AssetsGraphQL
import com.ktor.stock.market.game.jbosak.model.graphql.CompanyGraphQL
import com.ktor.stock.market.game.jbosak.model.graphql.ShareGraphQL
import com.ktor.stock.market.game.jbosak.model.toGraphQL
import com.ktor.stock.market.game.jbosak.repository.PlayerRepository
import com.ktor.stock.market.game.jbosak.service.getCompany
import com.ktor.stock.market.game.jbosak.service.getQuote
import org.dataloader.BatchLoader
import org.dataloader.DataLoader
import java.util.concurrent.CompletableFuture

private fun handleCompany(dataLoaderKey: DataLoaderKey<*>): (Int) -> CompanyGraphQL? {
    val companyLoader = (dataLoaderKey as DataLoaderKey<Any>)
        .resolve<CompanyGraphQL, Any>("company")
    return { companyId:Int ->
        companyLoader(companyId)
            .let { company ->
                if(company?.quote.toOption().isDefined()) company
                else {
                    val simpleCompany = getCompany(id = companyId)
                    val quote = simpleCompany?.ticker?.let { it1 ->
                        getQuote(it1)
                            .valueOr { throwable -> throw throwable }
                    }
                    simpleCompany?.toGraphQL(quote)
                }
            }
    }
}
private fun sumAssetsValue(assets:List<ShareGraphQL>) =
    assets.map { share ->
        share
            .company
            ?.quote
            ?.currentPrice
            ?.let { it1 -> share.amount.toFloat().times(it1) }
            ?:0f
    }.sum()


fun assetsDataLoader(): DataLoader<DataLoaderKey<Int>, AssetsGraphQL> {
    val loader = BatchLoader<DataLoaderKey<Int>, AssetsGraphQL> { keys ->
        CompletableFuture.supplyAsync {
            keys.map {
                val player = PlayerRepository
                    .findPlayer(it.key)
                    .getOrElse { throw ClientGraphQLException("Player not found") }
                val evalCompany = handleCompany(it)
                val assets = player.assets.map { share ->
                    val company = evalCompany(share.companyId)
                    ShareGraphQL(share.companyId, company, share.amount)
                }
                val accountValue = player.money + sumAssetsValue(assets)
                AssetsGraphQL(
                    player.money,
                    assets,
                    accountValue
                )
            }
        }

    }
    return DataLoader.newDataLoader(loader)
}