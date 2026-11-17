package dev.hirarki.portfolio.data.repository

import dev.hirarki.portfolio.data.datasource.JsonAssetDataSource
import dev.hirarki.portfolio.data.model.ExperienceModel
import dev.hirarki.portfolio.data.model.ProfileModel
import dev.hirarki.portfolio.data.model.ProjectModel
import dev.hirarki.portfolio.data.model.ServiceModel

class PortfolioRepositoryImpl(
    private val dataSource: JsonAssetDataSource? = null
) : PortfolioRepository {
    override suspend fun getProfile(): ProfileModel = ProfileModel()
    override suspend fun getProjects(): List<ProjectModel> = emptyList()
    override suspend fun getExperiences(): List<ExperienceModel> = emptyList()
    override suspend fun getServices(): List<ServiceModel> = emptyList()
}
