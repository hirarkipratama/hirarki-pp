package dev.hirarki.portfolio.presentation.viewmodel

import dev.hirarki.portfolio.data.repository.PortfolioRepository
import dev.hirarki.portfolio.presentation.state.PortfolioUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PortfolioViewModel(
    private val repository: PortfolioRepository? = null
) {
    private val _uiState = MutableStateFlow<PortfolioUiState>(PortfolioUiState.Loading)
    val uiState: StateFlow<PortfolioUiState> = _uiState.asStateFlow()
}
