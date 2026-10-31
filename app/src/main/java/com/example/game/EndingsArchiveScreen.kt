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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
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
fun EndingsArchiveScreen(
    unlockedEndingIds: Set<Int>,
    onBack: () -> Unit
) {
    val allEndings = ProceduralWorld.all100Endings

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        SpaceDark,
                        Color(0xFF1B0B33),
                        Color(0xFF0F041D)
                    )
                )
            )
            .statusBarsPadding()
            .padding(16.dp)
            .testTag("endings_archive_screen")
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "ХРОНИКИ 100 КОНЦОВОК",
                        color = MartianGold,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace
                    )
                    Text(
                        text = "Летопись марсианских побед Владика",
                        color = Color(0xFF94A3B8),
                        fontSize = 11.sp
                    )
                }

                Box(
                    modifier = Modifier
                        .background(Color(0xFF334155), RoundedCornerShape(8.dp))
                        .clickable { onBack() }
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                        .testTag("back_from_endings_button")
                ) {
                    Text("❮ НАЗАД", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Progress Banner
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SpaceSurface, RoundedCornerShape(12.dp))
                    .border(1.dp, NeonGlamPink, RoundedCornerShape(12.dp))
                    .padding(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("ПРОГРЕСС СЮЖЕТА:", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Text("В каждой концовке Владик празднует победу над цензорами!", color = Color(0xFFCBD5E1), fontSize = 10.sp)
                    }

                    Box(
                        modifier = Modifier
                            .background(MartianGold, RoundedCornerShape(8.dp))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            "${unlockedEndingIds.size} / 100",
                            color = Color.Black,
                            fontWeight = FontWeight.Black,
                            fontSize = 13.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // List of 100 Endings
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(allEndings) { ending ->
                    val isUnlocked = unlockedEndingIds.contains(ending.id)

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                if (isUnlocked) Color(0xFF1E1438) else Color(0xFF110D1F),
                                RoundedCornerShape(12.dp)
                            )
                            .border(
                                1.dp,
                                if (isUnlocked) MartianGold else Color(0xFF334155),
                                RoundedCornerShape(12.dp)
                            )
                            .padding(14.dp)
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = if (isUnlocked) ending.title else "🔒 Финал #${ending.id} [Заблокировано]",
                                    color = if (isUnlocked) MartianGold else Color(0xFF64748B),
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )

                                Text(
                                    text = if (isUnlocked) "ОТКРЫТО ★" else "СЕКРЕТ",
                                    color = if (isUnlocked) CyberCyan else Color(0xFF475569),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = if (isUnlocked) ending.description else "Побеждайте боссов на этажах особняка и ведите диалоги с NPC Шефом Маркусом и DJ Маликом для открытия.",
                                color = if (isUnlocked) Color(0xFFE2E8F0) else Color(0xFF475569),
                                fontSize = 11.sp,
                                lineHeight = 15.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
