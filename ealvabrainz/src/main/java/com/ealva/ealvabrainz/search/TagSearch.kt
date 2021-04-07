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

import com.ealva.ealvabrainz.brainz.data.Tag.SearchField
import com.ealva.ealvabrainz.brainz.data.Tag.SearchField.Default
import com.ealva.ealvabrainz.brainz.data.Tag.SearchField.Tag
import com.ealva.ealvabrainz.common.BrainzMarker
import com.ealva.ealvabrainz.lucene.Field
import com.ealva.ealvabrainz.lucene.Query
import com.ealva.ealvabrainz.lucene.Term
import com.ealva.ealvabrainz.search.term.TermBuilder

@BrainzMarker
public class TagSearch(query: Query = Query()) : BaseSearch<SearchField>(query) {
  /** Default searches [Tag] */
  public fun default(term: String): Field = default { Term(term) }
  public fun default(build: TermBuilder.() -> Term): Field = add(Default, TermBuilder().build())

  /** (part of) the tag's name  */
  public fun tag(build: TermBuilder.() -> Term): Field = add(Tag, TermBuilder().build())
  public fun tag(term: String): Field = tag { Term(term) }

  public companion object {
    public inline operator fun invoke(search: TagSearch.() -> Unit): String =
      TagSearch().apply(search).toString()
  }
}
