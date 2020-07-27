package com.ktor.stock.market.game.jbosak.server

import io.ktor.features.CORS
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod

fun CORS.Configuration.setup() {
    method(HttpMethod.Options)
    method(HttpMethod.Put)
    method(HttpMethod.Delete)
    method(HttpMethod.Patch)
    header(HttpHeaders.Authorization)
    method(HttpMethod.Options)
    header(HttpHeaders.XForwardedProto)
    HttpHeaders.Accept
    allowSameOrigin
    method(HttpMethod.Options)
    method(HttpMethod.Get)
    header("")
    method(HttpMethod.Post)
    method(HttpMethod.Put)
    method(HttpMethod.Delete)
    method(HttpMethod.Patch)
    header(HttpHeaders.AccessControlAllowHeaders)
    header(HttpHeaders.ContentType)
    header(HttpHeaders.AccessControlAllowOrigin)
    allowCredentials = true
    anyHost()
    header(HttpHeaders.Accept)
//    header("MyCustomHeader")
    allowCredentials = true
    anyHost()
}