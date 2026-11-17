package dev.hirarki.portfolio.di

import dev.hirarki.portfolio.data.datasource.JsonAssetDataSource
import dev.hirarki.portfolio.data.datasource.JsonAssetDataSourceImpl
import dev.hirarki.portfolio.data.repository.PortfolioRepository
import dev.hirarki.portfolio.data.repository.PortfolioRepositoryImpl
import dev.hirarki.portfolio.domain.usecase.GetExperiencesUseCase
import dev.hirarki.portfolio.domain.usecase.GetProfileUseCase
import dev.hirarki.portfolio.domain.usecase.GetProjectsUseCase
import dev.hirarki.portfolio.domain.usecase.GetServicesUseCase
import dev.hirarki.portfolio.domain.usecase.GetSkillsUseCase

object AppModule {
    val dataSource: JsonAssetDataSource = JsonAssetDataSourceImpl()
    val repository: PortfolioRepository = PortfolioRepositoryImpl(dataSource)
    val getProfile = GetProfileUseCase(repository)
    val getProjects = GetProjectsUseCase(repository)
    val getExperiences = GetExperiencesUseCase(repository)
    val getServices = GetServicesUseCase(repository)
    val getSkills = GetSkillsUseCase(repository)
}
