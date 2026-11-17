package dev.hirarki.portfolio.presentation.viewmodel

import dev.hirarki.portfolio.data.model.ProjectModel
import dev.hirarki.portfolio.domain.usecase.GetExperiencesUseCase
import dev.hirarki.portfolio.domain.usecase.GetProfileUseCase
import dev.hirarki.portfolio.domain.usecase.GetProjectsUseCase
import dev.hirarki.portfolio.domain.usecase.GetServicesUseCase
import dev.hirarki.portfolio.domain.usecase.GetSkillsUseCase
import dev.hirarki.portfolio.presentation.state.PortfolioUiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PortfolioViewModel(
    private val getProfileUseCase: GetProfileUseCase,
    private val getProjectsUseCase: GetProjectsUseCase,
    private val getExperiencesUseCase: GetExperiencesUseCase,
    private val getServicesUseCase: GetServicesUseCase,
    private val getSkillsUseCase: GetSkillsUseCase,
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Main)
) {
    private val _uiState = MutableStateFlow<PortfolioUiState>(PortfolioUiState.Loading)
    val uiState: StateFlow<PortfolioUiState> = _uiState.asStateFlow()

    private val _selectedProject = MutableStateFlow<ProjectModel?>(null)
    val selectedProject: StateFlow<ProjectModel?> = _selectedProject.asStateFlow()

    init {
        loadPortfolio()
    }

    fun loadPortfolio() {
        _uiState.value = PortfolioUiState.Loading
        coroutineScope.launch {
            try {
                val profileDeferred = async { getProfileUseCase() }
                val projectsDeferred = async { getProjectsUseCase() }
                val experiencesDeferred = async { getExperiencesUseCase() }
                val servicesDeferred = async { getServicesUseCase() }
                val skillsDeferred = async { getSkillsUseCase() }

                val profile = profileDeferred.await()
                val projects = projectsDeferred.await()
                val experiences = experiencesDeferred.await()
                val services = servicesDeferred.await()
                val skills = skillsDeferred.await()

                _uiState.value = PortfolioUiState.Success(
                    profile = profile,
                    projects = projects,
                    experiences = experiences,
                    services = services,
                    skills = skills
                )
            } catch (t: Throwable) {
                _uiState.value = PortfolioUiState.Error(t.message ?: "Failed to load portfolio data")
            }
        }
    }

    fun selectProject(project: ProjectModel) {
        _selectedProject.value = project
    }

    fun dismissProjectModal() {
        _selectedProject.value = null
    }
}
