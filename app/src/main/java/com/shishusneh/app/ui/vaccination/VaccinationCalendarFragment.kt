package com.shishusneh.app.ui.vaccination

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.shishusneh.app.R
import com.shishusneh.app.databinding.FragmentVaccinationCalendarBinding

class VaccinationCalendarFragment : Fragment() {

    private var _binding: FragmentVaccinationCalendarBinding? = null
    private val binding get() = _binding!!
    private val viewModel: VaccinationViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVaccinationCalendarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = VaccinationAdapter(
            onMarkDone = { vaccine ->
                viewModel.markAsCompleted(vaccine.id)
                Toast.makeText(requireContext(), getString(R.string.vaccination_recorded), Toast.LENGTH_SHORT).show()
            },
            onBookAppointment = { vaccine ->
                val bundle = bundleOf("vaccineName" to vaccine.name)
                findNavController().navigate(R.id.vaccineBookingFragment, bundle)
            }
        )

        binding.rvVaccines.apply {
            this.adapter = adapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        viewModel.vaccines.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
