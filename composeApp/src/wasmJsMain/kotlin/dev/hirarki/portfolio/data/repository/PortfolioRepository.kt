package dev.hirarki.portfolio.data.repository

import dev.hirarki.portfolio.data.model.ExperienceModel
import dev.hirarki.portfolio.data.model.ProfileModel
import dev.hirarki.portfolio.data.model.ProjectModel
import dev.hirarki.portfolio.data.model.ServiceModel
import dev.hirarki.portfolio.data.model.SkillsModel

interface PortfolioRepository {
    suspend fun getProfile(): ProfileModel
    suspend fun getProjects(): List<ProjectModel>
    suspend fun getExperiences(): List<ExperienceModel>
    suspend fun getServices(): List<ServiceModel>
    suspend fun getSkills(): SkillsModel
}
