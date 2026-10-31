package com.example.game

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

object PixelRenderer {

    fun drawWorldTile(
        drawScope: DrawScope,
        tile: TileType,
        screenX: Float,
        screenY: Float,
        size: Float,
        timeSec: Float
    ) {
        with(drawScope) {
            // Base background
            drawRect(
                color = tile.color,
                topLeft = Offset(screenX, screenY),
                size = Size(size, size)
            )

            when (tile) {
                TileType.PENTHOUSE_MARBLE -> {
                    // Polished checkerboard marble tile with subtle golden specks
                    val isChecker = ((screenX / size).toInt() + (screenY / size).toInt()) % 2 == 0
                    if (isChecker) {
                        drawRect(
                            color = Color(0x22FFFFFF),
                            topLeft = Offset(screenX + 2f, screenY + 2f),
                            size = Size(size - 4f, size - 4f)
                        )
                    }
                    // Shiny reflection line
                    drawLine(
                        color = Color(0x33FFD700),
                        start = Offset(screenX + 8f, screenY + size - 8f),
                        end = Offset(screenX + size - 8f, screenY + 8f),
                        strokeWidth = 1.5f
                    )
                }

                TileType.VELVET_CARPET -> {
                    // Crimson-purple velvet carpet runner with gold fringe
                    drawRect(
                        color = Color(0xFF9F1239),
                        topLeft = Offset(screenX, screenY + 6f),
                        size = Size(size, size - 12f)
                    )
                    // Gold fringe top & bottom
                    drawLine(Color(0xFFFFD700), Offset(screenX, screenY + 6f), Offset(screenX + size, screenY + 6f), 3f)
                    drawLine(Color(0xFFFFD700), Offset(screenX, screenY + size - 6f), Offset(screenX + size, screenY + size - 6f), 3f)
                    // Diamond embroidery in center
                    drawCircle(Color(0xFFFFD700), 4f, Offset(screenX + size / 2f, screenY + size / 2f))
                }

                TileType.MARBLE_PILLAR -> {
                    // 3D shaded white marble column with gold base and capital
                    drawRect(Color(0x33000000), Offset(screenX + 12f, screenY + size - 6f), Size(size - 24f, 8f))
                    // Gold base
                    drawRoundRect(
                        color = Color(0xFFFFD700),
                        topLeft = Offset(screenX + 8f, screenY + size - 16f),
                        size = Size(size - 16f, 12f),
                        cornerRadius = CornerRadius(4f, 4f)
                    )
                    // Marble column body
                    drawRoundRect(
                        color = Color(0xFFF8FAFC),
                        topLeft = Offset(screenX + 14f, screenY + 12f),
                        size = Size(size - 28f, size - 26f),
                        cornerRadius = CornerRadius(3f, 3f)
                    )
                    // Fluting grooves on marble
                    drawLine(Color(0xFFCBD5E1), Offset(screenX + 22f, screenY + 14f), Offset(screenX + 22f, screenY + size - 16f), 2f)
                    drawLine(Color(0xFFCBD5E1), Offset(screenX + size - 22f, screenY + 14f), Offset(screenX + size - 22f, screenY + size - 16f), 2f)
                    // Gold capital
                    drawRoundRect(
                        color = Color(0xFFFFD700),
                        topLeft = Offset(screenX + 6f, screenY + 4f),
                        size = Size(size - 12f, 10f),
                        cornerRadius = CornerRadius(3f, 3f)
                    )
                }

                TileType.LAVA_LAMP -> {
                    // Futuristic giant neon lava lamp cylinder
                    drawOval(Color(0x55000000), Offset(screenX + 14f, screenY + size - 8f), Size(size - 28f, 10f))
                    // Metallic base
                    drawRoundRect(Color(0xFF475569), Offset(screenX + 16f, screenY + size - 14f), Size(size - 32f, 10f), CornerRadius(4f, 4f))
                    // Glass cylinder
                    drawRoundRect(
                        color = Color(0x3310B981),
                        topLeft = Offset(screenX + 18f, screenY + 10f),
                        size = Size(size - 36f, size - 24f),
                        cornerRadius = CornerRadius(8f, 8f)
                    )
                    drawRoundRect(
                        color = Color(0x8800F0FF),
                        topLeft = Offset(screenX + 18f, screenY + 10f),
                        size = Size(size - 36f, size - 24f),
                        cornerRadius = CornerRadius(8f, 8f),
                        style = Stroke(2f)
                    )
                    // Animated floating lava blobs
                    val blob1Y = screenY + 16f + ((sin(timeSec * 2f + screenX) + 1f) * 0.5f) * (size - 44f)
                    val blob2Y = screenY + 16f + ((cos(timeSec * 1.5f + screenY) + 1f) * 0.5f) * (size - 44f)
                    drawCircle(Color(0xFFFF007F), 7f, Offset(screenX + size / 2f - 2f, blob1Y))
                    drawCircle(Color(0xFF00F0FF), 5f, Offset(screenX + size / 2f + 3f, blob2Y))
                    // Metallic cap
                    drawRoundRect(Color(0xFF475569), Offset(screenX + 16f, screenY + 4f), Size(size - 32f, 8f), CornerRadius(3f, 3f))
                }

                TileType.MARTIAN_RED_DUNES -> {
                    // Red Mars dunes with wind ripples
                    val rippleOffset = sin(screenX * 0.05f + screenY * 0.05f) * 4f
                    drawLine(
                        color = Color(0xFFB93815),
                        start = Offset(screenX, screenY + 16f + rippleOffset),
                        end = Offset(screenX + size, screenY + 24f + rippleOffset),
                        strokeWidth = 2f
                    )
                    drawLine(
                        color = Color(0xFF7A1D0B),
                        start = Offset(screenX, screenY + 42f + rippleOffset),
                        end = Offset(screenX + size, screenY + 50f + rippleOffset),
                        strokeWidth = 2f
                    )
                    // Tiny glittering red dust specs
                    drawCircle(Color(0xFFFF7A59), 1.5f, Offset(screenX + 14f, screenY + 18f))
                    drawCircle(Color(0xFFFF7A59), 1.5f, Offset(screenX + 44f, screenY + 36f))
                }

                TileType.CANYON_EDGE -> {
                    // Deep chasm with layered rock strata
                    val path = Path().apply {
                        moveTo(screenX, screenY)
                        lineTo(screenX + size, screenY)
                        lineTo(screenX + size * 0.85f, screenY + size)
                        lineTo(screenX + size * 0.15f, screenY + size)
                        close()
                    }
                    drawPath(path, Color(0xFF260502))
                    drawLine(Color(0xFF8B2500), Offset(screenX, screenY + 4f), Offset(screenX + size, screenY + 4f), 3f)
                }

                TileType.MARTIAN_CRATER -> {
                    drawCircle(Color(0xFF4A1005), size * 0.35f, Offset(screenX + size / 2f, screenY + size / 2f))
                    drawCircle(Color(0xFF260502), size * 0.22f, Offset(screenX + size / 2f, screenY + size / 2f))
                }

                TileType.MARTIAN_ROCK -> {
                    val rockPath = Path().apply {
                        moveTo(screenX + size / 2f, screenY + 12f)
                        lineTo(screenX + size - 14f, screenY + size - 14f)
                        lineTo(screenX + 14f, screenY + size - 14f)
                        close()
                    }
                    drawPath(rockPath, Color(0xFF380C04))
                    drawLine(Color(0xFF7A1D0B), Offset(screenX + size / 2f, screenY + 12f), Offset(screenX + size / 2f, screenY + size - 14f), 2f)
                }

                TileType.DIAMOND_CRYSTAL -> {
                    val crystalPath = Path().apply {
                        moveTo(screenX + size / 2f, screenY + 8f)
                        lineTo(screenX + size - 12f, screenY + size / 2f)
                        lineTo(screenX + size / 2f, screenY + size - 8f)
                        lineTo(screenX + 12f, screenY + size / 2f)
                        close()
                    }
                    drawPath(crystalPath, Color(0xFF00F0FF))
                    drawPath(crystalPath, Color.White, style = Stroke(2f))
                    drawCircle(Color.White, 3f, Offset(screenX + size / 2f, screenY + size / 2f))
                }

                TileType.NEON_DANCEPAD -> {
                    val pulse = (sin(timeSec * 5f + screenX * 0.02f) + 1f) * 0.5f
                    drawRect(
                        color = Color(0xFFFF007F).copy(alpha = 0.35f + pulse * 0.45f),
                        topLeft = Offset(screenX + 4f, screenY + 4f),
                        size = Size(size - 8f, size - 8f)
                    )
                    drawRect(Color(0xFF00F0FF), Offset(screenX + 4f, screenY + 4f), Size(size - 8f, size - 8f), style = Stroke(2f))
                }

                TileType.PARKED_HYPERCAR -> {
                    // Parked luxury hypercar in garage
                    drawOval(Color(0x66000000), Offset(screenX + 6f, screenY + size - 12f), Size(size - 12f, 10f))
                    drawRoundRect(
                        color = Color(0xFFFFD700),
                        topLeft = Offset(screenX + 8f, screenY + 18f),
                        size = Size(size - 16f, size - 36f),
                        cornerRadius = CornerRadius(8f, 8f)
                    )
                    // Neon pink racing stripe
                    drawRect(Color(0xFFFF007F), Offset(screenX + 8f, screenY + size / 2f - 4f), Size(size - 16f, 8f))
                    // Cyan cockpit
                    drawRoundRect(Color(0xFF00F0FF), Offset(screenX + 20f, screenY + 22f), Size(size - 40f, 14f), CornerRadius(4f, 4f))
                }

                TileType.GARAGE_SPEEDSTRIPE -> {
                    drawRect(Color(0xFFFFD700).copy(alpha = 0.5f), Offset(screenX, screenY + size * 0.4f), Size(size, size * 0.2f))
                }

                else -> {
                    drawRect(Color(0x11FFFFFF), Offset(screenX, screenY), Size(size, size), style = Stroke(1f))
                }
            }
        }
    }

    fun drawVlaDick(
        drawScope: DrawScope,
        player: Player,
        screenX: Float,
        screenY: Float,
        timeSec: Float
    ) {
        with(drawScope) {
            // Walking cycle calculations
            val stepPhase = player.walkCycleTimer * 12f
            val isMoving = player.isMoving
            val bobY = if (isMoving) abs(sin(stepPhase)) * 4f else sin(timeSec * 3f) * 1.5f
            val footSway = if (isMoving) sin(stepPhase) * 10f else 0f
            val coatSway = if (isMoving) sin(stepPhase) * 3f else 0f

            val currentY = screenY - bobY

            // 1. Dynamic Shadow underneath
            drawOval(
                color = Color(0x66000000),
                topLeft = Offset(screenX - 26f, screenY + 22f),
                size = Size(52f, 16f)
            )

            // Active Companion Drone/NPC
            player.activeCompanion?.let { comp ->
                val compX = screenX - 36f
                val compY = currentY - 32f + sin(timeSec * 4f) * 6f
                drawCircle(comp.color, 12f, Offset(compX, compY))
                drawCircle(Color.White, 5f, Offset(compX, compY))
                drawLine(comp.color, Offset(screenX, currentY - 20f), Offset(compX, compY), strokeWidth = 2f)
            }

            // God Mode Vlad Aura Ring
            if (player.isGodModeVlad) {
                drawCircle(
                    color = Color(0xFFFFD700).copy(alpha = 0.4f),
                    radius = 54f + sin(timeSec * 8f) * 6f,
                    center = Offset(screenX, currentY)
                )
                drawCircle(
                    color = Color(0xFF00F0FF).copy(alpha = 0.3f),
                    radius = 62f + cos(timeSec * 6f) * 8f,
                    center = Offset(screenX, currentY),
                    style = Stroke(3f)
                )
            }

            // 2. Animated Designer Sneakers & Legs
            // Left foot
            drawRoundRect(
                color = Color.White,
                topLeft = Offset(screenX - 16f + footSway, currentY + 16f),
                size = Size(12f, 10f),
                cornerRadius = CornerRadius(3f, 3f)
            )
            drawRect(Color(0xFFFF007F), Offset(screenX - 16f + footSway, currentY + 22f), Size(12f, 4f)) // Pink sole

            // Right foot
            drawRoundRect(
                color = Color.White,
                topLeft = Offset(screenX + 4f - footSway, currentY + 16f),
                size = Size(12f, 10f),
                cornerRadius = CornerRadius(3f, 3f)
            )
            drawRect(Color(0xFFFF007F), Offset(screenX + 4f - footSway, currentY + 22f), Size(12f, 4f))

            // 3. Luxurious Fluffy White Fur Coat with pink interior
            // Coat body
            drawRoundRect(
                color = Color(0xFFF8FAFC),
                topLeft = Offset(screenX - 22f + coatSway, currentY - 14f),
                size = Size(44f, 34f),
                cornerRadius = CornerRadius(10f, 10f)
            )
            // Fluffy fur texture trim
            drawCircle(Color(0xFFE2E8F0), 6f, Offset(screenX - 22f, currentY))
            drawCircle(Color(0xFFE2E8F0), 6f, Offset(screenX + 22f, currentY))
            // Pink silk lapels inside
            drawRect(
                color = Color(0xFFFF1493),
                topLeft = Offset(screenX - 8f, currentY - 10f),
                size = Size(16f, 28f)
            )

            // 4. Extravagant Rainbow Diamond Pride Necklace
            val rainbowColors = listOf(
                Color(0xFFFF0055), // Red
                Color(0xFFFF7F00), // Orange
                Color(0xFFFFD700), // Yellow
                Color(0xFF10B981), // Green
                Color(0xFF00E5FF), // Cyan
                Color(0xFF6366F1), // Blue
                Color(0xFFD946EF)  // Violet
            )
            val chainRadius = 13f
            for (i in rainbowColors.indices) {
                val angle = i * (Math.PI / 6.0) - Math.PI / 2.0
                val cx = screenX + (cos(angle) * chainRadius).toFloat()
                val cy = currentY - 2f + (sin(angle) * 7f).toFloat()
                drawCircle(color = rainbowColors[i], radius = 3f, center = Offset(cx, cy))
            }
            // Hanging Pride Flag Diamond Medallion
            val medallionY = currentY + 10f + (sin(stepPhase) * 2f)
            drawRoundRect(
                color = Color(0xFFFFD700),
                topLeft = Offset(screenX - 7f, medallionY),
                size = Size(14f, 12f),
                cornerRadius = CornerRadius(3f, 3f)
            )
            drawCircle(Color.White, 3f, Offset(screenX, medallionY + 6f))

            // 5. Head & Facial Details
            drawCircle(
                color = Color(0xFFFFD1A4),
                radius = 16f,
                center = Offset(screenX, currentY - 24f)
            )

            // Cool rap sunglasses (narrow cyber black + cyan sparkle)
            drawRoundRect(
                color = Color(0xFF0F172A),
                topLeft = Offset(screenX - 14f, currentY - 28f),
                size = Size(28f, 9f),
                cornerRadius = CornerRadius(3f, 3f)
            )
            // Cyan laser glare reflection
            val glareX = screenX - 10f + ((sin(timeSec * 4f) + 1f) * 8f)
            drawRect(Color(0xFF00F0FF), Offset(glareX, currentY - 27f), Size(5f, 3f))

            // Smirk with diamond grillz
            drawRect(Color(0xFFB45309), Offset(screenX - 5f, currentY - 16f), Size(10f, 3f))
            drawRect(Color(0xFFFFD700), Offset(screenX - 3f, currentY - 16f), Size(6f, 2f)) // Gold teeth

            // 6. 3D Golden Rap Crown with sparkling rubies
            val crownPath = Path().apply {
                moveTo(screenX - 16f, currentY - 34f)
                lineTo(screenX - 14f, currentY - 48f) // Left spike
                lineTo(screenX - 5f, currentY - 38f)
                lineTo(screenX, currentY - 52f)      // Tall central peak
                lineTo(screenX + 5f, currentY - 38f)
                lineTo(screenX + 14f, currentY - 48f) // Right spike
                lineTo(screenX + 16f, currentY - 34f)
                close()
            }
            drawPath(crownPath, Color(0xFFFFD700))
            drawPath(crownPath, Color(0xFFFFFBEB), style = Stroke(2f))

            // Rubies & Emeralds embedded in crown
            drawCircle(Color(0xFFFF0055), 3f, Offset(screenX, currentY - 42f))
            drawCircle(Color(0xFF10B981), 2.5f, Offset(screenX - 10f, currentY - 38f))
            drawCircle(Color(0xFF10B981), 2.5f, Offset(screenX + 10f, currentY - 38f))

            // 7. Weapon in hand with animated swing
            val weaponAngle = player.facingAngle
            rotate(weaponAngle * 57.2958f, pivot = Offset(screenX, currentY)) {
                when (player.equippedWeapon.type) {
                    WeaponType.RUBBER_BAT -> {
                        // Squeaky pink rubber bat
                        drawRoundRect(
                            color = Color(0xFFFF1493),
                            topLeft = Offset(screenX + 16f, currentY - 8f),
                            size = Size(34f, 16f),
                            cornerRadius = CornerRadius(8f, 8f)
                        )
                        // Yellow grip
                        drawRect(Color(0xFFFDE047), Offset(screenX + 6f, currentY - 4f), Size(12f, 8f))
                        // Sparkle on bat tip
                        drawCircle(Color.White, 3f, Offset(screenX + 44f, currentY))
                    }
                    WeaponType.GLAM_WHIP -> {
                        val whipPath = Path().apply {
                            moveTo(screenX + 8f, currentY)
                            cubicTo(screenX + 22f, currentY - 18f, screenX + 34f, currentY + 18f, screenX + 50f, currentY - 6f)
                        }
                        drawPath(whipPath, Color(0xFFC084FC), style = Stroke(5f))
                        drawCircle(Color(0xFF00F0FF), 4f, Offset(screenX + 50f, currentY - 6f))
                    }
                    WeaponType.PINK_SHOCKER -> {
                        drawRect(Color(0xFFFF69B4), Offset(screenX + 8f, currentY - 4f), Size(28f, 8f))
                        drawCircle(Color(0xFFFF007F), 9f, Offset(screenX + 38f, currentY))
                        drawCircle(Color.White, 4f, Offset(screenX + 38f, currentY))
                    }
                    WeaponType.AUTOTUNE_MIC -> {
                        drawRect(Color(0xFFFFD700), Offset(screenX + 8f, currentY - 5f), Size(22f, 10f))
                        drawCircle(Color(0xFFF1F5F9), 8f, Offset(screenX + 30f, currentY))
                        drawCircle(Color(0xFFFFD700), 9f, Offset(screenX + 30f, currentY), style = Stroke(2.5f))
                    }
                    WeaponType.PLATINUM_BLASTER -> {
                        drawRoundRect(Color(0xFFE2E8F0), Offset(screenX + 10f, currentY - 6f), Size(26f, 12f), CornerRadius(4f, 4f))
                        drawRect(Color(0xFF00F0FF), Offset(screenX + 16f, currentY - 3f), Size(12f, 6f))
                        drawCircle(Color.White, 4f, Offset(screenX + 36f, currentY))
                    }
                }
            }
        }
    }

    fun drawEnemy(
        drawScope: DrawScope,
        enemy: Enemy,
        screenX: Float,
        screenY: Float,
        timeSec: Float
    ) {
        with(drawScope) {
            // Shadow
            drawOval(
                color = Color(0x44000000),
                topLeft = Offset(screenX - enemy.radius * 0.9f, screenY + enemy.radius * 0.8f),
                size = Size(enemy.radius * 1.8f, 12f)
            )

            when (enemy.type) {
                EnemyType.CENSOR_DRONE -> {
                    // Flying metallic censor drone with spinning quad rotors
                    val bob = sin(timeSec * 7f + enemy.x) * 4f
                    val droneY = screenY + bob

                    // Rotor arms
                    drawLine(Color(0xFF64748B), Offset(screenX - 24f, droneY - 14f), Offset(screenX + 24f, droneY + 14f), 3f)
                    drawLine(Color(0xFF64748B), Offset(screenX - 24f, droneY + 14f), Offset(screenX + 24f, droneY - 14f), 3f)

                    // Spinning rotor discs
                    val rotorAngle = (timeSec * 25f)
                    drawCircle(Color(0x88CBD5E1), 8f, Offset(screenX - 24f, droneY - 14f))
                    drawCircle(Color(0x88CBD5E1), 8f, Offset(screenX + 24f, droneY - 14f))
                    drawCircle(Color(0x88CBD5E1), 8f, Offset(screenX - 24f, droneY + 14f))
                    drawCircle(Color(0x88CBD5E1), 8f, Offset(screenX + 24f, droneY + 14f))

                    // Drone Chassis
                    drawRoundRect(
                        color = Color(0xFF334155),
                        topLeft = Offset(screenX - 18f, droneY - 14f),
                        size = Size(36f, 28f),
                        cornerRadius = CornerRadius(6f, 6f)
                    )

                    // Glowing red [CENSOR] scanner matrix screen
                    drawRect(Color(0xFF0F172A), Offset(screenX - 14f, droneY - 8f), Size(28f, 16f))
                    val isBlink = ((timeSec * 4f).toInt() % 2 == 0)
                    drawRect(
                        color = if (isBlink) Color(0xFFDC2626) else Color(0xFFFF5555),
                        topLeft = Offset(screenX - 10f, droneY - 4f),
                        size = Size(20f, 8f)
                    )

                    // Red scanning laser beam onto ground
                    drawLine(
                        color = Color(0x66EF4444),
                        start = Offset(screenX, droneY + 14f),
                        end = Offset(screenX, screenY + 28f),
                        strokeWidth = 3f
                    )
                }

                EnemyType.SHIELD_ENFORCER -> {
                    // Heavy armored robot mech with energy shield
                    val r = enemy.radius

                    // Mech body
                    drawRoundRect(
                        color = Color(0xFF1E293B),
                        topLeft = Offset(screenX - 18f, screenY - 20f),
                        size = Size(36f, 38f),
                        cornerRadius = CornerRadius(6f, 6f)
                    )
                    // Yellow hazard stripes on chest
                    drawLine(Color(0xFFFACC15), Offset(screenX - 12f, screenY - 8f), Offset(screenX + 12f, screenY - 8f), 4f)

                    // Hydraulic legs
                    drawRect(Color(0xFF475569), Offset(screenX - 14f, screenY + 16f), Size(8f, 14f))
                    drawRect(Color(0xFF475569), Offset(screenX + 6f, screenY + 16f), Size(8f, 14f))

                    // Robotic sensor head
                    drawRect(Color(0xFF0F172A), Offset(screenX - 10f, screenY - 30f), Size(20f, 12f))
                    drawCircle(Color(0xFFEF4444), 4f, Offset(screenX, screenY - 24f))

                    // MASSIVE ENERGY SHIELD IN FRONT
                    val shieldAngle = enemy.facingAngle
                    rotate(shieldAngle * 57.2958f, pivot = Offset(screenX, screenY)) {
                        // Glowing cyan/magenta energy barrier
                        val shieldPath = Path().apply {
                            moveTo(screenX + 22f, screenY - 28f)
                            lineTo(screenX + 32f, screenY - 14f)
                            lineTo(screenX + 32f, screenY + 14f)
                            lineTo(screenX + 22f, screenY + 28f)
                            close()
                        }
                        drawPath(shieldPath, Color(0x9900F0FF))
                        drawPath(shieldPath, Color.White, style = Stroke(2f))
                        // Warning symbol on shield
                        drawLine(Color(0xFFFF0055), Offset(screenX + 27f, screenY - 10f), Offset(screenX + 27f, screenY + 10f), 3f)
                    }
                }

                EnemyType.BUREAU_WALKER -> {
                    // Tall 2-legged bureaucratic walker robot
                    val legBob = sin(timeSec * 8f) * 6f

                    // Tall hydraulic stilts
                    drawLine(Color(0xFF64748B), Offset(screenX - 12f, screenY), Offset(screenX - 14f, screenY + 22f + legBob), 4f)
                    drawLine(Color(0xFF64748B), Offset(screenX + 12f, screenY), Offset(screenX + 14f, screenY + 22f - legBob), 4f)

                    // Walker main cabin
                    drawRoundRect(
                        color = Color(0xFF334155),
                        topLeft = Offset(screenX - 20f, screenY - 26f),
                        size = Size(40f, 32f),
                        cornerRadius = CornerRadius(8f, 8f)
                    )
                    // Antenna
                    drawLine(Color(0xFF94A3B8), Offset(screenX - 10f, screenY - 26f), Offset(screenX - 14f, screenY - 38f), 2f)
                    drawCircle(Color(0xFFFF0055), 3f, Offset(screenX - 14f, screenY - 38f))

                    // Red stamp laser
                    drawCircle(Color(0xFFEF4444), 7f, Offset(screenX, screenY - 10f))
                    drawCircle(Color.White, 3f, Offset(screenX, screenY - 10f))
                }

                EnemyType.FITNESS_RAIDER -> {
                    // Muscular raider in tank top and gym shorts
                    val bob = sin(timeSec * 8f) * 3f
                    val rY = screenY + bob
                    // Head & headband
                    drawCircle(Color(0xFFFFD1A4), 13f, Offset(screenX, rY - 24f))
                    drawRect(Color(0xFFFF0055), Offset(screenX - 11f, rY - 32f), Size(22f, 6f))
                    // Muscular torso with purple tank top
                    drawRoundRect(Color(0xFF8B5CF6), Offset(screenX - 16f, rY - 14f), Size(32f, 26f), CornerRadius(4f, 4f))
                    // Biceps
                    drawCircle(Color(0xFFFFD1A4), 8f, Offset(screenX - 18f, rY - 4f))
                    drawCircle(Color(0xFFFFD1A4), 8f, Offset(screenX + 18f, rY - 4f))
                    // Dumbbell in hand
                    drawRect(Color(0xFF334155), Offset(screenX + 20f, rY - 10f), Size(6f, 18f))
                    drawCircle(Color.Black, 6f, Offset(screenX + 23f, rY - 10f))
                    drawCircle(Color.Black, 6f, Offset(screenX + 23f, rY + 8f))
                    // Gym shorts
                    drawRect(Color(0xFF1E293B), Offset(screenX - 14f, rY + 12f), Size(28f, 14f))
                }

                EnemyType.GRAND_CENSOR_BOSS -> {
                    // Huge 4-legged robotic censorship boss tank
                    val r = enemy.radius

                    // 4 Heavy hydraulic legs
                    for (i in listOf(-1, 1)) {
                        for (j in listOf(-1, 1)) {
                            drawLine(
                                Color(0xFF1E293B),
                                Offset(screenX, screenY),
                                Offset(screenX + i * (r + 14f), screenY + j * (r + 14f)),
                                8f
                            )
                            drawCircle(Color(0xFF475569), 9f, Offset(screenX + i * (r + 14f), screenY + j * (r + 14f)))
                        }
                    }

                    // Armored main chassis
                    drawRoundRect(
                        color = Color(0xFF0F172A),
                        topLeft = Offset(screenX - r, screenY - r),
                        size = Size(r * 2f, r * 2f),
                        cornerRadius = CornerRadius(14f, 14f)
                    )
                    drawRoundRect(
                        color = Color(0xFFFFD700),
                        topLeft = Offset(screenX - r + 8f, screenY - r + 8f),
                        size = Size(r * 2f - 16f, r * 2f - 16f),
                        cornerRadius = CornerRadius(10f, 10f)
                    )

                    // Revolving emergency warning sirens
                    val sirenColor = if ((timeSec * 6f).toInt() % 2 == 0) Color(0xFFFF0055) else Color(0xFF00F0FF)
                    drawCircle(sirenColor, 10f, Offset(screenX - r + 14f, screenY - r + 14f))
                    drawCircle(sirenColor, 10f, Offset(screenX + r - 14f, screenY - r + 14f))

                    // Pulsing central anti-fun core
                    val corePulse = (sin(timeSec * 8f) + 1f) * 0.5f
                    drawCircle(Color(0xFFDC2626), 18f + corePulse * 4f, Offset(screenX, screenY))
                }

                EnemyType.WHITE_BUFF_MALE -> {
                    val rY = screenY + sin(timeSec * 8f) * 2f
                    drawCircle(Color(0xFFFFF0E0), 12f, Offset(screenX, rY - 22f))
                    drawRect(Color(0xFF0F172A), Offset(screenX - 8f, rY - 24f), Size(16f, 5f))
                    drawRoundRect(Color(0xFFE2E8F0), Offset(screenX - 14f, rY - 10f), Size(28f, 24f), CornerRadius(4f, 4f))
                    drawCircle(Color(0xFFFFF0E0), 7f, Offset(screenX - 16f, rY - 2f))
                    drawCircle(Color(0xFFFFF0E0), 7f, Offset(screenX + 16f, rY - 2f))
                    drawRect(Color(0xFF1E293B), Offset(screenX - 12f, rY + 14f), Size(24f, 12f))
                }

                EnemyType.GIRL_RUNNER -> {
                    val rY = screenY + sin(timeSec * 12f) * 3f
                    drawCircle(Color(0xFFFFD1A4), 11f, Offset(screenX, rY - 20f))
                    drawCircle(Color(0xFFEC4899), 13f, Offset(screenX, rY - 22f), style = Stroke(3f))
                    drawRoundRect(Color(0xFFF43F5E), Offset(screenX - 11f, rY - 9f), Size(22f, 20f), CornerRadius(4f, 4f))
                    drawRect(Color(0xFF00F0FF), Offset(screenX - 10f, rY + 11f), Size(20f, 12f))
                }

                EnemyType.HEAVY_FEMALE -> {
                    val rY = screenY + sin(timeSec * 6f) * 2f
                    drawCircle(Color(0xFFFFE0BD), 15f, Offset(screenX, rY - 24f))
                    drawRoundRect(Color(0xFFA855F7), Offset(screenX - 18f, rY - 9f), Size(36f, 26f), CornerRadius(6f, 6f))
                    drawRect(Color(0xFF475569), Offset(screenX + 16f, rY - 20f), Size(8f, 32f))
                    drawCircle(Color(0xFFFFD700), 5f, Offset(screenX + 20f, rY - 20f))
                }

                EnemyType.WALKING_FEMALE_SIGN -> {
                    val rY = screenY + sin(timeSec * 9f) * 3f
                    drawCircle(Color(0xFFEC4899), 10f, Offset(screenX, rY - 22f))
                    val dressPath = Path().apply {
                        moveTo(screenX, rY - 12f)
                        lineTo(screenX - 16f, rY + 12f)
                        lineTo(screenX + 16f, rY + 12f)
                        close()
                    }
                    drawPath(dressPath, Color(0xFFF43F5E))
                    drawRect(Color(0xFF334155), Offset(screenX - 8f, rY + 12f), Size(5f, 14f))
                    drawRect(Color(0xFF334155), Offset(screenX + 3f, rY + 12f), Size(5f, 14f))
                }

                EnemyType.FAT_SKUF_BOSS -> {
                    val r = enemy.radius
                    val bob = sin(timeSec * 4f) * 4f
                    drawCircle(Color(0xFFFFE0BD), r, Offset(screenX, screenY + bob))
                    drawCircle(Color(0xFF10B981), r + 8f, Offset(screenX, screenY + bob), style = Stroke(4f))
                    drawCircle(Color(0xFFF8FAFC), r * 0.7f, Offset(screenX, screenY + bob))
                    drawCircle(Color(0xFFFFE0BD), r * 0.5f, Offset(screenX, screenY - r * 0.7f + bob))
                    drawArc(Color(0xFF78350F), 0f, 180f, false, Offset(screenX - r * 0.4f, screenY - r * 0.5f + bob), Size(r * 0.8f, r * 0.5f))
                    drawRoundRect(Color(0xFF15803D), Offset(screenX + r * 0.8f, screenY - 10f + bob), Size(18f, 40f), CornerRadius(8f, 8f))
                }

                EnemyType.USSR_MAN_BOSS -> {
                    val r = enemy.radius
                    drawRoundRect(Color(0xFF991B1B), Offset(screenX - r, screenY - r), Size(r * 2f, r * 2f), CornerRadius(12f, 12f))
                    drawCircle(Color(0xFFFFD700), r * 0.4f, Offset(screenX, screenY))
                    drawCircle(Color.Red, r * 0.25f, Offset(screenX, screenY))
                    drawRect(Color(0xFF334155), Offset(screenX - r * 0.7f, screenY - r - 10f), Size(r * 1.4f, 18f))
                }

                EnemyType.VLAD_MOM_BOSS -> {
                    val r = enemy.radius
                    drawCircle(Color(0xFFFFD1A4), r * 0.8f, Offset(screenX, screenY))
                    drawRoundRect(Color(0xFFE040FB), Offset(screenX - r * 0.9f, screenY - r * 0.5f), Size(r * 1.8f, r * 1.5f), CornerRadius(10f, 10f))
                    drawLine(Color(0xFFB45309), Offset(screenX - r - 10f, screenY - r), Offset(screenX + r + 10f, screenY - r + 15f), 10f)
                }

                EnemyType.VLAD_DAD_BOSS -> {
                    val r = enemy.radius
                    drawCircle(Color(0xFFFFD1A4), r * 0.85f, Offset(screenX, screenY))
                    drawRoundRect(Color.White, Offset(screenX - r * 0.7f, screenY - r * 0.4f), Size(r * 1.4f, r * 1.3f), CornerRadius(8f, 8f))
                    drawLine(Color(0xFF94A3B8), Offset(screenX + r * 0.5f, screenY - r), Offset(screenX + r * 0.8f, screenY + r), 12f)
                }

                EnemyType.GIRL_5YO_BOSS -> {
                    val r = enemy.radius
                    val pulse = sin(timeSec * 8f) * 5f
                    drawCircle(Color(0xFFFF69B4), r + pulse, Offset(screenX, screenY), style = Stroke(3f))
                    drawCircle(Color(0xFFFFF0F5), r * 0.75f, Offset(screenX, screenY))
                    drawCircle(Color(0xFFFF1493), r * 0.3f, Offset(screenX, screenY - 10f))
                    drawLine(Color(0xFFFFD700), Offset(screenX + 10f, screenY), Offset(screenX + r + 15f, screenY - r), 6f)
                    drawCircle(Color(0xFFFFD700), 12f, Offset(screenX + r + 15f, screenY - r))
                }
            }

            // Health bar above enemy
            val barWidth = enemy.radius * 2.2f
            val barHeight = if (enemy.type.isBoss) 9f else 6f
            val barY = screenY - enemy.radius - 22f
            val hpRatio = (enemy.hp / enemy.maxHp).coerceIn(0f, 1f)

            // Background
            drawRect(
                color = Color(0xAA000000),
                topLeft = Offset(screenX - barWidth / 2f, barY),
                size = Size(barWidth, barHeight)
            )
            // Fill
            val hpColor = if (enemy.type.isBoss) Color(0xFFFFD700) else Color(0xFFEF4444)
            drawRect(
                color = hpColor,
                topLeft = Offset(screenX - barWidth / 2f, barY),
                size = Size(barWidth * hpRatio, barHeight)
            )
            if (enemy.type.isBoss) {
                drawRect(
                    color = Color.White,
                    topLeft = Offset(screenX - barWidth / 2f, barY),
                    size = Size(barWidth, barHeight),
                    style = Stroke(1.5f)
                )
            }
        }
    }

    fun drawHypercar(drawScope: DrawScope, car: HypercarSummon, screenX: Float, screenY: Float) {
        with(drawScope) {
            rotate(car.angle * 57.2958f, pivot = Offset(screenX, screenY)) {
                // Sleek golden-pink hypercar
                drawRoundRect(
                    color = Color(0xFFFFD700),
                    topLeft = Offset(screenX - 48f, screenY - 24f),
                    size = Size(96f, 48f),
                    cornerRadius = CornerRadius(14f, 14f)
                )
                // Hot pink cyber racing stripes
                drawRect(
                    color = Color(0xFFFF007F),
                    topLeft = Offset(screenX - 48f, screenY - 6f),
                    size = Size(96f, 12f)
                )
                // Cyber cyan tinted canopy
                drawRoundRect(
                    color = Color(0xFF00F0FF),
                    topLeft = Offset(screenX - 14f, screenY - 15f),
                    size = Size(40f, 30f),
                    cornerRadius = CornerRadius(8f, 8f)
                )
                // Headlight beams
                drawCircle(Color(0xFFFFFBEB), 8f, Offset(screenX + 44f, screenY - 15f))
                drawCircle(Color(0xFFFFFBEB), 8f, Offset(screenX + 44f, screenY + 15f))
                // Exhaust fire
                drawCircle(Color(0xFFFF3D00), 9f, Offset(screenX - 52f, screenY))
                drawCircle(Color(0xFFFFD600), 6f, Offset(screenX - 56f, screenY))
            }
        }
    }

    fun drawDroppedItem(drawScope: DrawScope, item: DroppedItem, screenX: Float, screenY: Float, timeSec: Float) {
        with(drawScope) {
            val floatOffset = sin(timeSec * 5f + screenX) * 3f
            val itemY = screenY + floatOffset

            // Sparkle glow
            drawCircle(Color.White.copy(alpha = 0.35f), 12f, Offset(screenX, itemY))

            when (item.type) {
                DropType.GOLD -> {
                    drawCircle(Color(0xFFFFD700), 8f, Offset(screenX, itemY))
                    drawCircle(Color(0xFFFFFBEB), 3f, Offset(screenX - 2f, itemY - 2f))
                }
                DropType.DIAMOND -> {
                    val path = Path().apply {
                        moveTo(screenX, itemY - 9f)
                        lineTo(screenX + 7f, itemY)
                        lineTo(screenX, itemY + 9f)
                        lineTo(screenX - 7f, itemY)
                        close()
                    }
                    drawPath(path, Color(0xFF00E5FF))
                    drawPath(path, Color.White, style = Stroke(1.5f))
                }
                DropType.BATTERY -> {
                    drawRoundRect(Color(0xFF10B981), Offset(screenX - 5f, itemY - 8f), Size(10f, 16f), CornerRadius(2f, 2f))
                    drawRect(Color(0xFFE2E8F0), Offset(screenX - 2f, itemY - 10f), Size(4f, 3f))
                }
                DropType.HEART -> {
                    drawCircle(Color(0xFFFF0055), 7f, Offset(screenX, itemY))
                }
            }
        }
    }

    fun drawAmbientLighting(
        drawScope: DrawScope,
        timeOfDay: TimeOfDay,
        playerScreenX: Float,
        playerScreenY: Float,
        canvasWidth: Float,
        canvasHeight: Float
    ) {
        with(drawScope) {
            if (timeOfDay == TimeOfDay.NIGHT) {
                // Night spotlight around VlaDick
                val radialBrush = Brush.radialGradient(
                    colors = listOf(
                        Color.Transparent,
                        Color(0x33000000),
                        Color(0xBB0A071B),
                        Color(0xF00A071B)
                    ),
                    center = Offset(playerScreenX, playerScreenY),
                    radius = 340f
                )
                drawRect(
                    brush = radialBrush,
                    topLeft = Offset.Zero,
                    size = Size(canvasWidth, canvasHeight)
                )
            } else {
                // Day / Sunset subtle color overlay
                drawRect(
                    color = timeOfDay.ambientColor,
                    topLeft = Offset.Zero,
                    size = Size(canvasWidth, canvasHeight)
                )
            }
        }
    }

    fun drawFriendlyNPC(
        drawScope: DrawScope,
        npc: FriendlyNPC,
        screenX: Float,
        screenY: Float,
        timeSec: Float
    ) {
        with(drawScope) {
            val bob = sin(timeSec * 3f) * 2f
            val currentY = screenY + bob

            // Shadow
            drawOval(Color(0x66000000), Offset(screenX - 22f, screenY + 20f), Size(44f, 14f))

            // Subtle neon aura
            drawCircle(npc.color.copy(alpha = 0.22f), 34f, Offset(screenX, currentY))

            when (npc.id) {
                "npc_julien" -> {
                    // Julien: French muscular bodyguard & producer in stylish beret and gold chains
                    val skinColor = Color(0xFF3E2723)
                    // Head
                    drawCircle(skinColor, 14f, Offset(screenX, currentY - 24f))
                    // French luxury beret (magenta-violet) tilted
                    drawOval(Color(0xFFC026D3), Offset(screenX - 16f, currentY - 40f), Size(32f, 16f))
                    drawCircle(Color(0xFFE879F9), 3f, Offset(screenX, currentY - 41f))
                    // Gold hoop earring
                    drawCircle(Color(0xFFFFD700), 2.5f, Offset(screenX + 13f, currentY - 22f))
                    // Stylish sunglasses
                    drawRoundRect(Color(0xFF0F172A), Offset(screenX - 10f, currentY - 26f), Size(20f, 6f), CornerRadius(2f, 2f))

                    // Muscular athletic build with open luxury velvet vest
                    drawRoundRect(Color(0xFF701A75), Offset(screenX - 18f, currentY - 10f), Size(36f, 32f), CornerRadius(6f, 6f))
                    // Exposed muscular chest
                    drawRect(skinColor, Offset(screenX - 6f, currentY - 8f), Size(12f, 16f))
                    // Diamond Cuban chain
                    drawCircle(Color(0xFF00F0FF), 9f, Offset(screenX, currentY - 1f), style = Stroke(2.5f))

                    // Strong arms with gold bracelets
                    drawCircle(skinColor, 8.5f, Offset(screenX - 22f, currentY + 2f))
                    drawCircle(skinColor, 8.5f, Offset(screenX + 22f, currentY + 2f))
                    drawCircle(Color(0xFFFFD700), 4f, Offset(screenX - 22f, currentY + 6f), style = Stroke(2f))

                    // White luxury trousers
                    drawRect(Color(0xFFF8FAFC), Offset(screenX - 14f, currentY + 22f), Size(28f, 18f))
                }
                "npc_batya" -> {
                    // Batya: Giant burly ex-convict garage chief in mechanic leather vest with wrench & cigar
                    val skinColor = Color(0xFF42271D)
                    // Head
                    drawCircle(skinColor, 17f, Offset(screenX, currentY - 25f))
                    // Tough crew bandana
                    drawRect(Color(0xFFDC2626), Offset(screenX - 15f, currentY - 40f), Size(30f, 8f))
                    // Glowing cigar in mouth
                    drawLine(Color(0xFF78350F), Offset(screenX + 4f, currentY - 18f), Offset(screenX + 18f, currentY - 16f), 3f)
                    drawCircle(Color(0xFFFF4500), 2.5f, Offset(screenX + 19f, currentY - 16f))

                    // Massive heavyweight torso
                    drawRoundRect(Color(0xFF18181B), Offset(screenX - 22f, currentY - 9f), Size(44f, 35f), CornerRadius(8f, 8f))
                    // Silver studs on leather vest
                    drawCircle(Color(0xFFCBD5E1), 2f, Offset(screenX - 16f, currentY - 2f))
                    drawCircle(Color(0xFFCBD5E1), 2f, Offset(screenX + 16f, currentY - 2f))

                    // Huge massive tattooed arms
                    drawCircle(skinColor, 11f, Offset(screenX - 26f, currentY + 4f))
                    drawCircle(skinColor, 11f, Offset(screenX + 26f, currentY + 4f))
                    // Heavy titanium wrench in right hand
                    drawLine(Color(0xFF94A3B8), Offset(screenX + 26f, currentY - 6f), Offset(screenX + 34f, currentY + 16f), 5f)
                    drawCircle(Color(0xFF64748B), 5f, Offset(screenX + 25f, currentY - 7f))

                    // Heavy duty work pants
                    drawRect(Color(0xFF334155), Offset(screenX - 17f, currentY + 26f), Size(34f, 18f))
                }
                "npc_marcus" -> {
                    // Marcus: Muscular black athlete, gold tank top, massive gold chain
                    val skinColor = Color(0xFF4E342E)
                    // Head
                    drawCircle(skinColor, 15f, Offset(screenX, currentY - 24f))
                    // Beard & hair
                    drawRect(Color(0xFF1E1E1E), Offset(screenX - 12f, currentY - 37f), Size(24f, 7f))
                    drawRect(Color(0xFF1E1E1E), Offset(screenX - 8f, currentY - 14f), Size(16f, 6f))

                    // Muscular torso with gold athletic top
                    drawRoundRect(Color(0xFFFFD700), Offset(screenX - 18f, currentY - 10f), Size(36f, 32f), CornerRadius(6f, 6f))

                    // Huge muscular arms
                    drawCircle(skinColor, 9f, Offset(screenX - 22f, currentY + 2f))
                    drawCircle(skinColor, 9f, Offset(screenX + 22f, currentY + 2f))

                    // Heavy gold chain
                    drawCircle(Color(0xFFFFD700), 10f, Offset(screenX, currentY - 2f), style = Stroke(3f))
                    drawCircle(Color(0xFFFFD700), 4f, Offset(screenX, currentY + 6f))

                    // Trousers
                    drawRect(Color(0xFF1E293B), Offset(screenX - 14f, currentY + 22f), Size(28f, 18f))
                }
                else -> {
                    // DJ Malik: Neon cyan headphones, purple bomber, synthesizer deck
                    val skinColor = Color(0xFF5D4037)
                    drawCircle(skinColor, 14f, Offset(screenX, currentY - 22f))
                    // Neon cyan DJ headphones
                    drawArc(Color(0xFF00F0FF), 180f, 180f, false, Offset(screenX - 18f, currentY - 38f), Size(36f, 30f), style = Stroke(4f))
                    drawCircle(Color(0xFF00F0FF), 6f, Offset(screenX - 16f, currentY - 22f))
                    drawCircle(Color(0xFF00F0FF), 6f, Offset(screenX + 16f, currentY - 22f))

                    // Purple bomber jacket
                    drawRoundRect(Color(0xFF9333EA), Offset(screenX - 16f, currentY - 8f), Size(32f, 28f), CornerRadius(6f, 6f))
                    drawLine(Color(0xFF00F0FF), Offset(screenX, currentY - 8f), Offset(screenX, currentY + 20f), 2f)

                    // Synthesizer DJ deck
                    drawRoundRect(Color(0xFF0F172A), Offset(screenX - 22f, currentY + 14f), Size(44f, 14f), CornerRadius(4f, 4f))
                    drawCircle(Color(0xFFFF007F), 3f, Offset(screenX - 12f, currentY + 21f))
                    drawCircle(Color(0xFF00F0FF), 3f, Offset(screenX + 12f, currentY + 21f))
                }
            }

            // Floating interaction indicator bubble
            val bubbleY = currentY - 50f + (sin(timeSec * 4f) * 3f)
            drawRoundRect(Color(0xEE0F172A), Offset(screenX - 36f, bubbleY - 8f), Size(72f, 18f), CornerRadius(6f, 6f))
            drawRoundRect(npc.color, Offset(screenX - 36f, bubbleY - 8f), Size(72f, 18f), CornerRadius(6f, 6f), style = Stroke(1.5f))
        }
    }

    fun drawWorldArtifact(
        drawScope: DrawScope,
        worldArt: WorldArtifact,
        screenX: Float,
        screenY: Float,
        timeSec: Float
    ) {
        if (worldArt.isCollected) return
        with(drawScope) {
            val hover = sin(timeSec * 4f) * 6f
            val artY = screenY - 22f + hover

            // Pedestal shadow & base
            drawOval(Color(0x66000000), Offset(screenX - 22f, screenY + 16f), Size(44f, 14f))
            drawRoundRect(Color(0xFFFFD700), Offset(screenX - 18f, screenY + 2f), Size(36f, 16f), CornerRadius(4f, 4f))
            drawRoundRect(Color(0xFFFAF5FF), Offset(screenX - 14f, screenY - 10f), Size(28f, 14f), CornerRadius(3f, 3f))

            // Light beam
            drawLine(
                Color(0x44FFD700),
                Offset(screenX, screenY),
                Offset(screenX, screenY - 70f),
                strokeWidth = 16f
            )

            // Relic sphere / jewel
            drawCircle(worldArt.artifact.rarity.color, 14f, Offset(screenX, artY))
            drawCircle(Color.White, 7f, Offset(screenX, artY))

            // Sparkle particles
            val sOffset = sin(timeSec * 6f) * 10f
            drawCircle(Color(0xFF00F0FF), 3f, Offset(screenX + sOffset, artY - 12f))
            drawCircle(Color(0xFFFF007F), 3f, Offset(screenX - sOffset, artY + 12f))

            // Floating indicator bubble
            val bubbleY = artY - 24f
            drawRoundRect(Color(0xEE0F172A), Offset(screenX - 38f, bubbleY - 8f), Size(76f, 18f), CornerRadius(6f, 6f))
            drawRoundRect(Color(0xFFFFD700), Offset(screenX - 38f, bubbleY - 8f), Size(76f, 18f), CornerRadius(6f, 6f), style = Stroke(1.5f))
        }
    }

    fun drawActiveCompanionRunner(
        drawScope: DrawScope,
        companion: ActiveCompanion,
        screenX: Float,
        screenY: Float,
        timeSec: Float
    ) {
        with(drawScope) {
            val bob = sin(timeSec * 10f) * 3f
            val cY = screenY + bob

            // Shadow
            drawOval(Color(0x66000000), Offset(screenX - 16f, screenY + 16f), Size(32f, 10f))

            // Dark runner body with cyan glowing eyes & visor
            drawCircle(Color(0xFF0F172A), 12f, Offset(screenX, cY - 18f))
            drawRect(Color(0xFF00F0FF), Offset(screenX - 8f, cY - 20f), Size(16f, 5f))

            // Muscular torso with companion emblem
            drawRoundRect(Color(0xFF1E293B), Offset(screenX - 12f, cY - 6f), Size(24f, 22f), CornerRadius(4f, 4f))
            drawCircle(companion.color, 4f, Offset(screenX, cY + 2f))

            // Laser weapon in hand
            drawRect(Color(0xFF00F0FF), Offset(screenX + 10f, cY - 4f), Size(18f, 6f))
        }
    }
}
