package com.example.requests

import kotlinx.serialization.Serializable

@Serializable
data class PostRequest (
    val username : String,
    val image : String,
    val description: String,
    val time : Long
)