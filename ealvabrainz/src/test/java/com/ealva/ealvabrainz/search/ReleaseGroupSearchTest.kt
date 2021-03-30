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
import com.ealva.ealvabrainz.brainz.data.ReleaseGroup
import com.ealva.ealvabrainz.common.AlbumTitle
import com.ealva.ealvabrainz.common.ArtistMbid
import com.ealva.ealvabrainz.common.ArtistName
import com.ealva.ealvabrainz.common.ReleaseGroupMbid
import com.ealva.ealvabrainz.common.ReleaseMbid
import com.ealva.ealvabrainz.lucene.SingleTerm
import com.ealva.ealvabrainz.matchers.expect
import com.ealva.ealvabrainz.matchers.toBeAsString
import com.nhaarman.expect.expect
import org.junit.Test
import java.util.Date

public class ReleaseGroupSearchTest {
  @Test
  public fun `test expected search fields`() {
    /*
    https://musicbrainz.org/doc/Indexed_Search_Syntax#Search_Fields_9
    Field	Description
    alias	(part of) any alias attached to the release group (diacritics are ignored)
    arid	the MBID of any of the release group artists
    artist	(part of) the combined credited artist name for the release group, including join
        phrases (e.g. "Artist X feat.")
    artistname	(part of) the name of any of the release group artists
    comment	(part of) the release group's disambiguation comment
    creditname	(part of) the credited name of any of the release group artists on this particular
        release group
    firstreleasedate	the release date of the earliest release in this release group (e.g.
        "1980-01-22")
    primarytype	the primary type of the release group
    reid	the MBID of any of the releases in the release group
    release	(part of) the title of any of the releases in the release group
    releasegroup	(part of) the release group's title (diacritics are ignored)
    releasegroupaccent	(part of) the release group's title (with the specified diacritics)
    releases	the number of releases in the release group
    rgid	the release group's MBID
    secondarytype	any of the secondary types of the release group
    status	the status of any of the releases in the release group
    tag	(part of) a tag attached to the release group
    DEPRECATED type	legacy release group type field that predates the ability to set multiple types
     */
    val values = ReleaseGroup.SearchField.values()
    val set = values.mapTo(mutableSetOf()) { it.value }
    expect(set).toHaveSize(18)
    expect(set).toContain("")
    expect(set).toContain("alias")
    expect(set).toContain("arid")
    expect(set).toContain("artist")
    expect(set).toContain("artistname")
    expect(set).toContain("comment")
    expect(set).toContain("creditname")
    expect(set).toContain("firstreleasedate")
    expect(set).toContain("primarytype")
    expect(set).toContain("reid")
    expect(set).toContain("release")
    expect(set).toContain("releasegroup")
    expect(set).toContain("releasegroupaccent")
    expect(set).toContain("releases")
    expect(set).toContain("rgid")
    expect(set).toContain("secondarytype")
    expect(set).toContain("status")
    expect(set).toContain("tag")
  }

  @Test
  public fun `test all term functions cover all fields`() {
    val value = "z"
    val term = SingleTerm(value)
    val search = ReleaseGroupSearch()
    search.alias { term }
    search.artist { term }
    search.artistId { term }
    search.artistName { term }
    search.comment { term }
    search.creditName { term }
    search.default { term }
    search.firstReleaseDate { term }
    search.primaryType { term }
    search.release { term }
    search.releaseCount { term }
    search.releaseId { term }
    search.releaseGroup { term }
    search.releaseGroupAccentedName { term }
    search.releaseGroupId { term }
    search.secondaryType { term }
    search.status { term }
    search.tag { term }
    var result = search.toString()
    ReleaseGroup.SearchField.values()
      .filterNot { it === ReleaseGroup.SearchField.Default }
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
    val artistMbid = ArtistMbid("5b11f4ce-a62d-471e-81fc-a69a8278c7da")
    expect(ReleaseGroupSearch().alias { value }).toBeAsString("alias:$value")
    expect(ReleaseGroupSearch().artist { ArtistName(value) }).toBeAsString("artist:$value")
    expect(ReleaseGroupSearch().artistId { artistMbid }).toBeAsString("arid:${artistMbid.value}")
    expect(ReleaseGroupSearch().artistName { ArtistName(value) }).toBeAsString("artistname:$value")
    expect(ReleaseGroupSearch().comment { value }).toBeAsString("comment:$value")
    expect(ReleaseGroupSearch().creditName { ArtistName(value) }).toBeAsString("creditname:$value")
    expect(ReleaseGroupSearch().default { AlbumTitle(value) }).toBeAsString(value)
    expect(ReleaseGroupSearch().firstReleaseDate { Date(0) })
      .toBeAsString("firstreleasedate:1969\\-12\\-31")
    expect(ReleaseGroupSearch().primaryType { Release.Type.Ep }).toBeAsString("primarytype:ep")
    expect(ReleaseGroupSearch().release { AlbumTitle(value) }).toBeAsString("release:$value")
    expect(ReleaseGroupSearch().releaseCount { 69 }).toBeAsString("releases:69")
    expect(ReleaseGroupSearch().releaseId { releaseMbid }).toBeAsString("reid:${releaseMbid.value}")
    expect(ReleaseGroupSearch().releaseGroup { AlbumTitle(value) })
      .toBeAsString("releasegroup:$value")
    expect(ReleaseGroupSearch().releaseGroupAccentedName { value })
      .toBeAsString("releasegroupaccent:$value")
    expect(ReleaseGroupSearch().releaseGroupId { releaseGroupMbid })
      .toBeAsString("rgid:${releaseGroupMbid.value}")
    expect(ReleaseGroupSearch().secondaryType { Release.Type.Album })
      .toBeAsString("secondarytype:album")
    expect(ReleaseGroupSearch().status { Release.Status.Bootleg }).toBeAsString("status:bootleg")
    expect(ReleaseGroupSearch().tag { value }).toBeAsString("tag:$value")
  }
}
