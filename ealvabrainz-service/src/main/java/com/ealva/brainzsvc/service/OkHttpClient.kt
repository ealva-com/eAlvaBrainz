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

@file:Suppress("Indentation")

package com.ealva.brainzsvc.service

import com.ealva.brainzsvc.net.BrainzJsonFormatUserAgentInterceptor
import com.ealva.brainzsvc.net.CacheControlInterceptor
import com.ealva.brainzsvc.net.ThrottlingInterceptor
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber
import java.io.File

private const val DAYS_MAX_AGE = 14
private const val DAYS_MIN_FRESH = 14
private const val DAYS_MAX_STALE = 365
private const val MUSICBRAINZ_MAX_CALLS_PER_SECOND = 1.0

private const val TEN_MEG = 10 * 1024 * 1024

internal fun makeOkHttpClient(
  serviceName: String,
  appName: String,
  appVersion: String,
  contactEmail: String,
  cacheDirectory: File,
  addLoggingInterceptor: Boolean = false
): OkHttpClient {
  return OkHttpClient.Builder()
    .addInterceptor(CacheControlInterceptor(DAYS_MAX_AGE, DAYS_MIN_FRESH, DAYS_MAX_STALE))
    .addInterceptor(ThrottlingInterceptor(MUSICBRAINZ_MAX_CALLS_PER_SECOND, serviceName))
    .addInterceptor(BrainzJsonFormatUserAgentInterceptor(appName, appVersion, contactEmail))
    .interceptLoggingInDebug(addLoggingInterceptor)
    .cache(Cache(cacheDirectory, TEN_MEG.toLong()))
    .build()
}

@Suppress("NOTHING_TO_INLINE")
internal inline fun OkHttpClient.Builder.interceptLoggingInDebug(
  addLoggingInterceptor: Boolean
): OkHttpClient.Builder {
  if (addLoggingInterceptor) {
    addInterceptor(
      HttpLoggingInterceptor { message ->
        Timber.i(message)
      }.apply {
        level = HttpLoggingInterceptor.Level.BASIC
      }
    )
  }
  return this
}
