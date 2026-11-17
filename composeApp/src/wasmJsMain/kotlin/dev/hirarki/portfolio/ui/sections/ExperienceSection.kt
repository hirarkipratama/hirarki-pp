package dev.hirarki.portfolio.ui.sections

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.hirarki.portfolio.data.model.ExperienceModel
import dev.hirarki.portfolio.ui.components.GlowCard
import dev.hirarki.portfolio.ui.components.SectionHeader
import dev.hirarki.portfolio.ui.components.parseHexColor
import dev.hirarki.portfolio.ui.theme.JetBrainsMonoFamily
import dev.hirarki.portfolio.ui.theme.ObsidianColors
import dev.hirarki.portfolio.ui.theme.PortfolioTypography
import kotlinx.coroutines.delay

@Composable
fun ExperienceSection(
    experiences: List<ExperienceModel>,
    modifier: Modifier = Modifier
) {
    if (experiences.isEmpty()) return

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 80.dp)
    ) {
        val isDesktop = maxWidth >= 1024.dp
        val horizontalPadding = if (maxWidth > 1200.dp) 120.dp else if (maxWidth > 600.dp) 48.dp else 20.dp

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = horizontalPadding),
            verticalArrangement = Arrangement.spacedBy(48.dp)
        ) {
            SectionHeader(
                label = "04.",
                title = "Career Journey &\nProduction Track Record"
            )

            // Timeline container
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(32.dp)
            ) {
                experiences.forEachIndexed { index, exp ->
                    TimelineItem(
                        experience = exp,
                        index = index,
                        isDesktop = isDesktop
                    )
                }
            }
        }
    }
}

@Composable
private fun TimelineItem(
    experience: ExperienceModel,
    index: Int,
    isDesktop: Boolean
) {
    val accentColor = parseHexColor(experience.accentColor)
    val isEven = index % 2 == 0

    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(index * 120L)
        visible = true
    }

    if (isDesktop) {
        // Desktop: Row with [LeftContent | TimelineNode | RightContent]
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isEven) {
                // Card on left
                AnimatedVisibility(
                    visible = visible,
                    enter = fadeIn(tween(600)) + slideInHorizontally(tween(600)) { -it / 2 },
                    modifier = Modifier.weight(1f)
                ) {
                    ExperienceCard(experience = experience, accentColor = accentColor)
                }
                TimelineNode(
                    current = experience.current,
                    accentColor = accentColor,
                    modifier = Modifier.width(60.dp)
                )
                Spacer(modifier = Modifier.weight(1f))
            } else {
                // Card on right
                Spacer(modifier = Modifier.weight(1f))
                TimelineNode(
                    current = experience.current,
                    accentColor = accentColor,
                    modifier = Modifier.width(60.dp)
                )
                AnimatedVisibility(
                    visible = visible,
                    enter = fadeIn(tween(600)) + slideInHorizontally(tween(600)) { it / 2 },
                    modifier = Modifier.weight(1f)
                ) {
                    ExperienceCard(experience = experience, accentColor = accentColor)
                }
            }
        }
    } else {
        // Mobile: Node on left, Card on right
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TimelineNode(
                current = experience.current,
                accentColor = accentColor,
                modifier = Modifier.width(32.dp)
            )
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(tween(600)) + slideInHorizontally(tween(600)) { it / 2 },
                modifier = Modifier.weight(1f)
            ) {
                ExperienceCard(experience = experience, accentColor = accentColor)
            }
        }
    }
}

@Composable
private fun TimelineNode(
    current: Boolean,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition()
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200),
            repeatMode = RepeatMode.Reverse
        )
    )

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.size(24.dp),
            contentAlignment = Alignment.Center
        ) {
            if (current) {
                // Pulsing ring
                Box(
                    modifier = Modifier
                        .size(16.dp * pulseScale)
                        .clip(CircleShape)
                        .background(accentColor.copy(alpha = 0.25f))
                )
            }
            // Center filled circle
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .clip(CircleShape)
                    .background(accentColor)
                    .border(2.dp, ObsidianColors.Background, CircleShape)
            )
        }
    }
}

@Composable
private fun ExperienceCard(
    experience: ExperienceModel,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    GlowCard(
        modifier = modifier.fillMaxWidth(),
        glowColor = accentColor.copy(alpha = 0.15f),
        borderColor = if (experience.current) accentColor.copy(alpha = 0.4f) else ObsidianColors.Border,
        cornerRadius = 16.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Period + Current Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = experience.period,
                    style = PortfolioTypography.labelLarge.copy(
                        fontFamily = JetBrainsMonoFamily,
                        color = accentColor,
                        fontWeight = FontWeight.SemiBold
                    )
                )

                if (experience.current) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50.dp))
                            .background(ObsidianColors.Secondary)
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "CURRENT",
                            style = PortfolioTypography.labelSmall.copy(
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            }

            // Role
            Text(
                text = experience.role,
                style = PortfolioTypography.titleLarge.copy(
                    color = ObsidianColors.OnSurface,
                    fontWeight = FontWeight.Bold
                )
            )

            // Company & Type
            Text(
                text = "${experience.company} | ${experience.companyType}",
                style = PortfolioTypography.bodyMedium.copy(color = ObsidianColors.OnSurfaceDim)
            )

            // Description
            if (experience.description.isNotBlank()) {
                Text(
                    text = experience.description,
                    style = PortfolioTypography.bodyMedium.copy(
                        color = ObsidianColors.OnSurfaceDim,
                        lineHeight = 22.sp
                    ),
                    maxLines = 4
                )
            }

            // Achievements bullet list
            if (experience.achievements.isNotEmpty()) {
                Column(
                    modifier = Modifier.padding(top = 4.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    experience.achievements.forEach { achievement ->
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Box(
                                modifier = Modifier
                                    .padding(top = 7.dp)
                                    .size(5.dp)
                                    .clip(CircleShape)
                                    .background(accentColor)
                            )
                            Text(
                                text = achievement,
                                style = PortfolioTypography.bodyMedium.copy(
                                    color = ObsidianColors.OnSurface,
                                    lineHeight = 20.sp
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}
