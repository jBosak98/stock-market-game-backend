package com.ktor.stock.market.game.jbosak

import com.ktor.stock.market.game.jbosak.model.User
import com.ktor.stock.market.game.jbosak.model.UserDTO
import com.ktor.stock.market.game.jbosak.model.Users
import io.ktor.auth.UserPasswordCredential
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.collections.ArrayList

class UserRepository {
    fun getAll(): ArrayList<User> {
        val users: ArrayList<User> = arrayListOf()
        transaction {
            Users
                .selectAll()
                .map {
                    users.add(
                        User(
                            id = it[Users.id],
                            password = it[Users.password],
                            email = it[Users.name]
                        )
                    )
                }
        }
        return users
    }

    fun findUserById(id: Int) =
        transaction {
            Users.select { Users.id eq id }
                .map {
                    User(
                        it[Users.id],
                        it[Users.name],
                        it[Users.password]
                    )
                }.singleOrNull()
        }

    fun doesUserExist(email:String) =
        transaction {
            Users.select { Users.name eq email }.count() != 0
        }
    fun findUserByEmail(email: String) =
        transaction {
            Users.select { Users.name eq email }
                .map {
                    User(
                        it[Users.id],
                        it[Users.name],
                        it[Users.password]
                    )
                }.singleOrNull()
        }


    fun findUserByCredentials(credential: UserPasswordCredential) =
        transaction {
            Users
                .select { (Users.name eq credential.name) and (Users.password eq credential.password) }
                .map {
                    User(
                        it[Users.id],
                        it[Users.name],
                        it[Users.password]
                    )
                }.singleOrNull()
        }


    fun insert(user: UserDTO) {
        transaction {
            Users.insert {
                it[name] = user.email
                it[password] = user.password
            }
        }
    }

    fun update(user: UserDTO, id: Int) {
        transaction {
            Users.update({ Users.id eq id }) {
                it[name] = user.email
                it[password] = user.password
            }
        }
    }

    fun delete(id: Int) {
        transaction {
            Users.deleteWhere { Users.id eq id }
        }
    }

}