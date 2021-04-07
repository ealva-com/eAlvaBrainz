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

import com.ealva.ealvabrainz.brainz.data.Label.SearchField
import com.ealva.ealvabrainz.brainz.data.Label.SearchField.Alias
import com.ealva.ealvabrainz.brainz.data.Label.SearchField.Area
import com.ealva.ealvabrainz.brainz.data.Label.SearchField.Begin
import com.ealva.ealvabrainz.brainz.data.Label.SearchField.Code
import com.ealva.ealvabrainz.brainz.data.Label.SearchField.Comment
import com.ealva.ealvabrainz.brainz.data.Label.SearchField.Country
import com.ealva.ealvabrainz.brainz.data.Label.SearchField.Default
import com.ealva.ealvabrainz.brainz.data.Label.SearchField.End
import com.ealva.ealvabrainz.brainz.data.Label.SearchField.Ended
import com.ealva.ealvabrainz.brainz.data.Label.SearchField.Ipi
import com.ealva.ealvabrainz.brainz.data.Label.SearchField.Isni
import com.ealva.ealvabrainz.brainz.data.Label.SearchField.Label
import com.ealva.ealvabrainz.brainz.data.Label.SearchField.LabelAccent
import com.ealva.ealvabrainz.brainz.data.Label.SearchField.LabelId
import com.ealva.ealvabrainz.brainz.data.Label.SearchField.ReleaseCount
import com.ealva.ealvabrainz.brainz.data.Label.SearchField.Type
import com.ealva.ealvabrainz.brainz.data.LabelMbid
import com.ealva.ealvabrainz.common.AreaName
import com.ealva.ealvabrainz.common.BrainzMarker
import com.ealva.ealvabrainz.common.LabelName
import com.ealva.ealvabrainz.common.Year
import com.ealva.ealvabrainz.lucene.Field
import com.ealva.ealvabrainz.lucene.Query
import com.ealva.ealvabrainz.lucene.Term
import com.ealva.ealvabrainz.search.term.DateTermBuilder
import com.ealva.ealvabrainz.search.term.MbidTermBuilder
import com.ealva.ealvabrainz.search.term.NumberTermBuilder
import com.ealva.ealvabrainz.search.term.TermBuilder
import java.time.LocalDate

@BrainzMarker
public class LabelSearch(query: Query = Query()) : BaseSearch<SearchField>(query) {
  /**
   * (part of) any [alias](https://musicbrainz.org/doc/Aliases) attached to the label (diacritics
   * are ignored)
   */
  public fun alias(term: String): Field = alias { Term(term) }
  public fun alias(build: TermBuilder.() -> Term): Field = add(Alias, TermBuilder().build())

  /** (part of) the name of the label's main associated area */
  public fun area(term: AreaName): Field = area { Term(term) }
  public fun area(build: TermBuilder.() -> Term): Field = add(Area, TermBuilder().build())

  /** the label's begin date (e.g. "1980-01-22") */
  public fun beginDate(term: LocalDate): Field = beginDate { Term(term) }
  public fun beginDate(term: Year): Field = beginDate { Term(term) }
  public fun beginDate(build: DateTermBuilder.() -> Term): Field =
    add(Begin, DateTermBuilder().build())

  /** (part of) the label's disambiguation comment */
  public fun comment(term: String): Field = comment { Term(term) }
  public fun comment(build: TermBuilder.() -> Term): Field = add(Comment, TermBuilder().build())

  /** the 2-letter code (ISO 3166-1 alpha-2) for the label's associated country */
  public fun country(term: String): Field = country { Term(term) }
  public fun country(build: TermBuilder.() -> Term): Field = add(Country, TermBuilder().build())

  /** Default searches [Alias] and [Label] */
  public fun default(name: LabelName): Field = default { Term(name) }
  public fun default(build: TermBuilder.() -> Term): Field = add(Default, TermBuilder().build())

  /** the label's end date (e.g. "1980-01-22") */
  public fun endDate(term: LocalDate): Field = endDate { Term(term) }
  public fun endDate(term: Year): Field = endDate { Term(term) }
  public fun endDate(build: DateTermBuilder.() -> Term): Field = add(End, DateTermBuilder().build())

  /** a boolean flag (true/false) indicating whether or not the label has ended (is dissolved) */
  public fun ended(term: Boolean): Field = add(Ended, Term(term))

  /** an IPI code associated with the label */
  public fun ipi(term: String): Field = ipi { Term(term) }
  public fun ipi(build: TermBuilder.() -> Term): Field = add(Ipi, TermBuilder().build())

  /** an ISNI code associated with the label */
  public fun isni(term: String): Field = isni { Term(term) }
  public fun isni(build: TermBuilder.() -> Term): Field = add(Isni, TermBuilder().build())

  /** (part of) the label's name (diacritics are ignored) */
  public fun label(term: LabelName): Field = label { Term(term) }
  public fun label(build: TermBuilder.() -> Term): Field = add(Label, TermBuilder().build())

  /** (part of) the label's name (with the specified diacritics) */
  public fun labelAccent(term: LabelName): Field = labelAccent { Term(term) }
  public fun labelAccent(build: TermBuilder.() -> Term): Field =
    add(LabelAccent, TermBuilder().build())

  /**
   * the [label code](https://musicbrainz.org/doc/Label/Label_Code) for the label (only the
   * numbers, without "LC")
   */
  public fun labelCode(term: String): Field = labelCode { Term(term) }
  public fun labelCode(build: TermBuilder.() -> Term): Field = add(Code, TermBuilder().build())

  /** the label's MBID */
  public fun labelId(term: LabelMbid): Field = labelId { Term(term) }
  public fun labelId(build: MbidTermBuilder<LabelMbid>.() -> Term): Field =
    add(LabelId, MbidTermBuilder<LabelMbid>().build())

  /** the number of releases related to the label */
  public fun releaseCount(term: Int): Field = releaseCount { Term(term) }
  public fun releaseCount(build: NumberTermBuilder.() -> Term): Field =
    add(ReleaseCount, NumberTermBuilder().build())

  /** (part of) a tag attached to the label */
  public fun tag(build: TermBuilder.() -> Term): Field = add(SearchField.Tag, TermBuilder().build())
  public fun tag(term: String): Field = tag { Term(term) }

  /** the label's [type](https://musicbrainz.org/doc/Label/Type) */
  public fun type(type: String): Field = type { Term(type) }
  public fun type(build: TermBuilder.() -> Term): Field = add(Type, TermBuilder().build())

  public companion object {
    public inline operator fun invoke(search: LabelSearch.() -> Unit): String {
      return LabelSearch().apply(search).toString()
    }
  }
}
