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

package com.ealva.ealvabrainz.lucene

import com.nhaarman.expect.expect
import org.junit.Test

public class QueryTest {
  @Test
  public fun `test query append`() {
    expect(Query("title" to "Hey Joe").toString()).toBe("""title:"Hey Joe"""")
    val query = Query("title" to "Hey Joe", "artist" to "Jimi Hendrix")
    expect(query.toString()).toBe("""title:"Hey Joe" artist:"Jimi Hendrix"""")
  }

  @Test
  public fun `test query replace `() {
    val titleField = Field("title", Term("Hey Joe"))
    val artistField = Field("artist", Term("Jimi Hendrix"))
    val titleAndArtist = titleField and artistField
    Query(titleField).apply {
      expect(toString()).toBe("title:\"Hey Joe\"")
      replaceOrAdd(titleField, titleAndArtist)
      expect(toString()).toBe("(title:\"Hey Joe\" AND artist:\"Jimi Hendrix\")")
    }

    val unused = Field("no", Term("no"))
    Query().apply {
      replaceOrAdd(unused, titleField)
      expect(toString()).toBe("title:\"Hey Joe\"")
      replaceOrAdd(titleField, titleAndArtist)
      expect(toString()).toBe("(title:\"Hey Joe\" AND artist:\"Jimi Hendrix\")")
    }

    Query().apply {
      replaceOrAdd(unused, titleField)
      expect(toString()).toBe("title:\"Hey Joe\"")
      replaceOrAdd(unused, artistField)
      expect(toString()).toBe("title:\"Hey Joe\" artist:\"Jimi Hendrix\"")
    }
  }
}

private infix fun Field.and(other: Field): Field = AndExp(listOf(this, other))
