package com.example.model.collegeAdmin

import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq

class CADSImpl(db : CoroutineDatabase) : CollegeAdminDataSource {
    private val admin = db.getCollection<Admin>()
    override suspend fun getAdminByEmail(username: String): Admin? {
        return admin.findOne(Admin::email eq username)
    }

    override suspend fun insertUser(user: Admin): Boolean {
        return admin.insertOne(user).wasAcknowledged()
    }

}