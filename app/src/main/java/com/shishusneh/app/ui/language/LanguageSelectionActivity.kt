package com.shishusneh.app.ui.language

import android.content.Intent
import android.os.Bundle
import com.shishusneh.app.R
import com.shishusneh.app.databinding.ActivityLanguageSelectionBinding
import com.shishusneh.app.ui.common.BaseActivity
import com.shishusneh.app.ui.profile.BabyProfileSetupActivity
import com.shishusneh.app.utils.LocaleHelper

class LanguageSelectionActivity : BaseActivity() {

    private lateinit var binding: ActivityLanguageSelectionBinding
    private var selectedLanguage: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLanguageSelectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()
    }

    private fun setupListeners() {
        binding.cardEnglish.setOnClickListener { selectLanguage("en") }
        binding.cardHindi.setOnClickListener { selectLanguage("hi") }
        binding.cardKannada.setOnClickListener { selectLanguage("kn") }

        binding.btnContinue.setOnClickListener {
            if (selectedLanguage != null) {
                getSharedPreferences("shishu_prefs", MODE_PRIVATE)
                    .edit().putString("app_language", selectedLanguage).apply()
                
                // Re-apply locale for the next activity
                LocaleHelper.setLocale(this, selectedLanguage!!)
                
                startActivity(Intent(this, BabyProfileSetupActivity::class.java))
                finish()
            }
        }

        binding.btnSkip.setOnClickListener {
            startActivity(Intent(this, BabyProfileSetupActivity::class.java))
            finish()
        }
    }

    private fun selectLanguage(code: String) {
        selectedLanguage = code
        
        // Reset borders
        binding.cardEnglish.setStrokeColor(getColorStateList(R.color.card_bg))
        binding.cardHindi.setStrokeColor(getColorStateList(R.color.card_bg))
        binding.cardKannada.setStrokeColor(getColorStateList(R.color.card_bg))

        // Highlight selected
        val accentColor = getColorStateList(R.color.accent_primary)
        when (code) {
            "en" -> binding.cardEnglish.setStrokeColor(accentColor)
            "hi" -> binding.cardHindi.setStrokeColor(accentColor)
            "kn" -> binding.cardKannada.setStrokeColor(accentColor)
        }
    }
}
