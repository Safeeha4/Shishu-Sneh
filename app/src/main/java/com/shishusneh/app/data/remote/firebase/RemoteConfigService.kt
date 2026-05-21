package com.shishusneh.app.data.remote.firebase

import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.tasks.await

object RemoteConfigService {
    
    private val remoteConfig = Firebase.remoteConfig
    private val gson = Gson()
    
    init {
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 3600
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
    }
    
    suspend fun fetchAndActivate(): Boolean {
        return try {
            remoteConfig.fetchAndActivate().await()
        } catch (e: Exception) {
            false
        }
    }
    
    fun getDailyMotivation(language: String): String {
        return try {
            val json = remoteConfig.getString("daily_motivations_$language")
            if (json.isEmpty()) return "You're doing great! Every day you care for your baby is a step toward health."
            val list = gson.fromJson(json, Array<String>::class.java)
            list.random()
        } catch (e: Exception) {
            "You're doing great! Every day you care for your baby is a step toward health."
        }
    }

    fun getMilestonesForWeek(week: Int, language: String): List<MilestoneData> {
        return try {
            val json = remoteConfig.getString("milestones_week_$week")
            if (json.isEmpty() || json == "[]") return emptyList()
            val rawList = gson.fromJson(json, Array<RawMilestoneData>::class.java)
            rawList.map { it.toLocaleData(language) }
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun getWellnessContent(category: String, language: String): WellnessContent? {
        return try {
            val json = remoteConfig.getString("mother_wellness_${category}_$language")
            if (json.isEmpty()) return null
            gson.fromJson(json, WellnessContent::class.java)
        } catch (e: Exception) {
            null
        }
    }
}

private data class RawMilestoneData(
    val id: String,
    val category: String,
    @SerializedName("question_en") val questionEn: String,
    @SerializedName("question_kn") val questionKn: String,
    val critical: Boolean
) {
    fun toLocaleData(language: String) = MilestoneData(
        id = id,
        category = category,
        question = if (language == "kn") questionKn else questionEn,
        critical = critical
    )
}

data class MilestoneData(
    val id: String,
    val category: String,
    val question: String,
    val critical: Boolean
)

data class WellnessContent(
    val title: String,
    val sections: List<WellnessSection>
)

data class WellnessSection(
    val heading: String,
    val content: String,
    val items: List<String>? = null
)
