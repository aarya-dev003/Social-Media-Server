package com.example.requests

import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId

@Serializable
data class AdminRequest(
    @BsonId
    val username : String,
    @BsonId
    val name : String,
    @BsonId
    val email : String ,
    val password : String
)