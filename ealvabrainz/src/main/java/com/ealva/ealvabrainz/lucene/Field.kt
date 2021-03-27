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

import com.ealva.ealvabrainz.common.BrainzMarker

@BrainzMarker
public sealed class Field : BaseExpression() {
  @Suppress("MaxLineLength")
  /**
   * Lucene supports fielded data. When performing a search you can either specify a field, or use
   * the default field. The field names and default field is implementation specific. eAlvaBrainz
   * does not currently support using the default field, ie. fields must be named.
   *
   * You can search any field by typing the field name followed by a colon ":" and then the term you
   * are looking for.
   *
   * As an example, let's assume a Lucene index contains two fields, title and text and text is the
   * default field. If you want to find the document entitled "The Right Way" which contains the
   * text "don't go this way", you can enter:
   *
   * title:"The Right Way" AND text:go
   *
   * or
   *
   * title:"The Right Way" AND go
   *
   * Since text is the default field, the field indicator is not required.
   *
   * Note: The field is only valid for the term that it directly precedes, so the query
   *
   * title:The Right Way
   *
   * Will only find "The" in the title field. It will find "Right" and "Way" in the default field
   * (in this case the text field).
   *
   * See MusicBrainz [search](https://musicbrainz.org/doc/MusicBrainz_API/Search) page and the Lucene
   * [query parser](https://lucene.apache.org/core/7_7_2/queryparser/org/apache/lucene/queryparser/classic/package-summary.html#Terms)
   * docs for details on the query string format.
   */
  private class BasicField(private val name: String, private val terms: List<Term>) : Field() {
    override fun appendTo(builder: StringBuilder): StringBuilder = builder.apply {
      val termCount = terms.size
      if (termCount > 0) {
        if (name.isNotEmpty()) append(name).append(':')
        if (termCount > 1) append('(')
        terms.forEachIndexed { index, term ->
          if (index > 0) append(' ')
          appendExpression(term)
        }
        if (termCount > 1) append(')')
      } else append("-").append(name).append(':').append('*')
    }
  }

  public companion object {
    public operator fun invoke(name: String, term: Term): Field =
      BasicField(name, listOf(term))
    public operator fun invoke(name: String, term: Term, vararg trailing: Term): Field =
      BasicField(name, listOf(term, *trailing))
  }
}

public sealed class FieldExpression(
  private val operator: String,
  internal val fields: List<Field>
) : Field() {
  override fun appendTo(builder: StringBuilder): StringBuilder = builder.apply {
    append('(')
    fields.forEachIndexed { index, field ->
      if (index > 0) append(operator)
      appendExpression(field)
    }
    append(')')
  }
}

public class AndExp(fields: List<Field>) : FieldExpression(" AND ", fields)
public class OrExp(fields: List<Field>) : FieldExpression(" OR ", fields)

public infix fun Field.and(other: Field): Field = when {
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

/** A renaming of the infix and() just to avoid naming conflict, perceived or actual */
public fun Field.andAnother(other: Field): Field = this.and(other)

public infix fun Field.or(other: Field): Field = when {
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

/** A renaming of the infix or() just to avoid naming conflict, perceived or actual */
public fun Field.orAnother(other: Field): Field = this.or(other)
