package com.ktor.stock.market.game.jbosak

import com.ktor.stock.market.game.jbosak.model.RegistrationDetails
import com.ktor.stock.market.game.jbosak.model.UserDTO
import com.ktor.stock.market.game.jbosak.model.Users
import com.ktor.stock.market.game.jbosak.repository.toUser
import io.ktor.auth.UserPasswordCredential
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object UserRepository {

    fun getAll() = transaction {
        Users
            .selectAll()
            .map(ResultRow::toUser)
    }


    fun findUserById(id: Int) = transaction {
        Users
            .select { Users.id eq id }
            .singleOrNull()
            ?.let(ResultRow::toUser)
    }

    fun doesUserExist(email: String) = transaction {
        Users
            .select { Users.name eq email }
            .count() != 0
    }

    fun findUserByEmail(email: String) = transaction {
        Users
            .select { Users.name eq email }
            .map(ResultRow::toUser)
            .singleOrNull()
    }


    fun findUserByCredentials(credential: UserPasswordCredential) = transaction {
        Users
            .select { (Users.name eq credential.name) and (Users.password eq credential.password) }
            .map(ResultRow::toUser)
            .singleOrNull()
    }


    fun insert(details: RegistrationDetails) = transaction {
        val alreadyExists = Users.select { Users.name eq details.email }
            .firstOrNull() != null
        if (alreadyExists) throw Exception()
        Users.insert {
            it[name] = details.email
            it[password] = details.password
        } get Users.id
    }


    fun update(user: UserDTO, id: Int) = transaction {
        Users.update({ Users.id eq id }) {
            it[name] = user.email

        }
    }


    fun delete(id: Int) = transaction {
        Users.deleteWhere { Users.id eq id }
    }


}