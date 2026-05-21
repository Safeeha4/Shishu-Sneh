package com.shishusneh.app.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date
import java.util.UUID

@Entity(tableName = "baby_profiles")
data class BabyProfile(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val name: String,
    val dateOfBirth: Date,
    val birthWeight: Double, // kg
    val birthHeight: Double?, // cm
    val gender: String, // "male" or "female"
    val bloodGroup: String? = null,
    val photoUri: String? = null,
    val createdAt: Date = Date(),
    val updatedAt: Date = Date()
)
