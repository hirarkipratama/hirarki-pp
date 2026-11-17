package dev.hirarki.portfolio.ui.sections

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.hirarki.portfolio.data.model.SkillsModel
import dev.hirarki.portfolio.ui.components.GlowCard
import dev.hirarki.portfolio.ui.components.SectionHeader
import dev.hirarki.portfolio.ui.components.TechPill
import dev.hirarki.portfolio.ui.theme.ObsidianColors
import dev.hirarki.portfolio.ui.theme.PortfolioTypography
import kotlinx.coroutines.delay

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AboutSection(
    skills: SkillsModel,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 80.dp)
    ) {
        val isDesktop = maxWidth >= 1024.dp
        val horizontalPadding = if (maxWidth > 1200.dp) 80.dp else if (maxWidth > 768.dp) 40.dp else 24.dp

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = horizontalPadding)
        ) {
            if (isDesktop) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    // LEFT side (40% width on desktop)
                    AboutProfileMatrix(
                        modifier = Modifier.weight(0.42f)
                    )

                    Spacer(modifier = Modifier.width(48.dp))

                    // RIGHT side (55% width on desktop)
                    AboutSkillsStack(
                        skills = skills,
                        modifier = Modifier.weight(0.58f)
                    )
                }
            } else {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(48.dp)
                ) {
                    AboutProfileMatrix(modifier = Modifier.fillMaxWidth())
                    AboutSkillsStack(skills = skills, modifier = Modifier.fillMaxWidth())
                }
            }
        }
    }
}

@Composable
private fun AboutProfileMatrix(modifier: Modifier = Modifier) {
    val disciplines = listOf(
        "Pixel-Perfect UI & Micro-Animations" to "Crafting silky fluid 60/120fps component systems with custom Canvas shaders.",
        "Battery, Memory & GPU Optimization" to "Eliminating jank, minimizing draw calls, and preventing background leaks.",
        "Offline-First Synchronizations" to "Resilient local-first caching strategies with conflict resolution pipelines.",
        "Deep Linking & Push Architectures" to "Universal link routing, deferred deep linking, and low-latency push messaging."
    )

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        SectionHeader(
            label = "01.",
            title = "Profile Matrix\n& Core Competencies"
        )

        Text(
            text = "Crafting apps that balance architectural rigor with silky fluid interactions. " +
                    "Every line of code optimized for frame budgets, battery efficiency, and " +
                    "zero-compromise user experiences across iOS and Android.",
            style = PortfolioTypography.bodyLarge.copy(
                color = ObsidianColors.OnSurfaceDim,
                lineHeight = 28.sp
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Discipline rows
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            disciplines.forEach { (title, description) ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    // Small cyan bullet dot
                    Box(
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(ObsidianColors.Primary)
                    )

                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = title,
                            style = PortfolioTypography.titleMedium.copy(
                                color = ObsidianColors.OnSurface,
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                        Text(
                            text = description,
                            style = PortfolioTypography.bodyMedium.copy(
                                color = ObsidianColors.OnSurfaceDim,
                                lineHeight = 20.sp
                            )
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun AboutSkillsStack(
    skills: SkillsModel,
    modifier: Modifier = Modifier
) {
    GlowCard(
        modifier = modifier,
        glowColor = ObsidianColors.GlowCyan.copy(alpha = 0.15f),
        cornerRadius = 16.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(28.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text(
                text = "TECHNICAL ARSENAL",
                style = PortfolioTypography.labelLarge.copy(
                    color = ObsidianColors.Primary,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp
                )
            )

            skills.categories.forEachIndexed { index, category ->
                var isVisible by remember { mutableStateOf(false) }

                LaunchedEffect(Unit) {
                    delay(index * 100L)
                    isVisible = true
                }

                AnimatedVisibility(
                    visible = isVisible,
                    enter = fadeIn() + slideInVertically { it / 2 }
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = category.name,
                            style = PortfolioTypography.labelMedium.copy(
                                color = ObsidianColors.Secondary,
                                fontWeight = FontWeight.SemiBold
                            )
                        )

                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            category.skills.forEach { skill ->
                                TechPill(
                                    label = skill,
                                    color = ObsidianColors.Primary
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
