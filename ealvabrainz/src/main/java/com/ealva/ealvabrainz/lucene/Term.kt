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

@Suppress("NOTHING_TO_INLINE")
public inline fun String.toTerm(): Term = Term(this)

@Suppress("MaxLineLength")
/**
 * A query is broken up into terms and operators. There are two types of terms: Single Terms and
 * Phrases.
 * * A Single Term is a single word such as "test" or "hello".
 * * A Phrase is a group of words surrounded by double quotes such as "hello dolly".
 * * Multiple terms can be combined together with Boolean operators to form a more complex query
 *
 * Lucene supports single and multiple character wildcard searches within single terms (not within
 * phrase queries). To perform a single character wildcard search use the "?" symbol. To perform a
 * multiple character wildcard search use the "*" symbol.
 *
 * See MusicBrainz [search](https://musicbrainz.org/doc/MusicBrainz_API/Search) page and the Lucene
 * [query parser](https://lucene.apache.org/core/7_7_2/queryparser/org/apache/lucene/queryparser/classic/package-summary.html#Terms)
 * docs for details on the query string format.
 */
public sealed class Term : BaseExpression() {
  public companion object {
    /**
     * Constructs a [SingleTerm] or [Phrase] depending on if the [text] contains any whitespace
     * after trimming the ends. Only [SingleTerm]s can by [fuzzy][FuzzyTerm] and only [Phrase]s can
     * have a [proximity][ProximityPhrase].
     */
    public operator fun invoke(text: String): Term = text.trim().let { str ->
      if (str.any { it.isWhitespace() }) Phrase(str) else SingleTerm(str)
    }
  }
}

public class SingleTerm internal constructor(private val value: String) : Term() {
  override fun appendTo(builder: StringBuilder): StringBuilder = builder.apply {
    append(value.luceneEscape())
  }
}

public class Phrase internal constructor(private val value: String) : Term() {
  override fun appendTo(builder: StringBuilder): StringBuilder = builder.apply {
    append('"').append(value.luceneEscape()).append('"')
  }
}

@Suppress("NOTHING_TO_INLINE")
public inline fun String.toRegExTerm(): Term = RegExTerm(this)

@Suppress("KDocUnresolvedReference")
/**
 * Creates a Regular Expression term which will be surrounded by '/' characters.  "abc" -> "\/abc\/"
 * (the '/' must be escaped with a '\'). The text will not be trimmed as with a normal term as
 * whitespace at the ends of a regex string may be part of the regex.
 *
 * Lucene supports regular expression searches matching a pattern between forward slashes "/". The
 * syntax may change across releases, but the current supported syntax is documented in the RegExp
 * class. For example to find documents containing "moat" or "boat": /[[mb]]oat/
 */
public class RegExTerm(private val value: String) : Term() {
  override fun appendTo(builder: StringBuilder): StringBuilder = builder.apply {
    append("\\/").append(value.luceneEscape()).append("\\/")
  }
}

/**
 * A +term indicates the term is required to appear in the result of the query
 */
public operator fun Term.unaryPlus(): RequireTerm = RequireTerm(this)

public fun Term.require(): RequireTerm = RequireTerm(this)

/**
 * Prefix the term with a '+' to indicate the term is required in the query result.
 */
public class RequireTerm(private val term: Term) : Term() {
  override fun appendTo(builder: StringBuilder): StringBuilder = builder.apply {
    builder.append("\\+").appendExpression(term)
  }
}

/**
 * A -term indicates the term must not appear in the result of the query
 */
public operator fun Term.unaryMinus(): ProhibitTerm = ProhibitTerm(this)

/**
 * A !term indicates the term must not appear in the result of the query. The actual string will
 * be prefixed with a '-' character as that is proper for lucene
 */
public operator fun Term.not(): ProhibitTerm = ProhibitTerm(this)

public fun Term.prohibit(): ProhibitTerm = ProhibitTerm(this)

/**
 * Prefix the term with a '-' to indicate the term should not appear in the query result.
 */
public class ProhibitTerm(private val term: Term) : Term() {
  override fun appendTo(builder: StringBuilder): StringBuilder = builder.apply {
    builder.append("\\-").appendExpression(term)
  }
}

public fun Term.fuzzy(maxEditsAllowed: Int = 2): FuzzyTerm {
  require(this is SingleTerm) { "Only SingleTerms may be fuzzy" }
  return FuzzyTerm(this, maxEditsAllowed)
}

/**
 * Lucene supports fuzzy searches based on Damerau-Levenshtein Distance. To do a fuzzy search use
 * the tilde, "~", symbol at the end of a Single word Term. For example to search for a term similar
 * in spelling to "roam" use the fuzzy search:
 *
 * roam~
 *
 * This search will find terms like foam and roams.
 *
 * An additional (optional) parameter can specify the maximum number of edits allowed. The value is
 * between 0 and 2, For example:
 *
 * roam~1
 *
 * The default that is used if the parameter is not given is 2 edit distances.
 */
public class FuzzyTerm(
  private val term: SingleTerm,
  private val maxEditsAllowed: Int = 2
) : Term() {
  init {
    require(maxEditsAllowed in 0..2)
  }

  override fun appendTo(builder: StringBuilder): StringBuilder = builder.apply {
    append(term).append("\\~").append(maxEditsAllowed)
  }
}

public fun Term.proximity(proximity: Int): ProximityPhrase {
  require(this is Phrase) { "Only Phrases may have proximity" }
  return ProximityPhrase(this, proximity)
}

/**
 * Lucene supports finding words are a within a specific distance away. To do a proximity search use
 * the tilde, "~", symbol at the end of a Phrase. For example to search for a "apache" and "jakarta"
 * within 10 words of each other in a document use the search:
 *
 * "jakarta apache"~10
 */
public class ProximityPhrase(
  private val term: Phrase,
  private val proximity: Int
) : Term() {
  override fun appendTo(builder: StringBuilder): StringBuilder = builder.apply {
    append(term).append("\\~").append(proximity)
  }
}

public fun Term.boost(boost: Int): BoostTerm = BoostTerm(this, boost)

/**
 * Lucene provides the relevance level of matching documents based on the terms found. To boost a
 * term use the caret, "^", symbol with a boost factor (a number) at the end of the term you are
 * searching. The higher the boost factor, the more relevant the term will be.
 *
 * Boosting allows you to control the relevance of a document by boosting its term. For example, if
 * you are searching for
 *
 * jakarta apache
 *
 * and you want the term "jakarta" to be more relevant boost it using the ^ symbol along with the
 * boost factor next to the term. You would type:
 *
 * jakarta^4 apache
 *
 * This will make documents with the term jakarta appear more relevant. You can also boost Phrase
 * Terms as in the example:
 *
 * "jakarta apache"^4 "Apache Lucene"
 *
 * Lucene allows fractional boost, but we're requiring an Int
 */
public class BoostTerm(private val term: Term, private val boost: Int) : Term() {
  override fun appendTo(builder: StringBuilder): StringBuilder = builder.apply {
    appendExpression(term).append("\\^").append(boost)
  }
}

/**
 * A compound term is simply list of terms separated by an [operator] string and enclosed in
 * parenthesis.
 */
public sealed class CompoundTerm(
  private val operator: String,
  internal val terms: List<Term>
) : Term() {
  override fun appendTo(builder: StringBuilder): StringBuilder = builder.apply {
    append('(')
    terms.forEachIndexed { index, term ->
      if (index > 0) append(operator)
      appendExpression(term)
    }
    append(')')
  }
}

public class AndOp(terms: List<Term>) : CompoundTerm(" AND ", terms)
public class OrOp(terms: List<Term>) : CompoundTerm(" OR ", terms)

public infix fun Term.and(other: Term): Term = when {
  this is AndOp && other is AndOp -> AndOp(terms + other.terms)
  this is AndOp -> AndOp(terms + other)
  other is AndOp -> AndOp(
    ArrayList<Term>(other.terms.size + 1).also {
      it.add(this)
      it.addAll(other.terms)
    }
  )
  else -> AndOp(listOf(this, other))
}

public infix fun Term.and(other: String): Term = and(other.toTerm())

public infix fun Term.or(other: Term): Term = when {
  this is OrOp && other is OrOp -> OrOp(terms + other.terms)
  this is OrOp -> OrOp(terms + other)
  other is OrOp -> OrOp(
    ArrayList<Term>(other.terms.size + 1).also {
      it.add(this)
      it.addAll(other.terms)
    }
  )
  else -> OrOp(listOf(this, other))
}

public infix fun Term.or(other: String): Term = or(other.toTerm())

public infix fun Term.inclusive(other: Term): InclusiveRange = InclusiveRange(this, other)
public fun Pair<Term, Term>.inclusive(): InclusiveRange = InclusiveRange(first, second)

/**
 * Range Queries allow one to match documents whose field(s) values are between the lower and upper
 * bound specified by the Range Query. Range Queries can be inclusive or exclusive of the upper and
 * lower bounds. Sorting is done lexicographically.
 *
 * mod_date:[20020101 TO 20030101]
 *
 * This will find documents whose mod_date fields have values between 20020101 and 20030101,
 * inclusive. Note that Range Queries are not reserved for date fields. You could also use range
 * queries with non-date fields:
 *
 * Inclusive range queries are denoted by square brackets.
 */
public class InclusiveRange(private val from: Term, private val to: Term) : Term() {
  override fun appendTo(builder: StringBuilder): StringBuilder = builder.apply {
    append("\\[").appendExpression(from).append(" TO ").appendExpression(to).append("\\]")
  }
}

public infix fun Term.exclusive(other: Term): ExclusiveRange = ExclusiveRange(this, other)
public fun Pair<Term, Term>.exclusive(): ExclusiveRange = ExclusiveRange(first, second)

/**
 * Range Queries allow one to match documents whose field(s) values are between the lower and upper
 * bound specified by the Range Query. Range Queries can be inclusive or exclusive of the upper and
 * lower bounds. Sorting is done lexicographically.
 *
 * title:{Aida TO Carmen}
 *
 * This will find all documents whose titles are between Aida and Carmen, but not including Aida and
 * Carmen.
 *
 * Exclusive range queries are denoted by curly brackets.
 */
public class ExclusiveRange(private val from: Term, private val to: Term) : Term() {
  override fun appendTo(builder: StringBuilder): StringBuilder = builder.apply {
    append("\\{").appendExpression(from).append(" TO ").appendExpression(to).append("\\}")
  }
}
