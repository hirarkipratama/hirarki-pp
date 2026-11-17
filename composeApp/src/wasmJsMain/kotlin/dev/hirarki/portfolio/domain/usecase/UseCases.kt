package dev.hirarki.portfolio.domain.usecase

import dev.hirarki.portfolio.data.model.ExperienceModel
import dev.hirarki.portfolio.data.model.ProfileModel
import dev.hirarki.portfolio.data.model.ProjectModel
import dev.hirarki.portfolio.data.model.ServiceModel
import dev.hirarki.portfolio.data.model.SkillsModel
import dev.hirarki.portfolio.data.repository.PortfolioRepository

class GetProfileUseCase(
    private val repository: PortfolioRepository
) {
    suspend operator fun invoke(): ProfileModel = repository.getProfile()
}

class GetProjectsUseCase(
    private val repository: PortfolioRepository
) {
    suspend operator fun invoke(): List<ProjectModel> = repository.getProjects()
}

class GetExperiencesUseCase(
    private val repository: PortfolioRepository
) {
    suspend operator fun invoke(): List<ExperienceModel> = repository.getExperiences()
}

class GetServicesUseCase(
    private val repository: PortfolioRepository
) {
    suspend operator fun invoke(): List<ServiceModel> = repository.getServices()
}

class GetSkillsUseCase(
    private val repository: PortfolioRepository
) {
    suspend operator fun invoke(): SkillsModel = repository.getSkills()
}
