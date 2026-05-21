package com.shishusneh.app.ui.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.shishusneh.app.ShishuSnehApplication
import com.shishusneh.app.data.local.entities.BabyProfile
import com.shishusneh.app.utils.VaccinationScheduleGenerator
import com.shishusneh.app.workers.MilestoneReminderWorker
import com.shishusneh.app.workers.VaccinationReminderWorker
import kotlinx.coroutines.launch
import java.util.Date

class BabyProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val babyRepository = (application as ShishuSnehApplication).babyRepository
    private val vaccinationRepository = (application as ShishuSnehApplication).vaccinationRepository

    fun createProfile(
        name: String,
        dob: Date,
        weight: Double,
        height: Double?,
        gender: String,
        photoUri: String?,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            val baby = BabyProfile(
                name = name,
                dateOfBirth = dob,
                birthWeight = weight,
                birthHeight = height,
                gender = gender,
                photoUri = photoUri
            )
            babyRepository.insertProfile(baby)

            // 1. Auto-generate vaccination schedule & reminders
            val schedule = VaccinationScheduleGenerator.generateSchedule(baby.id, dob)
            schedule.forEach { 
                vaccinationRepository.insertVaccine(it)
                VaccinationReminderWorker.schedule(getApplication(), it.id, it.name, it.dueDate)
            }

            // 2. Schedule Weekly Milestone Reminders
            MilestoneReminderWorker.scheduleWeekly(getApplication(), baby.id)

            onSuccess()
        }
    }
}
