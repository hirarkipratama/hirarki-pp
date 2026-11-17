package dev.hirarki.portfolio.ui.sections

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.hirarki.portfolio.data.model.ServiceModel
import dev.hirarki.portfolio.ui.components.GlowCard
import dev.hirarki.portfolio.ui.components.NeonButton
import dev.hirarki.portfolio.ui.components.NeonButtonVariant
import dev.hirarki.portfolio.ui.components.SectionHeader
import dev.hirarki.portfolio.ui.theme.ObsidianColors
import dev.hirarki.portfolio.ui.theme.PortfolioTypography

@Composable
fun ServicesSection(
    services: List<ServiceModel>,
    onContactClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (services.isEmpty()) return

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .background(ObsidianColors.Surface)
            .padding(vertical = 80.dp)
    ) {
        val isDesktop = maxWidth >= 1024.dp
        val horizontalPadding = if (maxWidth > 1200.dp) 120.dp else if (maxWidth > 600.dp) 48.dp else 20.dp

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = horizontalPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(48.dp)
        ) {
            // Centered SectionHeader
            SectionHeader(
                label = "03.",
                title = "Engineering\nCapabilities & Services",
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            // Services Grid: 2x2 on desktop, 1 column on mobile
            if (isDesktop) {
                val chunked = services.chunked(2)
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    chunked.forEach { rowServices ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(24.dp)
                        ) {
                            rowServices.forEach { service ->
                                ServiceCard(
                                    service = service,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            if (rowServices.size == 1) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            } else {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    services.forEach { service ->
                        ServiceCard(
                            service = service,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            // Bottom CTA
            NeonButton(
                text = "Let's Build Together",
                onClick = onContactClick,
                variant = NeonButtonVariant.SECONDARY
            )
        }
    }
}

@Composable
private fun ServiceCard(
    service: ServiceModel,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    val scale by animateFloatAsState(
        targetValue = if (isHovered) 1.02f else 1.0f,
        animationSpec = tween(150)
    )

    // Icon key mapping: "cross-platform" → 📱, "native" → ⚡, "architecture" → 🏗, "ui-ux" → 🎨
    val iconEmoji = when (service.iconKey.lowercase()) {
        "cross-platform" -> "📱"
        "native" -> "⚡"
        "architecture" -> "🏗"
        "ui-ux" -> "🎨"
        else -> "🚀"
    }

    GlowCard(
        modifier = modifier.scale(scale),
        glowColor = ObsidianColors.GlowCyan.copy(alpha = if (isHovered) 0.35f else 0.12f),
        borderColor = if (isHovered) ObsidianColors.Primary else ObsidianColors.Border,
        cornerRadius = 16.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(28.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Icon: 48x48dp Container-background circle
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(ObsidianColors.ContainerMid),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = iconEmoji,
                    fontSize = 22.sp
                )
            }

            // Title
            Text(
                text = service.title,
                style = PortfolioTypography.headlineSmall.copy(
                    color = ObsidianColors.OnSurface,
                    fontWeight = FontWeight.Bold
                )
            )

            // Description
            Text(
                text = service.description,
                style = PortfolioTypography.bodyMedium.copy(
                    color = ObsidianColors.OnSurfaceDim,
                    lineHeight = 22.sp
                )
            )

            // Divider
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(ObsidianColors.Border)
            )

            // Features as bullet list
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                service.features.forEach { feature ->
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(ObsidianColors.Primary)
                        )
                        Text(
                            text = feature,
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
