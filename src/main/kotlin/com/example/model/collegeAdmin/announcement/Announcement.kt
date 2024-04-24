package com.example.model.collegeAdmin.announcement

import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@Serializable
data class Announcement(
    val description : String,
    val timestamp : Long,
    @BsonId
    val id : String = ObjectId.get().toString()
)

