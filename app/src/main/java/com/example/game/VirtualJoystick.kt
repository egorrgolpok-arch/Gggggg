package com.example.game

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.sqrt

@Composable
fun VirtualJoystick(
    modifier: Modifier = Modifier,
    size: Dp = 140.dp,
    onMove: (Float, Float) -> Unit
) {
    var knobOffset by remember { mutableStateOf(Offset.Zero) }

    Box(
        modifier = modifier
            .size(size)
            .testTag("virtual_joystick")
            .pointerInput(Unit) {
                val radius = this.size.width / 2f
                detectDragGestures(
                    onDragStart = { offset ->
                        val center = Offset(radius, radius)
                        val delta = offset - center
                        val dist = sqrt(delta.x * delta.x + delta.y * delta.y)
                        if (dist <= radius) {
                            knobOffset = delta
                            onMove(delta.x / radius, delta.y / radius)
                        } else {
                            val nx = delta.x / dist
                            val ny = delta.y / dist
                            knobOffset = Offset(nx * radius, ny * radius)
                            onMove(nx, ny)
                        }
                    },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        val current = knobOffset + dragAmount
                        val dist = sqrt(current.x * current.x + current.y * current.y)
                        if (dist <= radius) {
                            knobOffset = current
                            onMove(current.x / radius, current.y / radius)
                        } else {
                            val nx = current.x / dist
                            val ny = current.y / dist
                            knobOffset = Offset(nx * radius, ny * radius)
                            onMove(nx, ny)
                        }
                    },
                    onDragEnd = {
                        knobOffset = Offset.Zero
                        onMove(0f, 0f)
                    },
                    onDragCancel = {
                        knobOffset = Offset.Zero
                        onMove(0f, 0f)
                    }
                )
            }
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            val center = Offset(this.size.width / 2f, this.size.height / 2f)
            val outerRadius = this.size.width / 2f

            // Outer base
            drawCircle(
                color = Color(0x551E0A3C),
                radius = outerRadius,
                center = center
            )
            drawCircle(
                color = Color(0xAAFF007F),
                radius = outerRadius,
                center = center,
                style = Stroke(width = 3f)
            )

            // Inner crosshair guidelines
            drawLine(
                color = Color(0x33FFFFFF),
                start = Offset(center.x - outerRadius * 0.5f, center.y),
                end = Offset(center.x + outerRadius * 0.5f, center.y),
                strokeWidth = 2f
            )
            drawLine(
                color = Color(0x33FFFFFF),
                start = Offset(center.x, center.y - outerRadius * 0.5f),
                end = Offset(center.x, center.y + outerRadius * 0.5f),
                strokeWidth = 2f
            )

            // Knob
            val knobCenter = center + knobOffset
            drawCircle(
                color = Color(0xFFFF007F),
                radius = 28f,
                center = knobCenter
            )
            drawCircle(
                color = Color(0xFFFFD700),
                radius = 16f,
                center = knobCenter
            )
            drawCircle(
                color = Color.White,
                radius = 6f,
                center = knobCenter - Offset(4f, 4f)
            )
        }
    }
}
