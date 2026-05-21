package com.shishusneh.app.ui.dashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.shishusneh.app.R
import com.shishusneh.app.data.remote.firebase.MilestoneData
import com.shishusneh.app.databinding.ItemMilestoneHeaderBinding
import com.shishusneh.app.databinding.ItemMilestoneQuestionBinding

class MilestoneQuestionsAdapter(
    private val onAnswer: (MilestoneData, String) -> Unit
) : ListAdapter<MilestoneQuestionsAdapter.MilestoneItem, RecyclerView.ViewHolder>(DiffCallback()) {

    companion object {
        private const val TYPE_HEADER = 0
        private const val TYPE_QUESTION = 1
    }

    sealed class MilestoneItem {
        data class Header(val title: String) : MilestoneItem()
        data class Question(val data: MilestoneData, val answer: String? = null) : MilestoneItem()
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is MilestoneItem.Header -> TYPE_HEADER
            is MilestoneItem.Question -> TYPE_QUESTION
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_HEADER -> HeaderViewHolder(ItemMilestoneHeaderBinding.inflate(inflater, parent, false))
            else -> QuestionViewHolder(ItemMilestoneQuestionBinding.inflate(inflater, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        when (holder) {
            is HeaderViewHolder -> holder.bind(item as MilestoneItem.Header)
            is QuestionViewHolder -> holder.bind(item as MilestoneItem.Question)
        }
    }

    inner class HeaderViewHolder(private val binding: ItemMilestoneHeaderBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MilestoneItem.Header) {
            binding.tvCategoryHeader.text = item.title
        }
    }

    inner class QuestionViewHolder(private val binding: ItemMilestoneQuestionBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MilestoneItem.Question) {
            val data = item.data
            binding.tvQuestion.text = data.question
            
            if (item.answer != null) {
                binding.tvAnswered.visibility = View.VISIBLE
                binding.tvAnswered.text = binding.root.context.getString(R.string.vaccination_recorded) // Reusing or should create milestone specific
                binding.tvAnswered.text = "Answered: ${item.answer}"
                updateButtonStates(item.answer)
            } else {
                binding.tvAnswered.visibility = View.GONE
                resetButtonStates()
            }

            binding.btnYes.setOnClickListener { onAnswer(data, "YES") }
            binding.btnNo.setOnClickListener { onAnswer(data, "NO") }
            binding.btnSkip.setOnClickListener { onAnswer(data, "SKIPPED") }
        }

        private fun updateButtonStates(answer: String) {
            binding.btnYes.alpha = if (answer == "YES") 1.0f else 0.4f
            binding.btnNo.alpha = if (answer == "NO") 1.0f else 0.4f
            binding.btnSkip.alpha = if (answer == "SKIPPED") 1.0f else 0.4f
        }

        private fun resetButtonStates() {
            binding.btnYes.alpha = 1.0f
            binding.btnNo.alpha = 1.0f
            binding.btnSkip.alpha = 1.0f
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<MilestoneItem>() {
        override fun areItemsTheSame(oldItem: MilestoneItem, newItem: MilestoneItem): Boolean {
            return if (oldItem is MilestoneItem.Header && newItem is MilestoneItem.Header) {
                oldItem.title == newItem.title
            } else if (oldItem is MilestoneItem.Question && newItem is MilestoneItem.Question) {
                oldItem.data.id == newItem.data.id
            } else false
        }

        override fun areContentsTheSame(oldItem: MilestoneItem, newItem: MilestoneItem): Boolean {
            return oldItem == newItem
        }
    }
}
