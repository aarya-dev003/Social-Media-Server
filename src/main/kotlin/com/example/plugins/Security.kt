package com.example.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.model.admin.ClubAdminDataSource
import com.example.model.user.UserDataSource
import com.example.security.token.TokenConfig
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*

fun Application.configureSecurity(userDataSource: UserDataSource, config: TokenConfig,clubAdminDataSource: ClubAdminDataSource) {


    authentication {

        jwt("jwt") {
            realm = this@configureSecurity.environment.config.property("jwt.realm").getString()
            verifier(
                JWT
                    .require(Algorithm.HMAC512(config.secret))
                    .withAudience(config.audience)
                    .withIssuer(config.issuer)
                    .build()
            )

            validate { credential ->
                val payload = credential.payload
                // Extract necessary claims from the token payload
                val userId = payload.getClaim("userId").asString()

                // Retrieve user from data source based on extracted userId
                val user = userDataSource.getUserByEmail(userId)

                user
            }
        }
        jwt("club-jwt") {
            realm = this@configureSecurity.environment.config.property("jwt.realm").getString()
            verifier(
                JWT
                    .require(Algorithm.HMAC512(config.secret))
                    .withAudience(config.audience)
                    .withIssuer(config.issuer)
                    .build()
            )

            validate { credential ->
                val payload = credential.payload
                // Extract necessary claims from the token payload
                val userId = payload.getClaim("adminId").asString()

                // Retrieve user from data source based on extracted userId
                val user = clubAdminDataSource.getAdminByEmail(userId)

                user
            }
        }

        jwt {
            realm = this@configureSecurity.environment.config.property("jwt.realm").getString()
            verifier(
                JWT
                    .require(Algorithm.HMAC512(config.secret))
                    .withAudience(config.audience)
                    .withIssuer(config.issuer)
                    .build()
            )
            validate { credential ->
                // Validate audience of the token
                if (credential.payload.audience.contains(config.audience)) JWTPrincipal(credential.payload) else null
            }
        }
    }
}
