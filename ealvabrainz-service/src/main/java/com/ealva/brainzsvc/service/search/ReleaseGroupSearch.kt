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

import com.ealva.ealvabrainz.common.ArtistMbid
import com.ealva.ealvabrainz.brainz.data.Release
import com.ealva.ealvabrainz.brainz.data.ReleaseGroup.SearchField
import com.ealva.ealvabrainz.common.ReleaseGroupMbid
import com.ealva.ealvabrainz.common.ReleaseMbid
import com.ealva.ealvabrainz.common.AlbumTitle
import com.ealva.ealvabrainz.common.ArtistName
import com.ealva.ealvabrainz.lucene.BrainzMarker
import com.ealva.ealvabrainz.lucene.Field
import com.ealva.ealvabrainz.lucene.Term
import java.time.LocalDate
import kotlin.experimental.ExperimentalTypeInference

@OptIn(ExperimentalTypeInference::class)
@BrainzMarker
public class ReleaseGroupSearch : BaseSearch() {
  @JvmName("aliasTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun alias(term: () -> Term): Field = add(SearchField.Alias, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun alias(term: () -> String): Field = alias { Term(term()) }

  @JvmName("artistIdTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun artistId(term: () -> Term): Field = add(SearchField.ArtistId, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun artistId(term: () -> ArtistMbid): Field = artistId { Term(term()) }

  @JvmName("artistTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun artist(term: () -> Term): Field = add(SearchField.Artist, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun artist(term: () -> ArtistName): Field = artist { Term(term()) }

  @JvmName("artistNameTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun artistName(term: () -> Term): Field = add(SearchField.ArtistName, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun artistName(term: () -> ArtistName): Field = artistName { Term(term()) }

  @JvmName("commentTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun comment(term: () -> Term): Field = add(SearchField.Comment, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun comment(term: () -> String): Field = comment { Term(term()) }

  @JvmName("creditNameTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun creditName(term: () -> Term): Field = add(SearchField.CreditName, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun creditName(term: () -> ArtistName): Field = creditName { Term(term()) }

  @JvmName("dateTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun firstReleaseDate(term: () -> Term): Field =
    add(SearchField.FirstReleaseDate, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun firstReleaseDate(term: () -> LocalDate): Field =
    firstReleaseDate { Term(term()) }

  @JvmName("primaryTypeTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun primaryType(term: () -> Term): Field = add(SearchField.PrimaryType, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun primaryType(term: () -> Release.Type): Field = primaryType { Term(term()) }

  @JvmName("releaseIdTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun releaseId(term: () -> Term): Field = add(SearchField.ReleaseId, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun releaseId(term: () -> ReleaseMbid): Field = releaseId { Term(term()) }

  @JvmName("releaseTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun release(term: () -> Term): Field = add(SearchField.Release, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun release(term: () -> AlbumTitle): Field = release { Term(term()) }

  @JvmName("releaseCountTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun releaseCount(term: () -> Term): Field = add(SearchField.Releases, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun releaseCount(term: () -> Int): Field = releaseCount { Term(term()) }

  @JvmName("releaseGroupTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun releaseGroup(term: () -> Term): Field = add(SearchField.ReleaseGroup, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun releaseGroup(term: () -> AlbumTitle): Field = releaseGroup { Term(term()) }

  @JvmName("releaseGroupIdTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun releaseGroupId(term: () -> Term): Field =
    add(SearchField.ReleaseGroupId, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun releaseGroupId(term: () -> ReleaseGroupMbid): Field =
    releaseGroupId { Term(term()) }

  @JvmName("releaseGroupAccentedNameTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun releaseGroupAccentedName(term: () -> Term): Field =
    add(SearchField.ReleaseGroupAccentedName, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun releaseGroupAccentedName(term: () -> String): Field =
    releaseGroupAccentedName { Term(term()) }

  @JvmName("secondaryTypeTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun secondaryType(term: () -> Term): Field = add(SearchField.SecondaryType, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun secondaryType(term: () -> Release.Type): Field = secondaryType { Term(term()) }

  @JvmName("statusTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun status(term: () -> Term): Field = add(SearchField.Status, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun status(term: () -> Release.Status): Field = status { Term(term()) }

  @JvmName("tagTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun tag(term: () -> Term): Field = add(SearchField.Tag, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun tag(term: () -> String): Field = tag { Term(term()) }

  public fun add(field: SearchField, term: Term): Field = makeAndAddField(field.value, term)
}
