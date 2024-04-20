package com.example.model.collegeAdmin.announcement

interface AnnouncementDataSource {
    suspend fun createAnnouncement(announcement: Announcement): Boolean

    suspend fun updateAnnouncement(announcement: Announcement): Boolean

    suspend fun deleteAnnouncement(announcementId: String): Boolean

    suspend fun getAnnouncements(): List<Announcement>

    suspend fun getAnnouncement(announcementId: String): Announcement?
}