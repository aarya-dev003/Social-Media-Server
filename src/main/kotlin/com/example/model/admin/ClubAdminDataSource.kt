package com.example.model.admin

interface ClubAdminDataSource {
    suspend fun getAdminByEmail(username: String) : Club?

    suspend fun insertUser(user: Club) : Boolean
}