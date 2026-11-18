package dev.hirarki.portfolio.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

val LocalReducedMotion = compositionLocalOf { false }

@Composable
fun rememberPrefersReducedMotion(): Boolean {
    var reduced by remember { mutableStateOf(prefersReducedMotionJs()) }

    DisposableEffect(Unit) {
        val cleanup = addJsMediaQueryListener("(prefers-reduced-motion: reduce)") {
            reduced = prefersReducedMotionJs()
        }
        onDispose { cleanup() }
    }

    return reduced
}

@Composable
fun reducedMotion(): Boolean = LocalReducedMotion.current

fun Modifier.activateOnEnterOrSpace(onActivate: () -> Unit): Modifier = onKeyEvent { event ->
    if (event.type == KeyEventType.KeyUp && (event.key == Key.Enter || event.key == Key.Spacebar)) {
        onActivate()
        true
    } else {
        false
    }
}

@Composable
fun Modifier.accessibleFocusRing(cornerRadius: Dp = 4.dp): Modifier {
    var isFocused by remember { mutableStateOf(false) }
    val ringColor = ObsidianColors.Primary

    return this
        .onFocusChanged { focusState ->
            isFocused = focusState.isFocused || focusState.hasFocus
        }
        .drawBehind {
            if (!isFocused) return@drawBehind
            val offset = 2.dp.toPx()
            val strokeWidth = 2.dp.toPx()
            drawRoundRect(
                color = ringColor,
                topLeft = Offset(-offset, -offset),
                size = Size(size.width + offset * 2, size.height + offset * 2),
                cornerRadius = CornerRadius(cornerRadius.toPx()),
                style = Stroke(width = strokeWidth)
            )
        }
}

private fun prefersReducedMotionJs(): Boolean =
    js("!!(window.matchMedia && window.matchMedia('(prefers-reduced-motion: reduce)').matches)")

private fun addJsMediaQueryListener(query: String, callback: () -> Unit): () -> Unit =
    js(
        """
        {
            var media = window.matchMedia(query);
            var handler = function() { callback(); };
            if (media.addEventListener) {
                media.addEventListener('change', handler);
                return function() { media.removeEventListener('change', handler); };
            }
            media.addListener(handler);
            return function() { media.removeListener(handler); };
        }
        """
    )
