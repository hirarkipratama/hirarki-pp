package dev.hirarki.portfolio

import dev.hirarki.portfolio.data.model.ProfileModel
import kotlinx.browser.document
import org.w3c.dom.HTMLMetaElement

fun setupWebMetadata(profile: ProfileModel) {
    document.title = "${profile.name} · Mobile Developer Portfolio"

    setMetaTag(
        "description",
        "Portfolio of ${profile.name}, ${profile.title} specializing in Flutter, Kotlin, and Swift mobile development. ${profile.tagline}"
    )
    setMetaTag("og:title", "${profile.name} · Mobile Developer Portfolio")
    setMetaTag("og:description", profile.tagline)
    setMetaTag("og:image", absoluteAssetUrl(profile.avatarUrl))
    setMetaTag("og:type", "website")
    setMetaTag("twitter:card", "summary_large_image")
    setMetaTag("twitter:creator", "@hirarki")
    setMetaTag("theme-color", "#0C0E14")
}

fun setMetaTag(name: String, content: String) {
    val selector = "meta[name=\"$name\"], meta[property=\"$name\"]"
    val meta = document.querySelector(selector) ?: document.createElement("meta").also { element ->
        val metaElement = element as HTMLMetaElement
        if (name.startsWith("og:")) {
            metaElement.setAttribute("property", name)
        } else {
            metaElement.name = name
        }
        document.head?.appendChild(metaElement)
    }
    (meta as HTMLMetaElement).content = content
}

private fun absoluteAssetUrl(url: String): String {
    if (url.startsWith("http://") || url.startsWith("https://")) return url
    return "https://hirarki.dev/${url.removePrefix("/")}"
}
