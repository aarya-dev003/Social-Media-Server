package com.example.plugins

import com.example.model.user.UserDataSource
import com.example.routes.authenticate
import com.example.routes.getSecretInfo
import com.example.routes.signIn
import com.example.routes.signUp
import com.example.security.hashing.HashingService
import com.example.security.token.TokenConfig
import com.example.security.token.TokenService
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.http.ContentType
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.io.File

fun Application.configureRouting(
    userDataSource: UserDataSource,
    hashingService: HashingService,
    tokenConfig: TokenConfig,
    tokenService: TokenService
) {
    routing {
        get("/") {
            call.respondText("Hello MediVerse!")
        }

        signUp(
            hashingService,
            userDataSource
        )
        signIn(
            hashingService,
            userDataSource,
            tokenConfig,
            tokenService
        )
        authenticate()
        getSecretInfo()
    }

}
