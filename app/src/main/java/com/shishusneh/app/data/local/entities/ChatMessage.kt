package com.shishusneh.app.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date
import java.util.UUID

@Entity(tableName = "chat_messages")
data class ChatMessage(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val userId: String,
    val role: String, // "user" or "model"
    val message: String,
    val mediaUri: String? = null,
    val mediaType: String = "TEXT", // "TEXT", "IMAGE", "AUDIO"
    val language: String,
    val timestamp: Date = Date()
)
