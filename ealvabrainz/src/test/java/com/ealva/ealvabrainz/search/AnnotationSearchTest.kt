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

import com.ealva.ealvabrainz.brainz.data.Annotation
import com.ealva.ealvabrainz.common.ArtistMbid
import com.ealva.ealvabrainz.lucene.SingleTerm
import com.ealva.ealvabrainz.matchers.expect
import com.ealva.ealvabrainz.matchers.toBeAsString
import com.nhaarman.expect.expect
import org.junit.Test

public class AnnotationSearchTest {
  @Test
  public fun `test expected search fields`() {
    /*
    https://musicbrainz.org/doc/Indexed_Search_Syntax#Search_Fields_12
    Field	Description
    entity	the annotated entity's MBID
    id	    the numeric ID of the annotation (e.g. 703027)
    name	  the annotated entity's name or title (diacritics are ignored)
    text	  the annotation's content (includes wiki formatting)
    type	  the annotated entity's entity type
     */
    val values = Annotation.SearchField.values()
    val set = values.mapTo(mutableSetOf()) { it.value }
    expect(set).toHaveSize(6)
    expect(set).toContain("")
    expect(set).toContain("entity")
    expect(set).toContain("id")
    expect(set).toContain("name")
    expect(set).toContain("text")
    expect(set).toContain("type")
  }

  @Test
  public fun `test all term functions cover all fields`() {
    val value = "a"
    val term = SingleTerm(value)
    val search = AnnotationSearch()
    search.default { term }
    search.entity { term }
    search.id { term }
    search.name { term }
    search.text { term }
    search.type { term }
    var result = search.toString()
    Annotation.SearchField.values()
      .filterNot { it === Annotation.SearchField.Default }
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
    expect(AnnotationSearch().default { value }).toBeAsString(value)
    val mbid = ArtistMbid("5b11f4ce-a62d-471e-81fc-a69a8278c7da")
    expect(AnnotationSearch().entity { mbid }).toBeAsString("entity:${mbid.value}")
    expect(AnnotationSearch().id { 1 }).toBeAsString("id:1")
    expect(AnnotationSearch().name { value }).toBeAsString("name:$value")
    expect(AnnotationSearch().text { value }).toBeAsString("text:$value")
    expect(AnnotationSearch().type { value }).toBeAsString("type:$value")
  }
}
