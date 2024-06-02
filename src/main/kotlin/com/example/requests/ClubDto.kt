package com.example.requests

import kotlinx.serialization.Serializable

@Serializable
data class ClubDto (
    val username : String,
    val name : String,
    val email : String ,
    val imageUrl : String?
)