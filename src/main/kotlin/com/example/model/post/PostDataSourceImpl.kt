package com.example.model.post

import com.example.model.user.User
import org.litote.kmongo.coroutine.CoroutineDatabase

class PostDataSourceImpl (db: CoroutineDatabase): PostDataSource {

    private val posts = db.getCollection<Post>()
    private val user = db.getCollection<User>()
    override suspend fun getAllPosts(): List<Post> {
        val post = posts.find().toList()
        return post
    }

    override suspend fun createPost(post: Post): Boolean {
        return posts.insertOne(post).wasAcknowledged()
    }

    override suspend fun deletePost(id: String, email: String): Boolean {
//        val filter = and(
//            posts ::id eq ObjectId(id),
//            posts ::email eq email
//        )

        val deleteResult = posts.deleteOne(id)
        return deleteResult.wasAcknowledged()
    }
}