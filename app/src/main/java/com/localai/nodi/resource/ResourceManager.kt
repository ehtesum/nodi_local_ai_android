package com.localai.nodi.resource

import android.app.ActivityManager
import android.content.Context
import com.localai.nodi.resource.model.OperatingMode
import com.localai.nodi.resource.model.ResourceState
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ResourceManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager

    private val _resourceState = MutableStateFlow(ResourceState())
    val resourceState: StateFlow<ResourceState> = _resourceState.asStateFlow()

    init {
        startMonitoring()
    }

    private fun startMonitoring() {
        scope.launch {
            while (true) {
                updateMetrics()
                delay(3000L) // Poll telemetry every 3 seconds
            }
        }
    }

    private fun updateMetrics() {
        val memoryInfo = ActivityManager.MemoryInfo()
        activityManager.getMemoryInfo(memoryInfo)

        val totalRam = memoryInfo.totalMem
        val availRam = memoryInfo.availMem
        val availRamGb = availRam / (1024f * 1024f * 1024f)

        val current = _resourceState.value
        val newMode = when {
            availRamGb < 1.5f -> OperatingMode.ULTRA_LOW_MEMORY
            current.batteryLevelPercent < 20 -> OperatingMode.BATTERY_SAVER
            current.isCharging && current.thermalTempCelsius < 37f -> OperatingMode.PERFORMANCE
            else -> OperatingMode.BALANCED
        }

        _resourceState.value = current.copy(
            mode = newMode,
            totalRamBytes = totalRam,
            availRamBytes = availRam
        )
    }

    fun updateBatteryStatus(level: Int, charging: Boolean, tempCelsius: Float) {
        _resourceState.value = _resourceState.value.copy(
            batteryLevelPercent = level,
            isCharging = charging,
            thermalTempCelsius = tempCelsius
        )
    }
}
