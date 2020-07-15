package com.ktor.stock.market.game.jbosak.utils

import com.google.gson.Gson

fun <T> convertToObject(args:Map<String, Any>, type:Class<T>): T? {
    val gson = Gson()
    val jsonTree = gson.toJsonTree(args)
    return gson.fromJson(jsonTree, type)
}