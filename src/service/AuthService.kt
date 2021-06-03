package com.ktor.stock.market.game.jbosak.service

import arrow.core.Validated
import com.ktor.stock.market.game.jbosak.graphQL.ClientGraphQLException
import com.ktor.stock.market.game.jbosak.model.LoginCredentials
import com.ktor.stock.market.game.jbosak.model.RegistrationDetails
import com.ktor.stock.market.game.jbosak.model.User
import com.ktor.stock.market.game.jbosak.repository.UserRepository
import com.ktor.stock.market.game.jbosak.server.JwtConfig
import com.ktor.stock.market.game.jbosak.utils.BcryptHasher
import org.mindrot.jbcrypt.BCrypt

object AuthService {

    private fun validatePasswordLength(password:String):ClientGraphQLException? =
        if(password.length < 6)
            ClientGraphQLException("Password is too short")
        else
            null

    private fun validateEmail(email:String):ClientGraphQLException?{
        val emailRegex = "^((([!#\$%&'*+\\-/=?^_`{|}~\\w])|([!#\$%&'*+\\-/=?^_`{|}~\\w][!#\$%&'*+\\-/=?^_`{|}~\\.\\w]{0,}[!#\$%&'*+\\-/=?^_`{|}~\\w]))[@]\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*)\$".toRegex()
        val validation = email.matches(emailRegex)
        return when {
            validation -> null
            else -> ClientGraphQLException("Email is not valid")
        }
    }

    fun register(details: RegistrationDetails): Validated<ClientGraphQLException,User> = details.let { (email, password) ->
//        validatePasswordLength(password)?.let { return Validated.Invalid(it) }
//        validateEmail(email)?.let { return Validated.Invalid(it) }

        val hashed = BcryptHasher.hashPassword(password)
        val id = UserRepository.insert(details.copy(password = hashed))
        return Validated.Valid(User(id, email, "", null))
    }

    fun login(credentials: LoginCredentials): Validated<ClientGraphQLException, User> =
        credentials.let { (email, password) ->
            val user = UserRepository.findUserByEmail(email)
                ?: return Validated.Invalid(ClientGraphQLException("wrong credentials"))

            if (BCrypt.checkpw(password, user.password)) {
                val token = JwtConfig.makeToken(user)
                return Validated.Valid(user.copy(token = token))
            }
            return Validated.Invalid(ClientGraphQLException("wrong credentials"))

        }
}