package com.localai.nodi.orchestrator

import com.localai.nodi.core.constants.CoreConstants
import com.localai.nodi.data.storage.db.MessageEntity
import com.localai.nodi.data.storage.db.NodiDao
import com.localai.nodi.engine.emotion.EmotionEngine
import com.localai.nodi.engine.llm.LlmEngine
import com.localai.nodi.engine.llm.ModelConfig
import com.localai.nodi.resource.ResourceManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OrchestratorService @Inject constructor(
    private val llmEngine: LlmEngine,
    private val emotionEngine: EmotionEngine,
    private val resourceManager: ResourceManager,
    private val nodiDao: NodiDao
) {
    suspend fun initializeOfflineModel() {
        if (!llmEngine.isModelLoaded) {
            val mode = resourceManager.resourceState.value.mode
            llmEngine.loadModel(
                ModelConfig(
                    modelPath = CoreConstants.DEFAULT_MODEL_NAME,
                    maxTokens = mode.maxTokens,
                    temperature = mode.temperature
                )
            )
        }
    }

    suspend fun processUserTurn(conversationId: String, userInput: String): Flow<String> {
        // 1. Evaluate emotion state based on user input
        emotionEngine.evaluateTurn(userInput)

        // 2. Save user message to encrypted SQLite
        nodiDao.insertMessage(
            MessageEntity(
                conversationId = conversationId,
                role = "user",
                content = userInput
            )
        )

        // 3. Construct master system prompt header
        val emotionHeader = emotionEngine.currentState.value.toPromptHeader()
        val systemPrompt = """
            You are Nodi (${CoreConstants.NODI_NAME_BN}), a private offline AI companion.
            $emotionHeader
            User input: $userInput
        """.trimIndent()

        // 4. Stream response from offline LLM engine
        val responseBuilder = StringBuilder()
        return llmEngine.streamInference(systemPrompt)
            .onStart {
                initializeOfflineModel()
            }
            .map { token ->
                responseBuilder.append(token)
                token
            }
            .onCompletion {
                // 5. Store completed assistant response
                nodiDao.insertMessage(
                    MessageEntity(
                        conversationId = conversationId,
                        role = "assistant",
                        content = responseBuilder.toString().trim()
                    )
                )
            }
    }

    fun getActiveTokensPerSec(): Float = llmEngine.currentTokensPerSecond
}
