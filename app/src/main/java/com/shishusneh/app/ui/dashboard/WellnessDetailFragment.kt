package com.shishusneh.app.ui.dashboard

import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.shishusneh.app.databinding.FragmentWellnessDetailBinding

class WellnessDetailFragment : Fragment() {

    private var _binding: FragmentWellnessDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MotherWellnessViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWellnessDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val category = arguments?.getString("category") ?: "diet"
        
        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }

        when (category) {
            "diet" -> viewModel.dietContent.observe(viewLifecycleOwner) { content ->
                content?.let { displayContent(it.title, it.sections) }
            }
            "exercise" -> viewModel.exerciseContent.observe(viewLifecycleOwner) { content ->
                content?.let { displayContent(it.title, it.sections) }
            }
            "mental" -> viewModel.mentalHealthContent.observe(viewLifecycleOwner) { content ->
                content?.let { displayContent(it.title, it.sections) }
            }
        }
    }

    private fun displayContent(title: String, sections: List<com.shishusneh.app.data.remote.firebase.WellnessSection>) {
        binding.toolbar.title = title
        binding.layoutContent.removeAllViews()

        for (section in sections) {
            // Heading
            val headingTv = TextView(requireContext()).apply {
                text = section.heading
                textSize = 18f
                setTextColor(context.getColor(com.shishusneh.app.R.color.accent_secondary))
                setTypeface(null, Typeface.BOLD)
                layoutParams = LinearLayout.LayoutParams(-1, -2).apply { topMargin = 24.toDp() }
            }
            binding.layoutContent.addView(headingTv)

            // Content
            val contentTv = TextView(requireContext()).apply {
                text = section.content
                textSize = 16f
                setTextColor(context.getColor(com.shishusneh.app.R.color.text_primary))
                layoutParams = LinearLayout.LayoutParams(-1, -2).apply { topMargin = 8.toDp() }
            }
            binding.layoutContent.addView(contentTv)

            // Items (if any)
            section.items?.forEach { item ->
                val itemTv = TextView(requireContext()).apply {
                    text = "• $item"
                    textSize = 15f
                    setTextColor(context.getColor(com.shishusneh.app.R.color.text_primary))
                    layoutParams = LinearLayout.LayoutParams(-1, -2).apply { 
                        topMargin = 4.toDp()
                        marginStart = 12.toDp()
                    }
                }
                binding.layoutContent.addView(itemTv)
            }
        }
    }

    private fun Int.toDp() = (this * resources.displayMetrics.density).toInt()

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
