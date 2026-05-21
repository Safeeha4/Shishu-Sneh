package com.shishusneh.app.data.local.dao

import androidx.room.*
import com.shishusneh.app.data.local.entities.BabyProfile
import kotlinx.coroutines.flow.Flow

@Dao
interface BabyProfileDao {
    @Query("SELECT * FROM baby_profiles ORDER BY createdAt DESC")
    fun getAllProfiles(): Flow<List<BabyProfile>>
    
    @Query("SELECT * FROM baby_profiles WHERE id = :babyId")
    fun getProfileById(babyId: String): Flow<BabyProfile?>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profile: BabyProfile)
    
    @Update
    suspend fun updateProfile(profile: BabyProfile)
    
    @Delete
    suspend fun deleteProfile(profile: BabyProfile)
    
    @Query("SELECT * FROM baby_profiles LIMIT 1")
    suspend fun getPrimaryProfile(): BabyProfile?

    @Query("SELECT * FROM baby_profiles LIMIT 1")
    fun getPrimaryProfileFlow(): Flow<BabyProfile?>
}
