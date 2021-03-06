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

@file:Suppress("unused")

package com.ealva.brainzsvc.service.search

import com.ealva.ealvabrainz.brainz.data.Artist.SearchField
import com.ealva.ealvabrainz.brainz.data.ArtistMbid
import com.ealva.ealvabrainz.brainz.data.ArtistType
import com.ealva.ealvabrainz.common.ArtistName
import com.ealva.ealvabrainz.lucene.BrainzMarker
import com.ealva.ealvabrainz.lucene.Field
import com.ealva.ealvabrainz.lucene.Term
import java.time.LocalDate
import java.util.Date
import kotlin.experimental.ExperimentalTypeInference

@OptIn(ExperimentalTypeInference::class)
@BrainzMarker
public class ArtistSearch : BaseSearch() {
  /** (part of) any alias attached to the artist (diacritics are ignored) */
  @JvmName("aliasTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun alias(term: () -> Term): Field = add(SearchField.Alias, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun alias(term: () -> String): Field = alias { Term(term()) }

  /** (part of) any primary alias attached to the artist (diacritics are ignored) */
  @JvmName("primaryAliasTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun primaryAlias(term: () -> Term): Field = add(SearchField.PrimaryAlias, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun primaryAlias(term: () -> String): Field = primaryAlias { Term(term()) }

  /** (part of) the name of the artist's main associated area */
  @JvmName("areaTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun area(term: () -> Term): Field = add(SearchField.Area, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun area(term: () -> String): Field = area { Term(term()) }

  /** the artist's name (with accented characters) */
  @JvmName("artistAccentTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun artistAccent(term: () -> Term): Field = add(SearchField.ArtistAccent, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun artistAccent(term: () -> String): Field = artistAccent { Term(term()) }

  /** the artist's MBID */
  @JvmName("artistIdTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun artistId(term: () -> Term): Field = add(SearchField.ArtistId, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun artistId(mbid: () -> ArtistMbid): Field = artistId { Term(mbid()) }

  /** the artist's name (diacritics are ignored) */
  @JvmName("artistTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun artist(term: () -> Term): Field = add(SearchField.Artist, term())

  @JvmName("artistName")
  @OverloadResolutionByLambdaReturnType
  public inline fun artist(name: () -> ArtistName): Field = artist { Term(name()) }

  /** the artist's begin area */
  @JvmName("beginAreaTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun beginArea(term: () -> Term): Field = add(SearchField.BeginArea, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun beginArea(term: () -> String): Field = beginArea { Term(term()) }

  @JvmName("beginDateTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun beginDate(term: () -> Term): Field = add(SearchField.Begin, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun beginDate(term: () -> LocalDate): Field = beginDate { Term(term()) }

  @JvmName("beginDateOld")
  @OverloadResolutionByLambdaReturnType
  public inline fun beginDate(term: () -> Date): Field = beginDate { Term(term()) }

  /** the artist's disambiguation comment */
  @JvmName("commentTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun comment(term: () -> Term): Field = add(SearchField.Comment, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun comment(term: () -> String): Field = comment { Term(term()) }

  @JvmName("countryTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun country(term: () -> Term): Field = add(SearchField.Country, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun country(term: () -> String): Field = country { Term(term()) }

  @JvmName("endAreaTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun endArea(term: () -> Term): Field = add(SearchField.EndArea, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun endArea(term: () -> String): Field = endArea { Term(term()) }

  @JvmName("endDateTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun endDate(term: () -> Term): Field = add(SearchField.End, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun endDate(term: () -> LocalDate): Field = endDate { Term(term()) }

  @JvmName("endDateOld")
  @OverloadResolutionByLambdaReturnType
  public inline fun endDate(term: () -> Date): Field = endDate { Term(term()) }

  /** a flag indicating whether or not the artist has ended */
  @JvmName("endedTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun ended(term: () -> Term): Field = add(SearchField.Ended, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun ended(term: () -> Boolean): Field = ended { Term(term()) }

  @JvmName("genderTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun gender(term: () -> Term): Field = add(SearchField.Gender, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun gender(term: () -> String): Field = gender { Term(term()) }

  /** A number identifying persons connected to ISWC registered works (authors, composers, etc.) */
  @JvmName("ipiTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun ipi(term: () -> Term): Field = add(SearchField.Ipi, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun ipi(term: () -> String): Field = ipi { Term(term()) }

  @JvmName("isniTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun isni(term: () -> Term): Field = add(SearchField.Isni, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun isni(term: () -> String): Field = isni { Term(term()) }

  /** Used to sort the artist */
  @JvmName("sortNameTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun sortName(term: () -> Term): Field = add(SearchField.SortName, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun sortName(term: () -> String): Field = sortName { Term(term()) }

  /** a tag attached to the artist */
  @JvmName("tagTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun tag(term: () -> Term): Field = add(SearchField.Tag, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun tag(term: () -> String): Field = tag { Term(term()) }

  /** The [artist type][ArtistType] */
  @JvmName("typeTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun type(term: () -> Term): Field = add(SearchField.Type, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun type(type: () -> ArtistType): Field = add(SearchField.Type, Term(type().value))

  public fun add(field: SearchField, term: Term): Field = makeAndAddField(field.value, term)
}
