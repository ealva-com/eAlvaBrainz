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

import com.ealva.ealvabrainz.brainz.data.Area.SearchField
import com.ealva.ealvabrainz.brainz.data.Area.SearchField.Alias
import com.ealva.ealvabrainz.brainz.data.Area.SearchField.AreaAccent
import com.ealva.ealvabrainz.brainz.data.Area.SearchField.AreaId
import com.ealva.ealvabrainz.brainz.data.Area.SearchField.Begin
import com.ealva.ealvabrainz.brainz.data.Area.SearchField.Comment
import com.ealva.ealvabrainz.brainz.data.Area.SearchField.Default
import com.ealva.ealvabrainz.brainz.data.Area.SearchField.End
import com.ealva.ealvabrainz.brainz.data.Area.SearchField.Iso
import com.ealva.ealvabrainz.brainz.data.Area.SearchField.Tag
import com.ealva.ealvabrainz.brainz.data.Area.SearchField.Type
import com.ealva.ealvabrainz.brainz.data.AreaMbid
import com.ealva.ealvabrainz.common.AreaName
import com.ealva.ealvabrainz.common.BrainzMarker
import com.ealva.ealvabrainz.common.Year
import com.ealva.ealvabrainz.lucene.Field
import com.ealva.ealvabrainz.lucene.Term
import com.ealva.ealvabrainz.search.term.DateTermBuilder
import com.ealva.ealvabrainz.search.term.MbidTermBuilder
import com.ealva.ealvabrainz.search.term.TermBuilder
import java.time.LocalDate

@BrainzMarker
public class AreaSearch : BaseSearch<SearchField>() {
  /** (part of) any alias attached to the area (diacritics are ignored) */
  public fun alias(term: String): Field = alias { Term(term) }
  public fun alias(build: TermBuilder.() -> Term): Field = add(Alias, TermBuilder().build())

  /** the area's MBID */
  public fun areaId(mbid: AreaMbid): Field = areaId { Term(mbid) }
  public fun areaId(build: MbidTermBuilder<AreaMbid>.() -> Term): Field =
    add(AreaId, MbidTermBuilder<AreaMbid>().build())

  /** (part of) the name of the artist's main associated area */
  public fun area(term: AreaName): Field = area { Term(term) }
  public fun area(build: TermBuilder.() -> Term): Field =
    add(SearchField.AreaName, TermBuilder().build())

  /** (part of) the area's name (with the specified diacritics) */
  public fun areaAccent(term: String): Field = areaAccent { Term(term) }
  public fun areaAccent(build: TermBuilder.() -> Term): Field =
    add(AreaAccent, TermBuilder().build())

  /** the area's begin date (e.g. "1980-01-22") */
  public fun beginDate(term: LocalDate): Field = beginDate { Term(term) }
  public fun beginDate(term: Year): Field = beginDate { Term(term) }
  public fun beginDate(build: DateTermBuilder.() -> Term): Field =
    add(Begin, DateTermBuilder().build())

  /** (part of) the area's disambiguation comment */
  public fun comment(term: String): Field = comment { Term(term) }
  public fun comment(build: TermBuilder.() -> Term): Field = add(Comment, TermBuilder().build())

  /** Default searches the [AreaName] */
  public fun default(name: AreaName): Field = default { Term(name) }
  public fun default(build: TermBuilder.() -> Term): Field = add(Default, TermBuilder().build())

  /** the area's end date (e.g. "1980-01-22") */
  public fun endDate(term: LocalDate): Field = endDate { Term(term) }
  public fun endDate(term: Year): Field = endDate { Term(term) }
  public fun endDate(build: DateTermBuilder.() -> Term): Field = add(End, DateTermBuilder().build())

  /**
   * A boolean flag (true/false) indicating whether or not the area has ended (is no longer
   * current)
   */
  public fun ended(term: Boolean): Field = add(SearchField.Ended, Term(term))

  /**
   * An [ISO 3166-1, 3166-2 or 3166-3](https://en.wikipedia.org/wiki/ISO_3166) code attached to the
   * area
   */
  public fun iso(term: String): Field = iso { Term(term) }
  public fun iso(build: TermBuilder.() -> Term): Field = add(Iso, TermBuilder().build())

  /** a tag attached to the area */
  public fun tag(term: String): Field = tag { Term(term) }
  public fun tag(build: TermBuilder.() -> Term): Field = add(Tag, TermBuilder().build())

  /** the area's [type](https://musicbrainz.org/doc/Area#Type)  */
  public fun type(type: String): Field = type { Term(type) }
  public fun type(build: TermBuilder.() -> Term): Field = add(Type, TermBuilder().build())

  public companion object {
    public inline operator fun invoke(search: AreaSearch.() -> Unit): String {
      return AreaSearch().apply(search).toString()
    }
  }
}
