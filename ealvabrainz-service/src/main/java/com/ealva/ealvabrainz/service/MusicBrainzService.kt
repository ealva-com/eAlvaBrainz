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
import com.ealva.ealvabrainz.art.RemoteImage
import com.ealva.ealvabrainz.brainz.MusicBrainz
import com.ealva.ealvabrainz.brainz.data.Artist
import com.ealva.ealvabrainz.brainz.data.ArtistList
import com.ealva.ealvabrainz.brainz.data.ArtistMbid
import com.ealva.ealvabrainz.brainz.data.BrainzError
import com.ealva.ealvabrainz.brainz.data.BrowseReleaseGroupList
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
import com.ealva.ealvabrainz.common.ensureExhaustive
import com.ealva.ealvabrainz.net.RetrofitRawResponse
import com.ealva.ealvabrainz.service.MusicBrainzResult.Success
import com.ealva.ealvabrainz.service.MusicBrainzResult.Unsuccessful
import com.ealva.ealvabrainz.service.MusicBrainzResult.Unsuccessful.ErrorResult
import com.ealva.ealvabrainz.service.MusicBrainzResult.Unsuccessful.Exceptional
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
import java.io.File

/**
 * Result of various calls to [MusicBrainzService]
 */
sealed class MusicBrainzResult<out T : Any> {
  /** Call was successful and contains the [value] */
  data class Success<T : Any>(val value: T) : MusicBrainzResult<T>()

  /** Group non-success subclasses under this sealed class */
  sealed class Unsuccessful : MusicBrainzResult<Nothing>() {

    /** Server returned an error and was converted to the common error body response */
    data class ErrorResult(val error: BrainzError) : Unsuccessful()

    /** An [exception] was thrown */
    data class Exceptional(val exception: MusicBrainzException) : Unsuccessful()

    /**
     * If using a [MusicBrainzResult.Unsuccessful] in a LiveData, or some other data holder, use
     * this as a "No Error" marker instead of null
     */
    object None : Unsuccessful()
  }

}

/**
 * A BrainzCall is a suspending function which accepts a [MusicBrainz] instance and returns
 * a Retrofit Response with a type parameter of the returned MusicBrainz entity.
 */
typealias BrainzCall<T> = suspend (brainz: MusicBrainz) -> Response<T>

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
    type: List<Release.Type> = emptyList(),
    status: List<Release.Status> = emptyList()
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
    type: List<Release.Type> = emptyList(),
    status: List<Release.Status> = emptyList()
  ): MusicBrainzResult<ReleaseGroup>

  suspend fun getArtistReleaseGroups(
    artistMbid: ArtistMbid,
    include: List<ReleaseGroup.Browse> = emptyList(),
    type: List<Release.Type> = emptyList()
  ): MusicBrainzResult<List<ReleaseGroup>>

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
    type: List<Release.Type> = emptyList(),
    status: List<Release.Status> = emptyList()
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
    type: List<Release.Type> = emptyList(),
    status: List<Release.Status> = emptyList()
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
   * @return a [MusicBrainzResult.Success] or, if response is not successful, a
   * [MusicBrainzResult.Unsuccessful.ErrorResult]. [MusicBrainzResult.Unsuccessful.Exceptional] is
   * returned if an underlying exception is thrown or if the response is not successful and the
   * error body could not be decoded. If unable to decode the error body the actual exception
   * is [MusicBrainzUnknownError] which contains a [RetrofitRawResponse].
   */
  suspend fun <T : Any> brainz(block: BrainzCall<T>): MusicBrainzResult<T>

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
      make(
        buildMusicBrainz(appName, appVersion, contact, File(ctx.cacheDir, CACHE_DIR)),
        coverArt,
        Dispatchers.IO
      )

    internal fun make(
      brainz: MusicBrainz,
      coverArt: CoverArtService,
      dispatcher: CoroutineDispatcher
    ): MusicBrainzService {
      return MusicBrainzServiceImpl(brainz, coverArt, dispatcher)
    }

    const val website = "https://musicbrainz.org/"
    const val registerUrl = """${website}register"""
    const val forgotPasswordUrl = """${website}lost-password"""
    const val donateUrl = "http://metabrainz.org/donate"
  }
}

//private const val MUSIC_BRAINZ_API_URL = "http://musicbrainz.org/ws/2/"
private const val MUSIC_BRAINZ_API_SECURE_URL = "https://musicbrainz.org/ws/2/"

private val SERVICE_NAME = MusicBrainzServiceImpl::class.java.simpleName
private const val CACHE_DIR = "MusicBrainz"

internal class MusicBrainzServiceImpl(
  private val musicBrainz: MusicBrainz,
  private val coverArtService: CoverArtService,
  private val dispatcher: CoroutineDispatcher
) : MusicBrainzService {
  override suspend fun findRelease(
    artist: ArtistName,
    album: AlbumName,
    limit: Int?,
    offset: Int?
  ) = brainz {
    val query = """artist:"${artist.value}" AND release:"${album.value}""""
    musicBrainz.findRelease(query, limit, offset)
  }

  override suspend fun lookupRelease(
    mbid: ReleaseMbid,
    include: List<Release.Lookup>,
    type: List<Release.Type>,
    status: List<Release.Status>
  ) = brainz {
    musicBrainz.lookupRelease(mbid.value, include.joinToInc(), type.joinToInc(), status.joinToInc())
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
    type: List<Release.Type>,
    status: List<Release.Status>
  ) = brainz {
    include.ensureTypeValidity(type)
    musicBrainz.lookupReleaseGroup(
      mbid.value,
      include.joinToInc(),
      type.joinToInc(),
      status.joinToInc()
    )
  }

  override suspend fun getArtistReleaseGroups(
    artistMbid: ArtistMbid,
    include: List<ReleaseGroup.Browse>,
    type: List<Release.Type>
  ): MusicBrainzResult<List<ReleaseGroup>> {
    val list = ArrayList<ReleaseGroup>(200)
    var offset = 0
    val brainzInc = include.joinToInc()
    val brainzType = type.joinToInc()
    do {
      val result = brainz {
        musicBrainz.browseArtistReleaseGroups(
          artistMbid.value,
          100,
          offset,
          brainzInc,
          brainzType
        )
      }
      when (result) {
        is Success<BrowseReleaseGroupList> -> {
          val resultList = result.value
          val totalCount = resultList.releaseGroupCount
          val resultCount = resultList.releaseGroups.size
          offsetSanityCheck(resultList, offset)
          list.addAll(resultList.releaseGroups)
          offset = if (list.size < totalCount) offset + resultCount else -1
        }
        is ErrorResult -> return ErrorResult(result.error)
        is Exceptional -> return Exceptional(result.exception)
        is Unsuccessful.None -> {}
      }.ensureExhaustive
    } while (offset > 0)
    return Success(list)
  }

  private fun offsetSanityCheck(resultList: BrowseReleaseGroupList, offset: Int) {
    if (resultList.releaseGroupOffset != offset)
      Timber.e("Offset mismatch, requested:%d result:%d", offset, resultList.releaseGroupOffset)
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
    type: List<Release.Type>,
    status: List<Release.Status>
  ) = brainz {
    include.ensureTypeValidity(type)
    include.ensureStatusValidity(status)
    musicBrainz.lookupArtist(mbid.value, include.joinToInc(), type.joinToInc(), status.joinToInc())
  }

  private fun List<Include>.ensureStatusValidity(status: List<Release.Status>) {
    if (status.isNotEmpty() && none { it.value === "releases" })
      throw MusicBrainzException("status is not a valid parameter unless include contains releases")
  }

  private fun List<Include>.ensureTypeValidity(type: List<Release.Type>) {
    if (type.isNotEmpty() &&
      none {
        when (it.value) {
          "releases" -> true
          "release-groups" -> true
          else -> false
        }.ensureExhaustive
      }
    ) throw MusicBrainzException(
      "type is not a valid parameter unless include contains releases or release-groups"
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
    type: List<Release.Type>,
    status: List<Release.Status>
  ) = brainz {
    include.ensureTypeValidity(type)
    include.ensureStatusValidity(status)
    musicBrainz.lookupRecording(
      mbid.value,
      include.joinToInc(),
      type.joinToInc(),
      status.joinToInc()
    )
  }

  override suspend fun <T : Any> brainz(block: BrainzCall<T>) = withContext(dispatcher) {
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

private fun buildMusicBrainz(
  appName: String,
  appVersion: String,
  emailContact: String,
  cacheDirectory: File
) = Retrofit.Builder()
  .client(makeOkHttpClient(SERVICE_NAME, appName, appVersion, emailContact, cacheDirectory))
  .baseUrl(MUSIC_BRAINZ_API_SECURE_URL)
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
private fun <T : Any> Response<T>.handleErrorBody(): MusicBrainzResult<Nothing> {
  return errorString?.let { makeBrainzError(it) } ?: makeUnknown()
}

private val <T : Any> Response<T>.errorString: String? get() = errorBody()?.string()

private fun <T : Any> Response<T>.makeBrainzError(errorBody: String) = try {
  errorAdapter.fromJson(errorBody)?.let { makeError(it) } ?: makeUnknown()
} catch (e: Exception) {
  makeUnknown()
}

private fun <T : Any> Response<T>.makeUnknown() =
  MusicBrainzUnknownError(RetrofitRawResponse(this)).makeExceptional()

private fun makeError(it: BrainzError) = ErrorResult(it)

private fun <T : Any> Response<T>.handleBody() = body()?.let { Success(it) } ?: makeUnknown()

private fun Exception.makeExceptional(): Exceptional {
  return Exceptional(
    when (this) {
      is MusicBrainzException -> this
      else -> {
        MusicBrainzException(message ?: "${this.javaClass} : calling Retrofit service", this)
      }
    }.ensureExhaustive
  )
}

