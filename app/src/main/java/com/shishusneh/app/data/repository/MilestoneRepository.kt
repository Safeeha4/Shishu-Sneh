package com.shishusneh.app.data.repository

import com.shishusneh.app.data.local.dao.MilestoneLogDao
import com.shishusneh.app.data.local.entities.MilestoneLog
import kotlinx.coroutines.flow.Flow

class MilestoneRepository(private val milestoneLogDao: MilestoneLogDao) {
    fun getMilestonesByBaby(babyId: String): Flow<List<MilestoneLog>> = milestoneLogDao.getMilestonesByBaby(babyId)
    suspend fun getMilestonesForWeek(babyId: String, week: Int): List<MilestoneLog> = milestoneLogDao.getMilestonesForWeek(babyId, week)
    suspend fun insertMilestone(milestone: MilestoneLog) = milestoneLogDao.insertMilestone(milestone)
    suspend fun getDelayedCount(babyId: String, week: Int): Int = milestoneLogDao.getDelayedCount(babyId, week)
}
