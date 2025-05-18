package app.ice.readmanga.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import app.ice.readmanga.ui.theme.theme_colors.limeLightColors
import app.ice.readmanga.ui.theme.theme_colors.limeThemeColors

fun limeTheme(
    isDark: Boolean
): ColorScheme {
    val colors = if (isDark) {
        darkColorScheme(
            primary = limeThemeColors.primary,
            background = limeThemeColors.background,
            secondary = limeThemeColors.secondary,
            surface = limeThemeColors.surface,
            primaryContainer = limeThemeColors.primaryContainer,
            onPrimaryContainer = limeThemeColors.onPrimaryContainer,
            onSurface = limeThemeColors.onSurface,
            onBackground = limeThemeColors.onBackground,
            onPrimary = limeThemeColors.onPrimary,
        )
    } else {
        lightColorScheme(
            primary = limeLightColors.primary,
            primaryContainer = limeThemeColors.primaryContainer,
            onPrimary = limeThemeColors.onPrimary,
        )
    }

    return colors
}