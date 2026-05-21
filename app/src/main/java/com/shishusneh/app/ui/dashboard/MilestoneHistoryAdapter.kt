package com.shishusneh.app.ui.dashboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.shishusneh.app.data.local.entities.MilestoneLog
import com.shishusneh.app.databinding.ItemMilestoneHistoryBinding
import java.text.SimpleDateFormat
import java.util.*

class MilestoneHistoryAdapter : ListAdapter<MilestoneLog, MilestoneHistoryAdapter.ViewHolder>(DiffCallback()) {

    private val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMilestoneHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ItemMilestoneHistoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MilestoneLog) {
            binding.tvDate.text = dateFormat.format(item.answeredAt)
            binding.tvQuestion.text = item.question
            binding.tvAnswer.text = "Answered: ${item.answer}"
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<MilestoneLog>() {
        override fun areItemsTheSame(oldItem: MilestoneLog, newItem: MilestoneLog) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: MilestoneLog, newItem: MilestoneLog) = oldItem == newItem
    }
}
