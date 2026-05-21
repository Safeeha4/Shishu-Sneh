package com.shishusneh.app.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date
import java.util.UUID

@Entity(
    tableName = "feeding_sessions",
    foreignKeys = [
        ForeignKey(
            entity = BabyProfile::class,
            parentColumns = ["id"],
            childColumns = ["babyId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("babyId")]
)
data class FeedingSession(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val babyId: String,
    val startTime: Date = Date(),
    val duration: Int, // minutes
    val breast: String, // "LEFT", "RIGHT", "BOTH"
    val note: String? = null,
    val createdAt: Date = Date()
)
