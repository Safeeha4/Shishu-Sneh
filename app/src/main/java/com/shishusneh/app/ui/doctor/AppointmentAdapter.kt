package com.shishusneh.app.ui.doctor

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.shishusneh.app.R
import com.shishusneh.app.data.local.entities.Appointment
import com.shishusneh.app.databinding.ItemAppointmentBinding
import java.text.SimpleDateFormat
import java.util.*

class AppointmentAdapter(private val isDoctorView: Boolean = false) : ListAdapter<Appointment, AppointmentAdapter.ViewHolder>(DiffCallback()) {

    private val dateFormat = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAppointmentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ItemAppointmentBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Appointment) {
            val context = binding.root.context
            
            if (isDoctorView) {
                binding.tvDoctorName.text = "Patient: ${item.babyName}"
            } else {
                binding.tvDoctorName.text = "With ${item.doctorName}"
            }

            binding.tvAppointmentDate.text = dateFormat.format(item.appointmentDate)
            binding.tvPurpose.text = item.purpose
            binding.tvStatus.text = item.status

            val statusColor = when (item.status) {
                "SCHEDULED" -> ContextCompat.getColor(context, R.color.accent_primary)
                "COMPLETED" -> ContextCompat.getColor(context, R.color.success)
                "CANCELLED" -> ContextCompat.getColor(context, R.color.error)
                else -> ContextCompat.getColor(context, R.color.text_secondary)
            }
            binding.tvStatus.setTextColor(statusColor)
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Appointment>() {
        override fun areItemsTheSame(oldItem: Appointment, newItem: Appointment) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Appointment, newItem: Appointment) = oldItem == newItem
    }
}
