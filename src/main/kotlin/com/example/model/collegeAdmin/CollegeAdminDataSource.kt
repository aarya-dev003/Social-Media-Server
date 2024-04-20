package com.example.model.collegeAdmin

interface CollegeAdminDataSource {
    suspend fun getAdminByEmail(username: String) : Admin?

    suspend fun insertUser(user:Admin) : Boolean
}