package dev.hirarki.portfolio.data.datasource

interface JsonAssetDataSource {
    suspend fun fetchProfile(): String
    suspend fun fetchProjects(): String
    suspend fun fetchExperiences(): String
    suspend fun fetchServices(): String
    suspend fun fetchSkills(): String
}
