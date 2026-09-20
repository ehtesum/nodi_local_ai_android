package com.localai.nodi.engine.emotion

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EmotionEngine @Inject constructor() {
    private val _currentState = MutableStateFlow(evaluateCurrentTemporalState())
    val currentState: StateFlow<EmotionState> = _currentState.asStateFlow()

    fun evaluateTurn(userMessage: String) {
        val lower = userMessage.lowercase()
        val current = _currentState.value

        var mood = current.mood
        var humor = current.humor
        var style = current.conversationStyle

        if (lower.contains("sad") || lower.contains("কষ্ট") || lower.contains("মন খারাপ") || lower.contains("tired") || lower.contains("ক্লান্ত")) {
            mood = "Empathetic"
            humor = "Low"
            style = "Gentle & Supportive"
        } else if (lower.contains("happy") || lower.contains("আনন্দ") || lower.contains("মজা") || lower.contains("excited")) {
            mood = "Excited"
            humor = "High"
            style = "Upbeat & Playful"
        } else if (lower.contains("code") || lower.contains("error") || lower.contains("bug") || lower.contains("কোড")) {
            mood = "Focused"
            humor = "Low"
            style = "Structured & Technical"
        }

        val temporal = evaluateCurrentTemporalState()
        _currentState.value = temporal.copy(
            mood = mood,
            humor = humor,
            conversationStyle = style
        )
    }

    private fun evaluateCurrentTemporalState(): EmotionState {
        val calendar = Calendar.getInstance()
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val dayFormat = SimpleDateFormat("EEEE", Locale.getDefault())

        val timeStr = timeFormat.format(calendar.time)
        val dayStr = dayFormat.format(calendar.time)
        val hour = calendar.get(Calendar.HOUR_OF_DAY)

        val style = if (hour >= 22 || hour < 5) "Calm & Quiet" else "Relaxed"
        val energy = if (hour >= 22 || hour < 5) "Low" else "Medium"

        return EmotionState(
            time = timeStr,
            day = dayStr,
            conversationStyle = style,
            energy = energy
        )
    }
}
