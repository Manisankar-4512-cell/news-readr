package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = AccentBlue,
    onPrimary = Color.White,
    primaryContainer = EditorialSlate,
    onPrimaryContainer = Color(0xFFDBEAFE),
    secondary = AccentCyan,
    onSecondary = Color.Black,
    secondaryContainer = Color(0xFF0E7490),
    tertiary = AccentAmber,
    onTertiary = Color.Black,
    background = EditorialNavy,
    onBackground = Color(0xFFF8FAFC),
    surface = EditorialSlate,
    onSurface = Color(0xFFF8FAFC),
    surfaceVariant = EditorialCardDark,
    onSurfaceVariant = Color(0xFF94A3B8),
    outline = EditorialBorderDark
)

private val LightColorScheme = lightColorScheme(
    primary = AccentBlue,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFEFF6FF),
    onPrimaryContainer = Color(0xFF1E3A8A),
    secondary = Color(0xFF0284C7),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFE0F2FE),
    tertiary = Color(0xFFD97706),
    onTertiary = Color.White,
    background = Color(0xFFF8FAFC),
    onBackground = EditorialNavy,
    surface = Color.White,
    onSurface = EditorialNavy,
    surfaceVariant = Color(0xFFF1F5F9),
    onSurfaceVariant = Color(0xFF475569),
    outline = EditorialBorderLight
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Use our handcrafted editorial color scheme by default for a cohesive editorial look
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
