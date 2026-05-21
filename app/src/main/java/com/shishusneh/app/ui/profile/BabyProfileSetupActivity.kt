package com.shishusneh.app.ui.profile

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.shishusneh.app.R
import com.shishusneh.app.databinding.ActivityBabyProfileSetupBinding
import com.shishusneh.app.ui.common.BaseActivity
import com.shishusneh.app.ui.dashboard.DashboardActivity
import java.text.SimpleDateFormat
import java.util.*

class BabyProfileSetupActivity : BaseActivity() {

    private lateinit var binding: ActivityBabyProfileSetupBinding
    private val viewModel: BabyProfileViewModel by viewModels()
    private val calendar = Calendar.getInstance()
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    private var selectedPhotoUri: Uri? = null

    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            selectedPhotoUri = it
            binding.ivBaby.setImageURI(it)
            binding.ivBaby.imageAlpha = 255
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBabyProfileSetupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()
        setupInputValidation()
    }

    private fun setupListeners() {
        binding.cardPhoto.setOnClickListener {
            pickImage.launch("image/*")
        }

        binding.etDob.setOnClickListener {
            showDatePicker()
        }

        binding.btnCreateProfile.setOnClickListener {
            validateAndCreate()
        }

        binding.btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setupInputValidation() {
        binding.etName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.tilName.error = null
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        binding.etWeight.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.tilWeight.error = null
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun showDatePicker() {
        val datePicker = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                binding.etDob.setText(dateFormat.format(calendar.time))
                binding.tilDob.error = null
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePicker.datePicker.maxDate = System.currentTimeMillis()
        datePicker.show()
    }

    private fun validateAndCreate() {
        val name = binding.etName.text.toString().trim()
        val dobStr = binding.etDob.text.toString()
        val weightStr = binding.etWeight.text.toString()
        val heightStr = binding.etHeight.text.toString()
        val bloodGroup = binding.etBloodGroup.text.toString().trim().ifEmpty { null }
        val gender = if (binding.btnMale.isChecked) "male" else if (binding.btnFemale.isChecked) "female" else ""

        var isValid = true

        if (name.length < 2) {
            binding.tilName.error = getString(R.string.error_name_short)
            isValid = false
        }
        
        if (dobStr.isEmpty()) {
            binding.tilDob.error = getString(R.string.error_dob_required)
            isValid = false
        }
        
        val weight = weightStr.toDoubleOrNull()
        if (weight == null || weight !in 1.0..15.0) {
            binding.tilWeight.error = getString(R.string.error_weight_invalid)
            isValid = false
        }
        
        if (gender.isEmpty()) {
            Toast.makeText(this, getString(R.string.error_gender_required), Toast.LENGTH_SHORT).show()
            isValid = false
        }

        if (!isValid) return

        val height = heightStr.toDoubleOrNull()

        viewModel.createProfile(
            name = name,
            dob = calendar.time,
            weight = weight ?: 0.0,
            height = height,
            gender = gender,
            bloodGroup = bloodGroup,
            photoUri = selectedPhotoUri?.toString()
        ) {
            startActivity(Intent(this, DashboardActivity::class.java))
            finish()
        }
    }
}
