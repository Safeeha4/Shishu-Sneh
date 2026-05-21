package com.shishusneh.app.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.shishusneh.app.R
import com.shishusneh.app.databinding.FragmentMotherWellnessBinding

class MotherWellnessFragment : Fragment() {

    private var _binding: FragmentMotherWellnessBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MotherWellnessViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMotherWellnessBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObservers()
        setupListeners()
    }

    private fun setupObservers() {
        viewModel.dietContent.observe(viewLifecycleOwner) { content ->
            content?.let {
                binding.tvDietTitle.text = it.title
                binding.tvDietPreview.text = it.sections.firstOrNull()?.content ?: ""
            }
        }

        viewModel.exerciseContent.observe(viewLifecycleOwner) { content ->
            content?.let {
                binding.tvExerciseTitle.text = it.title
                binding.tvExercisePreview.text = it.sections.firstOrNull()?.content ?: ""
            }
        }

        viewModel.mentalHealthContent.observe(viewLifecycleOwner) { content ->
            content?.let {
                binding.tvMentalTitle.text = it.title
                binding.tvMentalPreview.text = it.sections.firstOrNull()?.content ?: ""
            }
        }

        viewModel.dailyTip.observe(viewLifecycleOwner) { tip ->
            binding.tvDailyTip.text = tip
        }
    }

    private fun setupListeners() {
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnViewDiet.setOnClickListener {
            navigateToDetail("diet")
        }

        binding.btnViewExercise.setOnClickListener {
            navigateToDetail("exercise")
        }

        binding.btnGetSupport.setOnClickListener {
            navigateToDetail("mental")
        }
    }

    private fun navigateToDetail(category: String) {
        val bundle = bundleOf("category" to category)
        findNavController().navigate(R.id.action_navigation_wellness_to_wellnessDetailFragment, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
