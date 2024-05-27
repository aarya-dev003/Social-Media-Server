package com.example

import com.example.model.clubAdmin.ClubAdminImpl
import com.example.model.collegeAdmin.CADSImpl
import com.example.model.collegeAdmin.announcement.AnnouncementDSImpl
import com.example.model.feedback.FeedbackDataSourceImpl
import com.example.model.post.PostDataSourceImpl
import com.example.model.user.UserDataSourceImpl
import com.example.plugins.configureRouting
import com.example.plugins.configureSecurity
import com.example.plugins.configureSerialization
import com.example.plugins.configureSockets
import com.example.security.hashing.SHAHashingServiceImpl
import com.example.security.token.TokenConfig
import com.example.security.token.TokenServiceImpl
import io.github.cdimascio.dotenv.dotenv
import io.ktor.server.application.*
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

val env = dotenv{
    ignoreIfMissing = true
}

fun main(args: Array<String>) {


    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {

    val mongoPw = env["MONGO_PW"]
    val dbName = "MediVerse-Database"
    val db = KMongo.createClient(
        connectionString = "mongodb+srv://aaryagupta2003:$mongoPw@cluster0.vgmsqf1.mongodb.net/$dbName?retryWrites=true&w=majority"
    ).coroutine
        .getDatabase(dbName)

    //datasources
    val userDataSource  = UserDataSourceImpl(db)
    val postDataSource = PostDataSourceImpl(db)
    val clubAdminDataSource = ClubAdminImpl(db)
    val announcementDataSource = AnnouncementDSImpl(db)
    val collegeAdminDataSource = CADSImpl(db)
    val feedbackDataSource = FeedbackDataSourceImpl(db)

    //security
    val tokenService = TokenServiceImpl()
    val tokenConfig = TokenConfig(
        issuer = environment.config.property("jwt.issuer").getString(),
        audience =environment.config.property("jwt.audience").getString(),
        expiresIn = 365L * 1000L * 60L * 60L * 24L,
        secret = env["JWT_SECRET"]
    )
    val hashingService = SHAHashingServiceImpl()

    configureSecurity(userDataSource,tokenConfig,clubAdminDataSource,collegeAdminDataSource)
    configureSerialization()
    configureRouting(userDataSource,
        hashingService,
        tokenConfig,
        tokenService,
        postDataSource,
        clubAdminDataSource,
        announcementDataSource,
        collegeAdminDataSource,
        feedbackDataSource
        )

    //websocket
    configureSockets()

}
