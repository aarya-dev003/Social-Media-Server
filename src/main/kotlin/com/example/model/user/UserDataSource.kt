package com.example.model.user

interface UserDataSource {

    suspend fun getUserByEmail(email: String) : User?

    suspend fun insertUser(user: User) : Boolean
}