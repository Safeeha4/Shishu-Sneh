package com.shishusneh.app.ui.splash

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.shishusneh.app.ShishuSnehApplication
import com.shishusneh.app.data.remote.firebase.RemoteConfigService
import com.shishusneh.app.databinding.ActivitySplashBinding
import com.shishusneh.app.ui.auth.LoginActivity
import com.shishusneh.app.ui.common.BaseActivity
import com.shishusneh.app.ui.dashboard.DashboardActivity
import com.shishusneh.app.ui.doctor.DoctorDashboardActivity
import com.shishusneh.app.ui.language.LanguageSelectionActivity
import kotlinx.coroutines.launch

class SplashActivity : BaseActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val quotes = listOf(
            "Bringing expert care to your fingertips...",
            "Your baby's health, our priority.",
            "Expert guidance for the first year.",
            "Nurturing the future, one step at a time."
        )
        binding.tvQuote.text = quotes.random()

        lifecycleScope.launch {
            // Load Remote Config as per SOP
            RemoteConfigService.fetchAndActivate()
            
            // Artificial delay for splash experience
            Handler(Looper.getMainLooper()).postDelayed({
                checkUserStatus()
            }, 2000)
        }
    }

    private fun checkUserStatus() {
        // 1. Check if a Doctor is logged in
        val prefs = getSharedPreferences("shishu_prefs", Context.MODE_PRIVATE)
        val isDoctorLoggedIn = prefs.getBoolean("is_doctor_logged_in", false)
        
        if (isDoctorLoggedIn) {
            startActivity(Intent(this, DoctorDashboardActivity::class.java))
            finish()
            return
        }

        // 2. Check Firebase User (Mother)
        val auth = Firebase.auth
        val currentUser = auth.currentUser

        if (currentUser == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        // 3. Check if Baby Profile exists
        val repository = (application as ShishuSnehApplication).babyRepository
        lifecycleScope.launch {
            val baby = repository.getPrimaryProfile()
            if (baby != null) {
                startActivity(Intent(this@SplashActivity, DashboardActivity::class.java))
            } else {
                startActivity(Intent(this@SplashActivity, LanguageSelectionActivity::class.java))
            }
            finish()
        }
    }
}
