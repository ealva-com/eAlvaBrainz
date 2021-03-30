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

public class FieldTest {
  @Test
  public fun `test field one term append`() {
    val field = Field("title", Term("The Right Way"))
    expect(field.toString()).toBe("""title:"The Right Way"""")
    val require = Field("text", Term("Hello").require())
    expect(require.toString()).toBe("""text:+Hello""")
    val range = Field("date", "20200102".toTerm() inclusive "20200104".toTerm())
    expect(range.toString()).toBe("""date:[20200102 TO 20200104]""")

    expect(Field("album", Term("Aqualung")).toString()).toBe("album:Aqualung")
    expect(Field("album", Term("Aqualung"), Term("Thick as a Brick")).toString())
      .toBe("""album:(Aqualung "Thick as a Brick")""")
  }

  @Test
  public fun `test field multiple terms append`() {
    val upAgain = Field("title", Term("The Right Way"), Term("Up Again"))
    expect(upAgain.toString()).toBe("""title:("The Right Way" "Up Again")""")
    val andAgain = Field("title", Term("The Right Way"), Term("Up Again"), Term("And Again"))
    expect(andAgain.toString()).toBe("""title:("The Right Way" "Up Again" "And Again")""")
    val prohibitUpAgain = Field("title", Term("The Right Way"), Term("Up Again").prohibit())
    expect(prohibitUpAgain.toString()).toBe("""title:("The Right Way" -"Up Again")""")
  }

  @Test
  public fun `test and fields`() {
    val album = Field("album", Term("Revolver"))
    val artist = Field("artist", Term("The Beatles"))
    expect((album and artist).toString()).toBe("""(album:Revolver AND artist:"The Beatles")""")
  }

  @Test
  public fun `test or fields`() {
    val album = Field("album", Term("Revolver"))
    val artist = Field("artist", Term("The Beatles"))
    expect((album or artist).toString()).toBe("""(album:Revolver OR artist:"The Beatles")""")
  }

  @Test
  public fun `test and-or field expressions`() {
    val revolver = Field("album", Term("Revolver"))
    val rubberSoul = Field("album", Term("Rubber Soul"))
    val beatles = Field("artist", Term("The Beatles"))
    val exp = beatles and (revolver or rubberSoul)
    expect(exp.toString())
      .toBe("""(artist:"The Beatles" AND (album:Revolver OR album:"Rubber Soul"))""")
    val exp2 = beatles and revolver or rubberSoul
    expect(exp2.toString())
      .toBe("""((artist:"The Beatles" AND album:Revolver) OR album:"Rubber Soul")""")
  }
}

private infix fun Field.and(other: Field): Field = AndExp(listOf(this, other))
private infix fun Field.or(other: Field): Field = OrExp(listOf(this, other))
