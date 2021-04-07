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

import com.ealva.ealvabrainz.brainz.data.Series
import com.ealva.ealvabrainz.brainz.data.SeriesMbid
import com.ealva.ealvabrainz.common.SeriesName
import com.ealva.ealvabrainz.lucene.SingleTerm
import com.ealva.ealvabrainz.matchers.expect
import com.ealva.ealvabrainz.matchers.toBeAsString
import com.nhaarman.expect.expect
import org.junit.Test

public class SeriesSearchTest {
  @Test
  public fun `test expected search fields`() {
    /*
    https://musicbrainz.org/doc/Indexed_Search_Syntax#Search_Fields_10
    Field	Description
    alias	(part of) any alias attached to the series (diacritics are ignored)
    comment	(part of) the series' disambiguation comment
    series	(part of) the series' name (diacritics are ignored)
    seriesaccent	(part of) the series' name (with the specified diacritics)
    sid	the series' MBID
    tag	(part of) a tag attached to the series
    type	the series' type
    */
    val values = Series.SearchField.values()
    val set = values.mapTo(mutableSetOf()) { it.value }
    expect(set).toHaveSize(8)
    expect(set).toContain("")
    expect(set).toContain("alias")
    expect(set).toContain("comment")
    expect(set).toContain("series")
    expect(set).toContain("seriesaccent")
    expect(set).toContain("sid")
    expect(set).toContain("tag")
    expect(set).toContain("type")
  }

  @Test
  public fun `test all term functions cover all fields`() {
    val value = "a"
    val term = SingleTerm(value)
    val search = SeriesSearch()
    search.alias { term }
    search.comment { term }
    search.default { term }
    search.series { term }
    search.seriesId { term }
    search.seriesAccent { term }
    search.tag { term }
    search.type { term }
    var result = search.toString()
    Series.SearchField.values()
      .filterNot { it === Series.SearchField.Default }
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
    val mbid = SeriesMbid("5b11f4ce-a62d-471e-81fc-a69a8278c7da")
    expect(SeriesSearch().alias(value)).toBeAsString("alias:$value")
    expect(SeriesSearch().comment(value)).toBeAsString("comment:$value")
    expect(SeriesSearch().default(value)).toBeAsString(value)
    expect(SeriesSearch().series(SeriesName(value))).toBeAsString("series:$value")
    expect(SeriesSearch().seriesId(mbid)).toBeAsString("sid:${mbid.value}")
    expect(SeriesSearch().seriesAccent(SeriesName(value))).toBeAsString("seriesaccent:$value")
    expect(SeriesSearch().tag(value)).toBeAsString("tag:$value")
    expect(SeriesSearch().type(value)).toBeAsString("type:$value")
  }
}
