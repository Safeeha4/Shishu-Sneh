package com.shishusneh.app.ui.growth

import android.app.Application
import androidx.lifecycle.*
import com.shishusneh.app.ShishuSnehApplication
import com.shishusneh.app.data.local.entities.BabyProfile
import com.shishusneh.app.data.local.entities.GrowthEntry
import kotlinx.coroutines.launch
import java.util.Date

class GrowthViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = (application as ShishuSnehApplication).growthRepository
    private val babyRepository = (application as ShishuSnehApplication).babyRepository

    private val _babyProfile = MutableLiveData<BabyProfile?>(null)
    val babyProfile: LiveData<BabyProfile?> = _babyProfile
    
    val growthEntries: LiveData<List<GrowthEntry>> = _babyProfile.switchMap { baby ->
        if (baby == null) MutableLiveData(emptyList())
        else repository.getEntriesByBaby(baby.id).asLiveData()
    }

    init {
        viewModelScope.launch {
            _babyProfile.value = babyRepository.getPrimaryProfile()
        }
    }

    fun addEntry(weight: Double, height: Double?, date: Date, note: String?) {
        viewModelScope.launch {
            _babyProfile.value?.let { baby ->
                val entry = GrowthEntry(
                    babyId = baby.id,
                    date = date,
                    weight = weight,
                    height = height,
                    note = note
                )
                repository.insertEntry(entry)
            }
        }
    }
}
