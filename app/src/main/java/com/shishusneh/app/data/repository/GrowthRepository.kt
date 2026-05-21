package com.shishusneh.app.data.repository

import com.shishusneh.app.data.local.dao.GrowthEntryDao
import com.shishusneh.app.data.local.entities.GrowthEntry
import kotlinx.coroutines.flow.Flow

class GrowthRepository(private val growthEntryDao: GrowthEntryDao) {
    fun getEntriesByBaby(babyId: String): Flow<List<GrowthEntry>> = growthEntryDao.getEntriesByBaby(babyId)
    suspend fun getLatestEntry(babyId: String): GrowthEntry? = growthEntryDao.getLatestEntry(babyId)
    suspend fun insertEntry(entry: GrowthEntry) = growthEntryDao.insertEntry(entry)
    suspend fun deleteEntry(entry: GrowthEntry) = growthEntryDao.deleteEntry(entry)
    suspend fun deleteAllForBaby(babyId: String) = growthEntryDao.deleteAllForBaby(babyId)
}
