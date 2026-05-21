package com.shishusneh.app.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date
import java.util.UUID

@Entity(
    tableName = "consultations",
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
data class Consultation(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val babyId: String,
    val doctorId: String,
    val notes: String,
    val advice: String? = null,
    val date: Date = Date()
)
