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

import com.ealva.ealvabrainz.brainz.data.Label
import com.ealva.ealvabrainz.brainz.data.LabelMbid
import com.ealva.ealvabrainz.common.AreaName
import com.ealva.ealvabrainz.common.LabelName
import com.ealva.ealvabrainz.common.toLocalDate
import com.ealva.ealvabrainz.lucene.SingleTerm
import com.ealva.ealvabrainz.matchers.expect
import com.ealva.ealvabrainz.matchers.toBeAsString
import com.nhaarman.expect.expect
import org.junit.Test
import java.util.Date

public class LabelSearchTest {
  @Test
  public fun `test expected search fields`() {
    /*
    https://musicbrainz.org/doc/Indexed_Search_Syntax#Search_Fields_5
    Field	Description
    alias	(part of) any alias attached to the label (diacritics are ignored)
    area	(part of) the name of the label's main associated area
    begin	the label's begin date (e.g. "1980-01-22")
    code	the label code for the label (only the numbers, without "LC")
    comment	(part of) the label's disambiguation comment
    country	the 2-letter code (ISO 3166-1 alpha-2) for the label's associated country
    end	the label's end date (e.g. "1980-01-22")
    ended	a boolean flag (true/false) indicating whether or not the label has ended (is dissolved)
    ipi	an IPI code associated with the label
    isni	an ISNI code associated with the label
    label	(part of) the label's name (diacritics are ignored)
    labelaccent	(part of) the label's name (with the specified diacritics)
    laid	the label's MBID
    release_count	the amount of releases related to the label
    DEPRECATED sortname	equivalent to name (labels no longer have separate sort names)
    tag	(part of) a tag attached to the label
    type	the label's type
     */
    val values = Label.SearchField.values()
    val set = values.mapTo(mutableSetOf()) { it.value }
    expect(set).toHaveSize(17)
    expect(set).toContain("")
    expect(set).toContain("alias")
    expect(set).toContain("area")
    expect(set).toContain("begin")
    expect(set).toContain("code")
    expect(set).toContain("comment")
    expect(set).toContain("country")
    expect(set).toContain("end")
    expect(set).toContain("ended")
    expect(set).toContain("ipi")
    expect(set).toContain("isni")
    expect(set).toContain("label")
    expect(set).toContain("labelaccent")
    expect(set).toContain("laid")
    expect(set).toContain("release_count")
    expect(set).toContain("tag")
    expect(set).toContain("type")
  }

  @Test
  public fun `test all term functions cover all fields`() {
    val value = "a"
    val term = SingleTerm(value)
    val search = LabelSearch()
    search.alias { term }
    search.area { term }
    search.beginDate { term }
    search.comment { term }
    search.country { term }
    search.default { term }
    search.endDate { term }
    search.ipi { term }
    search.isni { term }
    search.label { term }
    search.labelAccent { term }
    search.labelCode { term }
    search.labelId { term }
    search.releaseCount { term }
    search.tag { term }
    search.type { term }
    var result = search.toString()
    Label.SearchField.values()
      .filterNot { it === Label.SearchField.Default || it === Label.SearchField.Ended }
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
    val mbid = LabelMbid("5b11f4ce-a62d-471e-81fc-a69a8278c7da")
    expect(LabelSearch().alias(value)).toBeAsString("alias:$value")
    expect(LabelSearch().area(AreaName(value))).toBeAsString("area:$value")
    expect(LabelSearch().beginDate(Date(0).toLocalDate())).toBeAsString("begin:\"1969-12-31\"")
    expect(LabelSearch().comment(value)).toBeAsString("comment:$value")
    expect(LabelSearch().country(value)).toBeAsString("country:$value")
    expect(LabelSearch().default(LabelName(value))).toBeAsString(value)
    expect(LabelSearch().endDate(Date(0).toLocalDate())).toBeAsString("end:\"1969-12-31\"")
    expect(LabelSearch().ended(false)).toBeAsString("ended:false")
    expect(LabelSearch().ipi(value)).toBeAsString("ipi:$value")
    expect(LabelSearch().isni(value)).toBeAsString("isni:$value")
    expect(LabelSearch().label(LabelName(value))).toBeAsString("label:$value")
    expect(LabelSearch().labelAccent(LabelName(value))).toBeAsString("labelaccent:$value")
    expect(LabelSearch().labelCode(value)).toBeAsString("code:$value")
    expect(LabelSearch().labelId(mbid)).toBeAsString("laid:${mbid.value}")
    expect(LabelSearch().releaseCount(101)).toBeAsString("release_count:101")
    expect(LabelSearch().tag(value)).toBeAsString("tag:$value")
    expect(LabelSearch().type(value)).toBeAsString("type:$value")
  }
}
