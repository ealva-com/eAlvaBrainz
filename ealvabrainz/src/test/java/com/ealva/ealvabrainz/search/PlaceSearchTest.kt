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

import com.ealva.ealvabrainz.brainz.data.Place
import com.ealva.ealvabrainz.common.PlaceMbid
import com.ealva.ealvabrainz.common.PlaceName
import com.ealva.ealvabrainz.lucene.SingleTerm
import com.ealva.ealvabrainz.matchers.expect
import com.ealva.ealvabrainz.matchers.toBeAsString
import com.nhaarman.expect.expect
import org.junit.Test
import java.util.Date

public class PlaceSearchTest {
  @Test
  public fun `test expected search fields`() {
    /*
    https://musicbrainz.org/doc/Indexed_Search_Syntax#Search_Fields_6
    Field	Description
    address	(part of) the physical address for this place
    alias	(part of) any alias attached to the place (diacritics are ignored)
    area	(part of) the name of the place's main associated area
    begin	the place's begin date (e.g. "1980-01-22")
    comment	(part of) the place's disambiguation comment
    end	the place's end date (e.g. "1980-01-22")
    ended	a boolean flag (true/false) indicating whether or not the place has ended (is closed)
    lat	the (WGS 84) latitude of the place's coordinates (e.g. "58.388226")
    long	the (WGS 84) longitude of the place's coordinates (e.g. "26.702817")
    place	(part of) the place's name (diacritics are ignored)
    placeaccent	(part of) the place's name (with the specified diacritics)
    pid	the place's MBID
    type	the place's type
     */
    val values = Place.SearchField.values()
    val set = values.mapTo(mutableSetOf()) { it.value }
    expect(set).toHaveSize(14)
    expect(set).toContain("")
    expect(set).toContain("address")
    expect(set).toContain("alias")
    expect(set).toContain("area")
    expect(set).toContain("begin")
    expect(set).toContain("comment")
    expect(set).toContain("end")
    expect(set).toContain("ended")
    expect(set).toContain("lat")
    expect(set).toContain("long")
    expect(set).toContain("place")
    expect(set).toContain("placeaccent")
    expect(set).toContain("pid")
    expect(set).toContain("type")
  }

  @Test
  public fun `test all term functions cover all fields`() {
    val value = "a"
    val term = SingleTerm(value)
    val search = PlaceSearch()
    search.address { term }
    search.alias { term }
    search.area { term }
    search.beginDate { term }
    search.comment { term }
    search.default { term }
    search.endDate { term }
    search.ended { term }
    search.latitude { term }
    search.longitude { term }
    search.place { term }
    search.placeAccent { term }
    search.placeId { term }
    search.type { term }
    var result = search.toString()
    Place.SearchField.values()
      .filterNot { it === Place.SearchField.Default }
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
    val mbid = PlaceMbid("5b11f4ce-a62d-471e-81fc-a69a8278c7da")
    expect(PlaceSearch().address { value }).toBeAsString("address:$value")
    expect(PlaceSearch().alias { value }).toBeAsString("alias:$value")
    expect(PlaceSearch().area { value }).toBeAsString("area:$value")
    expect(PlaceSearch().beginDate { Date(0) }).toBeAsString("begin:1969\\-12\\-31")
    expect(PlaceSearch().comment { value }).toBeAsString("comment:$value")
    expect(PlaceSearch().default { value }).toBeAsString(value)
    expect(PlaceSearch().endDate { Date(0) }).toBeAsString("end:1969\\-12\\-31")
    expect(PlaceSearch().ended { false }).toBeAsString("ended:false")
    expect(PlaceSearch().latitude { value }).toBeAsString("lat:$value")
    expect(PlaceSearch().longitude { value }).toBeAsString("long:$value")
    expect(PlaceSearch().place { PlaceName(value) }).toBeAsString("place:$value")
    expect(PlaceSearch().placeAccent { PlaceName(value) }).toBeAsString("placeaccent:$value")
    expect(PlaceSearch().placeId { mbid }).toBeAsString("pid:${mbid.value}")
    expect(PlaceSearch().type { value }).toBeAsString("type:$value")
  }
}
