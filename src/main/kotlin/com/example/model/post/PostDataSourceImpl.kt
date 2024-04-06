package com.example.model.post

import com.mongodb.client.model.Filters
import org.bson.types.ObjectId
import org.litote.kmongo.coroutine.CoroutineDatabase

class PostDataSourceImpl (db: CoroutineDatabase): PostDataSource {

    private val posts = db.getCollection<Post>()
    override suspend fun getAllPosts(): List<Post> {
        val post = posts.find().toList()
        return post
    }

    override suspend fun findOneByIdAndDelete(id: ObjectId?): Post? {
        val filter = id?.let { Filters.eq("_id", it) } // Assuming "_id" is the field name in your collection for ObjectId
        val delPost = filter?.let { posts.findOneAndDelete(it) }
        return delPost
    }

    override suspend fun createPost(post: Post): Boolean {
        return posts.insertOne(post).wasAcknowledged()
    }


}