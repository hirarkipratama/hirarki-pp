# CMS Guide

## 1. Overview

This portfolio is a **JSON-first CMS**: there is no backend, database, or admin panel. All content lives in five JSON files that ship as static assets inside the Wasm bundle. At startup `JsonAssetDataSourceImpl` fetches them, `PortfolioRepositoryImpl` decodes them into Kotlin models, and the Compose UI renders whatever the JSON contains.

Consequences of this approach:

- Content changes are ordinary file edits — no migrations, no API keys, no server.
- Content is served relative to the deployment base path (`WebPaths.asset()`), so **keep every asset path relative and without a leading slash** (for example `assets/projects/dengerind/screen-01.webp`, not `/assets/...`).
- `assets/` at the repository root is the original export folder. The files actually served by the site are the mirrored copies under `composeApp/src/wasmJsMain/resources/assets/`. Update both.
- Every route is a single page; section navigation is driven by the data, not by URLs.

## 2. File locations

All CMS files are in `composeApp/src/wasmJsMain/resources/assets/data/`:

| File | Shape | What it controls |
| --- | --- | --- |
| `profile.json` | object | Hero copy, contact/social links, CV and avatar paths, headline metrics, stack chips |
| `projects.json` | array of objects | Project cards, case-study modal, screenshot galleries, project metrics |
| `experiences.json` | array of objects | Career timeline entries and achievements |
| `services.json` | array of objects | Capability cards and their bullet lists |
| `skills.json` | object with `categories` | Skill pills grouped by category in the About section |

Related assets:

- `composeApp/src/wasmJsMain/resources/assets/profile.webp` — avatar
- `composeApp/src/wasmJsMain/resources/assets/projects/<slug>/screen-XX.webp` — portrait screenshots
- `composeApp/src/wasmJsMain/resources/assets/cv/hirarki_cv.pdf` — downloadable CV
- `composeApp/src/commonMain/composeResources/drawable/` — vector icons (not CMS content)

## 3. Updating profile info

1. Open `composeApp/src/wasmJsMain/resources/assets/data/profile.json`.
2. Edit the fields you need (keep the JSON object shape unchanged).
3. If you changed `avatarUrl` or `cvUrl`, make sure the referenced file exists under `composeApp/src/wasmJsMain/resources/assets/`.
4. Validate the JSON, run the app locally, then commit and push.

| Field | Type | Notes |
| --- | --- | --- |
| `name` | string | Used in the browser title and metadata |
| `title`, `tagline` | string | Job title and SEO/social description |
| `location`, `email`, `whatsapp` | string | Contact card; WhatsApp should be digits with country code, e.g. `+628...` |
| `github`, `linkedin`, `twitter` | string | Full URLs; empty string hides/disables the link |
| `cvUrl`, `avatarUrl` | string | Relative asset paths |
| `heroSubheadline` | string | Hero paragraph; falls back to `tagline` when blank |
| `metrics` | array | `label` string, `value` **number**, `suffix` string |
| `stackChips` | array of strings | Rendered as pills in the hero |

Example (`profile.json`):

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
  "cvUrl": "assets/cv/hirarki_cv.pdf",
  "avatarUrl": "assets/profile.webp",
  "heroSubheadline": "Specialized in Flutter · Kotlin · BLoC/Cubit · Clean Architecture · Real-time RTC",
  "metrics": [
    { "label": "Years Experience", "value": 5.0, "suffix": "+" },
    { "label": "Crash-Free Sessions", "value": 99.8, "suffix": "%" }
  ],
  "stackChips": ["Flutter & Dart", "Kotlin & Android", "Clean Architecture"]
}
```

## 4. Adding a new project

1. Copy an existing entry in `projects.json` and give it a unique kebab-case `slug`.
2. Add **portrait** mobile screenshots (`390×844` or `360×800` WebP recommended) to `assets/projects/<slug>/` in the repo-root export folder, then mirror them under `composeApp/src/wasmJsMain/resources/assets/projects/<slug>/`.
3. Point `bannerUrl` at the first real screenshot (`assets/projects/<slug>/screen-01.webp`). There are no composite banner files.
4. Add one `screenshots[]` entry per file that actually exists, with meaningful `alt` text and an optional `caption`. Never invent image paths.
5. Fill `problem`, `solution`, and `impact` only when you have verified content; use `null` otherwise. Same rule for `githubUrl` and `caseStudyUrl` — a missing link renders no button.
6. `metrics[].value` is a **string** (e.g. `"24/7"`, `"< 50ms"`) because project results are not always numeric. `featured: true` promotes a card to the large desktop layout.
7. Validate the JSON, rebuild, and check the card plus its modal at mobile and desktop widths.

Example (one `projects.json` entry):

```json
{
  "slug": "dengerind",
  "title": "Dengerind",
  "category": "Mental Health & Teleconsultation",
  "summary": "24/7 mental health platform with real-time counseling chat, VoIP, and low-latency video.",
  "problem": "Immediate, private mental health support is hard to access on short notice.",
  "solution": "Cross-platform app with Agora RTC consultations, encrypted chat, and appointment scheduling.",
  "impact": null,
  "bannerUrl": "assets/projects/dengerind/screen-01.webp",
  "screenshots": [
    {
      "image": "assets/projects/dengerind/screen-01.webp",
      "alt": "Dengerind home screen showing counseling topics and counselor lists",
      "caption": "Consultation Home"
    },
    {
      "image": "assets/projects/dengerind/screen-02.webp",
      "alt": "Counselor profile with real-time availability and booking options",
      "caption": "Counselor Profile & Booking"
    }
  ],
  "technologies": ["Flutter", "Dart", "Agora RTC", "Firebase"],
  "metrics": [
    { "label": "Availability", "value": "24/7" }
  ],
  "accentColor": "#00F0FF",
  "platform": ["Android", "iOS"],
  "githubUrl": null,
  "caseStudyUrl": null,
  "featured": true
}
```

## 5. Replacing the CV

1. Replace `assets/cv/hirarki_cv.pdf` with the new PDF, then copy it to `composeApp/src/wasmJsMain/resources/assets/cv/hirarki_cv.pdf`.
2. If you prefer a different filename or location, update `cvUrl` in `profile.json` instead (keep the path relative, e.g. `assets/cv/hirarki_2026.pdf`).
3. The download button uses this URL directly; it does not need any Kotlin change. Verify that clicking **Download CV** saves a file rather than navigating away.

## 6. Updating the avatar image

1. Replace `assets/profile.webp` and its mirrored copy at `composeApp/src/wasmJsMain/resources/assets/profile.webp`.
2. Use a square WebP of at least `800×800` px for crisp rendering on high-density displays.
3. If the path changes, update `avatarUrl` in `profile.json`. The hero shows the `HW` initials fallback while the image loads or if it is missing.
4. Keep the optional `assets/profile-placeholder.webp` copy in sync if you update the placeholder.

## 7. Rebuilding after changes

```bash
./gradlew :composeApp:wasmJsBrowserDevelopmentRun
```

Open http://localhost:8080 and check the edited section at 360px, 768px, and 1280px widths. For a production check:

```bash
./gradlew :composeApp:wasmJsBrowserDistribution
```

Assets are copied into `composeApp/build/dist/wasmJs/productionExecutable/`. Commit and push to `main`; the GitHub Actions deploy workflow publishes that output to GitHub Pages automatically.

## 8. JSON validation

Paste edited files into [jsonlint.com](https://www.jsonlint.com/) (or run `python3 -m json.tool <file>`) before committing. A single trailing comma or missing quote stops the entire page from loading because the app shows a runtime fault state on parse failure. Also check that every asset path in the JSON resolves to a real file under `composeApp/src/wasmJsMain/resources/assets/`.

## Quick reference: other CMS shapes

`experiences.json` (array, newest first):

```json
[
  {
    "id": "axa-mandiri",
    "role": "Senior Android Developer (Kotlin)",
    "company": "AXA Mandiri",
    "companyType": "Financial Services & Insurance",
    "period": "Nov 2022 – Present",
    "description": "Enterprise mobile apps for policyholders and financial advisors.",
    "achievements": ["Implemented biometric auth and certificate pinning"],
    "current": true,
    "accentColor": "#00F0FF"
  }
]
```

`services.json` (array). `iconKey` must be one of `mobile`, `shield`, `rtc`, `speed`:

```json
[
  {
    "id": "cross-platform-dev",
    "title": "Cross-Platform Mobile Engineering",
    "description": "Production-ready Flutter & Android applications.",
    "features": ["Flutter & Dart development for Android and iOS"],
    "iconKey": "mobile"
  }
]
```

`skills.json` (single object; each pill is rendered as a `TechPill`):

```json
{
  "categories": [
    {
      "name": "Languages & Core",
      "skills": ["Dart", "Kotlin", "Java", "PHP", "SQL"]
    }
  ]
}
```
