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
import com.ealva.ealvabrainz.brainz.data.BrainzError
import com.ealva.ealvabrainz.brainz.data.Include
import com.ealva.ealvabrainz.brainz.data.Recording
import com.ealva.ealvabrainz.brainz.data.RecordingList
import com.ealva.ealvabrainz.brainz.data.RecordingMbid
import com.ealva.ealvabrainz.brainz.data.Release
import com.ealva.ealvabrainz.brainz.data.ReleaseGroup
import com.ealva.ealvabrainz.brainz.data.ReleaseGroupList
import com.ealva.ealvabrainz.brainz.data.ReleaseGroupMbid
import com.ealva.ealvabrainz.brainz.data.ReleaseList
import com.ealva.ealvabrainz.brainz.data.ReleaseMbid
import com.ealva.ealvabrainz.brainz.data.joinToInc
import com.ealva.ealvabrainz.brainz.data.mbid
import com.ealva.ealvabrainz.brainz.data.theMoshi
import com.ealva.ealvabrainz.common.AlbumName
import com.ealva.ealvabrainz.common.ArtistName
import com.ealva.ealvabrainz.common.RecordingName
import com.ealva.ealvabrainz.net.RawResponse
import com.ealva.ealvabrainz.net.RetrofitRawResponse
import com.ealva.ealvabrainz.service.MusicBrainzResult.Success
import com.ealva.ealvabrainz.service.MusicBrainzResult.Unknown
import com.squareup.moshi.JsonAdapter
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
 * Result of various calls to [MusicBrainzService]
 */
sealed class MusicBrainzResult<out T : Any> {
  /** Call was successful and contains the [value] */
  data class Success<T : Any>(val value: T) : MusicBrainzResult<T>()

  /** Server returned an error and was converted to the common error body response */
  data class Error(val error: BrainzError) : MusicBrainzResult<Nothing>()

  /**
   * An error occurred and we can't grok the error body. Punt to caller
   */
  data class Unknown(val response: RawResponse) : MusicBrainzResult<Nothing>()

  /** An [exception] was thrown */
  data class Exceptional(val exception: MusicBrainzException) : MusicBrainzResult<Nothing>()
}

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
   * @return [MusicBrainzResult] with the resulting ReleaseList or error/exception information
   * @throws MusicBrainzException if Retrofit throws
   */
  suspend fun findRelease(
    artist: ArtistName,
    album: AlbumName,
    limit: Int? = null,
    offset: Int? = null
  ): MusicBrainzResult<ReleaseList>

  /**
   * Find the [Release] identified by [mbid]. Use [include] to specify information to be
   * included in the Release.
   * @param mbid the MusicBrainzID to lookup
   * @param include list of [Release.Lookup] indicating what info linked to the entity is returned
   * @param type limit linked entities to this [Release.Type]
   * @param status limit linked entities to this [Release.Status]
   * @return a [MusicBrainzResult] with Release if successful
   */
  suspend fun lookupRelease(
    mbid: ReleaseMbid,
    include: List<Release.Lookup> = emptyList(),
    type: Release.Type = Release.Type.Any,
    status: Release.Status = Release.Status.Any
  ): MusicBrainzResult<Release>

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
   * @return a [MusicBrainzResult] with ReleaseGroupList if successful
   */
  suspend fun findReleaseGroup(
    artist: ArtistName,
    album: AlbumName,
    limit: Int? = null,
    offset: Int? = null
  ): MusicBrainzResult<ReleaseGroupList>

  /**
   * Find the [ReleaseGroup] identified by [ReleaseGroupMbid]. Use [include] to specify information
   * to be included in the ReleaseGroup.
   * @param mbid the MusicBrainzID to lookup
   * @param include list of [ReleaseGroup.Lookup] indicating what info linked to the entity is
   * returned
   * @param type limit linked entities to this [Release.Type]
   * @param status limit linked entities to this [Release.Status]
   * @return the [ReleaseGroup] associated with [mbid] or null
   * @throws MusicBrainzException if Retrofit throws or parameters are invalid
   */
  suspend fun lookupReleaseGroup(
    mbid: ReleaseGroupMbid,
    include: List<ReleaseGroup.Lookup> = emptyList(),
    type: Release.Type = Release.Type.Any,
    status: Release.Status = Release.Status.Any
  ): MusicBrainzResult<ReleaseGroup>

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
   * @throws MusicBrainzException if Retrofit throws
   */
  suspend fun findArtist(
    artist: ArtistName,
    limit: Int? = null,
    offset: Int? = null
  ): MusicBrainzResult<ArtistList>

  /**
   * Find the [Artist] identified by [ArtistMbid]. Use [include] to specify information to be
   * included in the Release.
   * @param mbid the MusicBrainzID to lookup
   * @param include list of [Artist.Lookup] indicating what info linked to the entity is returned
   * @param type limit linked entities to this [Release.Type]
   * @param status limit linked entities to this [Release.Status]
   * @return the [Artist] associated with [mbid] or null
   * @throws MusicBrainzException if Retrofit throws or parameters are invalid
   */
  suspend fun lookupArtist(
    mbid: ArtistMbid,
    include: List<Artist.Lookup> = emptyList(),
    type: Release.Type = Release.Type.Any,
    status: Release.Status = Release.Status.Any
  ): MusicBrainzResult<Artist>

  /**
   * Find an [RecordingList] based on [recording], [artist], and [album], limiting the results
   * to [limit], starting at [offset]. The [limit] and [offset] facilitate paging through results
   *
   * @param recording the name of the recording or track
   * @param artist the artist name (optional)
   * @param album the album name (optional)
   * @param limit how many entries should be returned
   * @param offset offset into the returned list, used for paging results
   * @return the [ArtistList] or null
   * @throws MusicBrainzException if Retrofit throws
   */
  suspend fun findRecording(
    recording: RecordingName,
    artist: ArtistName? = null,
    album: AlbumName? = null,
    limit: Int? = null,
    offset: Int? = null
  ): MusicBrainzResult<RecordingList>

  /**
   * Find the [Recording] identified by [RecordingMbid]. Use [include] to specify information to be
   * included in the result.
   * @param mbid the MusicBrainzID to lookup
   * @param include list of [Recording.Lookup] indicating what info linked to the entity is returned
   * @param type limit linked entities to this [Release.Type]
   * @param status limit linked entities to this [Release.Status]
   * @return the [Recording] associated with [mbid] or null
   * @throws MusicBrainzException if Retrofit throws or parameters are invalid
   */
  suspend fun lookupRecording(
    mbid: RecordingMbid,
    include: List<Recording.Lookup> = emptyList(),
    type: Release.Type = Release.Type.Any,
    status: Release.Status = Release.Status.Any
  ): MusicBrainzResult<Recording>

  /**
   * Call the [block] function, which receives [MusicBrainz] as a parameter, in the context of the
   * contained [CoroutineDispatcher] (typically [Dispatchers.IO] when not under test)
   *
   * Usually [block] is a lambda which makes a direct call to the MusicBrainz Retrofit client. The
   * caller is responsible for building the necessary String parameters, "query" in case of a
   * find call and "inc" include if doing a lookup. Use the Subquery and Misc defined
   * in the various entity objects for doing a lookup and use SearchField to build queries
   *
   * @return a [MusicBrainzResult.Success] or [MusicBrainzResult.Error] if response is not
   * successful. [MusicBrainzResult.Exceptional] is returned if an underlying exception is thrown,
   * such as an [IOException][java.io.IOException]. [MusicBrainzResult.Unknown] is returned
   * if there the response is not successful and the error body could not be decoded.
   */
  suspend fun <T : Any> brainz(block: suspend (brainz: MusicBrainz) -> Response<T>): MusicBrainzResult<T>

  @Suppress("MemberVisibilityCanBePrivate", "unused")
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

    const val website = "https://musicbrainz.org/"
    const val registerUrl = """${website}register"""
    const val forgotPasswordUrl = """${website}lost-password"""
    const val donateUrl = "http://metabrainz.org/donate"
  }
}

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
  ): MusicBrainzResult<ReleaseList> = brainz {
    val query = """artist:"${artist.value}" AND release:"${album.value}""""
    musicBrainz.findRelease(query, limit, offset)
  }

  override suspend fun lookupRelease(
    mbid: ReleaseMbid,
    include: List<Release.Lookup>,
    type: Release.Type,
    status: Release.Status
  ) = brainz {
    musicBrainz.lookupRelease(mbid.value, include.joinToInc(), type.value, status.value)
  }

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
  ) = brainz {
    val query = """artist:"${artist.value}" AND release:"${album.value}""""
    musicBrainz.findReleaseGroup(query, limit, offset)
  }

  override suspend fun lookupReleaseGroup(
    mbid: ReleaseGroupMbid,
    include: List<ReleaseGroup.Lookup>,
    type: Release.Type,
    status: Release.Status
  ): MusicBrainzResult<ReleaseGroup> {
    return brainz {
      include.ensureTypeValidity(type)
      musicBrainz.lookupReleaseGroup(
        mbid.value,
        include.joinToInc(),
        type.value,
        status.value
      )
    }
  }

  @UseExperimental(ExperimentalCoroutinesApi::class)
  override fun getReleaseGroupArt(
    artist: ArtistName,
    album: AlbumName,
    maxReleases: Int
  ) = findReleaseGroups(artist, album, maxReleases)
    .transform(coverArtService)
    .flowOn(dispatcher)

  override suspend fun findArtist(artist: ArtistName, limit: Int?, offset: Int?) = brainz {
    val query = """artist:"${artist.value}""""
    musicBrainz.findArtist(query, limit, offset)
  }

  override suspend fun lookupArtist(
    mbid: ArtistMbid,
    include: List<Artist.Lookup>,
    type: Release.Type,
    status: Release.Status
  ): MusicBrainzResult<Artist> {
    return brainz {
      include.ensureTypeValidity(type)
      include.ensureStatusValidity(status)
      musicBrainz.lookupArtist(mbid.value, include.joinToInc(), type.value, status.value)
    }
  }

  private fun List<Include>.ensureStatusValidity(status: Release.Status) {
    if (status !== Release.Status.Any &&
      none { it.value === "releases" }
    ) throw MusicBrainzException(
      "status is not a valid parameter unless include contains releases"
    )
  }

  private fun List<Include>.ensureTypeValidity(type: Release.Type) {
    if (type !== Release.Type.Any &&
      none {
        when (it.value) {
          "releases" -> true
          "release-groups" -> true
          else -> false
        }
      }
    ) throw MusicBrainzException(
      "type is not a valid parameter unless include contains releases or release groups"
    )
  }

  override suspend fun findRecording(
    recording: RecordingName,
    artist: ArtistName?,
    album: AlbumName?,
    limit: Int?,
    offset: Int?
  ) = brainz {
    val query = buildString {
      append("recording:\"")
      append(recording.value)
      append("\"")
      if (artist !== null) {
        append(" AND artist:\"")
        append(artist.value)
        append("\"")
      }
      if (album !== null) {
        append(" AND release:\"")
        append(album.value)
        append("\"")
      }
    }
    musicBrainz.findRecording(query, limit, offset)
  }

  override suspend fun lookupRecording(
    mbid: RecordingMbid,
    include: List<Recording.Lookup>,
    type: Release.Type,
    status: Release.Status
  ): MusicBrainzResult<Recording> {
    return brainz {
      include.ensureTypeValidity(type)
      include.ensureStatusValidity(status)
      musicBrainz.lookupRecording(mbid.value, include.joinToInc(), type.value, status.value)
    }
  }

  override suspend fun <T : Any> brainz(
    block: suspend (brainz: MusicBrainz) -> Response<T>
  ): MusicBrainzResult<T> = withContext(dispatcher) {
    try {
      val response = block(musicBrainz)
      if (response.isSuccessful) {
        response.handleBody()
      } else {
        response.handleErrorBody()
      }
    } catch (e: Exception) {
      e.makeExceptional()
    }
  }

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

private val errorAdapter: JsonAdapter<BrainzError> = theMoshi.adapter(BrainzError::class.java)

/** @throws java.io.IOException if Retrofit throws */
private fun <T : Any> Response<T>.handleErrorBody() =
  errorString?.let { makeBrainzError(it) } ?: makeUnknown()

private val <T : Any> Response<T>.errorString: String? get() = errorBody()?.string()

private fun <T : Any> Response<T>.makeBrainzError(errorBody: String) =
  errorAdapter.fromJson(errorBody)?.let { makeError(it) } ?: makeUnknown()

private fun <T : Any> Response<T>.makeUnknown() = Unknown(RetrofitRawResponse(this))

private fun makeError(it: BrainzError) = MusicBrainzResult.Error(it)

private fun <T : Any> Response<T>.handleBody() = body()?.let { Success(it) } ?: makeUnknown()

private fun Exception.makeExceptional(): MusicBrainzResult.Exceptional {
  return MusicBrainzResult.Exceptional(
    when (this) {
      is MusicBrainzException -> this
      else -> {
        MusicBrainzException(message ?: "${this.javaClass} : calling Retrofit service", this)
      }
    }
  )
}

