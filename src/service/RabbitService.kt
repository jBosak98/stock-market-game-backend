package com.ktor.stock.market.game.jbosak.service

import com.rabbitmq.client.ConnectionFactory

val rabbitConnectionFactory = { rabbitHost:String ->
    ConnectionFactory().apply {
        this.host = rabbitHost
        this.port = 5672
        this.virtualHost = "/"
        this.username = "guest"
        this.password = "guest"
    }
}