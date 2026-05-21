package com.shishusneh.app.ui.doctor

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.shishusneh.app.data.local.entities.Consultation
import com.shishusneh.app.databinding.ItemConsultationBinding
import java.text.SimpleDateFormat
import java.util.*

class ConsultationAdapter : ListAdapter<Consultation, ConsultationAdapter.ViewHolder>(DiffCallback()) {

    private val dateFormat = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemConsultationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ItemConsultationBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Consultation) {
            binding.tvConsultationDate.text = dateFormat.format(item.date)
            binding.tvNotes.text = item.notes
            
            if (!item.advice.isNullOrBlank()) {
                binding.tvAdvice.visibility = View.VISIBLE
                binding.tvAdviceLabel.visibility = View.VISIBLE
                binding.tvAdvice.text = item.advice
            } else {
                binding.tvAdvice.visibility = View.GONE
                binding.tvAdviceLabel.visibility = View.GONE
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Consultation>() {
        override fun areItemsTheSame(oldItem: Consultation, newItem: Consultation) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Consultation, newItem: Consultation) = oldItem == newItem
    }
}
