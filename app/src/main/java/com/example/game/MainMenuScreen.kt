package com.example.game

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.GoldCoin
import com.example.ui.theme.HotPink
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
        initialValue = 0.99f,
        targetValue = 1.01f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "title_pulse"
    )

    var selectedDifficulty by remember { mutableStateOf(GameDifficulty.NORMAL) }
    var starterBonus by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F041D)),
        contentAlignment = Alignment.Center
    ) {
        // High-res luxury baroque artwork full bleed background
        Image(
            painter = painterResource(id = R.drawable.bg_main_menu),
            contentDescription = "Mars World Luxury Menu Artwork",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Subtle gradient vignette to blend buttons with the baroque background
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color(0x22000000),
                            Color(0x44000000),
                            Color(0x990A0217)
                        )
                    )
                )
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(180.dp)) // Space for top logo artwork in background

            // Menu Buttons Container
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.92f)
                    .scale(titlePulse),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 1. Continue Saved Game
                BaroqueMenuButton(
                    onClick = { viewModel.continueSavedGame() },
                    enabled = state.hasSavedGame,
                    text = if (state.hasSavedGame) "▶ ПРОДОЛЖИТЬ ИГРУ" else "НЕТ СОХРАНЕНИЙ",
                    testTag = "continue_game_button",
                    bgBrush = if (state.hasSavedGame) {
                        Brush.horizontalGradient(listOf(Color(0xFF854D0E), Color(0xFFCA8A04), Color(0xFF854D0E)))
                    } else {
                        Brush.horizontalGradient(listOf(Color(0xFF1E293B), Color(0xFF334155), Color(0xFF1E293B)))
                    },
                    borderColor = if (state.hasSavedGame) MartianGold else Color(0xFF475569),
                    textColor = if (state.hasSavedGame) Color.White else Color(0xFF94A3B8)
                )

                // 2. New Game
                BaroqueMenuButton(
                    onClick = { viewModel.showNewGameDialog(true) },
                    enabled = true,
                    text = "✦ НОВАЯ ИГРА ✦",
                    testTag = "new_game_button",
                    bgBrush = Brush.horizontalGradient(
                        listOf(Color(0xFF9D174D), Color(0xFFDB2777), Color(0xFFBE185D))
                    ),
                    borderColor = MartianGold,
                    textColor = Color.White
                )

                // 3. 1111 Endings Archive
                BaroqueMenuButton(
                    onClick = { viewModel.openEndingsArchive() },
                    enabled = true,
                    text = "АРХИВ 1111 КОНЦОВОК",
                    badge = "${state.unlockedEndings.size}/1111",
                    testTag = "endings_archive_button",
                    bgBrush = Brush.horizontalGradient(
                        listOf(Color(0xFF4C1D95), Color(0xFF7C3AED), Color(0xFF4338CA), Color(0xFF0284C7))
                    ),
                    borderColor = MartianGold,
                    textColor = Color.White
                )

                // 4. VIP Hub Entrance
                BaroqueMenuButton(
                    onClick = { viewModel.openHub() },
                    enabled = true,
                    text = "🌟 VIP-ХАБ: ПЕНТХАУС И ТАЛАНТЫ",
                    testTag = "vip_hub_button",
                    bgBrush = Brush.horizontalGradient(
                        listOf(Color(0xFF713F12), Color(0xFFEAB308), Color(0xFFCA8A04), Color(0xFF713F12))
                    ),
                    borderColor = Color(0xFFFFFBEB),
                    textColor = Color.Black
                )

                // 5. Wardrobe Button
                BaroqueMenuButton(
                    onClick = { viewModel.openWardrobe(true) },
                    enabled = true,
                    text = "🥋 ГАРДЕРОБ ВЛАДИКА (СКИНЫ)",
                    badge = state.activeSkin.title,
                    testTag = "wardrobe_menu_button",
                    bgBrush = Brush.horizontalGradient(
                        listOf(Color(0xFF831843), Color(0xFFBE185D), Color(0xFF9D174D))
                    ),
                    borderColor = MartianGold,
                    textColor = Color.White
                )

                // 6. Quick Garage Access Button
                BaroqueMenuButton(
                    onClick = { viewModel.openGarage(true) },
                    enabled = true,
                    text = "🏎️ ГАРАЖ ВЛАДИКА: ХОЛСТ 32x32 & ТЮНИНГ",
                    badge = "${state.carUpgrades.getTotalLevel()}/5000",
                    testTag = "quick_garage_button",
                    bgBrush = Brush.horizontalGradient(
                        listOf(Color(0xFF0F172A), Color(0xFF0E7490), Color(0xFF0F172A))
                    ),
                    borderColor = CyberCyan,
                    textColor = Color.White
                )

                // Rebirth God Vlad Button (unlocked upon full maxing of endings & upgrades)
                val hasAllEndings = state.unlockedEndings.size >= ProceduralWorld.all100Endings.size || state.unlockedEndings.size >= 1111
                val isFullyMaxedOut = hasAllEndings && (state.talents.getTotalTalentLevel() >= 50 || state.carUpgrades.getTotalLevel() >= 50)
                if (isFullyMaxedOut) {
                    BaroqueMenuButton(
                        onClick = { viewModel.birthGodVlad() },
                        enabled = true,
                        text = "👑 РОЖДЕНИЕ ГЛАВНОГО ВЛАДА 👑",
                        testTag = "god_vlad_rebirth_button",
                        bgBrush = Brush.horizontalGradient(
                            listOf(Color(0xFFFF0055), Color(0xFFFFD700), Color(0xFF00E5FF))
                        ),
                        borderColor = Color.White,
                        textColor = Color.Black
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Studio signature
            Text(
                text = "PRIDE GAYS STUDIO • 2026 EDITION",
                color = Color(0xFFCBD5E1),
                fontSize = 10.sp,
                letterSpacing = 2.sp,
                fontWeight = FontWeight.Bold
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

        // Garage Modal if opened
        if (state.isGarageOpen) {
            GarageModal(
                state = state,
                viewModel = viewModel,
                onClose = { viewModel.openGarage(false) }
            )
        }

        // Wardrobe Modal if opened
        if (state.isWardrobeOpen) {
            WardrobeModal(
                state = state,
                viewModel = viewModel,
                onClose = { viewModel.openWardrobe(false) }
            )
        }
    }
}

@Composable
private fun BaroqueMenuButton(
    onClick: () -> Unit,
    enabled: Boolean,
    text: String,
    badge: String? = null,
    testTag: String,
    bgBrush: Brush,
    borderColor: Color,
    textColor: Color
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .background(
                brush = bgBrush,
                shape = RoundedCornerShape(12.dp)
            )
            .border(
                width = 2.dp,
                color = borderColor,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(enabled = enabled, onClick = onClick)
            .padding(horizontal = 16.dp)
            .testTag(testTag),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = text,
                color = textColor,
                fontSize = 13.sp,
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily.Monospace,
                letterSpacing = 1.sp
            )

            if (badge != null) {
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .background(MartianGold, RoundedCornerShape(6.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = badge,
                        color = Color.Black,
                        fontWeight = FontWeight.Black,
                        fontSize = 11.sp
                    )
                }
            }
        }
    }
}
