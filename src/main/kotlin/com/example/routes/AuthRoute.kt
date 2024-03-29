package com.example.routes

import ch.qos.logback.core.subst.Token
import com.example.model.user.User
import com.example.model.user.UserDataSource
import com.example.requests.AuthRequest
import com.example.requests.AuthResponse
import com.example.routes.authenticate
import com.example.security.hashing.HashingService
import com.example.security.hashing.SaltedHash
import com.example.security.token.TokenClaim
import com.example.security.token.TokenConfig
import com.example.security.token.TokenService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


const val API_VERSION = "v1"
const val ROUTE_TYPE = "auth"
const val REGISTER_END_POINT = "$API_VERSION/$ROUTE_TYPE/register"
const val LOGIN_END_POINT = "$API_VERSION/$ROUTE_TYPE/login"

fun Route.signUp(
    hashingService: HashingService,
    userDataSource: UserDataSource
){
    post("$REGISTER_END_POINT"){
        val request = kotlin.runCatching { call.receiveNullable<AuthRequest>() }.getOrNull() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest, "Unable to get Data Class")
            return@post
        }

        val areFieldBlank =request.username.isBlank() || request.email.isBlank() || request.password.isBlank()
        val isPasswordTooShort =request.password.length < 8
        if (areFieldBlank || isPasswordTooShort){
            call.respond(HttpStatusCode.Conflict, "Fields are blank or password too short")
            return@post
        }

        val saltedHash =hashingService.generateSaltedHash(value = request.password)
        val user = User(
            username = request.username,
            email = request.email,
            password = saltedHash.hash,
            name = request.name,
            salt = saltedHash.salt
        )

        val wasAcknowledged =userDataSource.insertUser(user)
        if (!wasAcknowledged){
            call.respond(HttpStatusCode.Conflict , "user cannot be inserted in database")
            return@post
        }

        call.respond(HttpStatusCode.OK , "User Inserted Into Database")

    }
}

fun Route.signIn(
    hashingService: HashingService,
    userDataSource: UserDataSource,
    tokenConfig: TokenConfig,
    tokenService: TokenService
){
    post("$LOGIN_END_POINT"){
        val request = kotlin.runCatching { call.receiveNullable<AuthRequest>() }.getOrNull() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest, "Data Format wrong")
            return@post
        }
        val user : User? = userDataSource.getUserByEmail(request.email)
        if(user == null){
            call.respond(HttpStatusCode.Conflict, "Incorrect email or Password")
            return@post
        }

        val isValidPassword :Boolean =hashingService.verify(
            value =request.password,
            saltedHash = SaltedHash(
            salt = user.salt,
            hash = user.password
        ))

        val token = tokenService.generate(
            config = tokenConfig,
            TokenClaim(
                name = "userId",
                value = user.id.toString()
            )
        )
        call.respond(
            status = HttpStatusCode.OK,
            message =AuthResponse(token = token)
        )
    }
}

fun Route.authenticate() {
//    authenticate {
//        get("authenticate") {
//            call.respond(HttpStatusCode.OK)
//        }
//    }
}

fun Route.getSecretInfo() {
//    authenticate {
//        get("secret") {
//            val principal = call.principal<JWTPrincipal>()
//            val userId = principal?.getClaim("userId", String::class)
//            call.respond(HttpStatusCode.OK, "Your userId is $userId")
//        }
//    }
}