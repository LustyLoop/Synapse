plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
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
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.ui)
    testImplementation(libs.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)
    implementation(libs.androidx.compose.material.icons.extended)
    implementation ("androidx.lifecycle:lifecycle-viewmodel-compose:2.11.0")
    implementation ("androidx.activity:activity-ktx:1.13.0")
    implementation("androidx.navigation:navigation-compose:2.8.0")

    implementation(platform("com.aallam.openai:openai-client-bom:4.1.0"))
    implementation("com.aallam.openai:openai-client")
    runtimeOnly("io.ktor:ktor-client-okhttp")

    implementation("com.mikepenz:multiplatform-markdown-renderer:0.43.0")
    implementation("com.mikepenz:multiplatform-markdown-renderer-m3:0.43.0")
    implementation("com.mikepenz:multiplatform-markdown-renderer-code:0.43.0")

    implementation("dev.seyfarth:tabler-icons-kmp:1.0.0")

    implementation("androidx.graphics:graphics-core:1.0.0")

    implementation("com.squareup.retrofit2:retrofit:2.11.0")

}

