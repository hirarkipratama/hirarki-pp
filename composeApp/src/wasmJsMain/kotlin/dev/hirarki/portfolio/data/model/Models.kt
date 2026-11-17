package dev.hirarki.portfolio.data.model

import kotlinx.serialization.Serializable

@Serializable
data class MetricModel(
    val label: String = "",
    val value: Double = 0.0,
    val suffix: String = ""
)

@Serializable
data class ProfileModel(
    val name: String = "",
    val title: String = "",
    val tagline: String = "",
    val location: String = "",
    val email: String = "",
    val whatsapp: String = "",
    val github: String = "",
    val linkedin: String = "",
    val twitter: String = "",
    val cvUrl: String = "",
    val avatarUrl: String = "",
    val heroSubheadline: String = "",
    val metrics: List<MetricModel> = emptyList(),
    val stackChips: List<String> = emptyList()
)

@Serializable
data class ScreenshotModel(
    val image: String = "",
    val alt: String = "",
    val caption: String? = null
)

@Serializable
data class ProjectMetricModel(
    val label: String = "",
    val value: String = ""
)

@Serializable
data class ProjectModel(
    val slug: String = "",
    val title: String = "",
    val category: String = "",
    val summary: String = "",
    val problem: String? = null,
    val solution: String? = null,
    val impact: String? = null,
    val bannerUrl: String = "",
    val screenshots: List<ScreenshotModel> = emptyList(),
    val technologies: List<String> = emptyList(),
    val metrics: List<ProjectMetricModel> = emptyList(),
    val accentColor: String = "#00F0FF",
    val platform: List<String> = emptyList(),
    val githubUrl: String? = null,
    val caseStudyUrl: String? = null,
    val featured: Boolean = false
)

@Serializable
data class ExperienceModel(
    val id: String = "",
    val role: String = "",
    val company: String = "",
    val companyType: String = "",
    val period: String = "",
    val description: String = "",
    val achievements: List<String> = emptyList(),
    val current: Boolean = false,
    val accentColor: String = "#00F0FF"
)

@Serializable
data class ServiceModel(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val features: List<String> = emptyList(),
    val iconKey: String = ""
)

@Serializable
data class SkillCategoryModel(
    val name: String = "",
    val skills: List<String> = emptyList()
)

@Serializable
data class SkillsModel(
    val categories: List<SkillCategoryModel> = emptyList()
)
