package com.ktor.stock.market.game.jbosak.service

import com.rabbitmq.client.ConnectionFactory

val rabbitConnectionFactory = ConnectionFactory().apply {
    this.host = "localhost"
    this.port = 5672
    this.virtualHost = "/"
    this.username = "guest"
    this.password = "guest"
}