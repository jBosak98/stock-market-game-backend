package com.ktor.stock.market.game.jbosak.graphQL.dataLoadersConfig

import arrow.core.getOrElse
import arrow.core.toOption
import arrow.syntax.function.pipe
import graphql.schema.DataFetchingEnvironment
import org.dataloader.DataLoader

data class DataLoaderKey<T>(
    val key: T,
    val selectedFields: List<String>,
    val env: DataFetchingEnvironment,
    val resolver: Lazy<Map<String, Pair<DataLoader<Any, Any>, (Any?) -> DataLoaderKey<Any?>>>>
)
typealias DataLoaderResolver = Map<String, Pair<DataLoader<Any, Any>, (Any?) -> DataLoaderKey<Any?>>>

inline fun <reified T> DataLoaderResolver.resolve(resolverName: String): (Any) -> T? {
    val (valueResolver, keyGenerator)
            = this.getOrElse(resolverName) { return { null } }
    return { key: Any ->
        val result = keyGenerator.invoke(key).pipe { valueResolver.load(it) }
        valueResolver.dispatch()
        result?.get() as T?
    }
}

inline fun <reified T, K> DataLoaderKey<K>.resolve(resolverName: String): (Any) -> T? =
    this.resolver.value.resolve<T>(resolverName)

fun dataloaderResolver(
    env: DataFetchingEnvironment,
    selectedField: List<String>? = null,
    methodName: String = ""
): Map<String, Pair<DataLoader<Any, Any>, (Any?) -> DataLoaderKey<Any?>>> {
    val allSelectedField = selectedField
        .toOption()
        .getOrElse { env.selectionSet.get().keySet().filterNotNull() }
        .map { if(methodName != "") "$methodName/$it" else it } + methodName
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
            val getResolver = lazy {
                dataloaderResolver(
                    env,
                    keySelectedField
                )
            }
            key to Pair(
                env.getDataLoader<Any, Any>(key),
                { arg: Any? ->
                    DataLoaderKey(
                        arg,
                        keySelectedField,
                        env,
                        getResolver
                    )
                }
            )
        }
        .toMap()
}


