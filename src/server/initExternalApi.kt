package com.ktor.stock.market.game.jbosak.server

import com.intrinio.invoker.Configuration
import com.intrinio.invoker.auth.ApiKeyAuth

fun initExternalApi(){
    val client = Configuration.getDefaultApiClient()
    val auth = client.getAuthentication("ApiKeyAuth") as ApiKeyAuth
    auth.apiKey = "OjdkYjE0NmNmM2YzZWM5ZDFlMDQ5YzlmMmFkZTU2MDFk"
}