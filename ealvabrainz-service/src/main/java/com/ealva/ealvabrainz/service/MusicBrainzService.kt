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
import com.ealva.ealvabrainz.R
import com.ealva.ealvabrainz.art.RemoteImage
import com.ealva.ealvabrainz.brainz.MusicBrainz
import com.ealva.ealvabrainz.brainz.data.Artist
import com.ealva.ealvabrainz.brainz.data.ArtistList
import com.ealva.ealvabrainz.brainz.data.ArtistMbid
import com.ealva.ealvabrainz.brainz.data.Release
import com.ealva.ealvabrainz.brainz.data.ReleaseGroup
import com.ealva.ealvabrainz.brainz.data.ReleaseGroupList
import com.ealva.ealvabrainz.brainz.data.ReleaseGroupMbid
import com.ealva.ealvabrainz.brainz.data.ReleaseList
import com.ealva.ealvabrainz.brainz.data.ReleaseMbid
import com.ealva.ealvabrainz.brainz.data.joinToInc
import com.ealva.ealvabrainz.brainz.data.mbid
import com.ealva.ealvabrainz.common.AlbumName
import com.ealva.ealvabrainz.common.ArtistName
import com.ealva.ealvabrainz.service.R.string.UnexpectedErrorWithMsg
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import retrofit2.Response
import retrofit2.Retrofit
import timber.log.Timber

/**
 * MusicBrainzService is a wrapper around a Retrofit MusicBrainz and CoverArt instance that
 * provides higher level functionality, including coordinating between the two to retrieve
 * artwork for Releases and Release Groups
 */
interface MusicBrainzService {

  /**
   * Find a [ReleaseList] based on [artist] and [album] (album = release), limiting
   * the results to [limit], starting at [offset]. The [limit] and [offset] facilitate paging
   * through results
   *
   * @return the [ReleaseList] or null
   * @throws MusicBrainzException which contains the original cause
   */
  suspend fun findRelease(
    artist: ArtistName,
    album: AlbumName,
    limit: Int? = null,
    offset: Int? = null
  ): ReleaseList?

  /**
   * Find the [Release] identified by [ReleaseMbid]. Use [include] to specify information to be
   * included in the Release.
   *
   * @return the [Release] associated with [mbid] or null
   * @throws MusicBrainzException which contains the original cause
   */
  suspend fun lookupRelease(
    mbid: ReleaseMbid,
    include: List<Release.Lookup> = emptyList()
  ): Release?

  /**
   * Find [Release]s based on [artist] and [album] and convert the results to a flow
   * of [RemoteImage]
   */
  fun getReleaseArt(
    artist: ArtistName,
    album: AlbumName,
    maxReleases: Int = DEFAULT_MAX_RELEASE_COUNT
  ): Flow<RemoteImage>

  /**
   * Find a [ReleaseGroupList] based on [artist] and [album] (album = release), limiting
   * the results to [limit], starting at [offset]. The [limit] and [offset] facilitate paging
   * through results
   *
   * @return the [ReleaseGroupList] or null
   * @throws MusicBrainzException which contains the original cause
   */
  suspend fun findReleaseGroup(
    artist: ArtistName,
    album: AlbumName,
    limit: Int? = null,
    offset: Int? = null
  ): ReleaseGroupList?

  /**
   * Find the [ReleaseGroup] identified by [ReleaseGroupMbid]. Use [include] to specify information
   * to be included in the ReleaseGroup.
   *
   * @return the [ReleaseGroup] associated with [mbid] or null
   * @throws MusicBrainzException which contains the original cause
   */
  suspend fun lookupReleaseGroup(
    mbid: ReleaseGroupMbid,
    include: List<ReleaseGroup.Lookup>
  ): ReleaseGroup?

  /**
   * Find [ReleaseGroup]s based on [artist] and [album] and convert the results to a flow
   * of [RemoteImage]
   */
  fun getReleaseGroupArt(
    artist: ArtistName,
    album: AlbumName,
    maxReleases: Int = DEFAULT_MAX_RELEASE_COUNT
  ): Flow<RemoteImage>

  /**
   * Find an [ArtistList] based on [artist], limiting the results to [limit], starting
   * at [offset]. The [limit] and [offset] facilitate paging through results
   *
   * @return the [ArtistList] or null
   * @throws MusicBrainzException which contains the original cause
   */
  suspend fun findArtist(
    artist: ArtistName,
    limit: Int? = null,
    offset: Int? = null
  ): ArtistList?

  /**
   * Find the [Artist] identified by [ArtistMbid]. Use [include] to specify information to be
   * included in the Release.
   *
   * @return the [Artist] associated with [mbid] or null
   * @throws MusicBrainzException which contains the original cause
   */
  suspend fun lookupArtist(
    mbid: ArtistMbid,
    include: List<Artist.Lookup> = emptyList()
  ): Artist?

  companion object {
    const val DEFAULT_MAX_RELEASE_COUNT = 10

    fun make(
      ctx: Context,
      appName: String,
      appVersion: String,
      contact: String,
      coverArt: CoverArtService
    ): MusicBrainzService =
      make(ctx, ctx.buildMusicBrainz(appName, appVersion, contact), coverArt, Dispatchers.IO)

    internal fun make(
      ctx: Context,
      brainz: MusicBrainz,
      coverArt: CoverArtService,
      dispatcher: CoroutineDispatcher
    ): MusicBrainzService {
      return MusicBrainzServiceImpl(ctx.applicationContext, brainz, coverArt, dispatcher)
    }
  }
}

//    private static final String URL_REGISTER = WEBSITE + "register";
//    private static final String URL_FORGOT_PASS = WEBSITE + "lost-password";
//    private static final String URL_DONATE = "http://metabrainz.org/donate";

private val SERVICE_NAME = MusicBrainzServiceImpl::class.java.simpleName
private const val CACHE_DIR = "MusicBrainz"

internal class MusicBrainzServiceImpl(
  private val context: Context,
  private val musicBrainz: MusicBrainz,
  private val coverArtService: CoverArtService,
  private val dispatcher: CoroutineDispatcher
) : MusicBrainzService {

  override suspend fun findRelease(
    artist: ArtistName,
    album: AlbumName,
    limit: Int?,
    offset: Int?
  ): ReleaseList? = handleResponse {
    val query = """artist:"${artist.value}" AND release:"${album.value}""""
    musicBrainz.findRelease(query, limit, offset)
  }

  override suspend fun lookupRelease(
    mbid: ReleaseMbid,
    include: List<Release.Lookup>
  ) = handleResponse { musicBrainz.lookupRelease(mbid.value, include.joinToInc()) }

  @UseExperimental(ExperimentalCoroutinesApi::class)
  override fun getReleaseArt(
    artist: ArtistName,
    album: AlbumName,
    maxReleases: Int
  ) = findReleases(artist, album, maxReleases).transform(coverArtService).flowOn(dispatcher)

  override suspend fun findReleaseGroup(
    artist: ArtistName,
    album: AlbumName,
    limit: Int?,
    offset: Int?
  ) = handleResponse {
    val query = """artist:"${artist.value}" AND release:"${album.value}""""
    musicBrainz.findReleaseGroup(query, limit, offset)
  }

  override suspend fun lookupReleaseGroup(
    mbid: ReleaseGroupMbid,
    include: List<ReleaseGroup.Lookup>
  ) = handleResponse { musicBrainz.lookupReleaseGroup(mbid.value, include.joinToInc()) }

  @UseExperimental(ExperimentalCoroutinesApi::class)
  override fun getReleaseGroupArt(
    artist: ArtistName,
    album: AlbumName,
    maxReleases: Int
  ) = findReleaseGroups(artist, album, maxReleases)
    .transform(coverArtService)
    .flowOn(dispatcher)

  override suspend fun findArtist(artist: ArtistName, limit: Int?, offset: Int?) = handleResponse {
    val query = """artist:"${artist.value}""""
    musicBrainz.findArtist(query, limit, offset)
  }

  override suspend fun lookupArtist(mbid: ArtistMbid, include: List<Artist.Lookup>) =
    handleResponse { musicBrainz.lookupArtist(mbid.value, include.joinToInc()) }

  @UseExperimental(ExperimentalCoroutinesApi::class)
  private fun findReleases(artist: ArtistName, album: AlbumName, maxReleases: Int) = flow {
    val query = """artist:"${artist.value}" AND release:"${album.value}""""
    musicBrainz.findRelease(query, maxReleases)
      .list()
      .sortedByDescending { it.score }
      .forEach { emit(it.mbid) }
  }.flowOn(dispatcher)

  @UseExperimental(ExperimentalCoroutinesApi::class)
  private fun findReleaseGroups(artist: ArtistName, album: AlbumName, maxReleases: Int) = flow {
    val query = """artist:"${artist.value}" AND release:"${album.value}""""
    musicBrainz.findReleaseGroup(query, maxReleases)
      .list()
      .sortedByDescending { it.score }
      .forEach { emit(it.mbid) }
  }.flowOn(dispatcher)

  /**
   * If response isSuccessful returns T, otherwise logs and returns null.
   *
   * @throws MusicBrainzException if call to [block] throws (typically IOException from Retrofit
   * call)
   */
  @Suppress("BlockingMethodInNonBlockingContext")
  suspend fun <T> handleResponse(block: suspend () -> Response<T>): T? = withContext(dispatcher) {
    try {
      block().run {
        if (isSuccessful) {
          body()
        } else {
          Timber.e(
            "Response code:%d message:%s %s",
            code(),
            message(),
            errorBody()?.string() ?: "No error body"
          )
          null
        }
      }
    } catch (e: Exception) {
      throw MusicBrainzException(
        context.getString(UnexpectedErrorWithMsg, e.localizedMessage ?: "null"),
        e
      )
    }
  }
}

private fun Context.buildMusicBrainz(appName: String, appVersion: String, emailContact: String) =
  Retrofit.Builder()
    .client(makeOkHttpClient(SERVICE_NAME, CACHE_DIR, appName, appVersion, emailContact))
    .baseUrl(getString(R.string.music_brainz_api_secure_url))
    .addMoshiConverterFactory()
    .build()
    .create(MusicBrainz::class.java)

@JvmName(name = "getReleaseList")
private fun Response<ReleaseList>.list(): List<Release> {
  return if (isSuccessful) body()?.releases.orEmpty() else {
    Timber.e("%d %s", code(), raw().toString())
    emptyList()
  }
}

@JvmName(name = "getReleaseGroupList")
private fun Response<ReleaseGroupList>.list(): List<ReleaseGroup> {
  return if (isSuccessful) body()?.releaseGroups.orEmpty() else {
    Timber.e("%d %s", code(), raw().toString())
    emptyList()
  }
}

