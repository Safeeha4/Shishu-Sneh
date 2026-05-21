package com.shishusneh.app.ui.dashboard

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import coil.load
import coil.transform.CircleCropTransformation
import com.shishusneh.app.R
import com.shishusneh.app.databinding.FragmentDashboardBinding
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DashboardViewModel by viewModels()
    private val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
    private val adviceDateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.rotateMotivation()

        viewModel.babyProfile.observe(viewLifecycleOwner) { baby ->
            baby?.let {
                binding.tvBabyName.text = it.name
                binding.tvBabyAge.text = calculateAge(it.dateOfBirth)
                if (!it.photoUri.isNullOrEmpty()) {
                    binding.ivBaby.load(it.photoUri) {
                        crossfade(true)
                        transformations(CircleCropTransformation())
                        error(R.drawable.logo_intern)
                    }
                }
            }
        }

        viewModel.latestAdvice.observe(viewLifecycleOwner) { advice ->
            if (advice != null) {
                binding.tvLatestAdviceTitle.visibility = View.VISIBLE
                binding.cardLatestAdvice.visibility = View.VISIBLE
                binding.tvLatestAdviceText.text = advice.advice
                binding.tvLatestAdviceDate.text = getString(R.string.saved_on_format, adviceDateFormat.format(advice.date))
            } else {
                binding.tvLatestAdviceTitle.visibility = View.GONE
                binding.cardLatestAdvice.visibility = View.GONE
            }
        }

        viewModel.nextVaccine.observe(viewLifecycleOwner) { vaccine ->
            if (vaccine != null) {
                binding.tvNextVaccineName.text = vaccine.name
                binding.tvNextVaccineDate.text = getString(R.string.due_date_format, dateFormat.format(vaccine.dueDate))
                
                val daysUntil = calculateDaysUntil(vaccine.dueDate)
                binding.tvVaccineCountdown.text = when {
                    daysUntil < 0 -> getString(R.string.overdue)
                    daysUntil == 0L -> getString(R.string.due_today)
                    else -> getString(R.string.in_days, daysUntil.toInt())
                }
                
                val strokeWidth = if (daysUntil in 0..7) 2 else 1
                binding.cardVaccine.strokeWidth = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, strokeWidth.toFloat(), resources.displayMetrics
                ).toInt()

                binding.tvVaccineCountdown.visibility = View.VISIBLE
                binding.btnMarkVaccineDone.visibility = View.VISIBLE
                binding.btnBookAppointment.visibility = View.VISIBLE
                
                binding.btnMarkVaccineDone.setOnClickListener {
                    viewModel.markVaccineAsDone(vaccine.id)
                    Toast.makeText(requireContext(), getString(R.string.vaccination_recorded), Toast.LENGTH_SHORT).show()
                }

                binding.btnBookAppointment.setOnClickListener {
                    val bundle = bundleOf("vaccineName" to vaccine.name)
                    findNavController().navigate(R.id.vaccineBookingFragment, bundle)
                }
            } else {
                binding.tvNextVaccineName.text = getString(R.string.all_vaccines_completed)
                binding.tvNextVaccineDate.text = getString(R.string.great_job)
                binding.tvVaccineCountdown.visibility = View.GONE
                binding.btnMarkVaccineDone.visibility = View.GONE
                binding.btnBookAppointment.visibility = View.GONE
            }
        }

        viewModel.motivation.observe(viewLifecycleOwner) { motivation ->
            binding.tvMotivation.text = motivation
        }

        setupNavigation()
        setupQuickHelp()
    }

    private fun setupNavigation() {
        binding.btnDietTips.setOnClickListener { findNavController().navigate(R.id.navigation_wellness) }
        binding.btnExercise.setOnClickListener { findNavController().navigate(R.id.navigation_wellness) }
        binding.btnMentalHealth.setOnClickListener { findNavController().navigate(R.id.navigation_wellness) }
        binding.cardMilestonesHub.setOnClickListener { findNavController().navigate(R.id.navigation_milestones) }
        binding.cardGrowthHub.setOnClickListener { findNavController().navigate(R.id.navigation_growth) }
        binding.cardDoctorHelp.setOnClickListener { findNavController().navigate(R.id.navigation_doctor_help) }
        binding.cardFeedingTracker.setOnClickListener { findNavController().navigate(R.id.navigation_feeding) }
        binding.cardBabyProfile.setOnClickListener { findNavController().navigate(R.id.navigation_profile) }
        binding.btnViewCalendar.setOnClickListener { findNavController().navigate(R.id.navigation_vaccination) }
    }

    private fun setupQuickHelp() {
        binding.btnFindHospitalsQuick.setOnClickListener {
            val gmmIntentUri = Uri.parse("geo:0,0?q=children+hospitals+near+me")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            if (mapIntent.resolveActivity(requireActivity().packageManager) != null) {
                startActivity(mapIntent)
            } else {
                Toast.makeText(context, "Google Maps not found", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnEmergencyCallQuick.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:102")
            }
            startActivity(intent)
        }
    }

    private fun calculateAge(dob: Date): String {
        val diff = Date().time - dob.time
        val days = TimeUnit.MILLISECONDS.toDays(diff)
        return when {
            days < 7 -> getString(R.string.days_old, days.toInt())
            days < 60 -> getString(R.string.weeks_old, (days / 7).toInt())
            else -> getString(R.string.months_old, (days / 30).toInt())
        }
    }

    private fun calculateDaysUntil(dueDate: Date): Long {
        val today = Calendar.getInstance().apply { 
            set(Calendar.HOUR_OF_DAY, 0); set(Calendar.MINUTE, 0); set(Calendar.SECOND, 0); set(Calendar.MILLISECOND, 0) 
        }.timeInMillis
        val due = Calendar.getInstance().apply { 
            time = dueDate; set(Calendar.HOUR_OF_DAY, 0); set(Calendar.MINUTE, 0); set(Calendar.SECOND, 0); set(Calendar.MILLISECOND, 0) 
        }.timeInMillis
        return TimeUnit.MILLISECONDS.toDays(due - today)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
