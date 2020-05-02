package com.ktor.stock.market.game.jbosak.service

import com.ktor.stock.market.game.jbosak.BcryptHasher
import com.ktor.stock.market.game.jbosak.JwtConfig
import com.ktor.stock.market.game.jbosak.UserRepository
import com.ktor.stock.market.game.jbosak.model.LoginCredentials
import com.ktor.stock.market.game.jbosak.model.RegistrationDetails
import com.ktor.stock.market.game.jbosak.model.User
import org.mindrot.jbcrypt.BCrypt

object AuthService {
    fun register(details: RegistrationDetails): User = details.let { ( email, password) ->
        val hashed = BcryptHasher.hashPassword(password)
        val id = UserRepository.insert(details.copy(password = hashed))
        return User(id, email, "", "" ).run {
            copy(token = JwtConfig.makeToken(this))
        }
    }

    fun login(credentials: LoginCredentials):User = credentials.let { (email, password) ->
        println(email)
        val user  = UserRepository.findUserByEmail(email)?: throw Exception()
        if(BCrypt.checkpw(password, user.password)){
            val token = JwtConfig.makeToken(user)
            return user.copy(token = token)
        }
        throw Exception()

    }
}