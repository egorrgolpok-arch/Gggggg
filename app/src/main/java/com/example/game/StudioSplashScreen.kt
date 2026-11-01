package com.example.game

import androidx.compose.animation.core.Animatable
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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
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
import com.example.ui.theme.MartianGold
import com.example.ui.theme.NeonGlamPink
import com.example.ui.theme.SpaceDark
import kotlinx.coroutines.delay

@Composable
fun StudioSplashScreen(
    onStartGame: () -> Unit
) {
    val screenAlpha = remember { Animatable(0f) }

    // 3.0s total duration with smooth fade-in and fade-out
    LaunchedEffect(Unit) {
        // Fade in: 0f -> 1f in 450ms
        screenAlpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(450, easing = FastOutSlowInEasing)
        )
        // Hold on screen for 2.1s
        delay(2100)
        // Fade out: 1f -> 0f in 450ms
        screenAlpha.animateTo(
            targetValue = 0f,
            animationSpec = tween(450, easing = FastOutSlowInEasing)
        )
        onStartGame()
    }

    val infiniteTransition = rememberInfiniteTransition(label = "studio_splash_pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.98f,
        targetValue = 1.02f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )

    val blinkAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "blink_alpha"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .alpha(screenAlpha.value)
            .clickable { onStartGame() }
            .testTag("splash_screen_container"),
        contentAlignment = Alignment.Center
    ) {
        // Full bleed background artwork from Pride Gays Studio
        Image(
            painter = painterResource(id = R.drawable.bg_splash),
            contentDescription = "Pride Gays Studio Splash Art",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Subtle gradient vignette overlay at the bottom for prompt readability
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Transparent,
                            Color(0x99000000),
                            Color(0xEE0D0221)
                        )
                    )
                )
        )

        // Tap to skip prompt at bottom
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 36.dp, start = 24.dp, end = 24.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .scale(pulseScale)
                    .background(
                        color = Color(0xCC13092A),
                        shape = RoundedCornerShape(24.dp)
                    )
                    .border(
                        width = 1.5.dp,
                        brush = Brush.horizontalGradient(
                            listOf(
                                Color(0xFFFF0055),
                                Color(0xFFFF7F00),
                                Color(0xFFFFD700),
                                Color(0xFF00FF66),
                                Color(0xFF00E5FF),
                                Color(0xFFB5179E)
                            )
                        ),
                        shape = RoundedCornerShape(24.dp)
                    )
                    .padding(horizontal = 24.dp, vertical = 10.dp)
                    .testTag("start_game_button")
            ) {
                Text(
                    text = "▶ НАЖМИТЕ ДЛЯ СТАРТА ◀",
                    color = Color.White.copy(alpha = blinkAlpha),
                    fontWeight = FontWeight.Black,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 14.sp,
                    letterSpacing = 2.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
