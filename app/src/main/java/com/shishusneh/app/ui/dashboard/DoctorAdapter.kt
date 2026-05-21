package com.shishusneh.app.ui.dashboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.shishusneh.app.data.local.entities.Doctor
import com.shishusneh.app.databinding.ItemDoctorSavedBinding

class DoctorAdapter(
    private val onCallClick: (Doctor) -> Unit,
    private val onDeleteClick: (Doctor) -> Unit
) : ListAdapter<Doctor, DoctorAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemDoctorSavedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ItemDoctorSavedBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Doctor) {
            binding.tvDoctorName.text = item.name
            binding.tvSpecialization.text = item.specialization
            binding.btnCall.setOnClickListener { onCallClick(item) }
            binding.btnDelete.setOnClickListener { onDeleteClick(item) }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Doctor>() {
        override fun areItemsTheSame(oldItem: Doctor, newItem: Doctor) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Doctor, newItem: Doctor) = oldItem == newItem
    }
}
