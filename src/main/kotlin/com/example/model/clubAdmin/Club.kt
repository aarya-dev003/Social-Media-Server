package com.example.model.clubAdmin

import io.ktor.server.auth.*
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class Club(
    @BsonId
    val username : String,
    @BsonId
    val name : String,
    @BsonId
    val email : String ,
    val password : String,
    val salt : String ,
    @BsonId
    val id : ObjectId = ObjectId(),
    val imageUrl : String
): Principal
