package com.shishusneh.app.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date
import java.util.UUID

@Entity(
    tableName = "milestone_logs",
    foreignKeys = [
        ForeignKey(
            entity = BabyProfile::class,
            parentColumns = ["id"],
            childColumns = ["babyId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("babyId"), Index("week")]
)
data class MilestoneLog(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val babyId: String,
    val milestoneId: String,
    val week: Int,
    val category: String, // PHYSICAL, COGNITIVE, SOCIAL, COMMUNICATION
    val question: String,
    val answer: String, // YES, NO, SKIPPED
    val answeredAt: Date = Date()
)
