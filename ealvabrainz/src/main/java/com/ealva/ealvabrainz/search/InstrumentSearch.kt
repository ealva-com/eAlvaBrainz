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

import com.ealva.ealvabrainz.brainz.data.Instrument
import com.ealva.ealvabrainz.brainz.data.Instrument.SearchField
import com.ealva.ealvabrainz.brainz.data.Instrument.SearchField.Alias
import com.ealva.ealvabrainz.brainz.data.Instrument.SearchField.Description
import com.ealva.ealvabrainz.common.BrainzMarker
import com.ealva.ealvabrainz.common.InstrumentMbid
import com.ealva.ealvabrainz.common.InstrumentName
import com.ealva.ealvabrainz.lucene.Field
import com.ealva.ealvabrainz.lucene.Query
import com.ealva.ealvabrainz.lucene.Term
import kotlin.experimental.ExperimentalTypeInference

@OptIn(ExperimentalTypeInference::class)
@BrainzMarker
public class InstrumentSearch(query: Query = Query()) : BaseSearch<SearchField>(query) {
  @JvmName("aliasTerm")
  @OverloadResolutionByLambdaReturnType
  /** (part of) any alias attached to the instrument (diacritics are ignored) */
  public inline fun alias(term: () -> Term): Field = add(Alias, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun alias(term: () -> String): Field = alias { Term(term()) }

  @JvmName("commentTerm")
  @OverloadResolutionByLambdaReturnType
  /** (part of) the instrument's disambiguation comment */
  public inline fun comment(term: () -> Term): Field = add(SearchField.Comment, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun comment(term: () -> String): Field = comment { Term(term()) }

  @JvmName("defaultTerm")
  @OverloadResolutionByLambdaReturnType
  /** Default searches [Alias], [Description], and [Instrument] */
  public inline fun default(term: () -> Term): Field = add(SearchField.Default, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun default(term: () -> String): Field = default { Term(term()) }

  @JvmName("descriptionTerm")
  @OverloadResolutionByLambdaReturnType
  /** (part of) the description of the instrument (in English) */
  public inline fun description(term: () -> Term): Field = add(Description, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun description(term: () -> String): Field = description { Term(term()) }

  @JvmName("instrumentIdTerm")
  @OverloadResolutionByLambdaReturnType
  /** the MBID of the instrument */
  public inline fun instrumentId(term: () -> Term): Field = add(SearchField.InstrumentId, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun instrumentId(mbid: () -> InstrumentMbid): Field = instrumentId { Term(mbid()) }

  @JvmName("instrumentTerm")
  @OverloadResolutionByLambdaReturnType
  /** (part of) the name of the artist's main associated instrument */
  public inline fun instrument(term: () -> Term): Field = add(SearchField.InstrumentName, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun instrument(term: () -> InstrumentName): Field = instrument { Term(term()) }

  @JvmName("instrumentAccentTerm")
  @OverloadResolutionByLambdaReturnType
  /** the instrument's name (with accented characters) */
  public inline fun instrumentAccent(
    term: () -> Term
  ): Field = add(SearchField.InstrumentAccent, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun instrumentAccent(term: () -> String): Field = instrumentAccent { Term(term()) }

  @JvmName("tagTerm")
  @OverloadResolutionByLambdaReturnType
  /** (part of) a tag attached to the instrument */
  public inline fun tag(term: () -> Term): Field = add(SearchField.Tag, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun tag(term: () -> String): Field = tag { Term(term()) }

  @JvmName("typeTerm")
  @OverloadResolutionByLambdaReturnType
  /** the instrument's [type](https://musicbrainz.org/doc/Instrument#Type)  */
  public inline fun type(term: () -> Term): Field = add(SearchField.Type, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun type(type: () -> String): Field = type { Term(type()) }

  public companion object {
    public inline operator fun invoke(search: InstrumentSearch.() -> Unit): String {
      return InstrumentSearch().apply(search).toString()
    }
  }
}
