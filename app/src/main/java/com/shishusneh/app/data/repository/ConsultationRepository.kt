package com.shishusneh.app.data.repository

import com.shishusneh.app.data.local.dao.ConsultationDao
import com.shishusneh.app.data.local.entities.Consultation
import kotlinx.coroutines.flow.Flow

class ConsultationRepository(private val consultationDao: ConsultationDao) {
    fun getConsultationsForBaby(babyId: String): Flow<List<Consultation>> = 
        consultationDao.getConsultationsForBaby(babyId)

    suspend fun insertConsultation(consultation: Consultation) = 
        consultationDao.insertConsultation(consultation)
}
