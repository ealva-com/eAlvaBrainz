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

import com.ealva.ealvabrainz.brainz.data.Area
import com.ealva.ealvabrainz.common.AreaName
import com.ealva.ealvabrainz.lucene.SingleTerm
import com.ealva.ealvabrainz.matchers.expect
import com.ealva.ealvabrainz.matchers.toBeAsString
import com.nhaarman.expect.expect
import org.junit.Test
import java.util.Date

public class AreaSearchTest {
  @Test
  public fun `test expected search fields`() {
    /*
    https://musicbrainz.org/doc/Indexed_Search_Syntax#Search_Fields
    aid	the area's MBID
    alias	(part of) any alias attached to the artist (diacritics are ignored)
    area	(part of) the area's name (diacritics are ignored)
    areaaccent	(part of) the area's name (with the specified diacritics)
    begin	the area's begin date (e.g. "1980-01-22")
    comment	(part of) the area's disambiguation comment
    end	the area's end date (e.g. "1980-01-22")
    ended	a boolean flag (true/false) indicating whether or not the area has ended
      (is no longer current)
    iso	an ISO 3166-1, 3166-2 or 3166-3 code attached to the area
    USE ISO iso1	an ISO 3166-1 code attached to the area
    USE ISO iso2	an ISO 3166-2 code attached to the area
    USE ISO iso3	an ISO 3166-3 code attached to the area
    DEPRECATED sortname	equivalent to name (areas no longer have separate sort names)
    tag	(part of) a tag attached to the area
    type the area's type
     */
    val values = Area.SearchField.values()
    val set = values.mapTo(mutableSetOf()) { it.value }
    expect(set).toHaveSize(12)
    expect(set).toContain("")
    expect(set).toContain("aid")
    expect(set).toContain("alias")
    expect(set).toContain("area")
    expect(set).toContain("areaaccent")
    expect(set).toContain("begin")
    expect(set).toContain("comment")
    expect(set).toContain("end")
    expect(set).toContain("ended")
    expect(set).toContain("iso")
    expect(set).toContain("tag")
    expect(set).toContain("type")
  }

  @Test
  public fun `test all term functions cover all fields`() {
    val value = "a"
    val term = SingleTerm(value)
    val search = AreaSearch()
    search.alias { term }
    search.area { term }
    search.areaId { term }
    search.areaAccent { term }
    search.beginDate { term }
    search.comment { term }
    search.default { term }
    search.endDate { term }
    search.ended { term }
    search.iso { term }
    search.tag { term }
    search.type { term }
    var result = search.toString()
    Area.SearchField.values()
      .filterNot { it === Area.SearchField.Default }
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
    expect(AreaSearch().alias { value }).toBeAsString("alias:$value")
    expect(AreaSearch().area { AreaName(value) }).toBeAsString("area:$value")
    expect(AreaSearch().areaAccent { value }).toBeAsString("areaaccent:$value")
    expect(AreaSearch().beginDate { Date(0) }).toBeAsString("begin:\"1969-12-31\"")
    expect(AreaSearch().comment { value }).toBeAsString("comment:$value")
    expect(AreaSearch().default { value }).toBeAsString(value)
    expect(AreaSearch().endDate { Date(0) }).toBeAsString("end:\"1969-12-31\"")
    expect(AreaSearch().ended { true }).toBeAsString("ended:true")
    expect(AreaSearch().iso { value }).toBeAsString("iso:$value")
    expect(AreaSearch().tag { value }).toBeAsString("tag:$value")
    expect(AreaSearch().type { value }).toBeAsString("type:$value")
  }
}
