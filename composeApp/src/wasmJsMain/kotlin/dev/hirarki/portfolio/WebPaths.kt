package dev.hirarki.portfolio

import kotlinx.browser.window

object WebPaths {
    val base: String by lazy {
        val location = window.location
        val segments = location.pathname.trim('/').split('/').filter { it.isNotEmpty() }
        if (location.hostname.endsWith(".github.io") && segments.isNotEmpty()) {
            "/${segments.first()}"
        } else {
            ""
        }
    }

    fun asset(path: String): String {
        if (path.startsWith("http://") || path.startsWith("https://") || path.startsWith("data:")) {
            return path
        }
        return "$base/${path.removePrefix("/")}"
    }

    fun absoluteAssetUrl(path: String): String {
        if (path.startsWith("http://") || path.startsWith("https://")) return path
        return "${window.location.origin}${asset(path)}"
    }
}
