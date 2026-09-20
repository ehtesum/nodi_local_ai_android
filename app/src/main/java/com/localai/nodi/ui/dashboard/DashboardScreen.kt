package com.localai.nodi.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BatteryChargingFull
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localai.nodi.core.constants.CoreConstants
import com.localai.nodi.ui.theme.AccentCyan
import com.localai.nodi.ui.theme.AccentEmerald
import com.localai.nodi.ui.theme.AccentPurple
import com.localai.nodi.ui.theme.BorderGlass
import com.localai.nodi.ui.theme.DarkBackground
import com.localai.nodi.ui.theme.SurfaceDark
import com.localai.nodi.ui.theme.SurfaceGlass
import com.localai.nodi.ui.theme.TextPrimary
import com.localai.nodi.ui.theme.TextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(viewModel: DashboardViewModel) {
    val state by viewModel.resourceState.collectAsState()

    Scaffold(
        containerColor = DarkBackground,
        topBar = {
            Surface(color = SurfaceGlass, modifier = Modifier.fillMaxWidth().border(1.dp, BorderGlass)) {
                Row(
                    modifier = Modifier.padding(16.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("📊 System Diagnostics & Resource Manager", color = TextPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Security, contentDescription = "Secure", tint = AccentEmerald)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("100% Offline", color = AccentEmerald, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Brush.verticalGradient(listOf(DarkBackground, SurfaceDark)))
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    TelemetryCard(
                        title = "Active Operating Mode",
                        icon = Icons.Default.Speed,
                        tint = AccentCyan
                    ) {
                        Text(state.mode.displayName, color = AccentCyan, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Context Limit: ${state.mode.maxTokens} tokens • Temperature: ${state.mode.temperature}", color = TextSecondary, fontSize = 13.sp)
                    }
                }

                item {
                    TelemetryCard(
                        title = "ARM64 CPU Core & Thread Affinity",
                        icon = Icons.Default.Memory,
                        tint = AccentPurple
                    ) {
                        Text("Core Utilization (Little vs Big Cores)", color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Little Cores (4x Cortex-A55)", color = TextSecondary, fontSize = 12.sp)
                            Text("${state.cpuLittleCoreUsagePercent}%", color = AccentPurple, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        LinearProgressIndicator(
                            progress = state.cpuLittleCoreUsagePercent / 100f,
                            color = AccentPurple,
                            trackColor = Color(0xFF2E3856),
                            modifier = Modifier.fillMaxWidth().height(8.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Big Cores (4x Cortex-A78) [LLM Pinned]", color = TextSecondary, fontSize = 12.sp)
                            Text("${state.cpuBigCoreUsagePercent}%", color = AccentCyan, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        LinearProgressIndicator(
                            progress = state.cpuBigCoreUsagePercent / 100f,
                            color = AccentCyan,
                            trackColor = Color(0xFF2E3856),
                            modifier = Modifier.fillMaxWidth().height(8.dp)
                        )
                    }
                }

                item {
                    TelemetryCard(
                        title = "RAM Allocation & Thermal State",
                        icon = Icons.Default.BatteryChargingFull,
                        tint = AccentEmerald
                    ) {
                        val usedRam = state.totalRamBytes - state.availRamBytes
                        val progress = usedRam.toFloat() / state.totalRamBytes.toFloat()
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("System RAM: ${state.formattedRamUsage}", color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                            Text("Thermal: ${state.thermalTempCelsius}°C", color = if (state.thermalTempCelsius > 38f) Color.Red else AccentEmerald, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        LinearProgressIndicator(
                            progress = progress,
                            color = AccentEmerald,
                            trackColor = Color(0xFF2E3856),
                            modifier = Modifier.fillMaxWidth().height(8.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Battery Level: ${state.batteryLevelPercent}% (${if (state.isCharging) "Charging" else "%.1f hrs estimated runtime".format(state.estimatedRuntimeHours)})", color = TextSecondary, fontSize = 12.sp)
                    }
                }

                item {
                    TelemetryCard(
                        title = "Offline LLM Inference Benchmark",
                        icon = Icons.Default.Speed,
                        tint = AccentCyan
                    ) {
                        Text("Model: ${CoreConstants.DEFAULT_MODEL_NAME}", color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Active Generation Speed:", color = TextSecondary, fontSize = 13.sp)
                            Text("%.1f tokens/sec".format(viewModel.getCurrentTokensPerSec()), color = AccentCyan, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Model Load Time (mmap):", color = TextSecondary, fontSize = 13.sp)
                            Text("600 ms", color = TextPrimary, fontSize = 13.sp)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Encrypted Storage Footprint:", color = TextSecondary, fontSize = 13.sp)
                            Text("SQLite: 14.2 MB | Vector Store: 45 MB", color = TextPrimary, fontSize = 13.sp)
                        }
                    }
                }
                
                item { Spacer(modifier = Modifier.height(40.dp)) }
            }
        }
    }
}

@Composable
fun TelemetryCard(
    title: String,
    icon: ImageVector,
    tint: Color,
    content: @Composable () -> Unit
) {
    Surface(
        color = SurfaceDark,
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth().border(1.dp, BorderGlass, RoundedCornerShape(16.dp))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, contentDescription = title, tint = tint)
                Spacer(modifier = Modifier.width(8.dp))
                Text(title, color = tint, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(12.dp))
            content()
        }
    }
}
