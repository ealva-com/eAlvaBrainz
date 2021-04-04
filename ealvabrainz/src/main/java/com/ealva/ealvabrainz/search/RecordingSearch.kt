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

import com.ealva.ealvabrainz.brainz.data.Recording.SearchField
import com.ealva.ealvabrainz.brainz.data.Recording.SearchField.Recording
import com.ealva.ealvabrainz.brainz.data.Release
import com.ealva.ealvabrainz.brainz.data.ReleaseGroup
import com.ealva.ealvabrainz.common.AlbumTitle
import com.ealva.ealvabrainz.brainz.data.ArtistMbid
import com.ealva.ealvabrainz.common.ArtistName
import com.ealva.ealvabrainz.common.BrainzMarker
import com.ealva.ealvabrainz.brainz.data.RecordingMbid
import com.ealva.ealvabrainz.common.RecordingTitle
import com.ealva.ealvabrainz.brainz.data.ReleaseGroupMbid
import com.ealva.ealvabrainz.brainz.data.ReleaseMbid
import com.ealva.ealvabrainz.brainz.data.TrackMbid
import com.ealva.ealvabrainz.lucene.Field
import com.ealva.ealvabrainz.lucene.Query
import com.ealva.ealvabrainz.lucene.Term
import java.time.LocalDate
import java.util.Date
import kotlin.experimental.ExperimentalTypeInference

@OptIn(ExperimentalTypeInference::class)
@BrainzMarker
public class RecordingSearch(query: Query = Query()) : BaseSearch<SearchField>(query) {
  @JvmName("aliasTerm")
  @OverloadResolutionByLambdaReturnType
  /**
   * (part of) any [alias](https://musicbrainz.org/doc/Aliases) attached to the recording
   * (diacritics are ignored)
   */
  public inline fun alias(term: () -> Term): Field = add(SearchField.Alias, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun alias(term: () -> String): Field = alias { Term(term()) }

  @JvmName("artistTerm")
  @OverloadResolutionByLambdaReturnType
  /**
   * (part of) the combined credited artist name for the recording, including join phrases
   * (e.g. "Artist X feat.")
   */
  public inline fun artist(term: () -> Term): Field = add(SearchField.Artist, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun artist(name: () -> ArtistName): Field = artist { Term(name()) }

  @JvmName("artistIdTerm")
  @OverloadResolutionByLambdaReturnType
  /** the MBID of any of the recording artists */
  public inline fun artistId(term: () -> Term): Field = add(SearchField.ArtistId, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun artistId(mbid: () -> ArtistMbid): Field = artistId { Term(mbid()) }

  @JvmName("artistNameTerm")
  @OverloadResolutionByLambdaReturnType
  /** (part of) the name of any of the recording artists */
  public inline fun artistName(term: () -> Term): Field = add(SearchField.ArtistName, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun artistName(name: () -> ArtistName): Field = artistName { Term(name()) }

  @JvmName("commentTerm")
  @OverloadResolutionByLambdaReturnType
  /** (part of) the recording's disambiguation comment */
  public inline fun comment(term: () -> Term): Field = add(SearchField.Comment, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun comment(term: () -> String): Field = comment { Term(term()) }

  @JvmName("countryTerm")
  @OverloadResolutionByLambdaReturnType
  /**
   * the 2-letter code (ISO 3166-1 alpha-2) for the country any release of this recording was
   * released in
   */
  public inline fun country(term: () -> Term): Field = add(SearchField.Country, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun country(term: () -> String): Field = country { Term(term()) }

  @JvmName("creditNameTerm")
  @OverloadResolutionByLambdaReturnType
  /** (part of) the credited name of any of the recording artists on this particular recording */
  public inline fun creditName(term: () -> Term): Field = add(SearchField.CreditName, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun creditName(term: () -> ArtistName): Field = creditName { Term(term()) }

  @JvmName("dateTerm")
  @OverloadResolutionByLambdaReturnType
  /** the release date of any release including this recording (e.g. "1980-01-22") */
  public inline fun date(term: () -> Term): Field = add(SearchField.Date, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun date(term: () -> LocalDate): Field = date { Term(term()) }

  @JvmName("dateOld")
  @OverloadResolutionByLambdaReturnType
  public inline fun date(term: () -> Date): Field = date { Term(term()) }

  @JvmName("dateString")
  @OverloadResolutionByLambdaReturnType
  public inline fun date(term: () -> String): Field = date { Term(term()) }

  @JvmName("defaultTerm")
  @OverloadResolutionByLambdaReturnType
  /** Default searches [Recording] */
  public inline fun default(term: () -> Term): Field = add(SearchField.Default, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun default(term: () -> RecordingTitle): Field = default { Term(term()) }

  @JvmName("durationTerm")
  @OverloadResolutionByLambdaReturnType
  /** duration of track in milliseconds */
  public inline fun duration(term: () -> Term): Field = add(SearchField.Duration, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun duration(term: () -> Long): Field = duration { Term(term()) }

  @JvmName("firstReleaseDateTerm")
  @OverloadResolutionByLambdaReturnType
  /** the release date of the earliest release including this recording (e.g. "1980-01-22") */
  public inline fun firstReleaseDate(term: () -> Term): Field =
    add(SearchField.FirstReleaseDate, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun firstReleaseDate(term: () -> LocalDate): Field =
    firstReleaseDate { Term(term()) }

  @JvmName("firstReleaseDateDate")
  @OverloadResolutionByLambdaReturnType
  public inline fun firstReleaseDate(term: () -> Date): Field = firstReleaseDate { Term(term()) }

  @JvmName("formatTerm")
  @OverloadResolutionByLambdaReturnType
  /**
   * the [format](https://musicbrainz.org/doc/Release/Format) of any medium including this
   * recording (insensitive to case, spaces, and separators)
   */
  public inline fun format(term: () -> Term): Field = add(SearchField.Format, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun format(term: () -> String): Field = format { Term(term()) }

  @JvmName("isrcTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun isrc(term: () -> Term): Field = add(SearchField.Isrc, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun isrc(term: () -> String): Field = isrc { Term(term()) }

  @JvmName("numberTerm")
  @OverloadResolutionByLambdaReturnType
  /** the free-text number of the track on any medium including this recording (e.g. "A4")  */
  public inline fun number(term: () -> Term): Field = add(SearchField.Number, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun number(term: () -> String): Field = number { Term(term()) }

  @JvmName("positionTerm")
  @OverloadResolutionByLambdaReturnType
  /** the position inside its release of any medium including this recording (starts at 1)  */
  public inline fun position(term: () -> Term): Field = add(SearchField.Position, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun position(term: () -> Int): Field = position { Term(term()) }

  @JvmName("primaryTypeTerm")
  @OverloadResolutionByLambdaReturnType
  /** [Primary type][ReleaseGroup.Type] of the release group (album, single, ep, other) */
  public inline fun primaryType(term: () -> Term): Field = add(SearchField.PrimaryType, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun primaryType(term: () -> ReleaseGroup.Type): Field = primaryType { Term(term()) }

  @JvmName("quantizedDurationTerm")
  @OverloadResolutionByLambdaReturnType
  /** the recording duration, quantized (duration in milliseconds / 2000) */
  public inline fun quantizedDuration(term: () -> Term): Field =
    add(SearchField.QuantizedDuration, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun quantizedDuration(term: () -> Long): Field = quantizedDuration { Term(term()) }

  @JvmName("recordingTerm")
  @OverloadResolutionByLambdaReturnType
  /**
   * (part of) the recording's title, or the name of a track connected to this recording
   * (diacritics are ignored)
   */
  public inline fun recording(term: () -> Term): Field = add(Recording, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun recording(term: () -> RecordingTitle): Field = recording { Term(term()) }

  @JvmName("recordingAccentTerm")
  @OverloadResolutionByLambdaReturnType
  /**
   * (part of) the recording's name, or the name of a track connected to this recording (with the
   * specified diacritics)
   */
  public inline fun recordingAccent(term: () -> Term): Field =
    add(SearchField.RecordingAccent, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun recordingAccent(term: () -> String): Field = recordingAccent { Term(term()) }

  @JvmName("recordingIdTerm")
  @OverloadResolutionByLambdaReturnType
  /** the recording's MBID  */
  public inline fun recordingId(term: () -> Term): Field = add(SearchField.RecordingId, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun recordingId(term: () -> RecordingMbid): Field = recordingId { Term(term()) }

  @JvmName("releaseTerm")
  @OverloadResolutionByLambdaReturnType
  /** (part of) the name of any release including this recording */
  public inline fun release(term: () -> Term): Field = add(SearchField.Release, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun release(term: () -> AlbumTitle): Field = release { Term(term()) }

  @JvmName("releaseIdTerm")
  @OverloadResolutionByLambdaReturnType
  /** the MBID of any release group including this recording */
  public inline fun releaseId(term: () -> Term): Field = add(SearchField.ReleaseId, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun releaseId(term: () -> ReleaseMbid): Field = releaseId { Term(term()) }

  @JvmName("releaseGroupIdTerm")
  @OverloadResolutionByLambdaReturnType
  /** the MBID of any release group including this recording */
  public inline fun releaseGroupId(term: () -> Term): Field =
    add(SearchField.ReleaseGroupId, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun releaseGroupId(term: () -> ReleaseGroupMbid): Field =
    releaseGroupId { Term(term()) }

  @JvmName("secondaryTypeTerm")
  @OverloadResolutionByLambdaReturnType
  /**
   * secondary type of the release group (audiobook, compilation, interview, live, remix
   * soundtrack, spokenword)
   */
  public inline fun secondaryType(term: () -> Term): Field = add(SearchField.SecondaryType, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun secondaryType(term: () -> ReleaseGroup.Type): Field =
    secondaryType { Term(term()) }

  @JvmName("statusTerm")
  @OverloadResolutionByLambdaReturnType
  /**
   * the [status](https://musicbrainz.org/doc/Release#Status) of any release including this
   * recording
   */
  public inline fun status(term: () -> Term): Field = add(SearchField.Status, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun status(term: () -> Release.Status): Field = status { Term(term()) }

  @JvmName("tagTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun tag(term: () -> Term): Field = add(SearchField.Tag, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun tag(term: () -> String): Field = tag { Term(term()) }

  @JvmName("trackIdTerm")
  @OverloadResolutionByLambdaReturnType
  /** the MBID of a track connected to this recording  */
  public inline fun trackId(term: () -> Term): Field = add(SearchField.TrackId, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun trackId(term: () -> TrackMbid): Field = trackId { Term(term()) }

  @JvmName("trackNumberTerm")
  @OverloadResolutionByLambdaReturnType
  /**
   * the position of the track on any medium including this recording (starts at 1, pre-gaps at 0)
   */
  public inline fun trackNumber(term: () -> Term): Field = add(SearchField.TrackNumber, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun trackNumber(term: () -> Int): Field = trackNumber { Term(term()) }

  @JvmName("tracksTerm")
  @OverloadResolutionByLambdaReturnType
  /** the number of tracks on any medium including this recording */
  public inline fun trackCount(term: () -> Term): Field = add(SearchField.TrackCount, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun trackCount(term: () -> Int): Field = trackCount { Term(term()) }

  @JvmName("tracksReleaseTerm")
  @OverloadResolutionByLambdaReturnType
  /** the number of tracks on any release (as a whole) including this recording */
  public inline fun releaseTrackCount(term: () -> Term): Field =
    add(SearchField.ReleaseTrackCount, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun releaseTrackCount(term: () -> Int): Field = releaseTrackCount { Term(term()) }

  @JvmName("videoTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun video(term: () -> Term): Field = add(SearchField.Video, term())

  @OverloadResolutionByLambdaReturnType
  /** a boolean flag (true/false) indicating whether or not the recording is a video recording */
  public inline fun video(term: () -> Boolean): Field = video { Term(term()) }

  public companion object {
    public inline operator fun invoke(search: RecordingSearch.() -> Unit): String {
      return RecordingSearch().apply(search).toString()
    }
  }
}
