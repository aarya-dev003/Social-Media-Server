package com.example.routes

import com.example.model.post.Post
import com.example.model.post.PostDataSource
import com.example.requests.PostRequest
import com.example.requests.PostsDTO
import com.example.utils.Constants.CREATE_END_POINT
import com.example.utils.Constants.DELETE_END_POINT
import com.example.utils.Constants.RETRIEVE_END_POINT
import com.example.utils.Constants.RETRIEVE_END_POINT_USER
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

import org.bson.types.ObjectId

fun Route.postRoutes(
    postDataSource: PostDataSource
){
    authenticate("club-jwt"){
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

        get(RETRIEVE_END_POINT) {
                try {
                    val posts = postDataSource.getAllPosts()
                    val postDTOs = posts.map { it.toDTO() } // Convert List<Post> to List<PostDTO>
                    call.respond(HttpStatusCode.OK, postDTOs)
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.Conflict, emptyList<PostsDTO>()) // Respond with empty list of PostDTO
                }
        }



        delete (DELETE_END_POINT) {
            val postId = call.parameters["id"]
            if (postId == null) {
                call.respond(HttpStatusCode.BadRequest, "ID parameter is missing")
                return@delete
            }

            try {
                val objectId = ObjectId(postId) // Convert the string postId to ObjectId
                val post = postDataSource.findOneByIdAndDelete(objectId)
                if (post == null) {
                    call.respond(HttpStatusCode.NotFound, "Post not found")
                    return@delete
                }

                // Delete the post from the database
               // postDataSource.deletePost(objectId) // Use ObjectId instead of string for deletion

                call.respond(HttpStatusCode.OK, "Post deleted successfully")
            } catch (e: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest, "Invalid ObjectId provided")
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, "Failed to delete post: ${e.message}")
            }
        }
    }
    authenticate("jwt"){
        get(RETRIEVE_END_POINT_USER) {
            try {
                val posts = postDataSource.getAllPosts()
                val postDTOs = posts.map { it.toDTO() } // Convert List<Post> to List<PostDTO>
                call.respond(HttpStatusCode.OK, postDTOs)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.Conflict, emptyList<PostsDTO>()) // Respond with empty list of PostDTO
            }
        }
    }

}

//converted to data transfer object (DTO) or simply just filled the details from post class to postRequest
fun Post.toDTO() = PostsDTO(username, image, description, time, id.toString())