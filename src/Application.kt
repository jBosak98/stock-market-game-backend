package com.ktor.stock.market.game.jbosak

import com.ktor.stock.market.game.jbosak.model.UserDTO
import com.ktor.stock.market.game.jbosak.model.Users
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.auth.Authentication
import io.ktor.auth.UserPasswordCredential
import io.ktor.auth.authenticate
import io.ktor.auth.jwt.JWTPrincipal
import io.ktor.auth.jwt.jwt
import io.ktor.features.CORS
import io.ktor.features.ContentNegotiation
import io.ktor.gson.gson
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils.create
import org.jetbrains.exposed.sql.transactions.transaction


fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    val userController = UserRepository()

    install(CORS) {
        method(HttpMethod.Options)
        method(HttpMethod.Put)
        method(HttpMethod.Delete)
        method(HttpMethod.Patch)
        header(HttpHeaders.Authorization)
        header("MyCustomHeader")
        allowCredentials = true
        anyHost()
    }
    initDB()



    install(Authentication) {
        jwt {
            verifier(JwtConfig.verifier)
            realm = "ktor.io"
            validate {
                println(it.payload)
                val user = it
                    .payload
                    .getClaim("id")
                    .asInt()
                    ?.let(userController::findUserById)
                if(user === null) null
                else JWTPrincipal(it.payload)
            }
        }
    }

    install(ContentNegotiation) {
        gson {
        }
    }


    routing {
        authenticate {
            get("/") {
                call.respondText("HELLO WORLD!", contentType = ContentType.Text.Plain)
            }
        }
        post("/auth/register") {
            val userDto = call.receive<UserDTO>()
            if(userController.doesUserExist(userDto.email))
                call.respond(HttpStatusCode.BadRequest)
            else {
                userController.insert(userDto)
                call.respond(HttpStatusCode.Created)
            }

        }
        post("/auth/login") {
            val credential = call.receive<UserPasswordCredential>()
            println(credential)
            val user = userController.findUserByCredentials(credential)
            if(user === null) return@post  call.respond(HttpStatusCode.Unauthorized)
            val token = JwtConfig.makeToken(user)
            call.respond(HttpStatusCode.Created, token)
        }
//        get("/json/jackson") {
//            call.respond(mapOf("hello" to "world"))
//        }
//
//        get("/users") {
//            call.respond(userController.getAll())
//        }
//
//        post("/users") {
//            val userDto = call.receive<UserDTO>()
//            userController.insert(userDto)
//            call.respond(HttpStatusCode.Created)
//        }
//
//        put("/users/{id}") {
//            val id: Int = call.parameters["id"] as Int
//            val userDTO = call.receive<UserDTO>()
//            userController.update(userDTO, id)
//            call.respond(HttpStatusCode.OK)
//        }
//
//        delete("/users/{id}") {
//            val id: Int = call.parameters["id"] as Int
//            userController.delete(id)
//            call.respond(HttpStatusCode.OK)
//        }
    }
}

fun initDB() {
    val config = HikariConfig("/hikari.properties")
    config.schema = "public"
    val ds = HikariDataSource(config)
    Database.connect(ds)
    transaction {
        create(Users)
    }
}