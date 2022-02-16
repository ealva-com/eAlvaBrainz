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

import android.content.Intent
import androidx.core.net.toUri
import com.ealva.ealvabrainz.brainz.CoverArt
import com.ealva.ealvabrainz.brainz.data.CoverArtRelease
import com.ealva.ealvabrainz.brainz.data.ReleaseGroupMbid
import com.ealva.ealvabrainz.brainz.data.ReleaseMbid
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.coroutines.runSuspendCatching
import com.github.michaelbull.result.mapError
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit

public typealias CoverArtResult = Result<CoverArtRelease, BrainzMessage>

/**
 * CoverArtService is a wrapper around a Retrofit CoverArt instance that provides higher level
 * functionality and some extra type safety.
 */
public interface CoverArtService {

  public suspend fun getReleaseArt(mbid: ReleaseMbid): CoverArtResult

  public suspend fun getReleaseGroupArt(mbid: ReleaseGroupMbid): CoverArtResult

  public companion object {
    /**
     * Intent to view the MusicBrainz website
     */
    @Suppress("unused")
    public val BRAINZ_INTENT: Intent =
      Intent(Intent.ACTION_VIEW, "https://musicbrainz.org/".toUri())

    public operator fun invoke(
      okHttpClient: OkHttpClient,
      dispatcher: CoroutineDispatcher = Dispatchers.IO
    ): CoverArtService = CoverArtServiceImpl(buildCoverArt(okHttpClient), dispatcher)
  }
}

private fun buildCoverArt(client: OkHttpClient) = Retrofit.Builder()
  .client(client)
  .baseUrl(COVER_ART_API_SECURE_URL)
  .addMoshiConverterFactory()
  .build()
  .create(CoverArt::class.java)

// private const val COVER_ART_API_URL = "http://coverartarchive.org/"
private const val COVER_ART_API_SECURE_URL = "https://coverartarchive.org/"

private typealias CoverArtCall<T> = suspend CoverArt.() -> Response<T>

private class CoverArtServiceImpl(
  private val coverArt: CoverArt,
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
    runSuspendCatching { coverArt.block() }
      .mapError { ex -> BrainzMessage.BrainzExceptionMessage(ex) }
      .mapResponse()
  }
}
