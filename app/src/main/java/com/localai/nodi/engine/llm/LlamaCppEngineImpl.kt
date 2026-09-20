package com.localai.nodi.engine.llm

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

@Singleton
class LlamaCppEngineImpl @Inject constructor() : LlmEngine {
    private var _isLoaded = true
    private var _tps = 24.8f

    override val isModelLoaded: Boolean
        get() = _isLoaded

    override val currentTokensPerSecond: Float
        get() = _tps

    override suspend fun loadModel(config: ModelConfig): Result<Unit> {
        delay(600) // Simulate mmap and KV cache allocation
        _isLoaded = true
        return Result.success(Unit)
    }

    override fun streamInference(prompt: String): Flow<String> = flow {
        // Simulate response tokens in conversational Bengali / English
        val responseTokens = listOf(
            "আরে! ", "অবশ্যই। ", "জীবনের ", "তিনটা ", "নিশ্চিত ", "জিনিস— ",
            "মৃত্যু, ", "ট্যাক্স, ", "আর ", "সোমবারের ", "মিটিং! 😄 ",
            "একটু ", "চা ", "খেয়ে ", "বিশ্রাম ", "নিন। ",
            "আমি ", "সবসময় ", "আপনার ", "পাশেই ", "আছি।"
        )
        for (token in responseTokens) {
            delay(Random.nextLong(30L, 55L)) // ~25 tokens/sec latency simulation
            _tps = Random.nextFloat() * 4f + 23f
            emit(token)
        }
    }

    override suspend fun unloadModel() {
        _isLoaded = false
    }
}
