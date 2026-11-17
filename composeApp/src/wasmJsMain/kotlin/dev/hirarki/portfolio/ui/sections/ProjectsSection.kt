package dev.hirarki.portfolio.ui.sections

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.hirarki.portfolio.data.model.ProjectModel
import dev.hirarki.portfolio.ui.components.AsyncImage
import dev.hirarki.portfolio.ui.components.GlowCard
import dev.hirarki.portfolio.ui.components.NeonButton
import dev.hirarki.portfolio.ui.components.NeonButtonVariant
import dev.hirarki.portfolio.ui.components.ScreenshotGallery
import dev.hirarki.portfolio.ui.components.SectionHeader
import dev.hirarki.portfolio.ui.components.TechPill
import dev.hirarki.portfolio.ui.components.parseHexColor
import dev.hirarki.portfolio.ui.theme.ObsidianColors
import dev.hirarki.portfolio.ui.theme.PortfolioTypography
import kotlinx.browser.window

@Composable
fun ProjectsSection(
    projects: List<ProjectModel>,
    onProjectClick: (ProjectModel) -> Unit,
    modifier: Modifier = Modifier
) {
    if (projects.isEmpty()) return

    val featuredProject = projects.firstOrNull { it.featured } ?: projects.first()
    val otherProjects = projects.filter { it.slug != featuredProject.slug }

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 80.dp)
    ) {
        val containerWidth = maxWidth
        val isDesktop = containerWidth >= 1024.dp
        val horizontalPadding = if (containerWidth > 1200.dp) 80.dp else if (containerWidth > 768.dp) 40.dp else 24.dp

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = horizontalPadding),
            verticalArrangement = Arrangement.spacedBy(48.dp)
        ) {
            // Section Header
            SectionHeader(
                label = "02.",
                title = "Featured Engineering\nCase Studies"
            )

            // Step 3: Featured Project Card
            FeaturedProjectCard(
                project = featuredProject,
                onProjectClick = { onProjectClick(featuredProject) },
                isDesktop = isDesktop
            )

            // Step 4: Remaining Project Cards Grid
            if (otherProjects.isNotEmpty()) {
                ProjectsGrid(
                    projects = otherProjects,
                    onProjectClick = onProjectClick,
                    isDesktop = isDesktop,
                    isTablet = containerWidth in 768.dp..1023.dp
                )
            }
        }
    }
}

// -------------------------------------------------------------------------
// Featured Project Card Composable
// -------------------------------------------------------------------------
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun FeaturedProjectCard(
    project: ProjectModel,
    onProjectClick: () -> Unit,
    isDesktop: Boolean,
    modifier: Modifier = Modifier
) {
    val accentColor = parseHexColor(project.accentColor)

    GlowCard(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onProjectClick)
            .semantics {
                role = Role.Button
                contentDescription = "${project.title} - ${project.category}"
            },
        glowColor = accentColor.copy(alpha = 0.2f),
        borderColor = accentColor.copy(alpha = 0.4f),
        cornerRadius = 20.dp
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Accent color strip at top
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .background(accentColor)
            )

            if (isDesktop) {
                // Desktop: Row (55% media | 45% content)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(28.dp),
                    horizontalArrangement = Arrangement.spacedBy(32.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // MEDIA AREA (Left)
                    FeaturedMediaArea(
                        project = project,
                        modifier = Modifier.weight(0.55f)
                    )

                    // CONTENT AREA (Right)
                    FeaturedContentArea(
                        project = project,
                        accentColor = accentColor,
                        onProjectClick = onProjectClick,
                        modifier = Modifier.weight(0.45f)
                    )
                }
            } else {
                // Mobile/Tablet: Stacked vertically
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    FeaturedMediaArea(
                        project = project,
                        modifier = Modifier.fillMaxWidth()
                    )

                    FeaturedContentArea(
                        project = project,
                        accentColor = accentColor,
                        onProjectClick = onProjectClick,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
private fun FeaturedMediaArea(
    project: ProjectModel,
    modifier: Modifier = Modifier
) {
    val accentColor = parseHexColor(project.accentColor)

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Banner image with ambient glow
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(ObsidianColors.ContainerMid)
                .drawBehind {
                    drawIntoCanvas { canvas ->
                        val paint = Paint().apply {
                            color = accentColor.copy(alpha = 0.15f)
                        }
                        canvas.drawRoundRect(
                            0f, 0f, size.width, size.height,
                            14.dp.toPx(), 14.dp.toPx(), paint
                        )
                    }
                }
        ) {
            AsyncImage(
                url = project.bannerUrl,
                contentDescription = "${project.title} overview banner",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit,
                fallbackText = project.title
            )
        }

        // Screenshots side-by-side or scroll rail
        if (project.screenshots.isNotEmpty()) {
            ScreenshotGallery(
                screenshots = project.screenshots,
                screenshotHeight = 220,
                maxScreenshotsDesktop = 3
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun FeaturedContentArea(
    project: ProjectModel,
    accentColor: Color,
    onProjectClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Platform badges
        if (project.platform.isNotEmpty()) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                project.platform.forEach { plat ->
                    TechPill(label = plat, color = accentColor)
                }
            }
        }

        // Title & Category
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = project.title,
                style = PortfolioTypography.headlineMedium.copy(
                    color = ObsidianColors.OnSurface,
                    fontWeight = FontWeight.Bold
                )
            )
            Text(
                text = project.category,
                style = PortfolioTypography.bodyMedium.copy(color = ObsidianColors.OnSurfaceDim)
            )
        }

        // Summary
        Text(
            text = project.summary,
            style = PortfolioTypography.bodyMedium.copy(
                color = ObsidianColors.OnSurface,
                lineHeight = 22.sp
            )
        )

        // Technologies FlowRow
        if (project.technologies.isNotEmpty()) {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                project.technologies.forEach { tech ->
                    TechPill(label = tech, color = ObsidianColors.Primary)
                }
            }
        }

        // Mini metrics chips
        if (project.metrics.isNotEmpty()) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                project.metrics.forEach { metric ->
                    GlowCard(
                        glowColor = accentColor.copy(alpha = 0.1f),
                        cornerRadius = 8.dp
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = metric.label,
                                style = PortfolioTypography.labelSmall.copy(color = ObsidianColors.OnSurfaceDim)
                            )
                            Text(
                                text = metric.value,
                                style = PortfolioTypography.labelMedium.copy(
                                    color = accentColor,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }
                }
            }
        }

        // CTA Row
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 8.dp)
        ) {
            NeonButton(
                text = "Explore Details →",
                onClick = onProjectClick,
                variant = NeonButtonVariant.PRIMARY
            )

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

// -------------------------------------------------------------------------
// Remaining Project Cards Grid
// -------------------------------------------------------------------------
@Composable
private fun ProjectsGrid(
    projects: List<ProjectModel>,
    onProjectClick: (ProjectModel) -> Unit,
    isDesktop: Boolean,
    isTablet: Boolean,
    modifier: Modifier = Modifier
) {
    if (isDesktop || isTablet) {
        // 2-Column grid using chunked rows
        val chunked = projects.chunked(2)
        Column(
            modifier = modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            chunked.forEach { rowProjects ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    rowProjects.forEach { project ->
                        ProjectGridCard(
                            project = project,
                            onProjectClick = { onProjectClick(project) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    if (rowProjects.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    } else {
        // Mobile: single column
        Column(
            modifier = modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            projects.forEach { project ->
                ProjectGridCard(
                    project = project,
                    onProjectClick = { onProjectClick(project) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ProjectGridCard(
    project: ProjectModel,
    onProjectClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val accentColor = parseHexColor(project.accentColor)
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    val scale by animateFloatAsState(
        targetValue = if (isHovered) 1.02f else 1.0f,
        animationSpec = tween(150)
    )

    GlowCard(
        modifier = modifier
            .scale(scale)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onProjectClick
            )
            .semantics {
                role = Role.Button
                contentDescription = "${project.title} - ${project.category}"
            },
        glowColor = accentColor.copy(alpha = if (isHovered) 0.35f else 0.15f),
        borderColor = if (isHovered) accentColor else ObsidianColors.Border,
        cornerRadius = 16.dp
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Banner at top
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(ObsidianColors.ContainerMid)
            ) {
                AsyncImage(
                    url = project.bannerUrl,
                    contentDescription = "${project.title} banner",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit,
                    fallbackText = project.title
                )
            }

            // 3.dp accent line
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(3.dp)
                    .background(accentColor)
            )

            // Body padding 20.dp
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Platform badges
                if (project.platform.isNotEmpty()) {
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        project.platform.forEach { plat ->
                            TechPill(label = plat, color = accentColor)
                        }
                    }
                }

                Text(
                    text = project.title,
                    style = PortfolioTypography.titleLarge.copy(
                        color = ObsidianColors.OnSurface,
                        fontWeight = FontWeight.Bold
                    )
                )

                Text(
                    text = project.category,
                    style = PortfolioTypography.bodySmall.copy(color = ObsidianColors.OnSurfaceDim)
                )

                Text(
                    text = project.summary,
                    style = PortfolioTypography.bodyMedium.copy(
                        color = ObsidianColors.OnSurfaceDim,
                        lineHeight = 20.sp
                    ),
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )

                // Technologies FlowRow (up to 4 pills)
                if (project.technologies.isNotEmpty()) {
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        project.technologies.take(4).forEach { tech ->
                            TechPill(label = tech, color = ObsidianColors.Primary)
                        }
                        if (project.technologies.size > 4) {
                            TechPill(
                                label = "+${project.technologies.size - 4}",
                                color = ObsidianColors.OnSurfaceDim
                            )
                        }
                    }
                }

                // Bottom row: Details text button
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "View Case Study →",
                        style = PortfolioTypography.labelMedium.copy(
                            color = accentColor,
                            fontWeight = FontWeight.SemiBold
                        )
                    )

                    if (project.githubUrl != null) {
                        Text(
                            text = "GitHub",
                            style = PortfolioTypography.labelSmall.copy(color = ObsidianColors.OnSurfaceDim),
                            modifier = Modifier.clickable { window.open(project.githubUrl, "_blank") }
                        )
                    }
                }
            }
        }
    }
}
