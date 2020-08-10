package com.ktor.stock.market.game.jbosak.utils

import org.mindrot.jbcrypt.BCrypt

object BcryptHasher {

    fun hashPassword(password:String): String =
        BCrypt.hashpw(password, BCrypt.gensalt())

}