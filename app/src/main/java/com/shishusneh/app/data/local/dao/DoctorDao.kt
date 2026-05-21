package com.shishusneh.app.data.local.dao

import androidx.room.*
import com.shishusneh.app.data.local.entities.Doctor
import kotlinx.coroutines.flow.Flow

@Dao
interface DoctorDao {
    @Query("SELECT * FROM doctors")
    fun getAllDoctors(): Flow<List<Doctor>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDoctor(doctor: Doctor)

    @Delete
    suspend fun deleteDoctor(doctor: Doctor)

    @Query("SELECT * FROM doctors WHERE id = :id")
    suspend fun getDoctorById(id: String): Doctor?

    @Query("SELECT * FROM doctors WHERE doctorId = :doctorId LIMIT 1")
    suspend fun getDoctorByLoginId(doctorId: String): Doctor?
}
