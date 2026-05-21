package com.shishusneh.app.ui.profile

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.load
import coil.transform.CircleCropTransformation
import com.shishusneh.app.R
import com.shishusneh.app.ShishuSnehApplication
import com.shishusneh.app.databinding.DialogEditBirthDetailsBinding
import com.shishusneh.app.databinding.FragmentBabyProfileDetailBinding
import com.shishusneh.app.ui.dashboard.DashboardViewModel
import com.shishusneh.app.ui.language.LanguageSelectionActivity
import com.shishusneh.app.utils.HealthReportGenerator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class BabyProfileDetailFragment : Fragment() {

    private var _binding: FragmentBabyProfileDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DashboardViewModel by viewModels()
    private val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
    private val babyRepository by lazy { (requireActivity().application as ShishuSnehApplication).babyRepository }
    private val firestoreService by lazy { (requireActivity().application as ShishuSnehApplication).firestoreService }

    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { updateProfilePicture(it) }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBabyProfileDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.babyProfile.observe(viewLifecycleOwner) { baby ->
            baby?.let {
                binding.tvName.text = it.name
                binding.tvAge.text = calculateAge(it.dateOfBirth)
                binding.tvDob.text = dateFormat.format(it.dateOfBirth)
                binding.tvBirthWeight.text = getString(R.string.weight_format, it.birthWeight.toString())
                binding.tvGender.text = it.gender.replaceFirstChar { char -> char.uppercase() }
                
                it.birthHeight?.let { height ->
                    binding.tvCurrentHeight.text = getString(R.string.height_format, height.toString())
                } ?: run {
                    binding.tvCurrentHeight.text = getString(R.string.not_available)
                }

                if (!it.photoUri.isNullOrEmpty()) {
                    binding.ivBabyPhoto.load(it.photoUri) {
                        crossfade(true)
                        transformations(CircleCropTransformation())
                        error(R.drawable.logo_intern)
                    }
                }
            }
        }

        binding.ivBabyPhoto.setOnClickListener {
            pickImage.launch("image/*")
        }
        
        binding.btnEditBirthDetails.setOnClickListener {
            showEditBirthDetailsDialog()
        }

        binding.btnViewGrowth.setOnClickListener {
            findNavController().navigate(R.id.navigation_growth)
        }
        
        binding.btnDownloadReport.setOnClickListener {
            generateAndShareReport()
        }

        binding.btnSyncCloud.setOnClickListener {
            performCloudBackup()
        }
        
        binding.btnDeleteProfile.setOnClickListener {
            showDeleteConfirmation()
        }

        binding.btnChangeLanguage.setOnClickListener {
            showLanguageSelectionDialog()
        }

        binding.btnNotifications.setOnClickListener {
            findNavController().navigate(R.id.navigation_settings)
        }
        
        updateLastBackupText()
    }

    private fun showEditBirthDetailsDialog() {
        val baby = viewModel.babyProfile.value ?: return
        val dialogBinding = DialogEditBirthDetailsBinding.inflate(layoutInflater)
        
        // Pre-fill
        dialogBinding.etName.setText(baby.name)
        val dialogDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        dialogBinding.etDob.setText(dialogDateFormat.format(baby.dateOfBirth))
        dialogBinding.etWeight.setText(baby.birthWeight.toString())
        dialogBinding.etHeight.setText(baby.birthHeight?.toString() ?: "")

        val calendar = Calendar.getInstance().apply { time = baby.dateOfBirth }

        dialogBinding.etDob.setOnClickListener {
            DatePickerDialog(
                requireContext(),
                { _, year, month, dayOfMonth ->
                    calendar.set(year, month, dayOfMonth)
                    dialogBinding.etDob.setText(dialogDateFormat.format(calendar.time))
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).apply {
                datePicker.maxDate = System.currentTimeMillis()
                show()
            }
        }

        AlertDialog.Builder(requireContext())
            .setView(dialogBinding.root)
            .setPositiveButton("Update") { _, _ ->
                val newName = dialogBinding.etName.text.toString().trim()
                val newWeight = dialogBinding.etWeight.text.toString().toDoubleOrNull() ?: baby.birthWeight
                val newHeight = dialogBinding.etHeight.text.toString().toDoubleOrNull()
                
                if (newName.isNotEmpty()) {
                    val updatedBaby = baby.copy(
                        name = newName,
                        dateOfBirth = calendar.time,
                        birthWeight = newWeight,
                        birthHeight = newHeight,
                        updatedAt = Date()
                    )
                    lifecycleScope.launch(Dispatchers.IO) {
                        babyRepository.updateProfile(updatedBaby)
                        withContext(Dispatchers.Main) {
                            Toast.makeText(context, "Profile updated", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun performCloudBackup() {
        lifecycleScope.launch {
            binding.btnSyncCloud.isEnabled = false
            Toast.makeText(context, "Backing up health records...", Toast.LENGTH_SHORT).show()
            
            val success = viewModel.syncAllData(firestoreService)
            
            if (success) {
                val prefs = requireContext().getSharedPreferences("shishu_prefs", Context.MODE_PRIVATE)
                prefs.edit().putLong("last_backup_time", System.currentTimeMillis()).apply()
                updateLastBackupText()
                Toast.makeText(context, "Backup successful!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Backup failed. Please check internet.", Toast.LENGTH_LONG).show()
            }
            binding.btnSyncCloud.isEnabled = true
        }
    }

    private fun updateLastBackupText() {
        val prefs = requireContext().getSharedPreferences("shishu_prefs", Context.MODE_PRIVATE)
        val lastBackup = prefs.getLong("last_backup_time", 0L)
        if (lastBackup != 0L) {
            val sdf = SimpleDateFormat("dd MMM, HH:mm", Locale.getDefault())
            binding.tvLastBackup.text = "Last backup: ${sdf.format(Date(lastBackup))}"
        }
    }

    private fun generateAndShareReport() {
        lifecycleScope.launch {
            Toast.makeText(context, "Generating Health Report PDF...", Toast.LENGTH_SHORT).show()
            val data = viewModel.getFullHealthData()
            if (data != null) {
                val (baby, growth, vaccines) = data
                val file = withContext(Dispatchers.IO) {
                    HealthReportGenerator.generateReport(requireContext(), baby, growth, vaccines)
                }
                
                if (file != null && file.exists()) {
                    sharePdf(file)
                } else {
                    Toast.makeText(context, "Failed to generate report", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "No health data available", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun sharePdf(file: File) {
        val uri = FileProvider.getUriForFile(
            requireContext(),
            "${requireContext().packageName}.fileprovider",
            file
        )
        
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "application/pdf"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        startActivity(Intent.createChooser(intent, "Share Health Report"))
    }

    private fun showDeleteConfirmation() {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.delete_baby_profile)
            .setMessage("Are you sure you want to delete this profile? This action cannot be undone.")
            .setPositiveButton("Delete") { _, _ ->
                viewModel.deleteProfile {
                    Toast.makeText(context, "Profile deleted successfully", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(requireContext(), LanguageSelectionActivity::class.java))
                    requireActivity().finish()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
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
                    updateLanguage(selectedLang)
                }
                dialog.dismiss()
            }
            .setNegativeButton(android.R.string.cancel, null)
            .show()
    }

    private fun updateLanguage(langCode: String) {
        val prefs = requireContext().getSharedPreferences("shishu_prefs", Context.MODE_PRIVATE)
        prefs.edit().putString("app_language", langCode).apply()

        // Restart activity to apply changes
        val intent = requireActivity().intent
        requireActivity().finish()
        startActivity(intent)
    }

    private fun updateProfilePicture(uri: Uri) {
        lifecycleScope.launch {
            val currentBaby = viewModel.babyProfile.value
            currentBaby?.let {
                val updatedBaby = it.copy(photoUri = uri.toString(), updatedAt = Date())
                withContext(Dispatchers.IO) {
                    babyRepository.updateProfile(updatedBaby)
                }
                binding.ivBabyPhoto.load(uri) {
                    transformations(CircleCropTransformation())
                }
                Toast.makeText(context, "Profile picture updated successfully!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun calculateAge(dob: Date): String {
        val diff = Date().time - dob.time
        val days = TimeUnit.MILLISECONDS.toDays(diff)
        val weeks = days / 7
        return if (weeks < 10) "$weeks weeks old" else "${weeks / 4} months old"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
