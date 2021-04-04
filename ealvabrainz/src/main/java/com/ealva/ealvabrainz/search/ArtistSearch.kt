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

import com.ealva.ealvabrainz.brainz.data.Artist.SearchField
import com.ealva.ealvabrainz.brainz.data.Artist.SearchField.Alias
import com.ealva.ealvabrainz.brainz.data.Artist.SearchField.Artist
import com.ealva.ealvabrainz.brainz.data.Artist.SearchField.SortName
import com.ealva.ealvabrainz.brainz.data.ArtistType
import com.ealva.ealvabrainz.common.AreaName
import com.ealva.ealvabrainz.brainz.data.ArtistMbid
import com.ealva.ealvabrainz.common.ArtistName
import com.ealva.ealvabrainz.common.BrainzMarker
import com.ealva.ealvabrainz.lucene.Field
import com.ealva.ealvabrainz.lucene.Term
import java.time.LocalDate
import java.util.Date
import kotlin.experimental.ExperimentalTypeInference

@Suppress("unused")
@OptIn(ExperimentalTypeInference::class)
@BrainzMarker
public class ArtistSearch : BaseSearch<SearchField>() {
  @JvmName("aliasTerm")
  @OverloadResolutionByLambdaReturnType
  /** (part of) any alias attached to the artist (diacritics are ignored) */
  public inline fun alias(term: () -> Term): Field = add(Alias, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun alias(term: () -> String): Field = alias { Term(term()) }

  @JvmName("primaryAliasTerm")
  @OverloadResolutionByLambdaReturnType
  /** (part of) any primary alias attached to the artist (diacritics are ignored) */
  public inline fun primaryAlias(term: () -> Term): Field = add(SearchField.PrimaryAlias, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun primaryAlias(term: () -> String): Field = primaryAlias { Term(term()) }

  @JvmName("areaTerm")
  @OverloadResolutionByLambdaReturnType
  /** (part of) the name of the artist's main associated area */
  public inline fun area(term: () -> Term): Field = add(SearchField.Area, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun area(term: () -> AreaName): Field = area { Term(term()) }

  @JvmName("artistTerm")
  @OverloadResolutionByLambdaReturnType
  /** the artist's name (diacritics are ignored) */
  public inline fun artist(term: () -> Term): Field = add(Artist, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun artist(name: () -> ArtistName): Field = artist { Term(name()) }

  @JvmName("artistAccentTerm")
  @OverloadResolutionByLambdaReturnType
  /** the artist's name (with accented characters) */
  public inline fun artistAccent(term: () -> Term): Field = add(SearchField.ArtistAccent, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun artistAccent(term: () -> String): Field = artistAccent { Term(term()) }

  @JvmName("artistIdTerm")
  @OverloadResolutionByLambdaReturnType
  /** the artist's MBID */
  public inline fun artistId(term: () -> Term): Field = add(SearchField.ArtistId, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun artistId(mbid: () -> ArtistMbid): Field = artistId { Term(mbid()) }

  @JvmName("beginAreaTerm")
  @OverloadResolutionByLambdaReturnType
  /** (part of) the name of the artist's begin area */
  public inline fun beginArea(term: () -> Term): Field = add(SearchField.BeginArea, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun beginArea(term: () -> String): Field = beginArea { Term(term()) }

  @JvmName("beginDateTerm")
  @OverloadResolutionByLambdaReturnType
  /** the artist's begin date (e.g. "1980-01-22")  */
  public inline fun beginDate(term: () -> Term): Field = add(SearchField.Begin, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun beginDate(term: () -> LocalDate): Field = beginDate { Term(term()) }

  @JvmName("beginDateOld")
  @OverloadResolutionByLambdaReturnType
  public inline fun beginDate(term: () -> Date): Field = beginDate { Term(term()) }

  @JvmName("commentTerm")
  @OverloadResolutionByLambdaReturnType
  /** (part of) the artist's disambiguation comment  */
  public inline fun comment(term: () -> Term): Field = add(SearchField.Comment, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun comment(term: () -> String): Field = comment { Term(term()) }

  @JvmName("countryTerm")
  @OverloadResolutionByLambdaReturnType
  /**
   * the 2-letter code (ISO 3166-1 alpha-2) for the artist's main associated country, or “unknown”
   */
  public inline fun country(term: () -> Term): Field = add(SearchField.Country, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun country(term: () -> String): Field = country { Term(term()) }

  @JvmName("defaultTerm")
  @OverloadResolutionByLambdaReturnType
  /** Default searches [Alias], [Artist], and [SortName] */
  public inline fun default(term: () -> Term): Field = add(SearchField.Default, term())

  @JvmName("defaultName")
  @OverloadResolutionByLambdaReturnType
  public inline fun default(name: () -> String): Field = default { Term(name()) }

  @JvmName("endAreaTerm")
  @OverloadResolutionByLambdaReturnType
  /** 	(part of) the name of the artist's end area  */
  public inline fun endArea(term: () -> Term): Field = add(SearchField.EndArea, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun endArea(term: () -> String): Field = endArea { Term(term()) }

  @JvmName("endDateTerm")
  @OverloadResolutionByLambdaReturnType
  /** the artist's end date (e.g. "1980-01-22")  */
  public inline fun endDate(term: () -> Term): Field = add(SearchField.End, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun endDate(term: () -> LocalDate): Field = endDate { Term(term()) }

  @JvmName("endDateOld")
  @OverloadResolutionByLambdaReturnType
  public inline fun endDate(term: () -> Date): Field = endDate { Term(term()) }

  @JvmName("endedTerm")
  @OverloadResolutionByLambdaReturnType
  /**
   * 	a boolean flag (true/false) indicating whether or not the artist has ended (is
   * dissolved/deceased)
   */
  public inline fun ended(term: () -> Term): Field = add(SearchField.Ended, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun ended(term: () -> Boolean): Field = ended { Term(term()) }

  @JvmName("genderTerm")
  @OverloadResolutionByLambdaReturnType
  /** the artist's gender (“male”, “female”, “other” or “not applicable”)  */
  public inline fun gender(term: () -> Term): Field = add(SearchField.Gender, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun gender(term: () -> String): Field = gender { Term(term()) }

  @JvmName("ipiTerm")
  @OverloadResolutionByLambdaReturnType
  /** an IPI code associated with the artist */
  public inline fun ipi(term: () -> Term): Field = add(SearchField.Ipi, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun ipi(term: () -> String): Field = ipi { Term(term()) }

  @JvmName("isniTerm")
  @OverloadResolutionByLambdaReturnType
  /** an ISNI code associated with the artist */
  public inline fun isni(term: () -> Term): Field = add(SearchField.Isni, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun isni(term: () -> String): Field = isni { Term(term()) }

  @JvmName("sortNameTerm")
  @OverloadResolutionByLambdaReturnType
  /**
   * (part of) the artist's [sort name](https://musicbrainz.org/doc/Artist#Sort_name)
   *
   * Sort name [style](https://musicbrainz.org/doc/Style/Artist/Sort_Name)
   */
  public inline fun sortName(term: () -> Term): Field = add(SortName, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun sortName(term: () -> String): Field = sortName { Term(term()) }

  @JvmName("tagTerm")
  @OverloadResolutionByLambdaReturnType
  /** (part of) a tag attached to the artist */
  public inline fun tag(term: () -> Term): Field = add(SearchField.Tag, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun tag(term: () -> String): Field = tag { Term(term()) }

  @JvmName("typeTerm")
  @OverloadResolutionByLambdaReturnType
  /** the artist's [type](https://musicbrainz.org/doc/Artist#Type) (“person”, “group”, etc.) */
  public inline fun type(term: () -> Term): Field = add(SearchField.Type, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun type(type: () -> ArtistType): Field = type { Term(type()) }

  public companion object {
    public inline operator fun invoke(search: ArtistSearch.() -> Unit): String {
      return ArtistSearch().apply(search).toString()
    }
  }
}
