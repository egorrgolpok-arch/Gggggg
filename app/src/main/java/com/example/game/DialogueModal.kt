package com.example.game

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.MartianGold
import com.example.ui.theme.NeonGlamPink
import com.example.ui.theme.SpaceDark
import com.example.ui.theme.SpaceSurface

@Composable
fun DialogueModal(
    npc: FriendlyNPC,
    nodeIndex: Int,
    onSelectOption: (DialogueOption) -> Unit,
    onClose: () -> Unit
) {
    val currentNode = if (nodeIndex in npc.dialogues.indices) npc.dialogues[nodeIndex] else npc.dialogues.first()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0x99000000))
            .clickable { onClose() },
        contentAlignment = Alignment.BottomCenter
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(SpaceDark, RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                .border(2.dp, npc.color, RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                .clickable(enabled = false) {}
                .padding(20.dp)
                .testTag("dialogue_modal_box")
        ) {
            Column {
                // Speaker Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("💬", fontSize = 22.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = npc.name,
                                color = npc.color,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Black,
                                fontFamily = FontFamily.Monospace
                            )
                            Text(
                                text = npc.role,
                                color = Color(0xFF94A3B8),
                                fontSize = 11.sp
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .background(Color(0xFF334155), RoundedCornerShape(8.dp))
                            .clickable { onClose() }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text("✕ ЗАКРЫТЬ", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Speech Box
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(SpaceSurface, RoundedCornerShape(12.dp))
                        .padding(14.dp)
                ) {
                    Text(
                        text = currentNode.text,
                        color = Color.White,
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Dialogue Options
                Text(
                    text = "ОТВЕТ ВЛАДИКА:",
                    color = MartianGold,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    currentNode.options.forEach { option ->
                        Button(
                            onClick = { onSelectOption(option) },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E293B)),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, NeonGlamPink, RoundedCornerShape(10.dp))
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "➔ ${option.label}",
                                    color = Color.White,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier.weight(1f)
                                )
                                if (option.rewardGold > 0) {
                                    Text("+${option.rewardGold} 🪙", color = MartianGold, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                                if (option.unlocksEndingId != null) {
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("★ ФИНАЛ", color = CyberCyan, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
