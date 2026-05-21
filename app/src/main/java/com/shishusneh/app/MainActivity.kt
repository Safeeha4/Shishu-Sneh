package com.shishusneh.app

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.shishusneh.app.ui.splash.SplashActivity

/**
 * Redundant activity. The app uses SplashActivity as the entry point 
 * and DashboardActivity as the main container.
 */
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Redirect to splash if somehow opened
        startActivity(Intent(this, SplashActivity::class.java))
        finish()
    }
}
