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

package com.ealva.ealvabrainz.service

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ealva.ealvabrainz.okhttp.BrainzJsonFormatUserAgentInterceptor
import com.ealva.ealvabrainz.okhttp.CacheControlInterceptor
import com.ealva.ealvabrainz.okhttp.ThrottlingInterceptor
import com.nhaarman.expect.expect
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class OkHttpClientBuilderTest {
  private val context = ApplicationProvider.getApplicationContext<Context>()

  @Test
  fun `test okhttp built correctly`() {
    val cacheDir = "CacheDir"
    val okhttp = context.makeOkHttpClient("ServiceName", cacheDir, "appName", "version", "email")
    expect(okhttp.interceptors.find { it is CacheControlInterceptor }).toNotBeNull { "Missing CacheControlInterceptor" }
    expect(okhttp.interceptors.find { it is ThrottlingInterceptor }).toNotBeNull { "Missing ThrottlingInterceptor" }
    expect(okhttp.interceptors.find { it is BrainzJsonFormatUserAgentInterceptor }).toNotBeNull { "Missing BrainzUserAgentInterceptor" }
    expect(okhttp.cache).toNotBeNull { "Cache not found and MusicBrainz requires" }
    expect(okhttp.cache?.directory?.path).toEndWith(cacheDir)
  }

}
