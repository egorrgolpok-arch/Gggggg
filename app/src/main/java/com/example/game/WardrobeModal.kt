package com.example.game

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.HotPink
import com.example.ui.theme.MartianGold
import com.example.ui.theme.SpaceDark
import com.example.ui.theme.SurfaceCard

@Composable
fun WardrobeModal(
    state: GameState,
    viewModel: GameViewModel,
    onClose: () -> Unit
) {
    val unlockedCount = state.unlockedEndings.size
    val activeSkin = state.activeSkin

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xEE080214))
            .clickable(onClick = onClose)
            .testTag("wardrobe_modal"),
        contentAlignment = Alignment.Center
    ) {
        // Luxury full-bleed background artwork for the modal
        Image(
            painter = painterResource(id = R.drawable.bg_main_menu),
            contentDescription = "Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Dark overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xDD0A0217))
        )

        Card(
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .fillMaxSize(0.92f)
                .clickable(enabled = false) {}
                .border(2.dp, MartianGold, RoundedCornerShape(20.dp)),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xEE140A26))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("🥋", fontSize = 24.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "ГАРДЕРОБ ВЛАДИКА",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Black,
                                fontFamily = FontFamily.Monospace,
                                color = MartianGold
                            )
                        }
                        Text(
                            text = "Открыто концовок: $unlockedCount / 1111",
                            fontSize = 12.sp,
                            color = CyberCyan,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .background(Color(0xFF334155), CircleShape)
                            .clickable { onClose() }
                            .testTag("wardrobe_close_btn"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("✕", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Skins list
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(VladSkin.values().toList()) { skin ->
                        val isUnlocked = unlockedCount >= skin.unlockedEndingThreshold
                        val isSelected = activeSkin == skin

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(
                                    2.dp,
                                    if (isSelected) MartianGold else if (isUnlocked) CyberCyan.copy(alpha = 0.6f) else Color(0xFF334155),
                                    RoundedCornerShape(14.dp)
                                ),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected) Color(0xFF2D124D) else SurfaceCard
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Sprite preview image
                                Box(
                                    modifier = Modifier
                                        .size(72.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(Color(0xFF0F041D))
                                        .border(1.dp, MartianGold.copy(alpha = 0.5f), RoundedCornerShape(10.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Image(
                                        painter = painterResource(id = skin.drawableResId),
                                        contentDescription = skin.title,
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Crop
                                    )

                                    if (!isUnlocked) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .background(Color(0xCC000000)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text("🔒", fontSize = 24.sp)
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(skin.iconEmoji, fontSize = 16.sp)
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = skin.title,
                                            fontWeight = FontWeight.Black,
                                            fontSize = 14.sp,
                                            color = if (isUnlocked) Color.White else Color(0xFF94A3B8)
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(2.dp))

                                    Text(
                                        text = skin.description,
                                        fontSize = 11.sp,
                                        color = Color(0xFFCBD5E1),
                                        lineHeight = 14.sp
                                    )

                                    Spacer(modifier = Modifier.height(4.dp))

                                    if (!isUnlocked) {
                                        Text(
                                            text = "Нужно концовок: ${skin.unlockedEndingThreshold} (сейчас $unlockedCount)",
                                            fontSize = 10.sp,
                                            color = HotPink,
                                            fontWeight = FontWeight.Bold
                                        )
                                    } else {
                                        Text(
                                            text = if (isSelected) "ВЫБРАНО" else "РАЗБЛОКИРОВАНО",
                                            fontSize = 10.sp,
                                            color = if (isSelected) MartianGold else Color(0xFF10B981),
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.width(8.dp))

                                if (isUnlocked) {
                                    Button(
                                        onClick = { viewModel.selectVladSkin(skin) },
                                        enabled = !isSelected,
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = if (isSelected) MartianGold else HotPink,
                                            disabledContainerColor = MartianGold
                                        ),
                                        shape = RoundedCornerShape(8.dp),
                                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                                        modifier = Modifier.testTag("select_skin_${skin.name.lowercase()}_btn")
                                    ) {
                                        Text(
                                            text = if (isSelected) "НАДЕТО" else "НАДЕТЬ",
                                            fontWeight = FontWeight.Black,
                                            fontSize = 11.sp,
                                            color = if (isSelected) Color.Black else Color.White
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
