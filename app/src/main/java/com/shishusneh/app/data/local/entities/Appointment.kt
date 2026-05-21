package com.shishusneh.app.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date
import java.util.UUID

@Entity(
    tableName = "appointments",
    foreignKeys = [
        ForeignKey(
            entity = BabyProfile::class,
            parentColumns = ["id"],
            childColumns = ["babyId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("babyId"), Index("doctorId")]
)
data class Appointment(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val babyId: String,
    val babyName: String = "", // Added for easy display in doctor dashboard
    val doctorId: String,
    val doctorName: String,
    val purpose: String,
    val appointmentDate: Date,
    val status: String = "SCHEDULED", // SCHEDULED, COMPLETED, CANCELLED
    val createdAt: Date = Date()
)
