package com.example.game

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.ElectricPurple
import com.example.ui.theme.GoldCoin
import com.example.ui.theme.HotPink
import com.example.ui.theme.SpaceDark
import com.example.ui.theme.SurfaceCard

@Composable
fun HubScreen(
    state: GameState,
    onStartRaid: () -> Unit,
    onOpenTalents: () -> Unit,
    onOpenQuests: () -> Unit,
    onOpenEndings: () -> Unit,
    onTalkToNpc: (FriendlyNPC) -> Unit,
    onHitTrainingDummy: (Float) -> Unit,
    onGenerateNogAi: (Boolean) -> Unit,
    onSearchNpc: (String) -> Unit,
    onDismissNogAiResult: () -> Unit,
    onMainMenu: () -> Unit
) {
    val p = state.player
    var dummyHits by remember { mutableIntStateOf(0) }
    var dummyTotalDamage by remember { mutableFloatStateOf(0f) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF1E0A3C),
                        SpaceDark,
                        Color(0xFF160B2E)
                    )
                )
            )
            .testTag("hub_screen")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Top Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "🌟 VIP-ХАБ: ПЕНТХАУС 800 ЭТАЖА",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Black,
                        color = GoldCoin
                    )
                    Text(
                        text = "Главная резиденция Владика на Марсе",
                        fontSize = 11.sp,
                        color = Color(0xFF94A3B8)
                    )
                }

                OutlinedButton(
                    onClick = onMainMenu,
                    modifier = Modifier.testTag("hub_to_menu_btn"),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text("Меню", fontSize = 12.sp)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Resource Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SurfaceCard, RoundedCornerShape(12.dp))
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("💎", fontSize = 14.sp)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("${p.diamonds}", fontWeight = FontWeight.Bold, color = CyberCyan, fontSize = 13.sp)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("💰", fontSize = 14.sp)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("${p.gold}", fontWeight = FontWeight.Bold, color = GoldCoin, fontSize = 13.sp)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("🔋", fontSize = 14.sp)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("${p.batteries}", fontWeight = FontWeight.Bold, color = Color(0xFF10B981), fontSize = 13.sp)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("💀", fontSize = 14.sp)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("${p.kills} Kills", fontWeight = FontWeight.Bold, color = HotPink, fontSize = 13.sp)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Scrollable Hub Stations
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // nOG AI Generator Station
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, CyberCyan.copy(alpha = 0.6f), RoundedCornerShape(14.dp)),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = SurfaceCard)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("🤖", fontSize = 22.sp)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text(
                                            text = "nOG AI: Нейро-Генератор Забега",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp,
                                            color = CyberCyan
                                        )
                                        val hourBlock = (System.currentTimeMillis() / (5 * 3600 * 1000)).toInt()
                                        val dynSeed = kotlin.math.abs(hourBlock * 73856093)
                                        val dynG = 30 + (dynSeed % 40)
                                        val dynD = 3 + (dynSeed % 5)
                                        Text(
                                            text = "Динамическая цена (каждые 5ч): $dynG 🪙 + $dynD 💎",
                                            fontSize = 11.sp,
                                            color = Color(0xFF94A3B8)
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Button(
                                    onClick = { onGenerateNogAi(true) },
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(34.dp)
                                        .testTag("nog_ai_weapon_btn"),
                                    shape = RoundedCornerShape(8.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = CyberCyan)
                                ) {
                                    Text("Оружие nOG AI", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                                }
                                Button(
                                    onClick = { onGenerateNogAi(false) },
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(34.dp)
                                        .testTag("nog_ai_artifact_btn"),
                                    shape = RoundedCornerShape(8.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = HotPink)
                                ) {
                                    Text("Артефакт nOG AI", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }

                // Julien Station
                item {
                    val julien = state.npcs.find { it.id == "npc_julien" }
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, Color(0xFFE040FB).copy(alpha = 0.5f), RoundedCornerShape(14.dp)),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = SurfaceCard)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(46.dp)
                                    .background(Color(0xFFE040FB).copy(alpha = 0.25f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("🇫🇷", fontSize = 22.sp)
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Жульен «Французский Люкс»",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = Color(0xFFE040FB)
                                )
                                Text(
                                    text = "Бодигард, французский шансон-трэп и охота",
                                    fontSize = 11.sp,
                                    color = Color(0xFF94A3B8)
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    if (julien != null) {
                                        Button(
                                            onClick = { onTalkToNpc(julien) },
                                            modifier = Modifier
                                                .height(30.dp)
                                                .testTag("talk_julien_btn"),
                                            shape = RoundedCornerShape(6.dp),
                                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE040FB)),
                                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 0.dp)
                                        ) {
                                            Text("Поговорить", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                    Button(
                                        onClick = onOpenQuests,
                                        modifier = Modifier
                                            .height(30.dp)
                                            .testTag("quests_julien_btn"),
                                        shape = RoundedCornerShape(6.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = CyberCyan),
                                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 0.dp)
                                    ) {
                                        Text("Контракты", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                                    }
                                }
                            }
                        }
                    }
                }

                // Batya Station
                item {
                    val batya = state.npcs.find { it.id == "npc_batya" }
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, Color(0xFFFF9100).copy(alpha = 0.5f), RoundedCornerShape(14.dp)),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = SurfaceCard)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(46.dp)
                                    .background(Color(0xFFFF9100).copy(alpha = 0.25f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("🏎️", fontSize = 22.sp)
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Батя «Титановый Поршень»",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = Color(0xFFFF9100)
                                )
                                Text(
                                    text = "Гараж 6969 гиперкаров, байки и кузница талантов",
                                    fontSize = 11.sp,
                                    color = Color(0xFF94A3B8)
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    if (batya != null) {
                                        Button(
                                            onClick = { onTalkToNpc(batya) },
                                            modifier = Modifier
                                                .height(30.dp)
                                                .testTag("talk_batya_btn"),
                                            shape = RoundedCornerShape(6.dp),
                                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9100)),
                                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 0.dp)
                                        ) {
                                            Text("Поговорить", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                    Button(
                                        onClick = onOpenTalents,
                                        modifier = Modifier
                                            .height(30.dp)
                                            .testTag("talents_batya_btn"),
                                        shape = RoundedCornerShape(6.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = GoldCoin),
                                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 0.dp)
                                    ) {
                                        Text("Таланты", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                                    }
                                }
                            }
                        }
                    }
                }

                // Training Range Station (Тир манекенов)
                item {
                    val damageMultiplier = state.talents.getDamageMultiplier()
                    val weaponDamage = p.equippedWeapon.damage * damageMultiplier

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, CyberCyan.copy(alpha = 0.4f), RoundedCornerShape(14.dp)),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = SurfaceCard)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("🎯", fontSize = 20.sp)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text(
                                            text = "Тир: Манекен Робота-Цензора",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp,
                                            color = CyberCyan
                                        )
                                        Text(
                                            text = "Оружие: ${p.equippedWeapon.name} (Урон: ${weaponDamage.toInt()})",
                                            fontSize = 11.sp,
                                            color = Color(0xFFCBD5E1)
                                        )
                                    }
                                }
                                Text(
                                    text = "Всего урона: ${dummyTotalDamage.toInt()}",
                                    fontSize = 11.sp,
                                    color = GoldCoin,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Ударов: $dummyHits | Множитель таланта: +${((damageMultiplier - 1f) * 100).toInt()}%",
                                    fontSize = 10.sp,
                                    color = Color(0xFF94A3B8)
                                )
                                Button(
                                    onClick = {
                                        dummyHits += 1
                                        dummyTotalDamage += weaponDamage
                                        onHitTrainingDummy(weaponDamage)
                                    },
                                    modifier = Modifier
                                        .height(30.dp)
                                        .testTag("dummy_hit_button"),
                                    shape = RoundedCornerShape(6.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = HotPink),
                                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp)
                                ) {
                                    Text("УДАРИТЬ", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }

                // 100 Endings Archive Shortcut
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, ElectricPurple.copy(alpha = 0.5f), RoundedCornerShape(14.dp)),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = SurfaceCard)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("🏆", fontSize = 20.sp)
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = "Архив 100 Концовок Марса",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = ElectricPurple
                                    )
                                    Text(
                                        text = "Открыто: ${state.unlockedEndings.size} из 100 финалов",
                                        fontSize = 11.sp,
                                        color = Color(0xFFCBD5E1)
                                    )
                                }
                            }
                            Button(
                                onClick = onOpenEndings,
                                modifier = Modifier
                                    .height(32.dp)
                                    .testTag("hub_endings_btn"),
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = ElectricPurple),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp)
                            ) {
                                Text("Обзор", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
                // VIP Lounge & 10,000 Martian Retinue Members
                item {
                    var showAllRetinue by remember { androidx.compose.runtime.mutableStateOf(false) }
                    var searchText by remember { androidx.compose.runtime.mutableStateOf("") }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, GoldCoin.copy(alpha = 0.5f), RoundedCornerShape(14.dp)),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = SurfaceCard)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Марсианская Свита (10,000 Персонажей)",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = GoldCoin
                                    )
                                    Text(
                                        text = "Поиск любого из 10,000 жителей с уникальными диалогами",
                                        fontSize = 11.sp,
                                        color = Color(0xFF94A3B8)
                                    )
                                }
                                Button(
                                    onClick = { showAllRetinue = !showAllRetinue },
                                    modifier = Modifier.height(30.dp),
                                    shape = RoundedCornerShape(6.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = CyberCyan),
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp)
                                ) {
                                    Text(if (showAllRetinue) "Скрыть" else "Список 10к", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                                }
                            }

                            if (showAllRetinue) {
                                Spacer(modifier = Modifier.height(10.dp))

                                // Quick Search / Index Jump
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    androidx.compose.material3.OutlinedTextField(
                                        value = searchText,
                                        onValueChange = {
                                            searchText = it
                                            onSearchNpc(it)
                                        },
                                        placeholder = { Text("Имя, роль или номер (1-10000)...", fontSize = 11.sp, color = Color.Gray) },
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(48.dp),
                                        singleLine = true,
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.height(10.dp))
                                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    state.npcs.take(30).forEach { npc ->
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .background(Color(0xFF1E1535), RoundedCornerShape(8.dp))
                                                .padding(horizontal = 10.dp, vertical = 6.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Column(modifier = Modifier.weight(1f)) {
                                                Text(npc.name, color = npc.color, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                                Text(npc.role, color = Color(0xFF94A3B8), fontSize = 10.sp)
                                            }
                                            Button(
                                                onClick = { onTalkToNpc(npc) },
                                                modifier = Modifier.height(26.dp),
                                                shape = RoundedCornerShape(6.dp),
                                                colors = ButtonDefaults.buttonColors(containerColor = npc.color),
                                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp)
                                            ) {
                                                Text("Чат", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Main Raid Start Button
            Button(
                onClick = onStartRaid,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .testTag("start_raid_elevator_button"),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = GoldCoin)
            ) {
                Text(
                    text = "🚀 ЛИФТ НА ЭТАЖИ: НАЧАТЬ РЕЙД",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.Black
                )
            }
        }

        // Animated nOG AI Loading Screen
        if (state.isGeneratingNogAi) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xEE090514)),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(24.dp)
                ) {
                    Text("🤖", fontSize = 54.sp)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "nOG AI СОЗДАЕТ АРТЕФАКТ ДЛЯ ВЛАДА...",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Black,
                        color = CyberCyan,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    androidx.compose.material3.LinearProgressIndicator(
                        progress = state.nogAiProgress,
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .height(10.dp)
                            .clip(RoundedCornerShape(5.dp)),
                        color = HotPink,
                        trackColor = Color(0xFF334155)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "${(state.nogAiProgress * 100).toInt()}% • Нейро-синтез 808 басов",
                        fontSize = 12.sp,
                        color = Color.White
                    )
                }
            }
        }

        // Animated nOG AI Result Modal
        if (state.generatedNogAiWeapon != null || state.generatedNogAiArtifact != null) {
            val itemWeapon = state.generatedNogAiWeapon
            val itemArtifact = state.generatedNogAiArtifact
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xDD000000)),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .border(2.dp, GoldCoin, RoundedCornerShape(16.dp)),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = SpaceDark)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("🎉 СИНТЕЗ nOG AI ЗАВЕРШЕН! 🎉", color = GoldCoin, fontWeight = FontWeight.Black, fontSize = 16.sp)
                        Spacer(modifier = Modifier.height(12.dp))

                        if (itemWeapon != null) {
                            Text(itemWeapon.name, color = itemWeapon.color, fontSize = 18.sp, fontWeight = FontWeight.Black)
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(itemWeapon.description, color = Color.White, fontSize = 12.sp, textAlign = TextAlign.Center)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("⚡ УРОН: ${itemWeapon.damage.toInt()} | ЛЕГЕНДАРНЫЙ КЛАСС", color = CyberCyan, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        } else if (itemArtifact != null) {
                            Text(itemArtifact.name, color = GoldCoin, fontSize = 18.sp, fontWeight = FontWeight.Black)
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(itemArtifact.description, color = Color.White, fontSize = 12.sp, textAlign = TextAlign.Center)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("✦ +${itemArtifact.bonusHp.toInt()} HP | +${itemArtifact.bonusDamage.toInt()} Урона", color = HotPink, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = onDismissNogAiResult,
                            colors = ButtonDefaults.buttonColors(containerColor = GoldCoin),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("ЭКИПИРОВАТЬ И НАЧАТЬ РЕЙД!", color = Color.Black, fontWeight = FontWeight.Black, fontSize = 13.sp)
                        }
                    }
                }
            }
        }

        // Floor Transition Elevator Loading Screen
        if (state.isTransitioningFloor) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF0F081D)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("🛗", fontSize = 60.sp)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "ЛИФТ 800-ГО ЭТАЖА ПОДНИМАЕТСЯ...",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        color = GoldCoin
                    )
                    Text(
                        text = "Этаж ${state.transitionFloorNumber} из 800",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = CyberCyan
                    )
                }
            }
        }
    }
}
