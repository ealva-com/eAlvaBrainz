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
import com.ealva.brainzsvc.common.AlbumTitle
import com.ealva.brainzsvc.common.ArtistName
import com.ealva.brainzsvc.common.RecordingName
import com.ealva.brainzsvc.net.toSecureUri
import com.ealva.brainzsvc.service.BrainzMessage.BrainzExceptionMessage
import com.ealva.brainzsvc.service.BrainzMessage.BrainzStatusMessage.BrainzErrorCodeMessage
import com.ealva.brainzsvc.service.BrainzMessage.BrainzStatusMessage.BrainzNullReturn
import com.ealva.brainzsvc.service.browse.ReleaseBrowse
import com.ealva.brainzsvc.service.browse.ReleaseBrowseOp
import com.ealva.brainzsvc.service.lookup.AreaLookup
import com.ealva.brainzsvc.service.lookup.AreaLookupOp
import com.ealva.brainzsvc.service.lookup.ArtistLookup
import com.ealva.brainzsvc.service.lookup.ArtistLookupOp
import com.ealva.brainzsvc.service.lookup.LabelLookup
import com.ealva.brainzsvc.service.lookup.LabelLookupOp
import com.ealva.brainzsvc.service.lookup.RecordingLookup
import com.ealva.brainzsvc.service.lookup.RecordingLookupOp
import com.ealva.brainzsvc.service.lookup.ReleaseGroupLookup
import com.ealva.brainzsvc.service.lookup.ReleaseGroupLookupOp
import com.ealva.brainzsvc.service.lookup.ReleaseLookup
import com.ealva.brainzsvc.service.lookup.ReleaseLookupOp
import com.ealva.brainzsvc.service.search.ArtistSearch
import com.ealva.brainzsvc.service.search.ReleaseGroupSearch
import com.ealva.brainzsvc.service.search.ReleaseSearch
import com.ealva.ealvabrainz.brainz.MusicBrainz
import com.ealva.ealvabrainz.brainz.data.Area
import com.ealva.ealvabrainz.brainz.data.AreaMbid
import com.ealva.ealvabrainz.brainz.data.Artist
import com.ealva.ealvabrainz.brainz.data.ArtistList
import com.ealva.ealvabrainz.brainz.data.ArtistMbid
import com.ealva.ealvabrainz.brainz.data.BrowseReleaseList
import com.ealva.ealvabrainz.brainz.data.CoverArtRelease
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
import com.ealva.ealvabrainz.lucene.BrainzMarker
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
@BrainzMarker
public interface MusicBrainzService {
  /**
   * Lookup an [Area] with [areaMbid] and specify other information to be included via the
   * optional [lookup] lambda with receiver [AreaLookup]
   */
  public suspend fun lookupArea(
    areaMbid: AreaMbid,
    lookup: AreaLookup.() -> Unit = {}
  ): BrainzResult<Area>

  /**
   * Find the [Artist] with the [artistMbid] ID. Provide an optional lambda with an [ArtistLookup]
   * receiver to specify if any other information should be included.
   */
  public suspend fun lookupArtist(
    artistMbid: ArtistMbid,
    lookup: ArtistLookup.() -> Unit = {}
  ): BrainzResult<Artist>

  /**
   * Find the [Label] with the [labelMbid] ID. Provide an optional lambda with a
   * [LabelLookup] receiver to specify if any other information should be included.
   */
  public suspend fun lookupLabel(
    labelMbid: LabelMbid,
    lookup: LabelLookup.() -> Unit
  ): BrainzResult<Label>

  /**
   * Find the [Recording] with the [recordingMbid] ID. Provide an optional lambda with a
   * [RecordingLookup] receiver to specify if any other information should be included.
   */
  public suspend fun lookupRecording(
    recordingMbid: RecordingMbid,
    lookup: RecordingLookup.() -> Unit
  ): BrainzResult<Recording>

  /**
   * Find the [Release] with the [releaseMbid] ID. Provide an optional lambda with a
   * [ReleaseLookup] receiver to specify if any other information should be included.
   */
  public suspend fun lookupRelease(
    releaseMbid: ReleaseMbid,
    lookup: ReleaseLookup.() -> Unit = {}
  ): BrainzResult<Release>

  /**
   * Find the [ReleaseGroup] with the [releaseGroupMbid] ID. Provide an optional lambda with a
   * [ReleaseGroupLookup] receiver to specify if any other information should be included.
   */
  public suspend fun lookupReleaseGroup(
    releaseGroupMbid: ReleaseGroupMbid,
    lookup: ReleaseGroupLookup.() -> Unit = {}
  ): BrainzResult<ReleaseGroup>

  public suspend fun findArtist(
    limit: Int? = null,
    offset: Int? = null,
    search: ArtistSearch.() -> Unit
  ): BrainzResult<ArtistList>

  public suspend fun findReleaseGroup(
    limit: Int? = null,
    offset: Int? = null,
    search: ReleaseGroupSearch.() -> Unit
  ): BrainzResult<ReleaseGroupList>

  public suspend fun findRelease(
    limit: Int? = null,
    offset: Int? = null,
    search: ReleaseSearch.() -> Unit
  ): BrainzResult<ReleaseList>

  public suspend fun browseReleases(
    browseOn: ReleaseBrowse.BrowseOn,
    limit: Int? = null,
    offset: Int? = null,
    browse: ReleaseBrowse.() -> Unit
  ): BrainzResult<BrowseReleaseList>

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
    album: AlbumTitle? = null,
    limit: Int? = null,
    offset: Int? = null
  ): BrainzResult<RecordingList>

  public suspend fun getReleaseGroupArtwork(mbid: ReleaseGroupMbid): Uri

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

  override suspend fun lookupArea(
    areaMbid: AreaMbid,
    lookup: AreaLookup.() -> Unit
  ): BrainzResult<Area> = brainz {
    AreaLookupOp().apply(lookup).execute(areaMbid, this)
  }

  override suspend fun lookupArtist(
    artistMbid: ArtistMbid,
    lookup: ArtistLookup.() -> Unit
  ): BrainzResult<Artist> = brainz {
    ArtistLookupOp().apply(lookup).execute(artistMbid, this)
  }

  override suspend fun lookupLabel(
    labelMbid: LabelMbid,
    lookup: LabelLookup.() -> Unit
  ): BrainzResult<Label> = brainz {
    LabelLookupOp().apply(lookup).execute(labelMbid, this)
  }

  override suspend fun lookupRecording(
    recordingMbid: RecordingMbid,
    lookup: RecordingLookup.() -> Unit
  ): BrainzResult<Recording> = brainz {
    RecordingLookupOp().apply(lookup).execute(recordingMbid, this)
  }

  override suspend fun lookupRelease(
    releaseMbid: ReleaseMbid,
    lookup: ReleaseLookup.() -> Unit
  ): BrainzResult<Release> = brainz {
    ReleaseLookupOp().apply(lookup).execute(releaseMbid, this)
  }

  override suspend fun lookupReleaseGroup(
    releaseGroupMbid: ReleaseGroupMbid,
    lookup: ReleaseGroupLookup.() -> Unit
  ): BrainzResult<ReleaseGroup> = brainz {
    ReleaseGroupLookupOp().apply(lookup).execute(releaseGroupMbid, this)
  }

  override suspend fun browseReleases(
    browseOn: ReleaseBrowse.BrowseOn,
    limit: Int?,
    offset: Int?,
    browse: ReleaseBrowse.() -> Unit
  ): BrainzResult<BrowseReleaseList> = brainz {
    ReleaseBrowseOp(browseOn).apply(browse).execute(this, limit, offset)
  }

  override suspend fun findArtist(
    limit: Int?,
    offset: Int?,
    search: ArtistSearch.() -> Unit
  ): BrainzResult<ArtistList> = brainz {
    musicBrainz.findArtist(ArtistSearch().apply(search).toString(), limit, offset)
  }

  override suspend fun findReleaseGroup(
    limit: Int?,
    offset: Int?,
    search: ReleaseGroupSearch.() -> Unit
  ): BrainzResult<ReleaseGroupList> = brainz {
    musicBrainz.findReleaseGroup(ReleaseGroupSearch().apply(search).toString(), limit, offset)
  }

  override suspend fun findRelease(
    limit: Int?,
    offset: Int?,
    search: ReleaseSearch.() -> Unit
  ): BrainzResult<ReleaseList> = brainz {
    musicBrainz.findRelease(ReleaseSearch().apply(search).toString(), limit, offset)
  }

  override suspend fun findRecording(
    recording: RecordingName,
    artist: ArtistName?,
    album: AlbumTitle?,
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
