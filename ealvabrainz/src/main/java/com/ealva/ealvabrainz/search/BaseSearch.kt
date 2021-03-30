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

import com.ealva.ealvabrainz.brainz.data.EntitySearchField
import com.ealva.ealvabrainz.lucene.AndExp
import com.ealva.ealvabrainz.lucene.BaseExpression
import com.ealva.ealvabrainz.lucene.Field
import com.ealva.ealvabrainz.lucene.OrExp
import com.ealva.ealvabrainz.lucene.ProhibitField
import com.ealva.ealvabrainz.lucene.Query
import com.ealva.ealvabrainz.lucene.RequireField
import com.ealva.ealvabrainz.lucene.Term
import com.ealva.ealvabrainz.lucene.appendExpression

/**
 * Base of the lucene query builder DSL
 */
public abstract class BaseSearch<F : EntitySearchField>(
  private val query: Query = Query()
) : BaseExpression() {
  public fun add(field: F, term: Term): Field = Field(field.value, term).also { query.add(it) }

  public operator fun Field.not(): Field {
    return query.replaceOrAdd(this, prohibit())
  }

  public operator fun Field.unaryMinus(): Field {
    return query.replaceOrAdd(this, prohibit())
  }

  public operator fun Field.unaryPlus(): Field {
    return query.replaceOrAdd(this, require())
  }

  public infix fun Field.and(field: Field): Field {
    query.remove(field) // field likely added when made, so remove it before "and"ing to this
    return query.replaceOrAdd(this, andTo(field))
  }

  public infix fun Field.or(field: Field): Field {
    query.remove(field) // field likely added when made, so remove it before "and"ing to this
    return query.replaceOrAdd(this, orTo(field))
  }

  override fun appendTo(builder: StringBuilder): StringBuilder = builder.apply {
    builder.appendExpression(query)
  }

  private fun Field.prohibit(): Field = ProhibitField(this)
  private fun Field.require(): Field = RequireField(this)

  private fun Field.andTo(other: Field): Field = when {
    this is AndExp && other is AndExp -> AndExp(fields + other.fields)
    this is AndExp -> AndExp(fields + other)
    other is AndExp -> AndExp(
      ArrayList<Field>(other.fields.size + 1).also {
        it.add(this)
        it.addAll(other.fields)
      }
    )
    else -> AndExp(listOf(this, other))
  }

  private fun Field.orTo(other: Field): Field = when {
    this is OrExp && other is OrExp -> OrExp(fields + other.fields)
    this is OrExp -> OrExp(fields + other)
    other is OrExp -> OrExp(
      ArrayList<Field>(other.fields.size + 1).also {
        it.add(this)
        it.addAll(other.fields)
      }
    )
    else -> OrExp(listOf(this, other))
  }
}
