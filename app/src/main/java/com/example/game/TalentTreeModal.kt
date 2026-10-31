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
import androidx.compose.ui.text.style.TextAlign
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
fun TalentTreeModal(
    talents: MetaprogressionTalents,
    diamonds: Int,
    gold: Int,
    onUpgradeTalent: (TalentType) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.92f)
                .clip(RoundedCornerShape(20.dp))
                .border(2.dp, Brush.linearGradient(listOf(GoldCoin, HotPink, ElectricPurple)), RoundedCornerShape(20.dp))
                .testTag("talent_tree_modal"),
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
                            text = "ДЕРЕВО ТАЛАНТОВ БАТИ",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            color = GoldCoin
                        )
                        Text(
                            text = "Постоянные апгрейды на 5000+ часов",
                            fontSize = 12.sp,
                            color = Color(0xFF94A3B8)
                        )
                    }
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(36.dp)
                            .background(Color(0x33FFFFFF), CircleShape)
                            .testTag("close_talent_tree")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Закрыть",
                            tint = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Currencies display
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(SurfaceCard, RoundedCornerShape(12.dp))
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "💎", fontSize = 16.sp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "$diamonds Алмазов",
                            fontWeight = FontWeight.Bold,
                            color = CyberCyan,
                            fontSize = 14.sp
                        )
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "💰", fontSize = 16.sp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "$gold Золота",
                            fontWeight = FontWeight.Bold,
                            color = GoldCoin,
                            fontSize = 14.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // List of talents
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(bottom = 12.dp)
                ) {
                    items(TalentType.values()) { talentType ->
                        val currentTier = when (talentType) {
                            TalentType.HEALTH -> talents.healthTier
                            TalentType.DAMAGE -> talents.damageTier
                            TalentType.DASH -> talents.dashCooldownTier
                            TalentType.HYPE -> talents.hypeRateTier
                            TalentType.GREED -> talents.greedTier
                            TalentType.VAMPIRISM -> talents.vampirismTier
                        }
                        val isMaxed = currentTier >= talentType.maxTier
                        val reqDiamonds = talentType.baseCostDiamonds * (currentTier + 1)
                        val reqGold = talentType.baseCostGold * (currentTier + 1)
                        val canAfford = !isMaxed && diamonds >= reqDiamonds && gold >= reqGold

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(
                                    1.dp,
                                    if (canAfford) GoldCoin.copy(alpha = 0.6f) else Color(0x22FFFFFF),
                                    RoundedCornerShape(14.dp)
                                ),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = SurfaceCard)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(40.dp)
                                                .background(
                                                    Brush.radialGradient(listOf(HotPink.copy(alpha = 0.3f), Color.Transparent)),
                                                    CircleShape
                                                ),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(text = talentType.iconEmoji, fontSize = 20.sp)
                                        }
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Column {
                                            Text(
                                                text = talentType.title,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 15.sp,
                                                color = Color.White
                                            )
                                            Text(
                                                text = talentType.description,
                                                fontSize = 11.sp,
                                                color = Color(0xFFCBD5E1)
                                            )
                                        }
                                    }

                                    Text(
                                        text = "$currentTier / ${talentType.maxTier}",
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 13.sp,
                                        color = if (isMaxed) GoldCoin else CyberCyan
                                    )
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                // Progress bar
                                LinearProgressIndicator(
                                    progress = { currentTier.toFloat() / talentType.maxTier },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(6.dp)
                                        .clip(RoundedCornerShape(3.dp)),
                                    color = if (isMaxed) GoldCoin else HotPink,
                                    trackColor = Color(0xFF1E293B)
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                // Upgrade row
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    if (isMaxed) {
                                        Text(
                                            text = "МАКСИМАЛЬНЫЙ РАНГ!",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.sp,
                                            color = GoldCoin
                                        )
                                    } else {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(
                                                text = "💎 $reqDiamonds",
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.SemiBold,
                                                color = if (diamonds >= reqDiamonds) CyberCyan else Color(0xFFEF4444)
                                            )
                                            Spacer(modifier = Modifier.width(10.dp))
                                            Text(
                                                text = "💰 $reqGold",
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.SemiBold,
                                                color = if (gold >= reqGold) GoldCoin else Color(0xFFEF4444)
                                            )
                                        }

                                        Button(
                                            onClick = { onUpgradeTalent(talentType) },
                                            enabled = canAfford,
                                            modifier = Modifier
                                                .height(34.dp)
                                                .testTag("upgrade_${talentType.name.lowercase()}"),
                                            shape = RoundedCornerShape(8.dp),
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = GoldCoin,
                                                contentColor = Color.Black,
                                                disabledContainerColor = Color(0xFF334155),
                                                disabledContentColor = Color(0xFF64748B)
                                            ),
                                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 0.dp)
                                        ) {
                                            Text(
                                                text = "Прокачать",
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold
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
                        .testTag("exit_talents_button"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = HotPink)
                ) {
                    Text(
                        text = "ВЕРНУТЬСЯ В ХАБ",
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}
