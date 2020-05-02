package com.ktor.stock.market.game.jbosak.model

import java.security.Principal
import java.util.*

data class User (
    val id:Int,
    val email:String,
    val password:String
): Principal {
    override fun getName(): String = email
}