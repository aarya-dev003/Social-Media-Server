package com.example.model.collegeAdmin.feedback

interface MessageDataSource {
    suspend fun getAllMessages() : List<Message>

    suspend fun insertMessages(message: Message)
}