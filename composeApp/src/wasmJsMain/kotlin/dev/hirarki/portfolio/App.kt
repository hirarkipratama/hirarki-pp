package dev.hirarki.portfolio

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import dev.hirarki.portfolio.ui.theme.rememberWindowDimensions
import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.dom.HTMLMetaElement

private fun jsScrollToSection(sectionId: String): Unit =
    js("document.getElementById(sectionId)?.scrollIntoView({ behavior: 'smooth', block: 'start' })")

fun scrollToSection(sectionId: String) {
    jsScrollToSection(sectionId)
}

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

    LaunchedEffect(Unit) {
        document.title = "Hirarki Wendri · Mobile Developer Portfolio"
        val metaDesc = document.querySelector("meta[name='description']") as? HTMLMetaElement
        metaDesc?.content = "Hirarki Wendri — Senior Mobile Developer specializing in Flutter, Kotlin Multiplatform, and reactive native mobile architectures."
    }

    PortfolioTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(ObsidianColors.Background)
        ) {
            when (val state = uiState) {
                is PortfolioUiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            CircularProgressIndicator(color = ObsidianColors.Primary)
                            Text(
                                text = "INITIALIZING ARCHITECTURE // LOADING DATA...",
                                style = PortfolioTypography.labelMedium.copy(color = ObsidianColors.Primary)
                            )
                        }
                    }
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
                        isMobileMenuOpen = isMobileMenuOpen,
                        onMenuToggle = { isMobileMenuOpen = it },
                        onProjectClick = { viewModel.selectProject(it) },
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
                enter = fadeIn() + scaleIn(initialScale = 0.9f) + slideInVertically { it / 8 },
                exit = fadeOut() + scaleOut(targetScale = 0.9f) + slideOutVertically { it / 8 }
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
    isMobileMenuOpen: Boolean,
    onMenuToggle: (Boolean) -> Unit,
    onProjectClick: (dev.hirarki.portfolio.data.model.ProjectModel) -> Unit,
    onNavClick: (String) -> Unit,
    onDownloadCV: () -> Unit
) {
    val scrollState = rememberScrollState()
    val windowDimensions = rememberWindowDimensions()
    val isMobile = windowDimensions.isMobile

    // Active section tracking based on scroll offset
    // Approximate breakpoints for sections in scroll offset
    val activeSection by remember {
        derivedStateOf {
            val scrollY = scrollState.value
            when {
                scrollY < 500 -> "hero"
                scrollY < 1200 -> "about"
                scrollY < 2400 -> "projects"
                scrollY < 3400 -> "services"
                scrollY < 4600 -> "experience"
                else -> "contact"
            }
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
                modifier = Modifier.fillMaxWidth()
            )

            MetricsSection(
                metrics = uiState.profile.metrics,
                modifier = Modifier.fillMaxWidth()
            )

            AboutSection(
                skills = uiState.skills,
                modifier = Modifier.fillMaxWidth()
            )

            ProjectsSection(
                projects = uiState.projects,
                onProjectClick = onProjectClick,
                modifier = Modifier.fillMaxWidth()
            )

            ServicesSection(
                services = uiState.services,
                onContactClick = { onNavClick("contact") },
                modifier = Modifier.fillMaxWidth()
            )

            ExperienceSection(
                experiences = uiState.experiences,
                modifier = Modifier.fillMaxWidth()
            )

            ContactSection(
                profile = uiState.profile,
                onBackToTopClick = { onNavClick("hero") },
                modifier = Modifier.fillMaxWidth()
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
