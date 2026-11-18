package dev.hirarki.portfolio.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.hirarki.portfolio.ui.theme.ObsidianColors
import dev.hirarki.portfolio.ui.theme.PortfolioTypography
import dev.hirarki.portfolio.ui.theme.SpaceGroteskFamily
import dev.hirarki.portfolio.ui.theme.accessibleFocusRing
import dev.hirarki.portfolio.ui.theme.activateOnEnterOrSpace
import dev.hirarki.portfolio.ui.theme.reducedMotion

@Composable
fun NavBar(
    activeSection: String,
    isScrolled: Boolean,
    isMobile: Boolean,
    isMenuOpen: Boolean,
    onMenuToggle: (Boolean) -> Unit,
    onNavClick: (sectionId: String) -> Unit,
    onDownloadCV: () -> Unit,
    modifier: Modifier = Modifier
) {
    val navItems = listOf("About", "Projects", "Services", "Experience", "Contact")
    val reducedMotion = reducedMotion()

    val navBgColor by animateColorAsState(
        targetValue = if (isScrolled) ObsidianColors.Surface.copy(alpha = 0.95f) else Color.Transparent,
        animationSpec = tween(durationMillis = if (reducedMotion) 0 else 300)
    )

    val navBorderColor by animateColorAsState(
        targetValue = if (isScrolled) ObsidianColors.Border else Color.Transparent,
        animationSpec = tween(durationMillis = if (reducedMotion) 0 else 300)
    )

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .semantics {
                isTraversalGroup = true
                traversalIndex = 0f
            },
        color = navBgColor,
        border = BorderStroke(1.dp, navBorderColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = if (isMobile) 20.dp else 40.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // HW Monogram logo
            Text(
                text = "HW",
                style = PortfolioTypography.headlineMedium.copy(
                    fontFamily = SpaceGroteskFamily,
                    fontWeight = FontWeight.Bold,
                    color = ObsidianColors.Primary
                ),
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .clickable { onNavClick("hero") }
                    .activateOnEnterOrSpace { onNavClick("hero") }
                    .accessibleFocusRing()
                    .semantics(mergeDescendants = true) {
                        role = Role.Button
                        contentDescription = "Navigate to hero"
                    }
                    .padding(4.dp)
            )

            if (!isMobile) {
                // Desktop Nav Links
                Row(
                    horizontalArrangement = Arrangement.spacedBy(32.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    navItems.forEach { item ->
                        val sectionId = item.lowercase()
                        val isActive = activeSection.equals(sectionId, ignoreCase = true)

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .clickable { onNavClick(sectionId) }
                                .activateOnEnterOrSpace { onNavClick(sectionId) }
                                .accessibleFocusRing()
                                .semantics(mergeDescendants = true) {
                                    role = Role.Button
                                    contentDescription = "Navigate to $sectionId"
                                }
                                .padding(vertical = 4.dp, horizontal = 4.dp)
                        ) {
                            Text(
                                text = item,
                                style = PortfolioTypography.bodyMedium.copy(
                                    fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isActive) ObsidianColors.Primary else ObsidianColors.OnSurface
                                )
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            // Active underline/accent indicator
                            Box(
                                modifier = Modifier
                                    .size(
                                        width = if (isActive) 16.dp else 0.dp,
                                        height = 2.dp
                                    )
                                    .background(ObsidianColors.Primary)
                            )
                        }
                    }
                }

                // Right: Download CV
                NeonButton(
                    text = "Download CV",
                    onClick = onDownloadCV,
                    variant = NeonButtonVariant.SECONDARY
                )
            } else {
                // Mobile Hamburger Toggle
                IconButton(
                    onClick = { onMenuToggle(!isMenuOpen) },
                    modifier = Modifier.accessibleFocusRing()
                ) {
                    Icon(
                        imageVector = if (isMenuOpen) Icons.Default.Close else Icons.Default.Menu,
                        contentDescription = if (isMenuOpen) "Close navigation menu" else "Open navigation menu",
                        tint = ObsidianColors.Primary
                    )
                }
            }
        }
    }
}

@Composable
fun FullscreenMobileMenu(
    isOpen: Boolean,
    activeSection: String,
    onNavClick: (String) -> Unit,
    onDownloadCV: () -> Unit,
    onClose: () -> Unit
) {
    val navItems = listOf("About", "Projects", "Services", "Experience", "Contact")
    val reducedMotion = reducedMotion()

    AnimatedVisibility(
        visible = isOpen,
        enter = if (reducedMotion) {
            EnterTransition.None
        } else {
            fadeIn(tween(250)) + slideInVertically(tween(300)) { -it }
        },
        exit = if (reducedMotion) {
            ExitTransition.None
        } else {
            fadeOut(tween(200)) + slideOutVertically(tween(250)) { -it }
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(ObsidianColors.Surface)
                .padding(24.dp)
                .semantics {
                    isTraversalGroup = true
                    traversalIndex = 0f
                }
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Top row with Monogram and Close Button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "HW",
                        style = PortfolioTypography.headlineMedium.copy(
                            fontFamily = SpaceGroteskFamily,
                            fontWeight = FontWeight.Bold,
                            color = ObsidianColors.Primary
                        ),
                        modifier = Modifier
                            .clickable {
                                onClose()
                                onNavClick("hero")
                            }
                            .activateOnEnterOrSpace {
                                onClose()
                                onNavClick("hero")
                            }
                            .accessibleFocusRing()
                            .semantics(mergeDescendants = true) {
                                role = Role.Button
                                contentDescription = "Navigate to hero"
                            }
                    )
                    IconButton(
                        onClick = onClose,
                        modifier = Modifier.accessibleFocusRing()
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close menu",
                            tint = ObsidianColors.Primary,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }

                // Nav Links list (headlineMedium with Primary accent for active)
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    navItems.forEach { item ->
                        val sectionId = item.lowercase()
                        val isActive = activeSection.equals(sectionId, ignoreCase = true)

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onClose()
                                    onNavClick(sectionId)
                                }
                                .activateOnEnterOrSpace {
                                    onClose()
                                    onNavClick(sectionId)
                                }
                                .accessibleFocusRing()
                                .semantics(mergeDescendants = true) {
                                    role = Role.Button
                                    contentDescription = "Navigate to $sectionId"
                                }
                                .padding(vertical = 8.dp)
                        ) {
                            if (isActive) {
                                Box(
                                    modifier = Modifier
                                        .size(4.dp, 24.dp)
                                        .background(ObsidianColors.Primary)
                                )
                                Spacer(modifier = Modifier.size(12.dp))
                            }
                            Text(
                                text = item,
                                style = PortfolioTypography.headlineMedium.copy(
                                    fontWeight = if (isActive) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isActive) ObsidianColors.Primary else ObsidianColors.OnSurface,
                                    letterSpacing = 1.sp
                                )
                            )
                        }
                    }
                }

                // Bottom CV Button
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    NeonButton(
                        text = "Download CV",
                        onClick = {
                            onClose()
                            onDownloadCV()
                        },
                        variant = NeonButtonVariant.PRIMARY,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}
