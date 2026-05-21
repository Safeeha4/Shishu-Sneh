package com.shishusneh.app.data.local.dao

import androidx.room.*
import com.shishusneh.app.data.local.entities.Consultation
import kotlinx.coroutines.flow.Flow

@Dao
interface ConsultationDao {
    @Query("SELECT * FROM consultations WHERE babyId = :babyId ORDER BY date DESC")
    fun getConsultationsForBaby(babyId: String): Flow<List<Consultation>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConsultation(consultation: Consultation)

    @Delete
    suspend fun deleteConsultation(consultation: Consultation)
}
