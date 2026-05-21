package com.shishusneh.app.ui.dashboard

import android.app.Application
import androidx.lifecycle.*
import com.shishusneh.app.ShishuSnehApplication
import com.shishusneh.app.data.local.entities.*
import com.shishusneh.app.data.remote.firebase.FirestoreService
import com.shishusneh.app.workers.VaccinationReminderWorker
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.*

class DashboardViewModel(application: Application) : AndroidViewModel(application) {

    private val babyRepository = (application as ShishuSnehApplication).babyRepository
    private val vaccinationRepository = (application as ShishuSnehApplication).vaccinationRepository
    private val consultationRepository = (application as ShishuSnehApplication).consultationRepository
    private val appointmentRepository = (application as ShishuSnehApplication).appointmentRepository
    private val growthRepository = (application as ShishuSnehApplication).growthRepository
    private val feedingRepository = (application as ShishuSnehApplication).feedingRepository

    private val _babyProfile = MutableLiveData<BabyProfile?>(null)
    val babyProfile: LiveData<BabyProfile?> = _babyProfile

    private val _nextVaccine = MutableLiveData<VaccineRecord?>(null)
    val nextVaccine: LiveData<VaccineRecord?> = _nextVaccine

    private val _appointments = MutableLiveData<List<Appointment>>()
    val appointments: LiveData<List<Appointment>> = _appointments

    private val _consultations = MutableLiveData<List<Consultation>>(emptyList())
    val consultations: LiveData<List<Consultation>> = _consultations

    // Observed by DashboardFragment to show the latest doctor guidance
    val latestAdvice: LiveData<Consultation?> = _consultations.map { list ->
        list.filter { !it.advice.isNullOrBlank() }.maxByOrNull { it.date }
    }

    private val _motivation = MutableLiveData<String>()
    val motivation: LiveData<String> = _motivation

    private val motivations = listOf(
        "A mother's arms are made of tenderness and children sleep soundly in them.",
        "The best start for a baby is a healthy and happy mother. Take care of yourself too!",
        "Every giggle and coo is a tiny 'thank you' for the amazing care you provide.",
        "Your love is the most essential nutrient for your baby's growth.",
        "Celebrate every milestone, no matter how small. They are all miracles."
    )

    init {
        loadData()
        rotateMotivation()
    }

    private fun loadData() {
        viewModelScope.launch {
            babyRepository.getPrimaryProfileFlow().collectLatest { baby ->
                _babyProfile.postValue(baby)
                baby?.let { b ->
                    launch {
                        vaccinationRepository.getNextPendingVaccine(b.id).collectLatest { vaccine ->
                            _nextVaccine.postValue(vaccine)
                        }
                    }
                    launch {
                        appointmentRepository.getAppointmentsForBaby(b.id).collectLatest { list ->
                            _appointments.postValue(list)
                        }
                    }
                    launch {
                        consultationRepository.getConsultationsForBaby(b.id).collectLatest { list ->
                            _consultations.postValue(list)
                        }
                    }
                }
            }
        }
    }

    fun rotateMotivation() {
        _motivation.value = motivations.random()
    }

    fun markVaccineAsDone(vaccineId: String) {
        viewModelScope.launch {
            vaccinationRepository.markAsCompleted(vaccineId, Date())
            VaccinationReminderWorker.cancel(getApplication(), vaccineId)
        }
    }

    fun deleteProfile(onDeleted: () -> Unit) {
        viewModelScope.launch {
            _babyProfile.value?.let {
                babyRepository.deleteProfile(it)
                _babyProfile.postValue(null)
                onDeleted()
            }
        }
    }

    suspend fun getFullHealthData(): Triple<BabyProfile, List<GrowthEntry>, List<VaccineRecord>>? {
        val baby = _babyProfile.value ?: return null
        val growth = growthRepository.getEntriesByBaby(baby.id).first()
        val vaccines = vaccinationRepository.getVaccinesByBaby(baby.id).first()
        return Triple(baby, growth, vaccines)
    }

    suspend fun syncAllData(firestoreService: FirestoreService): Boolean {
        val baby = _babyProfile.value ?: return false
        
        return try {
            val growthEntries = growthRepository.getEntriesByBaby(baby.id).first()
            val vaccines = vaccinationRepository.getVaccinesByBaby(baby.id).first()
            val feedingSessions = feedingRepository.getRecentSessions(baby.id).first()
            
            firestoreService.backupBabyData(baby, growthEntries, vaccines, feedingSessions)
        } catch (e: Exception) {
            false
        }
    }
}
