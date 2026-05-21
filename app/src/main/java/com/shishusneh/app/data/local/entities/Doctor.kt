package com.shishusneh.app.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "doctors")
data class Doctor(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val doctorId: String = "", // Human-readable ID for login (e.g. DOC123)
    val password: String = "",
    val name: String,
    val qualification: String? = null, // e.g., MBBS, MD (Pediatrics)
    val specialization: String,
    val experienceYears: Int? = null,
    val registrationNumber: String? = null, // Medical Registration Number
    val phoneNumber: String,
    val photoUri: String? = null,
    val email: String? = null,
    val clinicAddress: String? = null
)
