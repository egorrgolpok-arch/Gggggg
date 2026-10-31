package com.example.game

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
import com.example.ui.theme.SpaceDark

@Composable
fun StudioSplashScreen(
    onStartGame: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "studio_splash")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.96f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )

    val blinkAlpha by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "blink_alpha"
    )

    val rainbowBrush = Brush.horizontalGradient(
        colors = listOf(
            Color(0xFFFF0055),
            Color(0xFFFF7F00),
            Color(0xFFFFD700),
            Color(0xFF00FF66),
            Color(0xFF00E5FF),
            Color(0xFFB5179E)
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        SpaceDark,
                        Color(0xFF1E0A3C),
                        Color(0xFF0D0221)
                    )
                )
            )
            .clickable { onStartGame() }
            .testTag("splash_screen_container"),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(24.dp)
        ) {
            // Retro Starfield / Pixel Studio Banner
            Box(
                modifier = Modifier
                    .scale(pulseScale)
                    .background(Color(0xFF13092A), RoundedCornerShape(16.dp))
                    .border(3.dp, rainbowBrush, RoundedCornerShape(16.dp))
                    .padding(horizontal = 28.dp, vertical = 20.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "★ ★ ★",
                        color = MartianGold,
                        fontSize = 18.sp,
                        letterSpacing = 6.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "PRIDE GAYS STUDIO",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        letterSpacing = 2.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "GAMES & LUXURY PRODUCTIONS",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = CyberCyan,
                        letterSpacing = 3.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = "ПРЕДСТАВЛЯЕТ",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFFC084FC),
                letterSpacing = 6.sp
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Game Title
            Text(
                text = "MARS WORLD:\nРОГАЛИК",
                fontSize = 36.sp,
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily.Monospace,
                color = MartianGold,
                textAlign = TextAlign.Center,
                lineHeight = 42.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "800 ЭТАЖЕЙ РОСКОШИ РЭПЕРА ВЛАДИКА",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = NeonGlamPink,
                textAlign = TextAlign.Center,
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "6969 ГИПЕРКАРОВ • БРИЛЛИАНТОВЫЙ БЛИНГ • ОТКРЫТЫЙ МИР",
                fontSize = 11.sp,
                color = Color(0xFF94A3B8),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(54.dp))

            // Press start button
            Box(
                modifier = Modifier
                    .background(
                        color = NeonGlamPink.copy(alpha = blinkAlpha),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .border(2.dp, Color.White, RoundedCornerShape(12.dp))
                    .padding(horizontal = 32.dp, vertical = 14.dp)
                    .testTag("start_game_button")
            ) {
                Text(
                    text = "▶ НАЖМИТЕ ДЛЯ СТАРТА ◀",
                    color = Color.White,
                    fontWeight = FontWeight.Black,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 16.sp,
                    letterSpacing = 2.sp
                )
            }
        }
    }
}
