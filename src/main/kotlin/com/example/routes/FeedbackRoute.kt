package com.example.routes

import com.example.model.feedback.Feedback
import com.example.model.feedback.FeedbackDataSource
import com.example.model.user.User
import com.example.requests.FeedbackRequest
import com.example.utils.Constants.CREATE_FEEDBACK
import com.example.utils.Constants.GET_FEEDBACK_ADMIN
import com.example.utils.Constants.GET_FEEDBACK_CLUB
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.feedbackRoute(
    feedbackDataSource: FeedbackDataSource
) {
    authenticate("jwt"){
        post(CREATE_FEEDBACK){
            val request = kotlin.runCatching { call.receiveNullable<FeedbackRequest>() }.getOrNull() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest, "Request Not received")
                return@post
            }
            val username = call.principal<User>()!!.email

            val feedback = Feedback(
                issueType = request.issueType,
                issue = request.issue,
                time = request.time,
                username = username
            )

            val wasAcknowledged = feedbackDataSource.createFeedback(feedback)
            if (!wasAcknowledged) {
                call.respond(HttpStatusCode.Conflict, "Unable to create Feedback")
                return@post
            }
            call.respond(HttpStatusCode.OK, "Feedback was created")
        }
    }

    authenticate("college-jwt"){
        get(GET_FEEDBACK_ADMIN){
            try {
                val feedback = feedbackDataSource.getFeedbackAdmin()
                call.respond(HttpStatusCode.OK, feedback)
            }catch (e:Exception){
                call.respond(HttpStatusCode.InternalServerError, "Feedback not found")
            }

        }
    }
    authenticate("club-jwt"){
        get(GET_FEEDBACK_CLUB){
            try {
                val feedback: List<Feedback> = feedbackDataSource.getFeedbackClub()
                call.respond(HttpStatusCode.OK, feedback)
            }catch (e:Exception){
                call.respond(HttpStatusCode.InternalServerError, "Feedback not found")
            }

        }
    }
}
