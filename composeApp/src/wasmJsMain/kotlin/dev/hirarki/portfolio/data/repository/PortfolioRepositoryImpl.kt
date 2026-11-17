package dev.hirarki.portfolio.data.repository

import dev.hirarki.portfolio.data.datasource.JsonAssetDataSource
import dev.hirarki.portfolio.data.model.ExperienceModel
import dev.hirarki.portfolio.data.model.ProfileModel
import dev.hirarki.portfolio.data.model.ProjectModel
import dev.hirarki.portfolio.data.model.ServiceModel
import dev.hirarki.portfolio.data.model.SkillsModel
import kotlinx.serialization.json.Json

class PortfolioRepositoryImpl(
    private val dataSource: JsonAssetDataSource
) : PortfolioRepository {

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    override suspend fun getProfile(): ProfileModel {
        val raw = dataSource.fetchProfile()
        return json.decodeFromString(raw)
    }

    override suspend fun getProjects(): List<ProjectModel> {
        val raw = dataSource.fetchProjects()
        return json.decodeFromString(raw)
    }

    override suspend fun getExperiences(): List<ExperienceModel> {
        val raw = dataSource.fetchExperiences()
        return json.decodeFromString(raw)
    }

    override suspend fun getServices(): List<ServiceModel> {
        val raw = dataSource.fetchServices()
        return json.decodeFromString(raw)
    }

    override suspend fun getSkills(): SkillsModel {
        val raw = dataSource.fetchSkills()
        return json.decodeFromString(raw)
    }
}
