package dev.hirarki.portfolio.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.unit.dp
import dev.hirarki.portfolio.ui.theme.ObsidianColors
import dev.hirarki.portfolio.ui.theme.reducedMotion
import kotlin.math.PI
import kotlin.math.sin
import kotlin.random.Random

private data class FloatingParticle(
    val x: Float,
    val y: Float,
    val radiusDp: Float,
    val speed: Float,
    val swaySpeed: Float,
    val phase: Float,
    val color: Color,
    val alpha: Float
)

@Composable
fun BackgroundCanvas(
    modifier: Modifier = Modifier,
    showParticles: Boolean = true
) {
    val particles = remember { createFloatingParticles(26) }
    val isAnimating = showParticles && !reducedMotion()
    var elapsedSeconds by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(isAnimating) {
        if (!isAnimating) return@LaunchedEffect
        val startNanos = withFrameNanos { it }
        while (true) {
            val frameNanos = withFrameNanos { it }
            elapsedSeconds = (frameNanos - startNanos) / 1_000_000_000f
        }
    }

    Canvas(modifier = modifier.clearAndSetSemantics { }) {
        if (!isAnimating) return@Canvas
        val swayPx = 12.dp.toPx()
        particles.forEach { particle ->
            val normalizedY = ((particle.y - elapsedSeconds * particle.speed) % 1f + 1f) % 1f
            val sway = sin((elapsedSeconds * particle.swaySpeed + particle.phase) * 2f * PI.toFloat()) * swayPx
            val center = Offset(
                x = particle.x * size.width + sway,
                y = normalizedY * size.height
            )
            val radius = particle.radiusDp.dp.toPx()
            val twinkle = 0.7f + 0.3f * sin(elapsedSeconds * 1.2f + particle.phase)
            val alpha = particle.alpha * twinkle
            drawCircle(
                color = particle.color.copy(alpha = alpha * 0.25f),
                radius = radius * 3f,
                center = center
            )
            drawCircle(
                color = particle.color.copy(alpha = alpha),
                radius = radius,
                center = center
            )
        }
    }
}

private fun createFloatingParticles(count: Int): List<FloatingParticle> {
    val random = Random(seed = 20251120L)
    return List(count) { index ->
        FloatingParticle(
            x = random.nextFloat(),
            y = random.nextFloat(),
            radiusDp = 3f + random.nextFloat() * 3f,
            speed = 0.008f + random.nextFloat() * 0.02f,
            swaySpeed = 0.03f + random.nextFloat() * 0.09f,
            phase = random.nextFloat() * 2f * PI.toFloat(),
            color = if (index % 4 == 0) ObsidianColors.Secondary else ObsidianColors.Primary,
            alpha = 0.08f + random.nextFloat() * 0.12f
        )
    }
}
