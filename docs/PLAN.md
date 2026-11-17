# 📐 Project Plan — Hirarki Wendri · Mobile Developer Portfolio
> **Stack:** Kotlin Multiplatform (KMP) + Compose Multiplatform (Web target only)  
> **Design:** Obsidian Cyber-Core · Stitch Project `11677376707151998401`  
> **Platform:** Web (Wasm/JS via Compose for Web) — no Android/iOS targets  
> **PRD:** `portfolio_prd_project_brief.md`

---

## 🗺 Phase Overview

| Phase | Name | Deliverable |
|-------|------|-------------|
| **P0** | Foundation & Toolchain | Gradle multi-module KMP/CMP project scaffolding, web-only config |
| **P1** | Design System | Tokens, typography, palette, component library |
| **P2** | JSON CMS Layer | Local `assets/` JSON schema + repository + ViewModel |
| **P3** | Core UI Sections | All 7 portfolio sections as Composable screens |
| **P4** | Features & Interactivity | Smooth scroll nav, animated counters, project modals, CV download |
| **P5** | Polish & Performance | Animations, Lighthouse ≥ 95, WCAG 2.1 AA, SEO meta |
| **P6** | Build & Deploy | Webpack/WASM bundle optimisation, GitHub Actions CI/CD |

---

## 🏗 Architecture

```
kmp-mobile-portofolio/
├── composeApp/                    ← CMP/Web module (wasmJs target)
│   ├── src/
│   │   ├── wasmJsMain/
│   │   │   └── kotlin/
│   │   │       └── dev/hirarki/portfolio/
│   │   │           ├── main.kt                ← entry point (canvasBasedRenderer)
│   │   │           ├── App.kt                 ← root Composable, router
│   │   │           ├── di/
│   │   │           │   └── AppModule.kt       ← manual DI (no Koin for wasm compat)
│   │   │           ├── data/
│   │   │           │   ├── model/             ← data classes (kotlinx.serialization)
│   │   │           │   │   ├── ProfileModel.kt
│   │   │           │   │   ├── ProjectModel.kt
│   │   │           │   │   ├── ExperienceModel.kt
│   │   │           │   │   ├── ServiceModel.kt
│   │   │           │   │   └── SkillModel.kt
│   │   │           │   ├── repository/
│   │   │           │   │   └── PortfolioRepository.kt   ← loads JSON from assets
│   │   │           │   └── datasource/
│   │   │           │       └── JsonAssetDataSource.kt   ← fetch() JSON in wasmJs
│   │   │           ├── domain/
│   │   │           │   └── usecase/
│   │   │           │       ├── GetProfileUseCase.kt
│   │   │           │       ├── GetProjectsUseCase.kt
│   │   │           │       ├── GetExperiencesUseCase.kt
│   │   │           │       └── GetServicesUseCase.kt
│   │   │           ├── presentation/
│   │   │           │   ├── viewmodel/
│   │   │           │   │   └── PortfolioViewModel.kt    ← StateFlow-based
│   │   │           │   └── state/
│   │   │           │       └── PortfolioUiState.kt
│   │   │           ├── ui/
│   │   │           │   ├── theme/
│   │   │           │   │   ├── Color.kt                 ← Obsidian Cyber-Core palette
│   │   │           │   │   ├── Typography.kt            ← Space Grotesk + JetBrains Mono
│   │   │           │   │   ├── Shapes.kt
│   │   │           │   │   └── Theme.kt                 ← MaterialTheme wrapper
│   │   │           │   ├── components/                  ← reusable atoms/molecules
│   │   │           │   │   ├── NeonButton.kt
│   │   │           │   │   ├── TechPill.kt
│   │   │           │   │   ├── SectionHeader.kt
│   │   │           │   │   ├── GlowCard.kt
│   │   │           │   │   ├── TerminalText.kt
│   │   │           │   │   ├── AnimatedCounter.kt
│   │   │           │   │   └── NavBar.kt
│   │   │           │   └── sections/                    ← full-page sections
│   │   │           │       ├── HeroSection.kt
│   │   │           │       ├── MetricsSection.kt
│   │   │           │       ├── AboutSection.kt
│   │   │           │       ├── ProjectsSection.kt
│   │   │           │       ├── ServicesSection.kt
│   │   │           │       ├── ExperienceSection.kt
│   │   │           │       └── ContactSection.kt
│   │   ├── commonMain/
│   │   │   └── composeResources/
│   │   │       └── drawable/                ← vector icons (Res.drawable.*)
│   │   └── wasmJsMain/
│   │       └── resources/
│   │           ├── index.html
│   │           ├── favicon.svg
│   │           └── assets/
│   │               ├── data/
│   │               │   └── profile.json / projects.json / experiences.json / services.json / skills.json
│   │               ├── profile.webp
│   │               ├── projects/<slug>/screen-XX.webp
│   │               └── cv/hirarki_cv.pdf     ← downloadable resume
│   └── build.gradle.kts
├── gradle/
│   ├── libs.versions.toml             ← version catalog
│   └── wrapper/
├── build.gradle.kts                   ← root build
├── settings.gradle.kts
├── PLAN.md                            ← this file
├── PROMPTS.md                         ← phase-by-phase AI prompts
└── portfolio_prd_project_brief.md
```

---

## 🔧 Technology Stack

### Core
| Layer | Technology | Version |
|-------|-----------|---------|
| Language | Kotlin | 2.1.x |
| UI Framework | Compose Multiplatform (Web) | 1.7.x |
| Build System | Gradle (KTS) | 8.x |
| Target Platform | `wasmJs` (primary) + `js` (fallback) | — |

### Key Libraries (libs.versions.toml)
| Library | Purpose |
|---------|---------|
| `kotlinx-serialization-json` | Deserialize JSON CMS assets |
| `kotlinx-coroutines-core` | Async data loading, StateFlow |
| `compose-multiplatform` | Full UI layer |
| `ktor-client-js` | (Optional) future remote CMS fetch |
| `kotlin-browser` (wasmJs stdlib) | DOM interop for CV download & scroll |

### Design System — Obsidian Cyber-Core
| Token | Value |
|-------|-------|
| `ColorBackground` | `#0C0E14` |
| `ColorSurface` | `#111319` |
| `ColorContainer` | `#191B22` |
| `ColorContainerMid` | `#23252E` |
| `ColorContainerHigh` | `#373940` |
| `ColorPrimary` (Neon Cyan) | `#00F0FF` |
| `ColorSecondary` (Cyber Magenta) | `#FF007A` |
| `ColorOnSurface` | `#E8EAED` |
| `ColorBorder` | `rgba(255,255,255,0.08)` |

---

## 📦 JSON CMS Schema

### `profile.json`
```json
{
  "name": "Hirarki Ardi Pratama",
  "title": "Senior Mobile Developer (Flutter & Android)",
  "tagline": "Engineering High-Performance, Resilient Cross-Platform Mobile Experiences",
  "location": "Jakarta Selatan, Indonesia",
  "email": "hirarki.pratama@gmail.com",
  "whatsapp": "+62895800267596",
  "github": "https://github.com/hirarki",
  "linkedin": "https://linkedin.com/in/hirarki-pratama",
  "twitter": "",
  "cvUrl": "/assets/cv/hirarki_cv.pdf",
  "avatarUrl": "assets/profile.webp",
  "heroSubheadline": "Specialized in Flutter · Kotlin · BLoC/Cubit · Clean Architecture · Real-time RTC",
  "metrics": [
    { "label": "Years Experience", "value": 5.0, "suffix": "+" },
    { "label": "Active Users Served", "value": 50.0, "suffix": "k+" },
    { "label": "Crash-Free Sessions", "value": 99.8, "suffix": "%" },
    { "label": "Code Reusability Boost", "value": 35.0, "suffix": "%" }
  ],
  "stackChips": ["Flutter & Dart", "Kotlin & Android", "BLoC / Cubit", "Clean Architecture", "Agora RTC & WebSockets"]
}
```

### `projects.json`
```json
The canonical data lives in `composeApp/src/wasmJsMain/resources/assets/data/projects.json`.
Real slugs: `dengerind`, `sai-coworking`, `iwwi-maintenance`, `perfect-assistant`.
`bannerUrl` points at the first existing screenshot (`assets/projects/<slug>/screen-01.webp`); there are no `banner.webp` files.

Excerpt (first project):
```json
{
  "slug": "dengerind",
  "title": "Dengerind",
  "category": "Mental Health & Teleconsultation",
  "bannerUrl": "assets/projects/dengerind/screen-01.webp",
  "screenshots": [
    {
      "image": "assets/projects/dengerind/screen-01.webp",
      "alt": "Dengerind home screen showing teleconsultation options, counseling topics, and counselor lists",
      "caption": "Consultation Home"
    }
  ]
}
```

### `experiences.json`
See `composeApp/src/wasmJsMain/resources/assets/data/experiences.json` for the canonical entries (AXA Mandiri, PT Ragdalion Revolusi Industri).

Excerpt:
```json
{
  "id": "axa-mandiri",
  "role": "Senior Android Developer (Kotlin)",
  "company": "AXA Mandiri",
  "period": "Nov 2022 – Present",
  "current": true,
  "accentColor": "#00F0FF"
}
```

---

## 🎨 Stitch Design Reference

**Project ID:** `11677376707151998401`  
**Screen:** `Hirarki Wendri - Mobile Developer Portfolio` (2560×10784 desktop)  
**Design Language:** Dark obsidian surfaces · Neon cyan/magenta accents · Glowing card borders  
**Screen Structure (top→bottom):**
1. Sticky NavBar — `HW` monogram + links + Download CV CTA
2. Hero — full-bleed portrait, terminal runtime badge, stack chips, dual CTAs
3. Metrics Bar — 4 animated stat counters
4. About / Profile Matrix — tech pills grid by category
5. Projects — 4 featured case study cards with modal capability
6. Services — 4 capability cards
7. Experience Timeline — vertical step timeline
8. Contact + Footer — email/social links, back-to-top

---

## 🚦 Performance & Quality Targets

| Metric | Target |
|--------|--------|
| Lighthouse Performance | ≥ 95 |
| First Contentful Paint | < 1.0s |
| Cumulative Layout Shift | < 0.05 |
| Accessibility | WCAG 2.1 AA |
| Responsive Range | 360px → 4K |
| Bundle Size (WASM) | < 2MB gzipped |

---

## 🔄 Data Flow

```
JSON Assets (compile-time)
        │
        ▼
JsonAssetDataSource          ← fetch("/assets/data/*.json") via JS interop
        │
        ▼
PortfolioRepository          ← suspending, coroutine-based
        │
        ▼
UseCase (domain layer)       ← pure business logic, testable
        │
        ▼
PortfolioViewModel           ← StateFlow<PortfolioUiState>
        │
        ▼
Composable Sections          ← collectAsState(), recompose on data
```

---

## 🌐 Web Interop Points

| Feature | Mechanism |
|---------|-----------|
| CV Download | `window.open(cvUrl, "_blank")` via `kotlinx.browser.window` |
| Smooth Scroll | `document.getElementById(id).scrollIntoView()` |
| Page Title / Meta | `document.title` + meta tag manipulation on init |
| Font Loading | CSS `@font-face` injected via `index.html` |
| JSON Fetch | `window.fetch("/assets/data/...")` → coroutine await |

---

## 📅 Milestone Timeline

```
Week 1  │ P0 Scaffolding + P1 Design System
Week 2  │ P2 JSON CMS + Data Layer
Week 3  │ P3 Core UI Sections (Hero → Contact)
Week 4  │ P4 Interactivity (nav, modals, counters, CV download)
Week 5  │ P5 Polish, animations, WCAG, responsive testing
Week 6  │ P6 Build optimisation, CI/CD, deploy
```
