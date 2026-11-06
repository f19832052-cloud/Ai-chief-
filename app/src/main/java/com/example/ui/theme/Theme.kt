package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = ForestGreen,
    onPrimary = Color.White,
    primaryContainer = SoftSage,
    onPrimaryContainer = ForestGreenDark,

    secondary = ForestGreenLight,
    onSecondary = Color.White,
    secondaryContainer = SoftSageLight,
    onSecondaryContainer = ForestGreenDark,

    tertiary = FreshLimeDark,
    onTertiary = Charcoal,
    tertiaryContainer = FreshLimeContainer,
    onTertiaryContainer = Charcoal,

    background = WarmIvory,
    onBackground = Charcoal,

    surface = CardWhite,
    onSurface = Charcoal,
    surfaceVariant = SurfaceVariantLight,
    onSurfaceVariant = CharcoalMuted,

    outline = SoftSageBorder,
    outlineVariant = Color(0xFFE5EBE6),

    error = DangerRed,
    onError = Color.White,
    errorContainer = DangerRedContainer,
    onErrorContainer = DangerRed
)

private val DarkColorScheme = darkColorScheme(
    primary = FreshLime,
    onPrimary = Charcoal,
    primaryContainer = ForestGreenDark,
    onPrimaryContainer = FreshLimeContainer,

    secondary = SoftSage,
    onSecondary = ForestGreenDark,
    secondaryContainer = ForestGreenLight,
    onSecondaryContainer = Color.White,

    tertiary = FreshLimeDark,
    onTertiary = Charcoal,
    tertiaryContainer = ForestGreenDark,
    onTertiaryContainer = FreshLimeContainer,

    background = Color(0xFF141A16),
    onBackground = Color(0xFFF2F4F1),

    surface = Color(0xFF1B231F),
    onSurface = Color(0xFFF2F4F1),
    surfaceVariant = Color(0xFF232D28),
    onSurfaceVariant = Color(0xFFBAC2BB),

    outline = Color(0xFF38463E),
    outlineVariant = Color(0xFF2C3730),

    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005)
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Keep intentional brand identity as per design spec
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}
