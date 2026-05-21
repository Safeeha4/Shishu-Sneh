package com.shishusneh.app.ui.vaccination

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.shishusneh.app.R
import com.shishusneh.app.ShishuSnehApplication
import com.shishusneh.app.data.local.entities.Appointment
import com.shishusneh.app.databinding.FragmentVaccineBookingBinding
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class VaccineBookingFragment : Fragment() {

    private var _binding: FragmentVaccineBookingBinding? = null
    private val binding get() = _binding!!
    private val calendar = Calendar.getInstance()
    private val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
    
    private val appointmentRepository by lazy { 
        (requireActivity().application as ShishuSnehApplication).appointmentRepository 
    }
    private val babyRepository by lazy { 
        (requireActivity().application as ShishuSnehApplication).babyRepository 
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVaccineBookingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val vaccineName = arguments?.getString("vaccineName") ?: "Vaccine"
        binding.tvVaccineDetail.text = vaccineName
        binding.tvTitle.text = getString(R.string.book_appt)

        setupDatePicker()
        setupHealthCenters()

        binding.btnConfirmBooking.setOnClickListener {
            confirmBooking(vaccineName)
        }
    }

    private fun confirmBooking(vaccineName: String) {
        val dateStr = binding.etBookingDate.text.toString()
        if (dateStr.isEmpty()) {
            Toast.makeText(context, getString(R.string.select_date), Toast.LENGTH_SHORT).show()
            return
        }

        val center = binding.spinnerHealthCenter.selectedItem.toString()

        lifecycleScope.launch {
            val baby = babyRepository.getPrimaryProfile()
            if (baby != null) {
                val appointment = Appointment(
                    babyId = baby.id,
                    doctorId = "CENTER_APPOINTMENT", // Flag for general health center appointments
                    doctorName = center,
                    purpose = "Vaccination: $vaccineName",
                    appointmentDate = calendar.time,
                    status = "SCHEDULED"
                )
                appointmentRepository.insertAppointment(appointment)
                
                Toast.makeText(context, "Appointment confirmed for $vaccineName at $center", Toast.LENGTH_LONG).show()
                findNavController().navigateUp()
            } else {
                Toast.makeText(context, "Error: No baby profile found", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupDatePicker() {
        binding.etBookingDate.setOnClickListener {
            val datePicker = DatePickerDialog(
                requireContext(),
                { _, year, month, dayOfMonth ->
                    calendar.set(year, month, dayOfMonth)
                    binding.etBookingDate.setText(dateFormat.format(calendar.time))
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePicker.datePicker.minDate = System.currentTimeMillis()
            datePicker.show()
        }
    }

    private fun setupHealthCenters() {
        val centers = listOf(
            "Primary Health Center (PHC) - Main Branch",
            "Community Health Center (CHC) - South Sector",
            "District Government Hospital",
            "Sishu Seva Clinic",
            "Asha Worker Outreach Point"
        )
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, centers)
        binding.spinnerHealthCenter.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
