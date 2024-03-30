package com.example.model.post

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId


data class Post(
    val username: String,
    val image: String,
    val description: String,
    val time: Long,
    @BsonId
    val id: ObjectId = ObjectId()
)
