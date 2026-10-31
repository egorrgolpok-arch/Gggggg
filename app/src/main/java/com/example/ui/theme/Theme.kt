package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = NeonGlamPink,
    secondary = MartianGold,
    tertiary = CyberCyan,
    background = SpaceDark,
    surface = SpaceCard,
    surfaceVariant = SpaceSurface,
    onPrimary = TextWhite,
    onSecondary = SpaceDark,
    onTertiary = SpaceDark,
    onBackground = TextWhite,
    onSurface = TextWhite
)

@Composable
fun MyApplicationTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
