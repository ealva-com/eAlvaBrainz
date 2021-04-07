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

import com.ealva.ealvabrainz.brainz.data.Instrument
import com.ealva.ealvabrainz.brainz.data.InstrumentMbid
import com.ealva.ealvabrainz.common.InstrumentName
import com.ealva.ealvabrainz.lucene.SingleTerm
import com.ealva.ealvabrainz.matchers.expect
import com.ealva.ealvabrainz.matchers.toBeAsString
import com.nhaarman.expect.expect
import org.junit.Test

public class InstrumentSearchTest {
  @Test
  public fun `test expected search fields`() {
    /*
    https://musicbrainz.org/doc/Indexed_Search_Syntax#Search_Fields_4
    Field	Description
    alias	(part of) any alias attached to the instrument (diacritics are ignored)
    comment	(part of) the instrument's disambiguation comment
    description	(part of) the description of the instrument (in English)
    iid	the MBID of the instrument
    instrument	(part of) the instrument's name (diacritics are ignored)
    instrumentaccent	(part of) the instrument's name (with the specified diacritics)
    tag	(part of) a tag attached to the instrument
    type	the instrument's type
    */
    val values = Instrument.SearchField.values()
    val set = values.mapTo(mutableSetOf()) { it.value }
    expect(set).toHaveSize(9)
    expect(set).toContain("")
    expect(set).toContain("alias")
    expect(set).toContain("comment")
    expect(set).toContain("description")
    expect(set).toContain("iid")
    expect(set).toContain("instrument")
    expect(set).toContain("instrumentaccent")
    expect(set).toContain("tag")
    expect(set).toContain("type")
  }

  @Test
  public fun `test all term functions cover all fields`() {
    val value = "a"
    val term = SingleTerm(value)
    val search = InstrumentSearch()
    search.alias { term }
    search.comment { term }
    search.default { term }
    search.description { term }
    search.instrumentId { term }
    search.instrument { term }
    search.instrumentAccent { term }
    search.tag { term }
    search.type { term }
    var result = search.toString()
    Instrument.SearchField.values()
      .filterNot { it === Instrument.SearchField.Default }
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
    val instrumentId = InstrumentMbid("5b11f4ce-a62d-471e-81fc-a69a8278c7da")
    expect(InstrumentSearch().alias(value)).toBeAsString("alias:$value")
    expect(InstrumentSearch().comment(value)).toBeAsString("comment:$value")
    expect(InstrumentSearch().default(value)).toBeAsString(value)
    expect(InstrumentSearch().description(value)).toBeAsString("description:$value")
    expect(InstrumentSearch().instrumentId(instrumentId))
      .toBeAsString("iid:${instrumentId.value}")
    expect(InstrumentSearch().instrument(InstrumentName(value)))
      .toBeAsString("instrument:$value")
    expect(InstrumentSearch().instrumentAccent(value)).toBeAsString("instrumentaccent:$value")
    expect(InstrumentSearch().tag(value)).toBeAsString("tag:$value")
    expect(InstrumentSearch().type(value)).toBeAsString("type:$value")
  }
}
