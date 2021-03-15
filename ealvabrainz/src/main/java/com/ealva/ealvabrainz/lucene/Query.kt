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

package com.ealva.ealvabrainz.lucene

import it.unimi.dsi.fastutil.objects.ReferenceArraySet
import it.unimi.dsi.fastutil.objects.ReferenceLinkedOpenHashSet
import it.unimi.dsi.fastutil.objects.ReferenceSet

private const val INITIAL_CAPACITY = 8

public fun <T> refSetOf(capacity: Int = INITIAL_CAPACITY): ReferenceSet<T> =
  ReferenceArraySet(capacity)

public fun refSetOf(field: Field): ReferenceSet<Field> = refSetOf<Field>().apply { add(field) }

private fun Pair<String, String>.toField(): Field = Field(first, Term(second))

public interface Query : Expression {
  public fun add(field: Field): Boolean

  public fun remove(field: Field): Boolean

  /**
   * Replace [original] with [replacement] and returns [replacement]
   */
  public fun replace(original: Field, replacement: Field): Field

  public companion object {
    public operator fun invoke(): Query = QueryImpl()
    public operator fun invoke(field: Field): Query = QueryImpl(refSetOf(field))
    public operator fun invoke(fieldPair: Pair<String, String>): Query =
      QueryImpl(refSetOf(fieldPair.toField()))

    public operator fun invoke(
      fieldPair: Pair<String, String>,
      vararg trailing: Pair<String, String>
    ): Query = QueryImpl(
      refSetOf(fieldPair.toField()).apply { trailing.forEach { add(it.toField()) } }
    )
  }
}

private class QueryImpl(
  private var fields: ReferenceSet<Field> = refSetOf()
) : BaseExpression(), Query {

  override fun add(field: Field): Boolean = fields.add(field)

  override fun remove(field: Field): Boolean = fields.remove(field)

  override fun replace(original: Field, replacement: Field): Field {
    val currentFields = fields
    fields = ReferenceLinkedOpenHashSet<Field>(currentFields.size).also { newTerms ->
      currentFields.forEach { field ->
        newTerms.add(if (field === original) replacement else field)
      }
    }
    return replacement
  }

  override fun appendTo(builder: StringBuilder): StringBuilder = builder.apply {
    fields.forEachIndexed { index, searchField ->
      if (index > 0) append(' ')
      appendExpression(searchField)
    }
  }
}
