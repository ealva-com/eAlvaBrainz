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

import com.ealva.ealvabrainz.brainz.data.Annotation.SearchField
import com.ealva.ealvabrainz.brainz.data.Annotation.SearchField.EntityName
import com.ealva.ealvabrainz.brainz.data.Annotation.SearchField.EntityType
import com.ealva.ealvabrainz.brainz.data.Annotation.SearchField.Text
import com.ealva.ealvabrainz.common.BrainzMarker
import com.ealva.ealvabrainz.brainz.data.Mbid
import com.ealva.ealvabrainz.lucene.Field
import com.ealva.ealvabrainz.lucene.Term
import kotlin.experimental.ExperimentalTypeInference

@OptIn(ExperimentalTypeInference::class)
@BrainzMarker
public class AnnotationSearch : BaseSearch<SearchField>() {
  @JvmName("defaultTerm")
  @OverloadResolutionByLambdaReturnType
  /** Default searches [EntityName], [Text] and [EntityType] */
  public inline fun default(term: () -> Term): Field = add(SearchField.Default, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun default(default: () -> String): Field = default { Term(default()) }

  @JvmName("entityTerm")
  @OverloadResolutionByLambdaReturnType
  /** The annotated entity's MBID */
  public inline fun entity(term: () -> Term): Field = add(SearchField.Entity, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun entity(entity: () -> Mbid): Field = entity { Term(entity().value) }

  @JvmName("idTerm")
  @OverloadResolutionByLambdaReturnType
  /** The numeric ID of the annotation (e.g. 703027)  */
  public inline fun id(term: () -> Term): Field = add(SearchField.Id, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun id(id: () -> Long): Field = id { Term(id()) }

  @JvmName("nameTerm")
  @OverloadResolutionByLambdaReturnType
  /** The annotated entity's name or title (diacritics are ignored) */
  public inline fun name(term: () -> Term): Field = add(EntityName, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun name(name: () -> String): Field = name { Term(name()) }

  @JvmName("textTerm")
  @OverloadResolutionByLambdaReturnType
  /**
   * The annotation's content (includes
   * [wiki formatting](https://musicbrainz.org/doc/Annotation#Wiki_formatting))
   */
  public inline fun text(term: () -> Term): Field = add(Text, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun text(text: () -> String): Field = text { Term(text()) }

  @JvmName("typeTerm")
  @OverloadResolutionByLambdaReturnType
  /** The annotated entity's entity type */
  public inline fun type(term: () -> Term): Field = add(EntityType, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun type(type: () -> String): Field = add(EntityType, Term(type()))

  public companion object {
    public inline operator fun invoke(search: AnnotationSearch.() -> Unit): String {
      return AnnotationSearch().apply(search).toString()
    }
  }
}
