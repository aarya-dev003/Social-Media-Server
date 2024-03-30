package com.example.routes

import com.example.model.post.Post
import com.example.model.post.PostDataSource
import com.example.model.user.User
import com.example.requests.PostRequest
import com.example.utils.Constants.CREATE_END_POINT
import com.example.utils.Constants.DELETE_END_POINT
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.clubPostRoute(
    postDataSource: PostDataSource
){
    authenticate("jwt"){
        post (CREATE_END_POINT){
            val request = kotlin.runCatching { call.receiveNullable<PostRequest>() }.getOrNull() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest, "Request Not received")
                return@post
            }
            val  post = Post(
                username = request.username,
                image = request.image,
                description = request.description,
                time = request.time
            )
            val wasAcknowledged = postDataSource.createPost(post)
            if (!wasAcknowledged){
                call.respond(HttpStatusCode.Conflict , "user cannot be inserted in database")
                return@post
            }

            call.respond(HttpStatusCode.OK , "Post Created")
        }


        delete (DELETE_END_POINT){
            val postId = try {
                call.request.queryParameters["id"]!!
            }catch (e:Exception){
                call.respond(HttpStatusCode.BadRequest, "Some Error Occurred")
                return@delete
            }
            try {
                val email = call.principal<User>()!!.email
                postDataSource.deletePost(postId,email)
                call.respond(HttpStatusCode.OK,"Note Deleted Successfully")
            }catch (e:Exception){
                call.respond(HttpStatusCode.Conflict, e.message ?: "Some Problem Occurred")
            }
        }
    }
}