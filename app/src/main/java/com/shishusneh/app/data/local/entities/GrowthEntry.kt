package com.shishusneh.app.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date
import java.util.UUID

@Entity(
    tableName = "growth_entries",
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
data class GrowthEntry(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val babyId: String,
    val date: Date,
    val weight: Double, // kg
    val height: Double?, // cm
    val note: String? = null,
    val createdAt: Date = Date()
)
