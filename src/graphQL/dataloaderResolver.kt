package com.ktor.stock.market.game.jbosak.graphQL

import arrow.core.getOrElse
import arrow.core.toOption
import arrow.syntax.function.pipe
import graphql.schema.DataFetchingEnvironment
import org.dataloader.DataLoader

data class DataLoaderKey<T>(
    val key: T,
    val selectedFields: List<String>,
    val env: DataFetchingEnvironment,
    val resolver: Lazy<Map<String, Pair<DataLoader<Any, Any>, (String?) -> DataLoaderKey<String?>>>>
)

fun dataloaderResolver(
    env: DataFetchingEnvironment,
    selectedField: List<String>? = null
): Map<String, Pair<DataLoader<Any, Any>, (String?) -> DataLoaderKey<String?>>> {
    val allSelectedField = selectedField
        .toOption()
        .getOrElse { env.selectionSet.get().keySet().filterNotNull() }

    val splitBySlash = { value: String -> value.split("/") }

    return allSelectedField
        .map { keyPath ->
            val key = splitBySlash(keyPath).last()
            val filterByKey = { selected: List<String> -> selected.contains(key) }
            val extractDataLoaderKeys = { field: List<String> ->
                field.pipe { it.subList(it.indexOf(key) + 1, it.size).joinToString("/") }
            }
            val keySelectedField = allSelectedField
                .map(splitBySlash)
                .filter(filterByKey)
                .map(extractDataLoaderKeys)
            val getResolver
                    = lazy { dataloaderResolver(env, keySelectedField) }

            key to Pair(
                env.getDataLoader<Any, Any>(key),
                { arg: String? -> DataLoaderKey(arg, keySelectedField, env,getResolver) }
            )
        }
        .toMap()
}


