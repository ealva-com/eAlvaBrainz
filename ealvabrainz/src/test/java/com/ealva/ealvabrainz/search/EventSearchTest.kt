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

import com.ealva.ealvabrainz.brainz.data.Event
import com.ealva.ealvabrainz.brainz.data.AreaMbid
import com.ealva.ealvabrainz.common.AreaName
import com.ealva.ealvabrainz.brainz.data.ArtistMbid
import com.ealva.ealvabrainz.common.ArtistName
import com.ealva.ealvabrainz.brainz.data.EventMbid
import com.ealva.ealvabrainz.common.EventName
import com.ealva.ealvabrainz.brainz.data.PlaceMbid
import com.ealva.ealvabrainz.common.PlaceName
import com.ealva.ealvabrainz.common.toLocalDate
import com.ealva.ealvabrainz.lucene.SingleTerm
import com.ealva.ealvabrainz.matchers.expect
import com.ealva.ealvabrainz.matchers.toBeAsString
import com.nhaarman.expect.expect
import org.junit.Test
import java.util.Date

public class EventSearchTest {
  @Test
  public fun `test expected search fields`() {
    /*
    https://musicbrainz.org/doc/Indexed_Search_Syntax#Search_Fields_3
    Field	Description
    alias	(part of) any alias attached to the artist (diacritics are ignored)
    aid	the MBID of an area related to the event
    area	(part of) the name of an area related to the event
    arid	the MBID of an artist related to the event
    artist	(part of) the name of an artist related to the event
    begin	the event's begin date (e.g. "1980-01-22")
    comment	(part of) the artist's disambiguation comment
    end	the event's end date (e.g. "1980-01-22")
    ended	a boolean flag (true/false) indicating whether or not the event has an end date set
    eid	the MBID of the event
    event	(part of) the event's name (diacritics are ignored)
    eventaccent	(part of) the event's name (with the specified diacritics)
    pid	the MBID of a place related to the event
    place	(part of) the name of a place related to the event
    tag	(part of) a tag attached to the event
    type	the event's type
     */
    val values = Event.SearchField.values()
    val set = values.mapTo(mutableSetOf()) { it.value }
    expect(set).toHaveSize(17)
    expect(set).toContain("")
    expect(set).toContain("alias")
    expect(set).toContain("aid")
    expect(set).toContain("area")
    expect(set).toContain("arid")
    expect(set).toContain("artist")
    expect(set).toContain("begin")
    expect(set).toContain("comment")
    expect(set).toContain("end")
    expect(set).toContain("ended")
    expect(set).toContain("eid")
    expect(set).toContain("event")
    expect(set).toContain("eventaccent")
    expect(set).toContain("pid")
    expect(set).toContain("place")
    expect(set).toContain("tag")
    expect(set).toContain("type")
  }

  @Test
  public fun `test all term functions cover all fields`() {
    val value = "z"
    val term = SingleTerm(value)
    val search = EventSearch()
    search.alias { term }
    search.area { term }
    search.areaId { term }
    search.artist { term }
    search.artistId { term }
    search.beginDate { term }
    search.comment { term }
    search.default { term }
    search.endDate { term }
    search.event { term }
    search.eventAccent { term }
    search.eventId { term }
    search.place { term }
    search.placeId { term }
    search.tag { term }
    search.type { term }
    var result = search.toString()
    Event.SearchField.values()
      .filterNot { it === Event.SearchField.Default || it === Event.SearchField.Ended }
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
    val areaMbid = AreaMbid("5b11f4ce-a62d-471e-81fc-a69a8278c7da")
    val artistMbid = ArtistMbid("5b11f4ce-a62d-471e-81fc-a69a8278c7da")
    val eventMbid = EventMbid("5b11f4ce-a62d-471e-81fc-a69a8278c7da")
    val placeMbid = PlaceMbid("5b11f4ce-a62d-471e-81fc-a69a8278c7da")
    expect(EventSearch().alias(value)).toBeAsString("alias:$value")
    expect(EventSearch().area(AreaName(value))).toBeAsString("area:$value")
    expect(EventSearch().areaId(areaMbid)).toBeAsString("aid:${areaMbid.value}")
    expect(EventSearch().artist(ArtistName(value))).toBeAsString("artist:$value")
    expect(EventSearch().artistId(artistMbid)).toBeAsString("arid:${artistMbid.value}")
    expect(EventSearch().beginDate(Date(0).toLocalDate())).toBeAsString("begin:\"1969-12-31\"")
    expect(EventSearch().comment(value)).toBeAsString("comment:$value")
    expect(EventSearch().default(value)).toBeAsString(value)
    expect(EventSearch().endDate(Date(0).toLocalDate())).toBeAsString("end:\"1969-12-31\"")
    expect(EventSearch().ended(true)).toBeAsString("ended:true")
    expect(EventSearch().event(EventName(value))).toBeAsString("event:$value")
    expect(EventSearch().eventAccent(EventName(value))).toBeAsString("eventaccent:$value")
    expect(EventSearch().eventId(eventMbid)).toBeAsString("eid:${eventMbid.value}")
    expect(EventSearch().place(PlaceName(value))).toBeAsString("place:$value")
    expect(EventSearch().placeId(placeMbid)).toBeAsString("pid:${placeMbid.value}")
    expect(EventSearch().tag(value)).toBeAsString("tag:$value")
    expect(EventSearch().type(value)).toBeAsString("type:$value")
  }
}
