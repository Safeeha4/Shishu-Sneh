package com.shishusneh.app.data.local.dao

import androidx.room.*
import com.shishusneh.app.data.local.entities.Appointment
import kotlinx.coroutines.flow.Flow

@Dao
interface AppointmentDao {
    @Query("SELECT * FROM appointments WHERE babyId = :babyId ORDER BY appointmentDate ASC")
    fun getAppointmentsForBaby(babyId: String): Flow<List<Appointment>>

    @Query("SELECT * FROM appointments WHERE doctorId = :doctorId ORDER BY appointmentDate ASC")
    fun getAppointmentsForDoctor(doctorId: String): Flow<List<Appointment>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAppointment(appointment: Appointment)

    @Update
    suspend fun updateAppointment(appointment: Appointment)

    @Delete
    suspend fun deleteAppointment(appointment: Appointment)
}
