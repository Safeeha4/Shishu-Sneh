package com.shishusneh.app.data.repository

import com.shishusneh.app.data.local.dao.FeedingSessionDao
import com.shishusneh.app.data.local.entities.FeedingSession
import kotlinx.coroutines.flow.Flow

class FeedingRepository(private val feedingSessionDao: FeedingSessionDao) {
    fun getRecentSessions(babyId: String): Flow<List<FeedingSession>> = feedingSessionDao.getRecentSessions(babyId)
    suspend fun insertSession(session: FeedingSession) = feedingSessionDao.insertSession(session)
    suspend fun deleteSession(session: FeedingSession) = feedingSessionDao.deleteSession(session)
}
