package com.example.requests

import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId

@Serializable
data class AuthRequest(
    @BsonId
    val username : String,
    val name : String,
    @BsonId
    val email : String ,
    val password : String
)
