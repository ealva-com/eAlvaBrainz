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

import com.ealva.ealvabrainz.brainz.data.Artist
import com.ealva.ealvabrainz.brainz.data.ArtistList
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
 * Be sure to read [MusicBrainz requirements]
 * (https://musicbrainz.org/doc/XML_Web_Service/Rate_Limiting#Provide_meaningful_User-Agent_strings)
 * for querying their servers.
 *
 * MusicBrainz query [documentation](https://musicbrainz.org/doc/Development/XML_Web_Service/Version_2/Search)
 *
 * [Lucene Query syntax](https://lucene.apache.org/core/7_7_2/queryparser/org/apache/lucene/queryparser/classic/package-summary.html#package.description)
 */
public interface MusicBrainz {
  /**
   * Returns a [ReleaseList] given [query] which is the fully formed MusicBrainz query (in the
   * example this is: release:"Houses of the Holy" AND artist:"Led Zeppelin" . Optional [limit] is
   * the max number of releases returned and must be 1-100 inclusive, defaults to 25 if not
   * specified. Optional [offset] is used to page results. The count and offset are both
   * returned in the resulting ReleaseList
   *
   * [Example: http://musicbrainz.org/ws/2/release/?query=release:Houses%20of%20the%20Holy%20AND%20artist:Led%20Zeppelin&fmt=json](http://musicbrainz.org/ws/2/release/?query=release:Houses%20of%20the%20Holy%20AND%20artist:Led%20Zeppelin&fmt=json)
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
   * example this is: release:"artist:"Led Zeppelin". Optional [limit] is
   * the max number of artists returned and must be 1-100 inclusive, defaults to 25 if not
   * specified. Optional [offset] is used to page results. The count and offset are both
   * returned in the resulting ReleaseList
   *
   * [Example: http://musicbrainz.org/ws/2/release/?query=release:Houses%20of%20the%20Holy%20AND%20artist:Led%20Zeppelin&fmt=json](http://musicbrainz.org/ws/2/release/?query=release:Houses%20of%20the%20Holy%20AND%20artist:Led%20Zeppelin&fmt=json)
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
   * Browse albums (Release Groups) for a given artist with [artistId], starting at [offset] with
   * a [limit] to page results. As an example, Black Sabbath currently has 176 related
   * Release Groups, which include albums, compilations, EPs, singles, etc.
   *
   * Results are sorted by id, so any proper sorting requires paging though all items and then
   * performing a sort.
   *
   * Example for browsing Black Sabbath albums starting at offset 100 and
   * requesting the maximum limit of 100. In this case artist="Black Sabbath MBID", ie.
   * "5182c1d9-c7d2-4dad-afa0-ccfeada921a"
   *
   * [http://musicbrainz.org/ws/2/release-group?artist=5182c1d9-c7d2-4dad-afa0-ccfeada921a8&limit=100&offset=100&fmt=json]
   *
   * @param artistId the artist mbid
   * @param limit max entries returned, required, maximum 100
   * @param offset offset into total list, required
   * @param type limit the results to a particular release type(s), optional
   */
  @GET("release-group")
  public suspend fun browseArtistReleaseGroups(
    /**
     * The artist MBID to search for
     */
    @Query("artist") artistId: String,
    /**
     * Maximum number of release groups to return. With [offset] used for paging results
     */
    @Query("limit") limit: Int = 25,
    /**
     * Offset at where to start in the total list. With [limit] used for paging results
     */
    @Query("offset") offset: Int = 0,
    /** Specify how much data linked entities should contain */
    @Query("inc") include: String? = null,
    /**
     * Limit linked entities to this Release type.
     *
     * May be "nat", "album", "single", "ep", "compilation", "soundtrack", "spokenword",
     * "interview", "audiobook", "live", "remix", "other" or the default none (null)
     */
    @Query("type") type: String? = null
  ): Response<BrowseReleaseGroupList>

  /**
   * Browse Releases for a given entity (one or more of [artistId], [labelId], [releaseGroupId])
   * starting at [offset] with a [limit] to page results.
   *
   * @param artistId the artist mbid, optional
   * @param labelId the label mbid, optional
   * @param releaseGroupId the release group mbid, optional
   * @param limit max entries returned, required, maximum 100
   * @param offset offset into total list, required
   * @param include include linked entity data
   * @param type limit the results to a particular release type(s), optional
   * @param status limit the results to a particular release status, optional
   */
  @GET("release")
  public suspend fun browseReleases(
    /**
     * Browse releases for the given artist MBID
     */
    @Query("artist") artistId: String? = null,
    /**
     * Browse releases for the given label MBID
     */
    @Query("label") labelId: String? = null,
    /**
     * Browse releases for the given release group MBID
     */
    @Query("release-group") releaseGroupId: String? = null,
    /**
     * Maximum number of release groups to return. With [offset] used for paging results
     */
    @Query("limit") limit: Int = 25,
    /**
     * Offset at where to start in the total list. With [limit] used for paging results
     */
    @Query("offset") offset: Int = 0,
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
    @Query("inc") include: String? = null
  ): Response<Label>
}
