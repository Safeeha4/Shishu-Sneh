package com.shishusneh.app.ui.doctor

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import coil.transform.CircleCropTransformation
import com.shishusneh.app.R
import com.shishusneh.app.databinding.ActivityDoctorPatientDetailBinding
import com.shishusneh.app.ui.common.BaseActivity
import com.shishusneh.app.ui.growth.GrowthHistoryAdapter
import com.shishusneh.app.ui.vaccination.VaccinationAdapter
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class DoctorPatientDetailActivity : BaseActivity() {

    private lateinit var binding: ActivityDoctorPatientDetailBinding
    private val viewModel: DoctorViewModel by viewModels()
    private val calendar = Calendar.getInstance()
    private var loggedDoctorId: String? = null
    private var currentBabyName: String = "Patient"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDoctorPatientDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val babyId = intent.getStringExtra("baby_id")
        if (babyId == null) {
            finish()
            return
        }

        // Get logged in doctor info
        val prefs = getSharedPreferences("shishu_prefs", Context.MODE_PRIVATE)
        loggedDoctorId = prefs.getString("logged_doctor_id", null)
        
        loggedDoctorId?.let { viewModel.loadDoctor(it) }

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { finish() }

        setupBabyDetails(babyId)
        setupGrowthHistory(babyId)
        setupVaccinationStatus(babyId)
        setupConsultationHistory(babyId)

        binding.btnSaveConsultation.setOnClickListener {
            val notes = binding.etConsultationNotes.text.toString()
            val advice = binding.etConsultationAdvice.text.toString()
            
            if (notes.isNotBlank() || advice.isNotBlank()) {
                val doctorId = loggedDoctorId ?: "DOC123"
                viewModel.saveConsultation(babyId, doctorId, notes, advice.ifBlank { null })
                Toast.makeText(this, getString(R.string.consultation_saved), Toast.LENGTH_SHORT).show()
                binding.etConsultationNotes.text?.clear()
                binding.etConsultationAdvice.text?.clear()
            } else {
                Toast.makeText(this, getString(R.string.enter_notes_error), Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnBookAppointment.setOnClickListener {
            showDatePicker(babyId)
        }
    }

    private fun setupBabyDetails(babyId: String) {
        viewModel.allProfiles.observe(this) { profiles ->
            val baby = profiles.find { it.id == babyId }
            baby?.let {
                currentBabyName = it.name
                binding.tvName.text = it.name
                binding.tvAge.text = calculateAge(it.dateOfBirth)
                binding.tvGender.text = it.gender.replaceFirstChar { char -> char.uppercase() }
                binding.tvWeightHeight.text = getString(R.string.birth_weight_height_format, it.birthWeight.toString(), (it.birthHeight ?: 0.0).toString())

                if (!it.photoUri.isNullOrEmpty()) {
                    binding.ivBabyPhoto.load(it.photoUri) {
                        crossfade(true)
                        transformations(CircleCropTransformation())
                        error(R.drawable.logo_intern)
                    }
                }
            }
        }
    }

    private fun setupGrowthHistory(babyId: String) {
        val adapter = GrowthHistoryAdapter()
        binding.rvGrowthHistory.apply {
            this.adapter = adapter
            layoutManager = LinearLayoutManager(this@DoctorPatientDetailActivity)
            isNestedScrollingEnabled = false
        }
        viewModel.getGrowthHistoryForBaby(babyId).observe(this) { entries ->
            adapter.submitList(entries.sortedByDescending { it.date })
        }
    }

    private fun setupVaccinationStatus(babyId: String) {
        val adapter = VaccinationAdapter(
            onMarkDone = { _ ->
                Toast.makeText(this, getString(R.string.action_not_allowed_doctor), Toast.LENGTH_SHORT).show()
            },
            onBookAppointment = { _ ->
                Toast.makeText(this, getString(R.string.use_follow_up_hint), Toast.LENGTH_SHORT).show()
            }
        )
        binding.rvVaccinations.apply {
            this.adapter = adapter
            layoutManager = LinearLayoutManager(this@DoctorPatientDetailActivity)
            isNestedScrollingEnabled = false
        }
        viewModel.getVaccinationsForBaby(babyId).observe(this) { vaccines ->
            adapter.submitList(vaccines)
        }
    }

    private fun setupConsultationHistory(babyId: String) {
        val adapter = ConsultationAdapter()
        binding.rvConsultationHistory.apply {
            this.adapter = adapter
            layoutManager = LinearLayoutManager(this@DoctorPatientDetailActivity)
            isNestedScrollingEnabled = false
        }

        viewModel.getConsultationsForBaby(babyId).observe(this) { consultations ->
            if (consultations.isNullOrEmpty()) {
                binding.tvConsultationHistoryTitle.visibility = View.GONE
                binding.rvConsultationHistory.visibility = View.GONE
            } else {
                binding.tvConsultationHistoryTitle.visibility = View.VISIBLE
                binding.rvConsultationHistory.visibility = View.VISIBLE
                adapter.submitList(consultations)
            }
        }
    }

    private fun showDatePicker(babyId: String) {
        val datePicker = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                val appointmentDate = calendar.time
                val doctorId = loggedDoctorId ?: "DOC123"
                val doctorName = viewModel.currentDoctor.value?.name ?: "Dr. Professional"
                
                viewModel.bookAppointment(babyId, currentBabyName, doctorId, doctorName, "Follow-up", appointmentDate)
                val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                Toast.makeText(this, getString(R.string.appointment_booked_format, sdf.format(appointmentDate)), Toast.LENGTH_LONG).show()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePicker.datePicker.minDate = System.currentTimeMillis()
        datePicker.show()
    }

    private fun calculateAge(dob: Date): String {
        val diff = Date().time - dob.time
        val days = TimeUnit.MILLISECONDS.toDays(diff)
        val weeks = days / 7
        return if (weeks < 10) "$weeks weeks old" else "${weeks / 4} months old"
    }
}
