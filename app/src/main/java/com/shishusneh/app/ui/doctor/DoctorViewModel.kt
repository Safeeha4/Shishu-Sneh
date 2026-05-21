package com.shishusneh.app.ui.doctor

import android.app.Application
import androidx.lifecycle.*
import com.shishusneh.app.ShishuSnehApplication
import com.shishusneh.app.data.local.entities.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Date

class DoctorViewModel(application: Application) : AndroidViewModel(application) {
    private val babyRepository = (application as ShishuSnehApplication).babyRepository
    private val consultationRepository = (application as ShishuSnehApplication).consultationRepository
    private val appointmentRepository = (application as ShishuSnehApplication).appointmentRepository
    private val vaccinationRepository = (application as ShishuSnehApplication).vaccinationRepository
    private val growthRepository = (application as ShishuSnehApplication).growthRepository
    private val doctorRepository = (application as ShishuSnehApplication).doctorRepository
    
    val allProfiles: LiveData<List<BabyProfile>> = babyRepository.getAllProfiles().asLiveData()

    private val _currentDoctor = MutableLiveData<Doctor?>()
    val currentDoctor: LiveData<Doctor?> = _currentDoctor

    private val _appointments = MutableLiveData<List<Appointment>>(emptyList())
    val appointments: LiveData<List<Appointment>> = _appointments

    fun loadDoctor(id: String) {
        viewModelScope.launch {
            _currentDoctor.postValue(doctorRepository.getDoctorById(id))
            
            // Also load appointments for this doctor
            doctorRepository.getAppointmentsForDoctor(id).collectLatest {
                _appointments.postValue(it)
            }
        }
    }

    fun getConsultationsForBaby(babyId: String): LiveData<List<Consultation>> = 
        consultationRepository.getConsultationsForBaby(babyId).asLiveData()

    fun getVaccinationsForBaby(babyId: String): LiveData<List<VaccineRecord>> =
        vaccinationRepository.getVaccinesByBaby(babyId).asLiveData()

    fun getGrowthHistoryForBaby(babyId: String): LiveData<List<GrowthEntry>> =
        growthRepository.getEntriesByBaby(babyId).asLiveData()

    fun saveConsultation(babyId: String, doctorId: String, notes: String, advice: String?) {
        viewModelScope.launch {
            val consultation = Consultation(
                babyId = babyId,
                doctorId = doctorId,
                notes = notes,
                advice = advice,
                date = Date()
            )
            consultationRepository.insertConsultation(consultation)
        }
    }

    fun bookAppointment(babyId: String, babyName: String, doctorId: String, doctorName: String, purpose: String, date: Date) {
        viewModelScope.launch {
            val appointment = Appointment(
                babyId = babyId,
                babyName = babyName,
                doctorId = doctorId,
                doctorName = doctorName,
                purpose = purpose,
                appointmentDate = date
            )
            appointmentRepository.insertAppointment(appointment)
        }
    }
}
