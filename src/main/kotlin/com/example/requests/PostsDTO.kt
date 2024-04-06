package com.example.requests

import kotlinx.serialization.Serializable

@Serializable
data class PostsDTO(
    val username : String,
    val image : String,
    val description: String,
    val time : Long,
    val id : String
)
