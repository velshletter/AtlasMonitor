package com.velshletter.atlasmonitor.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    surface = ThemeColors.Night.surface,
    onSurface = ThemeColors.Night.onSurface,
    primary = ThemeColors.Night.accentColor,
    onPrimary = ThemeColors.Night.onAccentColor,
    secondary = ThemeColors.Night.secondary,
    onSecondary = ThemeColors.Night.onSecondary,
    error = ThemeColors.Night.error,
    tertiary = ThemeColors.Night.tertiary,
    onTertiary = ThemeColors.Night.onTertiary,
    primaryContainer = ThemeColors.Night.accentColor.copy(alpha = 0.7f),
    onPrimaryContainer = ThemeColors.Night.onAccentColor.copy(alpha = 0.7f),
    inversePrimary = ThemeColors.Night.accentColor.copy(alpha = 0.8f),
    secondaryContainer = ThemeColors.Night.secondary.copy(alpha = 0.7f),
    onSecondaryContainer = ThemeColors.Night.onSecondary.copy(alpha = 0.7f),
    tertiaryContainer = ThemeColors.Night.tertiary.copy(alpha = 0.7f),
    onTertiaryContainer = ThemeColors.Night.onTertiary.copy(alpha = 0.7f),
//    background = ThemeColors.Night.surface,
    onBackground = ThemeColors.Night.onSurface,
    surfaceVariant = ThemeColors.Night.surface,
    onSurfaceVariant = ThemeColors.Night.onSurface.copy(alpha = 0.5f),
    surfaceTint = ThemeColors.Night.accentColor,
    inverseSurface = ThemeColors.Night.onSurface.copy(alpha = 0.2f),
    inverseOnSurface = ThemeColors.Night.surface,
    errorContainer = ThemeColors.Night.error.copy(alpha = 0.7f),
    onErrorContainer = ThemeColors.Night.onSurface,
    outline = ThemeColors.Night.onSurface.copy(alpha = 0.5f),
    outlineVariant = ThemeColors.Night.onSurface.copy(alpha = 0.5f),
    scrim = ThemeColors.Night.onSurface.copy(alpha = 0.32f),
    surfaceBright = ThemeColors.Night.surface.copy(alpha = 0.6f),
    surfaceContainer = ThemeColors.Night.surface.copy(alpha = 0.95f),
    surfaceContainerHigh = ThemeColors.Night.surface.copy(alpha = 0.9f),
    surfaceContainerHighest = ThemeColors.Night.surface.copy(alpha = 0.85f),
    surfaceContainerLow = ThemeColors.Night.surface.copy(alpha = 0.8f),
    surfaceContainerLowest = ThemeColors.Night.surface.copy(alpha = 0.75f),
    surfaceDim = ThemeColors.Night.surface.copy(alpha = 0.6f)
)

private val LightColorScheme = lightColorScheme(
    surface = ThemeColors.Day.surface,
    onSurface = ThemeColors.Day.onSurface,
    primary = ThemeColors.Day.accentColor,
    onPrimary = ThemeColors.Day.onAccentColor,
    secondary = ThemeColors.Day.secondary,
    onSecondary = ThemeColors.Day.onSecondary,
    error = ThemeColors.Day.error,
    tertiary = ThemeColors.Day.tertiary,
    onTertiary = ThemeColors.Day.onTertiary,
    primaryContainer = ThemeColors.Day.accentColor.copy(alpha = 0.7f),
    onPrimaryContainer = ThemeColors.Day.onAccentColor.copy(alpha = 0.7f),
    inversePrimary = ThemeColors.Day.accentColor.copy(alpha = 0.8f),
    secondaryContainer = ThemeColors.Day.secondary.copy(alpha = 0.7f),
    onSecondaryContainer = ThemeColors.Day.onSecondary.copy(alpha = 0.7f),
    tertiaryContainer = ThemeColors.Day.tertiary.copy(alpha = 0.7f),
    onTertiaryContainer = ThemeColors.Day.onTertiary.copy(alpha = 0.7f),
//    background = ThemeColors.Day.surface,
    onBackground = ThemeColors.Day.onSurface,
//    surfaceVariant = ThemeColors.Day.surface.copy(alpha = 0.2f),
//    onSurfaceVariant = ThemeColors.Day.onSurface.copy(alpha = 0.2f),
    surfaceTint = ThemeColors.Day.accentColor,
//    inverseSurface = ThemeColors.Day.onSurface.copy(alpha = 0.2f),
//    inverseOnSurface = ThemeColors.Day.surface.copy(alpha = 0.2f),
    errorContainer = ThemeColors.Day.error.copy(alpha = 0.7f),
    onErrorContainer = ThemeColors.Day.onSurface,
    outline = ThemeColors.Day.onSurface.copy(alpha = 0.5f),
    outlineVariant = ThemeColors.Day.onSurface.copy(alpha = 0.5f),
    scrim = ThemeColors.Day.onSurface.copy(alpha = 0.32f),
    surfaceBright = ThemeColors.Day.surface.copy(alpha = 0.6f),
    surfaceContainer = ThemeColors.Day.surface.copy(alpha = 0.95f),
    surfaceContainerHigh = ThemeColors.Day.surface.copy(alpha = 0.9f),
    surfaceContainerHighest = ThemeColors.Day.surface.copy(alpha = 0.85f),
    surfaceContainerLow = ThemeColors.Day.surface.copy(alpha = 0.8f),
    surfaceContainerLowest = ThemeColors.Day.surface.copy(alpha = 0.75f),
    surfaceDim = ThemeColors.Day.surface.copy(alpha = 0.6f)
)

@Composable
fun AtlasMonitorTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}