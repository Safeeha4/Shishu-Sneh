package com.shishusneh.app.ui.feeding

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.shishusneh.app.data.local.entities.FeedingSession
import com.shishusneh.app.databinding.ItemFeedingSessionBinding
import java.text.SimpleDateFormat
import java.util.*

class FeedingHistoryAdapter : ListAdapter<FeedingSession, FeedingHistoryAdapter.ViewHolder>(DiffCallback()) {

    private val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    private val dateFormat = SimpleDateFormat("dd MMM", Locale.getDefault())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemFeedingSessionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ItemFeedingSessionBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: FeedingSession) {
            binding.tvTime.text = timeFormat.format(item.startTime)
            binding.tvDate.text = dateFormat.format(item.startTime)
            binding.tvDuration.text = "${item.duration} min"
            binding.tvBreast.text = item.breast
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<FeedingSession>() {
        override fun areItemsTheSame(oldItem: FeedingSession, newItem: FeedingSession) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: FeedingSession, newItem: FeedingSession) = oldItem == newItem
    }
}
