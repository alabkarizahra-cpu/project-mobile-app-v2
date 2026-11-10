
package com.example.project_mobile_app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LocalLensColorScheme = lightColorScheme(
    primary = LocalLensGreen,
    onPrimary = LocalLensWhite,

    secondary = LocalLensBeige,
    onSecondary = LocalLensBlack,


    background = LocalLensBeige,
    onBackground = LocalLensBlack,

    surface = LocalLensWhite,
    onSurface = LocalLensBlack,


    primaryContainer = LocalLensLightGreen,
    onPrimaryContainer = LocalLensGreen,

    surfaceVariant = LocalLensBeige,
    onSurfaceVariant = LocalLensGray
)

@Composable
fun ProjectmobileappTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LocalLensColorScheme,
        typography = Typography,
        content = content
    )
}
