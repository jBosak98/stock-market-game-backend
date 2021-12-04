package com.ktor.stock.market.game.jbosak

import cn.zenliu.ktor.redis.RedisFactory
import com.ktor.stock.market.game.jbosak.server.initDB
import com.ktor.stock.market.game.jbosak.server.initExternalApi
import com.ktor.stock.market.game.jbosak.server.setup
import io.ktor.application.*
import io.ktor.auth.Authentication
import io.ktor.features.CORS
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.features.ForwardedHeaderSupport
import io.ktor.gson.gson
import io.ktor.routing.Routing
import io.ktor.util.KtorExperimentalAPI


fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@KtorExperimentalAPI
@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    globalSecretKey = environment
        .config
        .property("ktor.deployment.secretKey")
        .getString()
    initDB()
    install(CORS) { setup() }

    initExternalApi(finnhubKey, infoProviderHost, "8050")

    install(DefaultHeaders)

    install(ForwardedHeaderSupport)
    install(Authentication) { setup() }

    install(ContentNegotiation) { gson() }
    install(Routing) { setup(environment.config) }

    install(RedisFactory){
        url=getRedisUrl(environment)
    }
}

fun getRedisUrl(environment:ApplicationEnvironment): String {
    val redisPassword = environment
        .config
        .property("ktor.deployment.redisPassword")
        .getString()
    val redisHost = environment
        .config
        .property("ktor.deployment.redisHost")
        .getString()
    val redisPort = environment
        .config
        .property("ktor.deployment.redisPort")
        .getString()
    return "redis://${redisPassword}@${redisHost}:${redisPort}/0?timeout=10s"
}

@KtorExperimentalAPI
val Application.finnhubKey get()
    = environment
        .config
        .property("ktor.deployment.finnhubKey")
        .getString()
@KtorExperimentalAPI

val Application.infoProviderHost get()
    = environment
        .config
        .property("ktor.deployment.infoProviderHost")
        .getString()

var globalSecretKey:String? = null
