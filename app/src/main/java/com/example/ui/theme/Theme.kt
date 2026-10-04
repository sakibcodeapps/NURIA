package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = IslamicEmerald,
    onPrimary = Color.White,
    primaryContainer = IslamicMintContainer,
    onPrimaryContainer = IslamicOnMintContainer,
    secondary = ChampagneGold,
    onSecondary = Color.White,
    secondaryContainer = GoldContainer,
    onSecondaryContainer = GoldOnContainer,
    tertiary = IslamicEmeraldDark,
    onTertiary = Color.White,
    background = IvoryBackground,
    onBackground = TextPrimaryLight,
    surface = IvorySurface,
    onSurface = TextPrimaryLight,
    surfaceVariant = IvorySurfaceVariant,
    onSurfaceVariant = TextSecondaryLight,
    outline = IvoryOutline
)

private val DarkColorScheme = darkColorScheme(
    primary = IslamicEmeraldLight,
    onPrimary = Color.White,
    primaryContainer = IslamicEmeraldDark,
    onPrimaryContainer = IslamicMintContainer,
    secondary = SoftGold,
    onSecondary = Color.Black,
    secondaryContainer = NightSurfaceVariant,
    onSecondaryContainer = GoldContainer,
    tertiary = ChampagneGold,
    onTertiary = Color.Black,
    background = NightBackground,
    onBackground = TextPrimaryDark,
    surface = NightSurface,
    onSurface = TextPrimaryDark,
    surfaceVariant = NightSurfaceVariant,
    onSurfaceVariant = TextSecondaryDark,
    outline = NightOutline
)

@Composable
fun NuriaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
