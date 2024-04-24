package com.example.model.feedback

import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@Serializable
data class Feedback(
    @BsonId
    val id: String = ObjectId.get().toString(),
    val username: String,
    val issue: String,
    val issueType: String,
    val time: Long
)
