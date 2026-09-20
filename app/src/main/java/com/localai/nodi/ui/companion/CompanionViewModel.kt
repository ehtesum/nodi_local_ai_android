package com.localai.nodi.ui.companion

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.localai.nodi.data.storage.db.MessageEntity
import com.localai.nodi.data.storage.db.NodiDao
import com.localai.nodi.engine.emotion.EmotionEngine
import com.localai.nodi.engine.emotion.EmotionState
import com.localai.nodi.orchestrator.OrchestratorService
import com.localai.nodi.resource.ResourceManager
import com.localai.nodi.resource.model.ResourceState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

data class UiMessage(
    val id: String = UUID.randomUUID().toString(),
    val role: String,
    val text: String,
    val isStreaming: Boolean = false
)

data class CompanionUiState(
    val messages: List<UiMessage> = listOf(
        UiMessage(
            role = "assistant",
            text = "শুভ সন্ধ্যা! আমি নদী (Nodi), আপনার একান্ত ব্যক্তিগত ও অফলাইন এআই সঙ্গী। আজকের দিনটা কেমন কাটল আপনার? 🙂"
        )
    ),
    val inputText: String = "",
    val isGenerating: Boolean = false,
    val currentTokensPerSec: Float = 0f,
    val emotionState: EmotionState = EmotionState(),
    val resourceState: ResourceState = ResourceState()
)

@HiltViewModel
class CompanionViewModel @Inject constructor(
    private val orchestratorService: OrchestratorService,
    private val emotionEngine: EmotionEngine,
    private val resourceManager: ResourceManager,
    private val nodiDao: NodiDao
) : ViewModel() {

    private val _uiState = MutableStateFlow(CompanionUiState())
    val uiState: StateFlow<CompanionUiState> = _uiState.asStateFlow()

    private val currentConversationId = UUID.randomUUID().toString()

    init {
        emotionEngine.currentState.onEach { emotion ->
            _uiState.value = _uiState.value.copy(emotionState = emotion)
        }.launchIn(viewModelScope)

        resourceManager.resourceState.onEach { res ->
            _uiState.value = _uiState.value.copy(resourceState = res)
        }.launchIn(viewModelScope)
    }

    fun onInputTextChanged(text: String) {
        _uiState.value = _uiState.value.copy(inputText = text)
    }

    fun sendMessage() {
        val text = _uiState.value.inputText.trim()
        if (text.isEmpty() || _uiState.value.isGenerating) return

        val userMsg = UiMessage(role = "user", text = text)
        val assistantPlaceholder = UiMessage(role = "assistant", text = "", isStreaming = true)

        _uiState.value = _uiState.value.copy(
            messages = _uiState.value.messages + userMsg + assistantPlaceholder,
            inputText = "",
            isGenerating = true
        )

        viewModelScope.launch {
            val responseBuilder = StringBuilder()
            orchestratorService.processUserTurn(currentConversationId, text)
                .collect { token ->
                    responseBuilder.append(token)
                    updateStreamingMessage(responseBuilder.toString(), orchestratorService.getActiveTokensPerSec())
                }
            finishStreamingMessage(responseBuilder.toString())
        }
    }

    private fun updateStreamingMessage(currentText: String, tps: Float) {
        val currentMsgs = _uiState.value.messages.toMutableList()
        if (currentMsgs.isNotEmpty()) {
            val lastIdx = currentMsgs.lastIndex
            currentMsgs[lastIdx] = currentMsgs[lastIdx].copy(text = currentText, isStreaming = true)
        }
        _uiState.value = _uiState.value.copy(
            messages = currentMsgs,
            currentTokensPerSec = tps
        )
    }

    private fun finishStreamingMessage(finalText: String) {
        val currentMsgs = _uiState.value.messages.toMutableList()
        if (currentMsgs.isNotEmpty()) {
            val lastIdx = currentMsgs.lastIndex
            currentMsgs[lastIdx] = currentMsgs[lastIdx].copy(text = finalText, isStreaming = false)
        }
        _uiState.value = _uiState.value.copy(
            messages = currentMsgs,
            isGenerating = false
        )
    }
}
