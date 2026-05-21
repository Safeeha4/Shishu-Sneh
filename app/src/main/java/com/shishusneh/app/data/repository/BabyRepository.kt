package com.shishusneh.app.data.repository

import com.shishusneh.app.data.local.dao.BabyProfileDao
import com.shishusneh.app.data.local.entities.BabyProfile
import kotlinx.coroutines.flow.Flow

class BabyRepository(private val babyProfileDao: BabyProfileDao) {
    fun getAllProfiles(): Flow<List<BabyProfile>> = babyProfileDao.getAllProfiles()
    fun getProfileById(babyId: String): Flow<BabyProfile?> = babyProfileDao.getProfileById(babyId)
    suspend fun insertProfile(profile: BabyProfile) = babyProfileDao.insertProfile(profile)
    suspend fun updateProfile(profile: BabyProfile) = babyProfileDao.updateProfile(profile)
    suspend fun deleteProfile(profile: BabyProfile) = babyProfileDao.deleteProfile(profile)
    suspend fun getPrimaryProfile(): BabyProfile? = babyProfileDao.getPrimaryProfile()
    fun getPrimaryProfileFlow(): Flow<BabyProfile?> = babyProfileDao.getPrimaryProfileFlow()
}
