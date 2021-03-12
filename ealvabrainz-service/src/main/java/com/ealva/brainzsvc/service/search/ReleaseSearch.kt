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
import com.ealva.brainzsvc.common.LabelName
import com.ealva.brainzsvc.common.brainzFormat
import com.ealva.ealvabrainz.brainz.data.ArtistMbid
import com.ealva.ealvabrainz.brainz.data.LabelMbid
import com.ealva.ealvabrainz.brainz.data.Release
import com.ealva.ealvabrainz.brainz.data.Release.SearchField
import com.ealva.ealvabrainz.brainz.data.ReleaseGroupMbid
import com.ealva.ealvabrainz.brainz.data.ReleaseMbid
import com.ealva.ealvabrainz.lucene.BrainzMarker
import com.ealva.ealvabrainz.lucene.Field
import com.ealva.ealvabrainz.lucene.Term
import java.time.LocalDate
import java.util.Date
import kotlin.experimental.ExperimentalTypeInference

@OptIn(ExperimentalTypeInference::class)
@BrainzMarker
public class ReleaseSearch : BaseSearch() {
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

  @JvmName("asinTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun asin(term: () -> Term): Field = make(SearchField.Asin, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun asin(term: () -> String): Field =
    make(SearchField.Asin, term())

  @JvmName("barcodeTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun barcode(term: () -> Term): Field = make(SearchField.Barcode, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun barcode(term: () -> String): Field =
    make(SearchField.Barcode, term())

  @JvmName("catalogNumberTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun catalogNumber(term: () -> Term): Field = make(SearchField.CatalogNumber, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun catalogNumber(term: () -> String): Field =
    make(SearchField.CatalogNumber, term())

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

  @JvmName("creditNameTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun creditName(term: () -> Term): Field = make(SearchField.CreditName, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun creditName(term: () -> ArtistName): Field =
    make(SearchField.CreditName, term().value)

  @JvmName("dateTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun date(term: () -> Term): Field = make(SearchField.Date, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun date(term: () -> LocalDate): Field =
    make(SearchField.Date, term().brainzFormat())

  @JvmName("dateOld")
  @OverloadResolutionByLambdaReturnType
  public inline fun date(term: () -> Date): Field =
    make(SearchField.Date, term().brainzFormat())

  @JvmName("dateString")
  @OverloadResolutionByLambdaReturnType
  public inline fun date(term: () -> String): Field =
    make(SearchField.Date, term())

  @JvmName("discIdCountTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun discIdCount(term: () -> Term): Field = make(SearchField.DiscIdCount, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun discIdCount(term: () -> String): Field =
    make(SearchField.DiscIdCount, term())

  @JvmName("formatTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun format(term: () -> Term): Field = make(SearchField.Format, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun format(term: () -> String): Field =
    make(SearchField.Format, term())

  @JvmName("labelTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun label(term: () -> Term): Field = make(SearchField.Label, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun label(term: () -> LabelName): Field =
    make(SearchField.Label, term().value)

  @JvmName("labelIdTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun labelId(term: () -> Term): Field = make(SearchField.LabelId, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun labelId(term: () -> LabelMbid): Field = make(SearchField.LabelId, term())

  @JvmName("languageTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun language(term: () -> Term): Field = make(SearchField.Language, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun language(term: () -> String): Field = make(SearchField.Language, term())

  @JvmName("mediumCountTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun mediumCount(term: () -> Term): Field = make(SearchField.MediumCount, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun mediumCount(term: () -> String): Field =
    make(SearchField.MediumCount, term())

  @JvmName("mediumDiscCountTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun mediumDiscCount(term: () -> Term): Field =
    make(SearchField.MediumDiscCount, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun mediumDiscCount(term: () -> String): Field =
    make(SearchField.MediumDiscCount, term())

  @JvmName("mediumTrackCountTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun mediumTrackCount(term: () -> Term): Field =
    make(SearchField.MediumTrackCount, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun mediumTrackCount(term: () -> String): Field =
    make(SearchField.MediumTrackCount, term())

  @JvmName("packagingTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun packaging(term: () -> Term): Field = make(SearchField.Packaging, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun packaging(term: () -> String): Field =
    make(SearchField.Packaging, term())

  @JvmName("primaryTypeTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun primaryType(term: () -> Term): Field = make(SearchField.PrimaryType, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun primaryType(term: () -> Release.Type): Field =
    make(SearchField.PrimaryType, term().value)

  @JvmName("primaryTypeString")
  @OverloadResolutionByLambdaReturnType
  public inline fun primaryType(term: () -> String): Field = make(SearchField.PrimaryType, term())

  @JvmName("qualityTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun quality(term: () -> Term): Field = make(SearchField.Quality, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun quality(term: () -> String): Field =
    make(SearchField.Quality, term())

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

  @JvmName("releaseGroupIdTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun releaseGroupId(term: () -> Term): Field =
    make(SearchField.ReleaseGroupId, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun releaseGroupId(term: () -> ReleaseGroupMbid): Field =
    make(SearchField.ReleaseGroupId, term())

  @JvmName("releaseAccentedNameTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun releaseAccentedName(term: () -> Term): Field =
    make(SearchField.ReleaseAccentedName, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun releaseAccentedName(term: () -> String): Field =
    make(SearchField.ReleaseAccentedName, term())

  @JvmName("scriptTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun script(term: () -> Term): Field = make(SearchField.Script, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun script(term: () -> String): Field = make(SearchField.Script, term())

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

  @JvmName("trackCountTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun trackCount(term: () -> Term): Field = make(SearchField.TrackCount, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun trackCount(term: () -> String): Field = make(SearchField.TrackCount, term())

  public fun <T> make(field: SearchField, term: T): Field =
    makeAndAdd(field.value, term)
}
