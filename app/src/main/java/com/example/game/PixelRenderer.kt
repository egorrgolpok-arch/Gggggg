package com.example.game

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
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
        timeSec: Float,
        skinBitmap: ImageBitmap? = null,
        weaponBdsmBitmap: ImageBitmap? = null,
        weaponUltraGayBitmap: ImageBitmap? = null
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

            if (skinBitmap != null) {
                // Render Vlad's Active Skin Medallion & Custom Pixel Avatar
                val avatarRadius = 24f
                val avatarCenterY = currentY - 26f

                // Glowing outer bling ring for active skin
                drawCircle(
                    color = Color(0xFFFFD700),
                    radius = avatarRadius + 4f,
                    center = Offset(screenX, avatarCenterY)
                )

                // Clip active skin sprite image inside clean circular masked avatar
                drawScope.drawIntoCanvas { canvas ->
                    val path = Path().apply {
                        addOval(
                            androidx.compose.ui.geometry.Rect(
                                left = screenX - avatarRadius,
                                top = avatarCenterY - avatarRadius,
                                right = screenX + avatarRadius,
                                bottom = avatarCenterY + avatarRadius
                            )
                        )
                    }
                    drawPath(path, Color(0xFF0F041D))
                    canvas.save()
                    canvas.clipPath(path)

                    val cols = 7
                    val rows = 6
                    val frameW = skinBitmap.width / cols
                    val frameH = skinBitmap.height / rows
                    val frameCol = if (isMoving) ((timeSec * 8f).toInt() % 4) + 1 else 0
                    val frameRow = 1
                    val srcX = (frameCol * frameW).coerceIn(0, skinBitmap.width - frameW)
                    val srcY = (frameRow * frameH).coerceIn(0, skinBitmap.height - frameH)

                    drawImage(
                        image = skinBitmap,
                        srcOffset = IntOffset(srcX, srcY),
                        srcSize = IntSize(frameW, frameH),
                        dstOffset = IntOffset((screenX - avatarRadius).toInt(), (avatarCenterY - avatarRadius).toInt()),
                        dstSize = IntSize((avatarRadius * 2f).toInt(), (avatarRadius * 2f).toInt())
                    )
                    canvas.restore()
                }

                // Active skin title badge emoji above head
                drawCircle(Color(0xFF0F172A), 9f, Offset(screenX + 18f, avatarCenterY - 18f))
                drawCircle(Color(0xFFFFD700), 9f, Offset(screenX + 18f, avatarCenterY - 18f), style = Stroke(1.5f))

                // 2. Animated Designer Sneakers & Legs
                drawRoundRect(
                    color = Color.White,
                    topLeft = Offset(screenX - 16f + footSway, currentY + 16f),
                    size = Size(12f, 10f),
                    cornerRadius = CornerRadius(3f, 3f)
                )
                drawRect(Color(0xFFFF007F), Offset(screenX - 16f + footSway, currentY + 22f), Size(12f, 4f))

                drawRoundRect(
                    color = Color.White,
                    topLeft = Offset(screenX + 4f - footSway, currentY + 16f),
                    size = Size(12f, 10f),
                    cornerRadius = CornerRadius(3f, 3f)
                )
                drawRect(Color(0xFFFF007F), Offset(screenX + 4f - footSway, currentY + 22f), Size(12f, 4f))

                // 3. Luxurious Fluffy White Fur Coat
                drawRoundRect(
                    color = Color(0xFFF8FAFC),
                    topLeft = Offset(screenX - 22f + coatSway, currentY - 10f),
                    size = Size(44f, 28f),
                    cornerRadius = CornerRadius(8f, 8f)
                )
                drawCircle(Color(0xFFE2E8F0), 6f, Offset(screenX - 22f, currentY))
                drawCircle(Color(0xFFE2E8F0), 6f, Offset(screenX + 22f, currentY))
            } else {
                // 2. Animated Designer Sneakers & Legs
                drawRoundRect(
                    color = Color.White,
                    topLeft = Offset(screenX - 16f + footSway, currentY + 16f),
                    size = Size(12f, 10f),
                    cornerRadius = CornerRadius(3f, 3f)
                )
                drawRect(Color(0xFFFF007F), Offset(screenX - 16f + footSway, currentY + 22f), Size(12f, 4f))

                drawRoundRect(
                    color = Color.White,
                    topLeft = Offset(screenX + 4f - footSway, currentY + 16f),
                    size = Size(12f, 10f),
                    cornerRadius = CornerRadius(3f, 3f)
                )
                drawRect(Color(0xFFFF007F), Offset(screenX + 4f - footSway, currentY + 22f), Size(12f, 4f))

                // 3. Luxurious Fluffy White Fur Coat
                drawRoundRect(
                    color = Color(0xFFF8FAFC),
                    topLeft = Offset(screenX - 22f + coatSway, currentY - 14f),
                    size = Size(44f, 34f),
                    cornerRadius = CornerRadius(10f, 10f)
                )
                drawCircle(Color(0xFFE2E8F0), 6f, Offset(screenX - 22f, currentY))
                drawCircle(Color(0xFFE2E8F0), 6f, Offset(screenX + 22f, currentY))
                drawRect(
                    color = Color(0xFFFF1493),
                    topLeft = Offset(screenX - 8f, currentY - 10f),
                    size = Size(16f, 28f)
                )
            }

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

            // 7. Weapons in hand with animated swing (supports multiple equipped weapons!)
            val weaponAngle = player.facingAngle
            val weaponsToDraw = player.getEffectiveEquippedWeapons()
            weaponsToDraw.forEachIndexed { index, weapon ->
                val angleOffset = if (weaponsToDraw.size > 1) {
                    (index - (weaponsToDraw.size - 1) / 2f) * 18f
                } else 0f
                rotate((weaponAngle * 57.2958f) + angleOffset, pivot = Offset(screenX, currentY)) {
                    when (weapon.type) {
                        WeaponType.RUBBER_BAT -> {
                            drawRoundRect(
                                color = Color(0xFFFF1493),
                                topLeft = Offset(screenX + 16f, currentY - 8f),
                                size = Size(34f, 16f),
                                cornerRadius = CornerRadius(8f, 8f)
                            )
                            drawRect(Color(0xFFFDE047), Offset(screenX + 6f, currentY - 4f), Size(12f, 8f))
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
                        WeaponType.EXPLODING_BLACK_MAN -> {
                            drawRoundRect(Color(0xFF0F172A), Offset(screenX + 10f, currentY - 7f), Size(26f, 14f), CornerRadius(4f, 4f))
                            drawCircle(Color.Black, 5f, Offset(screenX + 34f, currentY))
                            drawCircle(Color(0xFFEF4444), 2f, Offset(screenX + 36f, currentY - 2f))
                        }
                        WeaponType.RAINBOW_LGBT_FLAG -> {
                            drawLine(Color(0xFFFFD700), Offset(screenX + 8f, currentY), Offset(screenX + 40f, currentY), 3f)
                            val rainbowColors = listOf(Color.Red, Color(0xFFFF9800), Color.Yellow, Color.Green, Color.Blue, Color(0xFF9C27B0))
                            for (ci in rainbowColors.indices) {
                                drawLine(rainbowColors[ci], Offset(screenX + 18f, currentY - 10f + ci * 3.5f), Offset(screenX + 38f, currentY - 10f + ci * 3.5f), 3f)
                            }
                        }
                        WeaponType.CLUSTER_PINK_PHALLUS -> {
                            drawRoundRect(Color(0xFFFF1493), Offset(screenX + 12f, currentY - 6f), Size(26f, 12f), CornerRadius(6f, 6f))
                            drawCircle(Color(0xFFFF69B4), 7f, Offset(screenX + 38f, currentY))
                            drawCircle(Color(0xFFFF1493), 5f, Offset(screenX + 12f, currentY - 5f))
                            drawCircle(Color(0xFFFF1493), 5f, Offset(screenX + 12f, currentY + 5f))
                        }
                        WeaponType.EXPLOSIVE_BOOM_BLASTER -> {
                            drawRoundRect(Color(0xFFEA580C), Offset(screenX + 10f, currentY - 8f), Size(30f, 16f), CornerRadius(5f, 5f))
                            drawCircle(Color(0xFFFBBF24), 7f, Offset(screenX + 40f, currentY))
                            drawCircle(Color(0xFFEF4444), 4f, Offset(screenX + 40f, currentY))
                        }
                        WeaponType.WHITE_SLIME_DISPENSER -> {
                            drawRoundRect(Color(0xFFE2E8F0), Offset(screenX + 10f, currentY - 7f), Size(26f, 14f), CornerRadius(4f, 4f))
                            drawCircle(Color.White, 6f, Offset(screenX + 36f, currentY))
                            drawCircle(Color(0xFF00F0FF), 3f, Offset(screenX + 22f, currentY))
                        }
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

    fun drawHypercar(
        drawScope: DrawScope,
        car: HypercarSummon,
        screenX: Float,
        screenY: Float,
        customization: CarCustomization? = null,
        upgrades: CarUpgrades? = null
    ) {
        with(drawScope) {
            rotate(car.angle * 57.2958f, pivot = Offset(screenX, screenY)) {
                val hasCustomPixels = customization != null && customization.pixels.any { it != 0 }

                if (hasCustomPixels) {
                    val cSize = CarCustomization.CANVAS_SIZE
                    val pixelScale = 3.2f
                    val totalW = cSize * pixelScale
                    val totalH = cSize * pixelScale
                    val startX = screenX - totalW / 2f
                    val startY = screenY - totalH / 2f

                    // Draw 32x32 custom pixel matrix
                    for (y in 0 until cSize) {
                        for (x in 0 until cSize) {
                            val pColor = customization!!.pixels[y * cSize + x]
                            if (pColor != 0) {
                                drawRect(
                                    color = Color(pColor),
                                    topLeft = Offset(startX + x * pixelScale, startY + y * pixelScale),
                                    size = Size(pixelScale + 0.2f, pixelScale + 0.2f)
                                )
                            }
                        }
                    }
                } else {
                    // Sleek golden-pink hypercar base
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
                }

                // Headlight beams
                drawCircle(Color(0xFFFFFBEB), 7f, Offset(screenX + 46f, screenY - 14f))
                drawCircle(Color(0xFFFFFBEB), 7f, Offset(screenX + 46f, screenY + 14f))

                // TUNING UPGRADE 1: Bullbar with dildos (Кенгурятник с дилдоками)
                if (upgrades != null && upgrades.bullbarLevel > 0) {
                    // Golden heavy bullbar frame
                    drawRoundRect(
                        color = Color(0xFFFFD700),
                        topLeft = Offset(screenX + 44f, screenY - 22f),
                        size = Size(10f, 44f),
                        cornerRadius = CornerRadius(3f, 3f)
                    )
                    // Pink dildo horns (top, center, bottom)
                    drawRoundRect(
                        color = Color(0xFFFF007F),
                        topLeft = Offset(screenX + 50f, screenY - 18f),
                        size = Size(16f, 6f),
                        cornerRadius = CornerRadius(3f, 3f)
                    )
                    drawCircle(Color(0xFFFF69B4), 4f, Offset(screenX + 66f, screenY - 15f))

                    drawRoundRect(
                        color = Color(0xFFFF007F),
                        topLeft = Offset(screenX + 52f, screenY - 3f),
                        size = Size(18f, 6f),
                        cornerRadius = CornerRadius(3f, 3f)
                    )
                    drawCircle(Color(0xFFFF69B4), 4.5f, Offset(screenX + 70f, screenY))

                    drawRoundRect(
                        color = Color(0xFFFF007F),
                        topLeft = Offset(screenX + 50f, screenY + 12f),
                        size = Size(16f, 6f),
                        cornerRadius = CornerRadius(3f, 3f)
                    )
                    drawCircle(Color(0xFFFF69B4), 4f, Offset(screenX + 66f, screenY + 15f))
                }

                // TUNING UPGRADE 2: Vibrating spoiler (Вибро-спойлер)
                if (upgrades != null && upgrades.spoilerLevel > 0) {
                    val vib = (sin(System.currentTimeMillis() * 0.05f) * 2f).toFloat()
                    // Spoiler struts
                    drawLine(Color(0xFF94A3B8), Offset(screenX - 44f, screenY - 16f), Offset(screenX - 54f, screenY - 20f + vib), 3f)
                    drawLine(Color(0xFF94A3B8), Offset(screenX - 44f, screenY + 16f), Offset(screenX - 54f, screenY + 20f + vib), 3f)
                    // Aerodynamic neon wing
                    drawRoundRect(
                        color = Color(0xFF00F0FF),
                        topLeft = Offset(screenX - 58f, screenY - 26f + vib),
                        size = Size(8f, 52f),
                        cornerRadius = CornerRadius(3f, 3f)
                    )
                    drawRoundRect(
                        color = Color(0xFFFF007F),
                        topLeft = Offset(screenX - 57f, screenY - 18f + vib),
                        size = Size(6f, 36f),
                        cornerRadius = CornerRadius(2f, 2f)
                    )
                }

                // TUNING UPGRADE 3: Rocket engine (Ракетный двигатель)
                if (upgrades != null && upgrades.rocketLevel > 0) {
                    // Twin thruster nozzles
                    drawRect(Color(0xFF334155), Offset(screenX - 54f, screenY - 12f), Size(10f, 7f))
                    drawRect(Color(0xFF334155), Offset(screenX - 54f, screenY + 5f), Size(10f, 7f))
                    // Blue plasma core
                    drawCircle(Color(0xFF00E5FF), 8f, Offset(screenX - 60f, screenY - 8.5f))
                    drawCircle(Color(0xFF00E5FF), 8f, Offset(screenX - 60f, screenY + 8.5f))
                }

                // TUNING UPGRADE 4: Rainbow nitro trail (Радужное нитро)
                if (upgrades != null && upgrades.nitroLevel > 0) {
                    val rainbow = listOf(
                        Color(0xFFFF0055), Color(0xFFFF7F00), Color(0xFFFFD700),
                        Color(0xFF00FF66), Color(0xFF00E5FF), Color(0xFFB5179E)
                    )
                    for (i in 0..5) {
                        val nColor = rainbow[i]
                        drawCircle(nColor.copy(alpha = 0.85f), 9f - i * 0.8f, Offset(screenX - 54f - (i * 9f), screenY + (sin(i * 1.2f) * 6f).toFloat()))
                    }
                } else {
                    // Standard exhaust fire
                    drawCircle(Color(0xFFFF3D00), 9f, Offset(screenX - 52f, screenY))
                    drawCircle(Color(0xFFFFD600), 6f, Offset(screenX - 56f, screenY))
                }

                // TUNING UPGRADE 5: Diamond armor shield (Бриллиантовая броня)
                if (upgrades != null && upgrades.armorLevel > 0) {
                    drawRoundRect(
                        color = Color(0xFF00F0FF).copy(alpha = 0.45f),
                        topLeft = Offset(screenX - 54f, screenY - 28f),
                        size = Size(108f, 56f),
                        cornerRadius = CornerRadius(16f, 16f),
                        style = Stroke(2.5f)
                    )
                    drawCircle(Color.White, 3f, Offset(screenX - 30f, screenY - 26f))
                    drawCircle(Color.White, 3.5f, Offset(screenX + 30f, screenY + 26f))
                }
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
        timeSec: Float,
        companionBitmap: ImageBitmap? = null
    ) {
        with(drawScope) {
            val bob = sin(timeSec * 10f) * 3f
            val cY = screenY + bob

            // Shadow
            drawOval(Color(0x66000000), Offset(screenX - 16f, screenY + 16f), Size(32f, 10f))

            if (companionBitmap != null) {
                // Render muscular bodybuilder companion sprite sheet frame
                val avatarRadius = 22f
                val avatarCenterY = cY - 14f

                // Golden flex aura
                drawCircle(Color(0xFFFFD700).copy(alpha = 0.4f), avatarRadius + 4f, Offset(screenX, avatarCenterY))

                drawScope.drawIntoCanvas { canvas ->
                    val path = Path().apply {
                        addOval(
                            androidx.compose.ui.geometry.Rect(
                                left = screenX - avatarRadius,
                                top = avatarCenterY - avatarRadius,
                                right = screenX + avatarRadius,
                                bottom = avatarCenterY + avatarRadius
                            )
                        )
                    }
                    drawPath(path, Color(0xFF0F041D))
                    canvas.save()
                    canvas.clipPath(path)

                    val cols = 7
                    val rows = 3
                    val frameW = companionBitmap.width / cols
                    val frameH = companionBitmap.height / rows
                    val frameCol = ((timeSec * 8f).toInt() % 4) + 2
                    val frameRow = 1
                    val srcX = (frameCol * frameW).coerceIn(0, companionBitmap.width - frameW)
                    val srcY = (frameRow * frameH).coerceIn(0, companionBitmap.height - frameH)

                    drawImage(
                        image = companionBitmap,
                        srcOffset = IntOffset(srcX, srcY),
                        srcSize = IntSize(frameW, frameH),
                        dstOffset = IntOffset((screenX - avatarRadius).toInt(), (avatarCenterY - avatarRadius).toInt()),
                        dstSize = IntSize((avatarRadius * 2f).toInt(), (avatarRadius * 2f).toInt())
                    )
                    canvas.restore()
                }
            } else {
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

    fun drawProjectile(
        drawScope: DrawScope,
        proj: Projectile,
        screenX: Float,
        screenY: Float,
        timeSec: Float,
        weaponBdsmBitmap: ImageBitmap? = null,
        weaponUltraGayBitmap: ImageBitmap? = null
    ) {
        with(drawScope) {
            when (proj.projectileType) {
                ProjectileType.STANDARD -> {
                    drawCircle(proj.color, proj.radius, Offset(screenX, screenY))
                    drawCircle(Color.White, proj.radius * 0.45f, Offset(screenX, screenY))
                }
                ProjectileType.EXPLODING_MAN -> {
                    // Black runner miniature with red glowing eyes and fuse
                    drawCircle(Color.Black, 8f, Offset(screenX, screenY - 6f))
                    drawCircle(Color(0xFFEF4444), 2f, Offset(screenX + 3f, screenY - 7f))
                    drawRoundRect(Color(0xFF1E293B), Offset(screenX - 5f, screenY), Size(10f, 12f), CornerRadius(2f, 2f))
                    val legBob = sin(timeSec * 20f) * 5f
                    drawLine(Color.Black, Offset(screenX - 3f, screenY + 12f), Offset(screenX - 6f, screenY + 18f + legBob), 3f)
                    drawLine(Color.Black, Offset(screenX + 3f, screenY + 12f), Offset(screenX + 6f, screenY + 18f - legBob), 3f)
                    // Fuse spark
                    drawCircle(Color(0xFFFFD700), 3f, Offset(screenX, screenY - 13f))
                }
                ProjectileType.RAINBOW_FLAG -> {
                    // Flying fluttering pride flag
                    val flagColors = listOf(
                        Color(0xFFE11D48),
                        Color(0xFFF97316),
                        Color(0xFFFACC15),
                        Color(0xFF22C55E),
                        Color(0xFF3B82F6),
                        Color(0xFFA855F7)
                    )
                    val wave = sin(timeSec * 15f + screenX * 0.05f) * 4f
                    drawLine(Color(0xFFFFD700), Offset(screenX - 12f, screenY - 14f), Offset(screenX - 12f, screenY + 14f), 3f)
                    for (i in flagColors.indices) {
                        val sy = screenY - 12f + i * 4f
                        val path = Path().apply {
                            moveTo(screenX - 10f, sy)
                            quadraticBezierTo(screenX + 4f, sy + wave, screenX + 18f, sy)
                        }
                        drawPath(path, flagColors[i], style = Stroke(4f))
                    }
                }
                ProjectileType.CLUSTER_PHALLUS -> {
                    // Big pink toy phallus
                    drawRoundRect(Color(0xFFFF1493), Offset(screenX - 12f, screenY - 5f), Size(24f, 10f), CornerRadius(5f, 5f))
                    drawCircle(Color(0xFFFF69B4), 7f, Offset(screenX + 13f, screenY))
                    drawCircle(Color(0xFFFF1493), 5.5f, Offset(screenX - 13f, screenY - 4f))
                    drawCircle(Color(0xFFFF1493), 5.5f, Offset(screenX - 13f, screenY + 4f))
                    drawCircle(Color.White, 2.5f, Offset(screenX + 13f, screenY))
                }
                ProjectileType.CLUSTER_MINI -> {
                    // Mini phallus split pellet
                    drawCircle(Color(0xFFFF1493), proj.radius, Offset(screenX, screenY))
                    drawCircle(Color(0xFFFF69B4), proj.radius * 0.7f, Offset(screenX, screenY))
                    drawCircle(Color.White, proj.radius * 0.35f, Offset(screenX, screenY))
                }
                ProjectileType.EXPLOSION_ORB -> {
                    // Fiery expanding orb
                    val pulse = sin(timeSec * 18f) * 3f
                    drawCircle(Color(0xFFEF4444), proj.radius + 4f + pulse, Offset(screenX, screenY))
                    drawCircle(Color(0xFFF97316), proj.radius + pulse, Offset(screenX, screenY))
                    drawCircle(Color(0xFFFEF08A), proj.radius * 0.6f, Offset(screenX, screenY))
                    drawCircle(Color.White, proj.radius * 0.3f, Offset(screenX, screenY))
                }
                ProjectileType.WHITE_SLIME -> {
                    // White viscous slime shot
                    drawCircle(Color(0xEEFFFFFF), proj.radius, Offset(screenX, screenY))
                    drawCircle(Color(0xFFE2E8F0), proj.radius * 0.65f, Offset(screenX - 2f, screenY - 2f))
                    drawCircle(Color.White, proj.radius * 0.3f, Offset(screenX + 2f, screenY + 2f))
                }
            }
        }
    }

    fun drawGroundHazard(
        drawScope: DrawScope,
        hazard: GroundHazard,
        screenX: Float,
        screenY: Float,
        timeSec: Float
    ) {
        with(drawScope) {
            val alpha = (hazard.lifetime / hazard.maxLifetime).coerceIn(0.2f, 0.85f)
            val pulse = sin(timeSec * 4f + hazard.x) * 4f
            val r = hazard.radius + pulse

            // White liquid viscous puddle
            drawOval(
                color = Color(0xFFFFFFFF).copy(alpha = alpha * 0.75f),
                topLeft = Offset(screenX - r, screenY - r * 0.65f),
                size = Size(r * 2f, r * 1.3f)
            )
            // Outer slimy edge
            drawOval(
                color = Color(0xFFE2E8F0).copy(alpha = alpha),
                topLeft = Offset(screenX - r, screenY - r * 0.65f),
                size = Size(r * 2f, r * 1.3f),
                style = Stroke(3f)
            )
            // Bubbles inside white puddle
            val b1 = sin(timeSec * 5f) * 6f
            val b2 = cos(timeSec * 6f) * 6f
            drawCircle(Color.White.copy(alpha = alpha), 5f, Offset(screenX - r * 0.3f + b1, screenY - r * 0.1f))
            drawCircle(Color.White.copy(alpha = alpha), 4f, Offset(screenX + r * 0.4f, screenY + r * 0.2f + b2))
            drawCircle(Color(0xFF00F0FF).copy(alpha = alpha * 0.8f), 3f, Offset(screenX + b2, screenY - r * 0.25f))
        }
    }
}
