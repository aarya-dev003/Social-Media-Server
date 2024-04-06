package com.example.requests

import kotlinx.serialization.Serializable

@Serializable
data class ClubLoginRequest(
    val username : String,
    val password : String
)
