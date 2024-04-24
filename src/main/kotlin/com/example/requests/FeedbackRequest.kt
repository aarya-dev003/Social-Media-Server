package com.example.requests

import kotlinx.serialization.Serializable

@Serializable
data class FeedbackRequest(
    val issue : String,
    val issueType: String,
    val time: Long
)
