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

@file:Suppress("NOTHING_TO_INLINE")

package com.ealva.ealvabrainz.lucene

import com.ealva.ealvabrainz.brainz.data.ArtistType
import com.ealva.ealvabrainz.brainz.data.Release
import com.ealva.ealvabrainz.common.AlbumTitle
import com.ealva.ealvabrainz.common.AreaMbid
import com.ealva.ealvabrainz.common.ArtistMbid
import com.ealva.ealvabrainz.common.ArtistName
import com.ealva.ealvabrainz.common.BrainzMarker
import com.ealva.ealvabrainz.common.DiscId
import com.ealva.ealvabrainz.common.EventMbid
import com.ealva.ealvabrainz.common.GenreMbid
import com.ealva.ealvabrainz.common.InstrumentMbid
import com.ealva.ealvabrainz.common.LabelMbid
import com.ealva.ealvabrainz.common.LabelName
import com.ealva.ealvabrainz.common.PackagingMbid
import com.ealva.ealvabrainz.common.PlaceMbid
import com.ealva.ealvabrainz.common.RecordingMbid
import com.ealva.ealvabrainz.common.RecordingTitle
import com.ealva.ealvabrainz.common.ReleaseGroupMbid
import com.ealva.ealvabrainz.common.ReleaseMbid
import com.ealva.ealvabrainz.common.SeriesMbid
import com.ealva.ealvabrainz.common.TrackMbid
import com.ealva.ealvabrainz.common.UrlMbid
import com.ealva.ealvabrainz.common.WorkMbid
import com.ealva.ealvabrainz.common.WorkName
import com.ealva.ealvabrainz.common.brainzFormat
import java.time.LocalDate
import java.util.Date

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
 * Terms should be immutable. Operations should produce new terms.
 *
 * See MusicBrainz [search](https://musicbrainz.org/doc/MusicBrainz_API/Search) page and the Lucene
 * [query parser](https://lucene.apache.org/core/7_7_2/queryparser/org/apache/lucene/queryparser/classic/package-summary.html#Terms)
 * docs for details on the query string format.
 */
@BrainzMarker
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

    /*
     * NOTE: While this is a very long list of type to Term conversion functions, we'll provide
     * these as a user convenience and let the compiler decide the function to be called instead of
     * a large when() making a runtime decision. Since SingleTerm and Phrase are public this is
     * still open to change as new Terms may be introduced (which should be a very rare occurrence
     * once this library is version 1.0 complete)
    */

    // All MBID listed separately, instead of as Mbid, so they don't have to be boxed
    public inline operator fun invoke(mbid: AreaMbid): Term = SingleTerm(mbid.value)
    public inline operator fun invoke(mbid: ArtistMbid): Term = SingleTerm(mbid.value)
    public inline operator fun invoke(mbid: EventMbid): Term = SingleTerm(mbid.value)
    public inline operator fun invoke(mbid: GenreMbid): Term = SingleTerm(mbid.value)
    public inline operator fun invoke(mbid: InstrumentMbid): Term = SingleTerm(mbid.value)
    public inline operator fun invoke(mbid: LabelMbid): Term = SingleTerm(mbid.value)
    public inline operator fun invoke(mbid: PlaceMbid): Term = SingleTerm(mbid.value)
    public inline operator fun invoke(mbid: RecordingMbid): Term = SingleTerm(mbid.value)
    public inline operator fun invoke(mbid: ReleaseMbid): Term = SingleTerm(mbid.value)
    public inline operator fun invoke(mbid: ReleaseGroupMbid): Term = SingleTerm(mbid.value)
    public inline operator fun invoke(mbid: SeriesMbid): Term = SingleTerm(mbid.value)
    public inline operator fun invoke(mbid: WorkMbid): Term = SingleTerm(mbid.value)
    public inline operator fun invoke(mbid: UrlMbid): Term = SingleTerm(mbid.value)
    public inline operator fun invoke(mbid: TrackMbid): Term = SingleTerm(mbid.value)
    public inline operator fun invoke(mbid: PackagingMbid): Term = SingleTerm(mbid.value)

    public inline operator fun invoke(title: AlbumTitle): Term = Term(title.value)
    public inline operator fun invoke(type: ArtistType): Term = Term(type.value)
    public inline operator fun invoke(name: ArtistName): Term = Term(name.value)
    public inline operator fun invoke(discId: DiscId): Term = Term(discId.value)
    public inline operator fun invoke(name: LabelName): Term = Term(name.value)
    public inline operator fun invoke(title: RecordingTitle): Term = Term(title.value)
    public inline operator fun invoke(name: WorkName): Term = Term(name.value)
    public inline operator fun invoke(type: Release.Type): Term = SingleTerm(type.value)
    public inline operator fun invoke(status: Release.Status): Term = SingleTerm(status.value)

    public inline operator fun invoke(value: Int): Term = SingleTerm(value.toString())
    public inline operator fun invoke(value: Long): Term = SingleTerm(value.toString())
    public inline operator fun invoke(value: Boolean): Term = SingleTerm(value.toString())
    public inline operator fun invoke(date: LocalDate): Term = SingleTerm(date.brainzFormat())
    public inline operator fun invoke(date: Date): Term = SingleTerm(date.brainzFormat())
  }
}

public open class SingleTerm(private val value: String) : Term() {
  override fun appendTo(builder: StringBuilder): StringBuilder = builder.apply {
    append(value.luceneEscape())
  }
}

public class Phrase internal constructor(private val value: String) : Term() {
  override fun appendTo(builder: StringBuilder): StringBuilder = builder.apply {
    append('"').append(value.luceneEscape()).append('"')
  }
}

public inline fun String.toRegExTerm(): Term = RegExTerm(this)

@Suppress("KDocUnresolvedReference") // how to escape brackets? []
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
public operator fun Term.unaryPlus(): RequireTerm = require()

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
public operator fun Term.unaryMinus(): ProhibitTerm = prohibit()

/**
 * A !term indicates the term must not appear in the result of the query. The actual string will
 * be prefixed with a '-' character as that is canonical for lucene
 */
public operator fun Term.not(): ProhibitTerm = prohibit()

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
 * mod_date:[2002-01-01 TO 2003-01-01]
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
