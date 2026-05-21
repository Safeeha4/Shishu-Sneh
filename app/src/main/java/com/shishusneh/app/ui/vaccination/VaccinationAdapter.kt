package com.shishusneh.app.ui.vaccination

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.shishusneh.app.R
import com.shishusneh.app.data.local.entities.VaccineRecord
import com.shishusneh.app.databinding.ItemVaccineBinding
import java.text.SimpleDateFormat
import java.util.*

class VaccinationAdapter(
    private val onMarkDone: (VaccineRecord) -> Unit,
    private val onBookAppointment: (VaccineRecord) -> Unit
) : ListAdapter<VaccineRecord, VaccinationAdapter.ViewHolder>(DiffCallback()) {

    private val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemVaccineBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ItemVaccineBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: VaccineRecord) {
            val context = binding.root.context
            binding.tvVaccineName.text = item.name
            binding.tvDueDate.text = context.getString(R.string.due_date_label, dateFormat.format(item.dueDate))
            binding.tvDiseases.text = context.getString(R.string.prevents_label, item.diseases)

            if (item.status == "COMPLETED") {
                binding.btnMarkDone.visibility = View.GONE
                binding.btnBookAppointment.visibility = View.GONE
                binding.cardVaccine.setStrokeColor(context.getColorStateList(R.color.success))
            } else {
                binding.btnMarkDone.visibility = View.VISIBLE
                binding.btnBookAppointment.visibility = View.VISIBLE
                binding.cardVaccine.setStrokeColor(context.getColorStateList(R.color.card_bg))
                
                binding.btnMarkDone.setOnClickListener { onMarkDone(item) }
                binding.btnBookAppointment.setOnClickListener { onBookAppointment(item) }
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<VaccineRecord>() {
        override fun areItemsTheSame(oldItem: VaccineRecord, newItem: VaccineRecord) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: VaccineRecord, newItem: VaccineRecord) = oldItem == newItem
    }
}
