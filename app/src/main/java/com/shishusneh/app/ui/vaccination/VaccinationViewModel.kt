package com.shishusneh.app.ui.vaccination

import android.app.Application
import androidx.lifecycle.*
import com.shishusneh.app.ShishuSnehApplication
import com.shishusneh.app.data.local.entities.VaccineRecord
import com.shishusneh.app.workers.VaccinationReminderWorker
import kotlinx.coroutines.launch
import java.util.*

class VaccinationViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = (application as ShishuSnehApplication).vaccinationRepository
    private val babyRepository = (application as ShishuSnehApplication).babyRepository

    private val _babyId = MutableLiveData<String?>()

    val vaccines: LiveData<List<VaccineRecord>> = _babyId.switchMap { id ->
        if (id == null) MutableLiveData(emptyList())
        else repository.getVaccinesByBaby(id).asLiveData()
    }

    init {
        viewModelScope.launch {
            _babyId.value = babyRepository.getPrimaryProfile()?.id
        }
    }

    fun markAsCompleted(vaccineId: String) {
        viewModelScope.launch {
            repository.markAsCompleted(vaccineId, Date())
            // Cancel pending reminders for this vaccine
            VaccinationReminderWorker.cancel(getApplication(), vaccineId)
        }
    }
}
