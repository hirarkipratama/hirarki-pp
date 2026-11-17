# 🏛 Architecture — Hirarki Wendri · KMP Portfolio (Web Only)

> Single-module Compose Multiplatform project targeting **wasmJs** exclusively.  
> Clean Architecture layers: Data → Domain → Presentation → UI.

---

## 1. Module Structure

```
kmp-mobile-portofolio/
└── composeApp/              ← single CMP module, wasmJs target only
    └── src/
        ├── wasmJsMain/      ← web-platform code (fetch, browser interop)
        └── commonMain/      ← shared Kotlin code + resources
```

> [!NOTE]
> We use **one module** because this is a web-only project. No `androidMain` or `iosMain` source sets are configured — keeping the build fast and dependency graph simple.

---

## 2. Layer Diagram

```
┌─────────────────────────────────────────────────────────┐
│                        UI LAYER                         │
│  Composable Sections + Components (Compose for Web)     │
│  HeroSection · ProjectsSection · ContactSection · …     │
└───────────────────────┬─────────────────────────────────┘
                        │ collectAsState()
┌───────────────────────▼─────────────────────────────────┐
│                  PRESENTATION LAYER                      │
│  PortfolioViewModel  ←→  PortfolioUiState (sealed)      │
│  StateFlow<PortfolioUiState>                             │
└───────────────────────┬─────────────────────────────────┘
                        │ invoke UseCase
┌───────────────────────▼─────────────────────────────────┐
│                   DOMAIN LAYER                           │
│  GetProfileUseCase · GetProjectsUseCase                  │
│  GetExperiencesUseCase · GetServicesUseCase              │
│  (Pure Kotlin, no framework dependencies)                │
└───────────────────────┬─────────────────────────────────┘
                        │ call Repository
┌───────────────────────▼─────────────────────────────────┐
│                    DATA LAYER                            │
│  PortfolioRepository (interface + impl)                  │
│  JsonAssetDataSource (fetch JSON from /assets/)          │
│  kotlinx.serialization for parsing                       │
└─────────────────────────────────────────────────────────┘
```

---

## 3. File-by-File Architecture

### 3.1 Entry Point

**`main.kt`** (`wasmJsMain`)
```kotlin
fun main() {
    onWasmReady {
        CanvasBasedWindow(title = "Hirarki Wendri · Portfolio") {
            App()
        }
    }
}
```

### 3.2 Root App Composable

**`App.kt`**
```kotlin
@Composable
fun App() {
    PortfolioTheme {
        val viewModel = remember { PortfolioViewModel(AppModule.portfolioRepository) }
        val uiState by viewModel.uiState.collectAsState()
        
        PortfolioScaffold(uiState = uiState)
    }
}
```

### 3.3 Manual DI

**`AppModule.kt`** — no reflection-based DI (wasmJs constraint)
```kotlin
object AppModule {
    val jsonDataSource: JsonAssetDataSource = JsonAssetDataSourceImpl()
    val portfolioRepository: PortfolioRepository = PortfolioRepositoryImpl(jsonDataSource)
}
```

---

## 4. Data Layer Detail

### 4.1 JsonAssetDataSource

```kotlin
interface JsonAssetDataSource {
    suspend fun fetchProfile(): String          // raw JSON string
    suspend fun fetchProjects(): String
    suspend fun fetchExperiences(): String
    suspend fun fetchServices(): String
    suspend fun fetchSkills(): String
}
```

**`JsonAssetDataSourceImpl`** (wasmJs-specific):
- Uses `kotlinx.browser.window.fetch(url)` returning a `Promise<Response>`
- Bridges to coroutines via `await()` extension
- Base URL resolved from `window.location.origin`

### 4.2 Repository

```kotlin
interface PortfolioRepository {
    suspend fun getProfile(): ProfileModel
    suspend fun getProjects(): List<ProjectModel>
    suspend fun getExperiences(): List<ExperienceModel>
    suspend fun getServices(): List<ServiceModel>
}
```

Impl parses raw JSON strings with `Json.decodeFromString<T>()`.

### 4.3 Data Models (`kotlinx.serialization`)

```kotlin
@Serializable
data class ProfileModel(
    val name: String,
    val title: String,
    val tagline: String,
    val location: String,
    val email: String,
    val github: String,
    val linkedin: String,
    val cvUrl: String,
    val avatarUrl: String,
    val metrics: List<MetricModel>
)

@Serializable
data class ScreenshotModel(
    val image: String,
    val alt: String,
    val caption: String? = null
)

@Serializable
data class ProjectMetricModel(
    val label: String,
    val value: String
)

@Serializable
data class ProjectModel(
    val slug: String,
    val title: String,
    val category: String,
    val summary: String,
    val problem: String? = null,
    val solution: String? = null,
    val impact: String? = null,
    val bannerUrl: String,
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
    val id: String,
    val role: String,
    val company: String,
    val period: String,
    val achievements: List<String>,
    val current: Boolean = false
)

@Serializable
data class ServiceModel(
    val id: String,
    val title: String,
    val description: String,
    val iconKey: String
)

@Serializable
data class MetricModel(
    val label: String,
    val value: Double,
    val suffix: String
)
```

---

## 5. Presentation Layer Detail

### 5.1 UI State

```kotlin
sealed interface PortfolioUiState {
    data object Loading : PortfolioUiState
    data class Success(
        val profile: ProfileModel,
        val projects: List<ProjectModel>,
        val experiences: List<ExperienceModel>,
        val services: List<ServiceModel>
    ) : PortfolioUiState
    data class Error(val message: String) : PortfolioUiState
}
```

### 5.2 ViewModel

```kotlin
class PortfolioViewModel(
    private val repository: PortfolioRepository
) {
    private val _uiState = MutableStateFlow<PortfolioUiState>(PortfolioUiState.Loading)
    val uiState: StateFlow<PortfolioUiState> = _uiState.asStateFlow()

    init {
        loadAll()
    }

    private fun loadAll() {
        // Uses CoroutineScope tied to composition lifecycle
        CoroutineScope(Dispatchers.Main).launch {
            runCatching {
                val profile = repository.getProfile()
                val projects = repository.getProjects()
                val experiences = repository.getExperiences()
                val services = repository.getServices()
                PortfolioUiState.Success(profile, projects, experiences, services)
            }.fold(
                onSuccess = { _uiState.value = it },
                onFailure = { _uiState.value = PortfolioUiState.Error(it.message ?: "Unknown error") }
            )
        }
    }
}
```

---

## 6. UI Layer Detail

### 6.1 Theme System

**`Color.kt`**
```kotlin
object ObsidianColors {
    val Background    = Color(0xFF0C0E14)
    val Surface       = Color(0xFF111319)
    val Container     = Color(0xFF191B22)
    val ContainerMid  = Color(0xFF23252E)
    val ContainerHigh = Color(0xFF373940)
    val Primary       = Color(0xFF00F0FF)   // Neon Cyan
    val Secondary     = Color(0xFFFF007A)   // Cyber Magenta
    val OnSurface     = Color(0xFFE8EAED)
    val OnSurfaceDim  = Color(0xFF9AA0B2)
    val Border        = Color(0x14FFFFFF)   // rgba(255,255,255,0.08)
}
```

**`Typography.kt`** — Google Fonts via HTML link tag in `index.html`:
```kotlin
val PortfolioTypography = Typography(
    displayLarge = TextStyle(fontFamily = SpaceGroteskFamily, fontWeight = Bold, fontSize = 56.sp),
    headlineMedium = TextStyle(fontFamily = SpaceGroteskFamily, fontWeight = SemiBold, fontSize = 32.sp),
    bodyLarge = TextStyle(fontFamily = SpaceGroteskFamily, fontSize = 16.sp),
    labelMedium = TextStyle(fontFamily = JetBrainsMonoFamily, fontSize = 12.sp)
)
```

### 6.2 Core Components

| Component | Purpose |
|-----------|---------|
| `NavBar` | Sticky top bar: HW logo, nav links, Download CV button |
| `NeonButton` | Primary (cyan fill) & Secondary (outline) CTA variants |
| `GlowCard` | Surface card with `1px` neon border + subtle box-glow |
| `TechPill` | Rounded chip for tech stack labels |
| `SectionHeader` | Monospace label + display heading combo |
| `AnimatedCounter` | Count-up animation on scroll-into-view |
| `TerminalText` | Typewriter effect for hero terminal badge |
| `ProjectModal` | Overlay/dialog with project deep-dive details |

### 6.3 Section Composables

| Section | Scroll Anchor | Key Components |
|---------|--------------|----------------|
| `HeroSection` | `#hero` | Portrait image, TerminalText, NeonButton ×2, TechPill strip |
| `MetricsSection` | `#metrics` | 4× AnimatedCounter in horizontal row |
| `AboutSection` | `#about` | Narrative text, TechPill grid by category |
| `ProjectsSection` | `#projects` | 2×2 GlowCard grid, ProjectModal on click |
| `ServicesSection` | `#services` | 4 service GlowCards with tinted vector icons from `Res.drawable` |
| `ExperienceSection` | `#experience` | Vertical timeline with milestone nodes |
| `ContactSection` | `#contact` | Email/WhatsApp/Social links + back-to-top |

---

## 7. Web Interop Layer

### 7.1 CV Download

```kotlin
fun downloadCV(url: String) {
    val a = document.createElement("a") as HTMLAnchorElement
    a.href = url
    a.download = "Hirarki_Wendri_CV.pdf"
    a.click()
}
```

### 7.2 Smooth Scroll

```kotlin
fun scrollToSection(sectionId: String) {
    document.getElementById(sectionId)
        ?.scrollIntoView(js("({ behavior: 'smooth', block: 'start' })"))
}
```

### 7.3 JSON Fetch (wasmJs)

```kotlin
suspend fun fetchJson(path: String): String {
    val response = window.fetch(path).await()
    return response.text().await()
}
```

---

## 8. Build Configuration

### `settings.gradle.kts`
```kotlin
rootProject.name = "kmp-mobile-portofolio"
include(":composeApp")
```

### `composeApp/build.gradle.kts` (key sections)
```kotlin
kotlin {
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        moduleName = "composeApp"
        browser {
            commonWebpackConfig {
                outputFileName = "composeApp.js"
            }
        }
        binaries.executable()
    }

    sourceSets {
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.kotlinx.coroutines.core)
        }
        wasmJsMain.dependencies {
            implementation(libs.kotlinx.coroutines.core.wasm)
        }
    }
}
```

### `gradle/libs.versions.toml`
```toml
[versions]
kotlin = "2.1.0"
compose-multiplatform = "1.7.3"
kotlinx-serialization = "1.7.3"
kotlinx-coroutines = "1.9.0"

[libraries]
kotlinx-serialization-json = { module = "org.jetbrains.kotlinx:kotlinx-serialization-json", version.ref = "kotlinx-serialization" }
kotlinx-coroutines-core = { module = "org.jetbrains.kotlinx:kotlinx-coroutines-core", version.ref = "kotlinx-coroutines" }

[plugins]
compose-multiplatform = { id = "org.jetbrains.compose", version.ref = "compose-multiplatform" }
kotlin-multiplatform = { id = "org.jetbrains.kotlin.multiplatform", version.ref = "kotlin" }
kotlin-serialization = { id = "org.jetbrains.kotlin.plugin.serialization", version.ref = "kotlin" }
kotlin-compose = { id = "org.jetbrains.kotlin.plugin.compose", version.ref = "kotlin" }
```

---

## 9. CI/CD Pipeline (GitHub Actions)

```yaml
# .github/workflows/deploy.yml
on:
  push:
    branches: [main]

jobs:
  build-and-deploy:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-java@v4
        with: { java-version: '17', distribution: 'temurin' }
      - name: Build WASM
        run: ./gradlew :composeApp:wasmJsBrowserDistribution
      - name: Deploy to GitHub Pages
        uses: peaceiris/actions-gh-pages@v4
        with:
          github_token: ${{ secrets.GITHUB_TOKEN }}
          publish_dir: composeApp/build/dist/wasmJs/productionExecutable
```

---

## 10. Performance Strategy

| Concern | Solution |
|---------|---------|
| WASM initial load | Split `index.html` with preload hints for `.wasm` |
| Font FOIT | `font-display: swap` in CSS, preconnect to fonts.googleapis.com |
| Image optimization | All images as `.webp`, lazy load via `AsyncImage` equivalent |
| CLS prevention | Explicit width/height on all images + skeleton placeholders |
| JSON loading | Parallel `async/await` fetch all JSON on startup |
| Bundle size | `--release` Webpack production mode, tree-shaking |
