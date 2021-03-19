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
import com.ealva.ealvabrainz.brainz.data.BrowseRecordingList
import com.ealva.ealvabrainz.brainz.data.BrowseReleaseGroupList
import com.ealva.ealvabrainz.brainz.data.BrowseReleaseList
import com.ealva.ealvabrainz.brainz.data.BrowseWorkList
import com.ealva.ealvabrainz.brainz.data.DiscLookupList
import com.ealva.ealvabrainz.brainz.data.Event
import com.ealva.ealvabrainz.brainz.data.Genre
import com.ealva.ealvabrainz.brainz.data.Instrument
import com.ealva.ealvabrainz.brainz.data.IsrcRecordingList
import com.ealva.ealvabrainz.brainz.data.Label
import com.ealva.ealvabrainz.brainz.data.Place
import com.ealva.ealvabrainz.brainz.data.Recording
import com.ealva.ealvabrainz.brainz.data.RecordingList
import com.ealva.ealvabrainz.brainz.data.Release
import com.ealva.ealvabrainz.brainz.data.ReleaseGroup
import com.ealva.ealvabrainz.brainz.data.ReleaseGroupList
import com.ealva.ealvabrainz.brainz.data.ReleaseList
import com.ealva.ealvabrainz.brainz.data.Series
import com.ealva.ealvabrainz.brainz.data.Url
import com.ealva.ealvabrainz.brainz.data.Work
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
 * [MusicBrainz query documentation](https://musicbrainz.org/doc/Development/XML_Web_Service/Version_2/Search)
 *
 * [Lucene Query syntax](https://lucene.apache.org/core/7_7_2/queryparser/org/apache/lucene/queryparser/classic/package-summary.html#package.description)
 */
public interface MusicBrainz {
  /**
   * Lookup Area by mbid, example is the city "Pärnu"
   * https://musicbrainz.org/ws/2/area/45f07934-675a-46d6-a577-6f8637a411b1?inc=aliases&fmt=json
   *
   * @param mbid the Area mbid, 45f07934-675a-46d6-a577-6f8637a411b1 in above example
   * @param include the list of relationships, or other misc includes, to specify how
   * much of the data about the linked entities should be included
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
   * An example for looking up Nirvana by mbid would be:
   * [http://musicbrainz.org/ws/2/artist/5b11f4ce-a62d-471e-81fc-a69a8278c7da?fmt=json]
   *
   * Map may contain "inc", "type", and/or "status"
   *
   * @param mbid        the artist mbid. In the example this would be:
   * 5b11f4ce-a62d-471e-81fc-a69a8278c7da
   */
  @GET("artist/{mbid}")
  public suspend fun lookupArtist(
    @Path("mbid") mbid: String,
    @QueryMap options: Map<String, String> = emptyMap()
  ): Response<Artist>

  /**
   * Lookup Event by mbid, example is the event "Nine Inch Nails at Arena Riga"
   * https://musicbrainz.org/ws/2/event/fe39727a-3d21-4066-9345-3970cbd6cca4?inc=aliases+artist-rels+place-rels&fmt=json
   *
   * @param mbid the Event mbid, fe39727a-3d21-4066-9345-3970cbd6cca4 in above example
   * @param include the list of relationships, or other misc includes, to specify how
   * much of the data about the linked entities should be included
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
   * Lookup Genre by mbid, example is the genre "crust punk"
   * http://musicbrainz.org/ws/2/genre/f66d7266-eb3d-4ef3-b4d8-b7cd992f918b&fmt=json
   *
   * @param mbid the Genre mbid, f66d7266-eb3d-4ef3-b4d8-b7cd992f918b in above example
   * @param include the list of relationships, or other misc includes, to specify how
   * much of the data about the linked entities should be included
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
   * Lookup Instrument by mbid, example is the instrument "kemanak"
   * https://musicbrainz.org/ws/2/instrument/dd430e7f-36ba-49a5-825b-80a525e69190?inc=aliases&fmt=json
   *
   * @param mbid the Instrument mbid, dd430e7f-36ba-49a5-825b-80a525e69190 in above example
   * @param include the list of relationships, or other misc includes, to specify how
   * much of the data about the linked entities should be included
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
   * [mbid] is a Label mbid, c595c289-47ce-4fba-b999-b87503e8cb71 in above example, and [options]
   * may contain "inc", "type", and/or "status"\
   *
   * An example for looking up a Label (Warner Bros. Records in this example) by mbid is:
   * http://musicbrainz.org/ws/2/label/c595c289-47ce-4fba-b999-b87503e8cb71?inc=ratings+annotation&fmt=json
   */
  @GET("label/{mbid}")
  public suspend fun lookupLabel(
    @Path("mbid") mbid: String,
    @QueryMap options: Map<String, String> = emptyMap()
  ): Response<Label>

  /**
   * Lookup Place by mbid, example is the place "Arēna Rīga"
   * https://musicbrainz.org/ws/2/place/478558f9-a951-4067-ad91-e83f6ba63e74?inc=aliases&fmt=json
   *
   * @param mbid the Place mbid, 46d8f693-52e4-4d03-936f-7ca8459019a7 in above example
   * @param include the list of relationships, or other misc includes, to specify how
   * much of the data about the linked entities should be included
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
   * [mbid] is the recording mbid and [options] may contain "inc", "type", and/or "status"
   *
   * An example for lookup by mbid would be:
   * http://musicbrainz.org/ws/2/recording/fcbcdc39-8851-4efc-a02a-ab0e13be224f?fmt=json
   */
  @GET("recording/{mbid}")
  public suspend fun lookupRecording(
    @Path("mbid") mbid: String,
    @QueryMap options: Map<String, String> = emptyMap()
  ): Response<Recording>

  /**
   * Lookup by mbid for Release. Example is Led Zeppelin's "Houses of the Holy"
   *
   * [https://musicbrainz.org/ws/2/release/938cef50-de9a-3ced-a1fe-bdfbd3bc4315?fmt=json](https://musicbrainz.org/ws/2/release/938cef50-de9a-3ced-a1fe-bdfbd3bc4315?fmt=json)
   *
   * [mbid] is the release mbid. In the example this would be: 938cef50-de9a-3ced-a1fe-bdfbd3bc4315
   * and [options] may contain "inc", "type", and/or "status"
   */
  @GET("release/{mbid}")
  public suspend fun lookupRelease(
    @Path("mbid") mbid: String,
    @QueryMap options: Map<String, String> = emptyMap()
  ): Response<Release>

  /**
   * [mbid] is a release group mbid and [options] may contain "inc", "type", and/or "status"
   *
   * An example for looking up release group The Essential Alice in Chains by mbid would be:
   * https://musicbrainz.org/ws/2/release-group/e1b7e76e-09ff-36fd-b1fd-0c6cb199b817?fmt=json
   */
  @GET("release-group/{mbid}")
  public suspend fun lookupReleaseGroup(
    @Path("mbid") mbid: String,
    @QueryMap options: Map<String, String> = emptyMap()
  ): Response<ReleaseGroup>

  /**
   * Lookup Series by mbid, example is the series "The MDNA Tour"
   * https://musicbrainz.org/ws/2/series/300676c6-6e63-4d4d-9084-089efcd0113f?fmt=json
   *
   * @param mbid the Series mbid, 300676c6-6e63-4d4d-9084-089efcd0113f in above example
   * @param include the list of relationships, or other misc includes, to specify how
   * much of the data about the linked entities should be included
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
   * Lookup Url by mbid, example is the url "https://www.arvopart.ee/"
   * https://musicbrainz.org/ws/2/url/46d8f693-52e4-4d03-936f-7ca8459019a7?fmt=json
   *
   * @param mbid the Url mbid, 46d8f693-52e4-4d03-936f-7ca8459019a7 in above example
   * @param include the list of relationships, or other misc includes, to specify how
   * much of the data about the linked entities should be included
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
   * Lookup Work by mbid, example is the work entitled "HELLO! また会おうね"
   * http://musicbrainz.org/ws/2/work/b1df2cf3-69a9-3bc0-be44-f71e79b27a22?inc=artist-rels&fmt=json
   *
   * @param mbid the Work mbid, b1df2cf3-69a9-3bc0-be44-f71e79b27a22 in above example
   * @param include the list of relationships, or other misc includes, to specify how
   * much of the data about the linked entities should be included
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
   *
   * QueryMap
   * inc=""
   * toc=""
   * cdstubs=no
   * media-format=all
   */
  @GET("discid/{discid}")
  public suspend fun lookupDisc(
    @Path("discid") discid: String,
    @QueryMap options: Map<String, String> = emptyMap()
  ): Response<DiscLookupList>

  /**
   *
   * QueryMap
   * inc=""
   * toc=""
   * cdstubs=no
   * media-format=all
   */
  @GET("discid/-}")
  public suspend fun fuzzyLookupTOC(
    @QueryMap options: Map<String, String>
  ): Response<BrowseReleaseList>

  /**
   * An [isrc] lookup returns a list of recordings and [options] may contain "inc", "type", and/or
   * "status", which are identical to a [lookupRecording].
   *
   * [ISRC](https://isrc.ifpi.org/)
   *
   * [MusicBrainz ISRC](https://musicbrainz.org/doc/ISRC#Determining_ISRCs_of_recordings)
   */
  @GET("isrc/{isrc}")
  public suspend fun lookupIsrc(
    @Path("isrc") isrc: String,
    @QueryMap options: Map<String, String> = emptyMap()
  ): Response<IsrcRecordingList>

  /**
   * An ISWC lookup returns a list of works, the 'inc=' arguments supported are identical to a
   * lookup request for a work.
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
   * [Example: http://musicbrainz.org/ws/2/artist/?query=artist:%22Led%20Zeppelin%22&fmt=json](http://musicbrainz.org/ws/2/artist/?query=artist:%22Led%20Zeppelin%22&fmt=json)
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
}
