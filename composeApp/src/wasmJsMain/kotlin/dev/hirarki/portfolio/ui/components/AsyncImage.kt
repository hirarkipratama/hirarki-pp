package dev.hirarki.portfolio.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import dev.hirarki.portfolio.WebPaths
import dev.hirarki.portfolio.ui.theme.ObsidianColors
import dev.hirarki.portfolio.ui.theme.PortfolioTypography
import kotlinx.browser.window
import kotlinx.coroutines.await
import kotlinx.coroutines.delay
import org.jetbrains.skia.Image as SkiaImage
import org.khronos.webgl.ArrayBuffer
import org.khronos.webgl.Int8Array
import org.khronos.webgl.get
import org.w3c.fetch.Response

private val imageCache = mutableMapOf<String, ImageBitmap>()

enum class ImageLoadPriority { EAGER, LAZY }

@Composable
fun AsyncImage(
    url: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Fit,
    fallbackText: String? = null,
    priority: ImageLoadPriority = ImageLoadPriority.LAZY,
    placeholderUrl: String? = null
) {
    var bitmap by remember(url) { mutableStateOf(imageCache[url]) }
    var placeholderBitmap by remember(placeholderUrl) {
        mutableStateOf(placeholderUrl?.let { imageCache[it] })
    }
    var isLoading by remember(url) { mutableStateOf(bitmap == null) }
    var isError by remember(url) { mutableStateOf(false) }

    LaunchedEffect(url, placeholderUrl, priority) {
        if (placeholderUrl != null && placeholderBitmap == null) {
            loadImageBitmap(placeholderUrl)?.let { loaded ->
                imageCache[placeholderUrl] = loaded
                placeholderBitmap = loaded
            }
        }

        if (bitmap != null) return@LaunchedEffect
        if (priority == ImageLoadPriority.LAZY) delay(100)

        isLoading = true
        isError = false
        val loaded = loadImageBitmap(url)
        if (loaded == null) {
            isError = true
            isLoading = false
        } else {
            imageCache[url] = loaded
            bitmap = loaded
            isLoading = false
        }
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        val currentBitmap = bitmap
        val currentPlaceholder = placeholderBitmap
        if (currentBitmap != null) {
            Image(
                bitmap = currentBitmap,
                contentDescription = contentDescription,
                modifier = Modifier.fillMaxSize(),
                contentScale = contentScale
            )
        } else if (currentPlaceholder != null) {
            Image(
                bitmap = currentPlaceholder,
                contentDescription = contentDescription,
                modifier = Modifier.fillMaxSize(),
                contentScale = contentScale
            )
        } else if (isLoading && priority == ImageLoadPriority.EAGER) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(ObsidianColors.ContainerMid),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = ObsidianColors.Primary,
                    strokeWidth = 2.dp
                )
            }
        } else if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(ObsidianColors.ContainerMid)
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(ObsidianColors.ContainerMid),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = fallbackText ?: "No Image",
                    style = PortfolioTypography.labelMedium.copy(color = ObsidianColors.OnSurfaceDim)
                )
            }
        }
    }
}

private suspend fun loadImageBitmap(url: String): ImageBitmap? = try {
    val response = window.fetch(WebPaths.asset(url)).await<Response>()
    if (!response.ok) {
        null
    } else {
        val arrayBuffer = response.arrayBuffer().await<ArrayBuffer>()
        val int8Array = Int8Array(arrayBuffer)
        val byteArray = ByteArray(int8Array.length) { index -> int8Array[index] }
        SkiaImage.makeFromEncoded(byteArray).toComposeImageBitmap()
    }
} catch (e: Throwable) {
    null
}
