# Product Requirement Document (PRD) & Project Brief

**Project Name:** Hirarki Ardi Pratama Wendri – Personal Mobile Developer Portfolio  
**Status:** In Development / V1 Active  
**Author:** Product & Design Team  
**Date:** March 2025  
**Target Platform:** Web (Responsive Desktop & Mobile Viewports)  
**Live Canvas Reference:** `{{DATA:SCREEN:SCREEN_2}}`  
**Design System Reference:** `{{DATA:DESIGN_SYSTEM:DESIGN_SYSTEM_1}}` (Obsidian Cyber-Core)

---

## 1. Executive Summary & Objective

### 1.1 Objective
Deliver a high-impact, modern, and accessible personal developer portfolio for **Hirarki Ardi Pratama Wendri**, a Middle Mobile Developer specializing in high-performance cross-platform (Flutter) and native (Kotlin / Android Jetpack Compose / Swift) mobile architectures.

### 1.2 Target Audience
- **Tech Recruiters & Engineering Hiring Managers:** Seeking proof of architectural maturity, clean code standards, CI/CD fluency, and production track records.
- **Enterprise Clients & Founders:** Looking for end-to-end mobile app engineering capability (from greenfield MVP to app store deployment and BLE/IoT integrations).
- **Engineering Peers & Tech Community:** Reviewing open-source contributions, technical write-ups, and interactive case studies.

### 1.3 Core Value Proposition
Demonstrating that engineering rigor (clean architecture, BLoC/MVVM, frame budgets, zero Jank) can coexist with fluid 60-120fps motion design, haptics, and polished user experiences.

---

## 2. Branding & Design System Guidelines

Based on the **Obsidian Cyber-Core** design system:

| Attribute | Specification |
| :--- | :--- |
| **Color Scheme** | Deep Obsidian Dark Theme |
| **Surface Lowest / Background** | `#0C0E14` / `#111319` |
| **Surface Containers** | `#191B22` / `#23252E` / `#373940` |
| **Primary Accent** | Electric Neon Cyan (`#00F0FF` / `#00E5FF`) |
| **Secondary Accent** | Cyber Magenta / Electric Pink (`#FF007A`) |
| **Typography (Headings)** | Space Grotesk / Inter / Display Sans |
| **Typography (Body / Code)** | Clean geometric sans-serif + JetBrains Mono / monospace tags |
| **Corner Radius** | `rounded-xl` to `rounded-2xl` (4px – 16px) |
| **Elevation & Borders** | 1px subtle glowing border (`rgba(255,255,255,0.08)`) with ambient neon back-glows |

---

## 3. Information Architecture & Section Breakdown

The site is built as a single-page progressive disclosure application featuring seamless navigation:

```
[Sticky Header / Navbar]
       │
       ├── 00. Hero Section & Runtime Status Terminal
       ├── Impact Highlights & Performance Counters
       ├── 01. Profile Matrix & Core Competencies
       ├── 02. Featured Engineering Case Studies (Deep Dives)
       ├── 03. Engineering Capabilities & Services
       ├── 04. Career Journey & Production Track Record
       ├── 05. Contact Terminal & Social Connect
       │
[Footer & Metadata]
```

### 3.1 Global Navigation Bar
- **Brand Identity:** Custom `HW` Monogram logo with status pulse ("Middle Mobile Dev").
- **Navigation Links:** About (`#about`), Projects (`#projects`), Services (`#services`), Experience (`#experience`), Contact (`#contact`).
- **Primary Action:** Direct "Download CV" trigger with trackable download event.

### 3.2 Hero Section
- **Visuals:** Studio portrait with ambient neon rim lighting, dynamic runtime tag (`DEV_RUNTIME // READY`, `120 FPS`), and active status badge.
- **Headline:** *"Engineering Scalable High-Performance Mobile Experiences"*
- **Subheadline:** Articulating specialization in Flutter, Kotlin, Swift, BLoC, and Clean Architecture.
- **CTAs:** Primary "View Featured Projects" button (Pink accent) + Secondary "Download CV" outline button.
- **Stack Strip:** Visual chips highlighting Flutter 3.x, Kotlin & Compose, BLoC, Clean Architecture.

### 3.3 Key Metrics Bar (Proof of Scale)
1. **4+ Years Experience:** Production mobile lifecycle across fintech, logistics, & healthcare.
2. **5+ Mobile Apps Shipped:** Full Google Play & Apple App Store deployments from 0 to 1.
3. **99.8% Crash-Free Sessions:** Proactive error boundaries and Sentry telemetry.
4. **0 Critical Security CVEs:** Strict MASVS compliance, biometric authentication, encrypted keystores.

### 3.4 Profile Matrix & Technical Capabilities
- **Narrative:** Crafting apps that balance architectural rigor with silky fluid interactions.
- **Specialized Disciplines:**
  - Pixel-Perfect UI & Micro-Animations (Render objects, slivers, physics springs)
  - Battery, Memory & GPU Optimization (LeakCanary, profiling rebuild overhead)
  - Offline-First Synchronizations (SQLite / Room / Hive / background reconciliation)
  - Deep Linking & Push Architectures (Universal links, payload interceptors)
- **Categorized Tech Pills:**
  - *Mobile Frameworks:* Flutter 3.x, Dart, Kotlin, Android Jetpack Compose, Swift.
  - *State Management:* BLoC, Riverpod, Clean Architecture, MVVM + StateFlow.
  - *Backend & Storage:* Firebase, Supabase, RESTful/Dio, GraphQL, SQLite, Hive/Isar.
  - *Testing & CI/CD:* Unit & Widget tests, GitHub Actions, Fastlane, Sentry.

### 3.5 Featured Engineering Case Studies
Four flagship project cards with interactive modals/outlinks:
1. **PayPulse (Neobank & Crypto Mobile Wallet):** Real-time crypto pricing via WebSockets, QRIS cross-border payment gateway, biometric KYC verification, hardware-backed keystore.
2. **CareSync (Telehealth & Vitals Monitoring):** Native Android medical app synchronizing low-energy BLE IoT sensors, WebRTC consultations, HL7 FHIR compliance.
3. **AuraStore (Luxury Fashion & AR Fitting):** iOS e-commerce app with real-time AR product trial, Stripe Apple Pay, 120Hz micro-interactions.
4. **LogiRoute (Driver Fleet & Realtime Dispatch):** High-scale driver route optimization, turn-by-turn navigation, battery-efficient background telemetry.

#### 3.5.1 Project Media & Screenshot Showcase Requirements
- **Mobile First Visual Assets:** Most project visuals are actual **PORTRAIT MOBILE APPLICATION SCREENSHOTS**, not generic landscape thumbnails.
- **Aspect Ratio Integrity:** Under no circumstances should portrait screenshots be forced or cropped into arbitrary landscape ratios (such as 16:9).
- **Media Presentation Modes:**
  - **Composite Multi-Screenshot Banner (`bannerUrl`):** High-impact card header banner showcasing 2–3 portrait screens side-by-side on an obsidian themed backdrop.
  - **Individual Screen Gallery (`screenshots[]`):** Ordered list of discrete mobile screenshots with meaningful `alt` descriptions and optional `caption`.
  - **Desktop Display:** Featured case studies leverage wider media areas with side-by-side screenshot arrangements; secondary cards present full contain-fit banners with subtle ambient glow.
  - **Mobile Display:** Touch-optimized horizontally scrollable screenshot rails (LazyRow) with minimum 44dp touch targets and contain-fit behavior.
- **Data Governance & Integrity:**
  - Support `slug`, `title`, `category`, `summary`, optional `problem`, `solution`, `impact`, `bannerUrl`, `screenshots[]`, `technologies[]`, and `metrics[]`.
  - Strict anti-hallucination rule: Only render verified data; do not invent client names, metrics, or store links if absent.

### 3.6 Services & Capabilities
Four structured capability cards:
- Cross-Platform App Development
- Native Android & iOS Engineering (BLE, NFC, Sensors, Secure Enclave)
- App Architecture & Performance Profiling (Rebuild optimization, memory leak triage)
- UI/UX Translation & Fluid Animations (1:1 Figma-to-code fidelity, custom shaders)

### 3.7 Career Journey Timeline
Chronological career milestones highlighting achievements:
- **Middle Mobile Developer (TechNova Solutions - Fintech Flagship Unit):** Spearheading Flutter app for 250k+ MAU, cutting CI/CD deployment from 4h to 18min.
- **Junior Mobile App Developer (Global Digital Kreasi):** Offline caching, fleet telemetry, 99.6% crash-free stability across 12 unique Android OEMs.
- **Mobile Engineering Intern (AppWorks Studio):** Prototyping UI in Figma, writing unit test fixtures, optimizing asset bundling.

### 3.8 Contact Section & Footer
- Direct contact avenues: Email, WhatsApp, and location indicator (Jakarta, Indonesia).
- Social profiles: GitHub, LinkedIn, Twitter/X, Dribbble.
- Quick back-to-top anchor and copyright statement.

---

## 4. Technical Requirements & Performance Budgets

| Metric | Target | Verification Method |
| :--- | :--- | :--- |
| **Lighthouse Performance** | ≥ 95 / 100 | Google Lighthouse audit |
| **First Contentful Paint (FCP)**| < 1.0s | Web Vitals / SpeedCurve |
| **Cumulative Layout Shift (CLS)**| < 0.05 | Core Web Vitals |
| **Accessibility (a11y)** | WCAG 2.1 AA compliant | Axe DevTools & contrast checks (Neon Cyan/Pink on Obsidian) |
| **Responsiveness** | Fluid down to 360px up to 4K Ultrawide | Chrome DevTools device mode |

---

## 5. Roadmap & Future Enhancements

- **Phase 1 (Current):** Single-page portfolio launch with dynamic case studies and downloadable resume.
- **Phase 2 (Interactive Demos):** Embedded Flutter Web sandbox / interactive mock screen runners directly inside case study cards.
- **Phase 3 (Technical Blog / Engineering Notes):** Markdown-powered blog section detailing Flutter memory optimizations and BLE protocol troubleshooting.
