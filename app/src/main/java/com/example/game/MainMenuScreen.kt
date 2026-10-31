package com.example.game

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
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
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.SpaceCard
import com.example.ui.theme.SpaceDark
import com.example.ui.theme.SpaceSurface

@Composable
fun MainMenuScreen(
    viewModel: GameViewModel,
    state: GameState
) {
    val infiniteTransition = rememberInfiniteTransition(label = "menu_anim")
    val titlePulse by infiniteTransition.animateFloat(
        initialValue = 0.98f,
        targetValue = 1.02f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "title_pulse"
    )

    var selectedDifficulty by remember { mutableStateOf(GameDifficulty.NORMAL) }
    var starterBonus by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        SpaceDark,
                        Color(0xFF240E40),
                        Color(0xFF0F041D)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(24.dp)
        ) {
            // Main Title
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.scale(titlePulse)
            ) {
                Text(
                    text = "👑 MARS WORLD 👑",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Black,
                    fontFamily = FontFamily.Monospace,
                    color = MartianGold,
                    letterSpacing = 2.sp
                )
                Text(
                    text = "РОГАЛИК: 800 ЭТАЖЕЙ РОСКОШИ",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = NeonGlamPink,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "ВЛАДИК И 6969 ГИПЕРКАРОВ ПРОТИВ ЦЕНЗУРЫ",
                    fontSize = 10.sp,
                    color = CyberCyan,
                    letterSpacing = 1.sp
                )
            }

            Spacer(modifier = Modifier.height(36.dp))

            // Menu Buttons Container
            Column(
                modifier = Modifier.fillMaxWidth(0.85f),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Continue Saved Game
                Button(
                    onClick = { viewModel.continueSavedGame() },
                    enabled = state.hasSavedGame,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MartianGold,
                        disabledContainerColor = Color(0xFF334155),
                        contentColor = Color.Black,
                        disabledContentColor = Color(0xFF64748B)
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("continue_game_button")
                ) {
                    Text(
                        if (state.hasSavedGame) "▶ ПРОДОЛЖИТЬ ИГРУ" else "НЕТ СОХРАНЕНИЙ",
                        fontWeight = FontWeight.Black,
                        fontSize = 14.sp
                    )
                }

                // New Game
                Button(
                    onClick = { viewModel.showNewGameDialog(true) },
                    colors = ButtonDefaults.buttonColors(containerColor = NeonGlamPink),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("new_game_button")
                ) {
                    Text(
                        "✦ НОВАЯ ИГРА ✦",
                        fontWeight = FontWeight.Black,
                        fontSize = 14.sp,
                        color = Color.White
                    )
                }

                // 100 Endings Archive
                Button(
                    onClick = { viewModel.openEndingsArchive() },
                    colors = ButtonDefaults.buttonColors(containerColor = NeonPurple),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("endings_archive_button")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            "АРХИВ 100 КОНЦОВОК",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .background(MartianGold, RoundedCornerShape(6.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                "${state.unlockedEndings.size}/100",
                                color = Color.Black,
                                fontWeight = FontWeight.Black,
                                fontSize = 11.sp
                            )
                        }
                    }
                }

                // VIP Hub Entrance
                Button(
                    onClick = { viewModel.openHub() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E1B4B)),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, MartianGold),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("vip_hub_button")
                ) {
                    Text(
                        "🌟 VIP-ХАБ: ПЕНТХАУС И ТАЛАНТЫ",
                        fontWeight = FontWeight.Black,
                        fontSize = 13.sp,
                        color = MartianGold
                    )
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            // Studio signature
            Text(
                text = "PRIDE GAYS STUDIO • 2026 EDITION",
                color = Color(0xFF94A3B8),
                fontSize = 10.sp,
                letterSpacing = 1.sp
            )
        }

        // New Game Settings Modal
        if (state.showNewGameDialog) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xCC000000))
                    .clickable { viewModel.showNewGameDialog(false) },
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.92f)
                        .background(SpaceDark, RoundedCornerShape(20.dp))
                        .border(2.dp, MartianGold, RoundedCornerShape(20.dp))
                        .clickable(enabled = false) {}
                        .padding(20.dp)
                ) {
                    Column {
                        Text(
                            "ПАРАМЕТРЫ НОВОЙ ИГРЫ",
                            color = MartianGold,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            fontFamily = FontFamily.Monospace
                        )
                        Text(
                            "Настройте экспедицию Владика на Марсе",
                            color = Color(0xFF94A3B8),
                            fontSize = 11.sp
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text("СЛОЖНОСТЬ:", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        Spacer(modifier = Modifier.height(6.dp))

                        GameDifficulty.values().forEach { diff ->
                            val isSelected = selectedDifficulty == diff
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        if (isSelected) SpaceSurface else SpaceCard,
                                        RoundedCornerShape(10.dp)
                                    )
                                    .border(
                                        1.dp,
                                        if (isSelected) NeonGlamPink else Color(0xFF334155),
                                        RoundedCornerShape(10.dp)
                                    )
                                    .clickable { selectedDifficulty = diff }
                                    .padding(10.dp)
                            ) {
                                Column {
                                    Text(
                                        diff.title,
                                        color = if (isSelected) MartianGold else Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp
                                    )
                                    Text(diff.description, color = Color(0xFFCBD5E1), fontSize = 11.sp)
                                }
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Starter bonus toggle
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { starterBonus = !starterBonus }
                        ) {
                            Checkbox(
                                checked = starterBonus,
                                onCheckedChange = { starterBonus = it },
                                colors = CheckboxDefaults.colors(checkedColor = NeonGlamPink)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Column {
                                Text("VIP Стартовый Набор", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                Text("Начать с Платиновым Бластером и +150 Золота", color = Color(0xFF94A3B8), fontSize = 11.sp)
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Button(
                                onClick = { viewModel.showNewGameDialog(false) },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF334155)),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("ОТМЕНА", fontSize = 12.sp)
                            }

                            Button(
                                onClick = { viewModel.startNewGame(selectedDifficulty, starterBonus) },
                                colors = ButtonDefaults.buttonColors(containerColor = NeonGlamPink),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("СТАРТ!", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}
