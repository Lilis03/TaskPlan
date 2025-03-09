plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.elitecode.taskplan"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.elitecode.taskplan"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    buildFeatures{
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.compose)
    implementation ("androidx.compose.material3:material3:1.2.0")
    // Import the BoM for the Firebase platform
    implementation(platform("com.google.firebase:firebase-bom:33.8.0"))
    // Add the dependency for the Firebase Authentication library
    // When using the BoM, you don't specify versions in Firebase library dependencies
    implementation("com.google.firebase:firebase-auth")
    // Also add the dependency for the Google Play services library and specify its version
    implementation("com.google.android.gms:play-services-auth:21.3.0")
    //Google services
    implementation("com.google.android.gms:play-services-auth:21.3.0")
    implementation(libs.firebase.firestore.ktx)

    //Testing
    //JUnit para pruebas unitarias
    testImplementation("junit:junit:4.13.2")
    //Robolectric para pruebas de integración sin emulador
    testImplementation("org.robolectric:robolectric:4.10")
    //Compose UI Testing
    androidTestImplementation ("androidx.compose.ui:ui-test-junit4:1.5.0")
    androidTestImplementation(project(":app"))
    debugImplementation ("androidx.compose.ui:ui-test-manifest:1.5.0")
    // Espresso para UI Testing en Compose
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation ("androidx.test.espresso:espresso-contrib:3.5.0")
    androidTestImplementation ("androidx.test.espresso:espresso-idling-resource:3.5.0")
    androidTestImplementation ("org.hamcrest:hamcrest-library:2.2")


    // Testing con ViewModel y LiveData
    testImplementation ("androidx.arch.core:core-testing:2.2.0")
    testImplementation ("io.mockk:mockk:1.13.5")
    androidTestImplementation("org.mockito:mockito-core:4.11.0")
    androidTestImplementation("org.mockito:mockito-android:4.11.0")


    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}