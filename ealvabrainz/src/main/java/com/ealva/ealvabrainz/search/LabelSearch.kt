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
import com.ealva.ealvabrainz.brainz.data.Label.SearchField.Label
import com.ealva.ealvabrainz.common.LabelMbid
import com.ealva.ealvabrainz.common.LabelName
import com.ealva.ealvabrainz.common.BrainzMarker
import com.ealva.ealvabrainz.lucene.Field
import com.ealva.ealvabrainz.lucene.Query
import com.ealva.ealvabrainz.lucene.Term
import java.time.LocalDate
import java.util.Date
import kotlin.experimental.ExperimentalTypeInference

@OptIn(ExperimentalTypeInference::class)
@BrainzMarker
public class LabelSearch(query: Query = Query()) : BaseSearch<SearchField>(query) {
  @JvmName("aliasTerm")
  @OverloadResolutionByLambdaReturnType
  /**
   * (part of) any [alias](https://musicbrainz.org/doc/Aliases) attached to the label (diacritics
   * are ignored)
   */
  public inline fun alias(term: () -> Term): Field = add(Alias, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun alias(term: () -> String): Field = alias { Term(term()) }

  /** (part of) the name of the label's main associated area */
  @JvmName("areaTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun area(term: () -> Term): Field = add(SearchField.Area, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun area(term: () -> String): Field = area { Term(term()) }

  @JvmName("beginDateTerm")
  @OverloadResolutionByLambdaReturnType
  /** the label's begin date (e.g. "1980-01-22") */
  public inline fun beginDate(term: () -> Term): Field = add(SearchField.Begin, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun beginDate(term: () -> LocalDate): Field = beginDate { Term(term()) }

  @JvmName("beginDateOld")
  @OverloadResolutionByLambdaReturnType
  public inline fun beginDate(term: () -> Date): Field = beginDate { Term(term()) }

  @JvmName("commentTerm")
  @OverloadResolutionByLambdaReturnType
  /** (part of) the label's disambiguation comment */
  public inline fun comment(term: () -> Term): Field = add(SearchField.Comment, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun comment(term: () -> String): Field = comment { Term(term()) }

  @JvmName("countryTerm")
  @OverloadResolutionByLambdaReturnType
  /** the 2-letter code (ISO 3166-1 alpha-2) for the label's associated country */
  public inline fun country(term: () -> Term): Field = add(SearchField.Country, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun country(term: () -> String): Field = country { Term(term()) }

  @JvmName("defaultTerm")
  @OverloadResolutionByLambdaReturnType
  /** Default searches [Alias] and [Label] */
  public inline fun default(term: () -> Term): Field = add(SearchField.Default, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun default(term: () -> String): Field = default { Term(term()) }

  @JvmName("defaultLabelName")
  @OverloadResolutionByLambdaReturnType
  public inline fun default(term: () -> LabelName): Field = default { Term(term()) }

  @JvmName("endDateTerm")
  @OverloadResolutionByLambdaReturnType
  /** the label's end date (e.g. "1980-01-22") */
  public inline fun endDate(term: () -> Term): Field = add(SearchField.End, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun endDate(term: () -> LocalDate): Field = endDate { Term(term()) }

  @JvmName("endDateOld")
  @OverloadResolutionByLambdaReturnType
  public inline fun endDate(term: () -> Date): Field = endDate { Term(term()) }

  @JvmName("endedTerm")
  @OverloadResolutionByLambdaReturnType
  /** a boolean flag (true/false) indicating whether or not the label has ended (is dissolved) */
  public inline fun ended(term: () -> Term): Field = add(SearchField.Ended, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun ended(term: () -> Boolean): Field = ended { Term(term()) }

  @JvmName("ipiTerm")
  @OverloadResolutionByLambdaReturnType
  /** an IPI code associated with the label */
  public inline fun ipi(term: () -> Term): Field = add(SearchField.Ipi, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun ipi(term: () -> String): Field = ipi { Term(term()) }

  @JvmName("isniTerm")
  @OverloadResolutionByLambdaReturnType
  /** an ISNI code associated with the label */
  public inline fun isni(term: () -> Term): Field = add(SearchField.Isni, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun isni(term: () -> String): Field = isni { Term(term()) }

  @JvmName("labelTerm")
  @OverloadResolutionByLambdaReturnType
  /** (part of) the label's name (diacritics are ignored) */
  public inline fun label(term: () -> Term): Field = add(Label, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun label(term: () -> LabelName): Field = label { Term(term()) }

  @JvmName("labelAccentTerm")
  @OverloadResolutionByLambdaReturnType
  /** (part of) the label's name (with the specified diacritics) */
  public inline fun labelAccent(term: () -> Term): Field = add(SearchField.LabelAccent, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun labelAccent(term: () -> LabelName): Field = labelAccent { Term(term()) }

  @JvmName("labelCodeTerm")
  @OverloadResolutionByLambdaReturnType
  /**
   * the [label code](https://musicbrainz.org/doc/Label/Label_Code) for the label (only the
   * numbers, without "LC")
   */
  public inline fun labelCode(term: () -> Term): Field = add(SearchField.Code, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun labelCode(term: () -> String): Field = labelCode { Term(term()) }

  @JvmName("labelIdTerm")
  @OverloadResolutionByLambdaReturnType
  /** the label's MBID */
  public inline fun labelId(term: () -> Term): Field = add(SearchField.LabelId, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun labelId(term: () -> LabelMbid): Field = labelId { Term(term()) }

  @JvmName("releaseCountTerm")
  @OverloadResolutionByLambdaReturnType
  /** the number of releases related to the label */
  public inline fun releaseCount(term: () -> Term): Field = add(SearchField.ReleaseCount, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun releaseCount(term: () -> Int): Field = releaseCount { Term(term()) }

  @JvmName("tagTerm")
  @OverloadResolutionByLambdaReturnType
  /** (part of) a tag attached to the label */
  public inline fun tag(term: () -> Term): Field = add(SearchField.Tag, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun tag(term: () -> String): Field = tag { Term(term()) }

  @JvmName("typeTerm")
  @OverloadResolutionByLambdaReturnType
  /** the label's [type](https://musicbrainz.org/doc/Label/Type) */
  public inline fun type(term: () -> Term): Field = add(SearchField.Type, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun type(type: () -> String): Field = type { Term(type()) }

  public companion object {
    public inline operator fun invoke(search: LabelSearch.() -> Unit): String {
      return LabelSearch().apply(search).toString()
    }
  }
}
