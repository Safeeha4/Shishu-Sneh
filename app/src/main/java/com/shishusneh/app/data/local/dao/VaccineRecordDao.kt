package com.shishusneh.app.data.local.dao

import androidx.room.*
import com.shishusneh.app.data.local.entities.VaccineRecord
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface VaccineRecordDao {
    @Query("SELECT * FROM vaccine_records WHERE babyId = :babyId ORDER BY dueDate ASC")
    fun getVaccinesByBaby(babyId: String): Flow<List<VaccineRecord>>
    
    @Query("SELECT * FROM vaccine_records WHERE babyId = :babyId AND status = 'PENDING' ORDER BY dueDate ASC LIMIT 1")
    fun getNextPendingVaccine(babyId: String): Flow<VaccineRecord?>
    
    @Query("SELECT * FROM vaccine_records WHERE status = 'PENDING' AND dueDate <= :date")
    suspend fun getUpcomingVaccines(date: Date): List<VaccineRecord>

    @Query("SELECT * FROM vaccine_records WHERE id = :id")
    suspend fun getVaccineById(id: String): VaccineRecord?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVaccine(vaccine: VaccineRecord)
    
    @Update
    suspend fun updateVaccine(vaccine: VaccineRecord)
    
    @Query("UPDATE vaccine_records SET status = 'COMPLETED', givenDate = :givenDate WHERE id = :vaccineId")
    suspend fun markAsCompleted(vaccineId: String, givenDate: Date)
}
