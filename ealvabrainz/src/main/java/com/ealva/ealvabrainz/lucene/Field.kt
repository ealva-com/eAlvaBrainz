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

public abstract class SearchField : BaseExpression()

@Suppress("MaxLineLength")
/**
 * Lucene supports fielded data. When performing a search you can either specify a field, or use the
 * default field. The field names and default field is implementation specific.
 *
 * You can search any field by typing the field name followed by a colon ":" and then the term you
 * are looking for.
 *
 * As an example, let's assume a Lucene index contains two fields, title and text and text is the
 * default field. If you want to find the document entitled "The Right Way" which contains the text
 * "don't go this way", you can enter:
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
 * Will only find "The" in the title field. It will find "Right" and "Way" in the default field (in
 * this case the text field).
 *
 * See MusicBrainz [search](https://musicbrainz.org/doc/MusicBrainz_API/Search) page and the Lucene
 * [query parser](https://lucene.apache.org/core/7_7_2/queryparser/org/apache/lucene/queryparser/classic/package-summary.html#Terms)
 * docs for details on the query string format.
 */
public class Field(private val name: String, private val terms: List<Term>) : SearchField() {
  override fun appendTo(builder: StringBuilder): StringBuilder = builder.apply {
    val termCount = terms.size
    if (termCount > 0) {
      append(name).append(':')
      if (termCount > 1) append('(')
      terms.forEachIndexed { index, term ->
        if (index > 0) append(' ')
        appendExpression(term)
      }
      if (termCount > 1) append(')')
    } else append("-").append(name).append(':').append('*')
  }

  public companion object {
    public operator fun invoke(name: String, term: Term): Field = Field(name, listOf(term))
    public operator fun invoke(name: String, term: Term, vararg trailing: Term): Field =
      Field(name, listOf(term, *trailing))
    public operator fun invoke(name: String, term: String): Field = Field(name, listOf(Term(term)))
    public operator fun invoke(name: String, term: String, vararg trailing: String): Field =
      Field(name, mutableListOf(term.toTerm()).apply { trailing.forEach { add(it.toTerm()) } })
  }
}

public fun Pair<String, String>.toField(): Field = Field(first, second)

public abstract class FieldExpression(
  private val operator: String,
  internal val fields: List<SearchField>
) : SearchField() {
  override fun appendTo(builder: StringBuilder): StringBuilder = builder.apply {
    append('(')
    fields.forEachIndexed { index, field ->
      if (index > 0) append(operator)
      appendExpression(field)
    }
    append(')')
  }
}

public class AndExp(fields: List<SearchField>) : FieldExpression(" AND ", fields)
public class OrExp(fields: List<SearchField>) : FieldExpression(" OR ", fields)

public infix fun SearchField.and(other: SearchField): SearchField = when {
  this is AndExp && other is AndExp -> AndExp(fields + other.fields)
  this is AndExp -> AndExp(fields + other)
  other is AndExp -> AndExp(
    ArrayList<SearchField>(other.fields.size + 1).also {
      it.add(this)
      it.addAll(other.fields)
    }
  )
  else -> AndExp(listOf(this, other))
}

public infix fun SearchField.or(other: SearchField): SearchField = when {
  this is OrExp && other is OrExp -> OrExp(fields + other.fields)
  this is OrExp -> OrExp(fields + other)
  other is OrExp -> OrExp(
    ArrayList<SearchField>(other.fields.size + 1).also {
      it.add(this)
      it.addAll(other.fields)
    }
  )
  else -> OrExp(listOf(this, other))
}
