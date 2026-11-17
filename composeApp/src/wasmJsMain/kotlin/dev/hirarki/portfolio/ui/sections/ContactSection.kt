package dev.hirarki.portfolio.ui.sections

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
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
import dev.hirarki.portfolio.data.model.ProfileModel
import dev.hirarki.portfolio.ui.components.GlowCard
import dev.hirarki.portfolio.ui.components.NeonButton
import dev.hirarki.portfolio.ui.components.NeonButtonVariant
import dev.hirarki.portfolio.ui.components.SectionHeader
import dev.hirarki.portfolio.ui.theme.JetBrainsMonoFamily
import dev.hirarki.portfolio.ui.theme.ObsidianColors
import dev.hirarki.portfolio.ui.theme.PortfolioTypography
import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.dom.HTMLAnchorElement

fun downloadCV(url: String) {
    val a = document.createElement("a") as HTMLAnchorElement
    a.href = if (url.startsWith("/")) url else "/$url"
    a.download = "Hirarki_Wendri_CV.pdf"
    document.body?.appendChild(a)
    a.click()
    document.body?.removeChild(a)
}

@Composable
fun ContactSection(
    profile: ProfileModel,
    onBackToTopClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .background(ObsidianColors.ContainerMid)
            .padding(vertical = 80.dp)
    ) {
        val horizontalPadding = if (maxWidth > 1200.dp) 80.dp else if (maxWidth > 768.dp) 40.dp else 24.dp

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = horizontalPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(48.dp)
        ) {
            // Section Header
            SectionHeader(
                label = "05.",
                title = "Contact Terminal\n& Social Connect",
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            // 2. Terminal-style contact card (GlowCard, max 600dp wide)
            GlowCard(
                modifier = Modifier.widthIn(max = 600.dp),
                glowColor = ObsidianColors.GlowCyan.copy(alpha = 0.2f),
                borderColor = ObsidianColors.Border,
                cornerRadius = 16.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(28.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    // Monospace header bar: "$ open --channel communication"
                    Text(
                        text = "$ open --channel communication",
                        style = PortfolioTypography.labelLarge.copy(
                            fontFamily = JetBrainsMonoFamily,
                            color = ObsidianColors.Primary,
                            fontWeight = FontWeight.SemiBold
                        )
                    )

                    // Divider
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(ObsidianColors.Border)
                    )

                    // Contact Rows
                    if (profile.email.isNotBlank()) {
                        ContactRow(
                            icon = "📧",
                            title = "Email",
                            value = profile.email,
                            onClick = { window.open("mailto:${profile.email}", "_blank") }
                        )
                    }

                    if (profile.whatsapp.isNotBlank()) {
                        val waClean = profile.whatsapp.replace("+", "").replace(" ", "").replace("-", "")
                        ContactRow(
                            icon = "💬",
                            title = "WhatsApp",
                            value = "Chat on WhatsApp",
                            onClick = { window.open("https://wa.me/$waClean", "_blank") }
                        )
                    }

                    if (profile.location.isNotBlank()) {
                        ContactRow(
                            icon = "📍",
                            title = "Location",
                            value = profile.location,
                            onClick = null
                        )
                    }

                    // Divider
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(ObsidianColors.Border)
                    )

                    // Social links row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (profile.github.isNotBlank()) {
                            SocialButton(
                                label = "GitHub",
                                icon = "🐙",
                                onClick = { window.open(profile.github, "_blank") }
                            )
                        }

                        if (profile.linkedin.isNotBlank()) {
                            SocialButton(
                                label = "LinkedIn",
                                icon = "💼",
                                onClick = { window.open(profile.linkedin, "_blank") }
                            )
                        }

                        if (profile.twitter.isNotBlank()) {
                            SocialButton(
                                label = "Twitter",
                                icon = "🐦",
                                onClick = { window.open(profile.twitter, "_blank") }
                            )
                        }
                    }
                }
            }

            // 3. Download CV prominent CTA below contact card
            Box(
                modifier = Modifier.widthIn(max = 320.dp),
                contentAlignment = Alignment.Center
            ) {
                NeonButton(
                    text = "⬇ Download My CV",
                    onClick = { downloadCV(profile.cvUrl) },
                    variant = NeonButtonVariant.PRIMARY,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 4. Footer
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(ObsidianColors.Border)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "© 2025 Hirarki Ardi Pratama Wendri",
                        style = PortfolioTypography.labelMedium.copy(color = ObsidianColors.OnSurfaceDim)
                    )

                    Text(
                        text = "Back to Top ↑",
                        style = PortfolioTypography.labelMedium.copy(
                            color = ObsidianColors.Primary,
                            fontWeight = FontWeight.SemiBold
                        ),
                        modifier = Modifier.clickable(onClick = onBackToTopClick)
                    )
                }
            }
        }
    }
}

@Composable
private fun ContactRow(
    icon: String,
    title: String,
    value: String,
    onClick: (() -> Unit)?
) {
    val clickableModifier = if (onClick != null) {
        Modifier.clickable(onClick = onClick)
    } else Modifier

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(clickableModifier),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = icon, fontSize = 20.sp)
        Column {
            Text(
                text = title,
                style = PortfolioTypography.labelSmall.copy(color = ObsidianColors.OnSurfaceDim)
            )
            Text(
                text = value,
                style = PortfolioTypography.bodyMedium.copy(
                    color = if (onClick != null) ObsidianColors.Primary else ObsidianColors.OnSurface,
                    fontWeight = FontWeight.Medium
                )
            )
        }
    }
}

@Composable
private fun SocialButton(
    label: String,
    icon: String,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    val scale by animateFloatAsState(
        targetValue = if (isHovered) 1.08f else 1.0f,
        animationSpec = tween(150)
    )

    Box(
        modifier = Modifier
            .size(44.dp)
            .scale(scale)
            .clip(CircleShape)
            .background(ObsidianColors.Container)
            .border(
                1.dp,
                if (isHovered) ObsidianColors.Primary else ObsidianColors.Border,
                CircleShape
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(text = icon, fontSize = 18.sp)
    }
}
