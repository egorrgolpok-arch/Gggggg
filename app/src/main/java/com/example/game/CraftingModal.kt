package com.example.game

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.SpaceCard
import com.example.ui.theme.SpaceDark
import com.example.ui.theme.SpaceSurface

@Composable
fun CraftingModal(
    player: Player,
    recipes: List<CraftingRecipe>,
    onCraft: (CraftingRecipe) -> Unit,
    onEquipWeapon: (Weapon) -> Unit,
    onClose: () -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xCC000000))
            .clickable { onClose() },
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .fillMaxHeight(0.85f)
                .background(SpaceDark, RoundedCornerShape(20.dp))
                .border(2.dp, NeonGlamPink, RoundedCornerShape(20.dp))
                .clickable(enabled = false) {}
                .padding(18.dp)
                .testTag("crafting_modal_content")
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
                            text = "ВЕРСТАК ВЛАДИКА",
                            color = MartianGold,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black,
                            fontFamily = FontFamily.Monospace
                        )
                        Text(
                            text = "Синтез оружия и артефактов Марса",
                            color = Color(0xFF94A3B8),
                            fontSize = 11.sp
                        )
                    }

                    Box(
                        modifier = Modifier
                            .background(Color(0xFF334155), RoundedCornerShape(8.dp))
                            .clickable { onClose() }
                            .padding(horizontal = 14.dp, vertical = 6.dp)
                            .testTag("close_crafting_button")
                    ) {
                        Text("✕ ЗАКРЫТЬ", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Resources Bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(SpaceCard, RoundedCornerShape(10.dp))
                        .padding(horizontal = 14.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Text("🪙 ${player.gold} Золота", color = MartianGold, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Text("💎 ${player.diamonds} Брилл.", color = CyberCyan, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Text("⚡ ${player.batteries} Батарей", color = Color(0xFF10B981), fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Tabs
                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = SpaceCard,
                    contentColor = NeonGlamPink,
                    indicator = { tabPositions ->
                        TabRowDefaults.Indicator(
                            Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                            color = NeonGlamPink
                        )
                    }
                ) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        text = { Text("РЕЦЕПТЫ КРАФТА", fontWeight = FontWeight.Bold, fontSize = 12.sp) }
                    )
                    Tab(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        text = { Text("ИНВЕНТАРЬ И БЛИНГ", fontWeight = FontWeight.Bold, fontSize = 12.sp) }
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                if (selectedTab == 0) {
                    // Recipes list
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(recipes) { recipe ->
                            val canCraft = player.gold >= recipe.reqGold &&
                                    player.diamonds >= recipe.reqDiamonds &&
                                    player.batteries >= recipe.reqBatteries

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(SpaceSurface, RoundedCornerShape(12.dp))
                                    .border(
                                        1.dp,
                                        if (canCraft) NeonGlamPink else Color(0xFF334155),
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
                                            text = recipe.name,
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 15.sp
                                        )
                                        Text(
                                            text = recipe.producesWeapon?.rarity?.title ?: recipe.producesArtifact?.rarity?.title ?: "Редкий",
                                            color = recipe.producesWeapon?.rarity?.color ?: recipe.producesArtifact?.rarity?.color ?: MartianGold,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(recipe.description, color = Color(0xFFCBD5E1), fontSize = 12.sp)

                                    Spacer(modifier = Modifier.height(8.dp))

                                    // Cost & Craft button
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "🪙 ${recipe.reqGold}  💎 ${recipe.reqDiamonds}  ⚡ ${recipe.reqBatteries}",
                                            color = Color(0xFF94A3B8),
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.SemiBold
                                        )

                                        Button(
                                            onClick = { onCraft(recipe) },
                                            enabled = canCraft,
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = NeonGlamPink,
                                                disabledContainerColor = Color(0xFF334155)
                                            ),
                                            shape = RoundedCornerShape(8.dp),
                                            modifier = Modifier.testTag("craft_button_${recipe.id}")
                                        ) {
                                            Text(
                                                if (canCraft) "СИНТЕЗ" else "МАЛО РЕСУРСОВ",
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    // Inventory list
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        item {
                            Text(
                                "ЭКИПИРОВАННОЕ ОРУЖИЕ",
                                color = MartianGold,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }

                        items(player.inventoryWeapons) { weapon ->
                            val isEquipped = weapon.id == player.equippedWeapon.id
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(SpaceSurface, RoundedCornerShape(12.dp))
                                    .border(
                                        2.dp,
                                        if (isEquipped) MartianGold else Color.Transparent,
                                        RoundedCornerShape(12.dp)
                                    )
                                    .padding(12.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(weapon.name, color = weapon.color, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                        Text(
                                            "Урон: ${weapon.damage.toInt()} | Скорость: ${String.format("%.1f", weapon.attackSpeed)}/с",
                                            color = Color(0xFF94A3B8),
                                            fontSize = 11.sp
                                        )
                                        Text(weapon.description, color = Color(0xFFCBD5E1), fontSize = 11.sp)
                                    }

                                    if (isEquipped) {
                                        Text("В РУКАХ", color = MartianGold, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                    } else {
                                        Button(
                                            onClick = { onEquipWeapon(weapon) },
                                            colors = ButtonDefaults.buttonColors(containerColor = NeonPurple),
                                            shape = RoundedCornerShape(8.dp)
                                        ) {
                                            Text("ВЗЯТЬ", fontSize = 11.sp)
                                        }
                                    }
                                }
                            }
                        }

                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                "АКТИВНЫЙ БЛИНГ И АРТЕФАКТЫ (${player.equippedArtifacts.size})",
                                color = CyberCyan,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }

                        items(player.equippedArtifacts) { artifact ->
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(SpaceSurface, RoundedCornerShape(12.dp))
                                    .padding(12.dp)
                            ) {
                                Column {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(artifact.name, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                        Text(artifact.rarity.title, color = artifact.rarity.color, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    }
                                    Text(artifact.description, color = Color(0xFFCBD5E1), fontSize = 11.sp)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        "+${artifact.bonusHp.toInt()} HP | +${artifact.bonusDamage.toInt()} Урона | +${artifact.bonusSpeed.toInt()} Скорости",
                                        color = CyberCyan,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold
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
