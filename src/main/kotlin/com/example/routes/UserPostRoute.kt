package com.example.routes

import com.example.model.post.Post
import com.example.model.post.PostDataSource
import com.example.requests.PostRequest
import com.example.utils.Constants.RETRIEVE_END_POINT
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun Route.userPostRoute(
    postDataSource: PostDataSource
){
    authenticate("jwt"){
        get(RETRIEVE_END_POINT) {
            try {
                val posts = postDataSource.getAllPosts()
                val postDTOs = posts.map { it.toDTO() } // Convert List<Post> to List<PostDTO>
                call.respond(HttpStatusCode.OK, postDTOs)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.Conflict, emptyList<PostRequest>()) // Respond with empty list of PostDTO
            }
        }

    }
}

//converted to data transfer object (DTO) or simply just filled the details from post class to postRequest
fun Post.toDTO() = PostRequest(username, image, description, time)