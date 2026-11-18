import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinSerialization)
}

kotlin {
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        moduleName = "composeApp"
        browser {
            commonWebpackConfig {
                outputFileName = "composeApp.js"
                sourceMaps = false
            }
            webpackTask {
                mainOutputFileName = "composeApp.js"
                sourceMaps = false
            }
            runTask {
                mainOutputFileName = "composeApp.js"
            }
        }
        binaries.executable()
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.kotlinx.coroutines.core)
        }

        wasmJsMain.dependencies {
            implementation(libs.kotlinx.coroutines.core)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

tasks.named("wasmJsBrowserDistribution") {
    doLast {
        val workingDir = File(System.getProperty("user.dir"))
        val distDir = if (File(workingDir, "settings.gradle.kts").exists()) {
            File(workingDir, "composeApp/build/dist/wasmJs/productionExecutable")
        } else {
            File(workingDir, "build/dist/wasmJs/productionExecutable")
        }
        val jsFile = distDir.resolve("composeApp.js")
        val indexFile = distDir.resolve("index.html")
        distDir.resolve("composeApp.js.map").delete()
        if (jsFile.exists() && indexFile.exists()) {
            val jsText = jsFile.readText()
            val wasmFileNames = Regex("""a\.p\+"([a-f0-9]+\.wasm)"""")
                .findAll(jsText)
                .map { it.groupValues[1] }
                .toSet()
            val appWasmFileName = wasmFileNames.firstOrNull()
            if (appWasmFileName != null) {
                indexFile.writeText(
                    indexFile.readText().replace("href=\"composeApp.wasm\"", "href=\"$appWasmFileName\"")
                )
            }
            distDir.listFiles { file -> file.extension == "wasm" }
                ?.forEach { file ->
                    if (file.name !in wasmFileNames) file.delete()
                }
        }
    }
}
