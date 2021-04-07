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
import com.ealva.ealvabrainz.brainz.data.Recording.SearchField
import com.ealva.ealvabrainz.brainz.data.Recording.SearchField.Isrc
import com.ealva.ealvabrainz.brainz.data.Recording.SearchField.Number
import com.ealva.ealvabrainz.brainz.data.Recording.SearchField.Position
import com.ealva.ealvabrainz.brainz.data.Recording.SearchField.Recording
import com.ealva.ealvabrainz.brainz.data.RecordingMbid
import com.ealva.ealvabrainz.brainz.data.Release
import com.ealva.ealvabrainz.brainz.data.ReleaseGroup
import com.ealva.ealvabrainz.brainz.data.ReleaseGroupMbid
import com.ealva.ealvabrainz.brainz.data.ReleaseMbid
import com.ealva.ealvabrainz.brainz.data.TrackMbid
import com.ealva.ealvabrainz.common.AlbumTitle
import com.ealva.ealvabrainz.common.ArtistName
import com.ealva.ealvabrainz.common.BrainzMarker
import com.ealva.ealvabrainz.common.RecordingTitle
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
public class RecordingSearch(query: Query = Query()) : BaseSearch<SearchField>(query) {
  /**
   * (part of) any [alias](https://musicbrainz.org/doc/Aliases) attached to the recording
   * (diacritics are ignored)
   */
  public fun alias(term: String): Field = alias { Term(term) }
  public fun alias(build: TermBuilder.() -> Term): Field =
    add(SearchField.Alias, TermBuilder().build())

  /**
   * (part of) the combined credited artist name for the recording, including join phrases
   * (e.g. "Artist X feat.")
   */
  public fun artist(term: ArtistName): Field = artist { Term(term) }
  public fun artist(build: TermBuilder.() -> Term): Field =
    add(SearchField.Artist, TermBuilder().build())

  /** the MBID of any of the recording artists */
  public fun artistId(term: ArtistMbid): Field = artistId { Term(term) }
  public fun artistId(build: MbidTermBuilder<ArtistMbid>.() -> Term): Field =
    add(SearchField.ArtistId, MbidTermBuilder<ArtistMbid>().build())

  /** (part of) the name of any of the recording artists */
  public fun artistName(term: ArtistName): Field = artistName { Term(term) }
  public fun artistName(build: TermBuilder.() -> Term): Field =
    add(SearchField.ArtistName, TermBuilder().build())

  /** (part of) the recording's disambiguation comment */
  public fun comment(term: String): Field = comment { Term(term) }
  public fun comment(build: TermBuilder.() -> Term): Field =
    add(SearchField.Comment, TermBuilder().build())

  /**
   * the 2-letter code (ISO 3166-1 alpha-2) for the country any release of this recording was
   * released in
   */
  public fun country(term: String): Field = country { Term(term) }
  public fun country(build: TermBuilder.() -> Term): Field =
    add(SearchField.Country, TermBuilder().build())

  /** (part of) the credited name of any of the recording artists on this particular recording */
  public fun creditName(term: ArtistName): Field = creditName { Term(term) }
  public fun creditName(build: TermBuilder.() -> Term): Field =
    add(SearchField.CreditName, TermBuilder().build())

  /** the release date of any release including this recording (e.g. "1980-01-22") */
  public fun date(term: LocalDate): Field = date { Term(term) }
  public fun date(term: Year): Field = date { Term(term) }
  public fun date(build: DateTermBuilder.() -> Term): Field =
    add(SearchField.Date, DateTermBuilder().build())

  /** Default searches [Recording] */
  public fun default(term: RecordingTitle): Field = default { Term(term) }
  public fun default(build: TermBuilder.() -> Term): Field =
    add(SearchField.Default, TermBuilder().build())

  /** duration of track in milliseconds */
  public fun duration(term: Long): Field = duration { Term(term) }
  public fun duration(term: NumberTermBuilder.() -> Term): Field =
    add(SearchField.Duration, NumberTermBuilder().term())

  /** the release date of the earliest release including this recording (e.g. "1980-01-22") */
  public fun firstReleaseDate(term: LocalDate): Field = firstReleaseDate { Term(term) }
  public fun firstReleaseDate(term: Year): Field = firstReleaseDate { Term(term) }
  public fun firstReleaseDate(build: DateTermBuilder.() -> Term): Field =
    add(SearchField.FirstReleaseDate, DateTermBuilder().build())

  /**
   * the [format](https://musicbrainz.org/doc/Release/Format) of any medium including this
   * recording (insensitive to case, spaces, and separators)
   */
  public fun format(term: String): Field = format { Term(term) }
  public fun format(build: TermBuilder.() -> Term): Field =
    add(SearchField.Format, TermBuilder().build())

  /**
   * The International Standard Recording Code [(ISRC)](https://musicbrainz.org/doc/ISRC), an
   * identification system for audio and music video recordings. Includes isrcs for all recordings
   */
  public fun isrc(term: String): Field = isrc { Term(term) }
  public fun isrc(build: TermBuilder.() -> Term): Field = add(Isrc, TermBuilder().build())

  /** the free-text number of the track on any medium including this recording (e.g. "A4")  */
  public fun number(term: String): Field = number { Term(term) }
  public fun number(build: TermBuilder.() -> Term): Field = add(Number, TermBuilder().build())

  /** the position inside its release of any medium including this recording (starts at 1)  */
  public fun position(term: Int): Field = position { Term(term) }
  public fun position(term: NumberTermBuilder.() -> Term): Field =
    add(Position, NumberTermBuilder().term())

  /**
   * the primary [type](https://musicbrainz.org/doc/Release_Group/Type#Primary_types) of any
   * release group including this recording
   */
  public fun primaryType(type: ReleaseGroup.Type): Field = primaryType { Term(type) }
  public fun primaryType(build: ReleaseGroupTypeTerm.Builder.() -> Term): Field =
    add(SearchField.PrimaryType, ReleaseGroupTypeTerm.Builder().build())

  /** the recording duration, quantized (duration in milliseconds / 2000) */
  public fun quantizedDuration(term: Long): Field = quantizedDuration { Term(term) }
  public fun quantizedDuration(build: NumberTermBuilder.() -> Term): Field =
    add(SearchField.QuantizedDuration, NumberTermBuilder().build())

  /**
   * (part of) the recording's title, or the name of a track connected to this recording
   * (diacritics are ignored)
   */
  public fun recording(term: RecordingTitle): Field = recording { Term(term) }
  public fun recording(build: TermBuilder.() -> Term): Field = add(Recording, TermBuilder().build())

  /**
   * (part of) the recording's name, or the name of a track connected to this recording (with the
   * specified diacritics)
   */
  public fun recordingAccent(term: String): Field = recordingAccent { Term(term) }
  public fun recordingAccent(build: TermBuilder.() -> Term): Field =
    add(SearchField.RecordingAccent, TermBuilder().build())

  /** the recording's MBID  */
  public fun recordingId(term: RecordingMbid): Field = recordingId { Term(term) }
  public fun recordingId(build: MbidTermBuilder<RecordingMbid>.() -> Term): Field =
    add(SearchField.RecordingId, MbidTermBuilder<RecordingMbid>().build())

  /** (part of) the name of any release including this recording */
  public fun release(term: AlbumTitle): Field = release { Term(term) }
  public fun release(build: TermBuilder.() -> Term): Field =
    add(SearchField.Release, TermBuilder().build())

  /** the MBID of any release group including this recording */
  public fun releaseId(term: ReleaseMbid): Field = releaseId { Term(term) }
  public fun releaseId(build: MbidTermBuilder<ReleaseMbid>.() -> Term): Field =
    add(SearchField.ReleaseId, MbidTermBuilder<ReleaseMbid>().build())

  /** the MBID of any release group including this recording */
  public fun releaseGroupId(term: ReleaseGroupMbid): Field = releaseGroupId { Term(term) }
  public fun releaseGroupId(term: MbidTermBuilder<ReleaseMbid>.() -> Term): Field =
    add(SearchField.ReleaseGroupId, MbidTermBuilder<ReleaseMbid>().term())

  /**
   * any of the [secondary types](https://musicbrainz.org/doc/Release_Group/Type#Secondary_types)
   * of any release group including this recording
   */
  public fun secondaryType(type: ReleaseGroup.Type): Field = secondaryType { Term(type) }
  public fun secondaryType(builder: ReleaseGroupTypeTerm.Builder.() -> Term): Field =
    add(SearchField.SecondaryType, ReleaseGroupTypeTerm.Builder().builder())

  /**
   * the [status](https://musicbrainz.org/doc/Release#Status) of any release including this
   * recording
   */
  public fun status(type: Release.Status): Field = status { Term(type) }
  public fun status(builder: ReleaseStatusTerm.Builder.() -> Term): Field =
    add(SearchField.Status, ReleaseStatusTerm.Builder().builder())

  /** (part of) a tag attached to the recording  */
  public fun tag(build: TermBuilder.() -> Term): Field = add(SearchField.Tag, TermBuilder().build())
  public fun tag(term: String): Field = tag { Term(term) }

  /** the MBID of a track connected to this recording  */
  public fun trackId(term: TrackMbid): Field = trackId { Term(term) }
  public fun trackId(build: MbidTermBuilder<TrackMbid>.() -> Term): Field =
    add(SearchField.TrackId, MbidTermBuilder<TrackMbid>().build())

  /**
   * the position of the track on any medium including this recording (starts at 1, pre-gaps at 0)
   */
  public fun trackNumber(term: Int): Field = trackNumber { Term(term) }
  public fun trackNumber(build: NumberTermBuilder.() -> Term): Field =
    add(SearchField.TrackNumber, NumberTermBuilder().build())

  /** the number of tracks on any medium including this recording */
  public fun trackCount(term: Int): Field = trackCount { Term(term) }
  public fun trackCount(build: NumberTermBuilder.() -> Term): Field =
    add(SearchField.TrackCount, NumberTermBuilder().build())

  /** the number of tracks on any release (as a whole) including this recording */
  public fun releaseTrackCount(term: Int): Field = releaseTrackCount { Term(term) }
  public fun releaseTrackCount(build: NumberTermBuilder.() -> Term): Field =
    add(SearchField.ReleaseTrackCount, NumberTermBuilder().build())

  /** a boolean flag (true/false) indicating whether or not the recording is a video recording */
  public fun video(term: Boolean): Field = add(SearchField.Video, Term(term))

  public companion object {
    public inline operator fun invoke(search: RecordingSearch.() -> Unit): String {
      return RecordingSearch().apply(search).toString()
    }
  }
}
