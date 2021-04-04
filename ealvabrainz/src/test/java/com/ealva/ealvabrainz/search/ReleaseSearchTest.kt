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

package com.ealva.ealvabrainz.search

import com.ealva.ealvabrainz.brainz.data.Release
import com.ealva.ealvabrainz.common.AlbumTitle
import com.ealva.ealvabrainz.brainz.data.ArtistMbid
import com.ealva.ealvabrainz.common.ArtistName
import com.ealva.ealvabrainz.brainz.data.LabelMbid
import com.ealva.ealvabrainz.common.LabelName
import com.ealva.ealvabrainz.brainz.data.ReleaseGroupMbid
import com.ealva.ealvabrainz.brainz.data.ReleaseMbid
import com.ealva.ealvabrainz.lucene.SingleTerm
import com.ealva.ealvabrainz.matchers.toBeAsString
import com.nhaarman.expect.expect
import org.junit.Test

public class ReleaseSearchTest {
  @Test
  public fun `test expected search fields`() {
    /*
    https://musicbrainz.org/doc/Indexed_Search_Syntax#Search_Fields_8
    Field	Description
    alias	(part of) any alias attached to the release (diacritics are ignored)
    arid	the MBID of any of the release artists
    artist	(part of) the combined credited artist name for the release, including join phrases
        (e.g. "Artist X feat.")
    artistname	(part of) the name of any of the release artists
    asin	an Amazon ASIN for the release
    barcode	the barcode for the release
    catno	any catalog number for this release (insensitive to case, spaces, and separators)
    comment	(part of) the release's disambiguation comment
    country	the 2-letter code (ISO 3166-1 alpha-2) for any country the release was released in
    creditname	(part of) the credited name of any of the release artists on this particular release
    date	a release date for the release (e.g. "1980-01-22")
    discids	the total number of disc IDs attached to all mediums on the release
    discidsmedium	the number of disc IDs attached to any one medium on the release
    format	the format of any medium in the release (insensitive to case, spaces, and separators)
    laid	the MBID of any of the release labels
    label	(part of) the name of any of the release labels
    lang	the ISO 639-3 code for the release language
    mediums	the number of mediums on the release
    packaging	the format of the release (insensitive to case, spaces, and separators)
    primarytype	the primary type of the release group for this release
    quality	the listed quality of the data for the release (one of "low", "normal", "high")
    reid	the release's MBID
    release	(part of) the release's title (diacritics are ignored)
    releaseaccent	(part of) the release's title (with the specified diacritics)
    rgid	the MBID of the release group for this release
    script	the ISO 15924 code for the release script
    secondarytype	any of the secondary types of the release group for this release
    status	the status of the release
    tag	(part of) a tag attached to the release
    tracks	the total number of tracks on the release
    tracksmedium	the number of tracks on any one medium on the release
    DEPRECATED type	legacy release group type field that predates the ability to set multiple types
     */
    val values = Release.SearchField.values()
    val set = values.mapTo(mutableSetOf()) { it.value }
    com.ealva.ealvabrainz.matchers.expect(set).toHaveSize(32)
    com.ealva.ealvabrainz.matchers.expect(set).toContain("")
    com.ealva.ealvabrainz.matchers.expect(set).toContain("alias")
    com.ealva.ealvabrainz.matchers.expect(set).toContain("arid")
    com.ealva.ealvabrainz.matchers.expect(set).toContain("artist")
    com.ealva.ealvabrainz.matchers.expect(set).toContain("artistname")
    com.ealva.ealvabrainz.matchers.expect(set).toContain("asin")
    com.ealva.ealvabrainz.matchers.expect(set).toContain("barcode")
    com.ealva.ealvabrainz.matchers.expect(set).toContain("catno")
    com.ealva.ealvabrainz.matchers.expect(set).toContain("comment")
    com.ealva.ealvabrainz.matchers.expect(set).toContain("country")
    com.ealva.ealvabrainz.matchers.expect(set).toContain("creditname")
    com.ealva.ealvabrainz.matchers.expect(set).toContain("date")
    com.ealva.ealvabrainz.matchers.expect(set).toContain("discids")
    com.ealva.ealvabrainz.matchers.expect(set).toContain("discidsmedium")
    com.ealva.ealvabrainz.matchers.expect(set).toContain("format")
    com.ealva.ealvabrainz.matchers.expect(set).toContain("laid")
    com.ealva.ealvabrainz.matchers.expect(set).toContain("label")
    com.ealva.ealvabrainz.matchers.expect(set).toContain("lang")
    com.ealva.ealvabrainz.matchers.expect(set).toContain("mediums")
    com.ealva.ealvabrainz.matchers.expect(set).toContain("packaging")
    com.ealva.ealvabrainz.matchers.expect(set).toContain("primarytype")
    com.ealva.ealvabrainz.matchers.expect(set).toContain("quality")
    com.ealva.ealvabrainz.matchers.expect(set).toContain("reid")
    com.ealva.ealvabrainz.matchers.expect(set).toContain("release")
    com.ealva.ealvabrainz.matchers.expect(set).toContain("releaseaccent")
    com.ealva.ealvabrainz.matchers.expect(set).toContain("rgid")
    com.ealva.ealvabrainz.matchers.expect(set).toContain("script")
    com.ealva.ealvabrainz.matchers.expect(set).toContain("secondarytype")
    com.ealva.ealvabrainz.matchers.expect(set).toContain("status")
    com.ealva.ealvabrainz.matchers.expect(set).toContain("tag")
    com.ealva.ealvabrainz.matchers.expect(set).toContain("tracks")
  }

  @Test
  public fun `test all term functions cover all fields`() {
    val value = "z"
    val term = SingleTerm(value)
    val search = ReleaseSearch()
    search.alias { term }
    search.artist { term }
    search.artistId { term }
    search.artistName { term }
    search.asin { term }
    search.barcode { term }
    search.catalogNumber { term }
    search.comment { term }
    search.country { term }
    search.creditName { term }
    search.date { term }
    search.default { term }
    search.discIdCount { term }
    search.format { term }
    search.label { term }
    search.labelId { term }
    search.language { term }
    search.mediumCount { term }
    search.mediumDiscCount { term }
    search.mediumTrackCount { term }
    search.packaging { term }
    search.primaryType { term }
    search.quality { term }
    search.release { term }
    search.releaseAccentedName { term }
    search.releaseId { term }
    search.releaseGroupId { term }
    search.script { term }
    search.secondaryType { term }
    search.status { term }
    search.tag { term }
    search.trackCount { term }
    var result = search.toString()
    Release.SearchField.values()
      .filterNot { it === Release.SearchField.Default }
      .forEach { searchField ->
        val expected = "${searchField.value}:$value"
        expect(result).toContain(expected)
        result = result.replaceFirst(expected, "")
      }
    expect(result.trim()).toBe(value)
  }

  @Test
  public fun `test all non-term functions add expected field`() {
    val value = "a"
    val releaseMbid = ReleaseMbid("5b11f4ce-a62d-471e-81fc-a69a8278c7da")
    val releaseGroupMbid = ReleaseGroupMbid("5b11f4ce-a62d-471e-81fc-a69a8278c7da")
    val labelMbid = LabelMbid("5b11f4ce-a62d-471e-81fc-a69a8278c7da")
    val artistMbid = ArtistMbid("5b11f4ce-a62d-471e-81fc-a69a8278c7da")
    expect(ReleaseSearch().alias { value }).toBeAsString("alias:$value")
    expect(ReleaseSearch().artist { ArtistName(value) }).toBeAsString("artist:$value")
    expect(ReleaseSearch().artistId { artistMbid }).toBeAsString("arid:${artistMbid.value}")
    expect(ReleaseSearch().artistName { ArtistName(value) }).toBeAsString("artistname:$value")
    expect(ReleaseSearch().asin { value }).toBeAsString("asin:$value")
    expect(ReleaseSearch().barcode { value }).toBeAsString("barcode:$value")
    expect(ReleaseSearch().catalogNumber { value }).toBeAsString("catno:$value")
    expect(ReleaseSearch().comment { value }).toBeAsString("comment:$value")
    expect(ReleaseSearch().country { value }).toBeAsString("country:$value")
    expect(ReleaseSearch().creditName { ArtistName(value) }).toBeAsString("creditname:$value")
    expect(ReleaseSearch().date { value }).toBeAsString("date:$value")
    expect(ReleaseSearch().default { AlbumTitle(value) }).toBeAsString(value)
    expect(ReleaseSearch().discIdCount { 2 }).toBeAsString("discids:2")
    expect(ReleaseSearch().format { value }).toBeAsString("format:$value")
    expect(ReleaseSearch().label { LabelName(value) }).toBeAsString("label:$value")
    expect(ReleaseSearch().labelId { labelMbid }).toBeAsString("laid:${labelMbid.value}")
    expect(ReleaseSearch().language { value }).toBeAsString("lang:$value")
    expect(ReleaseSearch().mediumCount { 5 }).toBeAsString("mediums:5")
    expect(ReleaseSearch().mediumDiscCount { 6 }).toBeAsString("discidsmedium:6")
    expect(ReleaseSearch().mediumTrackCount { 10 }).toBeAsString("tracksmedium:10")
    expect(ReleaseSearch().packaging { value }).toBeAsString("packaging:$value")
    expect(ReleaseSearch().primaryType { value }).toBeAsString("primarytype:$value")
    expect(ReleaseSearch().quality { value }).toBeAsString("quality:$value")
    expect(ReleaseSearch().release { AlbumTitle(value) }).toBeAsString("release:$value")
    expect(ReleaseSearch().releaseAccentedName { value }).toBeAsString("releaseaccent:$value")
    expect(ReleaseSearch().releaseId { releaseMbid }).toBeAsString("reid:${releaseMbid.value}")
    expect(ReleaseSearch().releaseGroupId { releaseGroupMbid })
      .toBeAsString("rgid:${releaseGroupMbid.value}")
    expect(ReleaseSearch().script { value }).toBeAsString("script:$value")
    expect(ReleaseSearch().secondaryType { value }).toBeAsString("secondarytype:$value")
    expect(ReleaseSearch().status { Release.Status.Official }).toBeAsString("status:official")
    expect(ReleaseSearch().tag { value }).toBeAsString("tag:$value")
    expect(ReleaseSearch().trackCount { 11 }).toBeAsString("tracks:11")
  }
}
