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

package com.ealva.ealvabrainz.search

import com.ealva.ealvabrainz.brainz.data.ArtistMbid
import com.ealva.ealvabrainz.brainz.data.Release
import com.ealva.ealvabrainz.brainz.data.ReleaseGroup
import com.ealva.ealvabrainz.brainz.data.ReleaseGroup.SearchField
import com.ealva.ealvabrainz.brainz.data.ReleaseGroupMbid
import com.ealva.ealvabrainz.brainz.data.ReleaseMbid
import com.ealva.ealvabrainz.common.AlbumTitle
import com.ealva.ealvabrainz.common.ArtistName
import com.ealva.ealvabrainz.common.BrainzMarker
import com.ealva.ealvabrainz.common.Year
import com.ealva.ealvabrainz.lucene.Field
import com.ealva.ealvabrainz.lucene.Query
import com.ealva.ealvabrainz.lucene.Term
import com.ealva.ealvabrainz.search.term.DateTermBuilder
import com.ealva.ealvabrainz.search.term.MbidTermBuilder
import com.ealva.ealvabrainz.search.term.NumberTermBuilder
import com.ealva.ealvabrainz.search.term.ReleaseGroupTypeTerm
import com.ealva.ealvabrainz.search.term.ReleaseStatusTerm
import com.ealva.ealvabrainz.search.term.TermBuilder
import java.time.LocalDate

@BrainzMarker
public class ReleaseGroupSearch(query: Query = Query()) : BaseSearch<SearchField>(query) {
  /**
   * (part of) any [alias](https://musicbrainz.org/doc/Aliases) attached to the release group
   * (diacritics are ignored)
   */
  public fun alias(term: String): Field = alias { Term(term) }
  public fun alias(build: TermBuilder.() -> Term): Field =
    add(SearchField.Alias, TermBuilder().build())

  /**
   * (part of) the combined credited artist name for the release group, including join phrases
   * (e.g. "Artist X feat.")
   */
  public fun artist(term: ArtistName): Field = artist { Term(term) }
  public fun artist(build: TermBuilder.() -> Term): Field =
    add(SearchField.Artist, TermBuilder().build())

  /** MBID of the release group’s artist */
  public fun artistId(term: ArtistMbid): Field = artistId { Term(term) }
  public fun artistId(build: MbidTermBuilder<ArtistMbid>.() -> Term): Field =
    add(SearchField.ArtistId, MbidTermBuilder<ArtistMbid>().build())

  /** (part of) the name of any of the release group artists   */
  public fun artistName(term: ArtistName): Field = artistName { Term(term) }
  public fun artistName(build: TermBuilder.() -> Term): Field =
    add(SearchField.ArtistName, TermBuilder().build())

  /** (part of) the release group's disambiguation comment */
  public fun comment(term: String): Field = comment { Term(term) }
  public fun comment(build: TermBuilder.() -> Term): Field =
    add(SearchField.Comment, TermBuilder().build())

  /**
   * (part of) the credited name of any of the release group artists on this particular release
   * group
   */
  public fun creditName(term: ArtistName): Field = creditName { Term(term) }
  public fun creditName(build: TermBuilder.() -> Term): Field =
    add(SearchField.CreditName, TermBuilder().build())

  /** Default searches [ReleaseGroup] */
  public fun default(term: AlbumTitle): Field = default { Term(term) }
  public fun default(build: TermBuilder.() -> Term): Field =
    add(SearchField.Default, TermBuilder().build())

  /** the release date of the earliest release in this release group (e.g. "1980-01-22") */
  public fun firstReleaseDate(term: LocalDate): Field = firstReleaseDate { Term(term) }
  public fun firstReleaseDate(term: Year): Field = firstReleaseDate { Term(term) }
  public fun firstReleaseDate(build: DateTermBuilder.() -> Term): Field =
    add(SearchField.FirstReleaseDate, DateTermBuilder().build())

  /**
   * [primary type](https://musicbrainz.org/doc/Release_Group/Type#Primary_types) of the release
   * group
   */
  public fun primaryType(type: ReleaseGroup.Type): Field = primaryType { Term(type) }
  public fun primaryType(build: ReleaseGroupTypeTerm.Builder.() -> Term): Field =
    add(SearchField.PrimaryType, ReleaseGroupTypeTerm.Builder().build())

  /** (part of) the title of any of the releases in the release group */
  public fun release(term: AlbumTitle): Field = release { Term(term) }
  public fun release(build: TermBuilder.() -> Term): Field =
    add(SearchField.Release, TermBuilder().build())

  /** number of releases in this release group  */
  public fun releaseCount(term: Int): Field = releaseCount { Term(term) }
  public fun releaseCount(build: NumberTermBuilder.() -> Term): Field =
    add(SearchField.Releases, NumberTermBuilder().build())

  /** the MBID of any of the releases in the release group   */
  public fun releaseId(term: ReleaseMbid): Field = releaseId { Term(term) }
  public fun releaseId(build: MbidTermBuilder<ReleaseMbid>.() -> Term): Field =
    add(SearchField.ReleaseId, MbidTermBuilder<ReleaseMbid>().build())

  /**	(part of) the release group's title (diacritics are ignored) */
  public fun releaseGroup(term: AlbumTitle): Field = releaseGroup { Term(term) }
  public fun releaseGroup(build: TermBuilder.() -> Term): Field =
    add(SearchField.ReleaseGroup, TermBuilder().build())

  /** (part of) the release group's title (with the specified diacritics) */
  public fun releaseGroupAccentedName(term: String): Field = releaseGroupAccentedName { Term(term) }
  public fun releaseGroupAccentedName(build: TermBuilder.() -> Term): Field =
    add(SearchField.ReleaseGroupAccentedName, TermBuilder().build())

  /** the release group's MBID  */
  public fun releaseGroupId(term: ReleaseGroupMbid): Field = releaseGroupId { Term(term) }
  public fun releaseGroupId(term: MbidTermBuilder<ReleaseMbid>.() -> Term): Field =
    add(SearchField.ReleaseGroupId, MbidTermBuilder<ReleaseMbid>().term())

  /**
   * any of the [secondary types](https://musicbrainz.org/doc/Release_Group/Type#Secondary_types)
   * of the release group
   */
  public fun secondaryType(type: ReleaseGroup.Type): Field = secondaryType { Term(type) }
  public fun secondaryType(build: ReleaseGroupTypeTerm.Builder.() -> Term): Field =
    add(SearchField.SecondaryType, ReleaseGroupTypeTerm.Builder().build())

  /**
   * the [status][com.ealva.ealvabrainz.brainz.data.Release.Status] of any of the releases in the
   * release group
   */
  public fun status(type: Release.Status): Field = status { Term(type) }
  public fun status(build: ReleaseStatusTerm.Builder.() -> Term): Field =
    add(SearchField.Status, ReleaseStatusTerm.Builder().build())

  /** (part of) a tag attached to the release group */
  public fun tag(build: TermBuilder.() -> Term): Field = add(SearchField.Tag, TermBuilder().build())
  public fun tag(term: String): Field = tag { Term(term) }

  public companion object {
    public inline operator fun invoke(search: ReleaseGroupSearch.() -> Unit): String =
      ReleaseGroupSearch().apply(search).toString()
  }
}
