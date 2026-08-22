plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidKotlinMultiplatformLibrary)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    android {
        namespace = "com.phucynwa.geckompose.lib"
        compileSdk {
            version = release(37) {
                minorApiLevel = 1
            }
        }
        minSdk = 26

        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
        }
    }

    sourceSets {
        androidMain.dependencies {
            implementation(libs.core.ktx)
            implementation(libs.appcompat)
            implementation(libs.activity.compose)

            implementation(project.dependencies.platform(libs.compose.bom))
            implementation(libs.ui)
            implementation(libs.ui.graphics)
            implementation(libs.ui.tooling.preview)
            implementation(libs.material3)

            implementation(libs.geckoview)
        }
    }
}
