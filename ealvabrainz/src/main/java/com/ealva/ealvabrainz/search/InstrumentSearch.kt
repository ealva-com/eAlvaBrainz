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
import com.ealva.ealvabrainz.brainz.data.Instrument.SearchField.Comment
import com.ealva.ealvabrainz.brainz.data.Instrument.SearchField.Default
import com.ealva.ealvabrainz.brainz.data.Instrument.SearchField.Description
import com.ealva.ealvabrainz.brainz.data.Instrument.SearchField.InstrumentAccent
import com.ealva.ealvabrainz.brainz.data.Instrument.SearchField.InstrumentId
import com.ealva.ealvabrainz.brainz.data.Instrument.SearchField.Tag
import com.ealva.ealvabrainz.brainz.data.Instrument.SearchField.Type
import com.ealva.ealvabrainz.brainz.data.InstrumentMbid
import com.ealva.ealvabrainz.common.BrainzMarker
import com.ealva.ealvabrainz.common.InstrumentName
import com.ealva.ealvabrainz.lucene.Field
import com.ealva.ealvabrainz.lucene.Query
import com.ealva.ealvabrainz.lucene.Term
import com.ealva.ealvabrainz.search.term.MbidTermBuilder
import com.ealva.ealvabrainz.search.term.TermBuilder

@BrainzMarker
public class InstrumentSearch(query: Query = Query()) : BaseSearch<SearchField>(query) {
  /** (part of) any alias attached to the instrument (diacritics are ignored) */
  public fun alias(term: String): Field = alias { Term(term) }
  public fun alias(build: TermBuilder.() -> Term): Field = add(Alias, TermBuilder().build())

  /** (part of) the instrument's disambiguation comment */
  public fun comment(term: String): Field = comment { Term(term) }
  public fun comment(build: TermBuilder.() -> Term): Field = add(Comment, TermBuilder().build())

  /** Default searches [Alias], [Description], and [Instrument] */
  public fun default(name: String): Field = default { Term(name) }
  public fun default(build: TermBuilder.() -> Term): Field = add(Default, TermBuilder().build())

  /** (part of) the description of the instrument (in English) */
  public fun description(term: String): Field = description { Term(term) }
  public fun description(term: TermBuilder.() -> Term): Field =
    add(Description, TermBuilder().term())

  /** the MBID of the instrument */
  public fun instrumentId(mbid: InstrumentMbid): Field = instrumentId { Term(mbid) }
  public fun instrumentId(build: MbidTermBuilder<InstrumentMbid>.() -> Term): Field =
    add(InstrumentId, MbidTermBuilder<InstrumentMbid>().build())

  /** (part of) the name of the artist's main associated instrument */
  public fun instrument(term: InstrumentName): Field = instrument { Term(term) }
  public fun instrument(build: TermBuilder.() -> Term): Field =
    add(SearchField.InstrumentName, TermBuilder().build())

  /** the instrument's name (with accented characters) */
  public fun instrumentAccent(term: String): Field = instrumentAccent { Term(term) }
  public fun instrumentAccent(term: TermBuilder.() -> Term): Field =
    add(InstrumentAccent, TermBuilder().term())

  /** (part of) a tag attached to the instrument */
  public fun tag(term: String): Field = tag { Term(term) }
  public fun tag(build: TermBuilder.() -> Term): Field = add(Tag, TermBuilder().build())

  /** the instrument's [type](https://musicbrainz.org/doc/Instrument#Type)  */
  public fun type(type: String): Field = type { Term(type) }
  public fun type(build: TermBuilder.() -> Term): Field = add(Type, TermBuilder().build())

  public companion object {
    public inline operator fun invoke(search: InstrumentSearch.() -> Unit): String {
      return InstrumentSearch().apply(search).toString()
    }
  }
}
