package com.shishusneh.app.ui.chatbot

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.lifecycle.*
import com.shishusneh.app.ShishuSnehApplication
import com.shishusneh.app.data.local.entities.ChatMessage
import com.google.firebase.firestore.FirebaseFirestore
import com.shishusneh.app.data.remote.gemini.GeminiService
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.UnknownHostException

class ChatViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = (application as ShishuSnehApplication).chatRepository
    private val babyRepository = (application as ShishuSnehApplication).babyRepository
    private val firestore = FirebaseFirestore.getInstance()
    private val prefs = application.getSharedPreferences("shishu_prefs", Context.MODE_PRIVATE)

    private val _messages = MutableLiveData<List<ChatMessage>>(emptyList())
    val messages: LiveData<List<ChatMessage>> = _messages

    private val _statusMessage = MutableLiveData<String>()
    val statusMessage: LiveData<String> = _statusMessage

    private var userId: String = "default_user"
    private val generativeModel = GeminiService.getModel()

    init {
        viewModelScope.launch {
            val baby = babyRepository.getPrimaryProfile()
            userId = baby?.id ?: "default_user"

            repository.getMessagesByUser(userId).collectLatest { list ->
                _messages.postValue(list)
            }
        }
    }

    private fun getAppLanguage(): String {
        return prefs.getString("app_language", "en") ?: "en"
    }

    fun sendMessage(text: String, mediaUri: Uri? = null, mediaType: String = "TEXT") {
        if (text.isBlank() && mediaUri == null) return

        val currentLang = getAppLanguage()
        val userMsg = ChatMessage(
            userId = userId,
            role = "user",
            message = text,
            mediaUri = mediaUri?.toString(),
            mediaType = mediaType,
            language = currentLang
        )

        viewModelScope.launch {
            try {
                // 1. Save locally and Sync to Firestore
                repository.insertMessage(userMsg)
                syncToFirestore(userMsg)

                // 2. Get AI Response
                val responseText = try {
                    getAiResponse(text, mediaUri, mediaType, currentLang)
                } catch (e: Exception) {
                    Log.e("AI_DEBUG", "Actual Error: ${e.message}", e)
                    handleError(e)
                }

                // 3. Save AI response locally and Sync to Firestore
                val botMsg = ChatMessage(
                    userId = userId,
                    role = "model",
                    message = responseText,
                    language = currentLang
                )
                repository.insertMessage(botMsg)
                syncToFirestore(botMsg)

            } catch (e: Exception) {
                _statusMessage.postValue("Database Error: ${e.localizedMessage}")
            }
        }
    }

    private fun syncToFirestore(message: ChatMessage) {
        firestore.collection("users")
            .document(userId)
            .collection("messages")
            .add(message)
            .addOnFailureListener { e ->
                Log.e("FIRESTORE_ERROR", "Failed to sync: ${e.message}")
            }
    }

    private suspend fun getAiResponse(text: String, mediaUri: Uri?, mediaType: String, lang: String): String = withContext(Dispatchers.IO) {
        // Get recent history to provide context to Gemini
        val history = repository.getRecentMessages(userId).takeLast(5).map { msg ->
            content(if (msg.role == "user") "user" else "model") { text(msg.message) }
        }

        val prompt = if (text.isBlank()) {
            if (mediaType == "IMAGE") "Analyze this image and provide relevant baby care advice." 
            else "Listen to this audio and respond to the mother's concern."
        } else {
            text
        }

        val chat = generativeModel.startChat(history)
        
        val response = if (mediaUri != null) {
            val content = content {
                if (mediaType == "IMAGE") {
                    val bitmap = getBitmapFromUri(mediaUri)
                    bitmap?.let { image(it) }
                } else if (mediaType == "AUDIO") {
                    val audioData = getBytesFromUri(mediaUri)
                    audioData?.let { blob("audio/mp3", it) }
                }
                text(prompt)
            }
            chat.sendMessage(content)
        } else {
            chat.sendMessage(prompt)
        }

        response.text ?: "I'm sorry, I couldn't understand that."
    }

    private fun getBitmapFromUri(uri: Uri): Bitmap? {
        return try {
            val inputStream = getApplication<Application>().contentResolver.openInputStream(uri)
            BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
            null
        }
    }

    private fun getBytesFromUri(uri: Uri): ByteArray? {
        return try {
            getApplication<Application>().contentResolver.openInputStream(uri)?.readBytes()
        } catch (e: Exception) {
            null
        }
    }

    private fun handleError(e: Exception): String {
        return when (e) {
            is UnknownHostException -> "Network error: Please check your internet connection."
            else -> "The AI service is currently unavailable. Error: ${e.localizedMessage}"
        }
    }
}
