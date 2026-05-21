package com.shishusneh.app.ui.dashboard

import android.app.Application
import androidx.lifecycle.*
import com.shishusneh.app.ShishuSnehApplication
import com.shishusneh.app.data.local.entities.MilestoneLog
import com.shishusneh.app.data.remote.firebase.MilestoneData
import com.shishusneh.app.data.remote.firebase.RemoteConfigService
import kotlinx.coroutines.launch
import java.util.Date

class MilestoneViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = (application as ShishuSnehApplication).milestoneRepository
    private val babyRepository = (application as ShishuSnehApplication).babyRepository

    private val _babyId = MutableLiveData<String?>()
    val babyId: LiveData<String?> = _babyId

    private val _currentWeek = MutableLiveData<Int>()
    val currentWeek: LiveData<Int> = _currentWeek

    private val _allMilestones = MutableLiveData<List<MilestoneData>>()
    
    private val _currentIndex = MutableLiveData(0)
    val currentIndex: LiveData<Int> = _currentIndex

    private val _currentMilestone = MediatorLiveData<MilestoneData?>().apply {
        addSource(_allMilestones) { milestones ->
            value = milestones.getOrNull(_currentIndex.value ?: 0)
        }
        addSource(_currentIndex) { index ->
            value = _allMilestones.value?.getOrNull(index)
        }
    }
    val currentMilestone: LiveData<MilestoneData?> = _currentMilestone

    val milestoneHistory: LiveData<List<MilestoneLog>> = _babyId.switchMap { id ->
        if (id == null) MutableLiveData(emptyList())
        else repository.getMilestonesByBaby(id).asLiveData()
    }

    init {
        viewModelScope.launch {
            val baby = babyRepository.getPrimaryProfile()
            _babyId.value = baby?.id
            baby?.let {
                val diff = Date().time - it.dateOfBirth.time
                val weeks = (diff / (1000 * 60 * 60 * 24 * 7)).toInt()
                _currentWeek.value = weeks
                loadCurrentMilestones(weeks)
            }
        }
    }

    private fun loadCurrentMilestones(week: Int) {
        val milestones = RemoteConfigService.getMilestonesForWeek(week, "en")
        if (milestones.isNotEmpty()) {
            _allMilestones.value = milestones
        } else {
            _allMilestones.value = getFallbackMilestones(week)
        }
    }

    private fun getFallbackMilestones(week: Int): List<MilestoneData> {
        return when {
            week >= 8 -> listOf(
                MilestoneData("m_8_p1", "👶 PHYSICAL", "Does baby hold head up briefly when on tummy?", true),
                MilestoneData("m_8_p2", "👶 PHYSICAL", "Moves arms and legs actively?", false),
                MilestoneData("m_8_c1", "🧠 COGNITIVE", "Does baby track objects with eyes?", false),
                MilestoneData("m_8_s1", "😊 SOCIAL", "Does baby smile at people?", false),
                MilestoneData("m_8_com1", "📢 COMMUNICATION", "Makes sounds other than crying?", false)
            )
            else -> listOf(
                MilestoneData("m_0_p1", "👶 PHYSICAL", "Does baby react to loud sounds?", true),
                MilestoneData("m_0_s1", "😊 SOCIAL", "Does baby look at your face?", false),
                MilestoneData("m_0_com1", "📢 COMMUNICATION", "Cries when hungry or uncomfortable?", false)
            )
        }
    }

    fun saveMilestone(milestone: MilestoneData, answer: String) {
        viewModelScope.launch {
            _babyId.value?.let { bId ->
                val log = MilestoneLog(
                    babyId = bId,
                    milestoneId = milestone.id,
                    week = _currentWeek.value ?: 0,
                    category = milestone.category,
                    question = milestone.question,
                    answer = answer,
                    answeredAt = Date()
                )
                repository.insertMilestone(log)
                
                // Move to next question after short delay in fragment
            }
        }
    }

    fun moveToNext() {
        val nextIndex = (_currentIndex.value ?: 0) + 1
        if (nextIndex < (_allMilestones.value?.size ?: 0)) {
            _currentIndex.value = nextIndex
        } else {
            // End of questions for this session
            _currentIndex.value = nextIndex // This will make currentMilestone null
        }
    }
    
    fun getMilestoneProgress(): Pair<Int, Int> {
        val total = _allMilestones.value?.size ?: 0
        val current = _currentIndex.value ?: 0
        return Pair(current, total)
    }
}
