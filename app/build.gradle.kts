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
  compileSdkVersion(Sdk.COMPILE_SDK_VERSION)

  defaultConfig {
    minSdkVersion(Sdk.MIN_SDK_VERSION)
    targetSdkVersion(Sdk.TARGET_SDK_VERSION)

    applicationId = AppCoordinates.APP_ID
    versionCode = AppCoordinates.APP_VERSION_CODE
    versionName = AppCoordinates.APP_VERSION_NAME
    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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

  lintOptions {
    isWarningsAsErrors = false
    isAbortOnError = false
  }

  kotlinOptions {
    jvmTarget = "1.8"
    suppressWarnings = false
    verbose = true
    freeCompilerArgs = listOf(
      "-XXLanguage:+InlineClasses",
      "-Xinline-classes",
      "-Xopt-in=kotlin.RequiresOptIn"
    )
  }
}

dependencies {
  coreLibraryDesugaring(ToolsLib.DESUGARING)
  implementation(project(":ealvabrainz"))
  implementation(project(":ealvabrainz-service"))
  implementation(kotlin("stdlib-jdk8"))

  implementation(SupportLibs.ANDROIDX_APPCOMPAT)
  implementation(SupportLibs.ANDROIDX_CONSTRAINT_LAYOUT)
  implementation(SupportLibs.ANDROIDX_CORE_KTX)
  implementation(SupportLibs.ANDROIDX_LIFECYCLE_RUNTIME_KTX)

  implementation("androidx.viewpager2:viewpager2:1.0.0")
  implementation("androidx.activity:activity-ktx:1.1.0")
  implementation("androidx.fragment:fragment-ktx:1.2.5")
  implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.2.0")
  implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.2.0")
  implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.2.0")
  implementation("androidx.lifecycle:lifecycle-common-java8:2.2.0")
  implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
  implementation("androidx.constraintlayout:constraintlayout:2.0.4")
  implementation("com.google.android.material:material:1.2.1")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.1")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.4.1")
  implementation("com.jakewharton.timber:timber:4.7.1")
  implementation("com.squareup.retrofit2:retrofit:2.7.1")
  implementation("com.louiscad.splitties:splitties-systemservices:3.0.0-alpha06")
  implementation("com.louiscad.splitties:splitties-views-dsl:3.0.0-alpha06")
  implementation("com.louiscad.splitties:splitties-views-dsl-coordinatorlayout:3.0.0-alpha06")
  implementation("com.louiscad.splitties:splitties-views-dsl-constraintlayout:3.0.0-alpha06")
  implementation("com.louiscad.splitties:splitties-views-dsl-recyclerview:3.0.0-alpha06")
  implementation("com.louiscad.splitties:splitties-views-dsl-material:3.0.0-alpha06")
  implementation("com.louiscad.splitties:splitties-views-dsl-appcompat:3.0.0-alpha06")
  implementation("com.louiscad.splitties:splitties-toast:3.0.0-alpha06")
  implementation("com.louiscad.splitties:splitties-snackbar:3.0.0-alpha06")
  implementation("com.louiscad.splitties:splitties-resources:3.0.0-alpha06")
  implementation("me.gujun.android:span:1.7")
  implementation("com.mikepenz:iconics-core:5.0.1")
  implementation("com.mikepenz:material-design-iconic-typeface:2.2.0.6-kotlin@aar")
  implementation("com.github.castorflex.smoothprogressbar:library-circular:1.3.0")
  implementation("com.neovisionaries:nv-i18n:1.27")
  implementation("com.github.bumptech.glide:glide:4.11.0")
  implementation("com.github.bumptech.glide:okhttp3-integration:4.11.0")

  implementation(ThirdParty.EALVALOG)
  implementation(ThirdParty.EALVALOG_CORE)
  implementation(ThirdParty.EALVALOG_ANDROID)

  implementation(ThirdParty.KOIN)
  implementation(ThirdParty.KOIN_ANDROID)

  testImplementation(TestingLib.JUNIT)
  testImplementation(AndroidTestingLib.ANDROIDX_TEST_CORE) {
    exclude("junit", "junit")
  }
  testImplementation(AndroidTestingLib.ANDROIDX_TEST_RULES) {
    exclude("junit", "junit")
  }
  testImplementation(TestingLib.EXPECT)
}
