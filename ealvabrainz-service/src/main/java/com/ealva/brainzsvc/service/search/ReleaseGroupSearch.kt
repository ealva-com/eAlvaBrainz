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

import com.ealva.brainzsvc.common.AlbumTitle
import com.ealva.brainzsvc.common.ArtistName
import com.ealva.brainzsvc.common.brainzFormat
import com.ealva.ealvabrainz.brainz.data.ArtistMbid
import com.ealva.ealvabrainz.brainz.data.Release
import com.ealva.ealvabrainz.brainz.data.ReleaseGroup.SearchField
import com.ealva.ealvabrainz.brainz.data.ReleaseGroupMbid
import com.ealva.ealvabrainz.brainz.data.ReleaseMbid
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
  public inline fun alias(term: () -> Term): Field = make(SearchField.Alias, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun alias(term: () -> String): Field = make(SearchField.Alias, term())

  @JvmName("artistIdTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun artistId(term: () -> Term): Field = make(SearchField.ArtistId, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun artistId(term: () -> ArtistMbid): Field =
    make(SearchField.ArtistId, term())

  @JvmName("artistTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun artist(term: () -> Term): Field = make(SearchField.Artist, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun artist(term: () -> ArtistName): Field =
    make(SearchField.Artist, term().value)

  @JvmName("artistNameTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun artistName(term: () -> Term): Field = make(SearchField.ArtistName, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun artistName(term: () -> ArtistName): Field =
    make(SearchField.ArtistName, term().value)

  @JvmName("commentTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun comment(term: () -> Term): Field = make(SearchField.Comment, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun comment(term: () -> String): Field =
    make(SearchField.Comment, term())

  @JvmName("creditNameTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun creditName(term: () -> Term): Field = make(SearchField.CreditName, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun creditName(term: () -> ArtistName): Field =
    make(SearchField.CreditName, term().value)

  @JvmName("dateTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun firstReleaseDate(term: () -> Term): Field =
    make(SearchField.FirstReleaseDate, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun firstReleaseDate(term: () -> LocalDate): Field =
    make(SearchField.FirstReleaseDate, term().brainzFormat())

  @JvmName("primaryTypeTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun primaryType(term: () -> Term): Field = make(SearchField.PrimaryType, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun primaryType(term: () -> Release.Type): Field =
    make(SearchField.PrimaryType, term().value)

  @JvmName("primaryTypeString")
  @OverloadResolutionByLambdaReturnType
  public inline fun primaryType(term: () -> String): Field = make(SearchField.PrimaryType, term())

  @JvmName("releaseIdTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun releaseId(term: () -> Term): Field = make(SearchField.ReleaseId, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun releaseId(term: () -> ReleaseMbid): Field =
    make(SearchField.Artist, term())

  @JvmName("releaseTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun release(term: () -> Term): Field = make(SearchField.Release, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun release(term: () -> AlbumTitle): Field =
    make(SearchField.Release, term().value)

  @JvmName("releaseCountTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun releaseCount(term: () -> Term): Field = make(SearchField.Releases, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun releaseCount(term: () -> String): Field =
    make(SearchField.Releases, term())

  @JvmName("releaseGroupTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun releaseGroup(term: () -> Term): Field = make(SearchField.ReleaseGroup, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun releaseGroup(term: () -> AlbumTitle): Field =
    make(SearchField.ReleaseGroup, term().value)

  @JvmName("releaseGroupIdTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun releaseGroupId(term: () -> Term): Field =
    make(SearchField.ReleaseGroupId, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun releaseGroupId(term: () -> ReleaseGroupMbid): Field =
    make(SearchField.ReleaseGroupId, term())

  @JvmName("releaseGroupAccentedNameTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun releaseGroupAccentedName(term: () -> Term): Field =
    make(SearchField.ReleaseGroupAccentedName, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun releaseGroupAccentedName(term: () -> String): Field =
    make(SearchField.ReleaseGroupAccentedName, term())

  @JvmName("secondaryTypeTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun secondaryType(term: () -> Term): Field = make(SearchField.SecondaryType, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun secondaryType(term: () -> Release.Type): Field =
    make(SearchField.SecondaryType, term().value)

  @JvmName("secondaryTypeString")
  @OverloadResolutionByLambdaReturnType
  public inline fun secondaryType(term: () -> String): Field =
    make(SearchField.SecondaryType, term())

  @JvmName("statusTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun status(term: () -> Term): Field = make(SearchField.Status, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun status(term: () -> String): Field = make(SearchField.Status, term())

  @JvmName("tagTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun tag(term: () -> Term): Field = make(SearchField.Tag, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun tag(term: () -> String): Field = make(SearchField.Tag, term())

  public fun <T> make(field: SearchField, term: T): Field =
    makeAndAdd(field.value, term)
}
