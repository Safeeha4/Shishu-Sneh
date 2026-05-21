package com.shishusneh.app.ui.growth

import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.shishusneh.app.R
import com.shishusneh.app.data.local.entities.GrowthEntry
import com.shishusneh.app.databinding.FragmentGrowthTrackingBinding
import com.shishusneh.app.utils.WHOStandards
import java.text.SimpleDateFormat
import java.util.*

class GrowthTrackingFragment : Fragment() {

    private var _binding: FragmentGrowthTrackingBinding? = null
    private val binding get() = _binding!!
    private val viewModel: GrowthViewModel by viewModels()
    
    private var currentType = "WEIGHT" // or "HEIGHT"
    private var selectedDate = Calendar.getInstance()
    private val dateFormatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGrowthTrackingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupChart()
        setupHistoryList()
        setupListeners()
        setupObservers()
        updateDateDisplay()
    }

    private fun setupChart() {
        binding.growthChart.apply {
            description.isEnabled = false
            setTouchEnabled(true)
            setPinchZoom(true)
            setScaleEnabled(true)
            setDrawGridBackground(false)
            
            xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                textColor = Color.parseColor("#B8B8B8")
                setDrawGridLines(false)
                granularity = 1f
                valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String = "${value.toInt()}m"
                }
            }
            
            axisLeft.apply {
                textColor = Color.parseColor("#B8B8B8")
                gridColor = Color.parseColor("#16213E")
                setDrawGridLines(true)
            }
            
            axisRight.isEnabled = false
            legend.textColor = Color.WHITE
        }
    }

    private fun setupListeners() {
        binding.toggleDataType.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                currentType = if (checkedId == R.id.btnWeight) "WEIGHT" else "HEIGHT"
                updateChartData(viewModel.growthEntries.value ?: emptyList())
            }
        }

        binding.etDate.setOnClickListener {
            showDatePicker()
        }

        binding.btnSave.setOnClickListener {
            validateAndSave()
        }
    }

    private fun showDatePicker() {
        val baby = viewModel.babyProfile.value
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                selectedDate.set(Calendar.YEAR, year)
                selectedDate.set(Calendar.MONTH, month)
                selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateDisplay()
            },
            selectedDate.get(Calendar.YEAR),
            selectedDate.get(Calendar.MONTH),
            selectedDate.get(Calendar.DAY_OF_MONTH)
        )
        
        // Validation: Measurement cannot be before birth or in the future
        baby?.let {
            datePickerDialog.datePicker.minDate = it.dateOfBirth.time
        }
        datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
        datePickerDialog.show()
    }

    private fun updateDateDisplay() {
        binding.etDate.setText(dateFormatter.format(selectedDate.time))
    }

    private fun setupObservers() {
        viewModel.growthEntries.observe(viewLifecycleOwner) { entries ->
            updateChartData(entries)
            // Submit list to history adapter
            (binding.rvHistory.adapter as? GrowthHistoryAdapter)?.submitList(entries.sortedByDescending { it.date })
        }
    }

    private fun updateChartData(entries: List<GrowthEntry>) {
        if (entries.isEmpty()) {
            binding.growthChart.clear()
            binding.cardStatus.visibility = View.GONE
            return
        }

        val dataSets = mutableListOf<ILineDataSet>()
        val babyProfile = viewModel.babyProfile.value
        
        // 1. User Data
        val babyEntries = entries.map { entry ->
            val months = babyProfile?.let { calculateAgeInMonths(it.dateOfBirth, entry.date) } ?: 0
            val value = if (currentType == "WEIGHT") entry.weight else (entry.height ?: 0.0)
            Entry(months.toFloat(), value.toFloat())
        }.sortedBy { it.x }

        val babyDataSet = LineDataSet(babyEntries, if (currentType == "WEIGHT") "Baby Weight (kg)" else "Baby Height (cm)").apply {
            color = Color.parseColor("#E94560")
            setCircleColor(Color.parseColor("#E94560"))
            lineWidth = 3f
            circleRadius = 5f
            setDrawCircleHole(false)
            setDrawValues(false)
            mode = LineDataSet.Mode.CUBIC_BEZIER
        }
        dataSets.add(babyDataSet)

        // 2. WHO Percentiles
        if (currentType == "WEIGHT") {
            addWHOPercentileLines(dataSets)
        }

        binding.growthChart.data = LineData(dataSets)
        binding.growthChart.invalidate()
        
        // Update Status Indicator with latest entry
        entries.lastOrNull()?.let { updateStatus(it) }
    }

    private fun addWHOPercentileLines(dataSets: MutableList<ILineDataSet>) {
        // Approximate WHO Weight Data
        val p50 = listOf(Entry(0f, 3.3f), Entry(2f, 5.6f), Entry(4f, 7.0f), Entry(6f, 7.9f), Entry(12f, 9.6f))
        val p3 = listOf(Entry(0f, 2.4f), Entry(2f, 4.3f), Entry(4f, 5.5f), Entry(6f, 6.4f), Entry(12f, 7.7f))
        val p97 = listOf(Entry(0f, 4.4f), Entry(2f, 7.1f), Entry(4f, 8.7f), Entry(6f, 9.8f), Entry(12f, 12.0f))
        
        dataSets.add(createWHOSet(p50, "WHO 50th", "#FFB84D"))
        dataSets.add(createWHOSet(p3, "WHO 3rd", "#FF6B6B"))
        dataSets.add(createWHOSet(p97, "WHO 97th", "#6BCF7F"))
    }

    private fun createWHOSet(entries: List<Entry>, label: String, colorHex: String): LineDataSet {
        return LineDataSet(entries, label).apply {
            color = Color.parseColor(colorHex)
            lineWidth = 1.5f
            setDrawCircles(false)
            enableDashedLine(10f, 5f, 0f)
            setDrawValues(false)
        }
    }

    private fun updateStatus(entry: GrowthEntry) {
        val baby = viewModel.babyProfile.value ?: return
        val ageMonths = calculateAgeInMonths(baby.dateOfBirth, entry.date)
        val status = WHOStandards.assessGrowthStatus(entry.weight, baby.gender, ageMonths)
        
        binding.cardStatus.visibility = View.VISIBLE
        
        when (status) {
            WHOStandards.GrowthStatus.HEALTHY -> {
                binding.tvGrowthStatus.text = getString(R.string.healthy_growth_detected)
                binding.tvGrowthStatus.setTextColor(ContextCompat.getColor(requireContext(), R.color.success))
                binding.cardStatus.strokeColor = ContextCompat.getColor(requireContext(), R.color.success)
                binding.ivStatusIcon.setImageResource(android.R.drawable.ic_dialog_info)
                binding.ivStatusIcon.setColorFilter(ContextCompat.getColor(requireContext(), R.color.success))
            }
            WHOStandards.GrowthStatus.BELOW_NORMAL -> {
                binding.tvGrowthStatus.text = getString(R.string.growth_below_range)
                binding.tvGrowthStatus.setTextColor(ContextCompat.getColor(requireContext(), R.color.error))
                binding.cardStatus.strokeColor = ContextCompat.getColor(requireContext(), R.color.error)
                binding.ivStatusIcon.setImageResource(android.R.drawable.stat_sys_warning)
                binding.ivStatusIcon.setColorFilter(ContextCompat.getColor(requireContext(), R.color.error))
            }
            WHOStandards.GrowthStatus.ABOVE_NORMAL -> {
                binding.tvGrowthStatus.text = getString(R.string.growth_above_range)
                binding.tvGrowthStatus.setTextColor(ContextCompat.getColor(requireContext(), R.color.warning))
                binding.cardStatus.strokeColor = ContextCompat.getColor(requireContext(), R.color.warning)
                binding.ivStatusIcon.setImageResource(android.R.drawable.ic_dialog_alert)
                binding.ivStatusIcon.setColorFilter(ContextCompat.getColor(requireContext(), R.color.warning))
            }
            else -> binding.cardStatus.visibility = View.GONE
        }
    }

    private fun validateAndSave() {
        val weightStr = binding.etWeight.text.toString()
        val weight = weightStr.toDoubleOrNull()
        val height = binding.etHeight.text.toString().toDoubleOrNull()
        
        if (weight == null || weight !in 0.5..15.0) {
            binding.etWeight.error = getString(R.string.error_weight_invalid)
            return
        }

        viewModel.addEntry(weight, height, selectedDate.time, getString(R.string.manual_entry))
        binding.etWeight.text?.clear()
        binding.etHeight.text?.clear()
        // Reset date to today for next entry
        selectedDate = Calendar.getInstance()
        updateDateDisplay()

        Toast.makeText(context, getString(R.string.growth_recorded), Toast.LENGTH_SHORT).show()
    }

    private fun calculateAgeInMonths(dob: Date, entryDate: Date): Int {
        val startCalendar = Calendar.getInstance().apply { time = dob }
        val endCalendar = Calendar.getInstance().apply { time = entryDate }
        
        val diffYears = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR)
        val diffMonths = diffYears * 12 + endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH)
        
        return if (diffMonths < 0) 0 else diffMonths
    }

    private fun setupHistoryList() {
        val adapter = GrowthHistoryAdapter()
        binding.rvHistory.apply {
            this.adapter = adapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
