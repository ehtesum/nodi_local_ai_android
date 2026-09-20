package com.localai.nodi.engine.llm

import kotlinx.coroutines.flow.Flow

data class ModelConfig(
    val modelPath: String,
    val maxTokens: Int = 1024,
    val temperature: Float = 0.7f,
    val threadCount: Int = 4,
    val useMmap: Boolean = true
)

interface LlmEngine {
    val isModelLoaded: Boolean
    val currentTokensPerSecond: Float
    
    suspend fun loadModel(config: ModelConfig): Result<Unit>
    fun streamInference(prompt: String): Flow<String>
    suspend fun unloadModel()
}
