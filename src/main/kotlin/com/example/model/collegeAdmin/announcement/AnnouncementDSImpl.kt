package com.example.model.collegeAdmin.announcement

import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq

class AnnouncementDSImpl (db : CoroutineDatabase): AnnouncementDataSource {
    private val news = db.getCollection<Announcement>()

    override suspend fun createAnnouncement(announcement: Announcement): Boolean {
        return news.insertOne(announcement).wasAcknowledged()
    }

    override suspend fun updateAnnouncement(announcement: Announcement): Boolean {
        return news.updateOne(Announcement::id eq announcement.id, announcement).wasAcknowledged()
    }

    override suspend fun deleteAnnouncement(announcementId: String): Boolean {
        return news.deleteOne(Announcement::id eq announcementId).wasAcknowledged()
    }

    override suspend fun getAnnouncements(): List<Announcement> {
        return news.find().descendingSort(Announcement :: timestamp).toList()
    }

    override suspend fun getAnnouncement(announcementId: String): Announcement? {
        return news.findOne(Announcement::id eq announcementId)
    }
}