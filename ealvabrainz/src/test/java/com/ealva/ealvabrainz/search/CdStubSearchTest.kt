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

import com.ealva.ealvabrainz.brainz.data.CdStub
import com.ealva.ealvabrainz.common.AlbumTitle
import com.ealva.ealvabrainz.common.ArtistName
import com.ealva.ealvabrainz.common.DiscId
import com.ealva.ealvabrainz.lucene.SingleTerm
import com.ealva.ealvabrainz.matchers.expect
import com.ealva.ealvabrainz.matchers.toBeAsString
import com.nhaarman.expect.expect
import org.junit.Test
import java.util.Date

public class CdStubSearchTest {
  @Test
  public fun `test expected search fields`() {
    /*
    https://musicbrainz.org/doc/Indexed_Search_Syntax#Search_Fields_14
    Field	Description
    added	the date the CD stub was added (e.g. "2020-01-22")
    artist	(part of) the artist name set on the CD stub
    barcode	the barcode set on the CD stub
    comment	(part of) the comment set on the CD stub
    discid	the CD stub's Disc ID
    title	(part of) the release title set on the CD stub
    tracks	the number of tracks on the CD stub 
     */
    val values = CdStub.SearchField.values()
    val set = values.mapTo(mutableSetOf()) { it.value }
    expect(set).toHaveSize(8)
    expect(set).toContain("")
    expect(set).toContain("added")
    expect(set).toContain("artist")
    expect(set).toContain("barcode")
    expect(set).toContain("comment")
    expect(set).toContain("discid")
    expect(set).toContain("title")
    expect(set).toContain("tracks")
  }

  @Test
  public fun `test all term functions cover all fields`() {
    val value = "a"
    val term = SingleTerm(value)
    val search = CdStubSearch()
    search.added { term }
    search.artist { term }
    search.barcode { term }
    search.comment { term }
    search.default { term }
    search.discId { term }
    search.title { term }
    search.trackCount { term }
    var result = search.toString()
    CdStub.SearchField.values()
      .filterNot { it === CdStub.SearchField.Default }
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
    expect(CdStubSearch().added { Date(0) }).toBeAsString("added:\"1969-12-31\"")
    expect(CdStubSearch().artist { ArtistName(value) }).toBeAsString("artist:$value")
    expect(CdStubSearch().barcode { value }).toBeAsString("barcode:$value")
    expect(CdStubSearch().comment { value }).toBeAsString("comment:$value")
    expect(CdStubSearch().default { value }).toBeAsString(value)
    expect(CdStubSearch().discId { DiscId(value) }).toBeAsString("discid:$value")
    expect(CdStubSearch().title { AlbumTitle(value) }).toBeAsString("title:$value")
    expect(CdStubSearch().trackCount { 10 }).toBeAsString("tracks:10")
  }
}
