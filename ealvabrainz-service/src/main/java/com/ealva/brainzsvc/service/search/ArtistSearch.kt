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

import com.ealva.brainzsvc.common.ArtistName
import com.ealva.brainzsvc.common.brainzFormat
import com.ealva.ealvabrainz.brainz.data.Artist.SearchField
import com.ealva.ealvabrainz.brainz.data.ArtistMbid
import com.ealva.ealvabrainz.brainz.data.ArtistType
import com.ealva.ealvabrainz.lucene.BrainzMarker
import com.ealva.ealvabrainz.lucene.Field
import com.ealva.ealvabrainz.lucene.Term
import java.time.LocalDate
import java.util.Date
import kotlin.experimental.ExperimentalTypeInference

@OptIn(ExperimentalTypeInference::class)
@BrainzMarker
public class ArtistSearch : BaseSearch() {

  @JvmName("aliasTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun alias(term: () -> Term): Field = make(SearchField.Alias, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun alias(term: () -> String): Field = make(SearchField.Alias, term())

  @JvmName("primaryAliasTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun primaryAlias(term: () -> Term): Field = make(SearchField.PrimaryAlias, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun primaryAlias(term: () -> String): Field = make(SearchField.PrimaryAlias, term())

  @JvmName("areaTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun area(term: () -> Term): Field = make(SearchField.Area, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun area(term: () -> String): Field = make(SearchField.Area, term())

  @JvmName("artistAccentTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun artistAccent(term: () -> Term): Field = make(SearchField.ArtistAccent, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun artistAccent(term: () -> String): Field = make(SearchField.ArtistAccent, term())

  @JvmName("artistIdTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun artistId(term: () -> Term): Field = make(SearchField.ArtistId, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun artistId(mbid: () -> ArtistMbid): Field =
    make(SearchField.ArtistId, mbid())

  @JvmName("artistTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun artist(term: () -> Term): Field = make(SearchField.Artist, term())

  @JvmName("artistName")
  @OverloadResolutionByLambdaReturnType
  public inline fun artist(name: () -> ArtistName): Field = make(SearchField.Artist, name().value)

  @JvmName("beginAreaTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun beginArea(term: () -> Term): Field = make(SearchField.BeginArea, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun beginArea(term: () -> String): Field = make(SearchField.BeginArea, term())

  @JvmName("beginDateTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun beginDate(term: () -> Term): Field = make(SearchField.Begin, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun beginDate(term: () -> LocalDate): Field =
    make(SearchField.Begin, term().brainzFormat())

  @JvmName("beginDateOld")
  @OverloadResolutionByLambdaReturnType
  public inline fun beginDate(term: () -> Date): Field =
    make(SearchField.Begin, term().brainzFormat())

  @JvmName("beginDateString")
  @OverloadResolutionByLambdaReturnType
  public inline fun beginDate(term: () -> String): Field =
    make(SearchField.Begin, term())

  @JvmName("commentTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun comment(term: () -> Term): Field = make(SearchField.Comment, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun comment(term: () -> String): Field =
    make(SearchField.Comment, term())

  @JvmName("countryTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun country(term: () -> Term): Field = make(SearchField.Country, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun country(term: () -> String): Field =
    make(SearchField.Country, term())

  @JvmName("endAreaTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun endArea(term: () -> Term): Field = make(SearchField.EndArea, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun endArea(term: () -> String): Field = make(SearchField.EndArea, term())

  @JvmName("endDateTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun endDate(term: () -> Term): Field = make(SearchField.End, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun endDate(term: () -> LocalDate): Field =
    make(SearchField.End, term().brainzFormat())

  @JvmName("endDateOld")
  @OverloadResolutionByLambdaReturnType
  public inline fun endDate(term: () -> Date): Field =
    make(SearchField.End, term().brainzFormat())

  @JvmName("endDateString")
  @OverloadResolutionByLambdaReturnType
  public inline fun endDate(term: () -> String): Field =
    make(SearchField.End, term())

  @JvmName("endedTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun ended(term: () -> Term): Field = make(SearchField.Ended, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun ended(term: () -> Boolean): Field =
    make(SearchField.Ended, term())

  @JvmName("genderTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun gender(term: () -> Term): Field = make(SearchField.Gender, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun gender(term: () -> String): Field = make(SearchField.Gender, term())

  @JvmName("ipiTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun ipi(term: () -> Term): Field = make(SearchField.Ipi, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun ipi(term: () -> String): Field = make(SearchField.Ipi, term())

  @JvmName("isniTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun isni(term: () -> Term): Field = make(SearchField.Isni, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun isni(term: () -> String): Field = make(SearchField.Isni, term())

  @JvmName("sortNameTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun sortName(term: () -> Term): Field = make(SearchField.SortName, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun sortName(term: () -> String): Field = make(SearchField.SortName, term())

  @JvmName("tagTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun tag(term: () -> Term): Field = make(SearchField.Tag, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun tag(term: () -> String): Field = make(SearchField.Tag, term())

  @JvmName("typeTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun type(term: () -> Term): Field = make(SearchField.Type, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun type(type: () -> ArtistType): Field = make(SearchField.Type, type().value)

  public fun <T> make(field: SearchField, term: T): Field =
    makeAndAdd(field.value, term)
}
