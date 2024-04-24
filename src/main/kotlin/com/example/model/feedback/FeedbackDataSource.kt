package com.example.model.feedback

interface FeedbackDataSource {
    suspend fun createFeedback(feedbackRequest: Feedback): Boolean
    suspend fun getFeedbackClub(): List<Feedback>
    suspend fun getFeedbackAdmin(): List<Feedback>
}