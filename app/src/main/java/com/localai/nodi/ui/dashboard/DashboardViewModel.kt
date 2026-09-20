package com.localai.nodi.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.localai.nodi.orchestrator.OrchestratorService
import com.localai.nodi.resource.ResourceManager
import com.localai.nodi.resource.model.ResourceState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val resourceManager: ResourceManager,
    private val orchestratorService: OrchestratorService
) : ViewModel() {

    val resourceState: StateFlow<ResourceState> = resourceManager.resourceState
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ResourceState())

    fun getCurrentTokensPerSec(): Float = orchestratorService.getActiveTokensPerSec()
}
