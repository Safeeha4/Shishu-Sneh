package com.shishusneh.app.ui.doctor

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.shishusneh.app.ShishuSnehApplication
import com.shishusneh.app.databinding.ActivityDoctorLoginBinding
import com.shishusneh.app.ui.common.BaseActivity
import kotlinx.coroutines.launch

class DoctorLoginActivity : BaseActivity() {

    private lateinit var binding: ActivityDoctorLoginBinding
    private val doctorRepository by lazy { (application as ShishuSnehApplication).doctorRepository }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDoctorLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivBack.setOnClickListener {
            finish()
        }

        binding.btnLogin.setOnClickListener {
            performLogin()
        }
        
        // Option to pre-fill for demo/testing
        binding.tvTitle.setOnLongClickListener {
            binding.etDoctorId.setText("DOC123")
            binding.etPassword.setText("admin123")
            true
        }
    }

    private fun performLogin() {
        val doctorId = binding.etDoctorId.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()

        if (doctorId.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter both ID and Password", Toast.LENGTH_SHORT).show()
            return
        }

        binding.btnLogin.isEnabled = false
        
        lifecycleScope.launch {
            val doctor = doctorRepository.getDoctorByLoginId(doctorId)
            
            if (doctor != null && doctor.password == password) {
                // Save login state if needed (e.g., in SharedPreferences)
                getSharedPreferences("shishu_prefs", MODE_PRIVATE).edit().apply {
                    putBoolean("is_doctor_logged_in", true)
                    putString("logged_doctor_id", doctor.id)
                    apply()
                }
                
                startActivity(Intent(this@DoctorLoginActivity, DoctorDashboardActivity::class.java))
                finish()
            } else {
                Toast.makeText(this@DoctorLoginActivity, "Invalid Doctor ID or Password", Toast.LENGTH_SHORT).show()
                binding.btnLogin.isEnabled = true
            }
        }
    }
}
