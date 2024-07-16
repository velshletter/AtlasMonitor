package com.velshletter.atlasmonitor.ui.theme

import androidx.compose.ui.graphics.Color

object ThemeColors {
    object Night {
        val surface = Color.DarkGray
        val onSurface = Color(0xFFFFFFFF)
        val accentColor = Color.Blue
        val onAccentColor = Color.White
        val secondary = Color(0xff0360da)
        val onSecondary = Color(0xFF000000)
        val error = Color(0xFFCF6679)
        val tertiary = Color(0xff0024b3)
        val onTertiary = Color(0xFFFFFFFF)
    }

    object Day {
        val surface = Color(0xffffffff)
        val onSurface = Color(0xFF000000)
        val accentColor = Color.Blue
        val onAccentColor = Color(0xFFFFFFFF)
        val secondary = Color(0xff030eda)
        val onSecondary = Color(0xFF000000)
        val error = Color(0xFFB00020)
        val tertiary = Color(0xff0009b3)
        val onTertiary = Color(0xFFFFFFFF)
    }
}