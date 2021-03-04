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
import android.net.Uri
import com.ealva.brainzsvc.art.RemoteImage
import com.ealva.brainzsvc.common.AlbumName
import com.ealva.brainzsvc.common.ArtistName
import com.ealva.brainzsvc.common.RecordingName
import com.ealva.brainzsvc.net.toSecureUri
import com.ealva.brainzsvc.service.BrainzMessage.BrainzExceptionMessage
import com.ealva.brainzsvc.service.BrainzMessage.BrainzStatusMessage.BrainzErrorCodeMessage
import com.ealva.brainzsvc.service.BrainzMessage.BrainzStatusMessage.BrainzNullReturn
import com.ealva.ealvabrainz.brainz.MusicBrainz
import com.ealva.ealvabrainz.brainz.data.Artist
import com.ealva.ealvabrainz.brainz.data.ArtistList
import com.ealva.ealvabrainz.brainz.data.ArtistMbid
import com.ealva.ealvabrainz.brainz.data.BrowseReleaseGroupList
import com.ealva.ealvabrainz.brainz.data.BrowseReleaseList
import com.ealva.ealvabrainz.brainz.data.CoverArtRelease
import com.ealva.ealvabrainz.brainz.data.Include
import com.ealva.ealvabrainz.brainz.data.Label
import com.ealva.ealvabrainz.brainz.data.LabelMbid
import com.ealva.ealvabrainz.brainz.data.Recording
import com.ealva.ealvabrainz.brainz.data.RecordingList
import com.ealva.ealvabrainz.brainz.data.RecordingMbid
import com.ealva.ealvabrainz.brainz.data.Release
import com.ealva.ealvabrainz.brainz.data.ReleaseGroup
import com.ealva.ealvabrainz.brainz.data.ReleaseGroupList
import com.ealva.ealvabrainz.brainz.data.ReleaseGroupMbid
import com.ealva.ealvabrainz.brainz.data.ReleaseList
import com.ealva.ealvabrainz.brainz.data.ReleaseMbid
import com.ealva.ealvabrainz.brainz.data.join
import com.ealva.ealvabrainz.brainz.data.mbid
import com.ealva.ealvalog.e
import com.ealva.ealvalog.invoke
import com.ealva.ealvalog.lazyLogger
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.mapError
import com.github.michaelbull.result.runCatching
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import retrofit2.Response
import retrofit2.Retrofit
import java.io.File

/**
 * A BrainzCall is a suspending function which has a [MusicBrainz] receiver and returns a Retrofit
 * Response with a type parameter of the returned MusicBrainz entity.
 */
public typealias BrainzCall<T> = suspend MusicBrainz.() -> Response<T>

/**
 * BrainzResult<T> is a Result<T, BrainzMessage>, T being the Ok return type and a specialization
 * of BrainzMessage being the Err return type.
 */
public typealias BrainzResult<T> = Result<T, BrainzMessage>

/**
 * MusicBrainzService is a wrapper around a Retrofit MusicBrainz and CoverArt instance that
 * provides higher level functionality, including coordinating between the two to retrieve
 * artwork for Releases and Release Groups
 *
 * Most functions return a [BrainzResult] which is a Result<T, BrainzMessage>. If the result is [Ok]
 * it will contain an instance of T. If an error occurs an [Err] is returned with contains a
 * BrainzMessage.
 *
 * An [Err] will be a [BrainzMessage] of type:
 * * [BrainzExceptionMessage] if an underlying exception is thrown
 * * [BrainzNullReturn] subclass of BrainzStatusMessage, if the response is OK but null
 * * [BrainzErrorCodeMessage] subclass of BrainzStatusMessage, if the response is not successful
 */
public interface MusicBrainzService {
  /**
   * Find a [ReleaseList] based on [artist] and [album] (album = release), limiting
   * the results to [limit], starting at [offset]. The [limit] and [offset] facilitate paging
   * through results
   */
  public suspend fun findRelease(
    artist: ArtistName,
    album: AlbumName,
    limit: Int? = null,
    offset: Int? = null
  ): BrainzResult<ReleaseList>

  /**
   * Find the [Release] identified by [mbid]. Use [include], [type], and/or [status] to specify
   * information to be included in the Release.
   */
  public suspend fun lookupRelease(
    mbid: ReleaseMbid,
    include: List<Release.Lookup>? = null,
    type: List<Release.Type>? = null,
    status: List<Release.Status>? = null
  ): BrainzResult<Release>

  /**
   * Find [Release]s based on [artist] and [album] and convert the results to a flow
   * of [RemoteImage]
   */
  public fun getReleaseArt(
    artist: ArtistName,
    album: AlbumName,
    maxReleases: Int = DEFAULT_MAX_RELEASE_COUNT
  ): Flow<RemoteImage>

  /**
   * Find a [ReleaseGroupList] based on [artist] and [album] (album = release), limiting
   * the results to [limit], starting at [offset]. The [limit] and [offset] facilitate paging
   * through results
   */
  public suspend fun findReleaseGroup(
    artist: ArtistName,
    album: AlbumName,
    limit: Int? = null,
    offset: Int? = null
  ): BrainzResult<ReleaseGroupList>

  /**
   * Find the [ReleaseGroup] identified by [mbid]. Use [include], [type], and/or [status] to specify
   * information to be included in the ReleaseGroup.
   * @param mbid the MusicBrainzID to lookup
   * @param include list of [ReleaseGroup.Lookup] indicating what info linked to the entity is
   * returned
   * @param type limit linked entities to this [Release.Type]
   * @param status limit linked entities to this [Release.Status]
   */
  public suspend fun lookupReleaseGroup(
    mbid: ReleaseGroupMbid,
    include: List<ReleaseGroup.Lookup>? = null,
    type: List<Release.Type>? = null,
    status: List<Release.Status>? = null
  ): BrainzResult<ReleaseGroup>

  public suspend fun browseReleaseGroups(
    artistMbid: ArtistMbid,
    limit: Int? = null,
    offset: Int? = null,
    include: List<ReleaseGroup.Browse>? = null,
    type: List<Release.Type>? = null
  ): BrainzResult<BrowseReleaseGroupList>

  public suspend fun browseReleases(
    artistMbid: ArtistMbid,
    limit: Int? = null,
    offset: Int? = null,
    include: List<Release.Browse>? = null,
    type: List<Release.Type>? = null,
    status: List<Release.Status>? = null
  ): BrainzResult<BrowseReleaseList>

  /**
   * Find [ReleaseGroup]s based on [artist] and [album] and convert the results to a flow
   * of [RemoteImage]
   */
  public fun getReleaseGroupArt(
    artist: ArtistName,
    album: AlbumName,
    maxReleases: Int = DEFAULT_MAX_RELEASE_COUNT
  ): Flow<RemoteImage>

  /**
   * Find an [ArtistList] based on [artist], limiting the results to [limit], starting
   * at [offset]. The [limit] and [offset] facilitate paging through results
   */
  public suspend fun findArtist(
    artist: ArtistName,
    limit: Int? = null,
    offset: Int? = null
  ): BrainzResult<ArtistList>

  /**
   * Find the [Artist] identified by [ArtistMbid]. Use [include] to specify information to be
   * included in the Release.
   * @param mbid the MusicBrainzID to lookup
   * @param include list of [Artist.Lookup] indicating what info linked to the entity is returned
   * @param type limit linked entities to this [Release.Type]
   * @param status limit linked entities to this [Release.Status]
   */
  public suspend fun lookupArtist(
    mbid: ArtistMbid,
    include: List<Artist.Lookup>? = null,
    type: List<Release.Type>? = null,
    status: List<Release.Status>? = null
  ): BrainzResult<Artist>

  /**
   * Find an [RecordingList] based on [recording], [artist], and [album], limiting the results
   * to [limit], starting at [offset]. The [limit] and [offset] facilitate paging through results
   *
   * @param recording the name of the recording or track
   * @param artist the artist name (optional)
   * @param album the album name (optional)
   * @param limit how many entries should be returned
   * @param offset offset into the returned list, used for paging results
   */
  public suspend fun findRecording(
    recording: RecordingName,
    artist: ArtistName? = null,
    album: AlbumName? = null,
    limit: Int? = null,
    offset: Int? = null
  ): BrainzResult<RecordingList>

  /**
   * Find the [Recording] identified by [RecordingMbid]. Use [include] to specify information to be
   * included in the result.
   * @param mbid the MusicBrainzID to lookup
   * @param include list of [Recording.Lookup] indicating what info linked to the entity is returned
   * @param type limit linked entities to this [Release.Type]
   * @param status limit linked entities to this [Release.Status]
   * @return the [Recording] associated with [mbid] or null
   * @throws BrainzException if Retrofit throws or parameters are invalid
   */
  public suspend fun lookupRecording(
    mbid: RecordingMbid,
    include: List<Recording.Lookup>? = null,
    type: List<Release.Type>? = null,
    status: List<Release.Status>? = null
  ): BrainzResult<Recording>

  /**
   * Lookup Label identified by the [LabelMbid] and use [include] to specify information to be
   * included in the result.
   *
   * @param mbid the MusicBrainID (MBID) to lookup
   * @param include list of [Label.Lookup] specifying how much info from linked entities to include
   */
  public suspend fun lookupLabel(
    mbid: LabelMbid,
    include: List<Label.Lookup>? = null
  ): BrainzResult<Label>

  public suspend fun getReleaseGroupArtwork(mbid: ReleaseGroupMbid): Uri

  public suspend fun browseLabelReleases(
    mbid: LabelMbid,
    limit: Int,
    offset: Int,
    include: List<Release.Lookup>? = null,
    type: List<Release.Type>? = null,
    status: List<Release.Status>? = null
  ): BrainzResult<BrowseReleaseList>

  /**
   * A main-safe function that calls the [block] function, with [MusicBrainz] as a receiver,
   * dispatched by the contained [CoroutineDispatcher] (typically [Dispatchers.IO] when not under
   * test)
   *
   * Usually [block] is a lambda which makes a direct call to the MusicBrainz Retrofit client. The
   * [block] is responsible for building the necessary String parameters, "query" in case of a
   * find call and "inc" include if doing a lookup. Use the Subquery and Misc defined
   * in the various entity objects for doing a lookup and use SearchField to build queries
   *
   * @return an [Ok] with value of type [T] or, if response is not successful, an [Err]. An [Err]
   * will be a [BrainzMessage] of type:
   * * [BrainzExceptionMessage] if an underlying exception is thrown
   * * [BrainzNullReturn] subclass of BrainzStatusMessage, if the response is OK but null
   * * [BrainzErrorCodeMessage] subclass of BrainzStatusMessage, if the response is not successful
   */
  public suspend fun <T : Any> brainz(block: BrainzCall<T>): BrainzResult<T>

  public companion object {
    public const val DEFAULT_MAX_RELEASE_COUNT: Int = 10

    public operator fun invoke(
      ctx: Context,
      appName: String,
      appVersion: String,
      contact: String,
      coverArt: CoverArtService,
      dispatcher: CoroutineDispatcher = Dispatchers.IO
    ): MusicBrainzService =
      make(
        buildMusicBrainz(appName, appVersion, contact, File(ctx.cacheDir, CACHE_DIR)),
        coverArt,
        dispatcher
      )

    /** Internal for test, provides for injecting fakes/mocks/etc and test dispatcher. */
    internal fun make(
      brainz: MusicBrainz,
      coverArt: CoverArtService,
      dispatcher: CoroutineDispatcher
    ): MusicBrainzService {
      return MusicBrainzServiceImpl(brainz, coverArt, dispatcher)
    }

//    const val website = "https://musicbrainz.org/"
//    const val registerUrl = """${website}register"""
//    const val forgotPasswordUrl = """${website}lost-password"""
//    const val donateUrl = "http://metabrainz.org/donate"
  }
}

private val LOG by lazyLogger(MusicBrainzService::class)

// private const val MUSIC_BRAINZ_API_URL = "http://musicbrainz.org/ws/2/"
private const val MUSIC_BRAINZ_API_SECURE_URL = "https://musicbrainz.org/ws/2/"
private val SERVICE_NAME = MusicBrainzServiceImpl::class.java.simpleName
private const val CACHE_DIR = "MusicBrainz"

internal class MusicBrainzServiceImpl(
  private val musicBrainz: MusicBrainz,
  private val coverArtService: CoverArtService,
  private val dispatcher: CoroutineDispatcher
) : MusicBrainzService {
  private val resourceFetcher: ResourceFetcher
    get() = coverArtService.resourceFetcher

  override suspend fun findRelease(
    artist: ArtistName,
    album: AlbumName,
    limit: Int?,
    offset: Int?
  ): BrainzResult<ReleaseList> = brainz {
    findRelease("""artist:"${artist.value}" AND release:"${album.value}"""", limit, offset)
  }

  override suspend fun lookupRelease(
    mbid: ReleaseMbid,
    include: List<Release.Lookup>?,
    type: List<Release.Type>?,
    status: List<Release.Status>?
  ): BrainzResult<Release> = brainz {
    lookupRelease(
      mbid.value,
      include?.join(),
      type?.ensureValidType(include)?.join(),
      status?.ensureValidStatus(include)?.join()
    )
  }

  @OptIn(ExperimentalCoroutinesApi::class)
  override fun getReleaseArt(
    artist: ArtistName,
    album: AlbumName,
    maxReleases: Int
  ): Flow<RemoteImage> = findReleases(artist, album, maxReleases).transform(coverArtService)

  override suspend fun findReleaseGroup(
    artist: ArtistName,
    album: AlbumName,
    limit: Int?,
    offset: Int?
  ): BrainzResult<ReleaseGroupList> = brainz {
    findReleaseGroup("""artist:"${artist.value}" AND release:"${album.value}"""", limit, offset)
  }

  override suspend fun lookupReleaseGroup(
    mbid: ReleaseGroupMbid,
    include: List<ReleaseGroup.Lookup>?,
    type: List<Release.Type>?,
    status: List<Release.Status>?
  ): BrainzResult<ReleaseGroup> = brainz {
    lookupReleaseGroup(
      mbid.value,
      include?.join(),
      type?.ensureValidType(include)?.join(),
      status?.ensureValidStatus(include)?.join()
    )
  }

  override suspend fun browseReleaseGroups(
    artistMbid: ArtistMbid,
    limit: Int?,
    offset: Int?,
    include: List<ReleaseGroup.Browse>?,
    type: List<Release.Type>?,
  ): BrainzResult<BrowseReleaseGroupList> = brainz {
    browseArtistReleaseGroups(
      artistMbid.value,
      limit,
      offset,
      include?.join(),
      type?.ensureValidType(include)?.join()
    )
  }

  override suspend fun browseReleases(
    artistMbid: ArtistMbid,
    limit: Int?,
    offset: Int?,
    include: List<Release.Browse>?,
    type: List<Release.Type>?,
    status: List<Release.Status>?
  ): BrainzResult<BrowseReleaseList> = brainz {
    browseReleases(
      artistId = artistMbid.value,
      limit = limit,
      offset = offset,
      include = include?.join(),
      type = type?.ensureValidType(include)?.join(),
      status = status?.join()
    )
  }

//  private suspend fun FlowCollector<List<Release>>.emitReleaseList(
//    currentOffset: Int,
//    resultList: List<Release>,
//    resultOffset: Int,
//    resultTotalCount: Int,
//    collector: ArrayList<Release>,
//    message: () -> String = { "" }
//  ): Int {
//    val newOffset = handleBrowseList(
//      currentOffset,
//      resultList,
//      resultOffset,
//      resultTotalCount,
//      collector,
//      message
//    )
//    emit(collector.toList())
//    return newOffset
//  }

  @OptIn(ExperimentalCoroutinesApi::class)
  override fun getReleaseGroupArt(
    artist: ArtistName,
    album: AlbumName,
    maxReleases: Int
  ) = findReleaseGroups(artist, album, maxReleases)
    .transform(coverArtService)
    .flowOn(dispatcher)

  override suspend fun findArtist(
    artist: ArtistName,
    limit: Int?,
    offset: Int?
  ): BrainzResult<ArtistList> = brainz {
    musicBrainz.findArtist("""artist:"${artist.value}"""", limit, offset)
  }

  override suspend fun lookupArtist(
    mbid: ArtistMbid,
    include: List<Artist.Lookup>?,
    type: List<Release.Type>?,
    status: List<Release.Status>?
  ): BrainzResult<Artist> = brainz {
    lookupArtist(
      mbid.value,
      include?.join(),
      type?.ensureValidType(include)?.join(),
      status?.ensureValidStatus(include)?.join()
    )
  }

  override suspend fun findRecording(
    recording: RecordingName,
    artist: ArtistName?,
    album: AlbumName?,
    limit: Int?,
    offset: Int?
  ): BrainzResult<RecordingList> = brainz {
    val query = buildString {
      append("recording:\"")
      append(recording.value)
      append("\"")
      if (artist != null) {
        append(" AND artist:\"")
        append(artist.value)
        append("\"")
      }
      if (album != null) {
        append(" AND release:\"")
        append(album.value)
        append("\"")
      }
    }
    findRecording(query, limit, offset)
  }

  override suspend fun lookupRecording(
    mbid: RecordingMbid,
    include: List<Recording.Lookup>?,
    type: List<Release.Type>?,
    status: List<Release.Status>?
  ): BrainzResult<Recording> = brainz {
    lookupRecording(
      mbid.value,
      include?.join(),
      type?.ensureValidType(include)?.join(),
      status?.ensureValidStatus(include)?.join()
    )
  }

  override suspend fun lookupLabel(
    mbid: LabelMbid,
    include: List<Label.Lookup>?
  ): BrainzResult<Label> = brainz {
    lookupLabel(mbid.value, include?.join())
  }

  override suspend fun <T : Any> brainz(
    block: BrainzCall<T>
  ): Result<T, BrainzMessage> = withContext(dispatcher) {
    runCatching { musicBrainz.block() }
      .mapError { ex -> BrainzExceptionMessage(ex) }
      .mapResponse()
  }

  override suspend fun getReleaseGroupArtwork(mbid: ReleaseGroupMbid): Uri =
    when (val result = coverArtService.getReleaseGroupArt(mbid)) {
      is Ok ->
        result.value
          .releaseImageSequence()
          .filterNot { it.isBlank() }
          .firstOrNull()
          .toSecureUri()
      is Err -> {
        println("Error: ${result.getErrorString(resourceFetcher)}")
        LOG.e { it("Error: %s", result.getErrorString(resourceFetcher)) }
        Uri.EMPTY
      }
    }

  override suspend fun browseLabelReleases(
    mbid: LabelMbid,
    limit: Int,
    offset: Int,
    include: List<Release.Lookup>?,
    type: List<Release.Type>?,
    status: List<Release.Status>?
  ): BrainzResult<BrowseReleaseList> {
    return brainz {
      browseReleases(
        labelId = mbid.value,
        limit = limit,
        offset = offset,
        include = include?.join(),
        type = type?.ensureValidType(include)?.join(),
        status = status?.ensureValidStatus(include)?.join()
      )
    }
  }

  private fun CoverArtRelease?.releaseImageSequence(): Sequence<String> {
    return if (this == null) emptySequence() else sequence {
      images.forEach { coverArtImage ->
        coverArtImage.thumbnails.run {
          yield(small)
          yield(size250)
          yield(size500)
          yield(large)
          yield(size1200)
        }
        yield(coverArtImage.image)
      }
    }.distinct()
  }

  @OptIn(ExperimentalCoroutinesApi::class)
  private fun findReleases(artist: ArtistName, album: AlbumName, maxReleases: Int) = flow {
    musicBrainz.findRelease(
      """artist:"${artist.value}" AND release:"${album.value}"""",
      maxReleases
    )
      .list()
      .sortedByDescending { it.score }
      .forEach { emit(it.mbid) }
  }.flowOn(dispatcher)

  @OptIn(ExperimentalCoroutinesApi::class)
  private fun findReleaseGroups(artist: ArtistName, album: AlbumName, maxReleases: Int) = flow {
    musicBrainz.findReleaseGroup(
      """artist:"${artist.value}" AND release:"${album.value}"""",
      maxReleases
    )
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
    emptyList()
  }
}

@JvmName(name = "getReleaseGroupList")
private fun Response<ReleaseGroupList>.list(): List<ReleaseGroup> {
  return if (isSuccessful) body()?.releaseGroups.orEmpty() else {
    emptyList()
  }
}

private fun List<Release.Status>.ensureValidStatus(incList: List<Include>?) = apply {
  if (isNotEmpty() && incList.doesNotContainReleases()) throw BrainzStatusInvalidException()
}

private fun List<Release.Type>.ensureValidType(incList: List<Include>?) = apply {
  if (isNotEmpty() && incList.doesNotContainReleasesOrGroups()) throw BrainzTypeInvalidException()
}

@Suppress("NOTHING_TO_INLINE") // only used once
private inline fun List<Include>?.doesNotContainReleases(): Boolean =
  this == null || none { it.value == "releases" }

@Suppress("NOTHING_TO_INLINE") // only used once
private inline fun List<Include>?.doesNotContainReleasesOrGroups(): Boolean =
  this == null || none { it.value == "releases" || it.value == "release-groups" }

//  private fun <T> handleBrowseList(
//    currentOffset: Int,
//    resultList: List<T>,
//    resultOffset: Int,
//    resultTotalCount: Int,
//    collector: ArrayList<T>,
//    message: () -> String = { "" }
//  ): Int {
//    collector.ensureCapacity(resultTotalCount)
//    val resultCount = resultList.size
//    offsetSanityCheck(resultOffset, currentOffset, message)
//    collector.addAll(resultList)
//    return if (collector.size < resultTotalCount) resultOffset + resultCount else -1
//  }
//
//  private inline fun offsetSanityCheck(
//    offset: Int,
//    resultOffset: Int,
//    message: () -> String = { "" }
//  ) {
//    if (resultOffset != offset)
//      Timber.e("Offset mismatch, requested:%d result:%d %s", offset, resultOffset, message())
//  }
