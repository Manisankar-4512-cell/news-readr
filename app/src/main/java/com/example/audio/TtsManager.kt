package com.example.audio

import android.content.Context
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import com.example.model.NewsArticle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale

data class TtsPlaybackState(
    val isPlaying: Boolean = false,
    val currentArticle: NewsArticle? = null,
    val currentSectionIndex: Int = 0,
    val totalSections: Int = 0,
    val speechRate: Float = 1.0f,
    val isReady: Boolean = false
)

class TtsManager(context: Context) : TextToSpeech.OnInitListener {

    private val appContext = context.applicationContext
    private var tts: TextToSpeech? = null
    private val _state = MutableStateFlow(TtsPlaybackState())
    val state: StateFlow<TtsPlaybackState> = _state.asStateFlow()

    private var currentSections: List<String> = emptyList()

    init {
        tts = TextToSpeech(appContext, this)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts?.language = Locale.getDefault()
            tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                override fun onStart(utteranceId: String?) {
                    _state.value = _state.value.copy(isPlaying = true)
                }

                override fun onDone(utteranceId: String?) {
                    val nextIndex = _state.value.currentSectionIndex + 1
                    if (nextIndex < currentSections.size) {
                        _state.value = _state.value.copy(currentSectionIndex = nextIndex)
                        speakSection(nextIndex)
                    } else {
                        _state.value = _state.value.copy(isPlaying = false, currentSectionIndex = 0)
                    }
                }

                @Deprecated("Deprecated in Java")
                override fun onError(utteranceId: String?) {
                    _state.value = _state.value.copy(isPlaying = false)
                }

                override fun onError(utteranceId: String?, errorCode: Int) {
                    Log.e("TtsManager", "TTS error code: $errorCode")
                    _state.value = _state.value.copy(isPlaying = false)
                }
            })
            _state.value = _state.value.copy(isReady = true)
        } else {
            Log.e("TtsManager", "TTS initialization failed")
        }
    }

    fun playArticle(article: NewsArticle) {
        if (_state.value.currentArticle?.id == article.id && _state.value.isPlaying) {
            pause()
            return
        }

        // Build sections: Title, then Key Takeaways header + bullets, then paragraphs
        val sections = mutableListOf<String>()
        sections.add(article.title)
        sections.add("Key takeaways: " + article.keyTakeaways.joinToString(". "))
        sections.addAll(article.content)

        currentSections = sections
        _state.value = _state.value.copy(
            isPlaying = true,
            currentArticle = article,
            currentSectionIndex = 0,
            totalSections = sections.size
        )
        speakSection(0)
    }

    fun togglePlayPause() {
        val currentArticle = _state.value.currentArticle ?: return
        if (_state.value.isPlaying) {
            pause()
        } else {
            resume()
        }
    }

    fun pause() {
        tts?.stop()
        _state.value = _state.value.copy(isPlaying = false)
    }

    fun resume() {
        if (currentSections.isNotEmpty()) {
            _state.value = _state.value.copy(isPlaying = true)
            speakSection(_state.value.currentSectionIndex)
        }
    }

    fun stop() {
        tts?.stop()
        _state.value = _state.value.copy(
            isPlaying = false,
            currentArticle = null,
            currentSectionIndex = 0,
            totalSections = 0
        )
        currentSections = emptyList()
    }

    fun nextSection() {
        val next = (_state.value.currentSectionIndex + 1).coerceAtMost(currentSections.size - 1)
        if (next != _state.value.currentSectionIndex) {
            _state.value = _state.value.copy(currentSectionIndex = next)
            speakSection(next)
        }
    }

    fun prevSection() {
        val prev = (_state.value.currentSectionIndex - 1).coerceAtLeast(0)
        _state.value = _state.value.copy(currentSectionIndex = prev)
        speakSection(prev)
    }

    fun cycleSpeed() {
        val currentRate = _state.value.speechRate
        val nextRate = when (currentRate) {
            0.85f -> 1.0f
            1.0f -> 1.25f
            1.25f -> 1.5f
            else -> 0.85f
        }
        _state.value = _state.value.copy(speechRate = nextRate)
        tts?.setSpeechRate(nextRate)
        if (_state.value.isPlaying) {
            speakSection(_state.value.currentSectionIndex)
        }
    }

    private fun speakSection(index: Int) {
        if (index in currentSections.indices) {
            val text = currentSections[index]
            val params = Bundle()
            params.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "section_$index")
            tts?.setSpeechRate(_state.value.speechRate)
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, params, "section_$index")
        }
    }

    fun shutdown() {
        try {
            tts?.stop()
            tts?.shutdown()
        } catch (e: Exception) {
            Log.e("TtsManager", "Error shutting down TTS", e)
        }
    }
}
