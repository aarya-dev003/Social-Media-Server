package com.example.model.post

interface PostDataSource {

    suspend fun getAllPosts() : List<Post>

    suspend fun createPost(post : Post) :Boolean

    suspend fun deletePost(id: String, email:String) : Boolean
}