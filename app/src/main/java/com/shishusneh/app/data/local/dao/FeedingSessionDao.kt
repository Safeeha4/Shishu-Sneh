package com.shishusneh.app.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.shishusneh.app.data.local.entities.FeedingSession
import kotlinx.coroutines.flow.Flow

@Dao
interface FeedingSessionDao {
    @Query("SELECT * FROM feeding_sessions WHERE babyId = :babyId ORDER BY startTime DESC")
    fun getRecentSessions(babyId: String): Flow<List<FeedingSession>>
    
    @Insert
    suspend fun insertSession(session: FeedingSession)
    
    @Delete
    suspend fun deleteSession(session: FeedingSession)
}
