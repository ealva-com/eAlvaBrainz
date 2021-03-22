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
import com.ealva.ealvabrainz.common.LabelMbid
import com.ealva.ealvabrainz.brainz.data.Release
import com.ealva.ealvabrainz.brainz.data.Release.SearchField
import com.ealva.ealvabrainz.common.ReleaseGroupMbid
import com.ealva.ealvabrainz.common.ReleaseMbid
import com.ealva.ealvabrainz.common.AlbumTitle
import com.ealva.ealvabrainz.common.ArtistName
import com.ealva.ealvabrainz.common.LabelName
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
  public inline fun alias(term: () -> Term): Field = add(SearchField.Alias, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun alias(term: () -> String): Field = alias { Term(term()) }

  @JvmName("artistIdTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun artistId(term: () -> Term): Field = add(SearchField.ArtistId, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun artistId(mbid: () -> ArtistMbid): Field = artistId { Term(mbid()) }

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

  @JvmName("asinTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun asin(term: () -> Term): Field = add(SearchField.Asin, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun asin(term: () -> String): Field = asin { Term(term()) }

  @JvmName("barcodeTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun barcode(term: () -> Term): Field = add(SearchField.Barcode, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun barcode(term: () -> String): Field =
    add(SearchField.Barcode, Term(term()))

  @JvmName("catalogNumberTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun catalogNumber(term: () -> Term): Field = add(SearchField.CatalogNumber, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun catalogNumber(term: () -> String): Field =
    add(SearchField.CatalogNumber, Term(term()))

  @JvmName("commentTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun comment(term: () -> Term): Field = add(SearchField.Comment, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun comment(term: () -> String): Field =
    add(SearchField.Comment, Term(term()))

  @JvmName("countryTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun country(term: () -> Term): Field = add(SearchField.Country, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun country(term: () -> String): Field =
    add(SearchField.Country, Term(term()))

  @JvmName("creditNameTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun creditName(term: () -> Term): Field = add(SearchField.CreditName, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun creditName(term: () -> ArtistName): Field =
    add(SearchField.CreditName, Term(term()))

  @JvmName("dateTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun date(term: () -> Term): Field = add(SearchField.Date, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun date(term: () -> LocalDate): Field =
    add(SearchField.Date, Term(term()))

  @JvmName("dateOld")
  @OverloadResolutionByLambdaReturnType
  public inline fun date(term: () -> Date): Field =
    add(SearchField.Date, Term(term()))

  @JvmName("dateString")
  @OverloadResolutionByLambdaReturnType
  public inline fun date(term: () -> String): Field =
    add(SearchField.Date, Term(term()))

  @JvmName("discIdCountTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun discIdCount(term: () -> Term): Field = add(SearchField.DiscIdCount, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun discIdCount(term: () -> Int): Field =
    add(SearchField.DiscIdCount, Term(term()))

  @JvmName("formatTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun format(term: () -> Term): Field = add(SearchField.Format, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun format(term: () -> String): Field =
    add(SearchField.Format, Term(term()))

  @JvmName("labelTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun label(term: () -> Term): Field = add(SearchField.Label, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun label(term: () -> LabelName): Field =
    add(SearchField.Label, Term(term()))

  @JvmName("labelIdTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun labelId(term: () -> Term): Field = add(SearchField.LabelId, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun labelId(term: () -> LabelMbid): Field =
    add(SearchField.LabelId, Term(term().value))

  @JvmName("languageTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun language(term: () -> Term): Field = add(SearchField.Language, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun language(term: () -> String): Field = language { Term(term()) }

  @JvmName("mediumCountTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun mediumCount(term: () -> Term): Field = add(SearchField.MediumCount, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun mediumCount(term: () -> Int): Field = mediumCount { Term(term()) }

  @JvmName("mediumDiscCountTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun mediumDiscCount(term: () -> Term): Field =
    add(SearchField.MediumDiscCount, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun mediumDiscCount(term: () -> Int): Field = mediumDiscCount { Term(term()) }

  @JvmName("mediumTrackCountTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun mediumTrackCount(term: () -> Term): Field =
    add(SearchField.MediumTrackCount, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun mediumTrackCount(term: () -> Int): Field = mediumTrackCount { Term(term()) }

  @JvmName("packagingTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun packaging(term: () -> Term): Field = add(SearchField.Packaging, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun packaging(term: () -> String): Field = packaging { Term(term()) }

  @JvmName("primaryTypeTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun primaryType(term: () -> Term): Field = add(SearchField.PrimaryType, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun primaryType(term: () -> Release.Type): Field = primaryType { Term(term()) }

  @JvmName("primaryTypeString")
  @OverloadResolutionByLambdaReturnType
  public inline fun primaryType(term: () -> String): Field = primaryType { Term(term()) }

  @JvmName("qualityTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun quality(term: () -> Term): Field = add(SearchField.Quality, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun quality(term: () -> String): Field = quality { Term(term()) }

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

  @JvmName("releaseGroupIdTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun releaseGroupId(term: () -> Term): Field =
    add(SearchField.ReleaseGroupId, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun releaseGroupId(term: () -> ReleaseGroupMbid): Field =
    releaseGroupId { Term(term()) }

  @JvmName("releaseAccentedNameTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun releaseAccentedName(term: () -> Term): Field =
    add(SearchField.ReleaseAccentedName, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun releaseAccentedName(term: () -> String): Field =
    releaseAccentedName { Term(term()) }

  @JvmName("scriptTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun script(term: () -> Term): Field = add(SearchField.Script, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun script(term: () -> String): Field = script { Term(term()) }

  @JvmName("secondaryTypeTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun secondaryType(term: () -> Term): Field = add(SearchField.SecondaryType, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun secondaryType(type: () -> Release.Type): Field = secondaryType { Term(type()) }

  @JvmName("statusTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun status(term: () -> Term): Field = add(SearchField.Status, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun status(status: () -> Release.Status): Field = status { Term(status()) }

  @JvmName("trackCountTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun trackCount(term: () -> Term): Field = add(SearchField.TrackCount, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun trackCount(term: () -> Int): Field = trackCount { Term(term()) }

  public fun add(field: SearchField, term: Term): Field = makeAndAddField(field.value, term)
}
