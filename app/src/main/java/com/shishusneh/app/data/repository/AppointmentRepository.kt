package com.shishusneh.app.data.repository

import com.shishusneh.app.data.local.dao.AppointmentDao
import com.shishusneh.app.data.local.entities.Appointment
import kotlinx.coroutines.flow.Flow

class AppointmentRepository(private val appointmentDao: AppointmentDao) {
    fun getAppointmentsForBaby(babyId: String): Flow<List<Appointment>> = 
        appointmentDao.getAppointmentsForBaby(babyId)

    suspend fun insertAppointment(appointment: Appointment) = 
        appointmentDao.insertAppointment(appointment)
        
    suspend fun updateAppointment(appointment: Appointment) = 
        appointmentDao.updateAppointment(appointment)

    suspend fun deleteAppointment(appointment: Appointment) = 
        appointmentDao.deleteAppointment(appointment)
}
