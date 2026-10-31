package com.example.game

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
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
import kotlin.math.sqrt
import kotlin.random.Random

@Composable
fun GameWorldScreen(
    viewModel: GameViewModel
) {
    val state by viewModel.state.collectAsState()

    // 60 FPS update loop
    LaunchedEffect(Unit) {
        var lastTimeNanos = System.nanoTime()
        while (true) {
            withFrameNanos { nowNanos ->
                val dt = ((nowNanos - lastTimeNanos) / 1_000_000_000.0).toFloat().coerceIn(0.001f, 0.05f)
                lastTimeNanos = nowNanos
                viewModel.updateGame(dt)
            }
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "game_hud")
    val ultimateGlow by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "ultimate_glow"
    )

    BoxWithConstraints(modifier = Modifier.fillMaxSize().background(SpaceDark)) {
        val screenW = constraints.maxWidth.toFloat()
        val screenH = constraints.maxHeight.toFloat()
        val p = state.player

        val shakeX = if (state.screenShake > 0f) (Random.nextFloat() - 0.5f) * state.screenShake else 0f
        val shakeY = if (state.screenShake > 0f) (Random.nextFloat() - 0.5f) * state.screenShake else 0f

        val cameraCenterX = screenW / 2f + shakeX
        val cameraCenterY = screenH / 2f + shakeY

        val tileSize = ProceduralWorld.TILE_SIZE

        // Main 2D Pixel Canvas
        Canvas(modifier = Modifier.fillMaxSize()) {
            val timeSec = System.currentTimeMillis() / 1000f

            // 1. Draw Visible World Tiles
            val minTx = ((p.x - screenW / 2f) / tileSize).toInt() - 1
            val maxTx = ((p.x + screenW / 2f) / tileSize).toInt() + 1
            val minTy = ((p.y - screenH / 2f) / tileSize).toInt() - 1
            val maxTy = ((p.y + screenH / 2f) / tileSize).toInt() + 1

            for (tx in minTx..maxTx) {
                for (ty in minTy..maxTy) {
                    val tile = ProceduralWorld.getTile(tx, ty)
                    val worldX = tx * tileSize
                    val worldY = ty * tileSize
                    val screenTileX = cameraCenterX + (worldX - p.x)
                    val screenTileY = cameraCenterY + (worldY - p.y)

                    PixelRenderer.drawWorldTile(
                        drawScope = this,
                        tile = tile,
                        screenX = screenTileX,
                        screenY = screenTileY,
                        size = tileSize,
                        timeSec = timeSec
                    )
                }
            }

            // 2. Draw World Artifacts on Pedestals
            state.worldArtifacts.forEach { worldArt ->
                val sx = cameraCenterX + (worldArt.x - p.x)
                val sy = cameraCenterY + (worldArt.y - p.y)
                PixelRenderer.drawWorldArtifact(this, worldArt, sx, sy, timeSec)
            }

            // 3. Draw Friendly NPCs (Marcus & DJ Malik)
            state.npcs.forEach { npc ->
                val sx = cameraCenterX + (npc.x - p.x)
                val sy = cameraCenterY + (npc.y - p.y)
                PixelRenderer.drawFriendlyNPC(this, npc, sx, sy, timeSec)
            }

            // 4. Draw Dropped Items
            state.droppedItems.forEach { item ->
                val sx = cameraCenterX + (item.x - p.x)
                val sy = cameraCenterY + (item.y - p.y)
                PixelRenderer.drawDroppedItem(this, item, sx, sy, timeSec)
            }

            // 5. Draw Hypercars
            state.hypercars.forEach { car ->
                val sx = cameraCenterX + (car.x - p.x)
                val sy = cameraCenterY + (car.y - p.y)
                PixelRenderer.drawHypercar(this, car, sx, sy)
            }

            // 6. Draw VlaDick
            PixelRenderer.drawVlaDick(
                drawScope = this,
                player = p,
                screenX = cameraCenterX,
                screenY = cameraCenterY,
                timeSec = timeSec
            )

            // 7. Draw Enemies
            state.enemies.forEach { enemy ->
                val sx = cameraCenterX + (enemy.x - p.x)
                val sy = cameraCenterY + (enemy.y - p.y)
                PixelRenderer.drawEnemy(this, enemy, sx, sy, timeSec)
            }

            // 8. Draw Projectiles
            state.projectiles.forEach { proj ->
                val sx = cameraCenterX + (proj.x - p.x)
                val sy = cameraCenterY + (proj.y - p.y)
                drawCircle(proj.color, proj.radius, Offset(sx, sy))
                drawCircle(Color.White, proj.radius * 0.4f, Offset(sx, sy))
            }

            // 9. Draw Particles
            state.particles.forEach { part ->
                val sx = cameraCenterX + (part.x - p.x)
                val sy = cameraCenterY + (part.y - p.y)
                val alpha = (part.lifetime / part.maxLifetime).coerceIn(0f, 1f)
                drawCircle(part.color.copy(alpha = alpha), part.size, Offset(sx, sy))
            }

            // 10. Ambient Day/Night Lighting Filter
            PixelRenderer.drawAmbientLighting(
                drawScope = this,
                timeOfDay = state.timeOfDay,
                playerScreenX = cameraCenterX,
                playerScreenY = cameraCenterY,
                canvasWidth = screenW,
                canvasHeight = screenH
            )

            // 11. Damage popups
            drawIntoCanvas { canvas ->
                val paint = android.graphics.Paint().apply {
                    textSize = 32f
                    isFakeBoldText = true
                    textAlign = android.graphics.Paint.Align.CENTER
                }
                state.damagePopups.forEach { popup ->
                    val sx = cameraCenterX + (popup.x - p.x)
                    val sy = cameraCenterY + (popup.y - p.y)
                    paint.color = android.graphics.Color.argb(
                        (popup.lifetime * 255).toInt().coerceIn(0, 255),
                        (popup.color.red * 255).toInt(),
                        (popup.color.green * 255).toInt(),
                        (popup.color.blue * 255).toInt()
                    )
                    canvas.nativeCanvas.drawText(popup.text, sx, sy, paint)
                }
            }
        }

        // Detect nearby NPC or Artifact
        val nearbyNPC = state.npcs.firstOrNull { npc ->
            val dx = p.x - npc.x
            val dy = p.y - npc.y
            sqrt(dx * dx + dy * dy) < 85f
        }
        val nearbyArtifact = state.worldArtifacts.firstOrNull { art ->
            !art.isCollected && run {
                val dx = p.x - art.x
                val dy = p.y - art.y
                sqrt(dx * dx + dy * dy) < 75f
            }
        }

        // --- HUD OVERLAY ---
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 14.dp, vertical = 8.dp)
        ) {
            // Top Status Bar: Floor, Time, Biome, Menu and Crafting buttons
            val currentBiome = ProceduralWorld.getBiomeForPosition(p.x, p.y)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xDD13092A), RoundedCornerShape(12.dp))
                    .border(1.dp, Color(0x66FF007F), RoundedCornerShape(12.dp))
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "ЭТАЖ ${p.floor} / 800",
                        color = MartianGold,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace
                    )
                    Text(
                        text = currentBiome.displayName,
                        color = Color(0xFFE2E8F0),
                        fontSize = 10.sp
                    )
                }

                // Day / Night badge
                Box(
                    modifier = Modifier
                        .background(
                            when (state.timeOfDay) {
                                TimeOfDay.DAY -> Color(0x66FFA500)
                                TimeOfDay.SUNSET -> Color(0x66D946EF)
                                TimeOfDay.NIGHT -> Color(0x663B82F6)
                            },
                            RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = state.timeOfDay.displayName,
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    // Inventory button
                    Box(
                        modifier = Modifier
                            .background(NeonPurple, RoundedCornerShape(8.dp))
                            .clickable { viewModel.openInventory() }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                            .testTag("open_crafting_button")
                    ) {
                        Text("ВЕРСТАК ⚒", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                    }

                    // VIP Hub button
                    Box(
                        modifier = Modifier
                            .background(Color(0xFF5B21B6), RoundedCornerShape(8.dp))
                            .border(1.dp, MartianGold, RoundedCornerShape(8.dp))
                            .clickable {
                                viewModel.saveCurrentGame()
                                viewModel.openHub()
                            }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                            .testTag("world_to_hub_button")
                    ) {
                        Text("ХАБ 🌟", color = MartianGold, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                    }

                    // Main Menu / Save button
                    Box(
                        modifier = Modifier
                            .background(Color(0xFF334155), RoundedCornerShape(8.dp))
                            .clickable {
                                viewModel.saveCurrentGame()
                                viewModel.splashToMainMenu()
                            }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                            .testTag("to_main_menu_button")
                    ) {
                        Text("МЕНЮ ⌂", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Health & Hype Bars + Resources
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    var effectiveMaxHp = p.maxHp
                    p.equippedArtifacts.forEach { effectiveMaxHp += it.bonusHp }
                    val hpRatio = (p.hp / effectiveMaxHp).coerceIn(0f, 1f)

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("❤️", fontSize = 11.sp)
                        Spacer(modifier = Modifier.width(4.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.85f)
                                .height(12.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0x88000000))
                                .border(1.dp, Color(0xFFEF4444), RoundedCornerShape(6.dp))
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(hpRatio)
                                    .height(12.dp)
                                    .background(
                                        Brush.horizontalGradient(
                                            listOf(Color(0xFFDC2626), Color(0xFFFF0055))
                                        )
                                    )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    val hypeRatio = (p.hypeEnergy / 100f).coerceIn(0f, 1f)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("🔥", fontSize = 11.sp)
                        Spacer(modifier = Modifier.width(4.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.85f)
                                .height(10.dp)
                                .clip(RoundedCornerShape(5.dp))
                                .background(Color(0x88000000))
                                .border(1.dp, MartianGold, RoundedCornerShape(5.dp))
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(hypeRatio)
                                    .height(10.dp)
                                    .background(
                                        Brush.horizontalGradient(
                                            listOf(Color(0xFFFF007F), Color(0xFFFFD700), Color(0xFF00F0FF))
                                        )
                                    )
                            )
                        }
                    }
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text("🪙 ${p.gold}", color = MartianGold, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    Text("💎 ${p.diamonds}", color = CyberCyan, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    Text("⚡ ${p.batteries}", color = Color(0xFF10B981), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            }

            // VlaDick Speech Bubble
            if (state.showSpeechBubble) {
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xEE1E0A3C), RoundedCornerShape(12.dp))
                        .border(2.dp, MartianGold, RoundedCornerShape(12.dp))
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("👑", fontSize = 20.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                "ВЛАДИК (РЭПЕР №1 ВСЕЛЕННОЙ):",
                                color = MartianGold,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Black
                            )
                            Text(
                                state.currentPunchline,
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Contextual Interaction Button if near NPC or World Artifact
            if (nearbyNPC != null) {
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .background(Color(0xFF8B5CF6), RoundedCornerShape(12.dp))
                        .border(2.dp, MartianGold, RoundedCornerShape(12.dp))
                        .clickable { viewModel.interactWithNearest() }
                        .padding(horizontal = 20.dp, vertical = 10.dp)
                        .testTag("talk_npc_button")
                ) {
                    Text(
                        "💬 ПОГОВОРИТЬ: ${nearbyNPC.name}",
                        color = Color.White,
                        fontWeight = FontWeight.Black,
                        fontSize = 13.sp
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
            } else if (nearbyArtifact != null) {
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .background(Color(0xFF00E5FF), RoundedCornerShape(12.dp))
                        .border(2.dp, Color.White, RoundedCornerShape(12.dp))
                        .clickable { viewModel.interactWithNearest() }
                        .padding(horizontal = 20.dp, vertical = 10.dp)
                        .testTag("inspect_artifact_button")
                ) {
                    Text(
                        "✦ ОСМОТРЕТЬ РЕЛИКИЮ: ${nearbyArtifact.artifact.name}",
                        color = Color.Black,
                        fontWeight = FontWeight.Black,
                        fontSize = 13.sp
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
            }

            // Next Floor / Spawn Boss shortcut button if enemies cleared
            if (!state.bossActive && state.enemies.isEmpty()) {
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .background(MartianGold, RoundedCornerShape(12.dp))
                        .clickable { viewModel.nextFloor() }
                        .padding(horizontal = 24.dp, vertical = 10.dp)
                        .testTag("next_floor_button")
                ) {
                    Text(
                        "🎉 ПОДНЯТЬСЯ НА ЭТАЖ ${p.floor + 1} ➔",
                        color = Color.Black,
                        fontWeight = FontWeight.Black,
                        fontSize = 14.sp
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
            } else if (!state.bossActive && state.enemies.size < 5) {
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .background(Color(0xFF7C3AED), RoundedCornerShape(10.dp))
                        .clickable { viewModel.spawnBoss() }
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .testTag("call_boss_button")
                ) {
                    Text("⚠️ ВЫЗВАТЬ БОССА ЭТАЖА", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
                Spacer(modifier = Modifier.height(10.dp))
            }

            // Bottom Controls: Joystick on Left, Actions on Right
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                // Left: Virtual Joystick
                VirtualJoystick(
                    modifier = Modifier.size(130.dp),
                    onMove = { dx, dy -> viewModel.setJoystickInput(dx, dy) }
                )

                // Right: Action buttons
                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        // Dash Button
                        val dashCooldown = p.dashCooldown
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(if (dashCooldown <= 0f) Color(0xFF3B82F6) else Color(0xFF334155))
                                .border(2.dp, Color.White, CircleShape)
                                .clickable(enabled = dashCooldown <= 0f) { viewModel.performDash() }
                                .testTag("dash_button"),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                if (dashCooldown <= 0f) "РЫВОК\n⚡" else "${String.format("%.1f", dashCooldown)}s",
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                        }

                        // Ultimate 6969 Hypercar Button
                        val isUltimateReady = p.hypeEnergy >= 100f
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .scale(if (isUltimateReady) ultimateGlow else 1f)
                                .clip(CircleShape)
                                .then(
                                    if (isUltimateReady) {
                                        Modifier.background(Brush.radialGradient(listOf(Color(0xFFFF007F), Color(0xFFFFD700))))
                                    } else {
                                        Modifier.background(Color(0xFF1E293B))
                                    }
                                )
                                .border(
                                    2.dp,
                                    if (isUltimateReady) MartianGold else Color(0xFF475569),
                                    CircleShape
                                )
                                .clickable(enabled = isUltimateReady) { viewModel.triggerHypercarUltimate() }
                                .testTag("ultimate_button"),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                if (isUltimateReady) "6969\n🏎️" else "${p.hypeEnergy.toInt()}%",
                                color = if (isUltimateReady) Color.White else Color(0xFF94A3B8),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Black,
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    // Main Attack Button
                    Box(
                        modifier = Modifier
                            .size(76.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    listOf(p.equippedWeapon.color, Color(0xFF880044))
                                )
                            )
                            .border(3.dp, Color.White, CircleShape)
                            .clickable { viewModel.performAttack() }
                            .testTag("attack_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("УДАР", color = Color.White, fontWeight = FontWeight.Black, fontSize = 14.sp)
                            Text("💥", fontSize = 14.sp)
                        }
                    }
                }
            }
        }

        // Dialogue Modal
        if (state.screen == GameScreen.DIALOGUE && state.activeDialogueNPC != null) {
            DialogueModal(
                npc = state.activeDialogueNPC!!,
                nodeIndex = state.currentDialogueNodeIndex,
                onSelectOption = { option -> viewModel.selectDialogueOption(option) },
                onClose = { viewModel.closeDialogue() }
            )
        }

        // Artifact Inspect Modal
        if (state.screen == GameScreen.ARTIFACT_INSPECT && state.inspectingArtifact != null) {
            ArtifactInspectModal(
                worldArtifact = state.inspectingArtifact!!,
                onCollect = { worldArt -> viewModel.collectWorldArtifact(worldArt) },
                onClose = { viewModel.closeArtifactInspect() }
            )
        }

        // Inventory / Crafting Modal
        if (state.screen == GameScreen.INVENTORY) {
            CraftingModal(
                player = p,
                recipes = state.availableRecipes,
                onCraft = { recipe -> viewModel.craftRecipe(recipe) },
                onEquipWeapon = { weapon -> viewModel.equipWeapon(weapon) },
                onClose = { viewModel.closeInventory() }
            )
        }

        // Game Over Screen
        if (state.screen == GameScreen.GAME_OVER) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xDD000000)),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(24.dp)
                ) {
                    Text(
                        "ВЛАДИК ВЫБИТ С МАРСА!",
                        color = Color(0xFFEF4444),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        "Пройдено этажей: ${p.floor} из 800\nПобеждено завистников: ${p.kills}\nСобрано золота: ${p.gold}",
                        color = Color.White,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        lineHeight = 22.sp
                    )
                    Spacer(modifier = Modifier.height(28.dp))
                    Button(
                        onClick = { viewModel.restartGame() },
                        colors = ButtonDefaults.buttonColors(containerColor = NeonGlamPink),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.testTag("restart_button")
                    ) {
                        Text("ВОЗРОДИТЬСЯ И ОТОМСТИТЬ", fontWeight = FontWeight.Bold, fontSize = 14.sp)
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
                    Text("🛗", fontSize = 64.sp)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "ЛИФТ 800-ГО ЭТАЖА ПОДНИМАЕТСЯ...",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        color = MartianGold
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
