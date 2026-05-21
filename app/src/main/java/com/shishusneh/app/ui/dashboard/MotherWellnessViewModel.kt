package com.shishusneh.app.ui.dashboard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.shishusneh.app.data.remote.firebase.RemoteConfigService
import com.shishusneh.app.data.remote.firebase.WellnessContent
import com.shishusneh.app.data.remote.firebase.WellnessSection

class MotherWellnessViewModel(application: Application) : AndroidViewModel(application) {

    private val _dietContent = MutableLiveData<WellnessContent?>()
    val dietContent: LiveData<WellnessContent?> = _dietContent

    private val _exerciseContent = MutableLiveData<WellnessContent?>()
    val exerciseContent: LiveData<WellnessContent?> = _exerciseContent

    private val _mentalHealthContent = MutableLiveData<WellnessContent?>()
    val mentalHealthContent: LiveData<WellnessContent?> = _mentalHealthContent

    private val _dailyTip = MutableLiveData<String>()
    val dailyTip: LiveData<String> = _dailyTip

    private val fallbackTips = listOf(
        "Drink water every time you breastfeed. Stay hydrated for better milk production! 💧",
        "Nap when your baby naps. Rest is essential for your recovery and mood.",
        "Eat iron-rich foods like spinach and beans to boost your energy levels.",
        "Take a 10-minute walk outside. Fresh air and movement can work wonders.",
        "Don't be afraid to ask for help with chores. Focus on your baby and your health.",
        "Practice deep breathing exercises for 5 minutes daily to manage stress.",
        "Maintain a healthy diet with plenty of protein and calcium.",
        "Listen to your favorite music to relax and uplift your mood."
    )

    init {
        loadContent()
        _dailyTip.value = fallbackTips.random()
    }

    private fun loadContent() {
        // Attempt to load from remote config
        val diet = RemoteConfigService.getWellnessContent("diet", "en")
        val exercise = RemoteConfigService.getWellnessContent("exercise", "en")
        val mental = RemoteConfigService.getWellnessContent("mental", "en")

        // Set values with fallback data if remote config is empty
        _dietContent.value = diet ?: WellnessContent(
            "Diet & Nutrition",
            listOf(
                WellnessSection("Postnatal Nutrition", "Eating well after birth is crucial for your recovery and for providing quality breast milk.", listOf("Eat iron-rich foods like spinach, beetroots, and legumes.", "Include lactation-boosting foods like fenugreek (methi) and garlic.", "Aim for 3-4 liters of water daily.")),
                WellnessSection("Foods to Avoid", "While you need extra calories, try to limit certain items.", listOf("Excessive caffeine", "Highly processed or very spicy foods", "Raw or undercooked seafood"))
            )
        )

        _exerciseContent.value = exercise ?: WellnessContent(
            "Exercise & Rest",
            listOf(
                WellnessSection("Gentle Recovery", "Start slowly. Your body has been through a lot.", listOf("Pelvic floor (Kegel) exercises help with recovery.", "Short 10-minute walks around the house.", "Avoid heavy lifting for at least 6 weeks.")),
                WellnessSection("Importance of Rest", "Rest is as important as exercise.", listOf("Sleep when the baby sleeps.", "Accept help from family for chores.", "Short naps can significantly improve your mood."))
            )
        )

        _mentalHealthContent.value = mental ?: WellnessContent(
            "Mental Wellness",
            listOf(
                WellnessSection("Postpartum Emotions", "It's normal to feel a range of emotions after delivery.", listOf("The 'Baby Blues' usually last a few days to two weeks.", "Mood swings and tearfulness are common.", "Anxiety about the baby's health is normal but should be shared.")),
                WellnessSection("When to Seek Help", "If feelings of sadness or anxiety become overwhelming, reach out.", listOf("Persistent sadness lasting more than two weeks.", "Loss of interest in activities you used to enjoy.", "Difficulty bonding with your baby.", "Thoughts of harming yourself or the baby."))
            )
        )
    }
}
