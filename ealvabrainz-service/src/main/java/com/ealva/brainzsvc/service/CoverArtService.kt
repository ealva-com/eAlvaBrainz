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

package com.ealva.brainzsvc.service

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.ealva.brainzsvc.service.BrainzMessage.BrainzExceptionMessage
import com.ealva.ealvabrainz.brainz.CoverArt
import com.ealva.ealvabrainz.brainz.data.CoverArtRelease
import com.ealva.ealvabrainz.common.ReleaseGroupMbid
import com.ealva.ealvabrainz.common.ReleaseMbid
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.mapError
import com.github.michaelbull.result.runCatching
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import retrofit2.Retrofit
import java.io.File

public typealias CoverArtResult = Result<CoverArtRelease, BrainzMessage>

/**
 * CoverArtService is a wrapper around a Retrofit CoverArt instance that provides higher level
 * functionality and some extra type safety.
 */
public interface CoverArtService {

  public suspend fun getReleaseArt(mbid: ReleaseMbid): CoverArtResult

  public suspend fun getReleaseGroupArt(mbid: ReleaseGroupMbid): CoverArtResult

  public val resourceFetcher: ResourceFetcher

  public companion object {
    /**
     * Intent to view the MusicBrainz website
     */
    @Suppress("unused")
    public val BRAINZ_INTENT: Intent =
      Intent(Intent.ACTION_VIEW, Uri.parse("https://musicbrainz.org/"))

    /**
     * Instantiate a CoverArtService implementation which handles MusicBrainz server requirements
     * such as a required User-Agent format, throttling requests, and factories/adapters to support
     * the returned data classes.
     */
    public operator fun invoke(
      ctx: Context,
      appName: String,
      appVersion: String,
      contactEmail: String,
      resourceFetcher: ResourceFetcher,
      dispatcher: CoroutineDispatcher = Dispatchers.IO
    ): CoverArtService =
      make(
        buildCoverArt(appName, appVersion, contactEmail, File(ctx.cacheDir, CACHE_DIR)),
        resourceFetcher,
        dispatcher
      )

    /** Internal for test, provides for injecting fakes/mocks/etc and test dispatcher. */
    internal fun make(
      coverArt: CoverArt,
      resourceFetcher: ResourceFetcher,
      dispatcher: CoroutineDispatcher
    ): CoverArtService = CoverArtServiceImpl(coverArt, resourceFetcher, dispatcher)
  }
}

// private const val COVER_ART_API_URL = "http://coverartarchive.org/"
private const val COVER_ART_API_SECURE_URL = "https://coverartarchive.org/"

private val SERVICE_NAME = CoverArtServiceImpl::class.java.simpleName
private const val CACHE_DIR = "CoverArtArchive"
private typealias CoverArtCall<T> = suspend CoverArt.() -> Response<T>

private class CoverArtServiceImpl(
  private val coverArt: CoverArt,
  override val resourceFetcher: ResourceFetcher,
  private val dispatcher: CoroutineDispatcher
) : CoverArtService {
  override suspend fun getReleaseArt(mbid: ReleaseMbid): CoverArtResult = coverArt {
    getArtwork("release", mbid.value)
  }

  override suspend fun getReleaseGroupArt(mbid: ReleaseGroupMbid): CoverArtResult = coverArt {
    getArtwork("release-group", mbid.value)
  }

  suspend fun <T : Any> coverArt(
    block: CoverArtCall<T>
  ): Result<T, BrainzMessage> = withContext(dispatcher) {
    runCatching { coverArt.block() }
      .mapError { ex -> BrainzExceptionMessage(ex) }
      .mapResponse()
  }
}

private fun buildCoverArt(
  appName: String,
  appVersion: String,
  contactEmail: String,
  cacheDirectory: File
) = Retrofit.Builder()
  .client(makeOkHttpClient(SERVICE_NAME, appName, appVersion, contactEmail, cacheDirectory))
  .baseUrl(COVER_ART_API_SECURE_URL)
  .addMoshiConverterFactory()
  .build()
  .create(CoverArt::class.java)
