package com.shishusneh.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.shishusneh.app.data.local.entities.MilestoneLog
import kotlinx.coroutines.flow.Flow

@Dao
interface MilestoneLogDao {
    @Query("SELECT * FROM milestone_logs WHERE babyId = :babyId ORDER BY week ASC")
    fun getMilestonesByBaby(babyId: String): Flow<List<MilestoneLog>>
    
    @Query("SELECT * FROM milestone_logs WHERE babyId = :babyId AND week = :week")
    suspend fun getMilestonesForWeek(babyId: String, week: Int): List<MilestoneLog>
    
    @Insert
    suspend fun insertMilestone(milestone: MilestoneLog)
    
    @Query("SELECT COUNT(*) FROM milestone_logs WHERE babyId = :babyId AND answer = 'NO' AND week = :week")
    suspend fun getDelayedCount(babyId: String, week: Int): Int
}
