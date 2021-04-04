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
import com.ealva.ealvabrainz.lucene.Field
import com.ealva.ealvabrainz.lucene.Query
import com.ealva.ealvabrainz.lucene.Term
import java.time.LocalDate
import java.util.Date
import kotlin.experimental.ExperimentalTypeInference

@OptIn(ExperimentalTypeInference::class)
@BrainzMarker
public class ReleaseGroupSearch(query: Query = Query()) : BaseSearch<SearchField>(query) {
  @JvmName("aliasTerm")
  @OverloadResolutionByLambdaReturnType
  /**
   * (part of) any [alias](https://musicbrainz.org/doc/Aliases) attached to the release group
   * (diacritics are ignored)
   */
  public inline fun alias(term: () -> Term): Field = add(SearchField.Alias, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun alias(term: () -> String): Field = alias { Term(term()) }

  @JvmName("artistTerm")
  @OverloadResolutionByLambdaReturnType
  /**
   * (part of) the combined credited artist name for the release group, including join phrases
   * (e.g. "Artist X feat.")
   */
  public inline fun artist(term: () -> Term): Field = add(SearchField.Artist, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun artist(term: () -> ArtistName): Field = artist { Term(term()) }

  @JvmName("artistIdTerm")
  @OverloadResolutionByLambdaReturnType
  /** MBID of the release group’s artist */
  public inline fun artistId(term: () -> Term): Field = add(SearchField.ArtistId, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun artistId(term: () -> ArtistMbid): Field = artistId { Term(term()) }

  @JvmName("artistNameTerm")
  @OverloadResolutionByLambdaReturnType
  /** (part of) the name of any of the release group artists   */
  public inline fun artistName(term: () -> Term): Field = add(SearchField.ArtistName, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun artistName(term: () -> ArtistName): Field = artistName { Term(term()) }

  @JvmName("commentTerm")
  @OverloadResolutionByLambdaReturnType
  /** (part of) the release group's disambiguation comment */
  public inline fun comment(term: () -> Term): Field = add(SearchField.Comment, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun comment(term: () -> String): Field = comment { Term(term()) }

  @JvmName("creditNameTerm")
  @OverloadResolutionByLambdaReturnType
  /**
   * (part of) the credited name of any of the release group artists on this particular release
   * group
   */
  public inline fun creditName(term: () -> Term): Field = add(SearchField.CreditName, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun creditName(term: () -> ArtistName): Field = creditName { Term(term()) }

  @JvmName("defaultTerm")
  @OverloadResolutionByLambdaReturnType
  /** Default searches [ReleaseGroup] */
  public inline fun default(term: () -> Term): Field = add(SearchField.Default, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun default(term: () -> AlbumTitle): Field = default { Term(term()) }

  @JvmName("dateTerm")
  @OverloadResolutionByLambdaReturnType
  /** the release date of the earliest release in this release group (e.g. "1980-01-22") */
  public inline fun firstReleaseDate(term: () -> Term): Field =
    add(SearchField.FirstReleaseDate, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun firstReleaseDate(term: () -> LocalDate): Field =
    firstReleaseDate { Term(term()) }

  @JvmName("dateOldTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun firstReleaseDate(term: () -> Date): Field =
    firstReleaseDate { Term(term()) }

  @JvmName("primaryTypeTerm")
  @OverloadResolutionByLambdaReturnType
  /**
   * [primary type](https://musicbrainz.org/doc/Release_Group/Type#Primary_types) of the release
   * group
   */
  public inline fun primaryType(term: () -> Term): Field = add(SearchField.PrimaryType, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun primaryType(term: () -> ReleaseGroup.Type): Field = primaryType { Term(term()) }

  @JvmName("releaseTerm")
  @OverloadResolutionByLambdaReturnType
  /** (part of) the title of any of the releases in the release group */
  public inline fun release(term: () -> Term): Field = add(SearchField.Release, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun release(term: () -> AlbumTitle): Field = release { Term(term()) }

  @JvmName("releaseCountTerm")
  @OverloadResolutionByLambdaReturnType
  /** number of releases in this release group  */
  public inline fun releaseCount(term: () -> Term): Field = add(SearchField.Releases, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun releaseCount(term: () -> Int): Field = releaseCount { Term(term()) }

  @JvmName("releaseIdTerm")
  @OverloadResolutionByLambdaReturnType
  /** the MBID of any of the releases in the release group   */
  public inline fun releaseId(term: () -> Term): Field = add(SearchField.ReleaseId, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun releaseId(term: () -> ReleaseMbid): Field = releaseId { Term(term()) }

  @JvmName("releaseGroupTerm")
  @OverloadResolutionByLambdaReturnType
  /**	(part of) the release group's title (diacritics are ignored) */
  public inline fun releaseGroup(term: () -> Term): Field = add(SearchField.ReleaseGroup, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun releaseGroup(term: () -> AlbumTitle): Field = releaseGroup { Term(term()) }

  @JvmName("releaseGroupAccentedNameTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun releaseGroupAccentedName(term: () -> Term): Field =
    add(SearchField.ReleaseGroupAccentedName, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun releaseGroupAccentedName(term: () -> String): Field =
    releaseGroupAccentedName { Term(term()) }

  @JvmName("releaseGroupIdTerm")
  @OverloadResolutionByLambdaReturnType
  /** the release group's MBID  */
  public inline fun releaseGroupId(term: () -> Term): Field =
    add(SearchField.ReleaseGroupId, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun releaseGroupId(term: () -> ReleaseGroupMbid): Field =
    releaseGroupId { Term(term()) }

  @JvmName("secondaryTypeTerm")
  @OverloadResolutionByLambdaReturnType
  /**
   * any of the [secondary types](https://musicbrainz.org/doc/Release_Group/Type#Secondary_types)
   * of the release group
   */
  public inline fun secondaryType(term: () -> Term): Field = add(SearchField.SecondaryType, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun secondaryType(term: () -> ReleaseGroup.Type): Field =
    secondaryType { Term(term()) }

  @JvmName("statusTerm")
  @OverloadResolutionByLambdaReturnType
  /**
   * the [status][Release.Status] of any of the releases in the
   * release group
   */
  public inline fun status(term: () -> Term): Field = add(SearchField.Status, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun status(term: () -> Release.Status): Field = status { Term(term()) }

  @JvmName("tagTerm")
  @OverloadResolutionByLambdaReturnType
  /** (part of) a tag attached to the release group */
  public inline fun tag(term: () -> Term): Field = add(SearchField.Tag, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun tag(term: () -> String): Field = tag { Term(term()) }

  public companion object {
    public inline operator fun invoke(search: ReleaseGroupSearch.() -> Unit): String {
      return ReleaseGroupSearch().apply(search).toString()
    }
  }
}
