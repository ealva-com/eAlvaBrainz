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
import com.ealva.ealvabrainz.brainz.data.LabelMbid
import com.ealva.ealvabrainz.brainz.data.Release
import com.ealva.ealvabrainz.brainz.data.Release.SearchField
import com.ealva.ealvabrainz.brainz.data.ReleaseGroup
import com.ealva.ealvabrainz.brainz.data.ReleaseGroupMbid
import com.ealva.ealvabrainz.brainz.data.ReleaseMbid
import com.ealva.ealvabrainz.common.AlbumTitle
import com.ealva.ealvabrainz.common.ArtistName
import com.ealva.ealvabrainz.common.BrainzMarker
import com.ealva.ealvabrainz.common.LabelName
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
public class ReleaseSearch(query: Query = Query()) : BaseSearch<SearchField>(query) {
  /**
   * (part of) any [alias](https://musicbrainz.org/doc/Aliases) attached to the release (diacritics
   * are ignored)
   */
  public fun alias(term: String): Field = alias { Term(term) }
  public fun alias(build: TermBuilder.() -> Term): Field =
    add(SearchField.Alias, TermBuilder().build())

  /**
   * (part of) the combined credited artist name for the release, including join phrases (e.g.
   * "Artist X feat.")
   */
  public fun artist(term: ArtistName): Field = artist { Term(term) }
  public fun artist(build: TermBuilder.() -> Term): Field =
    add(SearchField.Artist, TermBuilder().build())

  /** the MBID of any of the release artists  */
  public fun artistId(term: ArtistMbid): Field = artistId { Term(term) }
  public fun artistId(build: MbidTermBuilder<ArtistMbid>.() -> Term): Field =
    add(SearchField.ArtistId, MbidTermBuilder<ArtistMbid>().build())

  /** (part of) the name of any of the release artists */
  public fun artistName(term: ArtistName): Field = artistName { Term(term) }
  public fun artistName(build: TermBuilder.() -> Term): Field =
    add(SearchField.ArtistName, TermBuilder().build())

  /** an Amazon [ASIN](https://musicbrainz.org/doc/ASIN) for the release */
  public fun asin(term: String): Field = asin { Term(term) }
  public fun asin(build: TermBuilder.() -> Term): Field =
    add(SearchField.Asin, TermBuilder().build())

  /**
   * The barcode of this release which is a machine-readable number used as stock control
   * mechanisms by retailers.
   */
  public fun barcode(term: String): Field = barcode { Term(term) }
  public fun barcode(build: TermBuilder.() -> Term): Field =
    add(SearchField.Barcode, TermBuilder().build())

  /** any catalog number for this release (insensitive to case, spaces, and separators) */
  public fun catalogNumber(term: String): Field = catalogNumber { Term(term) }
  public fun catalogNumber(term: TermBuilder.() -> Term): Field =
    add(SearchField.CatalogNumber, TermBuilder().term())

  /** (part of) the release's disambiguation comment */
  public fun comment(term: String): Field = comment { Term(term) }
  public fun comment(build: TermBuilder.() -> Term): Field =
    add(SearchField.Comment, TermBuilder().build())

  /** the 2-letter code (ISO 3166-1 alpha-2) for any country the release was released in  */
  public fun country(term: String): Field = country { Term(term) }
  public fun country(build: TermBuilder.() -> Term): Field =
    add(SearchField.Country, TermBuilder().build())

  /** (part of) the credited name of any of the release artists on this particular release */
  public fun creditName(term: ArtistName): Field = creditName { Term(term) }
  public fun creditName(build: TermBuilder.() -> Term): Field =
    add(SearchField.CreditName, TermBuilder().build())

  /** a release date for the release (e.g. "1980-01-22")  */
  public fun date(term: LocalDate): Field = date { Term(term) }
  public fun date(term: Year): Field = date { Term(term) }
  public fun date(build: DateTermBuilder.() -> Term): Field =
    add(SearchField.Date, DateTermBuilder().build())

  /** Default searches [SearchField.Release] */
  public fun default(term: AlbumTitle): Field = default { Term(term) }
  public fun default(build: TermBuilder.() -> Term): Field =
    add(SearchField.Default, TermBuilder().build())

  /** the total number of disc IDs attached to all mediums on the release  */
  public fun discIdCount(term: Int): Field = discIdCount { Term(term) }
  public fun discIdCount(build: NumberTermBuilder.() -> Term): Field =
    add(SearchField.DiscIdCount, NumberTermBuilder().build())

  /**
   * the [format](https://musicbrainz.org/doc/Release/Format) of any medium in the release
   * (insensitive to case, spaces, and separators)
   */
  public fun format(term: String): Field = format { Term(term) }
  public fun format(build: TermBuilder.() -> Term): Field =
    add(SearchField.Format, TermBuilder().build())

  /** (part of) the name of any of the release labels  */
  public fun label(term: LabelName): Field = label { Term(term) }
  public fun label(build: TermBuilder.() -> Term): Field =
    add(SearchField.Label, TermBuilder().build())

  /** the MBID of any of the release labels */
  public fun labelId(term: LabelMbid): Field = labelId { Term(term) }
  public fun labelId(build: MbidTermBuilder<LabelMbid>.() -> Term): Field =
    add(SearchField.LabelId, MbidTermBuilder<LabelMbid>().build())

  /**
   * the [ISO 639-3](https://iso639-3.sil.org/code_tables/639/data) code for the release language
   */
  public fun language(term: String): Field = language { Term(term) }
  public fun language(build: TermBuilder.() -> Term): Field =
    add(SearchField.Language, TermBuilder().build())

  /** number of mediums in the release */
  public fun mediumCount(term: Int): Field = mediumCount { Term(term) }
  public fun mediumCount(build: NumberTermBuilder.() -> Term): Field =
    add(SearchField.MediumCount, NumberTermBuilder().build())

  /** the number of disc IDs attached to any one medium on the release */
  public fun mediumDiscCount(term: Int): Field = mediumDiscCount { Term(term) }
  public fun mediumDiscCount(build: NumberTermBuilder.() -> Term): Field =
    add(SearchField.MediumDiscCount, NumberTermBuilder().build())

  /** the number of tracks on any one medium on the release */
  public fun mediumTrackCount(term: Int): Field = mediumTrackCount { Term(term) }
  public fun mediumTrackCount(build: NumberTermBuilder.() -> Term): Field =
    add(SearchField.MediumTrackCount, NumberTermBuilder().build())

  /**
   * the [format](https://musicbrainz.org/doc/Release/Packaging) of the release (insensitive to
   * case, spaces, and separators)
   */
  public fun packaging(term: String): Field = packaging { Term(term) }
  public fun packaging(build: TermBuilder.() -> Term): Field =
    add(SearchField.Packaging, TermBuilder().build())

  /**
   * the [primary type](https://musicbrainz.org/doc/Release_Group/Type#Primary_types) of the
   * release group for this release
   */
  public fun primaryType(type: ReleaseGroup.Type): Field = primaryType { Term(type) }
  public fun primaryType(build: ReleaseGroupTypeTerm.Builder.() -> Term): Field =
    add(SearchField.PrimaryType, ReleaseGroupTypeTerm.Builder().build())

  /**
   * the listed [quality](https://musicbrainz.org/doc/Release#Data_quality) of the data for the
   * release (one of "low", "normal", "high")
   */
  public fun quality(term: String): Field = quality { Term(term) }
  public fun quality(build: TermBuilder.() -> Term): Field =
    add(SearchField.Quality, TermBuilder().build())

  /** (part of) the release's title (diacritics are ignored)  */
  public fun release(term: AlbumTitle): Field = release { Term(term) }
  public fun release(build: TermBuilder.() -> Term): Field =
    add(SearchField.Release, TermBuilder().build())

  /** (part of) the release's title (with the specified diacritics) */
  public fun releaseAccentedName(term: String): Field = releaseAccentedName { Term(term) }
  public fun releaseAccentedName(build: TermBuilder.() -> Term): Field =
    add(SearchField.ReleaseAccentedName, TermBuilder().build())

  /** the release's MBID  */
  public fun releaseId(term: ReleaseMbid): Field = releaseId { Term(term) }
  public fun releaseId(build: MbidTermBuilder<ReleaseMbid>.() -> Term): Field =
    add(SearchField.ReleaseId, MbidTermBuilder<ReleaseMbid>().build())

  /** the MBID of the release group for this release */
  public fun releaseGroupId(term: ReleaseGroupMbid): Field = releaseGroupId { Term(term) }
  public fun releaseGroupId(term: MbidTermBuilder<ReleaseMbid>.() -> Term): Field =
    add(SearchField.ReleaseGroupId, MbidTermBuilder<ReleaseMbid>().term())

  /**
   * The 4 character the [ISO 15924](http://unicode.org/iso15924/iso15924-codes.html) script
   * script code (e.g. latn) used for this release
   */
  public fun script(term: String): Field = script { Term(term) }
  public fun script(build: TermBuilder.() -> Term): Field =
    add(SearchField.Script, TermBuilder().build())

  /**
   * any of the [secondary types](https://musicbrainz.org/doc/Release_Group/Type#Secondary_types)
   * of the release group for this release
   */
  public fun secondaryType(type: ReleaseGroup.Type): Field = secondaryType { Term(type) }
  public fun secondaryType(builder: ReleaseGroupTypeTerm.Builder.() -> Term): Field =
    add(SearchField.SecondaryType, ReleaseGroupTypeTerm.Builder().builder())

  /** the [status][com.ealva.ealvabrainz.brainz.data.Release.Status] of the release */
  public fun status(type: Release.Status): Field = status { Term(type) }
  public fun status(build: ReleaseStatusTerm.Builder.() -> Term): Field =
    add(SearchField.Status, ReleaseStatusTerm.Builder().build())

  /** a tag attached to the release */
  public fun tag(build: TermBuilder.() -> Term): Field = add(SearchField.Tag, TermBuilder().build())
  public fun tag(term: String): Field = tag { Term(term) }

  /** total number of tracks over all mediums on the release */
  public fun trackCount(term: Int): Field = trackCount { Term(term) }
  public fun trackCount(build: NumberTermBuilder.() -> Term): Field =
    add(SearchField.TrackCount, NumberTermBuilder().build())

  public companion object {
    public inline operator fun invoke(search: ReleaseSearch.() -> Unit): String {
      return ReleaseSearch().apply(search).toString()
    }
  }
}
