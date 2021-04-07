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
import com.ealva.ealvabrainz.brainz.data.Annotation.SearchField.Default
import com.ealva.ealvabrainz.brainz.data.Annotation.SearchField.Entity
import com.ealva.ealvabrainz.brainz.data.Annotation.SearchField.EntityName
import com.ealva.ealvabrainz.brainz.data.Annotation.SearchField.EntityType
import com.ealva.ealvabrainz.brainz.data.Annotation.SearchField.Id
import com.ealva.ealvabrainz.brainz.data.Annotation.SearchField.Text
import com.ealva.ealvabrainz.brainz.data.Mbid
import com.ealva.ealvabrainz.lucene.Field
import com.ealva.ealvabrainz.lucene.Term
import com.ealva.ealvabrainz.search.term.MbidTermBuilder
import com.ealva.ealvabrainz.search.term.TermBuilder
import kotlin.experimental.ExperimentalTypeInference
import com.ealva.ealvabrainz.search.term.NumberTermBuilder as NumBuilder

@OptIn(ExperimentalTypeInference::class)
public class AnnotationSearch : BaseSearch<SearchField>() {
  /** Default searches [EntityName], [Text] and [EntityType] */
  public fun default(name: String): Field = default { Term(name) }
  public fun default(build: TermBuilder.() -> Term): Field = add(Default, TermBuilder().build())

  /** The annotated entity's MBID */
  public fun <T : Mbid> entity(entity: T): Field = entity<T> { Term(entity.value) }
  public fun <T : Mbid> entity(build: MbidTermBuilder<T>.() -> Term): Field =
    add(Entity, MbidTermBuilder<T>().build())

  /** The numeric ID of the annotation (e.g. 703027)  */
  public fun id(id: Long): Field = id { Term(id) }
  public fun id(build: NumBuilder.() -> Term): Field = add(Id, NumBuilder().build())

  /** The annotated entity's name or title (diacritics are ignored) */
  public fun name(name: String): Field = name { Term(name) }
  public fun name(build: TermBuilder.() -> Term): Field = add(EntityName, TermBuilder().build())

  /**
   * The annotation's content (includes
   * [wiki formatting](https://musicbrainz.org/doc/Annotation#Wiki_formatting))
   */
  public fun text(text: String): Field = text { Term(text) }
  public fun text(build: TermBuilder.() -> Term): Field = add(Text, TermBuilder().build())

  /** The annotated entity's entity type */
  public fun type(type: String): Field = type { Term(type) }
  public fun type(build: TermBuilder.() -> Term): Field = add(EntityType, TermBuilder().build())

  public companion object {
    public inline operator fun invoke(search: AnnotationSearch.() -> Unit): String {
      return AnnotationSearch().apply(search).toString()
    }
  }
}
