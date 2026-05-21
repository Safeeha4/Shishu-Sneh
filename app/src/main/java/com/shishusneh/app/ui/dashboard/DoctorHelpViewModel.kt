package com.shishusneh.app.ui.dashboard

import android.app.Application
import androidx.lifecycle.*
import com.shishusneh.app.ShishuSnehApplication
import com.shishusneh.app.data.local.entities.Doctor
import kotlinx.coroutines.launch

class DoctorHelpViewModel(application: Application) : AndroidViewModel(application) {
    private val doctorRepository = (application as ShishuSnehApplication).doctorRepository

    val savedDoctors: LiveData<List<Doctor>> = doctorRepository.allDoctors.asLiveData()

    fun addDoctor(name: String, specialization: String, phone: String) {
        viewModelScope.launch {
            val doctor = Doctor(
                name = name,
                specialization = specialization,
                phoneNumber = phone
            )
            doctorRepository.insertDoctor(doctor)
        }
    }

    fun deleteDoctor(doctor: Doctor) {
        viewModelScope.launch {
            doctorRepository.deleteDoctor(doctor)
        }
    }
}
