This is a Kotlin Multiplatform / Compose Multiplatform portfolio website targeting Kotlin/Wasm.

The app lives in the single `:composeApp` module:

- `composeApp/src/wasmJsMain/kotlin/` — UI, data models, and JSON data source
- `composeApp/src/wasmJsMain/resources/assets/` — served content (`data/*.json`, `profile.webp`, `projects/<slug>/screen-XX.webp`, `cv/hirarki_cv.pdf`)
- `composeApp/src/commonMain/composeResources/drawable/` — vector icon assets used through generated `Res` accessors
- `composeApp/src/wasmJsMain/resources/favicon.svg` — browser favicon

The root `assets/` folder holds the original exported images; the served copies live under `composeApp/src/wasmJsMain/resources/assets/`.

### Running the app

```bash
./gradlew :composeApp:wasmJsBrowserDevelopmentRun
```

### Building for production

```bash
./gradlew :composeApp:wasmJsBrowserDistribution
```

Output: `composeApp/build/dist/wasmJs/productionExecutable/`
