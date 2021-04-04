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

import com.ealva.ealvabrainz.brainz.data.Series.SearchField
import com.ealva.ealvabrainz.brainz.data.Series.SearchField.Alias
import com.ealva.ealvabrainz.brainz.data.Series.SearchField.Series
import com.ealva.ealvabrainz.common.BrainzMarker
import com.ealva.ealvabrainz.brainz.data.SeriesMbid
import com.ealva.ealvabrainz.common.SeriesName
import com.ealva.ealvabrainz.lucene.Field
import com.ealva.ealvabrainz.lucene.Query
import com.ealva.ealvabrainz.lucene.Term
import kotlin.experimental.ExperimentalTypeInference

@OptIn(ExperimentalTypeInference::class)
@BrainzMarker
public class SeriesSearch(query: Query = Query()) : BaseSearch<SearchField>(query) {
  @JvmName("aliasTerm")
  @OverloadResolutionByLambdaReturnType
  /**
   * (part of) any [alias](https://musicbrainz.org/doc/Aliases attached to the series (diacritics
     * are ignored)
   */
  public inline fun alias(term: () -> Term): Field = add(Alias, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun alias(term: () -> String): Field = alias { Term(term()) }

  @JvmName("commentTerm")
  @OverloadResolutionByLambdaReturnType
  /** (part of) the series' disambiguation comment  */
  public inline fun comment(term: () -> Term): Field = add(SearchField.Comment, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun comment(term: () -> String): Field = comment { Term(term()) }

  @JvmName("defaultTerm")
  @OverloadResolutionByLambdaReturnType
  /** Default searches [Alias] and [Series] */
  public inline fun default(term: () -> Term): Field = add(SearchField.Default, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun default(term: () -> String): Field = default { Term(term()) }

  @JvmName("seriesTerm")
  @OverloadResolutionByLambdaReturnType
  /** (part of) the series's name (diacritics are ignored) */
  public inline fun series(term: () -> Term): Field = add(Series, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun series(term: () -> SeriesName): Field = series { Term(term()) }

  @JvmName("seriesAccentTerm")
  @OverloadResolutionByLambdaReturnType
  /** (part of) the series's name (with the specified diacritics) */
  public inline fun seriesAccent(term: () -> Term): Field = add(SearchField.SeriesAccent, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun seriesAccent(term: () -> SeriesName): Field = seriesAccent { Term(term()) }

  @JvmName("seriesIdTerm")
  @OverloadResolutionByLambdaReturnType
  /** the MBID of an series related to the series */
  public inline fun seriesId(term: () -> Term): Field = add(SearchField.SeriesId, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun seriesId(mbid: () -> SeriesMbid): Field = seriesId { Term(mbid()) }

  @JvmName("tagTerm")
  @OverloadResolutionByLambdaReturnType
  /** a tag attached to the series */
  public inline fun tag(term: () -> Term): Field = add(SearchField.Tag, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun tag(term: () -> String): Field = tag { Term(term()) }

  @JvmName("typeTerm")
  @OverloadResolutionByLambdaReturnType
  /** the series's [type](https://musicbrainz.org/doc/Area#Type)  */
  public inline fun type(term: () -> Term): Field = add(SearchField.Type, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun type(type: () -> String): Field = type { Term(type()) }

  public companion object {
    public inline operator fun invoke(search: SeriesSearch.() -> Unit): String {
      return SeriesSearch().apply(search).toString()
    }
  }
/*
    /** the Series MBID*/
    SeriesId("sid"),
 */
}
