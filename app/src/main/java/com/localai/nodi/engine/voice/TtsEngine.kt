package com.localai.nodi.engine.voice

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TtsEngine @Inject constructor() {

    private var isSpeaking = false

    fun synthesizeAndSpeak(text: String, pitch: Float = 1.0f, speechRate: Float = 0.95f) {
        isSpeaking = true
        // Simulated Piper TTS offline Bengali voice synthesis
        println("🔊 [Piper TTS Offline] Synthesizing Bengali Speech (Pitch: $pitch, Rate: $speechRate): \"$text\"")
        isSpeaking = false
    }

    fun stopSpeaking() {
        isSpeaking = false
    }
}
