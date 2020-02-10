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
import com.ealva.ealvabrainz.brainz.data.Release
import com.ealva.ealvabrainz.brainz.data.ReleaseGroup
import com.ealva.ealvabrainz.brainz.data.ReleaseGroupList
import com.ealva.ealvabrainz.brainz.data.ReleaseList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * MusicBrainz Retrofit interface
 *
 * Be sure to read [MusicBrainz requirements]
 * (https://musicbrainz.org/doc/XML_Web_Service/Rate_Limiting#Provide_meaningful_User-Agent_strings)
 * for querying their servers.
 */
interface MusicBrainz {
  /**
   * Returns a [ReleaseList] given [query] which is the fully formed MusicBrainz query (in the
   * example this is: release:"Houses of the Holy" AND artist:"Led Zeppelin" . Optional [limit] is
   * the max number of releases returned and must be 1-100 inclusive, defaults to 25 if not
   * specified. Optional [offset] is used to page results. The count and offset are both
   * returned in the resulting ReleaseList
   *
   * [Example: http://musicbrainz.org/ws/2/release/?query=release:Houses%20of%20the%20Holy%20AND%20artist:Led%20Zeppelin&fmt=json](http://musicbrainz.org/ws/2/release/?query=release:Houses%20of%20the%20Holy%20AND%20artist:Led%20Zeppelin&fmt=json)
   *
   * [Lucene Query syntax](https://lucene.apache.org/core/7_7_2/queryparser/org/apache/lucene/queryparser/classic/package-summary.html#package.description)
   *
   * @param query full MusicBrainz lucene query
   * @param limit 1..100 inclusive are valid. If skipped, defaults to 25
   * @param offset specifies starting offset into list, used to page results. Defaults to 0
   */
  @GET("release")
  suspend fun findRelease(
    @Query("query") query: String,
    @Query("limit") limit: Int? = null,
    @Query("offset") offset: Int? = null
  ): Response<ReleaseList>

  /**
   * Lookup by mbid for Release. Example is Led Zeppelin's "Houses of the Holy"
   *
   * [https://musicbrainz.org/ws/2/release/938cef50-de9a-3ced-a1fe-bdfbd3bc4315?fmt=json](https://musicbrainz.org/ws/2/release/938cef50-de9a-3ced-a1fe-bdfbd3bc4315?fmt=json)
   *
   * [MusicBrainz Lookup Docs](https://musicbrainz.org/doc/Development/JSON_Web_Service)
   *
   * @param mbid the release mbid. In the example this would be: 938cef50-de9a-3ced-a1fe-bdfbd3bc4315
   * @param include include parameters
   * @return the given Release if found
   */
  @GET("release/{mbid}")
  suspend fun lookupRelease(
    @Path("mbid") mbid: String,
    @Query("inc") include: String? = "recordings+artist-credits"
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
   * [Lucene Query syntax](https://lucene.apache.org/core/7_7_2/queryparser/org/apache/lucene/queryparser/classic/package-summary.html#package.description)
   *
   * @param query full MusicBrainz lucene query
   * @param limit 1..100 inclusive are valid. If skipped, defaults to 25
   * @param offset specifies starting offset into list, used to page results. Defaults to 0
   */
  @GET("release-group")
  suspend fun findReleaseGroup(
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
   *
   * @return the ReleaseGroup if found
   */
  @GET("release-group/{mbid}")
  suspend fun lookupReleaseGroup(
    @Path("mbid") mbid: String,
    @Query("inc") include: String? = "artist-credits+releases"
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
   * [Lucene Query syntax](https://lucene.apache.org/core/7_7_2/queryparser/org/apache/lucene/queryparser/classic/package-summary.html#package.description)
   *
   * @param query full MusicBrainz lucene query
   * @param limit 1..100 inclusive are valid. If skipped, defaults to 25
   * @param offset specifies starting offset into list, used to page results. Defaults to 0
   */
  @GET("artist")
  suspend fun findArtist(
    @Query("query") query: String,
    @Query("limit") limit: Int? = null,
    @Query("offset") offset: Int? = null
  ): Response<ArtistList>

  /**
   * An example for looking up Nirvana by mbid would be:
   * http://musicbrainz.org/ws/2/artist/5b11f4ce-a62d-471e-81fc-a69a8278c7da?fmt=json
   *
   * @param mbid        the artist mbid. In the example this would be:
   * 5b11f4ce-a62d-471e-81fc-a69a8278c7da
   *
   * @return Artist
   */
  @GET("artist/{mbid}")
  suspend fun lookupArtist(
    @Path("mbid") mbid: String,
    @Query("inc") include: String? = "aliases+tags+ratings"
  ): Response<Artist>

}
