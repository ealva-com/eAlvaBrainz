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
version = EalvaBrainzCoordinates.VERSION

plugins {
  id("com.android.library")
  kotlin("android")
  kotlin("kapt")
  id("kotlin-parcelize")
  id("org.jetbrains.dokka")
  id("com.vanniktech.maven.publish")
}

android {
  compileSdk = SdkVersion.COMPILE

  defaultConfig {
    minSdk = SdkVersion.MIN
    targetSdk = SdkVersion.TARGET

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    consumerProguardFiles("consumer-rules.pro")
  }

  compileOptions {
    isCoreLibraryDesugaringEnabled = true
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
  }

  buildTypes {
    getByName("debug") {
      isTestCoverageEnabled = false
    }

    getByName("release") {
      isMinifyEnabled = false
    }
  }

  sourceSets {
    val sharedTestDir = "src/sharedTest/java"
    getByName("test").java.srcDir(sharedTestDir)
    getByName("androidTest").java.srcDir(sharedTestDir)
  }

  lint {
    isWarningsAsErrors = false
    isAbortOnError = false
  }

  testOptions {
    unitTests.isIncludeAndroidResources = true
  }

  kotlinOptions {
    jvmTarget = "1.8"
    languageVersion = "1.5"
    apiVersion = "1.5"
    suppressWarnings = false
    verbose = true
    freeCompilerArgs = listOf(
      "-Xopt-in=kotlin.RequiresOptIn",
      "-Xexplicit-api=warning",
    )
  }
}

dependencies {
  coreLibraryDesugaring(Libs.DESUGAR)
  implementation(kotlin("stdlib-jdk8"))
  implementation(Libs.AndroidX.APPCOMPAT)
  implementation(Libs.AndroidX.Ktx.CORE)

  implementation(Libs.Log.EALVALOG)
  implementation(Libs.Log.CORE)
  implementation(Libs.Fastutil.FASTUTIL)
  implementation(Libs.Kotlin.Coroutines.CORE)
  implementation(Libs.Kotlin.Coroutines.ANDROID)

  implementation(Libs.Square.MOSHI)
  implementation(Libs.Square.MOSHI_RETROFIT)
  kapt(Libs.Square.MOSHI_CODEGEN)

  testImplementation(Libs.JUnit.JUNIT)
  testImplementation(Libs.AndroidX.Test.CORE) {
    exclude("junit", "junit")
  }
  testImplementation(Libs.AndroidX.Test.RULES) {
    exclude("junit", "junit")
  }
  testImplementation(Libs.Expect.EXPECT)
  testImplementation(Libs.Robolectric.ROBOLECTRIC)
  testImplementation(Libs.Kotlin.Coroutines.TEST)
  testImplementation(Libs.Mockito.KOTLIN)
  testImplementation(Libs.Mockito.INLINE)

  androidTestImplementation(Libs.AndroidX.Test.RUNNER) {
    exclude("junit", "junit")
  }
  androidTestImplementation(Libs.AndroidX.Test.Ext.JUNIT) {
    exclude("junit", "junit")
  }
  androidTestImplementation(Libs.JUnit.JUNIT)
  androidTestImplementation(Libs.Expect.EXPECT)
  androidTestImplementation(Libs.Kotlin.Coroutines.TEST)
}
