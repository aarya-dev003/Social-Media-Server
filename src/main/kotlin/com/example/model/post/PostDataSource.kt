package com.example.model.post

import org.bson.types.ObjectId

interface PostDataSource {

    suspend fun getAllPosts() : List<Post>

    suspend fun findOneByIdAndDelete(id: ObjectId?) : Post?

    suspend fun createPost(post : Post) :Boolean


}