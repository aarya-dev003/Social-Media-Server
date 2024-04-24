package com.example.model.feedback

import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq

class FeedbackDataSourceImpl(db: CoroutineDatabase): FeedbackDataSource {
    private val feedback = db.getCollection<Feedback>()
    override suspend fun createFeedback(feedbackRequest: Feedback): Boolean {
        return feedback.insertOne(feedbackRequest).wasAcknowledged()
    }

    override suspend fun getFeedbackClub(): List<Feedback> {
        val feedbacks = feedback.find()
            .filter(Feedback::issueType eq "club")
            .descendingSort(Feedback :: time)
            .toList()
        return feedbacks
    }

    override suspend fun getFeedbackAdmin(): List<Feedback> {
        return feedback.find()
            .filter(Feedback :: issueType eq "admin")
            .descendingSort(Feedback :: time)
            .toList()
    }
}