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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.MartianGold
import com.example.ui.theme.NeonGlamPink
import com.example.ui.theme.SpaceDark
import com.example.ui.theme.SpaceSurface

@Composable
fun ArtifactInspectModal(
    worldArtifact: WorldArtifact,
    onCollect: (WorldArtifact) -> Unit,
    onClose: () -> Unit
) {
    val art = worldArtifact.artifact

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xCC000000))
            .clickable { onClose() },
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .background(SpaceDark, RoundedCornerShape(20.dp))
                .border(2.dp, art.rarity.color, RoundedCornerShape(20.dp))
                .clickable(enabled = false) {}
                .padding(22.dp)
                .testTag("artifact_inspect_box")
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                // Relic badge
                Box(
                    modifier = Modifier
                        .size(68.dp)
                        .clip(CircleShape)
                        .background(art.rarity.color.copy(alpha = 0.2f))
                        .border(3.dp, art.rarity.color, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text("💎", fontSize = 32.sp)
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = art.name,
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                    fontFamily = FontFamily.Monospace,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = art.rarity.title,
                    color = art.rarity.color,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Lore Story Box
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(SpaceSurface, RoundedCornerShape(10.dp))
                        .padding(12.dp)
                ) {
                    Column {
                        Text("ИСТОРИЯ РЕЛИКИИ:", color = MartianGold, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            art.loreStory.ifEmpty { art.description },
                            color = Color(0xFFCBD5E1),
                            fontSize = 12.sp,
                            lineHeight = 16.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Stats
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    if (art.bonusHp > 0) Text("+${art.bonusHp.toInt()} ❤️ HP", color = Color(0xFFFF0055), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    if (art.bonusDamage > 0) Text("+${art.bonusDamage.toInt()} ⚔️ Урон", color = MartianGold, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    if (art.bonusSpeed > 0) Text("+${art.bonusSpeed.toInt()} ⚡ Скорость", color = CyberCyan, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = { onClose() },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF334155)),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("ОСТАВИТЬ", fontSize = 12.sp)
                    }

                    Button(
                        onClick = { onCollect(worldArtifact) },
                        colors = ButtonDefaults.buttonColors(containerColor = NeonGlamPink),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1.5f).testTag("collect_artifact_button")
                    ) {
                        Text("ЗАБРАТЬ СЕБЕ!", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}
