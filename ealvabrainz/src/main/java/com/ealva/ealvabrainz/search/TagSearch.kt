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
import com.ealva.ealvabrainz.brainz.data.Tag.SearchField.Tag
import com.ealva.ealvabrainz.common.BrainzMarker
import com.ealva.ealvabrainz.lucene.Field
import com.ealva.ealvabrainz.lucene.Query
import com.ealva.ealvabrainz.lucene.Term
import kotlin.experimental.ExperimentalTypeInference

@OptIn(ExperimentalTypeInference::class)
@BrainzMarker
public class TagSearch(query: Query = Query()) : BaseSearch<SearchField>(query) {
  @JvmName("defaultTerm")
  @OverloadResolutionByLambdaReturnType
  /** Default searches [Tag] */
  public inline fun default(term: () -> Term): Field = add(SearchField.Default, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun default(crossinline term: () -> String): Field = default { Term(term()) }

  @JvmName("tagTerm")
  @OverloadResolutionByLambdaReturnType
  /** (part of) the tag's name  */
  public inline fun tag(term: () -> Term): Field = add(Tag, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun tag(term: () -> String): Field = tag { Term(term()) }

  public companion object {
    public inline operator fun invoke(search: TagSearch.() -> Unit): String {
      return TagSearch().apply(search).toString()
    }
  }
}
