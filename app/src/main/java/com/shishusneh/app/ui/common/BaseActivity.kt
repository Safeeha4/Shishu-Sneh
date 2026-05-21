package com.shishusneh.app.ui.common

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.shishusneh.app.utils.LocaleHelper

open class BaseActivity : AppCompatActivity() {
    override fun attachBaseContext(newBase: Context) {
        val prefs = newBase.getSharedPreferences("shishu_prefs", Context.MODE_PRIVATE)
        val language = prefs.getString("app_language", "en") ?: "en"
        super.attachBaseContext(LocaleHelper.setLocale(newBase, language))
    }
}
