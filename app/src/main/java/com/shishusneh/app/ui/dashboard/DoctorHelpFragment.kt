package com.shishusneh.app.ui.dashboard

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.shishusneh.app.R
import com.shishusneh.app.databinding.DialogAddDoctorBinding
import com.shishusneh.app.databinding.FragmentDoctorHelpBinding
import com.shishusneh.app.ui.doctor.AppointmentAdapter
import com.shishusneh.app.ui.doctor.ConsultationAdapter
import java.util.concurrent.TimeUnit

class DoctorHelpFragment : Fragment() {

    private var _binding: FragmentDoctorHelpBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DashboardViewModel by viewModels()
    private val doctorViewModel: DoctorHelpViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDoctorHelpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAppointmentsList()
        setupConsultationsList()
        setupDoctorsList()
        setupListeners()
    }

    private fun setupAppointmentsList() {
        val adapter = AppointmentAdapter()
        binding.rvAppointments.apply {
            this.adapter = adapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        viewModel.appointments.observe(viewLifecycleOwner) { appointments ->
            if (appointments.isNullOrEmpty()) {
                binding.tvEmptyAppointments.visibility = View.VISIBLE
                binding.rvAppointments.visibility = View.GONE
            } else {
                binding.tvEmptyAppointments.visibility = View.GONE
                binding.rvAppointments.visibility = View.VISIBLE
                adapter.submitList(appointments)
            }
        }
    }

    private fun setupConsultationsList() {
        val adapter = ConsultationAdapter()
        binding.rvConsultations.apply {
            this.adapter = adapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        viewModel.consultations.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
        }
    }

    private fun setupDoctorsList() {
        val adapter = DoctorAdapter(
            onCallClick = { doctor ->
                val intent = Intent(Intent.ACTION_DIAL).apply {
                    data = Uri.parse("tel:${doctor.phoneNumber}")
                }
                startActivity(intent)
            },
            onDeleteClick = { doctor ->
                doctorViewModel.deleteDoctor(doctor)
            }
        )
        binding.rvSavedDoctors.apply {
            this.adapter = adapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        doctorViewModel.savedDoctors.observe(viewLifecycleOwner) { doctors ->
            adapter.submitList(doctors)
            binding.tvNoDoctors.visibility = if (doctors.isEmpty()) View.VISIBLE else View.GONE
        }
    }

    private fun setupListeners() {
        binding.btnEmergencyCall.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:102") 
            }
            startActivity(intent)
        }

        binding.btnBookConsultation.setOnClickListener {
            showAddDoctorDialog()
        }

        binding.btnShareStatus.setOnClickListener {
            shareBabyStatus()
        }
    }

    private fun showAddDoctorDialog() {
        val dialogBinding = DialogAddDoctorBinding.inflate(layoutInflater)
        AlertDialog.Builder(requireContext())
            .setTitle("Add Doctor Details")
            .setView(dialogBinding.root)
            .setPositiveButton("Add") { _, _ ->
                val name = dialogBinding.etDoctorName.text.toString()
                val spec = dialogBinding.etSpecialization.text.toString()
                val phone = dialogBinding.etPhone.text.toString()
                
                if (name.isNotBlank() && phone.isNotBlank()) {
                    doctorViewModel.addDoctor(name, spec, phone)
                } else {
                    Toast.makeText(context, "Name and Phone are required", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun shareBabyStatus() {
        val baby = viewModel.babyProfile.value
        if (baby == null) {
            Toast.makeText(context, "No baby profile found to share", Toast.LENGTH_SHORT).show()
            return
        }

        val age = calculateAge(baby.dateOfBirth)
        val shareText = "Baby Status Update:\nName: ${baby.name}\nAge: $age\nBirth Weight: ${baby.birthWeight}kg\nShared via Shishu-Sneh App"

        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, shareText)
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }

    private fun calculateAge(dob: java.util.Date): String {
        val diff = java.util.Date().time - dob.time
        val days = TimeUnit.MILLISECONDS.toDays(diff)
        val weeks = days / 7
        return if (weeks < 10) "$weeks weeks old" else "${weeks / 4} months old"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
