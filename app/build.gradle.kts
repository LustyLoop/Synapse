plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
    id("com.google.devtools.ksp")
    alias(libs.plugins.kotlin.serialization)
}
val aPI_KEY = project.properties["API_KEY"]
val yANDEX_FOLDER_ID = project.properties["YANDEX_FOLDER_ID"]
android {
    namespace = "com.example.synapseapp"
    compileSdk {
        version = release(37) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "com.example.synapseapp"
        minSdk = 28
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
        }
    }
    compileOptions {

        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}


dependencies {

    implementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(platform(libs.androidx.compose.bom))


    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.foundation)
    implementation("androidx.compose.material3:material3:1.5.0-beta02")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material.icons.extended)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.window)

    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.11.0")
    implementation("androidx.activity:activity-ktx:1.13.0")
    implementation("androidx.navigation:navigation-compose:2.8.0")
    implementation("androidx.compose.material3:material3-window-size-class")


    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)


    implementation(platform(libs.openai.client.bom))
    implementation(libs.openai.client)
    implementation(libs.ktor.client.content.negotiation)


    implementation(platform(libs.ktor.bom))
    implementation(libs.ktor.serialization.kotlinx.json)
    runtimeOnly(libs.ktor.client.okhttp)


    implementation("androidx.room3:room3-runtime:3.0.3")
    ksp("androidx.room3:room3-compiler:3.0.3")


    implementation("com.mikepenz:multiplatform-markdown-renderer:0.43.0")
    implementation("com.mikepenz:multiplatform-markdown-renderer-m3:0.43.0")
    implementation("com.mikepenz:multiplatform-markdown-renderer-code:0.43.0")
    implementation("dev.seyfarth:tabler-icons-kmp:1.0.0")
    implementation("androidx.graphics:graphics-core:1.0.0")
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.google.code.gson:gson:2.13.2")
}