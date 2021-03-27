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

package com.ealva.brainzsvc.service

import android.content.Context
import android.net.Uri
import com.ealva.ealvabrainz.common.TocParam
import com.ealva.brainzsvc.net.toSecureUri
import com.ealva.ealvabrainz.browse.AreaBrowse
import com.ealva.ealvabrainz.browse.ArtistBrowse
import com.ealva.ealvabrainz.browse.CollectionBrowse
import com.ealva.ealvabrainz.browse.EventBrowse
import com.ealva.ealvabrainz.browse.InstrumentBrowse
import com.ealva.ealvabrainz.browse.LabelBrowse
import com.ealva.ealvabrainz.browse.PlaceBrowse
import com.ealva.ealvabrainz.browse.RecordingBrowse
import com.ealva.ealvabrainz.browse.ReleaseBrowse
import com.ealva.ealvabrainz.browse.ReleaseGroupBrowse
import com.ealva.ealvabrainz.browse.SeriesBrowse
import com.ealva.ealvabrainz.browse.WorkBrowse
import com.ealva.ealvabrainz.lookup.AreaLookup
import com.ealva.ealvabrainz.lookup.ArtistLookup
import com.ealva.ealvabrainz.lookup.CollectionLookup
import com.ealva.ealvabrainz.lookup.EventLookup
import com.ealva.ealvabrainz.lookup.GenreLookup
import com.ealva.ealvabrainz.lookup.InstrumentLookup
import com.ealva.ealvabrainz.lookup.IsrcLookup
import com.ealva.ealvabrainz.lookup.IswcLookup
import com.ealva.ealvabrainz.lookup.LabelLookup
import com.ealva.ealvabrainz.lookup.PlaceLookup
import com.ealva.ealvabrainz.lookup.RecordingLookup
import com.ealva.ealvabrainz.lookup.ReleaseGroupLookup
import com.ealva.ealvabrainz.lookup.ReleaseLookup
import com.ealva.ealvabrainz.lookup.SeriesLookup
import com.ealva.ealvabrainz.lookup.UrlLookup
import com.ealva.ealvabrainz.lookup.WorkLookup
import com.ealva.ealvabrainz.search.AnnotationSearch
import com.ealva.ealvabrainz.search.ArtistSearch
import com.ealva.ealvabrainz.search.CdStubSearch
import com.ealva.ealvabrainz.search.LabelSearch
import com.ealva.ealvabrainz.search.RecordingSearch
import com.ealva.ealvabrainz.search.ReleaseGroupSearch
import com.ealva.ealvabrainz.search.ReleaseSearch
import com.ealva.ealvabrainz.search.TagSearch
import com.ealva.ealvabrainz.search.WorkSearch
import com.ealva.ealvabrainz.brainz.MusicBrainz
import com.ealva.ealvabrainz.brainz.data.AnnotationList
import com.ealva.ealvabrainz.brainz.data.Area
import com.ealva.ealvabrainz.brainz.data.Artist
import com.ealva.ealvabrainz.brainz.data.ArtistList
import com.ealva.ealvabrainz.brainz.data.BrowseAreaList
import com.ealva.ealvabrainz.brainz.data.BrowseArtistList
import com.ealva.ealvabrainz.brainz.data.BrowseCollectionList
import com.ealva.ealvabrainz.brainz.data.BrowseEventList
import com.ealva.ealvabrainz.brainz.data.BrowseInstrumentList
import com.ealva.ealvabrainz.brainz.data.BrowseLabelList
import com.ealva.ealvabrainz.brainz.data.BrowsePlaceList
import com.ealva.ealvabrainz.brainz.data.BrowseRecordingList
import com.ealva.ealvabrainz.brainz.data.BrowseReleaseGroupList
import com.ealva.ealvabrainz.brainz.data.BrowseReleaseList
import com.ealva.ealvabrainz.brainz.data.BrowseSeriesList
import com.ealva.ealvabrainz.brainz.data.BrowseWorkList
import com.ealva.ealvabrainz.brainz.data.CdStubList
import com.ealva.ealvabrainz.brainz.data.Collection
import com.ealva.ealvabrainz.brainz.data.CoverArtRelease
import com.ealva.ealvabrainz.brainz.data.DiscLookupList
import com.ealva.ealvabrainz.brainz.data.Event
import com.ealva.ealvabrainz.brainz.data.Genre
import com.ealva.ealvabrainz.brainz.data.Instrument
import com.ealva.ealvabrainz.brainz.data.IsrcRecordingList
import com.ealva.ealvabrainz.brainz.data.Label
import com.ealva.ealvabrainz.brainz.data.LabelList
import com.ealva.ealvabrainz.brainz.data.Place
import com.ealva.ealvabrainz.brainz.data.Recording
import com.ealva.ealvabrainz.brainz.data.RecordingList
import com.ealva.ealvabrainz.brainz.data.Release
import com.ealva.ealvabrainz.brainz.data.ReleaseGroup
import com.ealva.ealvabrainz.brainz.data.ReleaseGroupList
import com.ealva.ealvabrainz.brainz.data.ReleaseList
import com.ealva.ealvabrainz.brainz.data.Series
import com.ealva.ealvabrainz.brainz.data.TagList
import com.ealva.ealvabrainz.brainz.data.Url
import com.ealva.ealvabrainz.brainz.data.Work
import com.ealva.ealvabrainz.brainz.data.WorkList
import com.ealva.ealvabrainz.brainz.data.the250
import com.ealva.ealvabrainz.brainz.data.the500
import com.ealva.ealvabrainz.common.AreaMbid
import com.ealva.ealvabrainz.common.ArtistMbid
import com.ealva.ealvabrainz.common.CollectionMbid
import com.ealva.ealvabrainz.common.DiscId
import com.ealva.ealvabrainz.common.EventMbid
import com.ealva.ealvabrainz.common.GenreMbid
import com.ealva.ealvabrainz.common.InstrumentMbid
import com.ealva.ealvabrainz.common.Isrc
import com.ealva.ealvabrainz.common.Iswc
import com.ealva.ealvabrainz.common.LabelMbid
import com.ealva.ealvabrainz.common.Limit
import com.ealva.ealvabrainz.common.Offset
import com.ealva.ealvabrainz.common.PlaceMbid
import com.ealva.ealvabrainz.common.RecordingMbid
import com.ealva.ealvabrainz.common.ReleaseGroupMbid
import com.ealva.ealvabrainz.common.ReleaseMbid
import com.ealva.ealvabrainz.common.SeriesMbid
import com.ealva.ealvabrainz.common.UrlMbid
import com.ealva.ealvabrainz.common.WorkMbid
import com.ealva.ealvalog.e
import com.ealva.ealvalog.invoke
import com.ealva.ealvalog.lazyLogger
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.mapError
import com.github.michaelbull.result.runCatching
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import java.io.File

private val LOG by lazyLogger(MusicBrainzService::class)

// private const val MUSIC_BRAINZ_API_URL = "http://musicbrainz.org/ws/2/"
private const val MUSIC_BRAINZ_API_SECURE_URL = "https://musicbrainz.org/ws/2/"
private val SERVICE_NAME = MusicBrainzServiceImpl::class.java.simpleName
private const val CACHE_DIR = "MusicBrainz"

private class MusicBrainzServiceImpl(
  private val musicBrainz: MusicBrainz,
  private val coverArtService: CoverArtService,
  private val dispatcher: CoroutineDispatcher
) : MusicBrainzService {
  private val resourceFetcher: ResourceFetcher
    get() = coverArtService.resourceFetcher

  override suspend fun lookupArea(
    mbid: AreaMbid,
    lookup: AreaLookup.() -> Unit
  ): BrainzResult<Area> = brainz { lookupArea(mbid.value, AreaLookup(lookup)) }

  override suspend fun lookupArtist(
    mbid: ArtistMbid,
    lookup: ArtistLookup.() -> Unit
  ): BrainzResult<Artist> = brainz { lookupArtist(mbid.value, ArtistLookup(lookup)) }

  override suspend fun lookupCollection(
    mbid: CollectionMbid,
    lookup: CollectionLookup.() -> Unit
  ): BrainzResult<Collection> = brainz { lookupCollection(mbid.value, CollectionLookup(lookup)) }

  override suspend fun lookupEvent(
    mbid: EventMbid,
    lookup: EventLookup.() -> Unit
  ): BrainzResult<Event> = brainz { lookupEvent(mbid.value, EventLookup(lookup)) }

  override suspend fun lookupGenre(
    mbid: GenreMbid,
    lookup: GenreLookup.() -> Unit
  ): BrainzResult<Genre> = brainz { lookupGenre(mbid.value, GenreLookup(lookup)) }

  override suspend fun lookupInstrument(
    mbid: InstrumentMbid,
    lookup: InstrumentLookup.() -> Unit
  ): BrainzResult<Instrument> = brainz { lookupInstrument(mbid.value, InstrumentLookup(lookup)) }

  override suspend fun lookupLabel(
    mbid: LabelMbid,
    lookup: LabelLookup.() -> Unit
  ): BrainzResult<Label> = brainz { lookupLabel(mbid.value, LabelLookup(lookup)) }

  override suspend fun lookupPlace(
    mbid: PlaceMbid,
    lookup: PlaceLookup.() -> Unit
  ): BrainzResult<Place> = brainz { lookupPlace(mbid.value, PlaceLookup(lookup)) }

  override suspend fun lookupRecording(
    mbid: RecordingMbid,
    lookup: RecordingLookup.() -> Unit
  ): BrainzResult<Recording> = brainz { lookupRecording(mbid.value, RecordingLookup(lookup)) }

  override suspend fun lookupRelease(
    mbid: ReleaseMbid,
    lookup: ReleaseLookup.() -> Unit
  ): BrainzResult<Release> = brainz { lookupRelease(mbid.value, ReleaseLookup(lookup)) }

  override suspend fun lookupReleaseGroup(
    mbid: ReleaseGroupMbid,
    lookup: ReleaseGroupLookup.() -> Unit
  ): BrainzResult<ReleaseGroup> = brainz {
    lookupReleaseGroup(mbid.value, ReleaseGroupLookup(lookup))
  }

  override suspend fun lookupSeries(
    mbid: SeriesMbid,
    lookup: SeriesLookup.() -> Unit
  ): BrainzResult<Series> = brainz { lookupSeries(mbid.value, SeriesLookup(lookup)) }

  override suspend fun lookupUrl(
    mbid: UrlMbid,
    lookup: UrlLookup.() -> Unit
  ): BrainzResult<Url> = brainz { lookupUrl(mbid.value, UrlLookup(lookup)) }

  override suspend fun lookupWork(
    mbid: WorkMbid,
    lookup: WorkLookup.() -> Unit
  ): BrainzResult<Work> = brainz { lookupWork(mbid.value, WorkLookup(lookup)) }

  override suspend fun lookupDisc(
    discId: DiscId,
    toc: TocParam?,
    excludeCDStubs: Boolean,
    allMediumFormats: Boolean,
    lookup: ReleaseLookup.() -> Unit
  ): BrainzResult<DiscLookupList> = brainz {
    lookupDisc(discId.value, ReleaseLookup(toc, excludeCDStubs, allMediumFormats, lookup))
  }

  override suspend fun fuzzyTocLookup(
    toc: TocParam,
    excludeCDStubs: Boolean,
    allMediumFormats: Boolean,
    lookup: ReleaseLookup.() -> Unit
  ): BrainzResult<BrowseReleaseList> = brainz {
    fuzzyLookupTOC(ReleaseLookup(toc, excludeCDStubs, allMediumFormats, lookup))
  }

  override suspend fun lookupIsrc(
    isrc: Isrc,
    lookup: RecordingLookup.() -> Unit
  ): BrainzResult<IsrcRecordingList> = brainz { lookupIsrc(isrc.value, IsrcLookup(lookup)) }

  override suspend fun lookupIswc(
    iswc: Iswc,
    lookup: WorkLookup.() -> Unit
  ): BrainzResult<BrowseWorkList> = brainz { lookupIswc(iswc.value, IswcLookup(lookup)) }

  override suspend fun browseAreas(
    browseOn: AreaBrowse.BrowseOn,
    limit: Limit?,
    offset: Offset?,
    browse: AreaBrowse.() -> Unit
  ): BrainzResult<BrowseAreaList> = brainz {
    browseAreas(AreaBrowse(browseOn, limit, offset, browse))
  }

  override suspend fun browseArtists(
    browseOn: ArtistBrowse.BrowseOn,
    limit: Limit?,
    offset: Offset?,
    browse: ArtistBrowse.() -> Unit
  ): BrainzResult<BrowseArtistList> = brainz {
    browseArtists(ArtistBrowse(browseOn, limit, offset, browse))
  }

  override suspend fun browseCollections(
    browseOn: CollectionBrowse.BrowseOn,
    limit: Limit?,
    offset: Offset?,
    browse: CollectionBrowse.() -> Unit
  ): BrainzResult<BrowseCollectionList> = brainz {
    browseCollections(CollectionBrowse(browseOn, limit, offset, browse))
  }

  override suspend fun browseEvents(
    browseOn: EventBrowse.BrowseOn,
    limit: Limit?,
    offset: Offset?,
    browse: EventBrowse.() -> Unit
  ): BrainzResult<BrowseEventList> = brainz {
    browseEvents(EventBrowse(browseOn, limit, offset, browse))
  }

  override suspend fun browseInstruments(
    browseOn: InstrumentBrowse.BrowseOn,
    limit: Limit?,
    offset: Offset?,
    browse: InstrumentBrowse.() -> Unit
  ): BrainzResult<BrowseInstrumentList> = brainz {
    browseInstruments(InstrumentBrowse(browseOn, limit, offset, browse))
  }

  override suspend fun browseLabels(
    browseOn: LabelBrowse.BrowseOn,
    limit: Limit?,
    offset: Offset?,
    browse: LabelBrowse.() -> Unit
  ): BrainzResult<BrowseLabelList> = brainz {
    browseLabels(LabelBrowse(browseOn, limit, offset, browse))
  }

  override suspend fun browsePlaces(
    browseOn: PlaceBrowse.BrowseOn,
    limit: Limit?,
    offset: Offset?,
    browse: PlaceBrowse.() -> Unit
  ): BrainzResult<BrowsePlaceList> = brainz {
    browsePlaces(PlaceBrowse(browseOn, limit, offset, browse))
  }

  override suspend fun browseRecordings(
    browseOn: RecordingBrowse.BrowseOn,
    limit: Limit?,
    offset: Offset?,
    browse: RecordingBrowse.() -> Unit
  ): BrainzResult<BrowseRecordingList> = brainz {
    browseRecordings(RecordingBrowse(browseOn, limit, offset, browse))
  }

  override suspend fun browseReleases(
    browseOn: ReleaseBrowse.BrowseOn,
    limit: Limit?,
    offset: Offset?,
    browse: ReleaseBrowse.() -> Unit
  ): BrainzResult<BrowseReleaseList> = brainz {
    browseReleases(ReleaseBrowse(browseOn, limit, offset, browse))
  }

  override suspend fun browseReleaseGroups(
    browseOn: ReleaseGroupBrowse.BrowseOn,
    limit: Limit?,
    offset: Offset?,
    browse: ReleaseGroupBrowse.() -> Unit
  ): BrainzResult<BrowseReleaseGroupList> = brainz {
    browseReleaseGroups(ReleaseGroupBrowse(browseOn, limit, offset, browse))
  }

  override suspend fun browseSeries(
    browseOn: SeriesBrowse.BrowseOn,
    limit: Limit?,
    offset: Offset?,
    browse: SeriesBrowse.() -> Unit
  ): BrainzResult<BrowseSeriesList> = brainz {
    browseSeries(SeriesBrowse(browseOn, limit, offset, browse))
  }

  override suspend fun browseWorks(
    browseOn: WorkBrowse.BrowseOn,
    limit: Limit?,
    offset: Offset?,
    browse: WorkBrowse.() -> Unit
  ): BrainzResult<BrowseWorkList> = brainz {
    browseWorks(WorkBrowse(browseOn, limit, offset, browse))
  }

  override suspend fun findAnnotation(
    limit: Limit?,
    offset: Offset?,
    search: AnnotationSearch.() -> Unit
  ): BrainzResult<AnnotationList> = brainz {
    findAnnotation(AnnotationSearch(search), limit?.value, offset?.value)
  }

  override suspend fun findArtist(
    limit: Limit?,
    offset: Offset?,
    search: ArtistSearch.() -> Unit
  ): BrainzResult<ArtistList> = brainz {
    findArtist(ArtistSearch(search), limit?.value, offset?.value)
  }

  override suspend fun findCdStub(
    limit: Limit?,
    offset: Offset?,
    search: CdStubSearch.() -> Unit
  ): BrainzResult<CdStubList> = brainz {
    findCDStub(CdStubSearch(search), limit?.value, offset?.value)
  }

  override suspend fun findLabel(
    limit: Limit?,
    offset: Offset?,
    search: LabelSearch.() -> Unit
  ): BrainzResult<LabelList> = brainz {
    findLabel(LabelSearch(search), limit?.value, offset?.value)
  }

  override suspend fun findRecording(
    limit: Limit?,
    offset: Offset?,
    search: RecordingSearch.() -> Unit
  ): BrainzResult<RecordingList> = brainz {
    findRecording(RecordingSearch(search), limit?.value, offset?.value)
  }

  override suspend fun findRelease(
    limit: Limit?,
    offset: Offset?,
    search: ReleaseSearch.() -> Unit
  ): BrainzResult<ReleaseList> = brainz {
    findRelease(ReleaseSearch(search), limit?.value, offset?.value)
  }

  override suspend fun findReleaseGroup(
    limit: Limit?,
    offset: Offset?,
    search: ReleaseGroupSearch.() -> Unit
  ): BrainzResult<ReleaseGroupList> = brainz {
    findReleaseGroup(ReleaseGroupSearch(search), limit?.value, offset?.value)
  }

  override suspend fun findTag(
    limit: Limit?,
    offset: Offset?,
    search: TagSearch.() -> Unit
  ): BrainzResult<TagList> = brainz {
    findTag(TagSearch(search), limit?.value, offset?.value)
  }

  override suspend fun findWork(
    limit: Limit?,
    offset: Offset?,
    search: WorkSearch.() -> Unit
  ): BrainzResult<WorkList> = brainz {
    findWork(WorkSearch(search), limit?.value, offset?.value)
  }

  override suspend fun <T : Any> brainz(
    block: BrainzCall<T>
  ): Result<T, BrainzMessage> = withContext(dispatcher) {
    runCatching { musicBrainz.block() }
      .mapError { ex -> BrainzMessage.BrainzExceptionMessage(ex) }
      .mapResponse()
  }

  override suspend fun getReleaseGroupArtwork(mbid: ReleaseGroupMbid): Uri = when (
    val result = coverArtService.getReleaseGroupArt(mbid)
  ) {
    is Ok ->
      result.value
        .releaseImageSequence()
        .filterNot { it.isBlank() }
        .firstOrNull()
        .toSecureUri()
    is Err -> {
      LOG.e { it("Error: %s", result.getErrorString(resourceFetcher)) }
      Uri.EMPTY
    }
  }

  override suspend fun getReleaseArtwork(mbid: ReleaseMbid): Uri = when (
    val result = coverArtService.getReleaseArt(mbid)
  ) {
    is Ok ->
      result.value
        .releaseImageSequence()
        .filterNot { it.isBlank() }
        .firstOrNull()
        .toSecureUri()
    is Err -> {
      LOG.e { it("Error: %s", result.getErrorString(resourceFetcher)) }
      Uri.EMPTY
    }
  }

  private fun CoverArtRelease?.releaseImageSequence(): Sequence<String> {
    return if (this == null) emptySequence() else sequence {
      images.forEach { coverArtImage ->
        yield(coverArtImage.image)
        coverArtImage.thumbnails.run {
          yield(the500)
          yield(the250)
          yield(size1200)
        }
      }
    }.distinct()
  }
}

/**
 * Internal for test
 */
internal fun makeMusicBrainzService(
  musicBrainz: MusicBrainz,
  coverArtService: CoverArtService,
  dispatcher: CoroutineDispatcher
): MusicBrainzService = MusicBrainzServiceImpl(musicBrainz, coverArtService, dispatcher)

internal fun makeMusicBrainzService(
  appName: String,
  appVersion: String,
  contact: String,
  credentialsProvider: CredentialsProvider?,
  ctx: Context,
  coverArt: CoverArtService,
  dispatcher: CoroutineDispatcher
): MusicBrainzService = makeMusicBrainzService(
  buildMusicBrainz(
    appName,
    appVersion,
    contact,
    credentialsProvider,
    File(ctx.cacheDir, CACHE_DIR)
  ),
  coverArt,
  dispatcher
)

private fun buildMusicBrainz(
  appName: String,
  appVersion: String,
  emailContact: String,
  credentialsProvider: CredentialsProvider?,
  cacheDirectory: File
): MusicBrainz = Retrofit.Builder()
  .client(
    makeOkHttpClient(
      SERVICE_NAME,
      appName,
      appVersion,
      emailContact,
      cacheDirectory,
      credentialsProvider
    )
  )
  .baseUrl(MUSIC_BRAINZ_API_SECURE_URL)
  .addMoshiConverterFactory()
  .build()
  .create(MusicBrainz::class.java)
