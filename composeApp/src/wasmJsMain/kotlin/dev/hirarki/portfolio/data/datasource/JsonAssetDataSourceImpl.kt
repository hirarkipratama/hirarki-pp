package dev.hirarki.portfolio.data.datasource

import kotlinx.browser.window
import kotlinx.coroutines.await
import org.w3c.fetch.Response

class JsonAssetDataSourceImpl : JsonAssetDataSource {

    private suspend fun fetchJson(path: String): String {
        val response = window.fetch(path).await<Response>()
        return response.text().await<JsString>().toString()
    }

    override suspend fun fetchProfile(): String = fetchJson("/assets/data/profile.json")

    override suspend fun fetchProjects(): String = fetchJson("/assets/data/projects.json")

    override suspend fun fetchExperiences(): String = fetchJson("/assets/data/experiences.json")

    override suspend fun fetchServices(): String = fetchJson("/assets/data/services.json")

    override suspend fun fetchSkills(): String = fetchJson("/assets/data/skills.json")
}
