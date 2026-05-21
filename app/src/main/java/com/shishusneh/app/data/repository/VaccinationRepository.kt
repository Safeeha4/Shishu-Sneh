package com.shishusneh.app.data.repository

import com.shishusneh.app.data.local.dao.VaccineRecordDao
import com.shishusneh.app.data.local.entities.VaccineRecord
import kotlinx.coroutines.flow.Flow
import java.util.Date

class VaccinationRepository(private val vaccineRecordDao: VaccineRecordDao) {
    fun getVaccinesByBaby(babyId: String): Flow<List<VaccineRecord>> = vaccineRecordDao.getVaccinesByBaby(babyId)
    fun getNextPendingVaccine(babyId: String): Flow<VaccineRecord?> = vaccineRecordDao.getNextPendingVaccine(babyId)
    suspend fun getUpcomingVaccines(date: Date): List<VaccineRecord> = vaccineRecordDao.getUpcomingVaccines(date)
    suspend fun insertVaccine(vaccine: VaccineRecord) = vaccineRecordDao.insertVaccine(vaccine)
    suspend fun updateVaccine(vaccine: VaccineRecord) = vaccineRecordDao.updateVaccine(vaccine)
    suspend fun markAsCompleted(vaccineId: String, givenDate: Date) = vaccineRecordDao.markAsCompleted(vaccineId, givenDate)
}
