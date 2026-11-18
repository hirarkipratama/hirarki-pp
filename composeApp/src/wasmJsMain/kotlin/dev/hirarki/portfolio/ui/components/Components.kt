package dev.hirarki.portfolio.ui.components

import androidx.compose.animation.core.EaseOutCubic
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.hirarki.portfolio.ui.theme.ObsidianColors
import dev.hirarki.portfolio.ui.theme.PortfolioTypography
import dev.hirarki.portfolio.ui.theme.accessibleFocusRing
import dev.hirarki.portfolio.ui.theme.reducedMotion
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

// -------------------------------------------------------------------------
// 1. GlowCard — Card container with neon glow
// -------------------------------------------------------------------------
@Composable
fun GlowCard(
    modifier: Modifier = Modifier,
    glowColor: Color = ObsidianColors.GlowCyan,
    borderColor: Color = ObsidianColors.Border,
    cornerRadius: Dp = 16.dp,
    content: @Composable BoxScope.() -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    val reducedMotion = reducedMotion()

    val glowAlphaMultiplier by animateFloatAsState(
        targetValue = if (isHovered) 2.0f else 1.0f,
        animationSpec = if (reducedMotion) snap() else tween(durationMillis = 200)
    )

    val scale by animateFloatAsState(
        targetValue = if (isHovered && !reducedMotion) 1.015f else 1.0f,
        animationSpec = if (reducedMotion) {
            snap()
        } else {
            spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        }
    )

    Surface(
        modifier = modifier
            .hoverable(interactionSource)
            .scale(scale)
            .drawBehind {
                drawIntoCanvas { canvas ->
                    val paint = Paint()
                    canvas.drawRoundRect(
                        left = 0f,
                        top = 0f,
                        right = size.width,
                        bottom = size.height,
                        radiusX = cornerRadius.toPx(),
                        radiusY = cornerRadius.toPx(),
                        paint = paint.apply {
                            color = glowColor.copy(alpha = (glowColor.alpha * glowAlphaMultiplier).coerceAtMost(1f))
                        }
                    )
                }
            },
        shape = RoundedCornerShape(cornerRadius),
        color = ObsidianColors.Container,
        border = BorderStroke(1.dp, if (isHovered) glowColor.copy(alpha = 0.6f) else borderColor)
    ) {
        Box {
            content()
        }
    }
}

// -------------------------------------------------------------------------
// 2. NeonButton — CTA button in Primary and Secondary variants
// -------------------------------------------------------------------------
enum class NeonButtonVariant {
    PRIMARY,
    SECONDARY
}

@Composable
fun NeonButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: NeonButtonVariant = NeonButtonVariant.PRIMARY,
    icon: (@Composable () -> Unit)? = null
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    val isPressed by interactionSource.collectIsPressedAsState()
    val reducedMotion = reducedMotion()

    val scale by animateFloatAsState(
        targetValue = when {
            isPressed && !reducedMotion -> 0.96f
            isHovered && !reducedMotion -> 1.03f
            else -> 1.0f
        },
        animationSpec = if (reducedMotion) {
            snap()
        } else {
            spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessMedium
            )
        }
    )

    val shape = RoundedCornerShape(percent = 50)

    val (bgColor, textColor, borderStroke) = when (variant) {
        NeonButtonVariant.PRIMARY -> {
            val bg = when {
                isPressed -> ObsidianColors.Primary
                isHovered -> ObsidianColors.PrimaryDim
                else -> ObsidianColors.Primary
            }
            Triple(bg, Color.Black, null)
        }
        NeonButtonVariant.SECONDARY -> {
            val bg = when {
                isPressed -> ObsidianColors.Primary.copy(alpha = 0.25f)
                isHovered -> ObsidianColors.GlowCyan
                else -> Color.Transparent
            }
            Triple(bg, ObsidianColors.Primary, BorderStroke(1.dp, ObsidianColors.Primary))
        }
    }

    Surface(
        onClick = onClick,
        modifier = modifier
            .accessibleFocusRing(cornerRadius = 20.dp)
            .scale(scale)
            .clip(shape)
            .semantics(mergeDescendants = true) {
                role = Role.Button
                contentDescription = text
            },
        shape = shape,
        color = bgColor,
        border = borderStroke,
        interactionSource = interactionSource
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (icon != null) {
                icon()
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                text = text,
                style = PortfolioTypography.labelLarge.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = textColor
                )
            )
        }
    }
}

// -------------------------------------------------------------------------
// 3. TechPill — Technology label chip
// -------------------------------------------------------------------------
@Composable
fun TechPill(
    label: String,
    modifier: Modifier = Modifier,
    color: Color = ObsidianColors.Primary
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(50.dp))
            .background(ObsidianColors.Container)
            .border(1.dp, color, RoundedCornerShape(50.dp))
            .padding(horizontal = 12.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            style = PortfolioTypography.labelMedium.copy(color = color)
        )
    }
}

// -------------------------------------------------------------------------
// 4. SectionHeader — Section title with monospace label
// -------------------------------------------------------------------------
@Composable
fun SectionHeader(
    label: String,
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = label,
            style = PortfolioTypography.labelLarge.copy(color = ObsidianColors.Primary)
        )
        Text(
            text = title,
            style = PortfolioTypography.headlineLarge.copy(color = ObsidianColors.OnSurface),
            modifier = Modifier.semantics(mergeDescendants = true) { heading() }
        )
        if (!subtitle.isNullOrBlank()) {
            Text(
                text = subtitle,
                style = PortfolioTypography.bodyMedium.copy(color = ObsidianColors.OnSurfaceDim)
            )
        }
    }
}

// -------------------------------------------------------------------------
// 5. AnimatedCounter — Animated number counter
// -------------------------------------------------------------------------
@Composable
fun AnimatedCounter(
    targetValue: Double,
    suffix: String,
    label: String,
    modifier: Modifier = Modifier,
    durationMs: Int = 2000
) {
    var started by remember { mutableStateOf(false) }
    val reducedMotion = reducedMotion()

    LaunchedEffect(Unit) {
        started = true
    }

    val animatedValue by animateFloatAsState(
        targetValue = if (started || reducedMotion) targetValue.toFloat() else 0f,
        animationSpec = if (reducedMotion) {
            snap()
        } else {
            tween(
                durationMillis = durationMs,
                easing = EaseOutCubic
            )
        }
    )

    val displayValue = if (reducedMotion) targetValue else animatedValue.toDouble()
    val formattedNumber = if (targetValue % 1.0 == 0.0) {
        displayValue.roundToInt().toString()
    } else {
        val rounded = (displayValue * 10).roundToInt() / 10.0
        rounded.toString()
    }

    Column(
        modifier = modifier.semantics(mergeDescendants = true) {
            contentDescription = "$formattedNumber$suffix $label"
        },
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "$formattedNumber$suffix",
            style = PortfolioTypography.displayMedium.copy(color = ObsidianColors.Primary)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            style = PortfolioTypography.labelMedium.copy(color = ObsidianColors.OnSurfaceDim)
        )
    }
}

// -------------------------------------------------------------------------
// 6. TerminalText — Typewriter animated text
// -------------------------------------------------------------------------
@Composable
fun TerminalText(
    texts: List<String>,
    modifier: Modifier = Modifier,
    typingSpeedMs: Int = 80,
    cursorBlinkSpeedMs: Int = 500
) {
    var currentTextIndex by remember { mutableStateOf(0) }
    var displayedChars by remember { mutableStateOf(0) }
    var isDeleting by remember { mutableStateOf(false) }
    var cursorVisible by remember { mutableStateOf(true) }
    val reducedMotion = reducedMotion()

    if (!reducedMotion) {
        LaunchedEffect(texts) {
            if (texts.isEmpty()) return@LaunchedEffect
            while (true) {
                val fullText = texts[currentTextIndex]
                if (!isDeleting) {
                    if (displayedChars < fullText.length) {
                        displayedChars++
                        delay(typingSpeedMs.toLong())
                    } else {
                        delay(2000L)
                        isDeleting = true
                    }
                } else {
                    if (displayedChars > 0) {
                        displayedChars--
                        delay((typingSpeedMs / 2).toLong())
                    } else {
                        isDeleting = false
                        currentTextIndex = (currentTextIndex + 1) % texts.size
                        delay(400L)
                    }
                }
            }
        }

        LaunchedEffect(Unit) {
            while (true) {
                cursorVisible = !cursorVisible
                delay(cursorBlinkSpeedMs.toLong())
            }
        }
    }

    val currentText = texts.getOrNull(currentTextIndex) ?: ""
    val visibleText = if (reducedMotion) currentText else currentText.take(displayedChars)

    Text(
        text = visibleText + if (reducedMotion || cursorVisible) "█" else " ",
        style = PortfolioTypography.labelLarge.copy(
            color = ObsidianColors.Primary,
            fontSize = 16.sp
        ),
        modifier = modifier
    )
}
