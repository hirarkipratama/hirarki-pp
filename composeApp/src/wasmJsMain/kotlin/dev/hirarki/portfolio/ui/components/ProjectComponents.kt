package dev.hirarki.portfolio.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.paneTitle
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.hirarki.portfolio.data.model.ProjectModel
import dev.hirarki.portfolio.data.model.ScreenshotModel
import dev.hirarki.portfolio.ui.theme.ObsidianColors
import dev.hirarki.portfolio.ui.theme.PortfolioTypography
import kotlinx.browser.window

// Helper to parse hex colors
fun parseHexColor(hex: String, defaultColor: Color = ObsidianColors.Primary): Color {
    return try {
        val cleanHex = hex.removePrefix("#")
        val colorInt = cleanHex.toLong(16)
        if (cleanHex.length == 6) {
            Color(colorInt or 0x00000000FF000000)
        } else {
            Color(colorInt)
        }
    } catch (e: Exception) {
        defaultColor
    }
}

// -------------------------------------------------------------------------
// Single Portrait Screenshot Composable
// -------------------------------------------------------------------------
@Composable
fun ProjectScreenshot(
    screenshot: ScreenshotModel,
    modifier: Modifier = Modifier,
    aspectRatio: Float = 9f / 19.5f // Standard portrait mobile aspect ratio
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(ObsidianColors.ContainerMid)
    ) {
        AsyncImage(
            url = screenshot.image,
            contentDescription = screenshot.alt.ifEmpty { "Project screenshot" },
            contentScale = ContentScale.Fit,
            modifier = Modifier.fillMaxSize(),
            fallbackText = screenshot.caption ?: "Screenshot"
        )
    }
}

// -------------------------------------------------------------------------
// Screenshot Gallery Composable
// -------------------------------------------------------------------------
@Composable
fun ScreenshotGallery(
    screenshots: List<ScreenshotModel>,
    modifier: Modifier = Modifier,
    maxScreenshotsDesktop: Int = 3,
    screenshotHeight: Int = 380
) {
    if (screenshots.isEmpty()) return

    if (screenshots.size == 1) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(screenshotHeight.dp),
            contentAlignment = Alignment.Center
        ) {
            ProjectScreenshot(
                screenshot = screenshots.first(),
                modifier = Modifier
                    .fillMaxHeight()
                    .width((screenshotHeight * 9 / 19.5f).dp)
            )
        }
        return
    }

    BoxWithConstraints(modifier = modifier.fillMaxWidth()) {
        val isMobile = maxWidth < 768.dp

        if (!isMobile) {
            // Desktop: display up to maxScreenshotsDesktop side-by-side
            val displayList = screenshots.take(maxScreenshotsDesktop)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(screenshotHeight.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically
            ) {
                displayList.forEach { screenshot ->
                    ProjectScreenshot(
                        screenshot = screenshot,
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f, fill = false)
                            .width((screenshotHeight * 9 / 19.5f).dp)
                    )
                }
            }
        } else {
            // Mobile: horizontal scroll rail (LazyRow)
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(screenshotHeight.dp)
                    .focusable(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(horizontal = 8.dp)
            ) {
                items(screenshots) { screenshot ->
                    ProjectScreenshot(
                        screenshot = screenshot,
                        modifier = Modifier
                            .fillMaxHeight()
                            .width((screenshotHeight * 9 / 19.5f).dp)
                    )
                }
            }
        }
    }
}

// -------------------------------------------------------------------------
// Project Modal Overlay Composable
// -------------------------------------------------------------------------
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProjectModal(
    project: ProjectModel,
    onDismiss: () -> Unit
) {
    val accentColor = parseHexColor(project.accentColor)

    // Full-screen backdrop
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ObsidianColors.Background.copy(alpha = 0.92f))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onDismiss
            )
            .onKeyEvent { keyEvent ->
                if (keyEvent.key == Key.Escape) {
                    onDismiss()
                    true
                } else false
            }
            .semantics {
                role = Role.Button
                paneTitle = "Project Details Modal"
            },
        contentAlignment = Alignment.Center
    ) {
        // Modal Card Container
        GlowCard(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(0.92f)
                .fillMaxHeight(0.90f)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = { /* prevent click-through */ }
                ),
            glowColor = accentColor.copy(alpha = 0.25f),
            borderColor = accentColor.copy(alpha = 0.5f),
            cornerRadius = 20.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(28.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // Header row: Title + Close Button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = project.category.uppercase(),
                            style = PortfolioTypography.labelLarge.copy(
                                color = accentColor,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.2.sp
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = project.title,
                            style = PortfolioTypography.headlineLarge.copy(
                                color = ObsidianColors.OnSurface,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(ObsidianColors.ContainerMid)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close modal",
                            tint = ObsidianColors.OnSurface
                        )
                    }
                }

                // Platform tags
                if (project.platform.isNotEmpty()) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        project.platform.forEach { plat ->
                            TechPill(label = plat, color = accentColor)
                        }
                    }
                }

                // Screenshots Gallery inside modal
                if (project.screenshots.isNotEmpty()) {
                    ScreenshotGallery(
                        screenshots = project.screenshots,
                        screenshotHeight = 360,
                        maxScreenshotsDesktop = 3
                    )
                }

                // Summary
                Text(
                    text = project.summary,
                    style = PortfolioTypography.bodyLarge.copy(
                        color = ObsidianColors.OnSurface,
                        lineHeight = 26.sp
                    )
                )

                // The Problem
                if (!project.problem.isNullOrBlank()) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = "THE PROBLEM",
                            style = PortfolioTypography.labelLarge.copy(
                                color = accentColor,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Text(
                            text = project.problem,
                            style = PortfolioTypography.bodyMedium.copy(
                                color = ObsidianColors.OnSurfaceDim,
                                lineHeight = 22.sp
                            )
                        )
                    }
                }

                // The Approach / Solution
                if (!project.solution.isNullOrBlank()) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = "THE APPROACH & ARCHITECTURE",
                            style = PortfolioTypography.labelLarge.copy(
                                color = accentColor,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Text(
                            text = project.solution,
                            style = PortfolioTypography.bodyMedium.copy(
                                color = ObsidianColors.OnSurface,
                                lineHeight = 22.sp
                            )
                        )
                    }
                }

                // Impact
                if (!project.impact.isNullOrBlank()) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = "IMPACT & RESULTS",
                            style = PortfolioTypography.labelLarge.copy(
                                color = ObsidianColors.Secondary,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Text(
                            text = project.impact,
                            style = PortfolioTypography.bodyMedium.copy(
                                color = ObsidianColors.OnSurface,
                                lineHeight = 22.sp
                            )
                        )
                    }
                }

                // Metrics Chips
                if (project.metrics.isNotEmpty()) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = "KEY METRICS",
                            style = PortfolioTypography.labelLarge.copy(
                                color = accentColor,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            project.metrics.forEach { metric ->
                                GlowCard(
                                    glowColor = accentColor.copy(alpha = 0.15f),
                                    cornerRadius = 8.dp
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = metric.label,
                                            style = PortfolioTypography.labelSmall.copy(color = ObsidianColors.OnSurfaceDim)
                                        )
                                        Text(
                                            text = metric.value,
                                            style = PortfolioTypography.labelLarge.copy(
                                                color = accentColor,
                                                fontWeight = FontWeight.Bold
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Technologies
                if (project.technologies.isNotEmpty()) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = "TECHNOLOGIES",
                            style = PortfolioTypography.labelLarge.copy(
                                color = ObsidianColors.Primary,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            project.technologies.forEach { tech ->
                                TechPill(label = tech, color = ObsidianColors.Primary)
                            }
                        }
                    }
                }

                // CTAs Row
                if (project.caseStudyUrl != null || project.githubUrl != null) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        if (project.caseStudyUrl != null) {
                            NeonButton(
                                text = "View Case Study",
                                onClick = { window.open(project.caseStudyUrl, "_blank") },
                                variant = NeonButtonVariant.PRIMARY
                            )
                        }
                        if (project.githubUrl != null) {
                            NeonButton(
                                text = "GitHub",
                                onClick = { window.open(project.githubUrl, "_blank") },
                                variant = NeonButtonVariant.SECONDARY
                            )
                        }
                    }
                }
            }
        }
    }
}
