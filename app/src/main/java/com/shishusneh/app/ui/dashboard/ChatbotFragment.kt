package com.shishusneh.app.ui.dashboard

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.shishusneh.app.R
import com.shishusneh.app.databinding.FragmentChatbotBinding
import com.shishusneh.app.ui.chatbot.ChatAdapter
import com.shishusneh.app.ui.chatbot.ChatViewModel
import java.util.*

class ChatbotFragment : Fragment() {

    private var _binding: FragmentChatbotBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ChatViewModel by viewModels()
    private var selectedMediaUri: Uri? = null
    private var currentMediaType: String = "TEXT"
    private var speechRecognizer: SpeechRecognizer? = null

    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            showMediaPreview(it, "IMAGE")
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            startSpeechToText()
        } else {
            Toast.makeText(requireContext(), getString(R.string.record_audio), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatbotBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ChatAdapter()
        binding.rvChat.apply {
            this.adapter = adapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        viewModel.messages.observe(viewLifecycleOwner) {
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
    }

    private fun showMediaPreview(uri: Uri, type: String) {
        selectedMediaUri = uri
        currentMediaType = type
        binding.layoutAttachmentPreview.visibility = View.VISIBLE
        
        if (type == "IMAGE") {
            binding.ivAttachmentPreview.load(uri)
            binding.etMessage.hint = getString(R.string.image_attached_hint)
        }
    }

    private fun resetMediaAttachment() {
        selectedMediaUri = null
        currentMediaType = "TEXT"
        binding.layoutAttachmentPreview.visibility = View.GONE
        binding.etMessage.hint = getString(R.string.type_your_question)
    }

    private fun checkAudioPermission() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.RECORD_AUDIO
            ) == PackageManager.PERMISSION_GRANTED -> {
                startSpeechToText()
            }
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
            }
        }
    }

    private fun startSpeechToText() {
        if (!SpeechRecognizer.isRecognitionAvailable(requireContext())) {
            Toast.makeText(requireContext(), "Speech recognition not available on this device", Toast.LENGTH_SHORT).show()
            return
        }

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(requireContext())
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak now...")
        }

        speechRecognizer?.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {
                Toast.makeText(requireContext(), "Listening...", Toast.LENGTH_SHORT).show()
                binding.ivMic.setColorFilter(ContextCompat.getColor(requireContext(), R.color.accent_primary))
            }
            override fun onBeginningOfSpeech() {}
            override fun onRmsChanged(rmsdB: Float) {}
            override fun onBufferReceived(buffer: ByteArray?) {}
            override fun onEndOfSpeech() {
                binding.ivMic.setColorFilter(ContextCompat.getColor(requireContext(), R.color.text_secondary))
            }
            override fun onError(error: Int) {
                binding.ivMic.setColorFilter(ContextCompat.getColor(requireContext(), R.color.text_secondary))
                Toast.makeText(requireContext(), "Error recognizing speech", Toast.LENGTH_SHORT).show()
            }
            override fun onResults(results: Bundle?) {
                val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                if (!matches.isNullOrEmpty()) {
                    binding.etMessage.setText(matches[0])
                }
            }
            override fun onPartialResults(partialResults: Bundle?) {}
            override fun onEvent(eventType: Int, params: Bundle?) {}
        })

        speechRecognizer?.startListening(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        speechRecognizer?.destroy()
        _binding = null
    }
}
