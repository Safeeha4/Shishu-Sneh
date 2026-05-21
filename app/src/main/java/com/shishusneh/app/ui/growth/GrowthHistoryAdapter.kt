package com.shishusneh.app.ui.growth

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.shishusneh.app.data.local.entities.GrowthEntry
import com.shishusneh.app.databinding.ItemGrowthHistoryBinding
import java.text.SimpleDateFormat
import java.util.*

class GrowthHistoryAdapter : ListAdapter<GrowthEntry, GrowthHistoryAdapter.ViewHolder>(DiffCallback()) {

    private val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemGrowthHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ItemGrowthHistoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: GrowthEntry) {
            binding.tvDate.text = dateFormat.format(item.date)
            val heightStr = item.height?.let { " • ${it}cm" } ?: ""
            binding.tvStats.text = "${item.weight}kg$heightStr"
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<GrowthEntry>() {
        override fun areItemsTheSame(oldItem: GrowthEntry, newItem: GrowthEntry) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: GrowthEntry, newItem: GrowthEntry) = oldItem == newItem
    }
}
