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

import com.ealva.ealvabrainz.brainz.data.Artist
import com.ealva.ealvabrainz.brainz.data.ArtistType
import com.ealva.ealvabrainz.common.AreaName
import com.ealva.ealvabrainz.brainz.data.ArtistMbid
import com.ealva.ealvabrainz.common.ArtistName
import com.ealva.ealvabrainz.lucene.SingleTerm
import com.ealva.ealvabrainz.matchers.expect
import com.ealva.ealvabrainz.matchers.toBeAsString
import com.nhaarman.expect.expect
import org.junit.Test
import java.util.Date

public class ArtistSearchTest {
  @Test
  public fun `test expected search fields`() {
    /*
    https://musicbrainz.org/doc/Indexed_Search_Syntax#Search_Fields_2
    Field	Description
    alias	(part of) any alias attached to the artist (diacritics are ignored)
    primary_alias	(part of) any primary alias attached to the artist (diacritics are ignored)
    area	(part of) the name of the artist's main associated area
    arid	the artist's MBID
    artist	(part of) the artist's name (diacritics are ignored)
    artistaccent	(part of) the artist's name (with the specified diacritics)
    begin	the artist's begin date (e.g. "1980-01-22")
    beginarea	(part of) the name of the artist's begin area
    comment	(part of) the artist's disambiguation comment
    country	the 2-letter code (ISO 3166-1 alpha-2) for the artist's main associated country
    end	the artist's end date (e.g. "1980-01-22")
    endarea	(part of) the name of the artist's end area
    ended	a boolean flag (true/false) indicating whether or not the artist has ended (is
       dissolved/deceased)
    gender	the artist's gender (“male”, “female”, “other” or “not applicable”)
    ipi	an IPI code associated with the artist
    isni	an ISNI code associated with the artist
    sortname	(part of) the artist's sort name
    tag	(part of) a tag attached to the artist
    type	the artist's type (“person”, “group”, etc.)
     */
    val values = Artist.SearchField.values()
    val set = values.mapTo(mutableSetOf()) { it.value }
    expect(set).toHaveSize(20)
    expect(set).toContain("")
    expect(set).toContain("alias")
    expect(set).toContain("primary_alias")
    expect(set).toContain("area")
    expect(set).toContain("arid")
    expect(set).toContain("artist")
    expect(set).toContain("artistaccent")
    expect(set).toContain("begin")
    expect(set).toContain("beginarea")
    expect(set).toContain("comment")
    expect(set).toContain("country")
    expect(set).toContain("end")
    expect(set).toContain("endarea")
    expect(set).toContain("ended")
    expect(set).toContain("gender")
    expect(set).toContain("ipi")
    expect(set).toContain("isni")
    expect(set).toContain("sortname")
    expect(set).toContain("tag")
    expect(set).toContain("type")
  }

  @Test
  public fun `test all term functions cover all fields`() {
    val value = "z"
    val term = SingleTerm(value)
    val search = ArtistSearch()
    search.alias { term }
    search.primaryAlias { term }
    search.area { term }
    search.artist { term }
    search.artistAccent { term }
    search.artistId { term }
    search.beginArea { term }
    search.beginDate { term }
    search.comment { term }
    search.country { term }
    search.default { term }
    search.endArea { term }
    search.endDate { term }
    search.ended { term }
    search.gender { term }
    search.ipi { term }
    search.isni { term }
    search.sortName { term }
    search.tag { term }
    search.type { term }
    var result = search.toString()
    Artist.SearchField.values()
      .filterNot { it === Artist.SearchField.Default }
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
    val mbid = ArtistMbid("5b11f4ce-a62d-471e-81fc-a69a8278c7da")
    expect(ArtistSearch().alias { value }).toBeAsString("alias:$value")
    expect(ArtistSearch().primaryAlias { value }).toBeAsString("primary_alias:$value")
    expect(ArtistSearch().area { AreaName(value) }).toBeAsString("area:$value")
    expect(ArtistSearch().artist { ArtistName(value) }).toBeAsString("artist:$value")
    expect(ArtistSearch().artistAccent { value }).toBeAsString("artistaccent:$value")
    expect(ArtistSearch().artistId { mbid }).toBeAsString("arid:${mbid.value}")
    expect(ArtistSearch().beginArea { value }).toBeAsString("beginarea:$value")
    expect(ArtistSearch().beginDate { Date(0) }).toBeAsString("begin:\"1969-12-31\"")
    expect(ArtistSearch().comment { value }).toBeAsString("comment:$value")
    expect(ArtistSearch().country { value }).toBeAsString("country:$value")
    expect(ArtistSearch().default { value }).toBeAsString(value)
    expect(ArtistSearch().endArea { value }).toBeAsString("endarea:$value")
    expect(ArtistSearch().endDate { Date(0) }).toBeAsString("end:\"1969-12-31\"")
    expect(ArtistSearch().ended { true }).toBeAsString("ended:true")
    expect(ArtistSearch().gender { value }).toBeAsString("gender:$value")
    expect(ArtistSearch().ipi { value }).toBeAsString("ipi:$value")
    expect(ArtistSearch().isni { value }).toBeAsString("isni:$value")
    expect(ArtistSearch().sortName { value }).toBeAsString("sortname:$value")
    expect(ArtistSearch().tag { value }).toBeAsString("tag:$value")
    expect(ArtistSearch().type { ArtistType.Person })
      .toBeAsString("type:${ArtistType.Person.value}")
  }
}
