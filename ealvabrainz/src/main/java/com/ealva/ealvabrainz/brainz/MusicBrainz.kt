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
import com.ealva.ealvabrainz.brainz.data.Label
import com.ealva.ealvabrainz.brainz.data.Recording
import com.ealva.ealvabrainz.brainz.data.RecordingList
import com.ealva.ealvabrainz.brainz.data.Release
import com.ealva.ealvabrainz.brainz.data.ReleaseGroup
import com.ealva.ealvabrainz.brainz.data.ReleaseGroupList
import com.ealva.ealvabrainz.brainz.data.ReleaseList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

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
   * Lookup Area by mbid, example is the ciy "Pärnu"
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
   * @param mbid        the artist mbid. In the example this would be:
   * 5b11f4ce-a62d-471e-81fc-a69a8278c7da
   */
  @GET("artist/{mbid}")
  public suspend fun lookupArtist(
    @Path("mbid") mbid: String,
    @Query("inc") include: String? = null,
    /**
     * Limit linked entities to this Release type. Not valid unless "releases" or "release-groups"
     * are [include]d in the lookup
     *
     * May be "nat", "album", "single", "ep", "compilation", "soundtrack", "spokenword",
     * "interview", "audiobook", "live", "remix", "other" or the default none (null)
     */
    @Query("type") type: String? = null,
    /**
     * Limit linked entities to this Release status. Not valid unless "releases" are [include]d in
     * the lookup
     *
     * May be "official", "promotion", "bootleg", "pseudo-release" or the default none (null)
     */
    @Query("status") status: String? = null
  ): Response<Artist>

  /**
   * An example for looking up a Label (Warner Bros. Records in this example) by mbid is:
   * http://musicbrainz.org/ws/2/label/c595c289-47ce-4fba-b999-b87503e8cb71?inc=ratings+annotation&fmt=json
   *
   * @param mbid the Label mbid, c595c289-47ce-4fba-b999-b87503e8cb71 in above example
   * @param include the list of subqueries, relationships, or other misc includes, to specify how
   * much of the data about the linked entities should be included
   */
  @GET("label/{mbid}")
  public suspend fun lookupLabel(
    @Path("mbid") mbid: String,
    @Query("inc") include: String? = null,
    /**
     * Limit linked entities to this Release type. Not valid unless "releases" or "release-groups"
     * are [include]d in the lookup
     *
     * May be "nat", "album", "single", "ep", "compilation", "soundtrack", "spokenword",
     * "interview", "audiobook", "live", "remix", "other" or the default none (null)
     */
    @Query("type") type: String? = null,
    /**
     * Limit linked entities to this Release status. Not valid unless "releases" are [include]d in
     * the lookup
     *
     * May be "official", "promotion", "bootleg", "pseudo-release" or the default none (null)
     */
    @Query("status") status: String? = null
  ): Response<Label>

  /**
   * An example for lookup by mbid would be:
   * http://musicbrainz.org/ws/2/recording/fcbcdc39-8851-4efc-a02a-ab0e13be224f?fmt=json
   *
   * @param mbid  the recording mbid. In the example this would be:
   * fcbcdc39-8851-4efc-a02a-ab0e13be224f
   * @param include the list of subqueries, relationships, or other misc includes, to specify how
   * much of the data about the linked entities should be included
   * @param type limit linked entities to this Release type
   * @param status limit linked entities to this Release status
   */
  @GET("recording/{mbid}")
  public suspend fun lookupRecording(
    @Path("mbid") mbid: String,
    @Query("inc") include: String? = null,
    /**
     * Limit linked entities to this Release type. Not valid unless "releases" or "release-groups"
     * are [include]d in the lookup
     *
     * May be "nat", "album", "single", "ep", "compilation", "soundtrack", "spokenword",
     * "interview", "audiobook", "live", "remix", "other" or the default none (null)
     */
    @Query("type") type: String? = null,
    /**
     * Limit linked entities to this Release status. Not valid unless "releases" are [include]d in
     * the lookup
     *
     * May be "official", "promotion", "bootleg", "pseudo-release" or the default none (null)
     */
    @Query("status") status: String? = null
  ): Response<Recording>

  /**
   * Lookup by mbid for Release. Example is Led Zeppelin's "Houses of the Holy"
   *
   * [https://musicbrainz.org/ws/2/release/938cef50-de9a-3ced-a1fe-bdfbd3bc4315?fmt=json](https://musicbrainz.org/ws/2/release/938cef50-de9a-3ced-a1fe-bdfbd3bc4315?fmt=json)
   *
   * @param mbid the release mbid. In the example this would be: 938cef50-de9a-3ced-a1fe-bdfbd3bc4315
   * @param include include parameters
   * @param type restrict the result to this type of Release
   * @param status restrict the result to Releases with this status
   * @return the given Release if found
   */
  @GET("release/{mbid}")
  public suspend fun lookupRelease(
    @Path("mbid") mbid: String,
    @Query("inc") include: String? = null,
    /**
     * Limit linked entities to this Release type
     *
     * May be "nat", "album", "single", "ep", "compilation", "soundtrack", "spokenword",
     * "interview", "audiobook", "live", "remix", "other" or the default none (null)
     */
    @Query("type") type: String? = null,
    /**
     * Limit linked entities to this Release status.
     *
     * May be "official", "promotion", "bootleg", "pseudo-release" or the default none (null)
     */
    @Query("status") status: String? = null
  ): Response<Release>

  /**
   * An example for looking up release group The Essential Alice in Chains by mbid would be:
   * https://musicbrainz.org/ws/2/release-group/e1b7e76e-09ff-36fd-b1fd-0c6cb199b817?fmt=json
   *
   * @param mbid        the release group mbid. In the example this would be:
   * e1b7e76e-09ff-36fd-b1fd-0c6cb199b817
   * @param type limit linked entities to this Release type
   * @param status limit linked entities to the Release status, only valid if "releases" are
   * included
   */
  @GET("release-group/{mbid}")
  public suspend fun lookupReleaseGroup(
    @Path("mbid") mbid: String,
    @Query("inc") include: String? = null,
    /**
     * Limit linked entities to this Release type.
     *
     * May be "nat", "album", "single", "ep", "compilation", "soundtrack", "spokenword",
     * "interview", "audiobook", "live", "remix", "other" or the default none (null)
     */
    @Query("type") type: String? = null,
    /**
     * Limit linked entities to this Release status. Not valid unless "releases" are [include]d in
     * the lookup
     *
     * May be "official", "promotion", "bootleg", "pseudo-release" or the default none (null)
     */
    @Query("status") status: String? = null
  ): Response<ReleaseGroup>

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
   * * [areaId]
   * * [recordingId]
   * * [releaseId]
   * * [releaseGroupId]
   * * [workId]
   *
   * are allowed in an invocation. Using more than one ID will generate a malformed URI.
   */
  @GET("artist")
  public suspend fun browseArtists(
    @Query("area") areaId: String? = null,
    @Query("recording") recordingId: String? = null,
    @Query("release") releaseId: String? = null,
    @Query("release-group") releaseGroupId: String? = null,
    @Query("work") workId: String? = null,
    /**
     * Maximum number of artists to return. Default is 25. Use with [offset] used for paging
     * results
     */
    @Query("limit") limit: Int? = null,
    /**
     * Offset at where to start in the total list. Default is 0. Use With [limit] used for paging
     * results
     */
    @Query("offset") offset: Int? = null,
    /** Specify how much data linked entities should contain */
    @Query("inc") include: String? = null,
    /**
     * Limit linked entities to an included Release or Release Group.
     *
     * May be "nat", "album", "single", "ep", "compilation", "soundtrack", "spokenword",
     * "interview", "audiobook", "live", "remix", "other" or the default none (null)
     */
    @Query("type") type: String? = null,
    /**
     * Limit linked entities to this Release status.
     *
     * May be "official", "promotion", "bootleg", "pseudo-release" or the default none (null)
     */
    @Query("status") status: String? = null
  ): Response<BrowseArtistList>

  /**
   * Only one of:
   * * [areaId]
   * * [artistId]
   * * [collectionId]
   * * [labelId]
   * * [trackId]
   * * [trackArtistId]
   * * [recordingId]
   * * [releaseGroupId]
   *
   * are allowed in an invocation. Using more than one ID will generate a malformed URI.
   */
  @GET("release")
  public suspend fun browseReleases(
    @Query("area") areaId: String? = null,
    @Query("artist") artistId: String? = null,
    @Query("collection") collectionId: String? = null,
    @Query("label") labelId: String? = null,
    @Query("track") trackId: String? = null,
    @Query("track_artist") trackArtistId: String? = null,
    @Query("recording") recordingId: String? = null,
    @Query("release-group") releaseGroupId: String? = null,
    /**
     * Maximum number of release groups to return. Default is 25. Use with [offset] used for paging
     * results
     */
    @Query("limit") limit: Int? = null,
    /**
     * Offset at where to start in the total list. Default is 0. Use With [limit] used for paging
     * results
     */
    @Query("offset") offset: Int? = null,
    /** Specify how much data linked entities should contain */
    @Query("inc") include: String? = null,
    /**
     * Limit linked entities to this Release type.
     *
     * May be "nat", "album", "single", "ep", "compilation", "soundtrack", "spokenword",
     * "interview", "audiobook", "live", "remix", "other" or the default none (null)
     */
    @Query("type") type: String? = null,
    /**
     * Limit linked entities to this Release status.
     *
     * May be "official", "promotion", "bootleg", "pseudo-release" or the default none (null)
     */
    @Query("status") status: String? = null
  ): Response<BrowseReleaseList>

  /**
   * Only one of:
   * * [artistId]
   * * [releaseId]
   *
   * are allowed in an invocation. Using more than one ID will generate a malformed URI.
   */
  @GET("release")
  public suspend fun browseReleaseGroups(
    @Query("artist") artistId: String? = null,
    @Query("release") releaseId: String? = null,
    /**
     * Maximum number of releases to return. Default is 25. Use with [offset] used for paging
     * results
     */
    @Query("limit") limit: Int? = null,
    /**
     * Offset at where to start in the total list. Default is 0. Use With [limit] used for paging
     * results
     */
    @Query("offset") offset: Int? = null,
    /** Specify how much data linked entities should contain */
    @Query("inc") include: String? = null,
    /**
     * Limit linked entities to this Release type.
     *
     * May be "nat", "album", "single", "ep", "compilation", "soundtrack", "spokenword",
     * "interview", "audiobook", "live", "remix", "other" or the default none (null)
     */
    @Query("type") type: String? = null,
    /**
     * Limit linked entities to this Release status.
     *
     * May be "official", "promotion", "bootleg", "pseudo-release" or the default none (null)
     */
    @Query("status") status: String? = null
  ): Response<BrowseReleaseGroupList>

  /**
   * Only one of:
   * * [artistId]
   * * [releaseId]
   * * [workId]
   *
   * are allowed in an invocation. Using more than one ID will generate a malformed URI.
   */
  @GET("release")
  public suspend fun browseRecordings(
    @Query("artist") artistId: String? = null,
    @Query("release") releaseId: String? = null,
    @Query("work") workId: String? = null,
    /**
     * Maximum number of recordings to return. Default is 25. Use with [offset] used for paging
     * results
     */
    @Query("limit") limit: Int? = null,
    /**
     * Offset at where to start in the total list. Default is 0. Use With [limit] used for paging
     * results
     */
    @Query("offset") offset: Int? = null,
    /** Specify how much data linked entities should contain */
    @Query("inc") include: String? = null,
    /**
     * Limit linked entities to this Release type.
     *
     * May be "nat", "album", "single", "ep", "compilation", "soundtrack", "spokenword",
     * "interview", "audiobook", "live", "remix", "other" or the default none (null)
     */
    @Query("type") type: String? = null,
    /**
     * Limit linked entities to this Release status.
     *
     * May be "official", "promotion", "bootleg", "pseudo-release" or the default none (null)
     */
    @Query("status") status: String? = null
  ): Response<BrowseRecordingList>
}
