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

package com.ealva.brainzsvc.service.search

import com.ealva.ealvabrainz.brainz.data.Mbid
import com.ealva.ealvabrainz.lucene.BaseExpression
import com.ealva.ealvabrainz.lucene.BooleanTerm
import com.ealva.ealvabrainz.lucene.Field
import com.ealva.ealvabrainz.lucene.IntTerm
import com.ealva.ealvabrainz.lucene.LongTerm
import com.ealva.ealvabrainz.lucene.Query
import com.ealva.ealvabrainz.lucene.SingleTerm
import com.ealva.ealvabrainz.lucene.Term
import com.ealva.ealvabrainz.lucene.andAnother
import com.ealva.ealvabrainz.lucene.orAnother

public abstract class BaseSearch(private val query: Query = Query()) :
  BaseExpression(), Query by query {

  protected fun <T> makeAndAdd(field: String, term: T): Field = when (term) {
    is Mbid -> Field(field, SingleTerm(term.value))
    is Boolean -> Field(field, BooleanTerm(term)).also { query.add(it) }
    is Int -> Field(field, IntTerm(term)).also { query.add(it) }
    is Long -> Field(field, LongTerm(term)).also { query.add(it) }
    is Term -> Field(field, term).also { query.add(it) }
    else -> Field(field, term.toString()).also { query.add(it) }
  }

  public infix fun Field.and(field: Field) {
    query.remove(field) // field likely added when made, so remove it before "and"ing to another
    query.replace(this, this.andAnother(field))
  }

  public infix fun Field.or(field: Field) {
    query.remove(field) // field likely added when made, so remove it before "or"ing to another
    query.replace(this, this.orAnother(field))
  }
}
