package dev.hirarki.portfolio.domain.usecase

import dev.hirarki.portfolio.data.model.ExperienceModel
import dev.hirarki.portfolio.data.model.ProfileModel
import dev.hirarki.portfolio.data.model.ProjectModel
import dev.hirarki.portfolio.data.model.ServiceModel
import dev.hirarki.portfolio.data.repository.PortfolioRepository

class GetProfileUseCase(
    private val repository: PortfolioRepository? = null
) {
    suspend operator fun invoke(): ProfileModel = repository?.getProfile() ?: ProfileModel()
}

class GetProjectsUseCase(
    private val repository: PortfolioRepository? = null
) {
    suspend operator fun invoke(): List<ProjectModel> = repository?.getProjects() ?: emptyList()
}

class GetExperiencesUseCase(
    private val repository: PortfolioRepository? = null
) {
    suspend operator fun invoke(): List<ExperienceModel> = repository?.getExperiences() ?: emptyList()
}

class GetServicesUseCase(
    private val repository: PortfolioRepository? = null
) {
    suspend operator fun invoke(): List<ServiceModel> = repository?.getServices() ?: emptyList()
}
