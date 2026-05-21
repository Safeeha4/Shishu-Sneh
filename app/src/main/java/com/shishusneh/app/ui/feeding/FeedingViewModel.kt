package com.shishusneh.app.ui.feeding

import android.app.Application
import androidx.lifecycle.*
import com.shishusneh.app.ShishuSnehApplication
import com.shishusneh.app.data.local.entities.FeedingSession
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.*

class FeedingViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = (application as ShishuSnehApplication).feedingRepository
    private val babyRepository = (application as ShishuSnehApplication).babyRepository

    private val _sessions = MutableLiveData<List<FeedingSession>>(emptyList())
    val sessions: LiveData<List<FeedingSession>> = _sessions

    private var babyId: String? = null

    init {
        viewModelScope.launch {
            babyId = babyRepository.getPrimaryProfile()?.id
            babyId?.let { id ->
                repository.getRecentSessions(id).collectLatest {
                    _sessions.postValue(it)
                }
            }
        }
    }

    fun logFeeding(duration: Int, breast: String, note: String?) {
        val id = babyId ?: return
        viewModelScope.launch {
            val session = FeedingSession(
                babyId = id,
                startTime = Date(),
                duration = duration,
                breast = breast,
                note = note
            )
            repository.insertSession(session)
        }
    }

    fun deleteSession(session: FeedingSession) {
        viewModelScope.launch {
            repository.deleteSession(session)
        }
    }
}
