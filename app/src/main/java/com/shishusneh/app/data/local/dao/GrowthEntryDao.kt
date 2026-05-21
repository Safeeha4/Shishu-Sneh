package com.shishusneh.app.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.shishusneh.app.data.local.entities.GrowthEntry
import kotlinx.coroutines.flow.Flow

@Dao
interface GrowthEntryDao {
    @Query("SELECT * FROM growth_entries WHERE babyId = :babyId ORDER BY date ASC")
    fun getEntriesByBaby(babyId: String): Flow<List<GrowthEntry>>
    
    @Query("SELECT * FROM growth_entries WHERE babyId = :babyId ORDER BY date DESC LIMIT 1")
    suspend fun getLatestEntry(babyId: String): GrowthEntry?
    
    @Insert
    suspend fun insertEntry(entry: GrowthEntry)
    
    @Delete
    suspend fun deleteEntry(entry: GrowthEntry)
    
    @Query("DELETE FROM growth_entries WHERE babyId = :babyId")
    suspend fun deleteAllForBaby(babyId: String)
}
