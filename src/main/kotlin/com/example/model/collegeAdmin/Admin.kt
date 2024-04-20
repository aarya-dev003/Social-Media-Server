package com.example.model.collegeAdmin

import io.ktor.server.auth.*
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class Admin(
    @BsonId
    val username : String,
    @BsonId
    val name : String,
    @BsonId
    val email : String ,
    val password : String,
    val salt : String ,
    @BsonId
    val id : ObjectId = ObjectId()
): Principal