package com.localai.nodi.engine.voice

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

enum class VadState { IDLE, LISTENING, PROCESSING }

@Singleton
class VoiceEngine @Inject constructor() {

    private val _vadState = MutableStateFlow(VadState.IDLE)
    val vadState: StateFlow<VadState> = _vadState.asStateFlow()

    fun startListening() {
        _vadState.value = VadState.LISTENING
    }

    fun stopListeningAndTranscribe(): String {
        _vadState.value = VadState.PROCESSING
        // Offline whisper / sherpa-onnx simulated transcription in Bengali
        val sampleTranscriptions = listOf(
            "আজকের আবহাওয়া কেমন?",
            "আমার জন্য একটা কবিতা লেখো।",
            "নদী, তুমি কেমন আছো?"
        )
        val result = sampleTranscriptions.random()
        _vadState.value = VadState.IDLE
        return result
    }
}
