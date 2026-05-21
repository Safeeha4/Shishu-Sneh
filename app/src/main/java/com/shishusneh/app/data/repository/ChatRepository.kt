package com.shishusneh.app.data.repository

import com.shishusneh.app.data.local.dao.ChatMessageDao
import com.shishusneh.app.data.local.entities.ChatMessage
import kotlinx.coroutines.flow.Flow

class ChatRepository(private val chatMessageDao: ChatMessageDao) {
    fun getMessagesByUser(userId: String): Flow<List<ChatMessage>> = chatMessageDao.getMessagesByUser(userId)
    suspend fun getRecentMessages(userId: String): List<ChatMessage> = chatMessageDao.getRecentMessages(userId)
    suspend fun insertMessage(message: ChatMessage) = chatMessageDao.insertMessage(message)
    suspend fun clearChatHistory(userId: String) = chatMessageDao.clearChatHistory(userId)
}
