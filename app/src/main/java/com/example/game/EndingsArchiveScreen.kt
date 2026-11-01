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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
    var searchQuery by remember { mutableStateOf("") }

    val filteredEndings = remember(searchQuery, allEndings, unlockedEndingIds) {
        if (searchQuery.isBlank()) {
            allEndings
        } else {
            val q = searchQuery.trim().lowercase()
            allEndings.filter { ending ->
                val isUnlocked = unlockedEndingIds.contains(ending.id)
                ending.id.toString().contains(q) ||
                (isUnlocked && ending.title.lowercase().contains(q)) ||
                (isUnlocked && ending.description.lowercase().contains(q))
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .testTag("endings_archive_screen")
    ) {
        androidx.compose.foundation.Image(
            painter = androidx.compose.ui.res.painterResource(id = com.example.R.drawable.bg_main_menu),
            contentDescription = "Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = androidx.compose.ui.layout.ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xEE0F041D))
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(16.dp)
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
                        text = "ХРОНИКИ 1111 КОНЦОВОК",
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

            Spacer(modifier = Modifier.height(10.dp))

            // Progress Banner
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SpaceSurface, RoundedCornerShape(12.dp))
                    .border(1.dp, NeonGlamPink, RoundedCornerShape(12.dp))
                    .padding(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("ПРОГРЕСС СЮЖЕТА:", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Text("В каждой концовке Владик празднует победу над натуралами!", color = Color(0xFFCBD5E1), fontSize = 10.sp)
                    }

                    Box(
                        modifier = Modifier
                            .background(MartianGold, RoundedCornerShape(8.dp))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            "${unlockedEndingIds.size} / 1111",
                            color = Color.Black,
                            fontWeight = FontWeight.Black,
                            fontSize = 13.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Поиск среди 1111 финалов (номер, босс)...", color = Color(0xFF64748B), fontSize = 12.sp) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = CyberCyan,
                    unfocusedBorderColor = Color(0xFF334155),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedContainerColor = SpaceSurface,
                    unfocusedContainerColor = SpaceSurface
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            // List of 1111 Endings
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filteredEndings) { ending ->
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
                                text = if (isUnlocked) ending.description else "Побеждайте боссов на этажах особняка и прокачивайте таланты Влада в игре для открытия.",
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
}


