package com.localai.nodi.ui.developer

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localai.nodi.ui.theme.AccentCyan
import com.localai.nodi.ui.theme.AccentEmerald
import com.localai.nodi.ui.theme.AccentPurple
import com.localai.nodi.ui.theme.BorderGlass
import com.localai.nodi.ui.theme.DarkBackground
import com.localai.nodi.ui.theme.SurfaceDark
import com.localai.nodi.ui.theme.TextPrimary
import com.localai.nodi.ui.theme.TextSecondary

@Composable
fun DeveloperConsoleScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(DarkBackground, SurfaceDark)))
            .padding(16.dp)
    ) {
        LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            item {
                Text("🛠️ Developer Console & Prompt Inspector", color = TextPrimary, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Text("Inspect injected RAG contexts, emotion headers, and RAW llama.cpp prompts.", color = TextSecondary, fontSize = 14.sp)
            }

            item {
                Surface(
                    color = Color(0xFF131622),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().border(1.dp, AccentCyan, RoundedCornerShape(12.dp))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("LAST SYNTHESIZED SYSTEM PROMPT (RAW)", color = AccentCyan, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = """
<|begin_of_text|><|start_header_id|>system<|end_header_id|>
You are Nodi (নদী), a private offline AI companion.
[EMOTION STATE]: Current Mood: CALM | Style: COMPANION | Intensity: 0.5
[RAG RETRIEVAL CONTEXT]:
- Citation: Nodi Offline Privacy Manifesto (Section 1)
- Content: Nodi is built to operate completely offline on Android smartphones using ARM64 processors.
<|eot_id|><|start_header_id|>user<|end_header_id|>
আজকের আবহাওয়া কেমন?<|eot_id|><|start_header_id|>assistant<|end_header_id|>
                            """.trimIndent(),
                            color = AccentEmerald,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 12.sp,
                            lineHeight = 18.sp
                        )
                    }
                }
            }

            item {
                Surface(
                    color = SurfaceDark,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().border(1.dp, BorderGlass, RoundedCornerShape(12.dp))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("MEMORY TIER INSPECTOR", color = AccentPurple, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("• Working Tier: 1 active turn buffered in RAM", color = TextPrimary, fontSize = 13.sp)
                        Text("• Long-Term SQLCipher: 14 entries stored", color = TextPrimary, fontSize = 13.sp)
                        Text("• Semantic Vector Index: 4 document chunks active", color = TextPrimary, fontSize = 13.sp)
                    }
                }
            }
        }
    }
}
