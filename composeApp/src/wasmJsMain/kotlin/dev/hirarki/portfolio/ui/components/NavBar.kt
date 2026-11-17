package dev.hirarki.portfolio.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.hirarki.portfolio.ui.theme.ObsidianColors
import dev.hirarki.portfolio.ui.theme.PortfolioTypography
import dev.hirarki.portfolio.ui.theme.SpaceGroteskFamily

@Composable
fun NavBar(
    onNavClick: (sectionId: String) -> Unit,
    onDownloadCV: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isMenuOpen by remember { mutableStateOf(false) }
    val navItems = listOf("About", "Projects", "Services", "Experience", "Contact")

    Surface(
        modifier = modifier.fillMaxWidth(),
        color = ObsidianColors.Surface,
        border = BorderStroke(1.dp, ObsidianColors.Border)
    ) {
        BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
            val isMobile = maxWidth < 768.dp

            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 16.dp),
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
                        modifier = Modifier.clickable { onNavClick("hero") }
                    )

                    if (!isMobile) {
                        // Desktop Nav Links
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(32.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            navItems.forEach { item ->
                                Text(
                                    text = item,
                                    style = PortfolioTypography.bodyMedium.copy(color = ObsidianColors.OnSurface),
                                    modifier = Modifier.clickable {
                                        onNavClick(item.lowercase())
                                    }
                                )
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
                        IconButton(onClick = { isMenuOpen = !isMenuOpen }) {
                            Icon(
                                imageVector = if (isMenuOpen) Icons.Default.Close else Icons.Default.Menu,
                                contentDescription = if (isMenuOpen) "Close navigation menu" else "Open navigation menu",
                                tint = ObsidianColors.Primary
                            )
                        }
                    }
                }

                // Mobile Expandable Dropdown
                if (isMobile) {
                    AnimatedVisibility(
                        visible = isMenuOpen,
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp, vertical = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            navItems.forEach { item ->
                                Text(
                                    text = item,
                                    style = PortfolioTypography.titleMedium.copy(color = ObsidianColors.OnSurface),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            isMenuOpen = false
                                            onNavClick(item.lowercase())
                                        }
                                        .padding(vertical = 4.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            NeonButton(
                                text = "Download CV",
                                onClick = {
                                    isMenuOpen = false
                                    onDownloadCV()
                                },
                                variant = NeonButtonVariant.SECONDARY,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
        }
    }
}
