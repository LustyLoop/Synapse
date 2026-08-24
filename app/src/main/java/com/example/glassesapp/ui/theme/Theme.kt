package com.example.glassesapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color


private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF20808D),
    onPrimary = Color.White,
    background = Color(0xFFF8F9FA),
    inverseSurface = Color(0xFF121212),
    surface = Color(0xFFE9ECEF),
    onSurface = Color(0xFF121212),
    surfaceVariant = Color(0xFFE8F3FE),
    onSurfaceVariant = Color(0xFF495057)
)

private val DarkColorScheme = darkColorScheme(
    primary = SorfBlueColor,
    onPrimary = Color.Black,
    background = DarkBackgroundColor,
    inverseSurface = Color.White,
    surface = GreyColor,
    onSurface = Color(0xFFFFFFFF),
    surfaceVariant = UserMessageBackgroundColorDark,
    onSurfaceVariant = GreyForTextFieldColor
)

@Composable
fun MyAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = CustomTypography,
        content = content
    )
}
