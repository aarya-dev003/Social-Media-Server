package com.example.plugins

import com.example.model.post.PostDataSource
import com.example.model.user.UserDataSource
import com.example.routes.*
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
    tokenService: TokenService,
    postDataSource: PostDataSource
) {
    routing {
        get("/") {
            call.respondText("Hello MediVerse!")
        }

        signUp(
            hashingService,
            userDataSource,
            tokenService,
            tokenConfig
        )
        signIn(
            hashingService,
            userDataSource,
            tokenConfig,
            tokenService
        )
        clubPostRoute(
            postDataSource
        )
        userPostRoute(
            postDataSource
        )
        authenticate()
        getSecretInfo()
    }

}
