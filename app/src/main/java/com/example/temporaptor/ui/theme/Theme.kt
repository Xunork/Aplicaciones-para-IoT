package com.example.temporaptor.ui.theme

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
    primary = EmeraldAccent,
    onPrimary = Color.White,
    primaryContainer = JungleGreenPrimary,
    onPrimaryContainer = Color(0xFFD1FAE5),
    secondary = AmberGlow,
    onSecondary = Color.Black,
    secondaryContainer = ReptileDarkCard,
    tertiary = TempOrange,
    background = ReptileDarkBackground,
    surface = ReptileDarkSurface,
    surfaceVariant = ReptileDarkCard,
    outline = ReptileDarkCardBorder,
    onBackground = Color(0xFFECFDF5),
    onSurface = Color(0xFFECFDF5),
    onSurfaceVariant = Color(0xFFA7F3D0)
)

// Soft, glare-free, eye-friendly light terrarium theme
private val LightColorScheme = lightColorScheme(
    primary = SoftForestGreen,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFD3E8DC),
    onPrimaryContainer = SoftForestGreen,
    secondary = WarmTerracotta,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFFEF3C7),
    tertiary = TempOrange,
    background = SoftSageBackground,
    surface = SoftSageSurface,
    surfaceVariant = SoftSageCard,
    outline = SoftSageBorder,
    onBackground = SoftTextDark,
    onSurface = SoftTextDark,
    onSurfaceVariant = Color(0xFF385E49)
)

@Composable
fun TemporaptorTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Preserve reptilian theme identity
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
