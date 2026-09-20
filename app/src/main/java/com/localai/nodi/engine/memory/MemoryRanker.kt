package com.localai.nodi.engine.memory

import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.max

@Singleton
class MemoryRanker @Inject constructor() {

    fun calculateImportanceScore(text: String, lastAccessedTimestamp: Long): Float {
        val lower = text.lowercase()
        var score = 0.5f

        // Increase importance if it mentions explicit personal user facts or emotions
        if (lower.contains("my name is") || lower.contains("আমার নাম") ||
            lower.contains("i like") || lower.contains("আমি পছন্দ করি") ||
            lower.contains("remember") || lower.contains("মনে রেখো") ||
            lower.contains("always") || lower.contains("সবসময়")) {
            score += 0.35f
        }

        // Temporal decay calculation
        val hoursElapsed = max(0L, (System.currentTimeMillis() - lastAccessedTimestamp) / (1000L * 3600L))
        val decay = (hoursElapsed * 0.005f).coerceAtMost(0.3f)
        score -= decay

        return score.coerceIn(0.1f, 1.0f)
    }
}
