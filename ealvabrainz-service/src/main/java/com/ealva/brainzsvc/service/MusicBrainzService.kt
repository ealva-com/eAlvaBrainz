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
import com.ealva.brainzsvc.common.Limit
import com.ealva.brainzsvc.common.Offset
import com.ealva.brainzsvc.common.TocParam
import com.ealva.brainzsvc.service.BrainzMessage.BrainzExceptionMessage
import com.ealva.brainzsvc.service.BrainzMessage.BrainzStatusMessage.BrainzErrorCodeMessage
import com.ealva.brainzsvc.service.BrainzMessage.BrainzStatusMessage.BrainzNullReturn
import com.ealva.brainzsvc.service.browse.AreaBrowse
import com.ealva.brainzsvc.service.browse.ArtistBrowse
import com.ealva.brainzsvc.service.browse.CollectionBrowse
import com.ealva.brainzsvc.service.browse.EventBrowse
import com.ealva.brainzsvc.service.browse.InstrumentBrowse
import com.ealva.brainzsvc.service.browse.LabelBrowse
import com.ealva.brainzsvc.service.browse.PlaceBrowse
import com.ealva.brainzsvc.service.browse.RecordingBrowse
import com.ealva.brainzsvc.service.browse.ReleaseBrowse
import com.ealva.brainzsvc.service.browse.ReleaseGroupBrowse
import com.ealva.brainzsvc.service.browse.SeriesBrowse
import com.ealva.brainzsvc.service.browse.WorkBrowse
import com.ealva.brainzsvc.service.lookup.AreaLookup
import com.ealva.brainzsvc.service.lookup.ArtistLookup
import com.ealva.brainzsvc.service.lookup.CollectionLookup
import com.ealva.brainzsvc.service.lookup.EventLookup
import com.ealva.brainzsvc.service.lookup.GenreLookup
import com.ealva.brainzsvc.service.lookup.InstrumentLookup
import com.ealva.brainzsvc.service.lookup.LabelLookup
import com.ealva.brainzsvc.service.lookup.PlaceLookup
import com.ealva.brainzsvc.service.lookup.RecordingLookup
import com.ealva.brainzsvc.service.lookup.ReleaseGroupLookup
import com.ealva.brainzsvc.service.lookup.ReleaseLookup
import com.ealva.brainzsvc.service.lookup.SeriesLookup
import com.ealva.brainzsvc.service.lookup.UrlLookup
import com.ealva.brainzsvc.service.lookup.WorkLookup
import com.ealva.brainzsvc.service.search.AnnotationSearch
import com.ealva.brainzsvc.service.search.ArtistSearch
import com.ealva.brainzsvc.service.search.CdStubSearch
import com.ealva.brainzsvc.service.search.LabelSearch
import com.ealva.brainzsvc.service.search.RecordingSearch
import com.ealva.brainzsvc.service.search.ReleaseGroupSearch
import com.ealva.brainzsvc.service.search.ReleaseSearch
import com.ealva.brainzsvc.service.search.TagSearch
import com.ealva.brainzsvc.service.search.WorkSearch
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
import com.ealva.ealvabrainz.common.PlaceMbid
import com.ealva.ealvabrainz.common.RecordingMbid
import com.ealva.ealvabrainz.common.ReleaseGroupMbid
import com.ealva.ealvabrainz.common.ReleaseMbid
import com.ealva.ealvabrainz.common.SeriesMbid
import com.ealva.ealvabrainz.common.UrlMbid
import com.ealva.ealvabrainz.common.WorkMbid
import com.ealva.ealvabrainz.lucene.BrainzMarker
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import retrofit2.Response

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
 * it will contain an instance of T. If an error occurs an [Err] is returned which contains a
 * BrainzMessage.
 *
 * An [Err] will be a [BrainzMessage] of type:
 * * [BrainzExceptionMessage] if an underlying exception is thrown
 * * [BrainzErrorMessage] is MusicBrainz returns an error decoded to a
 * [BrainzError][com.ealva.ealvabrainz.brainz.data.BrainzError]
 * * [BrainzNullReturn] subclass of BrainzStatusMessage, if the response is OK but null
 * * [BrainzErrorCodeMessage] subclass of BrainzStatusMessage, if the response is not successful
 *
 * [MusicBrainz API](https://musicbrainz.org/doc/MusicBrainz_API#Introduction)
 */
@BrainzMarker
public interface MusicBrainzService {
  /**
   * Lookup an [Area] with [mbid] and specify other information to be included via the
   * optional [lookup] lambda with receiver [AreaLookup]
   */
  public suspend fun lookupArea(
    mbid: AreaMbid,
    lookup: AreaLookup.() -> Unit = {}
  ): BrainzResult<Area>

  /**
   * Find the [Artist] with the [mbid] ID. Provide an optional lambda with an [ArtistLookup]
   * receiver to specify if any other information should be included.
   */
  public suspend fun lookupArtist(
    mbid: ArtistMbid,
    lookup: ArtistLookup.() -> Unit = {}
  ): BrainzResult<Artist>

  public suspend fun lookupCollection(
    mbid: CollectionMbid,
    lookup: CollectionLookup.() -> Unit = {}
  ): BrainzResult<Collection>

  /**
   * Find the [Event] with the [mbid] ID. Provide an optional lambda with an [EventLookup]
   * receiver to specify if any other information should be included.
   */
  public suspend fun lookupEvent(
    mbid: EventMbid,
    lookup: EventLookup.() -> Unit = {}
  ): BrainzResult<Event>

  /**
   * Find the [Genre] with the [mbid] ID. Provide an optional lambda with an [GenreLookup]
   * receiver to specify if any other information should be included.
   */
  public suspend fun lookupGenre(
    mbid: GenreMbid,
    lookup: GenreLookup.() -> Unit = {}
  ): BrainzResult<Genre>

  /**
   * Find the [Instrument] with the [mbid] ID. Provide an optional lambda with an
   * [InstrumentLookup] receiver to specify if any other information should be included.
   */
  public suspend fun lookupInstrument(
    mbid: InstrumentMbid,
    lookup: InstrumentLookup.() -> Unit = {}
  ): BrainzResult<Instrument>

  /**
   * Find the [Label] with the [mbid] ID. Provide an optional lambda with a
   * [LabelLookup] receiver to specify if any other information should be included.
   */
  public suspend fun lookupLabel(
    mbid: LabelMbid,
    lookup: LabelLookup.() -> Unit = {}
  ): BrainzResult<Label>

  /**
   * Find the [Place] with the [mbid] ID. Provide an optional lambda with an [PlaceLookup]
   * receiver to specify if any other information should be included.
   */
  public suspend fun lookupPlace(
    mbid: PlaceMbid,
    lookup: PlaceLookup.() -> Unit = {}
  ): BrainzResult<Place>

  /**
   * Find the [Recording] with the [mbid] ID. Provide an optional lambda with a
   * [RecordingLookup] receiver to specify if any other information should be included.
   */
  public suspend fun lookupRecording(
    mbid: RecordingMbid,
    lookup: RecordingLookup.() -> Unit = {}
  ): BrainzResult<Recording>

  /**
   * Find the [Release] with the [mbid] ID. Provide an optional lambda with a
   * [ReleaseLookup] receiver to specify if any other information should be included.
   */
  public suspend fun lookupRelease(
    mbid: ReleaseMbid,
    lookup: ReleaseLookup.() -> Unit = {}
  ): BrainzResult<Release>

  /**
   * Find the [ReleaseGroup] with the [mbid] ID. Provide an optional lambda with a
   * [ReleaseGroupLookup] receiver to specify if any other information should be included.
   */
  public suspend fun lookupReleaseGroup(
    mbid: ReleaseGroupMbid,
    lookup: ReleaseGroupLookup.() -> Unit = {}
  ): BrainzResult<ReleaseGroup>

  /**
   * Find the [Series] with the [mbid] ID. Provide an optional lambda with an [SeriesLookup]
   * receiver to specify if any other information should be included.
   */
  public suspend fun lookupSeries(
    mbid: SeriesMbid,
    lookup: SeriesLookup.() -> Unit = {}
  ): BrainzResult<Series>

  /**
   * Find the [Url] with the [mbid] ID. Provide an optional lambda with an [UrlLookup]
   * receiver to specify if any other information should be included.
   */
  public suspend fun lookupUrl(
    mbid: UrlMbid,
    lookup: UrlLookup.() -> Unit = {}
  ): BrainzResult<Url>

  /**
   * Find the [Work] with the [mbid] ID. Provide an optional lambda with an [WorkLookup]
   * receiver to specify if any other information should be included.
   */
  public suspend fun lookupWork(
    mbid: WorkMbid,
    lookup: WorkLookup.() -> Unit = {}
  ): BrainzResult<Work>

  /**
   * A [discId] lookup returns a list of associated releases, and the 'inc=' arguments supported are
   * identical to a lookup request for a release and are specified via the [ReleaseLookup]
   * parameter [lookup]
   *
   * If there are no matching releases in MusicBrainz, but a matching CD stub exists, it will be
   * returned. This is the default behaviour. If you do not want to see CD stubs, specify
   * [excludeCDStubs] as true. CD stubs are contained within a <cdstub> element, and otherwise have
   * the same form as a release. Note that CD stubs do not have artist credits, just artists.
   *
   * If you provide the [toc] query parameter, and if the provided disc ID is not known by
   * MusicBrainz, a fuzzy lookup will be done to find matching MusicBrainz releases. Note that if CD
   * stubs are found this will not happen. If you do want TOC fuzzy lookup, but not CD stub
   * searching, specify [excludeCDStubs] as true.
   *
   * By default, fuzzy TOC searches only return mediums whose format is set to "CD." If you want to
   * search all mediums regardless of format, set [allMediumFormats] to true
   *
   * [Disc ID Calculation][https://musicbrainz.org/doc/Disc_ID_Calculation]
   */
  public suspend fun lookupDisc(
    discId: DiscId,
    toc: TocParam? = null,
    excludeCDStubs: Boolean = false,
    allMediumFormats: Boolean = false,
    lookup: ReleaseLookup.() -> Unit = {}
  ): BrainzResult<DiscLookupList>

  /**
   * A fuzzy TOC lookup up is similar to [lookupDisc] except no Disc ID is specified and the
   * [toc] parameter is required so MusicBrainz may attempt to match against the TOC (table of
   * contents. A fuzzy lookup will be done to find matching MusicBrainz releases. Note that if CD
   * stubs are found this will not happen. If you do want TOC fuzzy lookup, but not CD stub
   * searching, specify [excludeCDStubs] as true.
   *
   * By default, fuzzy TOC searches only return mediums whose format is set to "CD." If you want to
   * search all mediums regardless of format, set [allMediumFormats] to true
   *
   * @see lookupDisc
   * [How a TOC is derived][https://musicbrainz.org/doc/Disc_ID_Calculation]
   */
  public suspend fun fuzzyTocLookup(
    toc: TocParam,
    excludeCDStubs: Boolean = false,
    allMediumFormats: Boolean = false,
    lookup: ReleaseLookup.() -> Unit = {}
  ): BrainzResult<BrowseReleaseList>

  public suspend fun lookupIsrc(
    isrc: Isrc,
    lookup: RecordingLookup.() -> Unit = {}
  ): BrainzResult<IsrcRecordingList>

  public suspend fun lookupIswc(
    iswc: Iswc,
    lookup: WorkLookup.() -> Unit = {}
  ): BrainzResult<BrowseWorkList>

  public suspend fun browseAreas(
    browseOn: AreaBrowse.BrowseOn,
    limit: Limit? = null,
    offset: Offset? = null,
    browse: AreaBrowse.() -> Unit = {}
  ): BrainzResult<BrowseAreaList>

  public suspend fun browseArtists(
    browseOn: ArtistBrowse.BrowseOn,
    limit: Limit? = null,
    offset: Offset? = null,
    browse: ArtistBrowse.() -> Unit = {}
  ): BrainzResult<BrowseArtistList>

  /**
   * Authorization is required if [Collection.Browse.UserCollections] is included. See
   * [CredentialsProvider]
   */
  public suspend fun browseCollections(
    browseOn: CollectionBrowse.BrowseOn,
    limit: Limit? = null,
    offset: Offset? = null,
    browse: CollectionBrowse.() -> Unit = {}
  ): BrainzResult<BrowseCollectionList>

  public suspend fun browseEvents(
    browseOn: EventBrowse.BrowseOn,
    limit: Limit? = null,
    offset: Offset? = null,
    browse: EventBrowse.() -> Unit = {}
  ): BrainzResult<BrowseEventList>

  public suspend fun browseInstruments(
    browseOn: InstrumentBrowse.BrowseOn,
    limit: Limit?,
    offset: Offset?,
    browse: InstrumentBrowse.() -> Unit
  ): BrainzResult<BrowseInstrumentList>

  public suspend fun browseLabels(
    browseOn: LabelBrowse.BrowseOn,
    limit: Limit? = null,
    offset: Offset? = null,
    browse: LabelBrowse.() -> Unit = {}
  ): BrainzResult<BrowseLabelList>

  public suspend fun browsePlaces(
    browseOn: PlaceBrowse.BrowseOn,
    limit: Limit? = null,
    offset: Offset? = null,
    browse: PlaceBrowse.() -> Unit = {}
  ): BrainzResult<BrowsePlaceList>

  public suspend fun browseRecordings(
    browseOn: RecordingBrowse.BrowseOn,
    limit: Limit? = null,
    offset: Offset? = null,
    browse: RecordingBrowse.() -> Unit = {}
  ): BrainzResult<BrowseRecordingList>

  public suspend fun browseReleases(
    browseOn: ReleaseBrowse.BrowseOn,
    limit: Limit? = null,
    offset: Offset? = null,
    browse: ReleaseBrowse.() -> Unit = {}
  ): BrainzResult<BrowseReleaseList>

  public suspend fun browseReleaseGroups(
    browseOn: ReleaseGroupBrowse.BrowseOn,
    limit: Limit? = null,
    offset: Offset? = null,
    browse: ReleaseGroupBrowse.() -> Unit = {}
  ): BrainzResult<BrowseReleaseGroupList>

  public suspend fun browseSeries(
    browseOn: SeriesBrowse.BrowseOn,
    limit: Limit? = null,
    offset: Offset? = null,
    browse: SeriesBrowse.() -> Unit = {}
  ): BrainzResult<BrowseSeriesList>

  public suspend fun browseWorks(
    browseOn: WorkBrowse.BrowseOn,
    limit: Limit? = null,
    offset: Offset? = null,
    browse: WorkBrowse.() -> Unit = {}
  ): BrainzResult<BrowseWorkList>

  public suspend fun findAnnotation(
    limit: Limit? = null,
    offset: Offset? = null,
    search: AnnotationSearch.() -> Unit
  ): BrainzResult<AnnotationList>

  public suspend fun findArtist(
    limit: Limit? = null,
    offset: Offset? = null,
    search: ArtistSearch.() -> Unit
  ): BrainzResult<ArtistList>

  public suspend fun findCdStub(
    limit: Limit? = null,
    offset: Offset? = null,
    search: CdStubSearch.() -> Unit
  ): BrainzResult<CdStubList>

  public suspend fun findLabel(
    limit: Limit? = null,
    offset: Offset? = null,
    search: LabelSearch.() -> Unit
  ): BrainzResult<LabelList>

  public suspend fun findRecording(
    limit: Limit? = null,
    offset: Offset? = null,
    search: RecordingSearch.() -> Unit
  ): BrainzResult<RecordingList>

  public suspend fun findRelease(
    limit: Limit? = null,
    offset: Offset? = null,
    search: ReleaseSearch.() -> Unit
  ): BrainzResult<ReleaseList>

  public suspend fun findReleaseGroup(
    limit: Limit? = null,
    offset: Offset? = null,
    search: ReleaseGroupSearch.() -> Unit
  ): BrainzResult<ReleaseGroupList>

  /**
   * Authorization is required. See [CredentialsProvider]
   */
  public suspend fun findTag(
    limit: Limit? = null,
    offset: Offset? = null,
    search: TagSearch.() -> Unit
  ): BrainzResult<TagList>

  public suspend fun findWork(
    limit: Limit? = null,
    offset: Offset? = null,
    search: WorkSearch.() -> Unit
  ): BrainzResult<WorkList>

  public suspend fun getReleaseGroupArtwork(mbid: ReleaseGroupMbid): Uri

  public suspend fun getReleaseArtwork(mbid: ReleaseMbid): Uri

  /**
   * NOTE: when eAlvaBrainz is complete it should not be necessary for clients to invoke this
   * function.
   *
   * A main-safe function that calls the [block] function, with [MusicBrainz] as a receiver,
   * dispatched by the contained [CoroutineDispatcher] (typically [Dispatchers.IO] when not under
   * test)
   *
   * Usually [block] is a lambda which makes a direct call to the MusicBrainz Retrofit client. The
   * [block] is responsible for building the necessary String parameters, "query" in case of a
   * find call and "inc" include if doing a lookup. Use the Subquery and Misc defined
   * in the various entity objects for doing a lookup and use SearchField to build queries.
   *
   * @return an [Ok] with value of type [T] or, if response is not successful, an [Err]. An [Err]
   * will be a [BrainzMessage] of type:
   * * [BrainzExceptionMessage] if an underlying exception is thrown
   * * [BrainzErrorMessage] if a successfully decoded
   * [BrainzError][com.ealva.ealvabrainz.brainz.data.BrainzError] is returned from the server,
   * typically indicating an error in the URI (incorrect include, malformed, etc.)
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
      credentialsProvider: CredentialsProvider? = null,
      dispatcher: CoroutineDispatcher = Dispatchers.IO
    ): MusicBrainzService = makeMusicBrainzService(
      appName,
      appVersion,
      contact,
      credentialsProvider,
      ctx,
      coverArt,
      dispatcher
    )

    /** Internal for test, provides for injecting fakes/mocks/etc and test dispatcher. */
    internal fun make(
      brainz: MusicBrainz,
      coverArt: CoverArtService,
      dispatcher: CoroutineDispatcher
    ): MusicBrainzService {
      return makeMusicBrainzService(brainz, coverArt, dispatcher)
    }

//    const val website = "https://musicbrainz.org/"
//    const val registerUrl = """${website}register"""
//    const val forgotPasswordUrl = """${website}lost-password"""
//    const val donateUrl = "http://metabrainz.org/donate"
  }
}
