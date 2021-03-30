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

import com.ealva.ealvabrainz.brainz.data.Release
import com.ealva.ealvabrainz.brainz.data.Release.SearchField
import com.ealva.ealvabrainz.common.AlbumTitle
import com.ealva.ealvabrainz.common.ArtistMbid
import com.ealva.ealvabrainz.common.ArtistName
import com.ealva.ealvabrainz.common.BrainzMarker
import com.ealva.ealvabrainz.common.LabelMbid
import com.ealva.ealvabrainz.common.LabelName
import com.ealva.ealvabrainz.common.ReleaseGroupMbid
import com.ealva.ealvabrainz.common.ReleaseMbid
import com.ealva.ealvabrainz.lucene.Field
import com.ealva.ealvabrainz.lucene.Query
import com.ealva.ealvabrainz.lucene.Term
import java.time.LocalDate
import java.util.Date
import kotlin.experimental.ExperimentalTypeInference

@OptIn(ExperimentalTypeInference::class)
@BrainzMarker
public class ReleaseSearch(query: Query = Query()) : BaseSearch<SearchField>(query) {
  @JvmName("aliasTerm")
  @OverloadResolutionByLambdaReturnType
  /**
   * (part of) any [alias](https://musicbrainz.org/doc/Aliases) attached to the release (diacritics
   * are ignored)
   */
  public inline fun alias(term: () -> Term): Field = add(SearchField.Alias, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun alias(term: () -> String): Field = alias { Term(term()) }

  @JvmName("artistTerm")
  @OverloadResolutionByLambdaReturnType
  /**
   * (part of) the combined credited artist name for the release, including join phrases (e.g.
   * "Artist X feat.")
   */
  public inline fun artist(term: () -> Term): Field = add(SearchField.Artist, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun artist(term: () -> ArtistName): Field = artist { Term(term()) }

  @JvmName("artistIdTerm")
  @OverloadResolutionByLambdaReturnType
  /** the MBID of any of the release artists  */
  public inline fun artistId(term: () -> Term): Field = add(SearchField.ArtistId, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun artistId(mbid: () -> ArtistMbid): Field = artistId { Term(mbid()) }

  @JvmName("artistNameTerm")
  @OverloadResolutionByLambdaReturnType
  /** (part of) the name of any of the release artists */
  public inline fun artistName(term: () -> Term): Field = add(SearchField.ArtistName, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun artistName(term: () -> ArtistName): Field = artistName { Term(term()) }

  @JvmName("asinTerm")
  @OverloadResolutionByLambdaReturnType
  /** an Amazon [ASIN](https://musicbrainz.org/doc/ASIN) for the release */
  public inline fun asin(term: () -> Term): Field = add(SearchField.Asin, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun asin(term: () -> String): Field = asin { Term(term()) }

  @JvmName("barcodeTerm")
  @OverloadResolutionByLambdaReturnType
  /**
   * The barcode of this release which is a machine-readable number used as stock control
   * mechanisms by retailers.
   */
  public inline fun barcode(term: () -> Term): Field = add(SearchField.Barcode, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun barcode(term: () -> String): Field = barcode { Term(term()) }

  @JvmName("catalogNumberTerm")
  @OverloadResolutionByLambdaReturnType
  /** any catalog number for this release (insensitive to case, spaces, and separators) */
  public inline fun catalogNumber(term: () -> Term): Field = add(SearchField.CatalogNumber, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun catalogNumber(term: () -> String): Field = catalogNumber { Term(term()) }

  @JvmName("commentTerm")
  @OverloadResolutionByLambdaReturnType
  /** (part of) the release's disambiguation comment */
  public inline fun comment(term: () -> Term): Field = add(SearchField.Comment, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun comment(term: () -> String): Field = comment { Term(term()) }

  @JvmName("countryTerm")
  @OverloadResolutionByLambdaReturnType
  /** the 2-letter code (ISO 3166-1 alpha-2) for any country the release was released in  */
  public inline fun country(term: () -> Term): Field = add(SearchField.Country, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun country(term: () -> String): Field = country { Term(term()) }

  @JvmName("creditNameTerm")
  @OverloadResolutionByLambdaReturnType
  /** (part of) the credited name of any of the release artists on this particular release */
  public inline fun creditName(term: () -> Term): Field = add(SearchField.CreditName, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun creditName(term: () -> ArtistName): Field = creditName { Term(term()) }

  @JvmName("dateTerm")
  @OverloadResolutionByLambdaReturnType
  /** a release date for the release (e.g. "1980-01-22")  */
  public inline fun date(term: () -> Term): Field = add(SearchField.Date, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun date(term: () -> LocalDate): Field = date { Term(term()) }

  @JvmName("dateOld")
  @OverloadResolutionByLambdaReturnType
  public inline fun date(term: () -> Date): Field = date { Term(term()) }

  @JvmName("dateString")
  @OverloadResolutionByLambdaReturnType
  public inline fun date(term: () -> String): Field =
    add(SearchField.Date, Term(term()))

  @JvmName("defaultTerm")
  @OverloadResolutionByLambdaReturnType
  /** Default searches [SearchField.Release] */
  public inline fun default(term: () -> Term): Field = add(SearchField.Default, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun default(term: () -> AlbumTitle): Field = default { Term(term()) }

  @JvmName("discIdCountTerm")
  @OverloadResolutionByLambdaReturnType
  /** the total number of disc IDs attached to all mediums on the release  */
  public inline fun discIdCount(term: () -> Term): Field = add(SearchField.DiscIdCount, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun discIdCount(term: () -> Int): Field = discIdCount { Term(term()) }

  @JvmName("formatTerm")
  @OverloadResolutionByLambdaReturnType
  /**
   * the [format](https://musicbrainz.org/doc/Release/Format) of any medium in the release
   * (insensitive to case, spaces, and separators)
   */
  public inline fun format(term: () -> Term): Field = add(SearchField.Format, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun format(term: () -> String): Field = format { Term(term()) }

  @JvmName("labelTerm")
  @OverloadResolutionByLambdaReturnType
  /** (part of) the name of any of the release labels  */
  public inline fun label(term: () -> Term): Field = add(SearchField.Label, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun label(term: () -> LabelName): Field = label { Term(term()) }

  @JvmName("labelIdTerm")
  @OverloadResolutionByLambdaReturnType
  /** the MBID of any of the release labels */
  public inline fun labelId(term: () -> Term): Field = add(SearchField.LabelId, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun labelId(term: () -> LabelMbid): Field = labelId { Term(term()) }

  @JvmName("languageTerm")
  @OverloadResolutionByLambdaReturnType
  /**
   * the [ISO 639-3](https://iso639-3.sil.org/code_tables/639/data) code for the release language
   */
  public inline fun language(term: () -> Term): Field = add(SearchField.Language, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun language(term: () -> String): Field = language { Term(term()) }

  @JvmName("mediumCountTerm")
  @OverloadResolutionByLambdaReturnType
  /** number of mediums in the release */
  public inline fun mediumCount(term: () -> Term): Field = add(SearchField.MediumCount, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun mediumCount(term: () -> Int): Field = mediumCount { Term(term()) }

  @JvmName("mediumDiscCountTerm")
  @OverloadResolutionByLambdaReturnType
  /** the number of disc IDs attached to any one medium on the release */
  public inline fun mediumDiscCount(term: () -> Term): Field =
    add(SearchField.MediumDiscCount, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun mediumDiscCount(term: () -> Int): Field = mediumDiscCount { Term(term()) }

  @JvmName("mediumTrackCountTerm")
  @OverloadResolutionByLambdaReturnType
  /** the number of tracks on any one medium on the release */
  public inline fun mediumTrackCount(term: () -> Term): Field =
    add(SearchField.MediumTrackCount, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun mediumTrackCount(term: () -> Int): Field = mediumTrackCount { Term(term()) }

  @JvmName("packagingTerm")
  @OverloadResolutionByLambdaReturnType
  /**
   * the [format](https://musicbrainz.org/doc/Release/Packaging) of the release (insensitive to
   * case, spaces, and separators)
   */
  public inline fun packaging(term: () -> Term): Field = add(SearchField.Packaging, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun packaging(term: () -> String): Field = packaging { Term(term()) }

  @JvmName("primaryTypeTerm")
  @OverloadResolutionByLambdaReturnType
  /**
   * the [primary type](https://musicbrainz.org/doc/Release_Group/Type#Primary_types) of the
   * release group for this release
   */
  public inline fun primaryType(term: () -> Term): Field = add(SearchField.PrimaryType, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun primaryType(term: () -> Release.Type): Field = primaryType { Term(term()) }

  @JvmName("primaryTypeString")
  @OverloadResolutionByLambdaReturnType
  public inline fun primaryType(term: () -> String): Field = primaryType { Term(term()) }

  @JvmName("qualityTerm")
  @OverloadResolutionByLambdaReturnType
  /**
   * the listed [quality](https://musicbrainz.org/doc/Release#Data_quality) of the data for the
   * release (one of "low", "normal", "high")
   */
  public inline fun quality(term: () -> Term): Field = add(SearchField.Quality, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun quality(term: () -> String): Field = quality { Term(term()) }

  @JvmName("releaseTerm")
  @OverloadResolutionByLambdaReturnType
  /** (part of) the release's title (diacritics are ignored)  */
  public inline fun release(term: () -> Term): Field = add(SearchField.Release, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun release(term: () -> AlbumTitle): Field = release { Term(term()) }

  @JvmName("releaseAccentedNameTerm")
  @OverloadResolutionByLambdaReturnType
  /** (part of) the release's title (with the specified diacritics) */
  public inline fun releaseAccentedName(term: () -> Term): Field =
    add(SearchField.ReleaseAccentedName, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun releaseAccentedName(term: () -> String): Field =
    releaseAccentedName { Term(term()) }

  @JvmName("releaseIdTerm")
  @OverloadResolutionByLambdaReturnType
  /** the release's MBID  */
  public inline fun releaseId(term: () -> Term): Field = add(SearchField.ReleaseId, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun releaseId(term: () -> ReleaseMbid): Field = releaseId { Term(term()) }

  @JvmName("releaseGroupIdTerm")
  @OverloadResolutionByLambdaReturnType
  /** the MBID of the release group for this release */
  public inline fun releaseGroupId(term: () -> Term): Field =
    add(SearchField.ReleaseGroupId, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun releaseGroupId(term: () -> ReleaseGroupMbid): Field =
    releaseGroupId { Term(term()) }

  @JvmName("scriptTerm")
  @OverloadResolutionByLambdaReturnType
  /**
   * The 4 character the [ISO 15924](http://unicode.org/iso15924/iso15924-codes.html) script
   * script code (e.g. latn) used for this release
   */
  public inline fun script(term: () -> Term): Field = add(SearchField.Script, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun script(term: () -> String): Field = script { Term(term()) }

  @JvmName("secondaryTypeTerm")
  @OverloadResolutionByLambdaReturnType
  /**
   * any of the [secondary types](https://musicbrainz.org/doc/Release_Group/Type#Secondary_types)
   * of the release group for this release
   */
  public inline fun secondaryType(term: () -> Term): Field = add(SearchField.SecondaryType, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun secondaryType(type: () -> Release.Type): Field = secondaryType { Term(type()) }

  @JvmName("secondaryTypeString")
  @OverloadResolutionByLambdaReturnType
  public inline fun secondaryType(type: () -> String): Field = secondaryType { Term(type()) }

  @JvmName("statusTerm")
  @OverloadResolutionByLambdaReturnType
  /** the [status][com.ealva.ealvabrainz.brainz.data.Release.Status] of the release */
  public inline fun status(term: () -> Term): Field = add(SearchField.Status, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun status(status: () -> Release.Status): Field = status { Term(status()) }

  @JvmName("tagTerm")
  @OverloadResolutionByLambdaReturnType
  /** a tag attached to the release */
  public inline fun tag(term: () -> Term): Field = add(SearchField.Tag, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun tag(term: () -> String): Field = tag { Term(term()) }

  @JvmName("trackCountTerm")
  @OverloadResolutionByLambdaReturnType
  /** total number of tracks over all mediums on the release */
  public inline fun trackCount(term: () -> Term): Field = add(SearchField.TrackCount, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun trackCount(term: () -> Int): Field = trackCount { Term(term()) }

  public companion object {
    public inline operator fun invoke(search: ReleaseSearch.() -> Unit): String {
      return ReleaseSearch().apply(search).toString()
    }
  }
}
