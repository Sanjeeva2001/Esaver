package com.example.esaver.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.Typography as MaterialTypography
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = EcoGreenDark,
    onPrimary = Color.White,
    primaryContainer = EcoGreenPale,
    onPrimaryContainer = EcoGreenDark,
    background = EcoBackground,
    onBackground = Color.Black,
    surface = Color.White,
    onSurface = Color.Black
)

private val Typography = MaterialTypography()

@Composable
fun EsaverTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}

