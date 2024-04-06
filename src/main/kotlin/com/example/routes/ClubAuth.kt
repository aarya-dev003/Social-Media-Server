package com.example.routes

import com.example.model.admin.Club
import com.example.model.admin.ClubAdminDataSource
import com.example.requests.AuthResponse
import com.example.requests.ClubLoginRequest
import com.example.security.hashing.HashingService
import com.example.security.hashing.SaltedHash
import com.example.security.token.TokenClaim
import com.example.security.token.TokenConfig
import com.example.security.token.TokenService
import com.example.utils.Constants.GET_AUTHENTICATED_CLUB
import com.example.utils.Constants.GET_SECRET_CLUB
import com.example.utils.Constants.LOGIN_CLUB_ADMIN
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.clubLogin(
    hashingService: HashingService,
    tokenConfig: TokenConfig,
    tokenService: TokenService,
    clubAdminDataSource: ClubAdminDataSource
){
    post(LOGIN_CLUB_ADMIN){
        val request = kotlin.runCatching { call.receiveNullable<ClubLoginRequest>() }.getOrNull() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest, "Unable to get Data Class")
            return@post
        }

        val user : Club? = clubAdminDataSource.getAdminByEmail(request.username)
        if(user == null){
            call.respond(HttpStatusCode.Conflict, "Club Not found")
            return@post
        }
        val isValidPassword :Boolean = hashingService.verify(
            value =request.password,
            saltedHash = SaltedHash(
                salt = user.salt,
                hash = user.password
            )
        )
        if(!isValidPassword){
            call.respond(HttpStatusCode.Conflict, "Invalid Credentails")
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
            message = AuthResponse(token = token)
        )

    }
}


fun Route.authenticateClub() {
    authenticate("club-jwt") {
        get(GET_AUTHENTICATED_CLUB) {
            call.respond(HttpStatusCode.OK)
        }
    }
}

fun Route.getSecretInfoOfCLub() {
    authenticate{
        get(GET_SECRET_CLUB) {
            val principal = call.principal<JWTPrincipal>()
            val userId = principal?.getClaim("adminId", String::class)
            call.respond(HttpStatusCode.OK, "Your userId is $userId")
        }
    }
}