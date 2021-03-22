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

package com.ealva.ealvabrainz.brainz

import com.ealva.ealvabrainz.brainz.data.Area
import com.ealva.ealvabrainz.brainz.data.Artist
import com.ealva.ealvabrainz.brainz.data.ArtistList
import com.ealva.ealvabrainz.brainz.data.BrowseArtistList
import com.ealva.ealvabrainz.brainz.data.BrowseEventList
import com.ealva.ealvabrainz.brainz.data.BrowseLabelList
import com.ealva.ealvabrainz.brainz.data.BrowsePlaceList
import com.ealva.ealvabrainz.brainz.data.BrowseRecordingList
import com.ealva.ealvabrainz.brainz.data.BrowseReleaseGroupList
import com.ealva.ealvabrainz.brainz.data.BrowseReleaseList
import com.ealva.ealvabrainz.brainz.data.BrowseWorkList
import com.ealva.ealvabrainz.brainz.data.CdStubList
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
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

@Suppress("MaxLineLength")
/**
 * MusicBrainz Retrofit interface
 *
 * Possible response status codes are:
 * * 200 Ok, the response contains an entity/entities
 * * 400 if client data is bad, possibly a malformed MBID.
 * * 404 if there was nothing found.
 * * 405 if the request method is not one of GET or HEAD.
 * * 406 if the server is unable to generate a response suitable to the Accept header.
 * * 503 if the user has exceeded their rate limit.
 *
 * Be sure to read [MusicBrainz requirements](https://musicbrainz.org/doc/XML_Web_Service/Rate_Limiting#Provide_meaningful_User-Agent_strings)
 * for querying their servers.
 *
 * [MusicBrainz API](https://musicbrainz.org/doc/MusicBrainz_API
 *
 * [MusicBrainz query](https://musicbrainz.org/doc/Development/XML_Web_Service/Version_2/Search)
 *
 * [Lucene Query syntax](https://lucene.apache.org/core/7_7_2/queryparser/org/apache/lucene/queryparser/classic/package-summary.html#package.description)
 */
public interface MusicBrainz {
  /**
   * [mbid] is an Area MBID and [include] is the list of relationships, or other misc includes, to
   * specify how much of the data about the linked entities should be included
   *
   * [See](https://musicbrainz.org/doc/MusicBrainz_API/Examples#Area)
   *
   * Example lookup Area by mbid for the city
   * ["Pärnu"](https://musicbrainz.org/ws/2/area/45f07934-675a-46d6-a577-6f8637a411b1?inc=aliases&fmt=json)
   */
  @GET("area/{mbid}")
  public suspend fun lookupArea(
    /** Must be a valid Area MBID */
    @Path("mbid") mbid: String,
    /**
     * A combination of [Area.Misc] and/or
     * [Relationships][com.ealva.ealvabrainz.brainz.data.Relationships]
     */
    @Query("inc") include: String? = null
  ): Response<Area>

  /**
   * [mbid] is an Artist MBID and the [options] QueryMap may contain "inc", "type", and/or
   * "status".
   *
   * [See](https://musicbrainz.org/doc/MusicBrainz_API/Examples#Artist)
   *
   * Example lookup for the Artist
   * [Nirvana](http://musicbrainz.org/ws/2/artist/5b11f4ce-a62d-471e-81fc-a69a8278c7da?fmt=json)
   */
  @GET("artist/{mbid}")
  public suspend fun lookupArtist(
    @Path("mbid") mbid: String,
    @QueryMap options: Map<String, String> = emptyMap()
  ): Response<Artist>

  /**
   * [mbid] is an Event MBID and [include] is the list of relationships, or other misc includes, to
   * specify how much of the data about the linked entities should be included
   *
   * [See](https://musicbrainz.org/doc/MusicBrainz_API/Examples#Event)
   *
   * Example lookup for Event
   * [Nine Inch Nails at Arena Riga]()https://musicbrainz.org/ws/2/event/fe39727a-3d21-4066-9345-3970cbd6cca4?inc=aliases+artist-rels+place-rels&fmt=json)
   */
  @GET("event/{mbid}")
  public suspend fun lookupEvent(
    /** Must be a valid Event MBID */
    @Path("mbid") mbid: String,
    /**
     * A combination of [Event.Misc] and/or
     * [Relationships][com.ealva.ealvabrainz.brainz.data.Relationships]
     */
    @Query("inc") include: String? = null
  ): Response<Event>

  /**
   * [mbid] is a Genre mbid and [include] is the list of relationships, or other misc includes, to
   * specify how much of the data about the linked entities should be included
   *
   * [See](https://musicbrainz.org/doc/MusicBrainz_API/Examples#Genre)
   *
   * Example lookup for Genre
   * [crust punk]()http://musicbrainz.org/ws/2/genre/f66d7266-eb3d-4ef3-b4d8-b7cd992f918b&fmt=json)
   */
  @GET("genre/{mbid}")
  public suspend fun lookupGenre(
    /** Must be a valid Genre MBID */
    @Path("mbid") mbid: String,
    /**
     * A combination of [Genre.Misc] and/or
     * [Relationships][com.ealva.ealvabrainz.brainz.data.Relationships]
     */
    @Query("inc") include: String? = null
  ): Response<Genre>

  /**
   * [mbid] is an Instrument MBID and [include] is the list of relationships, or other misc
   * includes, to specify how much of the data about the linked entities should be included
   *
   * [See](https://musicbrainz.org/doc/MusicBrainz_API/Examples#Instrument)
   *
   * Example lookup for Instrument
   * [kemanak](https://musicbrainz.org/ws/2/instrument/dd430e7f-36ba-49a5-825b-80a525e69190?inc=aliases&fmt=json)
   */
  @GET("instrument/{mbid}")
  public suspend fun lookupInstrument(
    /** Must be a valid Instrument MBID */
    @Path("mbid") mbid: String,
    /**
     * A combination of [Instrument.Misc] and/or
     * [Relationships][com.ealva.ealvabrainz.brainz.data.Relationships]
     */
    @Query("inc") include: String? = null
  ): Response<Instrument>

  /**
   * [mbid] is a Label MBID and the [options] QueryMap may contain "inc", "type", and/or
   * "status".
   *
   * [See](https://musicbrainz.org/doc/MusicBrainz_API/Examples#Label)
   *
   * Example lookup for Label
   * [Warner Bros. Records](http://musicbrainz.org/ws/2/label/c595c289-47ce-4fba-b999-b87503e8cb71?inc=ratings+annotation&fmt=json)
   */
  @GET("label/{mbid}")
  public suspend fun lookupLabel(
    @Path("mbid") mbid: String,
    @QueryMap options: Map<String, String> = emptyMap()
  ): Response<Label>

  /**
   * [mbid] is a Place MBID and [include] is the list of relationships, or other misc
   * includes, to specify how much of the data about the linked entities should be included
   *
   * [See](https://musicbrainz.org/doc/MusicBrainz_API/Examples#Place)
   *
   * Example lookup for Place
   * [Arēna Rīga](https://musicbrainz.org/ws/2/place/478558f9-a951-4067-ad91-e83f6ba63e74?inc=aliases&fmt=json)
   */
  @GET("place/{mbid}")
  public suspend fun lookupPlace(
    /** Must be a valid Place MBID */
    @Path("mbid") mbid: String,
    /**
     * A combination of [Place.Misc] and/or
     * [Relationships][com.ealva.ealvabrainz.brainz.data.Relationships]
     */
    @Query("inc") include: String? = null
  ): Response<Place>

  /**
   * [mbid] is a Recording MBID and [options] QueryMap may contain "inc", "type", and/or "status"
   *
   * Example lookup for Recording
   * [LAST ANGEL](http://musicbrainz.org/ws/2/recording/fcbcdc39-8851-4efc-a02a-ab0e13be224f?fmt=json)
   */
  @GET("recording/{mbid}")
  public suspend fun lookupRecording(
    @Path("mbid") mbid: String,
    @QueryMap options: Map<String, String> = emptyMap()
  ): Response<Recording>

  /**
   * [mbid] is a Release MBID and [options] QueryMap may contain "inc", "type", and/or "status"
   *
   * [See](https://musicbrainz.org/doc/MusicBrainz_API/Examples#Release)
   *
   * Example lookup for Release
   * [Houses of the Holy](https://musicbrainz.org/ws/2/release/938cef50-de9a-3ced-a1fe-bdfbd3bc4315?fmt=json)
   */
  @GET("release/{mbid}")
  public suspend fun lookupRelease(
    @Path("mbid") mbid: String,
    @QueryMap options: Map<String, String> = emptyMap()
  ): Response<Release>

  /**
   * [mbid] is a Release Group MBID and [options] may contain "inc", "type", and/or "status"
   *
   * [See](https://musicbrainz.org/doc/MusicBrainz_API/Examples#Release_Group)
   *
   * Example lookup for Release Group
   * [The Essential Alice in Chains](https://musicbrainz.org/ws/2/release-group/e1b7e76e-09ff-36fd-b1fd-0c6cb199b817?fmt=json)
   */
  @GET("release-group/{mbid}")
  public suspend fun lookupReleaseGroup(
    @Path("mbid") mbid: String,
    @QueryMap options: Map<String, String> = emptyMap()
  ): Response<ReleaseGroup>

  /**
   * [mbid] is a Series MBID and [include] is the list of relationships, or other misc
   * includes, to specify how much of the data about the linked entities should be included
   *
   * Example lookup for Series
   * [The MDNA Tour](https://musicbrainz.org/ws/2/series/300676c6-6e63-4d4d-9084-089efcd0113f?fmt=json)
   */
  @GET("series/{mbid}")
  public suspend fun lookupSeries(
    /** Must be a valid Series MBID */
    @Path("mbid") mbid: String,
    /**
     * A combination of [Series.Misc] and/or
     * [Relationships][com.ealva.ealvabrainz.brainz.data.Relationships]
     */
    @Query("inc") include: String? = null
  ): Response<Series>

  /**
   * [mbid] is an Url MBID and [include] is the list of relationships, or other misc
   * includes, to specify how much of the data about the linked entities should be included
   *
   * [See](https://musicbrainz.org/doc/MusicBrainz_API/Examples#URL)
   *
   * Example lookup for Url
   * [https://www.arvopart.ee/](https://musicbrainz.org/ws/2/url/46d8f693-52e4-4d03-936f-7ca8459019a7?fmt=json)
   */
  @GET("url/{mbid}")
  public suspend fun lookupUrl(
    /** Must be a valid Url MBID */
    @Path("mbid") mbid: String,
    /**
     * A combination of [Url.Misc] and/or
     * [Relationships][com.ealva.ealvabrainz.brainz.data.Relationships]
     */
    @Query("inc") include: String? = null
  ): Response<Url>

  /**
   * [mbid] is an Work MBID and [include] is the list of relationships, or other misc
   * includes, to specify how much of the data about the linked entities should be included
   *
   * [See](https://musicbrainz.org/doc/MusicBrainz_API/Examples#Work)
   *
   * Example lookup for Work
   * [HELLO! また会おうね](http://musicbrainz.org/ws/2/work/b1df2cf3-69a9-3bc0-be44-f71e79b27a22?inc=artist-rels&fmt=json)
   */
  @GET("work/{mbid}")
  public suspend fun lookupWork(
    /** Must be a valid Work MBID */
    @Path("mbid") mbid: String,
    /**
     * A combination of [Work.Misc] and/or
     * [Relationships][com.ealva.ealvabrainz.brainz.data.Relationships]
     */
    @Query("inc") include: String? = null
  ): Response<Work>

  /**
   * [discId] is a Disc ID and [options] QueryMap may contain "inc", "toc", "cdstubs"="no", and/or
   * "media-format"="all"
   *
   * [See](https://musicbrainz.org/doc/MusicBrainz_API#discid)
   *
   * Example [DiscID](https://musicbrainz.org/ws/2/discid/I5l9cCSFccLKFEKS.7wqSZAorPU-?toc=1+12+267257+150+22767+41887+58317+72102+91375+104652+115380+132165+143932+159870+174597&cdstubs=no&media-format=all&fmt=json)
   */
  @GET("discid/{discid}")
  public suspend fun lookupDisc(
    @Path("discid") discId: String,
    @QueryMap options: Map<String, String> = emptyMap()
  ): Response<DiscLookupList>

  /**
   * A "fuzzy" TOC search is a Disc ID lookup that uses the Table of Contents to attempt to find a
   * matching Disc. [options] QueryMap must contain a "toc" and may contain "inc", "cdstubs"="no",
   * and/or "media-format"="all"
   *
   * [See](https://musicbrainz.org/doc/MusicBrainz_API#discid)
   *
   * Example [Fuzzy Toc Lookup](https://musicbrainz.org/ws/2/discid/-?toc=1+12+267257+150+22767+41887+58317+72102+91375+104652+115380+132165+143932+159870+174597&fmt=json)
   */
  @GET("discid/-}")
  public suspend fun fuzzyLookupTOC(
    @QueryMap options: Map<String, String>
  ): Response<BrowseReleaseList>

  /**
   * An [isrc] lookup returns a list of recordings and [options] may contain "inc", "type", and/or
   * "status", which are identical to a [lookupRecording].
   *
   * [See](https://musicbrainz.org/doc/MusicBrainz_API#isrc)
   * [ISRC](https://isrc.ifpi.org/)
   * [MusicBrainz ISRC](https://musicbrainz.org/doc/ISRC#Determining_ISRCs_of_recordings)
   *
   * Example [ISRC](https://musicbrainz.org/ws/2/isrc/JPB600760301?inc=artist-credits&fmt=json)
   */
  @GET("isrc/{isrc}")
  public suspend fun lookupIsrc(
    @Path("isrc") isrc: String,
    @QueryMap options: Map<String, String> = emptyMap()
  ): Response<IsrcRecordingList>

  /**
   * An [iswc] lookup returns a list of works, the 'inc=' arguments supported are identical to a
   * lookup request for a Work.
   *
   * [See](https://musicbrainz.org/doc/MusicBrainz_API#iswc)
   * [ISWC](https://iswcnet.cisac.org/)
   *
   * Example [ISWC](https://musicbrainz.org/ws/2/iswc/T-101.690.320-9?fmt=json)
   */
  @GET("iswc/{iswc}")
  public suspend fun lookupIswc(
    @Path("iswc") iswc: String,
    @Query("inc") include: String? = null
  ): Response<BrowseWorkList>

  /**
   * Returns an [ArtistList] given [query] which is the fully formed MusicBrainz query (in the
   * example this is: artist:"Led Zeppelin". Optional [limit] is
   * the max number of artists returned and must be 1-100 inclusive, defaults to 25 if not
   * specified. Optional [offset] is used to page results. The count and offset are both
   * returned in the resulting ReleaseList
   *
   * [Example](http://musicbrainz.org/ws/2/artist/?query=artist:%22Led%20Zeppelin%22&fmt=json)
   *
   * @param query full MusicBrainz lucene query
   * @param limit 1..100 inclusive are valid. If skipped, defaults to 25
   * @param offset specifies starting offset into list, used to page results. Defaults to 0
   */
  @GET("artist")
  public suspend fun findArtist(
    @Query("query") query: String,
    @Query("limit") limit: Int? = null,
    @Query("offset") offset: Int? = null
  ): Response<ArtistList>

  /**
   *
   * [Example](http://musicbrainz.org/ws/2/cdstub/?query=title:Doo)
   */
  @GET("cdstub")
  public suspend fun findCDStub(
    @Query("query") query: String,
    @Query("limit") limit: Int? = null,
    @Query("offset") offset: Int? = null
  ): Response<CdStubList>

  @GET("label")
  public suspend fun findLabel(
    @Query("query") query: String,
    @Query("limit") limit: Int? = null,
    @Query("offset") offset: Int? = null
  ): Response<LabelList>

  /**
   * Example is a query for Recording with MBID of "0fc4f7e7-8dcc-4dd3-8d35-d6f4c1f6b0f2", which is
   * a recording of the song "How Many Times" by the artist Wolfmother from their "New Crown"
   * release.
   *
   * Example [http://musicbrainz.org/ws/2/recording/?query=rid:%220fc4f7e7-8dcc-4dd3-8d35-d6f4c1f6b0f2%22&fmt=json]
   *
   * @param query full MusicBrainz lucene query
   * @param limit 1..100 inclusive are valid. If null, defaults to 25
   * @param offset specifies starting offset into list, used to page results. Defaults to 0
   * @return [Response] containing the query result [RecordingList]
   */
  @GET("recording")
  public suspend fun findRecording(
    @Query("query") query: String,
    @Query("limit") limit: Int? = null,
    @Query("offset") offset: Int? = null
  ): Response<RecordingList>

  /**
   * Returns a [ReleaseList] given [query] which is the fully formed MusicBrainz query (in the
   * example this is: release:"Houses of the Holy" AND artist:"Led Zeppelin" . Optional [limit] is
   * the max number of releases returned and must be 1-100 inclusive, defaults to 25 if not
   * specified. Optional [offset] is used to page results. The count and offset are both
   * returned in the resulting ReleaseList
   *
   * Example: [http://musicbrainz.org/ws/2/release/?query=release:Houses%20of%20the%20Holy%20AND%20artist:Led%20Zeppelin&fmt=json](http://musicbrainz.org/ws/2/release/?query=release:Houses%20of%20the%20Holy%20AND%20artist:Led%20Zeppelin&fmt=json)
   *
   * @param query full MusicBrainz lucene query
   * @param limit 1..100 inclusive are valid. If skipped, defaults to 25
   * @param offset specifies starting offset into list, used to page results. Defaults to 0
   */
  @GET("release")
  public suspend fun findRelease(
    @Query("query") query: String,
    @Query("limit") limit: Int? = null,
    @Query("offset") offset: Int? = null
  ): Response<ReleaseList>

  /**
   * Returns a [ReleaseGroupList] given [query] which is the fully formed MusicBrainz query (in the
   * example this is: release:"Houses of the Holy" AND artist:"Led Zeppelin" . Optional [limit] is
   * the max number of releases returned and must be 1-100 inclusive, defaults to 25 if not
   * specified. Optional [offset] is used to page results. The count and offset are both
   * returned in the resulting ReleaseList
   *
   * [Example: http://musicbrainz.org/ws/2/release-group/?query=release:%22Houses%20of%20the%20Holy%22%20AND%20artist:%22Led%20Zeppelin%22&fmt=json](http://musicbrainz.org/ws/2/release-group/?query=release:%22Houses%20of%20the%20Holy%22%20AND%20artist:%22Led%20Zeppelin%22&fmt=json)
   *
   * @param query full MusicBrainz lucene query
   * @param limit 1..100 inclusive are valid. If skipped, defaults to 25
   * @param offset specifies starting offset into list, used to page results. Defaults to 0
   */
  @GET("release-group")
  public suspend fun findReleaseGroup(
    @Query("query") query: String,
    @Query("limit") limit: Int? = null,
    @Query("offset") offset: Int? = null
  ): Response<ReleaseGroupList>

  @GET("tag")
  public suspend fun findTag(
    @Query("query") query: String,
    @Query("limit") limit: Int? = null,
    @Query("offset") offset: Int? = null
  ): Response<TagList>

  /**
   * https://musicbrainz.org/ws/2/work/?query=work:Frozen%20AND%20arid:4c006444-ccbf-425e-b3e7-03a98bab5997&fmt=json
   */
  @GET("work")
  public suspend fun findWork(
    @Query("query") query: String,
    @Query("limit") limit: Int? = null,
    @Query("offset") offset: Int? = null
  ): Response<WorkList>

  /**
   * Only one of:
   * * area
   * * recording
   * * release
   * * release-group
   * * work
   *
   * are allowed in an invocation. Using more than one ID will generate a malformed URI.
   */
  @GET("artist")
  public suspend fun browseArtists(
    @QueryMap options: Map<String, String>
  ): Response<BrowseArtistList>

  /**
   * Only one of:
   * * area
   * * artist
   * * place
   *
   * are allowed in an invocation. Using more than one ID will generate a malformed URI.
   */
  @GET("event")
  public suspend fun browseEvents(
    @QueryMap options: Map<String, String>
  ): Response<BrowseEventList>

  /**
   * Only one of:
   * * area
   * * release
   *
   * are allowed in an invocation. Using more than one ID will generate a malformed URI.
   */
  @GET("label")
  public suspend fun browseLabels(
    @QueryMap options: Map<String, String>
  ): Response<BrowseLabelList>

  /**
   * Only one of:
   * * area
   *
   * are allowed in an invocation. Using more than one ID will generate a malformed URI.
   */
  @GET("place")
  public suspend fun browsePlaces(
    @QueryMap options: Map<String, String>
  ): Response<BrowsePlaceList>

  /**
   * Only one of:
   * * artist
   * * release
   * * work
   *
   * are allowed in an invocation. Using more than one ID will generate a malformed URI.
   */
  @GET("recording")
  public suspend fun browseRecordings(
    @QueryMap options: Map<String, String>
  ): Response<BrowseRecordingList>

  /**
   * Only one of:
   * * area
   * * artist
   * * collection
   * * label
   * * trac
   * * track_artist
   * * recording
   * * release-group
   *
   * are allowed in an invocation. Using more than one ID will generate a malformed URI.
   */
  @GET("release")
  public suspend fun browseReleases(
    @QueryMap options: Map<String, String>
  ): Response<BrowseReleaseList>

  /**
   * Only one of:
   * artist
   * release
   *
   * are allowed in an invocation. Using more than one ID will generate a malformed URI.
   */
  @GET("release-group")
  public suspend fun browseReleaseGroups(
    @QueryMap options: Map<String, String>
  ): Response<BrowseReleaseGroupList>

  /**
   * Only one of:
   * artist
   *
   * are allowed in an invocation. Using more than one ID will generate a malformed URI.
   */
  @GET("work")
  public suspend fun browseWorks(
    @QueryMap options: Map<String, String>
  ): Response<BrowseWorkList>
}
