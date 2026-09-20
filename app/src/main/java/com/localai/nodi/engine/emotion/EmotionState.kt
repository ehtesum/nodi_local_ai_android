package com.localai.nodi.engine.emotion

data class EmotionState(
    val mood: String = "Happy",
    val energy: String = "Medium",
    val stress: String = "Low",
    val conversationStyle: String = "Relaxed",
    val humor: String = "Medium",
    val curiosity: String = "High",
    val language: String = "Bengali",
    val time: String = "22:15",
    val day: String = "Friday"
) {
    fun toPromptHeader(): String = """
        <EMOTION_STATE>
        Mood: $mood | Energy: $energy | Stress: $stress | Style: $conversationStyle | Humor: $humor | Time: $time | Day: $day
        </EMOTION_STATE>
    """.trimIndent()
}
