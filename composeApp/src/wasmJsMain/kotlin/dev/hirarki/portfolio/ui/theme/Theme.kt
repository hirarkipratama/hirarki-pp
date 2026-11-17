package dev.hirarki.portfolio.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    background = ObsidianColors.Background,
    surface = ObsidianColors.Surface,
    surfaceContainer = ObsidianColors.Container,
    surfaceContainerHigh = ObsidianColors.ContainerHigh,
    primary = ObsidianColors.Primary,
    secondary = ObsidianColors.Secondary,
    onBackground = ObsidianColors.OnSurface,
    onSurface = ObsidianColors.OnSurface,
    onSurfaceVariant = ObsidianColors.OnSurfaceDim,
    outline = ObsidianColors.Border
)

@Composable
fun PortfolioTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = PortfolioTypography,
        shapes = PortfolioShapes,
        content = content
    )
}
