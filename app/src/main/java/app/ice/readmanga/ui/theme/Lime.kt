package app.ice.readmanga.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import app.ice.readmanga.ui.theme.theme_colors.limeThemeColors

@Composable
fun Lime(
    isDark: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (isDark) {
        darkColorScheme(
            primary = limeThemeColors.primary,
            background = limeThemeColors.background,
            secondary = limeThemeColors.secondary,
            surface = limeThemeColors.surface,
            primaryContainer = limeThemeColors.primaryContainer,
            onPrimaryContainer = limeThemeColors.onPrimaryContainer,

        )
    } else {
        lightColorScheme(
            primary = limeThemeColors.primary,
            background = limeThemeColors.background,
            secondary = limeThemeColors.secondary,
            surface = limeThemeColors.surface,
            primaryContainer = limeThemeColors.primaryContainer,
            onPrimaryContainer = limeThemeColors.onPrimaryContainer,
        )
    }

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content
    )
}