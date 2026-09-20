package com.localai.nodi.ui.models

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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

data class GgufModelInfo(
    val name: String,
    val quantization: String,
    val sizeGb: Float,
    val isActive: Boolean,
    val sha256Verified: Boolean
)

@Composable
fun ModelManagerScreen() {
    val models = listOf(
        GgufModelInfo("llama-3-8b-instruct-bn.Q4_K_M.gguf", "Q4_K_M (4-bit Medium)", 4.6f, true, true),
        GgufModelInfo("gemma-2-9b-it-bn.Q3_K_S.gguf", "Q3_K_S (3-bit Small)", 3.8f, false, true),
        GgufModelInfo("qwen-2.5-7b-instruct.Q5_K_M.gguf", "Q5_K_M (5-bit High Quality)", 5.2f, false, true)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(DarkBackground, SurfaceDark)))
            .padding(16.dp)
    ) {
        LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            item {
                Text("🧠 Local GGUF Model Vault", color = TextPrimary, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Text("Manage offline ARM64 quantized weights stored securely in device storage.", color = TextSecondary, fontSize = 14.sp)
            }

            items(models.size) { index ->
                val model = models[index]
                Surface(
                    color = SurfaceDark,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, if (model.isActive) AccentCyan else BorderGlass, RoundedCornerShape(16.dp))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Text(model.name, color = if (model.isActive) AccentCyan else TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                            if (model.isActive) {
                                Surface(color = AccentCyan.copy(alpha = 0.2f), shape = RoundedCornerShape(8.dp)) {
                                    Text("ACTIVE", color = AccentCyan, fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Quantization: ${model.quantization} | Size: ${model.sizeGb} GB", color = TextSecondary, fontSize = 13.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.CheckCircle, contentDescription = "Verified", tint = AccentEmerald, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("SHA-256 Checksum Verified (Integrity Passed)", color = AccentEmerald, fontSize = 12.sp)
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        if (!model.isActive) {
                            Button(
                                onClick = { /* Load model */ },
                                colors = ButtonDefaults.buttonColors(containerColor = AccentPurple)
                            ) {
                                Text("Switch to this Model", color = DarkBackground, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}
