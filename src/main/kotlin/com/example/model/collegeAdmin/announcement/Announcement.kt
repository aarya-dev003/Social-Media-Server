package com.example.model.collegeAdmin.announcement

import kotlinx.serialization.Serializable
import org.bson.types.ObjectId

@Serializable
data class Announcement(
    val image : String,
    val description : String,
    val timestamp : Long,
    val id : String = ObjectId.get().toString()
)

