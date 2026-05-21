package com.shishusneh.app.ui.feeding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shishusneh.app.R
import com.shishusneh.app.databinding.FragmentFeedingGuideBinding

class FeedingFragment : Fragment() {

    private var _binding: FragmentFeedingGuideBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FeedingViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFeedingGuideBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupListeners()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        val adapter = FeedingHistoryAdapter()
        binding.rvFeedingHistory.apply {
            this.adapter = adapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        // Add Swipe-to-Delete functionality
        val swipeHandler = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val session = adapter.currentList[position]
                    viewModel.deleteSession(session)
                    Toast.makeText(requireContext(), "Session deleted", Toast.LENGTH_SHORT).show()
                }
            }
        }
        ItemTouchHelper(swipeHandler).attachToRecyclerView(binding.rvFeedingHistory)
    }

    private fun setupListeners() {
        binding.btnSave.setOnClickListener {
            val durationStr = binding.etDuration.text.toString()
            val duration = durationStr.toIntOrNull() ?: 0
            
            val breastId = binding.toggleBreast.checkedButtonId
            val breast = when (breastId) {
                R.id.btnLeft -> "LEFT"
                R.id.btnRight -> "RIGHT"
                R.id.btnBoth -> "BOTH"
                else -> ""
            }

            if (duration > 0 && breast.isNotEmpty()) {
                viewModel.logFeeding(duration, breast, null)
                binding.etDuration.text?.clear()
                binding.toggleBreast.clearChecked()
                Toast.makeText(requireContext(), "Feeding session saved", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Please enter duration and select breast", Toast.LENGTH_SHORT).show()
            }
        }
        
        binding.toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun observeViewModel() {
        viewModel.sessions.observe(viewLifecycleOwner) { sessions ->
            (binding.rvFeedingHistory.adapter as? FeedingHistoryAdapter)?.submitList(sessions)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
