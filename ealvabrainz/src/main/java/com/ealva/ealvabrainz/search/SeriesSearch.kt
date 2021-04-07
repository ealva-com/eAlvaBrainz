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
import com.ealva.ealvabrainz.brainz.data.SeriesMbid
import com.ealva.ealvabrainz.common.BrainzMarker
import com.ealva.ealvabrainz.common.SeriesName
import com.ealva.ealvabrainz.lucene.Field
import com.ealva.ealvabrainz.lucene.Query
import com.ealva.ealvabrainz.lucene.Term
import com.ealva.ealvabrainz.search.term.TermBuilder

@BrainzMarker
public class SeriesSearch(query: Query = Query()) : BaseSearch<SearchField>(query) {
  /**
   * (part of) any [alias](https://musicbrainz.org/doc/Aliases attached to the series (diacritics
     * are ignored)
   */
  public fun alias(term: String): Field = alias { Term(term) }
  public fun alias(build: TermBuilder.() -> Term): Field =
    add(Alias, TermBuilder().build())

  /** (part of) the series' disambiguation comment  */
  public fun comment(term: String): Field = comment { Term(term) }
  public fun comment(build: TermBuilder.() -> Term): Field =
    add(SearchField.Comment, TermBuilder().build())

  /** Default searches [Alias] and [Series] */
  public fun default(term: String): Field = default { Term(term) }
  public fun default(build: TermBuilder.() -> Term): Field =
    add(SearchField.Default, TermBuilder().build())

  /** (part of) the series's name (diacritics are ignored) */
  public fun series(term: SeriesName): Field = series { Term(term) }
  public fun series(build: TermBuilder.() -> Term): Field = add(Series, TermBuilder().build())

  /** (part of) the series's name (with the specified diacritics) */
  public fun seriesAccent(term: SeriesName): Field = seriesAccent { Term(term) }
  public fun seriesAccent(build: TermBuilder.() -> Term): Field =
    add(SearchField.SeriesAccent, TermBuilder().build())

  /** the MBID of an series related to the series */
  public fun seriesId(mbid: SeriesMbid): Field = seriesId { Term(mbid) }
  public fun seriesId(term: () -> Term): Field = add(SearchField.SeriesId, term())

  /** a tag attached to the series */
  public fun tag(build: TermBuilder.() -> Term): Field = add(SearchField.Tag, TermBuilder().build())
  public fun tag(term: String): Field = tag { Term(term) }

  /** the series' [type](https://musicbrainz.org/doc/Series#Type) */
  public fun type(type: String): Field = type { Term(type) }
  public fun type(build: TermBuilder.() -> Term): Field =
    add(SearchField.Type, TermBuilder().build())

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
