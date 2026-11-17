# 🤖 PROMPTS.md — Step-by-Step AI Prompts for KMP Portfolio
> **Project:** Hirarki Wendri · Mobile Developer Portfolio  
> **Stack:** Kotlin Multiplatform + Compose Multiplatform (wasmJs Web only)  
> **Design:** Obsidian Cyber-Core (Dark) · Stitch Project `11677376707151998401`  
> **Reference:** `PLAN.md` · `ARCHITECTURE.md` · `portfolio_prd_project_brief.md`

Use each prompt block in sequence. Each prompt is self-contained with context.  
Run prompts inside your AI coding assistant (Antigravity/Cursor/etc.) from the project root.

---

## ─────────────────────────────────────
## PHASE 0 — Project Scaffolding & Toolchain
## ─────────────────────────────────────

### PROMPT 0.1 — Initialize KMP/CMP Project (Web Only)

```
You are setting up a Kotlin Multiplatform + Compose Multiplatform project 
targeting wasmJs (web only). No Android or iOS targets are needed.

Project name: kmp-mobile-portofolio
Package: dev.hirarki.portfolio
Module: composeApp

Create the following files:

1. `settings.gradle.kts`:
   - Set rootProject.name = "kmp-mobile-portofolio"
   - Include ":composeApp"
   - Add plugin management with Kotlin and Compose Multiplatform repositories

2. `build.gradle.kts` (root):
   - Apply compose-multiplatform, kotlin-multiplatform, kotlin-serialization,
     and kotlin-compose plugins with `apply false`

3. `gradle/libs.versions.toml`:
   - kotlin = "2.1.0"
   - compose-multiplatform = "1.7.3"
   - kotlinx-serialization = "1.7.3"
   - kotlinx-coroutines = "1.9.0"
   - Define all library and plugin aliases

4. `composeApp/build.gradle.kts`:
   - Apply kotlin("multiplatform"), compose-multiplatform, kotlin-compose,
     and kotlin-serialization plugins
   - Configure wasmJs target ONLY:
     - moduleName = "composeApp"
     - browser { commonWebpackConfig { outputFileName = "composeApp.js" } }
     - binaries.executable()
   - commonMain dependencies:
     - compose.runtime, compose.foundation, compose.material3, compose.ui
     - compose.components.resources
     - kotlinx-serialization-json
     - kotlinx-coroutines-core
   - wasmJsMain dependencies:
     - kotlinx-coroutines-core (wasm variant if needed)

5. `composeApp/src/wasmJsMain/resources/index.html`:
   - Load Space Grotesk + JetBrains Mono from Google Fonts (preconnect)
   - Reference composeApp.js as module
   - Set base meta tags (viewport, description, og:title)
   - Dark background #0C0E14 as body background to avoid FOUC

6. `composeApp/src/wasmJsMain/kotlin/dev/hirarki/portfolio/main.kt`:
   - Import onWasmReady and CanvasBasedWindow
   - Entry point that calls App()

Confirm the project builds with: ./gradlew :composeApp:wasmJsBrowserDevelopmentRun
```

---

### PROMPT 0.2 — Create Directory Structure & Empty Stubs

```
In the project `kmp-mobile-portofolio`, create the complete directory structure
and empty stub files as defined in ARCHITECTURE.md.

Under `composeApp/src/wasmJsMain/kotlin/dev/hirarki/portfolio/` create:
- di/AppModule.kt (empty object)
- data/model/ (empty data class stubs: ProfileModel, ProjectModel,
  ScreenshotModel, ProjectMetricModel,
  ExperienceModel, ServiceModel, SkillModel, MetricModel)
- data/repository/PortfolioRepository.kt (empty interface)
- data/repository/PortfolioRepositoryImpl.kt (empty class)
- data/datasource/JsonAssetDataSource.kt (empty interface)
- data/datasource/JsonAssetDataSourceImpl.kt (empty class)
- domain/usecase/ (GetProfileUseCase, GetProjectsUseCase,
  GetExperiencesUseCase, GetServicesUseCase — all empty)
- presentation/viewmodel/PortfolioViewModel.kt (empty class)
- presentation/state/PortfolioUiState.kt (empty sealed interface)
- ui/theme/ (Color.kt, Typography.kt, Shapes.kt, Theme.kt — all empty)
- ui/components/ (NavBar, NeonButton, TechPill, SectionHeader, GlowCard,
  TerminalText, AnimatedCounter, ProjectCard, ProjectScreenshot,
  ScreenshotGallery, ProjectModal — all empty Composables)
- ui/sections/ (HeroSection, MetricsSection, AboutSection, ProjectsSection,
  ServicesSection, ExperienceSection, ContactSection — all empty Composables)
- App.kt (minimal Composable returning Box {})

Under `composeApp/src/commonMain/resources/assets/` create the following
directory tree with placeholder files:

assets/
├── data/
│   ├── profile.json          ← empty {}
│   ├── projects.json         ← empty []
│   ├── experiences.json      ← empty []
│   ├── services.json         ← empty []
│   └── skills.json           ← empty {}
├── images/
│   ├── avatar.webp           ← placeholder (developer profile photo)
│   └── projects/
│       ├── paypulse/
│       │   ├── banner.webp               ← multi-screenshot composite banner
│       │   ├── screen_home.webp          ← portrait mobile screenshot
│       │   ├── screen_wallet.webp
│       │   └── screen_transaction.webp
│       ├── caresync/
│       │   ├── banner.webp
│       │   ├── screen_dashboard.webp
│       │   └── screen_vitals.webp
│       ├── aurastore/
│       │   ├── banner.webp
│       │   ├── screen_home.webp
│       │   └── screen_ar.webp
│       └── logiroute/
│           ├── banner.webp
│           ├── screen_map.webp
│           └── screen_dispatch.webp
└── cv/
    └── hirarki_cv.pdf        ← placeholder CV file

IMPORTANT: All screenshot images are PORTRAIT mobile screenshots.
Do NOT create landscape thumbnails. The banner.webp files are horizontally
composed multi-screenshot composite images (3 portrait screens side-by-side
on a dark gradient background) — these are created by the developer, not
generated by code.

Add a README inside assets/images/projects/ explaining:
- Each project subfolder contains portrait mobile screenshots
- banner.webp = composite of 2-3 portrait screenshots for the card hero area
- Recommended screenshot dimensions: 390×844px (iPhone) or 360×800px (Android)
- All files should be .webp for performance

Verify the project still compiles after adding stubs.
```

---

## ─────────────────────────────────────
## PHASE 1 — Design System
## ─────────────────────────────────────

### PROMPT 1.1 — Implement Obsidian Cyber-Core Color Palette

```
In `composeApp/src/wasmJsMain/kotlin/dev/hirarki/portfolio/ui/theme/Color.kt`,
implement the full Obsidian Cyber-Core color palette for Compose Multiplatform.

Create an object `ObsidianColors` with these exact values:
- Background    = Color(0xFF0C0E14)
- Surface       = Color(0xFF111319)
- Container     = Color(0xFF191B22)
- ContainerMid  = Color(0xFF23252E)
- ContainerHigh = Color(0xFF373940)
- Primary       = Color(0xFF00F0FF)   // Neon Cyan
- PrimaryDim    = Color(0xFF00E5FF)
- Secondary     = Color(0xFFFF007A)   // Cyber Magenta
- OnSurface     = Color(0xFFE8EAED)
- OnSurfaceDim  = Color(0xFF9AA0B2)
- Border        = Color(0x14FFFFFF)   // rgba(255,255,255,0.08)
- GlowCyan      = Color(0x3300F0FF)   // cyan with 20% alpha for glow effects
- GlowMagenta   = Color(0x33FF007A)

Then in `Theme.kt`, create a `PortfolioTheme` Composable that:
1. Wraps content in MaterialTheme with a custom darkColorScheme using ObsidianColors
2. Sets CompositionLocalProvider for any custom locals needed
3. Maps: background→Background, surface→Surface, primary→Primary,
   secondary→Secondary, onSurface→OnSurface, surfaceContainer→Container

Do NOT use lightColorScheme. This is a dark-only theme.
```

---

### PROMPT 1.2 — Typography System

```
In `composeApp/src/wasmJsMain/kotlin/dev/hirarki/portfolio/ui/theme/Typography.kt`,
implement the typography system using Google Fonts loaded via index.html.

Since Compose Multiplatform for WASM loads fonts from the HTML, declare:
1. A `SpaceGroteskFamily` FontFamily using generic `FontFamily.SansSerif` as the
   base (the actual Space Grotesk will be applied via CSS fallback from index.html)
2. A `JetBrainsMonoFamily` FontFamily using `FontFamily.Monospace` as base

Create `PortfolioTypography` with these styles:
- displayLarge:   SpaceGrotesk, Bold, 56.sp, letterSpacing = (-0.02).em
- displayMedium:  SpaceGrotesk, Bold, 44.sp, letterSpacing = (-0.02).em  
- headlineLarge:  SpaceGrotesk, SemiBold, 36.sp
- headlineMedium: SpaceGrotesk, SemiBold, 28.sp
- headlineSmall:  SpaceGrotesk, SemiBold, 22.sp
- titleLarge:     SpaceGrotesk, Medium, 20.sp
- titleMedium:    SpaceGrotesk, Medium, 16.sp
- bodyLarge:      SpaceGrotesk, Normal, 16.sp, lineHeight = 28.sp
- bodyMedium:     SpaceGrotesk, Normal, 14.sp, lineHeight = 24.sp
- labelLarge:     JetBrainsMono, Normal, 14.sp, letterSpacing = (0.05).em
- labelMedium:    JetBrainsMono, Normal, 12.sp, letterSpacing = (0.05).em
- labelSmall:     JetBrainsMono, Normal, 10.sp

Update `index.html` to include:
<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link href="https://fonts.googleapis.com/css2?family=Space+Grotesk:wght@300;400;500;600;700&family=JetBrains+Mono:wght@400;500&display=swap" rel="stylesheet">

And add CSS to apply these font families:
* { font-family: 'Space Grotesk', sans-serif; }
code, .mono { font-family: 'JetBrains Mono', monospace; }
```

---

### PROMPT 1.3 — Reusable UI Components Library

```
Implement the core reusable UI components for the Obsidian Cyber-Core design
system in `composeApp/src/wasmJsMain/kotlin/dev/hirarki/portfolio/ui/components/`.

Implement each component:

1. **GlowCard.kt** — Card container with neon glow
   - Parameters: modifier, glowColor (default ObsidianColors.GlowCyan), 
     borderColor (ObsidianColors.Border), content lambda
   - Surface with RoundedCornerShape(16.dp), background = Container
   - Draw 1px border with borderColor
   - Add subtle box shadow / glow using drawBehind with glowColor

2. **NeonButton.kt** — CTA button in Primary and Secondary variants
   - Parameters: text, onClick, variant (PRIMARY/SECONDARY), modifier, icon
   - PRIMARY: filled with ObsidianColors.Primary, black text, rounded-full
   - SECONDARY: outlined with ObsidianColors.Primary border, Primary text
   - Both have hover state: scale 1.02, brightness increase
   - Include animated press ripple effect

3. **TechPill.kt** — Technology label chip
   - Parameters: label, color (optional accent override), modifier
   - Rounded pill shape (50.dp radius), Container background
   - 1px Primary border, Primary text in labelMedium style
   - Compact padding: horizontal 12.dp, vertical 4.dp

4. **SectionHeader.kt** — Section title with monospace label
   - Parameters: label (e.g. "01."), title, subtitle (optional), modifier
   - label: labelLarge style in Primary color  
   - title: headlineLarge style in OnSurface
   - subtitle: bodyMedium style in OnSurfaceDim
   - Vertical arrangement with 4.dp spacing

5. **AnimatedCounter.kt** — Animated number counter
   - Parameters: targetValue (Double), suffix (String), label, durationMs (Int = 2000)
   - Uses animateFloatAsState or manual animation loop
   - On first composition (or scroll-into-view), animates from 0 to targetValue
   - Displays formatted number (Int for whole numbers, 1 decimal for decimals)
   - Large displayMedium Primary text + labelMedium OnSurfaceDim label below

6. **TerminalText.kt** — Typewriter animated text
   - Parameters: texts (List<String>), typingSpeedMs (Int = 80), modifier
   - Cycles through texts with typewriter effect
   - Renders with JetBrainsMono in Primary color  
   - Shows blinking cursor character `█` at end

7. **NavBar.kt** — Sticky top navigation
   - Parameters: onNavClick (sectionId: String) → Unit, onDownloadCV () → Unit
   - `HW` monogram logo on left in Primary color, Space Grotesk Bold
   - Center nav links: About, Projects, Services, Experience, Contact
   - Right: "Download CV" NeonButton SECONDARY variant
   - Sticky positioned, Surface background with bottom border in Border color
   - On mobile (<600dp): collapse center links into hamburger menu icon

Each component must:
- Use ObsidianColors and PortfolioTypography
- Be fully in Compose Multiplatform (no Android-specific APIs)
- Include a @Preview annotation with dark background for IDE preview
```

---

## ─────────────────────────────────────
## PHASE 2 — JSON CMS Data Layer
## ─────────────────────────────────────

### PROMPT 2.1 — JSON Asset Files (Content)

```
Populate all JSON CMS asset files in
`composeApp/src/commonMain/resources/assets/data/` with real portfolio content
based on the PRD in `portfolio_prd_project_brief.md`.

IMPORTANT — Local Asset Conventions:
- All images are stored locally under `assets/images/`
- Project images live under `assets/images/projects/<slug>/`
- Each project has a `banner.webp` (multi-screenshot composite) AND
  individual portrait mobile screenshot files (screen_*.webp)
- ALL screenshots are PORTRAIT mobile screenshots (never landscape crops)
- The developer provides real screenshot files — do NOT invent image paths
  that don't correspond to real files

1. **profile.json** — Use this exact structure:
{
  "name": "Hirarki Ardi Pratama Wendri",
  "title": "Middle Mobile Developer",
  "tagline": "Engineering Scalable High-Performance Mobile Experiences",
  "location": "Jakarta, Indonesia",
  "email": "hirarki@example.com",
  "whatsapp": "+62811000000",
  "github": "https://github.com/hirarki",
  "linkedin": "https://linkedin.com/in/hirarki",
  "twitter": "https://twitter.com/hirarki",
  "cvUrl": "/assets/cv/hirarki_cv.pdf",
  "avatarUrl": "/assets/images/avatar.webp",
  "heroSubheadline": "Specialized in Flutter · Kotlin · Swift · BLoC · Clean Architecture",
  "metrics": [
    { "label": "Years Experience", "value": 4, "suffix": "+" },
    { "label": "Apps Shipped", "value": 5, "suffix": "+" },
    { "label": "Crash-Free Sessions", "value": 99.8, "suffix": "%" },
    { "label": "Critical CVEs", "value": 0, "suffix": "" }
  ],
  "stackChips": ["Flutter 3.x", "Kotlin & Compose", "BLoC", "Clean Architecture", "Swift"]
}

2. **projects.json** — Array of 4 projects: PayPulse, CareSync, AuraStore, LogiRoute.
   Use this EXACT expanded schema for each project (required by the showcase spec):

[
  {
    "slug": "paypulse",
    "title": "PayPulse",
    "category": "Neobank & Crypto Mobile Wallet",
    "summary": "Real-time crypto wallet with QRIS cross-border payments and biometric KYC.",
    "problem": "Users needed a single app for fiat and crypto with compliant KYC and instant cross-border QR payments.",
    "solution": "Built a Flutter/Kotlin app with WebSocket crypto feeds, QRIS gateway integration, and hardware-backed biometric KYC flows.",
    "impact": null,
    "bannerUrl": "/assets/images/projects/paypulse/banner.webp",
    "screenshots": [
      {
        "image": "/assets/images/projects/paypulse/screen_home.webp",
        "alt": "PayPulse home screen showing crypto portfolio balance and market overview",
        "caption": "Portfolio Overview"
      },
      {
        "image": "/assets/images/projects/paypulse/screen_wallet.webp",
        "alt": "PayPulse wallet screen with QRIS payment QR code and transaction history",
        "caption": "QRIS Wallet"
      },
      {
        "image": "/assets/images/projects/paypulse/screen_transaction.webp",
        "alt": "PayPulse transaction detail screen with biometric confirmation prompt",
        "caption": "Biometric Confirm"
      }
    ],
    "technologies": ["Flutter 3.x", "Kotlin", "WebSocket", "QRIS", "Biometrics", "Firebase"],
    "metrics": [
      { "label": "Real-time Feed Latency", "value": "< 50ms" },
      { "label": "KYC Completion Rate", "value": "94%" }
    ],
    "accentColor": "#00F0FF",
    "platform": ["Android", "iOS"],
    "githubUrl": null,
    "caseStudyUrl": null,
    "featured": true
  },
  {
    "slug": "caresync",
    "title": "CareSync",
    "category": "Telehealth & Vitals Monitoring",
    "summary": "Native Android app synchronizing BLE IoT medical sensors with HL7 FHIR compliance.",
    "problem": "Healthcare providers needed a compliant mobile tool to monitor patient vitals from BLE sensors and conduct WebRTC video consultations.",
    "solution": "Developed a Jetpack Compose app with low-energy BLE sensor sync, WebRTC consultation rooms, and HL7 FHIR data export.",
    "impact": null,
    "bannerUrl": "/assets/images/projects/caresync/banner.webp",
    "screenshots": [
      {
        "image": "/assets/images/projects/caresync/screen_dashboard.webp",
        "alt": "CareSync dashboard showing patient vitals cards with real-time BLE sensor data",
        "caption": "Vitals Dashboard"
      },
      {
        "image": "/assets/images/projects/caresync/screen_vitals.webp",
        "alt": "CareSync vitals detail screen with heart rate, SpO2 and temperature charts",
        "caption": "Sensor Detail"
      }
    ],
    "technologies": ["Kotlin", "Jetpack Compose", "BLE", "WebRTC", "HL7 FHIR", "Room"],
    "metrics": [
      { "label": "BLE Sync Interval", "value": "250ms" },
      { "label": "Uptime", "value": "99.9%" }
    ],
    "accentColor": "#FF007A",
    "platform": ["Android"],
    "githubUrl": null,
    "caseStudyUrl": null,
    "featured": true
  },
  {
    "slug": "aurastore",
    "title": "AuraStore",
    "category": "Luxury Fashion & AR Fitting",
    "summary": "iOS e-commerce app with real-time AR product try-on and 120Hz micro-interactions.",
    "problem": "Luxury buyers wanted to virtually try on fashion items before purchase without visiting a physical store.",
    "solution": "Built a Swift/SwiftUI app with ARKit real-time try-on, Stripe Apple Pay integration, and 120Hz spring-animated transitions.",
    "impact": null,
    "bannerUrl": "/assets/images/projects/aurastore/banner.webp",
    "screenshots": [
      {
        "image": "/assets/images/projects/aurastore/screen_home.webp",
        "alt": "AuraStore home screen featuring luxury fashion product grid with editorial layout",
        "caption": "Editorial Home"
      },
      {
        "image": "/assets/images/projects/aurastore/screen_ar.webp",
        "alt": "AuraStore AR fitting screen showing real-time virtual try-on of a fashion item",
        "caption": "AR Try-On"
      }
    ],
    "technologies": ["Swift", "SwiftUI", "ARKit", "Stripe", "Apple Pay", "Combine"],
    "metrics": [
      { "label": "AR Frame Rate", "value": "120 FPS" },
      { "label": "Checkout Conversion", "value": "+28%" }
    ],
    "accentColor": "#00F0FF",
    "platform": ["iOS"],
    "githubUrl": null,
    "caseStudyUrl": null,
    "featured": true
  },
  {
    "slug": "logiroute",
    "title": "LogiRoute",
    "category": "Driver Fleet & Realtime Dispatch",
    "summary": "High-scale driver app with turn-by-turn navigation and battery-efficient background telemetry.",
    "problem": "Fleet managers needed real-time driver location and route optimization without excessive battery drain on driver devices.",
    "solution": "Built a Kotlin/Compose app with Fused Location API, WorkManager background telemetry, and ML-based route optimization.",
    "impact": null,
    "bannerUrl": "/assets/images/projects/logiroute/banner.webp",
    "screenshots": [
      {
        "image": "/assets/images/projects/logiroute/screen_map.webp",
        "alt": "LogiRoute map screen showing driver route with turn-by-turn directions overlay",
        "caption": "Route Navigation"
      },
      {
        "image": "/assets/images/projects/logiroute/screen_dispatch.webp",
        "alt": "LogiRoute dispatch screen showing active deliveries list with status indicators",
        "caption": "Dispatch Board"
      }
    ],
    "technologies": ["Kotlin", "Jetpack Compose", "Fused Location", "WorkManager", "Maps SDK", "WebSocket"],
    "metrics": [
      { "label": "Battery Reduction", "value": "40%" },
      { "label": "ETA Accuracy", "value": "96%" }
    ],
    "accentColor": "#FF007A",
    "platform": ["Android"],
    "githubUrl": null,
    "caseStudyUrl": null,
    "featured": true
  }
]

   RULES for project data:
   - Do NOT invent metrics, App Store links, client names, or business outcomes
   - Set "impact": null if no verified data is available
   - Set "githubUrl": null — only populate when real repo URL exists
   - Set "caseStudyUrl": null — only populate when a real case study link exists
   - Only include screenshots[] entries for image files that actually exist in assets/
   - Every screenshot MUST have meaningful alt text describing what is visible on screen

3. **experiences.json** — Array of 3 experiences from the PRD career section.
   Each has: id, role, company, companyType, period, description,
   achievements[], current (bool), accentColor

4. **services.json** — Array of 4 services from section 3.6 of the PRD.
   Each has: id, title, description, features[], iconKey (string identifier)

5. **skills.json** — Categories object:
{
  "categories": [
    {
      "name": "Mobile Frameworks",
      "skills": ["Flutter 3.x", "Dart", "Kotlin", "Jetpack Compose", "Swift"]
    },
    {
      "name": "State & Architecture",
      "skills": ["BLoC", "Riverpod", "MVVM", "StateFlow", "Clean Architecture"]
    },
    {
      "name": "Backend & Storage",
      "skills": ["Firebase", "Supabase", "REST/Dio", "GraphQL", "SQLite", "Hive/Isar"]
    },
    {
      "name": "Testing & CI/CD",
      "skills": ["Unit Tests", "Widget Tests", "GitHub Actions", "Fastlane", "Sentry"]
    }
  ]
}

Ensure all JSON files are valid. Run through a mental JSON lint before saving.
```

---

### PROMPT 2.2 — Data Models with Serialization

```
Implement all Kotlin data models in
`composeApp/src/wasmJsMain/kotlin/dev/hirarki/portfolio/data/model/`
using `kotlinx.serialization`.

Each file:

1. **MetricModel.kt**: @Serializable data class with label: String, value: Double, suffix: String

2. **ProfileModel.kt**: @Serializable data class with all profile.json fields.
   Include: name, title, tagline, location, email, whatsapp, github, linkedin,
   twitter, cvUrl, avatarUrl, heroSubheadline, metrics: List<MetricModel>,
   stackChips: List<String>

3. **ScreenshotModel.kt**: @Serializable data class for a single mobile screenshot:
   - image: String          ← local asset path, e.g. "/assets/images/projects/paypulse/screen_home.webp"
   - alt: String            ← meaningful description of what is visible on the screen
   - caption: String? = null ← optional short label shown below screenshot

4. **ProjectMetricModel.kt**: @Serializable data class for a project-level key result:
   - label: String          ← e.g. "Real-time Feed Latency"
   - value: String          ← e.g. "< 50ms"  (String, not numeric — values vary)

5. **ProjectModel.kt**: @Serializable data class using the EXPANDED showcase schema:
   - slug: String                        ← kebab-case unique ID
   - title: String
   - category: String                    ← project subtitle/type
   - summary: String                     ← one-liner for the card
   - problem: String? = null             ← only populate when real content available
   - solution: String? = null            ← only populate when real content available
   - impact: String? = null              ← only populate when verified — never invent
   - bannerUrl: String                   ← composite multi-screenshot banner image path
   - screenshots: List<ScreenshotModel>  ← ordered list of PORTRAIT mobile screenshots
   - technologies: List<String>          ← tech stack labels
   - metrics: List<ProjectMetricModel>   ← key results (empty list if no verified data)
   - accentColor: String = "#00F0FF"
   - platform: List<String>             ← e.g. ["Android", "iOS"]
   - githubUrl: String? = null           ← null unless real repo URL exists
   - caseStudyUrl: String? = null        ← null unless real case study link exists
   - featured: Boolean = false

6. **ExperienceModel.kt**: @Serializable data class with id, role, company,
   companyType: String, period: String, description: String,
   achievements: List<String>, current: Boolean = false, accentColor: String

7. **ServiceModel.kt**: @Serializable data class with id, title, description,
   features: List<String>, iconKey: String

8. **SkillCategoryModel.kt**: @Serializable data class with name: String,
   skills: List<String>
   Also **SkillsModel.kt**: @Serializable data class with categories: List<SkillCategoryModel>

Use `@SerialName("fieldName")` where JSON keys differ from Kotlin naming conventions.
All models must use only types supported by kotlinx.serialization.

IMPORTANT design constraint:
- ProjectModel uses `screenshots: List<ScreenshotModel>` NOT a single thumbnailUrl.
- bannerUrl points to the composite banner image (pre-composed by the developer).
- Do NOT model screenshots as landscape thumbnails.
- The Composable layer must NEVER force portrait screenshots into landscape crop.
```

---

### PROMPT 2.3 — Data Source, Repository & Use Cases

```
Implement the data layer for the KMP portfolio in 
`composeApp/src/wasmJsMain/kotlin/dev/hirarki/portfolio/data/`.

1. **JsonAssetDataSource interface** (`datasource/JsonAssetDataSource.kt`):
   Define suspending functions:
   - suspend fun fetchProfile(): String
   - suspend fun fetchProjects(): String
   - suspend fun fetchExperiences(): String
   - suspend fun fetchServices(): String
   - suspend fun fetchSkills(): String

2. **JsonAssetDataSourceImpl** (`datasource/JsonAssetDataSourceImpl.kt`):
   - Import kotlinx.browser.window and kotlin.js.Promise
   - Implement fetch using JS interop:
     ```kotlin
     private suspend fun fetchJson(path: String): String {
         return window.fetch(path).await().text().await()
     }
     ```
   - Each function calls fetchJson with the appropriate asset path:
     "/assets/data/profile.json", etc.
   - Handle the fact that in wasmJs, Promise.await() is available via
     kotlinx.coroutines

3. **PortfolioRepository interface** (`repository/PortfolioRepository.kt`):
   - suspend fun getProfile(): ProfileModel
   - suspend fun getProjects(): List<ProjectModel>
   - suspend fun getExperiences(): List<ExperienceModel>
   - suspend fun getServices(): List<ServiceModel>
   - suspend fun getSkills(): SkillsModel

4. **PortfolioRepositoryImpl** (`repository/PortfolioRepositoryImpl.kt`):
   - Constructor: (private val dataSource: JsonAssetDataSource)
   - Use Json { ignoreUnknownKeys = true } for parsing
   - Each function: fetch raw JSON then decode with Json.decodeFromString<T>()

5. **Use Cases** (`domain/usecase/`):
   For each entity (Profile, Projects, Experiences, Services, Skills):
   - Single class with constructor(private val repository: PortfolioRepository)
   - `suspend operator fun invoke()` returning the appropriate model/list

6. **AppModule** (`di/AppModule.kt`):
   ```kotlin
   object AppModule {
       val dataSource: JsonAssetDataSource = JsonAssetDataSourceImpl()
       val repository: PortfolioRepository = PortfolioRepositoryImpl(dataSource)
       val getProfile = GetProfileUseCase(repository)
       val getProjects = GetProjectsUseCase(repository)
       val getExperiences = GetExperiencesUseCase(repository)
       val getServices = GetServicesUseCase(repository)
       val getSkills = GetSkillsUseCase(repository)
   }
   ```
```

---

### PROMPT 2.4 — ViewModel & UI State

```
Implement the presentation layer in 
`composeApp/src/wasmJsMain/kotlin/dev/hirarki/portfolio/presentation/`.

1. **PortfolioUiState** (`state/PortfolioUiState.kt`):
   ```kotlin
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
   ```

2. **PortfolioViewModel** (`viewmodel/PortfolioViewModel.kt`):
   - Constructor receives all 5 use cases
   - Private MutableStateFlow<PortfolioUiState> starting with Loading
   - Public val uiState: StateFlow<PortfolioUiState>
   - `init { loadPortfolio() }`
   - `private fun loadPortfolio()`:
     - Launch a CoroutineScope(Dispatchers.Main).launch { }
     - Parallel-load all data with `async { }` + `awaitAll()`
     - On success: emit Success state
     - On any failure: emit Error state with message
   - Also track `val selectedProject = MutableStateFlow<ProjectModel?>(null)` 
     for modal state
   - Functions: `fun selectProject(project: ProjectModel)` and 
     `fun dismissProjectModal()`

Note: For wasmJs CoroutineScope, use MainScope() from kotlinx.coroutines
or scope it to composition lifecycle via rememberCoroutineScope in the UI.
Prefer a simple object scope for now.
```

---

## ─────────────────────────────────────
## PHASE 3 — Core UI Sections
## ─────────────────────────────────────

### PROMPT 3.1 — App Root & Navigation Structure

```
Implement the root App composable and scrollable page structure in
`composeApp/src/wasmJsMain/kotlin/dev/hirarki/portfolio/App.kt`.

Requirements:
1. `App()` composable:
   - Create PortfolioViewModel using remember { } with AppModule use cases
   - Collect uiState via collectAsState()
   - Show Loading skeleton when state is Loading
   - Show full page when state is Success
   - Show error message when state is Error

2. `PortfolioPage(uiState: PortfolioUiState.Success)` composable:
   - Use Box as root container, background = ObsidianColors.Background
   - Use a LazyColumn or Column inside a verticalScroll for the main content
   - Sections in order (each gets an `id` for anchor nav):
     HeroSection, MetricsSection, AboutSection, ProjectsSection, 
     ServicesSection, ExperienceSection, ContactSection
   - NavBar overlaid at top using Box + align(Alignment.TopStart)
   - Add padding at top of LazyColumn equal to NavBar height (64.dp)

3. Navigation scroll behavior:
   - Implement `scrollToSection(sectionId: String)` using JS interop:
     ```kotlin
     fun scrollToSection(id: String) {
         document.getElementById(id)
             ?.scrollIntoView(js("({ behavior: 'smooth', block: 'start' })"))
     }
     ```
   - Pass this function as lambda to NavBar's onNavClick

4. Web page metadata:
   - On LaunchedEffect(Unit), set:
     - document.title = "Hirarki Wendri · Mobile Developer Portfolio"
     - Find and update meta description content

5. ProjectModal overlay:
   - Collect selectedProject from viewModel
   - If non-null, show ProjectModal composable as overlay
```

---

### PROMPT 3.2 — Hero Section

```
Implement HeroSection in 
`composeApp/src/wasmJsMain/kotlin/dev/hirarki/portfolio/ui/sections/HeroSection.kt`.

Design reference: Stitch project 11677376707151998401, Hero section of the 
"Hirarki Wendri - Mobile Developer Portfolio" screen (dark obsidian, neon accents).

Layout (responsive):
- Desktop (>1024dp): Two-column Row — left=content, right=portrait image  
- Mobile (<1024dp): Column, image on top, content below

LEFT COLUMN content (top to bottom):
1. TerminalText component cycling through:
   ["DEV_RUNTIME // READY", "FLUTTER 3.x // ACTIVE", "KMP // LOADED"]
   with Primary (cyan) color, labelLarge monospace style
   Prefix with "$ " and show blinking cursor

2. Active status badge: Row with animated pulsing green dot + "Available for opportunities"
   text in labelMedium OnSurfaceDim

3. Main headline: "Engineering Scalable\nHigh-Performance\nMobile Experiences"
   - displayLarge or displayMedium, OnSurface, Bold
   - Neon cyan (#00F0FF) gradient on "High-Performance" word using 
     Brush.linearGradient([Primary, Secondary])

4. Subheadline: profile.heroSubheadline in bodyLarge, OnSurfaceDim, maxLines=3

5. Stack chips Row (wrapping): profile.stackChips mapped to TechPill components
   Spacer 24.dp below

6. CTA Row:
   - NeonButton PRIMARY "View Featured Projects" → scrollToSection("projects")
   - Spacer 12.dp
   - NeonButton SECONDARY "Download CV" → trigger CV download

RIGHT COLUMN:
- AsyncImage / Image of profile.avatarUrl
  - Circular or slightly rounded (16.dp) crop
  - Neon cyan rim glow using drawBehind with Primary color Brush
  - Size: 400×400dp on desktop, 240×240dp on mobile
  - Fallback: initials avatar in Container surface

BACKGROUND:
- Subtle radial gradient glow in top-right: Primary at 5% alpha
- Optional: animated particles or grid pattern using Canvas

Section ID: "hero", padding 80.dp vertical, 120.dp vertical on desktop
```

---

### PROMPT 3.3 — Metrics Bar Section

```
Implement MetricsSection in 
`composeApp/src/wasmJsMain/kotlin/dev/hirarki/portfolio/ui/sections/MetricsSection.kt`.

This section displays 4 animated stat counters in a horizontal bar.

Requirements:
1. Layout: Row with equal weight distribution, ContainerMid background
   - Full width, padding 40.dp vertical, 80.dp horizontal
   - On mobile: 2×2 Grid using two Rows

2. For each metric in profile.metrics, render AnimatedCounter with:
   - targetValue = metric.value (animate to this)
   - suffix = metric.suffix  
   - label = metric.label

3. Each counter cell:
   - Vertically centered Column
   - AnimatedCounter (large Primary colored number)
   - Below: label text in bodyMedium OnSurfaceDim
   - Divider line between cells (vertical, Border color) except last

4. AnimatedCounter animation trigger:
   - Use `IntersectionObserver` via JS interop OR
   - Use LaunchedEffect + delay(300ms) on first composition
   - Count from 0 to targetValue over 2000ms using easeOut curve
   - Format: if value is whole number → show as Int, else show 1 decimal place

5. Subtle top/bottom borders in Border color on the section container

Section ID: "metrics"
```

---

### PROMPT 3.4 — About / Profile Matrix Section

```
Implement AboutSection in 
`composeApp/src/wasmJsMain/kotlin/dev/hirarki/portfolio/ui/sections/AboutSection.kt`.

Section ID: "about"
Padding: 80.dp vertical, responsive horizontal

Layout (desktop: Row, mobile: Column):

LEFT side (40% width on desktop):
1. SectionHeader with label="01.", title="Profile Matrix\n& Core Competencies"
2. Paragraph text (bodyLarge, OnSurfaceDim, lineHeight=28.sp):
   "Crafting apps that balance architectural rigor with silky fluid interactions.
    Every line of code optimized for frame budgets, battery efficiency, and 
    zero-compromise user experiences across iOS and Android."
3. Four discipline highlight rows, each:
   - Small cyan bullet dot
   - Title in titleMedium OnSurface
   - Description in bodyMedium OnSurfaceDim
   Disciplines from PRD section 3.4:
   - Pixel-Perfect UI & Micro-Animations
   - Battery, Memory & GPU Optimization  
   - Offline-First Synchronizations
   - Deep Linking & Push Architectures

RIGHT side (55% width on desktop):
1. Display skills.categories as a vertical stack of category blocks
2. Each category block:
   - Category name in labelLarge Primary
   - FlowRow of TechPill components for each skill
   - 12.dp gap between categories
3. Wrap in GlowCard with 24.dp padding

Animation: Fade-in + slide-up each category block with staggered delay (100ms each)
```

---

### PROMPT 3.5 — Projects / Case Studies Section

```
Implement ProjectsSection in
`composeApp/src/wasmJsMain/kotlin/dev/hirarki/portfolio/ui/sections/ProjectsSection.kt`.

Design source of truth: Stitch project 11677376707151998401, dark technical card style,
cyan/pink accents, subtle ambient glow per sample_prompt_project_showcases.md.

CRITICAL SCREENSHOT RULES (read before implementing):
- Project visuals are PORTRAIT mobile screenshots, not landscape thumbnails.
- NEVER crop portrait screenshots into a fixed landscape aspect ratio.
- bannerUrl points to a developer-provided composite banner image — display it as-is.
- screenshots[] is an ordered list of individual portrait screen images.
- Each screenshot has alt text — always pass it to the Image contentDescription.
- Only render optional fields (problem, solution, impact, caseStudyUrl) when non-null.
- Do NOT invent metrics, App Store links, or any content not present in the JSON.

---

Section ID: "projects"
Padding: 80.dp vertical, responsive horizontal

STEP 1 — Define these composables and their responsive behavior:

A. ProjectCard composable:
   - On Expanded (desktop): featured card is full-width with banner left + content right.
     Remaining cards use a 2-column grid.
   - On Medium (tablet): 2-column grid for all cards.
   - On Compact (mobile): single column, all cards stacked vertically.

B. ProjectScreenshot composable (single portrait screenshot):
   - ContentScale.Fit — NEVER ContentScale.Crop
   - Maintains intrinsic portrait aspect ratio (do not force 16:9)
   - Rounded corners 12.dp
   - Meaningful contentDescription from screenshot.alt

C. ScreenshotGallery composable (multiple screenshots):
   - Desktop: display 2–3 screenshots side-by-side in the media area
   - Mobile: horizontal scroll rail (LazyRow), touch-friendly, minimum 44.dp touch targets
   - No clipping of portrait screenshots at the ends of the rail
   - If only 1 screenshot: show single ProjectScreenshot, no gallery chrome

---

STEP 2 — Section header:

SectionHeader: label="02.", title="Featured Engineering\nCase Studies"

---

STEP 3 — Featured project card (first project where featured=true):

Desktop layout: Row (60% media | 40% content)

MEDIA AREA (left):
- Display project.bannerUrl as hero banner image:
  - Width: fills media column, height: auto (maintain aspect ratio)
  - ContentScale.Fit, rounded 16.dp
  - No overlay gradient that obscures the screenshots
  - Neon accent glow behind the image using drawBehind(glowColor)
- Below banner: ScreenshotGallery of project.screenshots
  - On desktop: show up to 3 portrait screenshots in a Row, equal width
  - Each screenshot: rounded 12.dp, ContentScale.Fit, full portrait height preserved

CONTENT AREA (right, inside GlowCard):
- Accent color strip at top (4.dp height, project.accentColor)
- Platform badges: Row of TechPill (compact variant)
- project.title in headlineMedium OnSurface
- project.category in bodyMedium OnSurfaceDim
- project.summary in bodyMedium OnSurface, 12.dp top margin
- If project.problem != null: collapsible "The Problem" section (bodyMedium OnSurfaceDim)
- If project.solution != null: collapsible "Our Solution" section (bodyMedium OnSurface)
- Technologies: FlowRow of TechPill (project.technologies)
- If project.metrics.isNotEmpty(): mini metric chips row
  Each chip: labelMedium Primary + labelSmall OnSurfaceDim, GlowCard background
- CTA row:
  - "View Case Study" text button with → icon (Primary) → only if caseStudyUrl != null
  - "GitHub" icon button → only if githubUrl != null
  - If both null: show nothing (do not show placeholder links)
- onClick: viewModel.selectProject(project)

---

STEP 4 — Remaining project cards (2-column grid on desktop):

GlowCard with:
- glowColor derived from project.accentColor with 20% alpha
- TOP: project.bannerUrl displayed with ContentScale.Fit
  - Width: full card width, height: auto — do NOT force 16:9
  - Fallback: gradient Container background showing project.title initial
- Accent line: 3.dp horizontal strip in accentColor below banner
- BODY padding 20.dp:
  * Platform badges (TechPill, compact)
  * project.title in headlineSmall OnSurface
  * project.category in bodyMedium OnSurfaceDim
  * project.summary in bodyMedium OnSurfaceDim, maxLines=3
  * Technologies FlowRow (compact TechPill, maxLines=2 overflow clipped with "+N more")
  * Bottom row: "Details" text button (Primary) + optional GitHub icon
- Hover: scale(1.02) spring animation + glow intensification (alpha 0x33 → 0x66)
- onClick: viewModel.selectProject(project)

---

STEP 5 — ProjectModal composable (in components/ProjectModal.kt):

Overlay triggered by viewModel.selectedProject non-null.

Structure:
- Full-screen semi-opaque backdrop (Background at 90% alpha)
  - Click on backdrop → dismiss
- Centered dialog card (GlowCard, max 760.dp wide, max 90% viewport height)
  - Vertically scrollable content inside

MODAL CONTENT (top to bottom):
1. Banner image: project.bannerUrl, ContentScale.Fit, full width
2. If project.screenshots.isNotEmpty():
   ScreenshotGallery:
   - Desktop: horizontal Row of portrait screenshots, equal width columns
   - Mobile: horizontal LazyRow scroll rail, 240.dp per screenshot width
   - Each screenshot: ProjectScreenshot composable with alt text
   - Keyboard accessible: focusable items, left/right arrow key navigation
3. Header row: project.title (headlineMedium) + X close button (top-right)
4. project.category (labelLarge Primary)
5. Platform TechPills row
6. project.summary (bodyLarge OnSurface)
7. If project.problem != null:
   "The Problem" section header (labelLarge Primary) + bodyMedium OnSurfaceDim text
8. If project.solution != null:
   "The Approach" section header (labelLarge Primary) + bodyMedium OnSurface text
9. If project.impact != null:
   "Impact" section header (labelLarge Secondary) + bodyMedium OnSurface text
10. If project.metrics.isNotEmpty():
    "Key Results" section with metric chips in a Wrap/FlowRow
    Each chip: GlowCard background, metric.label (labelSmall OnSurfaceDim) + metric.value (labelLarge Primary)
11. Technologies FlowRow (full TechPill list)
12. CTA buttons row:
    - NeonButton PRIMARY "View Case Study" → only if caseStudyUrl != null
    - NeonButton SECONDARY "GitHub" → only if githubUrl != null
    - If both null: omit CTA row entirely

MODAL BEHAVIOR:
- Dismiss: backdrop click, Escape key, X button
- Animation: AnimatedVisibility scale 0.9→1.0 + fade in; reverse on dismiss
- Background overlay: fade in independently (200ms)
- AnimatedContent: smooth switch if user opens another project while modal is open
- Focus trap: Tab key stays within modal while open
- Focus return: on dismiss, focus returns to the card that triggered the modal

---

STEP 6 — Accessibility:

- Every screenshot Image must have contentDescription = screenshot.alt
- Decorative banner images that are purely presentational may use contentDescription = null
  only if the project title/summary provides equivalent context nearby
- Gallery rail must be focusable and respond to arrow keys
- Touch targets for gallery nav buttons: minimum 44×44.dp
- Project cards: role = Role.Button, contentDescription includes title + category
- Modal: semantics role = SemanticsProperties.Role (dialog equivalent in CMP)
- Do not use animation as the sole method of conveying project information

---

STEP 7 — Performance:

- Lazy-load project screenshots below the fold (use lazyList or equivalent)
- bannerUrl images: load eagerly for above-fold featured card, lazily for grid cards
- Do NOT load all screenshots upfront — load on demand when modal opens
- Use appropriate image dimensions; avoid loading 3x resolution on 1x displays
- Avoid excessive glow/blur effects that cause recomposition every frame

---

STEP 8 — Responsive test matrix (verify after implementation):

□ 360px: single column, no screenshot overflow, gallery scrolls horizontally
□ 390px: same as 360px
□ 768px: 2-column grid, portrait screenshots not cropped
□ 1024px: featured card in hero layout, screenshots side-by-side
□ 1440px: full desktop, max-width constraint on content
□ Verify: no screenshot is unintentionally cropped at any breakpoint
□ Verify: horizontal page overflow does not occur from gallery
□ Verify: modal gallery keyboard navigable

Section is fully data-driven from projects list — no hardcoded project content.
```

---

### PROMPT 3.6 — Services Section

```
Implement ServicesSection in 
`composeApp/src/wasmJsMain/kotlin/dev/hirarki/portfolio/ui/sections/ServicesSection.kt`.

Section ID: "services"
Background: subtle alternate background (Surface instead of Background)
Padding: 80.dp vertical, responsive horizontal

1. SectionHeader: label="03.", title="Engineering\nCapabilities & Services"
   Centered alignment

2. Services grid: 2×2 on desktop, 1 column on mobile, 24.dp gap

3. For each service in services list, render ServiceCard:
   GlowCard with 28.dp padding:
   - Icon: mapped from service.iconKey to a Compose vector or Unicode emoji
     Icon keys: "cross-platform" → 📱, "native" → ⚡, "architecture" → 🏗, "ui-ux" → 🎨
     Render in a 48×48dp Container-background circle with Primary colored icon
   - service.title in headlineSmall OnSurface, 16.dp top margin
   - service.description in bodyMedium OnSurfaceDim, 8.dp top margin
   - Divider (Border color) 16.dp margin vertical
   - service.features as bullet list: each item with cyan bullet + bodyMedium OnSurface

4. Bottom CTA: centered NeonButton SECONDARY "Let's Build Together" → scrollToSection("contact")

Cards have hover glow intensification effect.
```

---

### PROMPT 3.7 — Experience Timeline Section

```
Implement ExperienceSection in 
`composeApp/src/wasmJsMain/kotlin/dev/hirarki/portfolio/ui/sections/ExperienceSection.kt`.

Section ID: "experience"
Padding: 80.dp vertical, responsive horizontal

1. SectionHeader: label="04.", title="Career Journey &\nProduction Track Record"

2. Vertical timeline layout:
   - Center vertical line: 2.dp width, gradient from Primary to Secondary
   - Each experience as a timeline node

3. For each experience in experiences list, render TimelineItem:
   
   Layout: Row with [LeftContent | TimelineNode | RightContent]
   - Alternate left/right on desktop (even index → left, odd → right)
   - On mobile: all cards on right side of timeline
   
   TimelineNode (center column, ~40.dp wide):
   - Filled circle: 16.dp diameter, color = experience.accentColor
   - If experience.current = true: pulsing animation ring around it
   - Connected to vertical line above/below
   
   ExperienceCard (GlowCard):
   - Period label: labelLarge Primary (JetBrains Mono)
   - Role: titleLarge OnSurface Bold
   - Company + companyType: bodyMedium OnSurfaceDim with "|" separator
   - Description: bodyMedium OnSurfaceDim, maxLines=3
   - Achievements: bullet list, 2.dp cyan bullet, bodyMedium OnSurface
   - If current: "Current" badge chip with Secondary (magenta) background
   
   Animate: each card slides in from its side (left cards from left, right from right)
   triggered on scroll-into-view.
```

---

### PROMPT 3.8 — Contact Section & Footer

```
Implement ContactSection in 
`composeApp/src/wasmJsMain/kotlin/dev/hirarki/portfolio/ui/sections/ContactSection.kt`.

Section ID: "contact"
Background: ContainerMid for visual differentiation
Padding: 80.dp vertical, responsive horizontal

1. SectionHeader: label="05.", title="Contact Terminal\n& Social Connect"
   Centered

2. Terminal-style contact card (GlowCard, max 600.dp wide, centered):
   - Monospace header bar: "$ open --channel communication" in Primary
   - Contact rows with icon + text:
     * 📧 Email: profile.email (clickable mailto: link)
     * 💬 WhatsApp: "Chat on WhatsApp" (opens wa.me link)
     * 📍 Location: profile.location
   - Divider
   - Social links Row: GitHub, LinkedIn, Twitter icons
     Each: 44×44dp clickable icon, Border background circle, Primary on hover
     Opens profile URLs in new tab

3. Download CV prominent CTA below contact card:
   NeonButton PRIMARY, full width (max 320.dp), centered:
   "⬇ Download My CV" → downloadCV(profile.cvUrl)

4. Footer (below ContactSection or as part of it):
   - Thin border top in Border color
   - Row: "© 2025 Hirarki Ardi Pratama Wendri" left, "Back to Top ↑" right
   - "Back to Top" → scrollToSection("hero")
   - Both in labelMedium OnSurfaceDim

CV Download implementation:
```kotlin
fun downloadCV(url: String) {
    val a = document.createElement("a") as HTMLAnchorElement
    a.href = url
    a.download = "Hirarki_Wendri_CV.pdf"
    document.body?.appendChild(a)
    a.click()
    document.body?.removeChild(a)
}
```
```

---

## ─────────────────────────────────────
## PHASE 4 — Features & Interactivity
## ─────────────────────────────────────

### PROMPT 4.1 — Smooth Scroll & Active Navigation State

```
Enhance the navigation system in the KMP portfolio to support:

1. **Active section highlighting** in NavBar:
   - Track which section is currently in viewport
   - Use JS IntersectionObserver or scroll position tracking:
     ```kotlin
     window.addEventListener("scroll", {
         updateActiveSection(window.scrollY)
     })
     ```
   - Highlight active nav link with Primary color underline/accent
   - Pass activeSection: State<String> down to NavBar

2. **NavBar scroll behavior**:
   - Transparent on hero section (scrollY < heroHeight)
   - Surface background + bottom border when scrolled past hero
   - Animate background change with 300ms transition

3. **Mobile hamburger menu**:
   - Below 768dp breakpoint, hide nav links
   - Show ☰ icon button
   - On click: show fullscreen overlay menu (Surface background)
   - Overlay menu: vertical list of nav links (headlineMedium), animated slide-in
   - Close on link tap or X button

4. **Keyboard navigation**:
   - Add event listener for keydown
   - Escape key: close any open modal or mobile menu
   - Tab navigation should work across all interactive elements

Implement breakpoint helper:
```kotlin
val windowWidth = window.innerWidth
fun isDesktop() = windowWidth > 1024
fun isTablet() = windowWidth in 768..1024  
fun isMobile() = windowWidth < 768
```
Use `rememberWindowSize()` composable that reacts to window resize events.
```

---

### PROMPT 4.2 — Project Modal & Animations

```
Implement polished animations throughout the KMP portfolio:

1. **Page load animations** (staggered fade-in):
   - Each section fades in + slides up 24dp as it enters viewport
   - Use `remember { Animatable(0f) }` + LaunchedEffect
   - Stagger delay: 150ms per section child element

2. **AnimatedCounter** (refine implementation):
   - Use `animateFloatAsState` with `tween(2000, easing = EaseOutCubic)`
   - Only trigger once (use `var started by remember { mutableStateOf(false) }`)
   - Show formatted value with `NumberFormat` or manual formatting

3. **ProjectModal** enhanced animations:
   - Entry: `AnimatedVisibility` with scale (0.9→1.0) + fade in (0→1) + slide up
   - Exit: reverse animation
   - Background overlay fades in separately
   - Use `AnimatedContent` for smooth switching between projects if user 
     opens another from within modal

4. **GlowCard hover states**:
   - Use `interactionSource` + `collectIsHoveredAsState()`
   - On hover: animate glow intensity (increase alpha from 0x33 to 0x66)
   - Animate scale: 1.0 → 1.02 with spring animation
   - Only on desktop (check window width)

5. **NeonButton press animation**:
   - Scale down to 0.96 on press
   - Brief brightness flash on release

6. **TerminalText** typewriter effect:
   - Implement character-by-character typing with 80ms delay per char
   - Pause 2s at end of each text before erasing
   - Erase at 40ms per char speed
   - Cycle to next text after erase
   - Blinking cursor: toggle every 500ms

7. **Timeline items**: slide in from sides using viewport detection
   via JS IntersectionObserver callback triggering Compose state.

All animations must be smooth at 60fps. Use spring() physics for
organic feel on interactive elements, tween() for entry animations.
```

---

### PROMPT 4.3 — Responsive Layout System

```
Implement a comprehensive responsive layout system for the KMP portfolio.

1. **WindowSizeClass** helper:
   ```kotlin
   enum class WindowSizeClass { Compact, Medium, Expanded }
   
   @Composable
   fun rememberWindowSizeClass(): WindowSizeClass {
       var width by remember { mutableStateOf(window.innerWidth) }
       DisposableEffect(Unit) {
           val listener = EventListener { width = window.innerWidth }
           window.addEventListener("resize", listener)
           onDispose { window.removeEventListener("resize", listener) }
       }
       return when {
           width < 600 -> WindowSizeClass.Compact
           width < 1200 -> WindowSizeClass.Medium
           else -> WindowSizeClass.Expanded
       }
   }
   ```

2. **Responsive padding** helper:
   ```kotlin
   @Composable
   fun responsiveHorizontalPadding(): Dp {
       val windowSize = rememberWindowSizeClass()
       return when (windowSize) {
           WindowSizeClass.Compact -> 20.dp
           WindowSizeClass.Medium -> 48.dp
           WindowSizeClass.Expanded -> 120.dp
       }
   }
   ```

3. Apply responsive layouts to each section:
   - HeroSection: Row on Expanded, Column on Compact/Medium
   - MetricsSection: 4-col on Expanded, 2×2 on Compact
   - ProjectsSection: 2-col grid on Expanded/Medium, 1-col on Compact
   - ServicesSection: 2×2 on Expanded, 1-col on Compact
   - ExperienceSection: alternating on Expanded, all-right on Compact
   - NavBar: full links on Expanded/Medium, hamburger on Compact

4. Typography scaling:
   - On Compact: reduce displayLarge to 36.sp, headlineLarge to 24.sp
   - Create `responsiveTextStyle(base: TextStyle, compactScale: Float = 0.75f)`

5. Test these viewport breakpoints:
   - 360px (minimum), 390px (iPhone 14), 768px (tablet), 1280px, 1920px, 2560px
```

---

## ─────────────────────────────────────
## PHASE 5 — Polish, Accessibility & Performance
## ─────────────────────────────────────

### PROMPT 5.1 — Accessibility (WCAG 2.1 AA)

```
Audit and fix accessibility (WCAG 2.1 AA) for the KMP portfolio web app.

1. **Color contrast**: Verify all text/background combinations:
   - Primary (#00F0FF) on Background (#0C0E14): must be ≥ 4.5:1 for normal text
   - OnSurface (#E8EAED) on Surface (#111319): verify ≥ 7:1 (AAA preferred)
   - Secondary (#FF007A) on dark: check if used for text (if so, adjust lightness)
   - Use https://webaim.org/resources/contrastchecker/ ratios as reference

2. **Semantic HTML via Modifier.semantics**:
   - NavBar links: add `Modifier.semantics { role = Role.Button; contentDescription = "Navigate to $section" }`
   - Images: add `Modifier.semantics { contentDescription = "Profile photo of Hirarki Wendri" }`
   - AnimatedCounter: add `Modifier.semantics { contentDescription = "$formattedValue $suffix $label" }`
   - NeonButton: ensure button role and descriptive label
   - ProjectModal: add dialog role, focus trap inside modal

3. **Keyboard navigation**:
   - All interactive elements reachable via Tab
   - NavBar links respond to Enter/Space
   - Modal dismisses on Escape
   - Focus returns to trigger element after modal close

4. **Motion preferences**:
   - Check `window.matchMedia("(prefers-reduced-motion: reduce)").matches`
   - If true: disable typewriter animation, skip counter animation (show final value),
     disable parallax/particles, use instant transitions instead of animated ones

5. **Focus indicators**:
   - Add visible focus ring to all interactive elements
   - Ring: 2dp solid Primary color, 2dp offset
   - Override default browser outline, ensure visibility on dark background

6. **Screen reader landmarks**:
   - Wrap NavBar in `Modifier.semantics { isTraversalGroup = true; traversalIndex = 0f }`
   - Each section: add heading role to SectionHeader title
   - Contact links: descriptive aria labels
```

---

### PROMPT 5.2 — SEO & Web Metadata

```
Implement SEO and web metadata for the KMP portfolio.

1. In `main.kt` or a dedicated `WebMetadata.kt`, add this LaunchedEffect:

```kotlin
fun setupWebMetadata(profile: ProfileModel) {
    document.title = "${profile.name} · Mobile Developer Portfolio"
    
    setMetaTag("description", 
        "Portfolio of ${profile.name}, ${profile.title} specializing in Flutter, Kotlin, and Swift mobile development. ${profile.tagline}")
    setMetaTag("og:title", "${profile.name} · Mobile Developer Portfolio")
    setMetaTag("og:description", profile.tagline)
    setMetaTag("og:image", profile.avatarUrl)
    setMetaTag("og:type", "website")
    setMetaTag("twitter:card", "summary_large_image")
    setMetaTag("twitter:creator", "@hirarki")
    setMetaTag("theme-color", "#0C0E14")
}

fun setMetaTag(name: String, content: String) {
    val meta = document.querySelector("meta[name=\"$name\"], meta[property=\"$name\"]")
        ?: document.createElement("meta").also { 
            if (name.startsWith("og:")) (it as HTMLMetaElement).setAttribute("property", name)
            else (it as HTMLMetaElement).name = name
            document.head?.appendChild(it) 
        }
    (meta as HTMLMetaElement).content = content
}
```

2. Update `index.html` with static fallback meta tags (for crawlers before JS loads):
   - All og: tags, twitter: tags, viewport, charset
   - Canonical URL link tag
   - JSON-LD structured data for Person schema:
   ```html
   <script type="application/ld+json">
   {
     "@context": "https://schema.org",
     "@type": "Person",
     "name": "Hirarki Ardi Pratama Wendri",
     "jobTitle": "Middle Mobile Developer",
     "url": "https://hirarki.dev",
     "sameAs": ["https://github.com/hirarki", "https://linkedin.com/in/hirarki"]
   }
   </script>
   ```

3. Add `robots.txt` and `sitemap.xml` to the resources root:
   - robots.txt: allow all, sitemap reference
   - sitemap.xml: single URL entry for the homepage

4. Add `manifest.json` for PWA support:
   - name, short_name, theme_color (#0C0E14), background_color, display: standalone
   - icons array (at minimum 192×192 and 512×512)
```

---

### PROMPT 5.3 — Performance Optimisation

```
Optimise the KMP portfolio web app for Lighthouse ≥ 95 performance score.

1. **index.html optimisations**:
   - Add `<link rel="preload">` for critical fonts (woff2 files)
   - Add `<link rel="preload">` for the .wasm binary
   - Add `<link rel="preconnect">` for fonts.googleapis.com and fonts.gstatic.com
   - Set `fetchpriority="high"` on hero image
   - Inline critical CSS (background color, font-display) in `<style>` tag

2. **Image optimisation**:
   - All project images must be .webp format
   - Add explicit width and height attributes to prevent CLS
   - Implement loading="lazy" for below-fold images
   - Provide srcset for 1x/2x/3x densities where applicable
   - Avatar image: also provide a low-quality blur placeholder

3. **WASM bundle size**:
   - In build.gradle.kts, enable production webpack config:
     ```kotlin
     webpackTask {
         mainOutputFileName = "composeApp.js"
         args.add("-p")  // production mode
     }
     ```
   - Enable Kotlin dead code elimination
   - Verify final gzipped bundle < 2MB

4. **Resource caching** (via GitHub Pages / server config):
   - Ensure static assets have long cache TTL
   - JSON files: short cache (for CMS updates)
   - Fonts/images: long cache (content-addressed)

5. **Rendering performance**:
   - Add `loading="eager"` only to above-fold hero image
   - Defer non-critical JS/resources
   - Avoid recomposition loops: audit all State reads in Composables
   - Use `derivedStateOf { }` for computed states to avoid excess recomposition
   - Profile with Chrome DevTools Performance tab, target < 16ms frame times

6. **Perceived performance**:
   - Show loading skeleton UI while JSON data fetches
   - Add progressive enhancement: render static content first, 
     enhance with dynamic data
   - Target FCP < 1.0s (font preloading critical path)
```

---

## ─────────────────────────────────────
## PHASE 6 — Build, Deploy & CI/CD
## ─────────────────────────────────────

### PROMPT 6.1 — Build Configuration & Production Output

```
Configure the KMP portfolio for production build and deployment.

1. **Production build command**:
   ```
   ./gradlew :composeApp:wasmJsBrowserDistribution
   ```
   Output: `composeApp/build/dist/wasmJs/productionExecutable/`

2. Update `composeApp/build.gradle.kts` to add:
   ```kotlin
   kotlin {
       wasmJs {
           browser {
               commonWebpackConfig {
                   outputFileName = "composeApp.js"
                   devServer = KotlinWebpackConfig.DevServer(
                       port = 8080,
                       open = false,
                       contentBase = listOf(
                           "$projectDir/src/wasmJsMain/resources"
                       )
                   )
               }
               webpackTask {
                   mainOutputFileName = "composeApp.js"
                   sourceMaps = false
               }
               runTask {
                   mainOutputFileName = "composeApp.js"
               }
           }
       }
   }
   ```

3. Create `composeApp/webpack.config.d/assets.js` to copy the assets/ folder
   to the webpack output directory:
   ```javascript
   const path = require('path');
   const CopyPlugin = require('copy-webpack-plugin');
   
   config.plugins.push(new CopyPlugin({
       patterns: [
           { from: 'src/wasmJsMain/resources/assets', to: 'assets' },
           { from: 'src/wasmJsMain/resources/index.html', to: '.' },
       ],
   }));
   ```

4. Create `.github/workflows/deploy.yml`:
   - Trigger on push to main branch
   - Java 17 (Temurin) setup
   - Gradle caching for faster builds
   - Run `wasmJsBrowserDistribution`
   - Deploy to GitHub Pages using peaceiris/actions-gh-pages@v4
   - Publish from `composeApp/build/dist/wasmJs/productionExecutable`

5. Create `.github/workflows/pr-check.yml`:
   - Trigger on pull requests
   - Run `./gradlew :composeApp:wasmJsBrowserDevelopmentWebpack`
   - Report build success/failure

6. Add `CNAME` file to resources for custom domain (if applicable)

7. Test locally:
   ```
   ./gradlew :composeApp:wasmJsBrowserDevelopmentRun
   ```
   Open http://localhost:8080
```

---

### PROMPT 6.2 — Final Integration Test & Launch Checklist

```
Run a final integration checklist for the KMP portfolio before launch.

Verify each item and fix any issues found:

**Functionality Checklist:**
□ All 7 sections render correctly with real data from JSON files
□ NavBar scroll-to-section works for all 5 nav links
□ Download CV button triggers file download (not navigation)
□ Project cards open modal on click
□ Project modal closes on X button, background tap, and Escape key
□ AnimatedCounter counts up on first view
□ TerminalText cycles through texts with typewriter effect
□ All social links open in new tab
□ Email link opens mail client (mailto:)
□ WhatsApp link opens wa.me properly
□ Back to top works from footer

**Responsive Checklist (test these widths):**
□ 360px — minimum mobile width, no overflow
□ 390px — iPhone viewport
□ 768px — tablet, 2-col projects visible
□ 1280px — desktop, full layout
□ 1920px — widescreen, content max-width centered
□ 2560px — 4K, no layout issues

**Performance Checklist:**
□ Run `./gradlew :composeApp:wasmJsBrowserDistribution`
□ Check bundle size: ls -lh composeApp/build/dist/wasmJs/productionExecutable/
□ Open Chrome DevTools → Lighthouse → Run audit
□ Performance score ≥ 95
□ FCP < 1.0s
□ CLS < 0.05
□ Accessibility score ≥ 90

**Code Quality:**
□ No hardcoded strings (all in JSON CMS files)
□ All Composables are @Preview annotated
□ No unused imports or dead code
□ All TODO comments resolved

**Production deployment:**
□ Push to main branch → GitHub Actions runs
□ Deploy to GitHub Pages succeeds
□ Check live URL loads correctly
□ Verify meta tags with https://metatags.io/
□ Verify structured data with Google Rich Results Test
```

---

## ─────────────────────────────────────
## BONUS — Enhancements (Phase 2 & 3)
## ─────────────────────────────────────

### PROMPT B.1 — Dark/Neon Particle Background (Optional)

```
Add an optional animated background to the Hero section of the KMP portfolio.

Using Compose Canvas, draw a subtle animated particle/grid background:

Option A — Dot Grid:
- Draw a grid of small dots (2dp diameter) spaced 32dp apart
- Color: Border color (rgba(255,255,255,0.08))
- On hover, nearby dots near cursor increase opacity to 0.3
- Animate with subtle wave effect using sin(time + x * 0.1) vertical offset

Option B — Floating Particles:
- 20-30 small circular particles floating upward slowly
- Random positions, Primary or Secondary color at 10-20% alpha
- Each particle: 3-6dp radius, random speed, respawn at bottom when off-screen
- Use `withInfiniteTransition()` or manual animation loop with LaunchedEffect

Option C — Neon Grid Lines:
- Sparse grid lines (1dp) in border color
- Perspective transform effect (lines converge at center horizon)
- Primary color glow at intersection points

Implement as `BackgroundCanvas(modifier: Modifier)` Composable using Canvas API.
Make it disable automatically when `prefers-reduced-motion: reduce` is true.
Gate behind a `showParticles: Boolean` parameter.
```

---

### PROMPT B.2 — CMS Update Guide

```
Write a developer guide for updating the JSON CMS content in this portfolio.

Create `CMS_GUIDE.md` in the project root with:

1. **Overview**: Explain the JSON-first CMS approach (no backend, compile-time assets)

2. **File locations**: 
   `composeApp/src/commonMain/resources/assets/data/`
   - profile.json — personal info, metrics, social links
   - projects.json — case study cards
   - experiences.json — career timeline
   - services.json — capability cards  
   - skills.json — tech pills by category

3. **Updating profile info**: Step-by-step with field descriptions

4. **Adding a new project**:
   - Copy a project entry in projects.json
   - Add thumbnail to assets/images/projects/
   - Rebuild and test

5. **Replacing the CV**:
   - Place new PDF at assets/cv/hirarki_cv.pdf
   - Or update cvUrl in profile.json to new path

6. **Updating the avatar image**:
   - Replace assets/images/avatar.webp
   - Use ≥ 800×800px square WebP for best quality

7. **Rebuilding after changes**:
   - `./gradlew :composeApp:wasmJsBrowserDevelopmentRun`
   - Check changes at localhost:8080
   - `git push origin main` to auto-deploy

8. **JSON validation**: Recommend jsonlint.com before committing

Include example JSON snippets for each file.
```

---

*End of PROMPTS.md — Total: 6 Phases + 2 Bonus Prompts*  
*Reference: PLAN.md · ARCHITECTURE.md · portfolio_prd_project_brief.md · Stitch #11677376707151998401*
