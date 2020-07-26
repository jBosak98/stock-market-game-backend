package com.ktor.stock.market.game.jbosak.graphQL

import arrow.core.getOrElse
import arrow.core.toOption
import arrow.syntax.function.pipe
import graphql.schema.DataFetchingEnvironment
import org.dataloader.DataLoader

data class DataLoaderKey<T>(
    val key:T,
    val selectedFields:List<String>,
    val env:DataFetchingEnvironment
)

fun dataloaderResolver(
    env: DataFetchingEnvironment,
    selectedField:List<String>? = null
): Map<String, DataLoader<Any, Any>> {
    val allSelectedField = selectedField
        .toOption()
        .getOrElse {
            env
                .selectionSet
                .get()
                .keySet()
                .filterNotNull()
        }

   return allSelectedField
        .map { keyPath ->
            val key = keyPath.split("/").last()
            val keySelectedField = allSelectedField
                .map {field -> field.split("/") }
                .filter { selected -> selected.contains(key) }
                .map { field ->
                    field.pipe {
                        it.subList(it.indexOf(key) + 1, it.size).joinToString("/")
                    }
                }
            key to env.getDataLoader<Any, Any>(key)
        }
        .toMap()
}