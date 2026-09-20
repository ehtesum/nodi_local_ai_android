package com.localai.nodi.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
import com.localai.nodi.ui.companion.CompanionScreen
import com.localai.nodi.ui.companion.CompanionViewModel
import com.localai.nodi.ui.dashboard.DashboardScreen
import com.localai.nodi.ui.dashboard.DashboardViewModel
import com.localai.nodi.ui.developer.DeveloperConsoleScreen
import com.localai.nodi.ui.models.ModelManagerScreen
import com.localai.nodi.ui.theme.AccentCyan
import com.localai.nodi.ui.theme.DarkBackground
import com.localai.nodi.ui.theme.NodiTheme
import com.localai.nodi.ui.theme.SurfaceDark
import com.localai.nodi.ui.theme.TextSecondary

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val companionViewModel: CompanionViewModel by viewModels()
    private val dashboardViewModel: DashboardViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NodiTheme {
                var selectedTab by remember { mutableStateOf(0) }

                Scaffold(
                    containerColor = DarkBackground,
                    bottomBar = {
                        NavigationBar(containerColor = SurfaceDark) {
                            NavigationBarItem(
                                selected = selectedTab == 0,
                                onClick = { selectedTab = 0 },
                                icon = { Icon(Icons.Default.ChatBubble, contentDescription = "Companion") },
                                label = { Text("🤖 Nodi") },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = AccentCyan,
                                    selectedTextColor = AccentCyan,
                                    unselectedIconColor = TextSecondary,
                                    unselectedTextColor = TextSecondary
                                )
                            )
                            NavigationBarItem(
                                selected = selectedTab == 1,
                                onClick = { selectedTab = 1 },
                                icon = { Icon(Icons.Default.Dashboard, contentDescription = "Diagnostics") },
                                label = { Text("📊 Telemetry") },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = AccentCyan,
                                    selectedTextColor = AccentCyan,
                                    unselectedIconColor = TextSecondary,
                                    unselectedTextColor = TextSecondary
                                )
                            )
                            NavigationBarItem(
                                selected = selectedTab == 2,
                                onClick = { selectedTab = 2 },
                                icon = { Icon(Icons.Default.Storage, contentDescription = "Models") },
                                label = { Text("🧠 Models") },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = AccentCyan,
                                    selectedTextColor = AccentCyan,
                                    unselectedIconColor = TextSecondary,
                                    unselectedTextColor = TextSecondary
                                )
                            )
                            NavigationBarItem(
                                selected = selectedTab == 3,
                                onClick = { selectedTab = 3 },
                                icon = { Icon(Icons.Default.Build, contentDescription = "Dev Console") },
                                label = { Text("🛠️ Dev") },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = AccentCyan,
                                    selectedTextColor = AccentCyan,
                                    unselectedIconColor = TextSecondary,
                                    unselectedTextColor = TextSecondary
                                )
                            )
                        }
                    }
                ) { innerPadding ->
                    androidx.compose.foundation.layout.Box(modifier = Modifier.padding(innerPadding)) {
                        when (selectedTab) {
                            0 -> CompanionScreen(viewModel = companionViewModel)
                            1 -> DashboardScreen(viewModel = dashboardViewModel)
                            2 -> ModelManagerScreen()
                            3 -> DeveloperConsoleScreen()
                        }
                    }
                }
            }
        }
    }
}
