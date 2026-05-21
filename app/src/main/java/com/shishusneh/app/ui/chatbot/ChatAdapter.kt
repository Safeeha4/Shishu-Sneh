package com.shishusneh.app.ui.chatbot

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.shishusneh.app.data.local.entities.ChatMessage
import com.shishusneh.app.databinding.ItemChatBotBinding
import com.shishusneh.app.databinding.ItemChatUserBinding
import java.text.SimpleDateFormat
import java.util.*

class ChatAdapter : ListAdapter<ChatMessage, RecyclerView.ViewHolder>(DiffCallback()) {

    private val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).role == "user") 1 else 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 1) {
            UserViewHolder(ItemChatUserBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        } else {
            BotViewHolder(ItemChatBotBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = getItem(position)
        if (holder is UserViewHolder) holder.bind(message)
        else if (holder is BotViewHolder) holder.bind(message)
    }

    inner class UserViewHolder(private val binding: ItemChatUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ChatMessage) {
            binding.tvMessage.text = item.message
            binding.tvTimestamp.text = timeFormat.format(item.timestamp)
            
            if (!item.mediaUri.isNullOrEmpty()) {
                binding.cardMedia.visibility = View.VISIBLE
                if (item.mediaType == "IMAGE") {
                    binding.ivContent.visibility = View.VISIBLE
                    binding.btnPlayAudio.visibility = View.GONE
                    binding.ivContent.load(item.mediaUri)
                } else if (item.mediaType == "AUDIO") {
                    binding.ivContent.visibility = View.GONE
                    binding.btnPlayAudio.visibility = View.VISIBLE
                }
            } else {
                binding.cardMedia.visibility = View.GONE
            }
        }
    }

    inner class BotViewHolder(private val binding: ItemChatBotBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ChatMessage) {
            binding.tvMessage.text = item.message
            binding.tvTimestamp.text = timeFormat.format(item.timestamp)
            
            if (!item.mediaUri.isNullOrEmpty()) {
                binding.cardMedia.visibility = View.VISIBLE
                binding.ivContent.load(item.mediaUri)
            } else {
                binding.cardMedia.visibility = View.GONE
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<ChatMessage>() {
        override fun areItemsTheSame(oldItem: ChatMessage, newItem: ChatMessage) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: ChatMessage, newItem: ChatMessage) = oldItem == newItem
    }
}
