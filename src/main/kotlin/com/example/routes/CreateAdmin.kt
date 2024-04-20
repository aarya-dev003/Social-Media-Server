package com.example.routes

import com.example.model.collegeAdmin.Admin
import com.example.model.collegeAdmin.CollegeAdminDataSource
import com.example.requests.AdminRequest
import com.example.requests.AuthResponse
import com.example.requests.LoginRequest
import com.example.security.hashing.HashingService
import com.example.security.hashing.SaltedHash
import com.example.security.token.TokenClaim
import com.example.security.token.TokenConfig
import com.example.security.token.TokenService
import com.example.utils.Constants.ADMIN_LOGIN
import com.example.utils.Constants.CREATE_ADMIN
import com.example.utils.Constants.GET_AUTHENTICATE_ADMIN
import com.example.utils.Constants.GET_SECRET_ADMIN
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.createAdmin(
    tokenConfig: TokenConfig,
    tokenService: TokenService,
    hashingService: HashingService,
    collegeAdminDataSource: CollegeAdminDataSource
){
    post(CREATE_ADMIN) {
        val request = kotlin.runCatching { call.receiveNullable<AdminRequest>() }.getOrNull() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest, "Cannot get request class")
            return@post
        }

        val areFieldBlank = request.username.isBlank() || request.email.isBlank() || request.password.isBlank()
        val isPasswordTooShort = request.password.length < 8
        if (areFieldBlank || isPasswordTooShort) {
            call.respond(HttpStatusCode.Conflict, "Fields are blank or password too short")
            return@post
        }

        val saltedHash = hashingService.generateSaltedHash(value = request.password)
        val user = Admin(
            username = request.username,
            email = request.email,
            password = saltedHash.hash,
            name = request.name,
            salt = saltedHash.salt
        )

        val wasAcknowledged = collegeAdminDataSource.insertUser(user)
        if (!wasAcknowledged) {
            call.respond(HttpStatusCode.Conflict, "club cannot be inserted in database")
            return@post
        }

        val token = tokenService.generate(
            config = tokenConfig,
            TokenClaim(
                name = "collegeId",
                value = user.email
            )
        )
        call.respond(
            status = HttpStatusCode.OK,
            message = AuthResponse(token = token),
        )

    }
}

fun Route.adminLogin(
    hashingService: HashingService,
    tokenConfig: TokenConfig,
    tokenService: TokenService,
    collegeAdminDataSource: CollegeAdminDataSource
){
    post(ADMIN_LOGIN){
        val request = kotlin.runCatching { call.receiveNullable<LoginRequest>() }.getOrNull() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest, "Unable to get Data Class")
            return@post
        }

        val user : Admin? = collegeAdminDataSource.getAdminByEmail(request.email)
        if(user == null){
            call.respond(HttpStatusCode.Conflict, "Admin Not found")
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
                name = "collegeId",
                value = user.email
            )
        )
        call.respond(
            status = HttpStatusCode.OK,
            message = AuthResponse(token = token)
        )

    }
}


fun Route.authenticateAdmin() {
    authenticate("college-jwt") {
        get(GET_AUTHENTICATE_ADMIN) {
            call.respond(HttpStatusCode.OK)
        }
    }
}

fun Route.getSecretInfoOfAdmin() {
    authenticate{
        get(GET_SECRET_ADMIN) {
            val principal = call.principal<JWTPrincipal>()
            val userId = principal?.getClaim("collegeId", String::class)
            call.respond(HttpStatusCode.OK, "Your userId is $userId")
        }
    }
}