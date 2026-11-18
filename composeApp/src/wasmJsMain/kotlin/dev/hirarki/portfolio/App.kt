package dev.hirarki.portfolio

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import dev.hirarki.portfolio.di.AppModule
import dev.hirarki.portfolio.presentation.state.PortfolioUiState
import dev.hirarki.portfolio.presentation.viewmodel.PortfolioViewModel
import dev.hirarki.portfolio.ui.components.FullscreenMobileMenu
import dev.hirarki.portfolio.ui.components.NavBar
import dev.hirarki.portfolio.ui.components.NeonButton
import dev.hirarki.portfolio.ui.components.NeonButtonVariant
import dev.hirarki.portfolio.ui.components.ProjectModal
import dev.hirarki.portfolio.ui.sections.AboutSection
import dev.hirarki.portfolio.ui.sections.ContactSection
import dev.hirarki.portfolio.ui.sections.ExperienceSection
import dev.hirarki.portfolio.ui.sections.HeroSection
import dev.hirarki.portfolio.ui.sections.MetricsSection
import dev.hirarki.portfolio.ui.sections.ProjectsSection
import dev.hirarki.portfolio.ui.sections.ServicesSection
import dev.hirarki.portfolio.ui.sections.downloadCV
import dev.hirarki.portfolio.ui.theme.ObsidianColors
import dev.hirarki.portfolio.ui.theme.PortfolioTheme
import dev.hirarki.portfolio.ui.theme.PortfolioTypography
import dev.hirarki.portfolio.ui.theme.reducedMotion
import dev.hirarki.portfolio.ui.theme.rememberWindowDimensions
import kotlin.math.roundToInt
import kotlinx.coroutines.launch

private fun addJsKeydownListener(onEscape: () -> Unit): () -> Unit =
    js("""
        {
            var handler = function(e) {
                if (e.key === 'Escape' || e.keyCode === 27) {
                    onEscape();
                }
            };
            window.addEventListener('keydown', handler);
            return function() { window.removeEventListener('keydown', handler); };
        }
    """)

@Composable
fun App() {
    val viewModel = remember {
        PortfolioViewModel(
            getProfileUseCase = AppModule.getProfile,
            getProjectsUseCase = AppModule.getProjects,
            getExperiencesUseCase = AppModule.getExperiences,
            getServicesUseCase = AppModule.getServices,
            getSkillsUseCase = AppModule.getSkills
        )
    }

    val uiState by viewModel.uiState.collectAsState()
    val selectedProject by viewModel.selectedProject.collectAsState()
    var isMobileMenuOpen by remember { mutableStateOf(false) }
    var projectCardFocusRequester by remember { mutableStateOf<FocusRequester?>(null) }
    val scrollState = rememberScrollState()
    val sectionOffsets = remember { mutableStateMapOf<String, Int>() }
    val coroutineScope = rememberCoroutineScope()
    val navBarHeightPx = with(LocalDensity.current) { 64.dp.toPx().roundToInt() }

    // Keyboard listener: Escape closes modal or mobile menu
    DisposableEffect(selectedProject, isMobileMenuOpen) {
        val cleanup = addJsKeydownListener {
            if (selectedProject != null) {
                viewModel.dismissProjectModal()
            } else if (isMobileMenuOpen) {
                isMobileMenuOpen = false
            }
        }
        onDispose { cleanup() }
    }

    LaunchedEffect(selectedProject) {
        if (selectedProject == null) {
            projectCardFocusRequester?.requestFocus()
            projectCardFocusRequester = null
        }
    }

    PortfolioTheme {
        val isReducedMotion = reducedMotion()
        val scrollToSection: (String) -> Unit = { sectionId ->
            val target = ((sectionOffsets[sectionId] ?: 0) - navBarHeightPx).coerceAtLeast(0)
            coroutineScope.launch {
                if (isReducedMotion) {
                    scrollState.scrollTo(target)
                } else {
                    scrollState.animateScrollTo(target)
                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(ObsidianColors.Background)
        ) {
            when (val state = uiState) {
                is PortfolioUiState.Loading -> {
                    LoadingSkeleton()
                }

                is PortfolioUiState.Error -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            modifier = Modifier.padding(24.dp)
                        ) {
                            Text(
                                text = "RUNTIME FAULT: ${state.message}",
                                style = PortfolioTypography.headlineSmall.copy(color = ObsidianColors.Secondary)
                            )
                            NeonButton(
                                text = "Retry",
                                onClick = { viewModel.loadPortfolio() },
                                variant = NeonButtonVariant.PRIMARY
                            )
                        }
                    }
                }

                is PortfolioUiState.Success -> {
                    PortfolioPage(
                        uiState = state,
                        scrollState = scrollState,
                        sectionOffsets = sectionOffsets,
                        onSectionPositioned = { id, y -> sectionOffsets[id] = y },
                        isMobileMenuOpen = isMobileMenuOpen,
                        onMenuToggle = { isMobileMenuOpen = it },
                        onProjectClick = { project, focusRequester ->
                            projectCardFocusRequester = focusRequester
                            viewModel.selectProject(project)
                        },
                        onNavClick = { sectionId ->
                            isMobileMenuOpen = false
                            scrollToSection(sectionId)
                        },
                        onDownloadCV = { downloadCV(state.profile.cvUrl) }
                    )
                }
            }

            // ProjectModal overlay with polished entry/exit animation
            AnimatedVisibility(
                visible = selectedProject != null,
                enter = if (isReducedMotion) {
                    EnterTransition.None
                } else {
                    fadeIn() + scaleIn(initialScale = 0.9f) + slideInVertically { it / 8 }
                },
                exit = if (isReducedMotion) {
                    ExitTransition.None
                } else {
                    fadeOut() + scaleOut(targetScale = 0.9f) + slideOutVertically { it / 8 }
                }
            ) {
                selectedProject?.let { project ->
                    ProjectModal(
                        project = project,
                        onDismiss = { viewModel.dismissProjectModal() }
                    )
                }
            }
        }
    }
}

@Composable
fun PortfolioPage(
    uiState: PortfolioUiState.Success,
    scrollState: ScrollState,
    sectionOffsets: Map<String, Int>,
    onSectionPositioned: (String, Int) -> Unit,
    isMobileMenuOpen: Boolean,
    onMenuToggle: (Boolean) -> Unit,
    onProjectClick: (dev.hirarki.portfolio.data.model.ProjectModel, FocusRequester) -> Unit,
    onNavClick: (String) -> Unit,
    onDownloadCV: () -> Unit
) {
    LaunchedEffect(uiState.profile) {
        setupWebMetadata(uiState.profile)
    }

    val windowDimensions = rememberWindowDimensions()
    val isMobile = windowDimensions.isMobile
    val navBarHeightPx = with(LocalDensity.current) { 64.dp.toPx().roundToInt() }

    val activeSection by remember {
        derivedStateOf {
            val scrollY = scrollState.value + navBarHeightPx
            sectionOffsets.entries
                .filter { it.value <= scrollY }
                .maxByOrNull { it.value }
                ?.key ?: "hero"
        }
    }

    val isScrolled by remember {
        derivedStateOf { scrollState.value > 100 }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ObsidianColors.Background)
    ) {
        // Main scrollable content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(top = 64.dp) // Equal to NavBar height
        ) {
            HeroSection(
                profile = uiState.profile,
                onViewProjectsClick = { onNavClick("projects") },
                onDownloadCVClick = onDownloadCV,
                modifier = Modifier
                    .fillMaxWidth()
                    .onGloballyPositioned { onSectionPositioned("hero", it.positionInParent().y.roundToInt()) }
            )

            MetricsSection(
                metrics = uiState.profile.metrics,
                modifier = Modifier
                    .fillMaxWidth()
                    .onGloballyPositioned { onSectionPositioned("metrics", it.positionInParent().y.roundToInt()) }
            )

            AboutSection(
                skills = uiState.skills,
                modifier = Modifier
                    .fillMaxWidth()
                    .onGloballyPositioned { onSectionPositioned("about", it.positionInParent().y.roundToInt()) }
            )

            ProjectsSection(
                projects = uiState.projects,
                onProjectClick = onProjectClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .onGloballyPositioned { onSectionPositioned("projects", it.positionInParent().y.roundToInt()) }
            )

            ServicesSection(
                services = uiState.services,
                onContactClick = { onNavClick("contact") },
                modifier = Modifier
                    .fillMaxWidth()
                    .onGloballyPositioned { onSectionPositioned("services", it.positionInParent().y.roundToInt()) }
            )

            ExperienceSection(
                experiences = uiState.experiences,
                modifier = Modifier
                    .fillMaxWidth()
                    .onGloballyPositioned { onSectionPositioned("experience", it.positionInParent().y.roundToInt()) }
            )

            ContactSection(
                profile = uiState.profile,
                onBackToTopClick = { onNavClick("hero") },
                modifier = Modifier
                    .fillMaxWidth()
                    .onGloballyPositioned { onSectionPositioned("contact", it.positionInParent().y.roundToInt()) }
            )
        }

        // Fixed Top Navigation Bar with active section & scroll styling
        NavBar(
            activeSection = activeSection,
            isScrolled = isScrolled,
            isMobile = isMobile,
            isMenuOpen = isMobileMenuOpen,
            onMenuToggle = onMenuToggle,
            onNavClick = onNavClick,
            onDownloadCV = onDownloadCV,
            modifier = Modifier
                .align(Alignment.TopStart)
                .fillMaxWidth()
        )

        // Mobile Fullscreen Menu overlay
        if (isMobile) {
            FullscreenMobileMenu(
                isOpen = isMobileMenuOpen,
                activeSection = activeSection,
                onNavClick = onNavClick,
                onDownloadCV = onDownloadCV,
                onClose = { onMenuToggle(false) }
            )
        }
    }
}

@Composable
private fun LoadingSkeleton() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 88.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(ObsidianColors.Container)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(320.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(ObsidianColors.ContainerMid)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            repeat(4) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(96.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(ObsidianColors.Container)
                )
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(ObsidianColors.Container)
        )
    }
}
