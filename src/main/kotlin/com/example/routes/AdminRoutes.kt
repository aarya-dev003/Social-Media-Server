package com.example.routes

import com.example.model.admin.Club
import com.example.model.admin.ClubAdminDataSource
import com.example.requests.AuthResponse
import com.example.requests.ClubAuthRequest
import com.example.security.hashing.HashingService
import com.example.security.token.TokenClaim
import com.example.security.token.TokenConfig
import com.example.security.token.TokenService
import com.example.utils.Constants.CREATE_CLUB
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.createCLubAdmin(
    tokenConfig: TokenConfig,
    tokenService: TokenService,
    hashingService: HashingService,
    clubAdminDataSource: ClubAdminDataSource
){
    post(CREATE_CLUB){
        val request = kotlin.runCatching { call.receiveNullable<ClubAuthRequest>() }.getOrNull() ?: kotlin.run {
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
        val user = Club(
            username = request.username,
            email = request.email,
            password = saltedHash.hash,
            name = request.name,
            salt = saltedHash.salt
        )

        val wasAcknowledged =clubAdminDataSource.insertUser(user)
        if (!wasAcknowledged){
            call.respond(HttpStatusCode.Conflict , "club cannot be inserted in database")
            return@post
        }

        val token = tokenService.generate(
            config = tokenConfig,
            TokenClaim(
                name = "adminId",
                value = user.username
            )
        )
        call.respond(
            status = HttpStatusCode.OK,
            message = AuthResponse(token = token) ,
        )

    }
}