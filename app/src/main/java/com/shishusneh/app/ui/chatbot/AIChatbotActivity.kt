package com.shishusneh.app.ui.chatbot

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import androidx.core.net.toUri
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.shishusneh.app.R
import com.shishusneh.app.databinding.ActivityAiChatbotBinding
import com.shishusneh.app.ui.common.BaseActivity
import java.io.File
import java.io.IOException

class AIChatbotActivity : BaseActivity() {

    private lateinit var binding: ActivityAiChatbotBinding
    private val viewModel: ChatViewModel by viewModels()
    private var selectedMediaUri: Uri? = null
    private var currentMediaType: String = "TEXT"
    
    private var mediaRecorder: MediaRecorder? = null
    private var audioFile: File? = null
    private var isRecording = false

    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            showMediaPreview(it, "IMAGE")
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            toggleRecording()
        } else {
            Toast.makeText(this, getString(R.string.record_audio), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAiChatbotBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { finish() }

        val adapter = ChatAdapter()
        binding.rvChat.apply {
            this.adapter = adapter
            layoutManager = LinearLayoutManager(this@AIChatbotActivity)
        }

        viewModel.messages.observe(this) {
            adapter.submitList(it)
            if (it.isNotEmpty()) {
                binding.rvChat.smoothScrollToPosition(it.size - 1)
            }
        }

        binding.ivGallery.setOnClickListener {
            pickImage.launch("image/*")
        }

        binding.ivMic.setOnClickListener {
            checkAudioPermission()
        }

        binding.btnRemoveAttachment.setOnClickListener {
            resetMediaAttachment()
        }

        binding.btnSend.setOnClickListener {
            val message = binding.etMessage.text.toString()
            if (message.isNotBlank() || selectedMediaUri != null) {
                viewModel.sendMessage(message, selectedMediaUri, currentMediaType)
                
                // Reset state after sending
                binding.etMessage.text.clear()
                resetMediaAttachment()
            }
        }

        showMedicalDisclaimer()
    }

    private fun showMedicalDisclaimer() {
        val sharedPrefs = getSharedPreferences("shishu_prefs", Context.MODE_PRIVATE)
        val hasSeenDisclaimer = sharedPrefs.getBoolean("seen_ai_disclaimer", false)
            
        if (!hasSeenDisclaimer) {
            AlertDialog.Builder(this)
                .setTitle(R.string.medical_disclaimer_title)
                .setMessage(R.string.medical_disclaimer_content)
                .setPositiveButton(R.string.i_understand) { _, _ ->
                    sharedPrefs.edit {
                        putBoolean("seen_ai_disclaimer", true)
                    }
                }
                .setCancelable(false)
                .show()
        }
    }

    private fun showMediaPreview(uri: Uri, type: String) {
        selectedMediaUri = uri
        currentMediaType = type
        binding.layoutAttachmentPreview.visibility = View.VISIBLE
        
        if (type == "IMAGE") {
            binding.ivAttachmentPreview.load(uri)
            binding.etMessage.hint = getString(R.string.image_attached_hint)
        } else if (type == "AUDIO") {
            binding.ivAttachmentPreview.setImageResource(android.R.drawable.ic_btn_speak_now)
            binding.etMessage.hint = getString(R.string.audio_recorded_hint)
        }
    }

    private fun resetMediaAttachment() {
        selectedMediaUri = null
        currentMediaType = "TEXT"
        binding.layoutAttachmentPreview.visibility = View.GONE
        binding.etMessage.hint = getString(R.string.type_your_question)
    }

    private fun checkAudioPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
            toggleRecording()
        } else {
            requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
        }
    }

    private fun toggleRecording() {
        if (!isRecording) {
            startRecording()
        } else {
            stopRecording()
        }
    }

    private fun startRecording() {
        try {
            audioFile = File(externalCacheDir, "rec_${System.currentTimeMillis()}.mp4")
            mediaRecorder = MediaRecorder().apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                setOutputFile(audioFile?.absolutePath)
                prepare()
                start()
            }
            isRecording = true
            binding.ivMic.setColorFilter(getColor(R.color.accent_primary))
            Toast.makeText(this, "Recording started...", Toast.LENGTH_SHORT).show()
        } catch (e: IOException) {
            Toast.makeText(this, "Failed to start recording", Toast.LENGTH_SHORT).show()
        }
    }

    private fun stopRecording() {
        try {
            mediaRecorder?.apply {
                stop()
                release()
            }
            mediaRecorder = null
            isRecording = false
            binding.ivMic.clearColorFilter()
            
            audioFile?.let {
                showMediaPreview(it.toUri(), "AUDIO")
                Toast.makeText(this, "Audio recorded", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Recording failed", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaRecorder?.release()
    }
}
