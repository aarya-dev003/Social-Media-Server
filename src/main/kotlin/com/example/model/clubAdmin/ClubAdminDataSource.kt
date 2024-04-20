package com.example.model.clubAdmin

interface ClubAdminDataSource {
    suspend fun getAdminByEmail(username: String) : Club?

    suspend fun insertUser(user: Club) : Boolean
}