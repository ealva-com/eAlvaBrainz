/*
 * Copyright (c) 2020  Eric A. Snell
 *
 * This file is part of eAlvaBrainz
 *
 * eAlvaBrainz is free software: you can redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation, either version 3 of
 *  the License, or (at your option) any later version.
 *
 * eAlvaBrainz is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with eAlvaBrainz.
 * If not, see <http://www.gnu.org/licenses/>.
 */

plugins {
  id("com.android.application")
  kotlin("android")
}

android {
  compileSdk = SdkVersion.COMPILE

  defaultConfig {
    minSdk = SdkVersion.MIN
    targetSdk = SdkVersion.TARGET

    applicationId = AppCoordinates.APP_ID
    versionCode = AppCoordinates.APP_VERSION_CODE
    versionName = AppCoordinates.APP_VERSION_NAME
    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    signingConfig = signingConfigs.getByName("debug")
  }
  compileOptions {
    isCoreLibraryDesugaringEnabled = true
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
  }
  buildTypes {
    getByName("release") {
      isMinifyEnabled = true
    }
  }

  lint {
    warningsAsErrors = false
    abortOnError = false
  }

  kotlinOptions {
    jvmTarget = "1.8"
    languageVersion = "1.5"
    apiVersion = "1.5"
    suppressWarnings = false
    verbose = true
    freeCompilerArgs = listOf(
      "-Xopt-in=kotlin.RequiresOptIn"
    )
  }
}

dependencies {
  coreLibraryDesugaring(Libs.DESUGAR)
  implementation(project(":ealvabrainz"))
  implementation(project(":ealvabrainz-service"))
  implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.6.10")

  implementation(Libs.AndroidX.APPCOMPAT)
  implementation("androidx.constraintlayout:constraintlayout:2.1.3")
//  implementation("com.android.support.constraint:constraint-layout:2.1.1")
  implementation(Libs.AndroidX.Ktx.CORE)
  implementation(Libs.AndroidX.Lifecycle.RUNTIME_KTX)

  implementation(Libs.Result.RESULT)

  implementation("androidx.viewpager2:viewpager2:1.0.0")
  implementation("androidx.activity:activity-ktx:1.4.0")
  implementation("androidx.fragment:fragment-ktx:1.4.1")
  implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.4.1")
  implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.4.1")
  implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.4.1")
  implementation("androidx.lifecycle:lifecycle-common-java8:2.4.1")
  implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
  implementation("androidx.constraintlayout:constraintlayout:2.1.3")
  implementation("com.google.android.material:material:1.5.0")
  implementation(Libs.Kotlin.Coroutines.CORE)
  implementation(Libs.Kotlin.Coroutines.ANDROID)
  implementation("com.squareup.retrofit2:retrofit:2.9.0")
  implementation("com.louiscad.splitties:splitties-systemservices:3.0.0")
  implementation("com.louiscad.splitties:splitties-views-dsl:3.0.0")
  implementation("com.louiscad.splitties:splitties-views-dsl-coordinatorlayout:3.0.0")
  implementation("com.louiscad.splitties:splitties-views-dsl-constraintlayout:3.0.0")
  implementation("com.louiscad.splitties:splitties-views-dsl-recyclerview:3.0.0")
  implementation("com.louiscad.splitties:splitties-views-dsl-material:3.0.0")
  implementation("com.louiscad.splitties:splitties-views-dsl-appcompat:3.0.0")
  implementation("com.louiscad.splitties:splitties-toast:3.0.0")
  implementation("com.louiscad.splitties:splitties-snackbar:3.0.0")
  implementation("com.louiscad.splitties:splitties-resources:3.0.0")
  implementation("com.mikepenz:iconics-core:5.3.3")
  implementation("com.mikepenz:material-design-iconic-typeface:2.2.0.8-kotlin@aar")
  implementation("com.github.castorflex.smoothprogressbar:library-circular:1.3.0")
  implementation("com.neovisionaries:nv-i18n:1.29")
  implementation("com.github.bumptech.glide:glide:4.13.0")
  implementation("com.github.bumptech.glide:okhttp3-integration:4.13.0")

  implementation(Libs.Log.EALVALOG)
  implementation(Libs.Log.CORE)
  implementation(Libs.Log.ANDROID)

  implementation(Libs.Koin.CORE)
  implementation(Libs.Koin.ANDROID)

  testImplementation(Libs.JUnit.JUNIT)
  testImplementation(Libs.AndroidX.Test.CORE) {
    exclude("junit", "junit")
  }
  testImplementation(Libs.AndroidX.Test.RULES) {
    exclude("junit", "junit")
  }
  testImplementation(Libs.Expect.EXPECT)
}
