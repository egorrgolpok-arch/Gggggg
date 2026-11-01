package com.example.game

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.GoldCoin
import com.example.ui.theme.HotPink

@Composable
fun CurrencyFloatingBadges(
    deltas: List<CurrencyDelta>,
    modifier: Modifier = Modifier
) {
    val now = System.currentTimeMillis()
    // Show recent deltas from last 2 seconds
    val activeDeltas = remember(deltas) {
        deltas.filter { now - it.timestamp < 2200 }.takeLast(4)
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.End
    ) {
        activeDeltas.forEach { delta ->
            SingleDeltaBadge(delta = delta)
        }
    }
}

@Composable
private fun SingleDeltaBadge(delta: CurrencyDelta) {
    val animOffsetY = remember { Animatable(0f) }
    val animAlpha = remember { Animatable(1f) }

    LaunchedEffect(delta.id) {
        animOffsetY.animateTo(
            targetValue = -35f,
            animationSpec = tween(1800, easing = FastOutSlowInEasing)
        )
    }

    LaunchedEffect(delta.id) {
        kotlinx.coroutines.delay(1000)
        animAlpha.animateTo(
            targetValue = 0f,
            animationSpec = tween(800)
        )
    }

    val isDeduction = delta.amount < 0
    val icon = when (delta.type) {
        "GOLD" -> "💰"
        "DIAMONDS" -> "💎"
        "BATTERIES" -> "🔋"
        else -> "🪙"
    }

    val textColor = when {
        isDeduction -> Color(0xFFFF453A) // bright red
        delta.type == "DIAMONDS" -> CyberCyan
        delta.type == "BATTERIES" -> Color(0xFF10B981)
        else -> GoldCoin
    }

    val sign = if (delta.amount > 0) "+" else ""

    Box(
        modifier = Modifier
            .offset(y = animOffsetY.value.dp)
            .alpha(animAlpha.value)
            .background(
                color = if (isDeduction) Color(0xDD3B0712) else Color(0xDD052E16),
                shape = RoundedCornerShape(8.dp)
            )
            .border(
                1.dp,
                if (isDeduction) Color(0xFFFF453A).copy(alpha = 0.8f) else Color(0xFF10B981).copy(alpha = 0.8f),
                RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 8.dp, vertical = 3.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(icon, fontSize = 12.sp)
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "$sign${delta.amount}",
                color = textColor,
                fontSize = 12.sp,
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily.Monospace
            )
        }
    }
}
