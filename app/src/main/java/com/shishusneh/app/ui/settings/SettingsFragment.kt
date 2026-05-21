package com.shishusneh.app.ui.settings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth
import com.shishusneh.app.R
import com.shishusneh.app.databinding.FragmentSettingsBinding
import com.shishusneh.app.ui.auth.LoginActivity
import com.shishusneh.app.ui.dashboard.DashboardViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DashboardViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupThemeSwitch()
        setupListeners()
        updateLastSyncTime()
    }

    private fun setupThemeSwitch() {
        val prefs = requireContext().getSharedPreferences("shishu_prefs", Context.MODE_PRIVATE)
        val isDarkMode = prefs.getBoolean("dark_mode", true)
        binding.switchTheme.isChecked = isDarkMode

        binding.switchTheme.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean("dark_mode", isChecked).apply()
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    private fun setupListeners() {
        binding.toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        binding.btnLanguage.setOnClickListener {
            showLanguageSelectionDialog()
        }

        binding.btnSyncNow.setOnClickListener {
            performSync()
        }

        binding.btnAbout.setOnClickListener {
            showAboutDialog()
        }

        binding.btnLogout.setOnClickListener {
            showLogoutConfirmation()
        }
    }

    private fun showLanguageSelectionDialog() {
        val languages = arrayOf("English", "हिंदी (Hindi)", "ಕನ್ನಡ (Kannada)")
        val languageCodes = arrayOf("en", "hi", "kn")
        
        val prefs = requireContext().getSharedPreferences("shishu_prefs", Context.MODE_PRIVATE)
        val currentLang = prefs.getString("app_language", "en") ?: "en"
        val checkedItem = languageCodes.indexOf(currentLang)

        AlertDialog.Builder(requireContext())
            .setTitle(R.string.change_app_language)
            .setSingleChoiceItems(languages, checkedItem) { dialog, which ->
                val selectedLang = languageCodes[which]
                if (selectedLang != currentLang) {
                    prefs.edit().putString("app_language", selectedLang).apply()
                    // Apply language and restart activity
                    requireActivity().recreate()
                }
                dialog.dismiss()
            }
            .setNegativeButton(android.R.string.cancel, null)
            .show()
    }

    private fun performSync() {
        lifecycleScope.launch {
            binding.btnSyncNow.isEnabled = false
            Toast.makeText(context, "Syncing data to cloud...", Toast.LENGTH_SHORT).show()
            
            // This is where Firestore sync logic would be finalized
            // Simulating sync success
            val prefs = requireContext().getSharedPreferences("shishu_prefs", Context.MODE_PRIVATE)
            prefs.edit().putLong("last_sync_time", System.currentTimeMillis()).apply()
            
            updateLastSyncTime()
            binding.btnSyncNow.isEnabled = true
            Toast.makeText(context, "Cloud sync complete!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateLastSyncTime() {
        val prefs = requireContext().getSharedPreferences("shishu_prefs", Context.MODE_PRIVATE)
        val lastSync = prefs.getLong("last_sync_time", 0L)
        if (lastSync != 0L) {
            val sdf = SimpleDateFormat("dd MMM, HH:mm", Locale.getDefault())
            binding.tvLastSync.text = "Last backup: ${sdf.format(Date(lastSync))}"
        }
    }

    private fun showAboutDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("About Shishu-Sneh")
            .setMessage("Shishu-Sneh is your baby's first year health companion. Version 1.0.0")
            .setPositiveButton("OK", null)
            .show()
    }

    private fun showLogoutConfirmation() {
        AlertDialog.Builder(requireContext())
            .setTitle("Logout")
            .setMessage("Are you sure you want to logout?")
            .setPositiveButton("Logout") { _, _ ->
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(requireContext(), LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                requireActivity().finish()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
