package com.shishusneh.app.ui.dashboard

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.shishusneh.app.R
import com.shishusneh.app.data.remote.firebase.MilestoneData
import com.shishusneh.app.databinding.FragmentMilestoneTrackingBinding

class MilestoneTrackingFragment : Fragment() {

    private var _binding: FragmentMilestoneTrackingBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MilestoneViewModel by viewModels()
    private lateinit var questionsAdapter: MilestoneQuestionsAdapter
    private lateinit var historyAdapter: MilestoneHistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMilestoneTrackingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerViews()
        setupObservers()
        setupListeners()
    }

    private fun setupRecyclerViews() {
        // Questions Adapter - set to only show one question at a time
        questionsAdapter = MilestoneQuestionsAdapter { milestone, answer ->
            handleMilestoneAnswer(milestone, answer)
        }
        binding.rvMilestoneQuestions.apply {
            adapter = questionsAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        // History Adapter
        historyAdapter = MilestoneHistoryAdapter()
        binding.rvMilestoneHistory.apply {
            adapter = historyAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setupObservers() {
        viewModel.currentWeek.observe(viewLifecycleOwner) { week ->
            binding.tvMilestoneHeader.text = getString(R.string.week_developmental_check, week)
        }

        // Observe the single current milestone for one-by-one flow
        viewModel.currentMilestone.observe(viewLifecycleOwner) { milestone ->
            // Hide feedback when a new milestone appears
            binding.tvMilestoneFeedback.visibility = View.GONE
            
            if (milestone != null) {
                binding.rvMilestoneQuestions.visibility = View.VISIBLE
                val item = MilestoneQuestionsAdapter.MilestoneItem.Question(milestone)
                questionsAdapter.submitList(listOf(item))
            } else {
                // All milestones completed for this session
                binding.rvMilestoneQuestions.visibility = View.GONE
                Toast.makeText(context, getString(R.string.all_milestones_done), Toast.LENGTH_SHORT).show()
            }
            updateProgressUI()
        }

        viewModel.milestoneHistory.observe(viewLifecycleOwner) { history ->
            historyAdapter.submitList(history.sortedByDescending { it.answeredAt })
        }
    }

    private fun updateProgressUI() {
        val (current, total) = viewModel.getMilestoneProgress()
        if (total > 0) {
            binding.tvProgressLabel.text = getString(R.string.milestone_progress, current, total)
            binding.pbMilestone.max = total
            binding.pbMilestone.progress = current
        }
    }

    private fun setupListeners() {
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnSaveContinue.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun handleMilestoneAnswer(milestone: MilestoneData, answer: String) {
        // 1. Provide Feedback on screen (Appreciation or Hope)
        binding.tvMilestoneFeedback.visibility = View.VISIBLE
        if (answer == "YES") {
            binding.tvMilestoneFeedback.text = getString(R.string.milestone_appreciation)
            binding.tvMilestoneFeedback.setTextColor(ContextCompat.getColor(requireContext(), R.color.success))
        } else if (answer == "NO") {
            binding.tvMilestoneFeedback.text = getString(R.string.milestone_hope)
            binding.tvMilestoneFeedback.setTextColor(ContextCompat.getColor(requireContext(), R.color.accent_primary))
            
            if (milestone.critical) {
                showDelayedMilestoneAlert()
            }
        } else {
            binding.tvMilestoneFeedback.visibility = View.GONE
        }

        // 2. Save the answer
        viewModel.saveMilestone(milestone, answer)

        // 3. Move to next question after a slightly longer delay so user can read feedback
        Handler(Looper.getMainLooper()).postDelayed({
            if (_binding != null) {
                viewModel.moveToNext()
            }
        }, 2000)
    }

    private fun showDelayedMilestoneAlert() {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.gentle_reminder))
            .setMessage(getString(R.string.milestone_delay_message))
            .setPositiveButton(getString(R.string.i_understand), null)
            .setNegativeButton(getString(R.string.contact_doc)) { _, _ ->
                findNavController().navigate(R.id.navigation_doctor_help)
            }
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
