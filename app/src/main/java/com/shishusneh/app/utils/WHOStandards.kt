package com.shishusneh.app.utils

import android.content.Context
import com.google.gson.Gson
import com.shishusneh.app.data.local.entities.BabyProfile
import java.io.InputStreamReader

object WHOStandards {
    
    private var boysWeightData: Map<Int, Percentiles>? = null
    private var girlsWeightData: Map<Int, Percentiles>? = null
    
    data class Percentiles(
        val month: Int,
        val p3: Double,
        val p50: Double,
        val p97: Double
    )
    
    fun initialize(context: Context) {
        if (boysWeightData == null) {
            boysWeightData = loadFromJson(context, "who_standards_boys_weight.json")
            girlsWeightData = loadFromJson(context, "who_standards_girls_weight.json")
        }
    }
    
    private fun loadFromJson(context: Context, fileName: String): Map<Int, Percentiles> {
        return try {
            val inputStream = context.assets.open(fileName)
            val reader = InputStreamReader(inputStream)
            val data = Gson().fromJson(reader, Array<Percentiles>::class.java)
            data.associateBy { it.month }
        } catch (e: Exception) {
            emptyMap()
        }
    }
    
    fun getWeightPercentiles(gender: String, ageMonths: Int): Percentiles? {
        val data = if (gender.lowercase() == "male") boysWeightData else girlsWeightData
        return data?.get(ageMonths)
    }
    
    fun assessGrowthStatus(
        currentWeight: Double,
        gender: String,
        ageMonths: Int
    ): GrowthStatus {
        val percentiles = getWeightPercentiles(gender, ageMonths) ?: return GrowthStatus.UNKNOWN
        
        return when {
            currentWeight < percentiles.p3 -> GrowthStatus.BELOW_NORMAL
            currentWeight > percentiles.p97 -> GrowthStatus.ABOVE_NORMAL
            else -> GrowthStatus.HEALTHY
        }
    }
    
    enum class GrowthStatus {
        BELOW_NORMAL, HEALTHY, ABOVE_NORMAL, UNKNOWN
    }
}
