package dev.hirarki.portfolio.presentation.state

import dev.hirarki.portfolio.data.model.ExperienceModel
import dev.hirarki.portfolio.data.model.ProfileModel
import dev.hirarki.portfolio.data.model.ProjectModel
import dev.hirarki.portfolio.data.model.ServiceModel
import dev.hirarki.portfolio.data.model.SkillsModel

sealed interface PortfolioUiState {
    data object Loading : PortfolioUiState
    data class Success(
        val profile: ProfileModel,
        val projects: List<ProjectModel>,
        val experiences: List<ExperienceModel>,
        val services: List<ServiceModel>,
        val skills: SkillsModel
    ) : PortfolioUiState
    data class Error(val message: String) : PortfolioUiState
}
