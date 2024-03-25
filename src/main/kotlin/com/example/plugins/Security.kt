package com.example.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.model.user.UserDataSource
import com.example.security.token.TokenConfig
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import org.litote.kmongo.coroutine.CoroutineDatabase

fun Application.configureSecurity(userDataSource: UserDataSource, config : TokenConfig) {
    data class MySession(val count: Int = 0)
    install(Sessions) {
        cookie<MySession>("MY_SESSION") {
            cookie.extensions["SameSite"] = "lax"
        }
    }


    authentication {
        jwt ("jwt"){
            realm = this@configureSecurity.environment.config.property("jwt.realm").getString()
            verifier(
                JWT
                    .require(Algorithm.HMAC256(config.secret))
                    .withAudience(config.audience)
                    .withIssuer(config.issuer)
                    .build()
            )

            validate {
                val payload = it.payload
                val email = payload.getClaim("email").asString()
                val user = userDataSource.getUserByEmail(email)
                user
            }
        }
    }
}
