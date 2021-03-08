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

public class Query(private val fields: List<SearchField>) : BaseExpression() {
  override fun appendTo(builder: StringBuilder): StringBuilder = builder.apply {
    fields.forEachIndexed { index, searchField ->
      if (index > 0) append(' ')
      appendExpression(searchField)
    }
  }

  public companion object {
    public operator fun invoke(field: Field): Query = Query(listOf(field))
    public operator fun invoke(fieldPair: Pair<String, String>): Query =
      Query(listOf(fieldPair.toField()))

    public operator fun invoke(
      fieldPair: Pair<String, String>,
      vararg trailing: Pair<String, String>
    ): Query = Query(
      mutableListOf(fieldPair.toField()).apply {
        trailing.forEach { add(it.toField()) }
      }
    )
  }
}
