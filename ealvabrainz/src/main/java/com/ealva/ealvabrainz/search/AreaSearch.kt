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
import com.ealva.ealvabrainz.brainz.data.AreaMbid
import com.ealva.ealvabrainz.common.AreaName
import com.ealva.ealvabrainz.common.BrainzMarker
import com.ealva.ealvabrainz.lucene.Field
import com.ealva.ealvabrainz.lucene.Term
import java.time.LocalDate
import java.util.Date
import kotlin.experimental.ExperimentalTypeInference

@Suppress("unused")
@OptIn(ExperimentalTypeInference::class)
@BrainzMarker
public class AreaSearch : BaseSearch<SearchField>() {
  @JvmName("aliasTerm")
  @OverloadResolutionByLambdaReturnType
  /** (part of) any alias attached to the area (diacritics are ignored) */
  public inline fun alias(term: () -> Term): Field = add(SearchField.Alias, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun alias(term: () -> String): Field = alias { Term(term()) }

  @JvmName("areaIdTerm")
  @OverloadResolutionByLambdaReturnType
  /** the area's MBID */
  public inline fun areaId(term: () -> Term): Field = add(SearchField.AreaId, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun areaId(mbid: () -> AreaMbid): Field = areaId { Term(mbid()) }

  @JvmName("areaTerm")
  @OverloadResolutionByLambdaReturnType
  /** (part of) the name of the artist's main associated area */
  public inline fun area(term: () -> Term): Field = add(SearchField.AreaName, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun area(term: () -> AreaName): Field = area { Term(term()) }

  @JvmName("areaAccentTerm")
  @OverloadResolutionByLambdaReturnType
  /** (part of) the area's name (with the specified diacritics) */
  public inline fun areaAccent(term: () -> Term): Field = add(SearchField.AreaAccent, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun areaAccent(term: () -> String): Field = areaAccent { Term(term()) }

  @JvmName("beginDateTerm")
  @OverloadResolutionByLambdaReturnType
  /** the area's begin date (e.g. "1980-01-22") */
  public inline fun beginDate(term: () -> Term): Field = add(SearchField.Begin, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun beginDate(term: () -> LocalDate): Field = beginDate { Term(term()) }

  @JvmName("beginDateOld")
  @OverloadResolutionByLambdaReturnType
  public inline fun beginDate(term: () -> Date): Field = beginDate { Term(term()) }

  @JvmName("commentTerm")
  @OverloadResolutionByLambdaReturnType
  /** (part of) the area's disambiguation comment */
  public inline fun comment(term: () -> Term): Field = add(SearchField.Comment, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun comment(term: () -> String): Field = comment { Term(term()) }

  @JvmName("defaultTerm")
  @OverloadResolutionByLambdaReturnType
  /** Default searches the [AreaName] */
  public inline fun default(term: () -> Term): Field = add(SearchField.Default, term())

  @JvmName("defaultString")
  @OverloadResolutionByLambdaReturnType
  public inline fun default(default: () -> String): Field = default { Term(default()) }

  @OverloadResolutionByLambdaReturnType
  public inline fun default(default: () -> AreaName): Field = default { Term(default()) }

  @JvmName("endDateTerm")
  @OverloadResolutionByLambdaReturnType
  /** the area's end date (e.g. "1980-01-22") */
  public inline fun endDate(term: () -> Term): Field = add(SearchField.End, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun endDate(term: () -> LocalDate): Field = endDate { Term(term()) }

  @JvmName("endDateOld")
  @OverloadResolutionByLambdaReturnType
  public inline fun endDate(term: () -> Date): Field = endDate { Term(term()) }

  @JvmName("endedTerm")
  @OverloadResolutionByLambdaReturnType
  /**
   * A boolean flag (true/false) indicating whether or not the area has ended (is no longer
   * current)
   */
  public inline fun ended(term: () -> Term): Field = add(SearchField.Ended, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun ended(term: () -> Boolean): Field = ended { Term(term()) }

  @JvmName("isoTerm")
  @OverloadResolutionByLambdaReturnType
  /**
   * An [ISO 3166-1, 3166-2 or 3166-3](https://en.wikipedia.org/wiki/ISO_3166) code attached to the
   * area
   */
  public inline fun iso(term: () -> Term): Field = add(SearchField.Iso, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun iso(term: () -> String): Field = iso { Term(term()) }

  @JvmName("tagTerm")
  @OverloadResolutionByLambdaReturnType
  /** a tag attached to the area */
  public inline fun tag(term: () -> Term): Field = add(SearchField.Tag, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun tag(term: () -> String): Field = tag { Term(term()) }

  @JvmName("typeTerm")
  @OverloadResolutionByLambdaReturnType
  /** the area's [type](https://musicbrainz.org/doc/Area#Type)  */
  public inline fun type(term: () -> Term): Field = add(SearchField.Type, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun type(type: () -> String): Field = type { Term(type()) }

  public companion object {
    public inline operator fun invoke(search: AreaSearch.() -> Unit): String {
      return AreaSearch().apply(search).toString()
    }
  }
}
