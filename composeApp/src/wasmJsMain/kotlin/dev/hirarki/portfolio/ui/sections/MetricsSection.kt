package dev.hirarki.portfolio.ui.sections

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import dev.hirarki.portfolio.data.model.MetricModel
import dev.hirarki.portfolio.ui.components.AnimatedCounter
import dev.hirarki.portfolio.ui.theme.ObsidianColors

@Composable
fun MetricsSection(
    metrics: List<MetricModel>,
    modifier: Modifier = Modifier
) {
    if (metrics.isEmpty()) return

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .background(ObsidianColors.ContainerMid)
            .drawBehind {
                // Subtle top/bottom borders in Border color
                drawLine(
                    color = ObsidianColors.Border,
                    start = Offset(0f, 0f),
                    end = Offset(size.width, 0f),
                    strokeWidth = 1.dp.toPx()
                )
                drawLine(
                    color = ObsidianColors.Border,
                    start = Offset(0f, size.height),
                    end = Offset(size.width, size.height),
                    strokeWidth = 1.dp.toPx()
                )
            }
    ) {
        val isMobile = maxWidth < 768.dp
        val horizontalPadding = if (maxWidth > 1024.dp) 80.dp else if (maxWidth > 600.dp) 40.dp else 24.dp

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = horizontalPadding, vertical = 40.dp)
        ) {
            if (!isMobile) {
                // Desktop: Horizontal row with equal weight distribution and vertical dividers
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    metrics.forEachIndexed { index, metric ->
                        Box(
                            modifier = Modifier.weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            AnimatedCounter(
                                targetValue = metric.value,
                                suffix = metric.suffix,
                                label = metric.label
                            )
                        }

                        if (index < metrics.size - 1) {
                            Box(
                                modifier = Modifier
                                    .width(1.dp)
                                    .height(64.dp)
                                    .background(ObsidianColors.Border)
                            )
                        }
                    }
                }
            } else {
                // Mobile: 2x2 grid using two Rows
                val chunkedMetrics = metrics.chunked(2)
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(28.dp)
                ) {
                    chunkedMetrics.forEach { rowMetrics ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            rowMetrics.forEach { metric ->
                                Box(
                                    modifier = Modifier.weight(1f),
                                    contentAlignment = Alignment.CenterStart
                                ) {
                                    AnimatedCounter(
                                        targetValue = metric.value,
                                        suffix = metric.suffix,
                                        label = metric.label
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
