package com.example.game

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.ElectricPurple
import com.example.ui.theme.GoldCoin
import com.example.ui.theme.HotPink
import com.example.ui.theme.SpaceDark
import com.example.ui.theme.SurfaceCard

@Composable
fun QuestLogModal(
    quests: List<Quest>,
    onClaimQuest: (Quest) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.9f)
                .clip(RoundedCornerShape(20.dp))
                .border(2.dp, Brush.linearGradient(listOf(CyberCyan, HotPink, GoldCoin)), RoundedCornerShape(20.dp))
                .testTag("quest_log_modal"),
            color = SpaceDark
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(18.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "ЖУРНАЛ ЗАДАНИЙ КРЮ",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            color = CyberCyan
                        )
                        Text(
                            text = "Контракты Жульена, Бати, Малика и Маркуса",
                            fontSize = 12.sp,
                            color = Color(0xFF94A3B8)
                        )
                    }
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(36.dp)
                            .background(Color(0x33FFFFFF), CircleShape)
                            .testTag("close_quest_log")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Закрыть",
                            tint = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(bottom = 12.dp)
                ) {
                    items(quests) { quest ->
                        val isReadyToClaim = quest.isCompleted && !quest.isClaimed
                        val ratio = (quest.currentProgress.toFloat() / quest.targetAmount.coerceAtLeast(1)).coerceIn(0f, 1f)

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(
                                    1.dp,
                                    when {
                                        quest.isClaimed -> Color(0xFF334155)
                                        isReadyToClaim -> GoldCoin
                                        else -> CyberCyan.copy(alpha = 0.4f)
                                    },
                                    RoundedCornerShape(14.dp)
                                ),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (quest.isClaimed) Color(0xFF131826) else SurfaceCard
                            )
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = quest.title,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 15.sp,
                                            color = if (quest.isClaimed) Color(0xFF94A3B8) else Color.White
                                        )
                                        Text(
                                            text = "Заказчик: ${quest.giverName}",
                                            fontSize = 11.sp,
                                            color = HotPink
                                        )
                                    }

                                    if (quest.isClaimed) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                imageVector = Icons.Default.CheckCircle,
                                                contentDescription = "Выполнено",
                                                tint = Color(0xFF10B981),
                                                modifier = Modifier.size(16.dp)
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(
                                                text = "Сдано",
                                                fontSize = 12.sp,
                                                color = Color(0xFF10B981),
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    } else {
                                        Text(
                                            text = "${quest.currentProgress} / ${quest.targetAmount}",
                                            fontWeight = FontWeight.ExtraBold,
                                            fontSize = 13.sp,
                                            color = if (quest.isCompleted) GoldCoin else CyberCyan
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                Text(
                                    text = quest.description,
                                    fontSize = 12.sp,
                                    color = Color(0xFFCBD5E1),
                                    lineHeight = 16.sp
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                LinearProgressIndicator(
                                    progress = { ratio },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(5.dp)
                                        .clip(RoundedCornerShape(3.dp)),
                                    color = if (quest.isCompleted) GoldCoin else CyberCyan,
                                    trackColor = Color(0xFF1E293B)
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = "Награда:",
                                            fontSize = 11.sp,
                                            color = Color(0xFF94A3B8)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "💎 +${quest.rewardDiamonds}",
                                            fontSize = 12.sp,
                                            color = CyberCyan,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = "💰 +${quest.rewardGold}",
                                            fontSize = 12.sp,
                                            color = GoldCoin,
                                            fontWeight = FontWeight.Bold
                                        )
                                        quest.unlocksEndingId?.let { eid ->
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                text = "🏆 Концовка #$eid",
                                                fontSize = 11.sp,
                                                color = ElectricPurple,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }

                                    if (isReadyToClaim) {
                                        Button(
                                            onClick = { onClaimQuest(quest) },
                                            modifier = Modifier
                                                .height(32.dp)
                                                .testTag("claim_${quest.id}"),
                                            shape = RoundedCornerShape(8.dp),
                                            colors = ButtonDefaults.buttonColors(containerColor = GoldCoin),
                                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp)
                                        ) {
                                            Text(
                                                text = "Забрать",
                                                color = Color.Black,
                                                fontWeight = FontWeight.ExtraBold,
                                                fontSize = 12.sp
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp)
                        .testTag("exit_quests_button"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = CyberCyan)
                ) {
                    Text(
                        text = "ЗАКРЫТЬ ЖУРНАЛ",
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
            }
        }
    }
}
