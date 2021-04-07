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
import com.ealva.ealvabrainz.brainz.data.Artist.SearchField.Area
import com.ealva.ealvabrainz.brainz.data.Artist.SearchField.Artist
import com.ealva.ealvabrainz.brainz.data.Artist.SearchField.ArtistAccent
import com.ealva.ealvabrainz.brainz.data.Artist.SearchField.ArtistId
import com.ealva.ealvabrainz.brainz.data.Artist.SearchField.Begin
import com.ealva.ealvabrainz.brainz.data.Artist.SearchField.BeginArea
import com.ealva.ealvabrainz.brainz.data.Artist.SearchField.Comment
import com.ealva.ealvabrainz.brainz.data.Artist.SearchField.Country
import com.ealva.ealvabrainz.brainz.data.Artist.SearchField.Default
import com.ealva.ealvabrainz.brainz.data.Artist.SearchField.End
import com.ealva.ealvabrainz.brainz.data.Artist.SearchField.EndArea
import com.ealva.ealvabrainz.brainz.data.Artist.SearchField.Ended
import com.ealva.ealvabrainz.brainz.data.Artist.SearchField.Gender
import com.ealva.ealvabrainz.brainz.data.Artist.SearchField.Ipi
import com.ealva.ealvabrainz.brainz.data.Artist.SearchField.Isni
import com.ealva.ealvabrainz.brainz.data.Artist.SearchField.PrimaryAlias
import com.ealva.ealvabrainz.brainz.data.Artist.SearchField.SortName
import com.ealva.ealvabrainz.brainz.data.Artist.SearchField.Tag
import com.ealva.ealvabrainz.brainz.data.Artist.SearchField.Type
import com.ealva.ealvabrainz.brainz.data.ArtistMbid
import com.ealva.ealvabrainz.brainz.data.ArtistType
import com.ealva.ealvabrainz.common.AreaName
import com.ealva.ealvabrainz.common.ArtistName
import com.ealva.ealvabrainz.common.BrainzMarker
import com.ealva.ealvabrainz.common.Year
import com.ealva.ealvabrainz.lucene.Field
import com.ealva.ealvabrainz.lucene.Term
import com.ealva.ealvabrainz.search.term.DateTermBuilder
import com.ealva.ealvabrainz.search.term.MbidTermBuilder
import com.ealva.ealvabrainz.search.term.TermBuilder
import java.time.LocalDate

@Suppress("unused")
@BrainzMarker
public class ArtistSearch : BaseSearch<SearchField>() {
  /** (part of) any alias attached to the area (diacritics are ignored) */
  public fun alias(term: String): Field = alias { Term(term) }
  public fun alias(build: TermBuilder.() -> Term): Field = add(Alias, TermBuilder().build())

  /** (part of) the name of the artist's main associated area */
  public fun area(term: AreaName): Field = area { Term(term) }
  public fun area(build: TermBuilder.() -> Term): Field = add(Area, TermBuilder().build())

  /** (part of) the artist's name (diacritics are ignored) */
  public fun artist(name: ArtistName): Field = artist { Term(name) }
  public fun artist(build: TermBuilder.() -> Term): Field = add(Artist, TermBuilder().build())

  /** the artist's name (with accented characters) */
  public fun artistAccent(term: String): Field = artistAccent { Term(term) }
  public fun artistAccent(build: TermBuilder.() -> Term): Field =
    add(ArtistAccent, TermBuilder().build())

  /** the artist's MBID */
  public fun artistId(mbid: ArtistMbid): Field = artistId { Term(mbid) }
  public fun artistId(build: MbidTermBuilder<ArtistMbid>.() -> Term): Field =
    add(ArtistId, MbidTermBuilder<ArtistMbid>().build())

  /** (part of) the name of the artist's begin area */
  public fun beginArea(term: String): Field = beginArea { Term(term) }
  public fun beginArea(build: TermBuilder.() -> Term): Field = add(BeginArea, TermBuilder().build())

  /** the artist's begin date (e.g. "1980-01-22")  */
  public fun beginDate(term: LocalDate): Field = beginDate { Term(term) }
  public fun beginDate(term: Year): Field = beginDate { Term(term) }
  public fun beginDate(build: DateTermBuilder.() -> Term): Field =
    add(Begin, DateTermBuilder().build())

  /** (part of) the artist's disambiguation comment  */
  public fun comment(term: String): Field = comment { Term(term) }
  public fun comment(build: TermBuilder.() -> Term): Field = add(Comment, TermBuilder().build())

  /**
   * the 2-letter code (ISO 3166-1 alpha-2) for the artist's main associated country, or “unknown”
   */
  public fun country(term: String): Field = country { Term(term) }
  public fun country(build: TermBuilder.() -> Term): Field = add(Country, TermBuilder().build())

  /** Default searches [SearchField.Alias], [SearchField.Artist], and [SearchField.SortName] */
  public fun default(name: String): Field = default { Term(name) }
  public fun default(build: TermBuilder.() -> Term): Field = add(Default, TermBuilder().build())

  /** (part of) the name of the artist's end area  */
  public fun endArea(term: String): Field = endArea { Term(term) }
  public fun endArea(build: TermBuilder.() -> Term): Field = add(EndArea, TermBuilder().build())

  /** the artist's end date (e.g. "1980-01-22")  */
  public fun endDate(term: LocalDate): Field = endDate { Term(term) }
  public fun endDate(term: Year): Field = endDate { Term(term) }
  public fun endDate(build: DateTermBuilder.() -> Term): Field = add(End, DateTermBuilder().build())

  /**
   * 	a boolean flag (true/false) indicating whether or not the artist has ended (is
   * dissolved/deceased)
   */
  public fun ended(term: Boolean): Field = add(Ended, Term(term))

  /** the artist's gender (“male”, “female”, “other” or “not applicable”) */
  public fun gender(term: String): Field = gender { Term(term) }
  public fun gender(build: TermBuilder.() -> Term): Field = add(Gender, TermBuilder().build())

  /** an IPI code associated with the artist */
  public fun ipi(term: String): Field = ipi { Term(term) }
  public fun ipi(build: TermBuilder.() -> Term): Field = add(Ipi, TermBuilder().build())

  /** an ISNI code associated with the artist */
  public fun isni(term: String): Field = isni { Term(term) }
  public fun isni(build: TermBuilder.() -> Term): Field = add(Isni, TermBuilder().build())

  /** (part of) any primary alias attached to the artist (diacritics are ignored) */
  public fun primaryAlias(term: String): Field = primaryAlias { Term(term) }
  public fun primaryAlias(build: TermBuilder.() -> Term): Field =
    add(PrimaryAlias, TermBuilder().build())

  /**
   * (part of) the artist's [sort name](https://musicbrainz.org/doc/Artist#Sort_name)
   *
   * Sort name [style](https://musicbrainz.org/doc/Style/Artist/Sort_Name)
   */
  public fun sortName(term: String): Field = sortName { Term(term) }
  public fun sortName(build: TermBuilder.() -> Term): Field = add(SortName, TermBuilder().build())

  /** (part of) a tag attached to the artist */
  public fun tag(term: String): Field = tag { Term(term) }
  public fun tag(build: TermBuilder.() -> Term): Field = add(Tag, TermBuilder().build())

  /** the artist's [type](https://musicbrainz.org/doc/Artist#Type) (“person”, “group”, etc.) */
  public fun type(type: ArtistType): Field = type { Term(type) }
  public fun type(build: TermBuilder.() -> Term): Field = add(Type, TermBuilder().build())

  public companion object {
    public inline operator fun invoke(search: ArtistSearch.() -> Unit): String {
      return ArtistSearch().apply(search).toString()
    }
  }
}
