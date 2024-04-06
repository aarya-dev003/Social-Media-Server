package com.example.model.admin

import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq

class ClubAdminImpl(db : CoroutineDatabase) : ClubAdminDataSource {
    private val club = db.getCollection<Club>()
    override suspend fun getAdminByEmail(username: String): Club? {
        return club.findOne(Club ::username eq username)
    }

    override suspend fun insertUser(user: Club): Boolean {
        return club.insertOne(user).wasAcknowledged()
    }
}