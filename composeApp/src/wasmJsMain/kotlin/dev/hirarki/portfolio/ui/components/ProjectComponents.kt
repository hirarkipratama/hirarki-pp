package dev.hirarki.portfolio.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.scrollBy
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
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.dialog
import androidx.compose.ui.semantics.paneTitle
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.hirarki.portfolio.data.model.ProjectModel
import dev.hirarki.portfolio.data.model.ScreenshotModel
import dev.hirarki.portfolio.ui.theme.ObsidianColors
import dev.hirarki.portfolio.ui.theme.PortfolioTypography
import dev.hirarki.portfolio.ui.theme.accessibleFocusRing
import dev.hirarki.portfolio.ui.theme.reducedMotion
import kotlinx.browser.window
import kotlinx.coroutines.launch

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
    screenshotHeight: Int = 380,
    focusRequester: FocusRequester? = null,
    nextFocusRequester: FocusRequester? = null
) {
    if (screenshots.isEmpty()) return

    val reducedMotion = reducedMotion()
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val density = LocalDensity.current

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
            val requester = focusRequester
            val galleryFocusModifier = if (requester != null) {
                Modifier
                    .focusRequester(requester)
                    .focusProperties {
                        next = nextFocusRequester ?: requester
                    }
            } else {
                Modifier
            }

            LazyRow(
                state = listState,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(screenshotHeight.dp)
                    .focusable()
                    .then(galleryFocusModifier)
                    .accessibleFocusRing()
                    .onKeyEvent { event ->
                        val direction = when (event.key) {
                            Key.DirectionRight -> 1f
                            Key.DirectionLeft -> -1f
                            else -> return@onKeyEvent false
                        }
                        val amount = with(density) { 240.dp.toPx() * direction }
                        coroutineScope.launch {
                            if (reducedMotion) {
                                listState.scrollBy(amount)
                            } else {
                                listState.animateScrollBy(amount)
                            }
                        }
                        true
                    },
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
    val closeFocusRequester = remember { FocusRequester() }
    val galleryFocusRequester = remember { FocusRequester() }
    val caseStudyFocusRequester = remember { FocusRequester() }
    val githubFocusRequester = remember { FocusRequester() }
    val hasGallery = project.screenshots.isNotEmpty()
    val hasCaseStudy = project.caseStudyUrl != null
    val hasGithub = project.githubUrl != null
    val firstContentFocusRequester = when {
        hasGallery -> galleryFocusRequester
        hasCaseStudy -> caseStudyFocusRequester
        hasGithub -> githubFocusRequester
        else -> closeFocusRequester
    }
    val lastContentFocusRequester = when {
        hasGithub -> githubFocusRequester
        hasCaseStudy -> caseStudyFocusRequester
        hasGallery -> galleryFocusRequester
        else -> closeFocusRequester
    }

    LaunchedEffect(project) {
        closeFocusRequester.requestFocus()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ObsidianColors.Background.copy(alpha = 0.92f))
            .pointerInput(onDismiss) {
                detectTapGestures { onDismiss() }
            }
            .onKeyEvent { keyEvent ->
                if (keyEvent.key == Key.Escape) {
                    onDismiss()
                    true
                } else false
            },
        contentAlignment = Alignment.Center
    ) {
        AnimatedContent(
            targetState = project,
            contentAlignment = Alignment.Center
        ) { currentProject ->
            val currentAccentColor = parseHexColor(currentProject.accentColor)

            // Modal Card Container
            GlowCard(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth(0.92f)
                    .fillMaxHeight(0.90f)
                    .pointerInput(Unit) {
                        detectTapGestures { }
                    }
                    .semantics {
                        dialog()
                        paneTitle = "Project details for ${currentProject.title}"
                    },
                glowColor = currentAccentColor.copy(alpha = 0.25f),
                borderColor = currentAccentColor.copy(alpha = 0.5f),
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
                                text = currentProject.category.uppercase(),
                                style = PortfolioTypography.labelLarge.copy(
                                    color = currentAccentColor,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.2.sp
                                )
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = currentProject.title,
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
                                .focusRequester(closeFocusRequester)
                                .focusProperties {
                                    previous = lastContentFocusRequester
                                    if (firstContentFocusRequester == closeFocusRequester) {
                                        next = closeFocusRequester
                                    }
                                }
                                .accessibleFocusRing(cornerRadius = 22.dp)
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
                    if (currentProject.platform.isNotEmpty()) {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            currentProject.platform.forEach { plat ->
                                TechPill(label = plat, color = currentAccentColor)
                            }
                        }
                    }

                    // Screenshots Gallery inside modal
                    if (currentProject.screenshots.isNotEmpty()) {
                        ScreenshotGallery(
                            screenshots = currentProject.screenshots,
                            screenshotHeight = 360,
                            maxScreenshotsDesktop = 3,
                            focusRequester = galleryFocusRequester,
                            nextFocusRequester = when {
                                currentProject.caseStudyUrl != null -> caseStudyFocusRequester
                                currentProject.githubUrl != null -> githubFocusRequester
                                else -> closeFocusRequester
                            }
                        )
                    }

                    // Summary
                    Text(
                        text = currentProject.summary,
                        style = PortfolioTypography.bodyLarge.copy(
                            color = ObsidianColors.OnSurface,
                            lineHeight = 26.sp
                        )
                    )

                    // The Problem
                    if (!currentProject.problem.isNullOrBlank()) {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                text = "THE PROBLEM",
                                style = PortfolioTypography.labelLarge.copy(
                                    color = currentAccentColor,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            Text(
                                text = currentProject.problem,
                                style = PortfolioTypography.bodyMedium.copy(
                                    color = ObsidianColors.OnSurfaceDim,
                                    lineHeight = 22.sp
                                )
                            )
                        }
                    }

                    // The Approach / Solution
                    if (!currentProject.solution.isNullOrBlank()) {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                text = "THE APPROACH & ARCHITECTURE",
                                style = PortfolioTypography.labelLarge.copy(
                                    color = currentAccentColor,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            Text(
                                text = currentProject.solution,
                                style = PortfolioTypography.bodyMedium.copy(
                                    color = ObsidianColors.OnSurface,
                                    lineHeight = 22.sp
                                )
                            )
                        }
                    }

                    // Impact
                    if (!currentProject.impact.isNullOrBlank()) {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                text = "IMPACT & RESULTS",
                                style = PortfolioTypography.labelLarge.copy(
                                    color = ObsidianColors.SecondaryText,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            Text(
                                text = currentProject.impact,
                                style = PortfolioTypography.bodyMedium.copy(
                                    color = ObsidianColors.OnSurface,
                                    lineHeight = 22.sp
                                )
                            )
                        }
                    }

                    // Metrics Chips
                    if (currentProject.metrics.isNotEmpty()) {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                text = "KEY METRICS",
                                style = PortfolioTypography.labelLarge.copy(
                                    color = currentAccentColor,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            FlowRow(
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                currentProject.metrics.forEach { metric ->
                                    GlowCard(
                                        glowColor = currentAccentColor.copy(alpha = 0.15f),
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
                                                    color = currentAccentColor,
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
                    if (currentProject.technologies.isNotEmpty()) {
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
                                currentProject.technologies.forEach { tech ->
                                    TechPill(label = tech, color = ObsidianColors.Primary)
                                }
                            }
                        }
                    }

                    // CTAs Row
                    if (currentProject.caseStudyUrl != null || currentProject.githubUrl != null) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(top = 8.dp)
                        ) {
                            if (currentProject.caseStudyUrl != null) {
                                NeonButton(
                                    text = "View Case Study",
                                    onClick = { window.open(currentProject.caseStudyUrl, "_blank") },
                                    variant = NeonButtonVariant.PRIMARY,
                                    modifier = Modifier
                                        .focusRequester(caseStudyFocusRequester)
                                        .focusProperties {
                                            if (currentProject.githubUrl == null) {
                                                next = closeFocusRequester
                                            }
                                        }
                                )
                            }
                            if (currentProject.githubUrl != null) {
                                NeonButton(
                                    text = "GitHub",
                                    onClick = { window.open(currentProject.githubUrl, "_blank") },
                                    variant = NeonButtonVariant.SECONDARY,
                                    modifier = Modifier
                                        .focusRequester(githubFocusRequester)
                                        .focusProperties {
                                            next = closeFocusRequester
                                        }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
