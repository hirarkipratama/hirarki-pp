package dev.hirarki.portfolio.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.browser.window
import org.w3c.dom.events.Event

private fun addJsWindowListener(type: String, callback: () -> Unit): () -> Unit =
    js("""
        {
            var handler = function(e) { callback(); };
            window.addEventListener(type, handler);
            return function() { window.removeEventListener(type, handler); };
        }
    """)

enum class WindowSizeClass {
    Compact,
    Medium,
    Expanded
}

data class WindowDimensions(
    val width: Int,
    val height: Int,
    val sizeClass: WindowSizeClass
) {
    val isCompact: Boolean get() = sizeClass == WindowSizeClass.Compact
    val isMedium: Boolean get() = sizeClass == WindowSizeClass.Medium
    val isExpanded: Boolean get() = sizeClass == WindowSizeClass.Expanded
    val isDesktop: Boolean get() = width > 1024
    val isTablet: Boolean get() = width in 768..1024
    val isMobile: Boolean get() = width < 768
}

@Composable
fun rememberWindowDimensions(): WindowDimensions {
    var width by remember { mutableStateOf(window.innerWidth) }
    var height by remember { mutableStateOf(window.innerHeight) }

    DisposableEffect(Unit) {
        val cleanup = addJsWindowListener("resize") {
            width = window.innerWidth
            height = window.innerHeight
        }
        onDispose {
            cleanup()
        }
    }

    val sizeClass = when {
        width < 600 -> WindowSizeClass.Compact
        width < 1200 -> WindowSizeClass.Medium
        else -> WindowSizeClass.Expanded
    }

    return WindowDimensions(width = width, height = height, sizeClass = sizeClass)
}

@Composable
fun rememberWindowSizeClass(): WindowSizeClass {
    return rememberWindowDimensions().sizeClass
}

@Composable
fun responsiveHorizontalPadding(): Dp {
    val windowSize = rememberWindowSizeClass()
    return when (windowSize) {
        WindowSizeClass.Compact -> 20.dp
        WindowSizeClass.Medium -> 48.dp
        WindowSizeClass.Expanded -> 120.dp
    }
}

fun responsiveTextStyle(base: TextStyle, compactScale: Float = 0.75f, isCompact: Boolean = false): TextStyle {
    if (!isCompact) return base
    val scaledFontSize = base.fontSize.value * compactScale
    val scaledLineHeight = if (base.lineHeight != androidx.compose.ui.unit.TextUnit.Unspecified) {
        base.lineHeight.value * compactScale
    } else {
        base.lineHeight.value
    }
    return base.copy(
        fontSize = scaledFontSize.sp,
        lineHeight = if (base.lineHeight != androidx.compose.ui.unit.TextUnit.Unspecified) scaledLineHeight.sp else androidx.compose.ui.unit.TextUnit.Unspecified
    )
}
