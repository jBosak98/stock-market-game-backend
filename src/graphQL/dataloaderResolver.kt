package com.ktor.stock.market.game.jbosak.graphQL

import graphql.schema.DataFetchingEnvironment
import org.dataloader.DataLoader

fun dataloaderResolver(
    env: DataFetchingEnvironment
): Map<String, DataLoader<Any, Any>> = env
    .selectionSet
    .get()
    .keySet()
    .filterNotNull()
    .map { it to env.getDataLoader<Any, Any>(it) }
    .toMap()
