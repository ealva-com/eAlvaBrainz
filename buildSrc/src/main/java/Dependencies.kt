/*
 * Copyright (c) 2021  Eric A. Snell
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

object SdkVersion {
  const val COMPILE = 31
  const val MIN = 21
  const val TARGET = 30
}

object PluginsVersion {
  const val AGP = "7.0.3"
  const val DETEKT = "1.18.1"
  const val DOKKA = "1.5.31"
  const val KOTLIN = "1.5.31"
  const val VANNIKTECH_PUBLISH = "0.18.0"
  const val VERSIONS = "0.39.0"
}

object Libs {
  const val AGP = "com.android.tools.build:gradle:${PluginsVersion.AGP}"
  const val DESUGAR = "com.android.tools:desugar_jdk_libs:1.1.5"

  object AndroidX {
    const val APPCOMPAT = "androidx.appcompat:appcompat:1.3.1"
    const val PALETTE = "androidx.palette:palette:1.0.0"
    const val STARTUP = "androidx.startup:startup-runtime:1.1.0"

    object Ktx {
      const val CORE = "androidx.core:core-ktx:1.7.0"
    }

    object Lifecycle {
      private const val VERSION = "2.3.1"
      const val RUNTIME_KTX = "androidx.lifecycle:lifecycle-runtime-ktx:2.4.0-alpha01"
    }

    object Test {
      private const val VERSION = "1.4.0"
      const val CORE = "androidx.test:core:$VERSION"
      const val RULES = "androidx.test:rules:$VERSION"
      const val RUNNER = "androidx.test:runner:$VERSION"

      object Ext {
        private const val VERSION = "1.1.3"
        const val JUNIT = "androidx.test.ext:junit-ktx:$VERSION"
      }
    }
  }

  object Credentials {
    const val OKHTTP_DIGEST = "io.github.rburgst:okhttp-digest:2.5"
  }

  object Expect {
    const val EXPECT = "com.nhaarman:expect.kt:1.0.1"
  }

  object Fastutil {
    const val FASTUTIL = "it.unimi.dsi:fastutil:7.2.1"
  }

  object Koin {
    private const val VERSION = "3.1.3"
    const val CORE = "io.insert-koin:koin-core:$VERSION"
    const val ANDROID = "io.insert-koin:koin-android:$VERSION"
  }

  object Kotlin {
    private const val VERSION = "1.5.31"
    const val KGP = "org.jetbrains.kotlin:kotlin-gradle-plugin:$VERSION"

    object Coroutines {
      private const val VERSION = "1.5.2"
      const val CORE = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$VERSION"
      const val ANDROID =
        "org.jetbrains.kotlinx:kotlinx-coroutines-android:$VERSION"
      const val TEST = "org.jetbrains.kotlinx:kotlinx-coroutines-test:$VERSION"
    }
  }

  object JUnit {
    private const val VERSION = "4.13.2"
    const val JUNIT = "junit:junit:$VERSION"
  }

  object Log {
    private const val VERSION = "0.5.6-SNAPSHOT"
    const val EALVALOG = "com.ealva:ealvalog:$VERSION"
    const val CORE = "com.ealva:ealvalog-core:$VERSION"
    const val ANDROID = "com.ealva:ealvalog-android:$VERSION"
  }

  object Mockito {
    const val INLINE = "org.mockito:mockito-inline:4.0.0"
    const val KOTLIN = "com.nhaarman.mockitokotlin2:mockito-kotlin:2.2.0"
  }

  object Result {
    private const val VERSION = "1.1.13"
    const val RESULT = "com.michael-bull.kotlin-result:kotlin-result:$VERSION"
    const val COROUTINES = "com.michael-bull.kotlin-result:kotlin-result-coroutines:$VERSION"
  }

  object Robolectric {
    const val ROBOLECTRIC = "org.robolectric:robolectric:4.6.1"
  }

  object Square {
    private const val MOSHI_VERSION = "1.12.0"
    private const val RETRO_VERSION = "2.9.0"
    private const val OK_VERSION = "4.9.2"
    const val MOSHI = "com.squareup.moshi:moshi:$MOSHI_VERSION"
    const val MOSHI_CODEGEN = "com.squareup.moshi:moshi-kotlin-codegen:$MOSHI_VERSION"
    const val MOSHI_RETROFIT = "com.squareup.retrofit2:converter-moshi:$RETRO_VERSION"
    const val OKHTTP = "com.squareup.okhttp3:okhttp:$OK_VERSION"
    const val OKHTTP_LOGGING = "com.squareup.okhttp3:logging-interceptor:$OK_VERSION"
    const val RETROFIT = "com.squareup.retrofit2:retrofit:$RETRO_VERSION"
  }
}
