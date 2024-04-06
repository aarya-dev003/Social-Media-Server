package com.example.plugins

import com.example.model.admin.ClubAdminDataSource
import com.example.model.post.PostDataSource
import com.example.model.user.UserDataSource
import com.example.routes.*
import com.example.security.hashing.HashingService
import com.example.security.token.TokenConfig
import com.example.security.token.TokenService
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting(
    userDataSource: UserDataSource,
    hashingService: HashingService,
    tokenConfig: TokenConfig,
    tokenService: TokenService,
    postDataSource: PostDataSource,
    clubAdminDataSource: ClubAdminDataSource
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

        clubLogin(hashingService,tokenConfig,tokenService, clubAdminDataSource)

        createCLubAdmin(tokenConfig, tokenService, hashingService, clubAdminDataSource)

        authenticateClub()
        getSecretInfoOfCLub()

        authenticate()
        getSecretInfo()
    }

}
