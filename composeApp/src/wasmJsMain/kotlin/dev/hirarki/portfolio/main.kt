package dev.hirarki.portfolio

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    CanvasBasedWindow(
        title = "Hirarki Wendri — Staff Mobile Engineer",
        canvasElementId = "ComposeTarget"
    ) {
        App()
    }
}
