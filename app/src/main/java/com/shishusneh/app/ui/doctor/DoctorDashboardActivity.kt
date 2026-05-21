package com.shishusneh.app.ui.doctor

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.shishusneh.app.R
import com.shishusneh.app.databinding.ActivityDoctorDashboardBinding
import com.shishusneh.app.ui.auth.LoginActivity
import com.shishusneh.app.ui.common.BaseActivity

class DoctorDashboardActivity : BaseActivity() {

    private lateinit var binding: ActivityDoctorDashboardBinding
    private val viewModel: DoctorViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDoctorDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        // Get logged in doctor info
        val prefs = getSharedPreferences("shishu_prefs", Context.MODE_PRIVATE)
        val loggedDoctorId = prefs.getString("logged_doctor_id", null)
        
        if (loggedDoctorId != null) {
            viewModel.loadDoctor(loggedDoctorId)
        }

        setupAdapters()
        observeViewModel()
    }

    private fun setupAdapters() {
        // Patients Adapter
        val patientAdapter = PatientAdapter { baby ->
            val intent = Intent(this, DoctorPatientDetailActivity::class.java)
            intent.putExtra("baby_id", baby.id)
            startActivity(intent)
        }
        binding.rvPatients.apply {
            adapter = patientAdapter
            layoutManager = LinearLayoutManager(this@DoctorDashboardActivity)
        }

        // Appointments Adapter - Pass true for doctor view
        val appointmentAdapter = AppointmentAdapter(isDoctorView = true)
        binding.rvAppointments.apply {
            adapter = appointmentAdapter
            layoutManager = LinearLayoutManager(this@DoctorDashboardActivity)
        }
    }

    private fun observeViewModel() {
        viewModel.currentDoctor.observe(this) { doctor ->
            doctor?.let {
                binding.tvWelcomeDoctor.text = getString(R.string.welcome_doctor, it.name)
            }
        }

        viewModel.allProfiles.observe(this) { profiles ->
            if (profiles.isNullOrEmpty()) {
                binding.tvEmptyState.visibility = View.VISIBLE
                binding.rvPatients.visibility = View.GONE
            } else {
                binding.tvEmptyState.visibility = View.GONE
                binding.rvPatients.visibility = View.VISIBLE
                (binding.rvPatients.adapter as? PatientAdapter)?.submitList(profiles)
            }
        }

        viewModel.appointments.observe(this) { appointments ->
            if (appointments.isNullOrEmpty()) {
                binding.tvEmptyAppointments.visibility = View.VISIBLE
                binding.rvAppointments.visibility = View.GONE
            } else {
                binding.tvEmptyAppointments.visibility = View.GONE
                binding.rvAppointments.visibility = View.VISIBLE
                (binding.rvAppointments.adapter as? AppointmentAdapter)?.submitList(appointments)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_doctor_dashboard, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                logout()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun logout() {
        getSharedPreferences("shishu_prefs", Context.MODE_PRIVATE).edit().apply {
            remove("is_doctor_logged_in")
            remove("logged_doctor_id")
            apply()
        }
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
