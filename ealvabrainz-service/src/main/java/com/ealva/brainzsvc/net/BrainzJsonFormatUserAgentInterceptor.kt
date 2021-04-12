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

package com.ealva.brainzsvc.net

import okhttp3.Interceptor
import okhttp3.Response

@Suppress("MaxLineLength")
/**
 * Adds a fmt=json and User-Agent to every call
 *
 * Be sure to read
 * [MusicBrainz requirements](https://musicbrainz.org/doc/XML_Web_Service/Rate_Limiting#Provide_meaningful_User-Agent_strings)
 * for querying their servers.
 */
internal class BrainzJsonFormatUserAgentInterceptor(
  userAgentAppName: String,
  userAgentAppVersion: String,
  userAgentContactEmail: String
) : Interceptor {
  private val userAgent = "$userAgentAppName/$userAgentAppVersion ( $userAgentContactEmail )"
  override fun intercept(chain: Interceptor.Chain): Response {
    val original = chain.request()
    val url = original.url.newBuilder().addQueryParameter("fmt", "json").build()
    val request = original.newBuilder().header("User-Agent", userAgent).url(url).build()
    return chain.proceed(request)
  }
}
