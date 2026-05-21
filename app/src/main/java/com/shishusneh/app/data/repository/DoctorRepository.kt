package com.shishusneh.app.data.repository

import com.shishusneh.app.data.local.dao.AppointmentDao
import com.shishusneh.app.data.local.dao.DoctorDao
import com.shishusneh.app.data.local.entities.Appointment
import com.shishusneh.app.data.local.entities.Doctor
import kotlinx.coroutines.flow.Flow

class DoctorRepository(
    private val doctorDao: DoctorDao,
    private val appointmentDao: AppointmentDao
) {
    val allDoctors: Flow<List<Doctor>> = doctorDao.getAllDoctors()

    suspend fun insertDoctor(doctor: Doctor) {
        doctorDao.insertDoctor(doctor)
    }

    suspend fun deleteDoctor(doctor: Doctor) {
        doctorDao.deleteDoctor(doctor)
    }

    suspend fun getDoctorById(id: String): Doctor? {
        return doctorDao.getDoctorById(id)
    }

    suspend fun getDoctorByLoginId(doctorId: String): Doctor? {
        return doctorDao.getDoctorByLoginId(doctorId)
    }

    fun getAppointmentsForDoctor(doctorId: String): Flow<List<Appointment>> {
        return appointmentDao.getAppointmentsForDoctor(doctorId)
    }
}
