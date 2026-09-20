package com.localai.nodi.resource.model

enum class OperatingMode(val displayName: String, val maxTokens: Int, val temperature: Float) {
    PERFORMANCE("Performance Mode", 1024, 0.7f),
    BALANCED("Balanced Mode", 512, 0.7f),
    BATTERY_SAVER("Battery Saver", 256, 0.6f),
    ULTRA_LOW_MEMORY("Ultra Low Memory Mode", 128, 0.5f)
}

data class ResourceState(
    val mode: OperatingMode = OperatingMode.BALANCED,
    val totalRamBytes: Long = 8L * 1024 * 1024 * 1024,
    val availRamBytes: Long = 4L * 1024 * 1024 * 1024,
    val batteryLevelPercent: Int = 85,
    val isCharging: Boolean = false,
    val thermalTempCelsius: Float = 36.5f,
    val cpuLittleCoreUsagePercent: Int = 30,
    val cpuBigCoreUsagePercent: Int = 60,
    val estimatedRuntimeHours: Float = 5.2f
) {
    val formattedRamUsage: String
        get() {
            val usedGb = (totalRamBytes - availRamBytes) / (1024f * 1024f * 1024f)
            val totalGb = totalRamBytes / (1024f * 1024f * 1024f)
            return "%.1f / %.1f GB".format(usedGb, totalGb)
        }
}
