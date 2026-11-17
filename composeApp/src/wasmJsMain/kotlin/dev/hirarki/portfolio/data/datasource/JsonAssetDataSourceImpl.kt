package dev.hirarki.portfolio.data.datasource

class JsonAssetDataSourceImpl : JsonAssetDataSource {
    override suspend fun fetchProfile(): String = ""
    override suspend fun fetchProjects(): String = ""
    override suspend fun fetchExperiences(): String = ""
    override suspend fun fetchServices(): String = ""
    override suspend fun fetchSkills(): String = ""
}
