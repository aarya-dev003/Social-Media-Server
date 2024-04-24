package com.example.plugins

import com.example.model.clubAdmin.ClubAdminDataSource
import com.example.model.collegeAdmin.CollegeAdminDataSource
import com.example.model.collegeAdmin.announcement.AnnouncementDataSource
import com.example.model.feedback.FeedbackDataSource
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
    clubAdminDataSource: ClubAdminDataSource,
    announcementDataSource: AnnouncementDataSource,
    collegeAdminDataSource: CollegeAdminDataSource,
    feedbackDataSource: FeedbackDataSource
) {
    routing {
        get("/") {
            call.respondText("Hello MediVerse!")
        }

        //user
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



        authenticate()
        getSecretInfo()


        //club
        clubLogin(hashingService,tokenConfig,tokenService, clubAdminDataSource)

        createCLubAdmin(tokenConfig, tokenService, hashingService, clubAdminDataSource)
        authenticateClub()
        getSecretInfoOfCLub()


        //college admin
        createAdmin(tokenConfig, tokenService, hashingService, collegeAdminDataSource)
        adminLogin(hashingService, tokenConfig, tokenService, collegeAdminDataSource)

        //announcemnt
        announcementRoutes(announcementDataSource)


        //posts
        postRoutes(
            postDataSource
        )

        feedbackRoute(feedbackDataSource)




    }

}
