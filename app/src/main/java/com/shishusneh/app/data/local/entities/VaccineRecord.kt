package com.shishusneh.app.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date
import java.util.UUID

@Entity(
    tableName = "vaccine_records",
    foreignKeys = [
        ForeignKey(
            entity = BabyProfile::class,
            parentColumns = ["id"],
            childColumns = ["babyId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("babyId"), Index("dueDate")]
)
data class VaccineRecord(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val babyId: String,
    val name: String,
    val dueDate: Date,
    val givenDate: Date? = null,
    val status: String, // PENDING, COMPLETED, SKIPPED, OVERDUE
    val diseases: String, // Comma separated list
    val dosageNumber: Int,
    val ageWeeks: Int,
    val notes: String? = null,
    val createdAt: Date = Date()
)
