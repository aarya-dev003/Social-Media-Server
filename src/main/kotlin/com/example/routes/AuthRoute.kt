package com.example.routes

import com.example.model.user.User
import com.example.model.user.UserDataSource
import com.example.requests.AuthRequest
import com.example.requests.AuthResponse
import com.example.requests.LoginRequest
import com.example.security.hashing.HashingService
import com.example.security.hashing.SaltedHash
import com.example.security.token.TokenClaim
import com.example.security.token.TokenConfig
import com.example.security.token.TokenService
import com.example.utils.Constants.LOGIN_END_POINT
import com.example.utils.Constants.REGISTER_END_POINT
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun Route.signUp(
    hashingService: HashingService,
    userDataSource: UserDataSource,
    tokenService: TokenService,
    tokenConfig : TokenConfig
){
    post(REGISTER_END_POINT){
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

        val token = tokenService.generate(
            config = tokenConfig,
            TokenClaim(
                name = "userId",
                value = user.email
            )
        )
        call.respond(
            status = HttpStatusCode.OK,
            message = AuthResponse(token = token) ,
        )

    }
}

fun Route.signIn(
    hashingService: HashingService,
    userDataSource: UserDataSource,
    tokenConfig: TokenConfig,
    tokenService: TokenService
){
    post(LOGIN_END_POINT){
        val request = kotlin.runCatching { call.receiveNullable<LoginRequest>() }.getOrNull() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest, "Data Format wrong")
            return@post
        }
        val user : User? = userDataSource.getUserByEmail(request.email)
        if(user == null){
            call.respond(HttpStatusCode.Conflict, "Incorrect email or Password")
            return@post
        }

        val isValidPassword :Boolean = hashingService.verify(
            value =request.password,
            saltedHash = SaltedHash(
            salt = user.salt,
            hash = user.password
        ))

        if(!isValidPassword){
            call.respond(HttpStatusCode.Conflict, "Invalid Credentails")
            return@post
        }

        val token = tokenService.generate(
            config = tokenConfig,
            TokenClaim(
                name = "userId",
                value = user.email
            )
        )
        call.respond(
            status = HttpStatusCode.OK,
            message =AuthResponse(token = token)
        )
    }
}

fun Route.authenticate() {
    authenticate {
        get("authenticate") {
            call.respond(HttpStatusCode.OK)
        }
    }
}

fun Route.getSecretInfo() {
    authenticate {
        get("secret") {
            val principal = call.principal<JWTPrincipal>()
            val userId = principal?.getClaim("userId", String::class)
            call.respond(HttpStatusCode.OK, "Your userId is $userId")
        }
    }
}