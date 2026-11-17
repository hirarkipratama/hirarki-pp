package dev.hirarki.portfolio.ui.sections

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.hirarki.portfolio.data.model.ProfileModel
import dev.hirarki.portfolio.ui.components.AsyncImage
import dev.hirarki.portfolio.ui.components.NeonButton
import dev.hirarki.portfolio.ui.components.NeonButtonVariant
import dev.hirarki.portfolio.ui.components.TechPill
import dev.hirarki.portfolio.ui.components.TerminalText
import dev.hirarki.portfolio.ui.theme.ObsidianColors
import dev.hirarki.portfolio.ui.theme.PortfolioTypography

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HeroSection(
    profile: ProfileModel,
    onViewProjectsClick: () -> Unit,
    onDownloadCVClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .drawBehind {
                // Subtle radial gradient glow in top-right: Primary at 5% alpha
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            ObsidianColors.Primary.copy(alpha = 0.08f),
                            Color.Transparent
                        ),
                        center = Offset(size.width * 0.85f, size.height * 0.2f),
                        radius = size.width * 0.45f
                    )
                )
            }
    ) {
        val containerWidth = maxWidth
        val isDesktop = containerWidth >= 1024.dp
        val horizontalPadding = if (containerWidth > 1200.dp) 80.dp else if (containerWidth > 768.dp) 40.dp else 24.dp
        val verticalPadding = if (isDesktop) 120.dp else 60.dp

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = horizontalPadding, vertical = verticalPadding)
        ) {
            if (isDesktop) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Left Column (Content)
                    HeroContentColumn(
                        profile = profile,
                        onViewProjectsClick = onViewProjectsClick,
                        onDownloadCVClick = onDownloadCVClick,
                        modifier = Modifier.weight(1.1f)
                    )

                    Spacer(modifier = Modifier.width(60.dp))

                    // Right Column (Portrait)
                    HeroAvatar(
                        avatarUrl = profile.avatarUrl,
                        sizeDp = 380,
                        modifier = Modifier.weight(0.9f)
                    )
                }
            } else {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(40.dp)
                ) {
                    // Avatar on top on mobile/tablet
                    HeroAvatar(
                        avatarUrl = profile.avatarUrl,
                        sizeDp = if (containerWidth > 600.dp) 280 else 220
                    )

                    // Content below
                    HeroContentColumn(
                        profile = profile,
                        onViewProjectsClick = onViewProjectsClick,
                        onDownloadCVClick = onDownloadCVClick,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun HeroContentColumn(
    profile: ProfileModel,
    onViewProjectsClick: () -> Unit,
    onDownloadCVClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // 1. TerminalText component
        TerminalText(
            texts = listOf(
                "DEV_RUNTIME // READY",
                "FLUTTER 3.x // ACTIVE",
                "KMP // LOADED"
            ),
            modifier = Modifier.padding(bottom = 4.dp)
        )

        // 2. Active status badge
        ActiveStatusBadge()

        // 3. Main headline: "Engineering Scalable\nHigh-Performance\nMobile Experiences"
        val headlineString = buildAnnotatedString {
            append("Engineering Scalable\n")
            // Apply neon cyan/secondary gradient to "High-Performance"
            withStyle(
                SpanStyle(
                    brush = Brush.linearGradient(
                        colors = listOf(ObsidianColors.Primary, ObsidianColors.Secondary)
                    ),
                    fontWeight = FontWeight.Bold
                )
            ) {
                append("High-Performance")
            }
            append("\nMobile Experiences")
        }

        Text(
            text = headlineString,
            style = PortfolioTypography.displayMedium.copy(
                fontWeight = FontWeight.Bold,
                color = ObsidianColors.OnSurface,
                lineHeight = 48.sp
            )
        )

        // 4. Subheadline
        val subhead = if (profile.heroSubheadline.isNotBlank()) profile.heroSubheadline else profile.tagline
        Text(
            text = subhead,
            style = PortfolioTypography.bodyLarge.copy(
                color = ObsidianColors.OnSurfaceDim,
                lineHeight = 26.sp
            ),
            maxLines = 3
        )

        // 5. Stack chips Row (wrapping)
        if (profile.stackChips.isNotEmpty()) {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(top = 8.dp, bottom = 12.dp)
            ) {
                profile.stackChips.forEach { chip ->
                    TechPill(label = chip)
                }
            }
        }

        // 6. CTA Row
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            NeonButton(
                text = "View Featured Projects",
                onClick = onViewProjectsClick,
                variant = NeonButtonVariant.PRIMARY
            )

            NeonButton(
                text = "Download CV",
                onClick = onDownloadCVClick,
                variant = NeonButtonVariant.SECONDARY
            )
        }
    }
}

@Composable
private fun ActiveStatusBadge(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition()
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(900),
            repeatMode = RepeatMode.Reverse
        )
    )

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(50.dp))
            .background(ObsidianColors.Container)
            .border(1.dp, ObsidianColors.Border, RoundedCornerShape(50.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(Color(0xFF00FF66).copy(alpha = pulseAlpha))
        )
        Text(
            text = "Available for opportunities",
            style = PortfolioTypography.labelMedium.copy(color = ObsidianColors.OnSurfaceDim)
        )
    }
}

@Composable
private fun HeroAvatar(
    avatarUrl: String,
    sizeDp: Int,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(sizeDp.dp)
                .drawBehind {
                    drawIntoCanvas { canvas ->
                        val paint = Paint().apply {
                            color = ObsidianColors.GlowCyan.copy(alpha = 0.25f)
                        }
                        canvas.drawCircle(
                            center = Offset(size.width / 2f, size.height / 2f),
                            radius = size.width / 2f + 16.dp.toPx(),
                            paint = paint
                        )
                    }
                }
                .border(2.dp, ObsidianColors.Primary.copy(alpha = 0.8f), CircleShape)
                .clip(CircleShape)
                .background(ObsidianColors.Container)
        ) {
            AsyncImage(
                url = avatarUrl,
                contentDescription = "Hirarki Wendri portrait",
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.Crop,
                fallbackText = "HW"
            )
        }
    }
}
