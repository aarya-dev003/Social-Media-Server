package com.example

import com.example.model.user.UserDataSourceImpl
import com.example.plugins.configureRouting
import com.example.plugins.configureSecurity
import com.example.plugins.configureSerialization
import com.example.security.hashing.SHAHashingServiceImpl
import com.example.security.token.TokenConfig
import com.example.security.token.TokenServiceImpl
import io.ktor.server.application.*
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {

    val mongoPw =System.getenv("MONGO_PW")
    val dbName = "MediVerse-Database"
    val db = KMongo.createClient(
        connectionString = "mongodb+srv://aaryagupta2003:$mongoPw@cluster0.vgmsqf1.mongodb.net/$dbName?retryWrites=true&w=majority"
    ).coroutine
        .getDatabase(dbName)

    val userDataSource  = UserDataSourceImpl(db)
    val tokenService = TokenServiceImpl()
    val tokenConfig = TokenConfig(
        issuer = environment.config.property("jwt.issuer").getString(),
        audience =environment.config.property("jwt.audience").getString(),
        expiresIn = 365L * 1000L * 60L * 60L * 24L,
        secret =System.getenv("JWT_SECRET")
    )
    val hashingService = SHAHashingServiceImpl()

    configureSecurity(userDataSource,tokenConfig)
    configureSerialization()
    configureRouting(userDataSource, hashingService, tokenConfig, tokenService)
}
