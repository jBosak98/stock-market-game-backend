package com.ktor.stock.market.game.jbosak.service

import arrow.core.Validated
import com.ktor.stock.market.game.jbosak.BcryptHasher
import com.ktor.stock.market.game.jbosak.JwtConfig
import com.ktor.stock.market.game.jbosak.repository.UserRepository
import com.ktor.stock.market.game.jbosak.graphQL.ClientGraphQLException
import com.ktor.stock.market.game.jbosak.model.LoginCredentials
import com.ktor.stock.market.game.jbosak.model.RegistrationDetails
import com.ktor.stock.market.game.jbosak.model.User
import org.mindrot.jbcrypt.BCrypt

object AuthService {
    fun register(details: RegistrationDetails): User = details.let { ( email, password) ->
        val hashed = BcryptHasher.hashPassword(password)
        val id = UserRepository.insert(details.copy(password = hashed))
        return User(id, email, "", null)
    }

    fun login(credentials: LoginCredentials): Validated<ClientGraphQLException, User> =
        credentials.let { (email, password) ->
        val user  = UserRepository.findUserByEmail(email)
            ?: return Validated.Invalid(ClientGraphQLException("wrong credentials"))

        if(BCrypt.checkpw(password, user.password)){
            val token = JwtConfig.makeToken(user)
            return Validated.Valid(user.copy(token = token))
        }
        return Validated.Invalid(ClientGraphQLException("wrong credentials"))

    }
}