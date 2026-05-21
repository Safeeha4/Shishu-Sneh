package com.shishusneh.app

import android.app.Application
import com.shishusneh.app.data.local.database.ShishuSnehDb
import com.shishusneh.app.data.local.entities.Doctor
import com.shishusneh.app.data.remote.firebase.FirestoreService
import com.shishusneh.app.data.repository.*
import com.shishusneh.app.utils.WHOStandards
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class ShishuSnehApplication : Application() {

    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    private val database by lazy { ShishuSnehDb.getDatabase(this) }
    
    val babyRepository by lazy { BabyRepository(database.babyProfileDao()) }
    val growthRepository by lazy { GrowthRepository(database.growthEntryDao()) }
    val vaccinationRepository by lazy { VaccinationRepository(database.vaccineRecordDao()) }
    val milestoneRepository by lazy { MilestoneRepository(database.milestoneLogDao()) }
    val chatRepository by lazy { ChatRepository(database.chatMessageDao()) }
    val consultationRepository by lazy { ConsultationRepository(database.consultationDao()) }
    val appointmentRepository by lazy { AppointmentRepository(database.appointmentDao()) }
    val doctorRepository by lazy { DoctorRepository(database.doctorDao(), database.appointmentDao()) }
    val feedingRepository by lazy { FeedingRepository(database.feedingSessionDao()) }
    
    val firestoreService by lazy { FirestoreService() }

    override fun onCreate() {
        super.onCreate()
        WHOStandards.initialize(this)
        seedDatabase()
    }

    private fun seedDatabase() {
        applicationScope.launch(Dispatchers.IO) {
            // Seed a default doctor for demo/testing purposes
            val existingDoctor = doctorRepository.getDoctorByLoginId("DOC123")
            if (existingDoctor == null) {
                val defaultDoctor = Doctor(
                    doctorId = "DOC123",
                    password = "admin123",
                    name = "Dr. Sameer Shishu",
                    qualification = "MD, MBBS",
                    specialization = "Senior Pediatrician",
                    experienceYears = 15,
                    registrationNumber = "MCI-12345",
                    phoneNumber = "+91 9876543210",
                    email = "dr.sameer@shishusneh.com",
                    clinicAddress = "Shishu Care Center, Block B, New Delhi"
                )
                doctorRepository.insertDoctor(defaultDoctor)
            }
        }
    }
}
