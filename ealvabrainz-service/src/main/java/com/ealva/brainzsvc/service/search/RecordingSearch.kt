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
import com.ealva.ealvabrainz.brainz.data.Recording.SearchField
import com.ealva.ealvabrainz.brainz.data.RecordingMbid
import com.ealva.ealvabrainz.brainz.data.Release
import com.ealva.ealvabrainz.brainz.data.ReleaseGroupMbid
import com.ealva.ealvabrainz.brainz.data.TrackMbid
import com.ealva.ealvabrainz.lucene.BrainzMarker
import com.ealva.ealvabrainz.lucene.Field
import com.ealva.ealvabrainz.lucene.Term
import java.time.LocalDate
import java.util.Date
import kotlin.experimental.ExperimentalTypeInference

@OptIn(ExperimentalTypeInference::class)
@BrainzMarker
public class RecordingSearch : BaseSearch() {
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

  @JvmName("durationTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun duration(term: () -> Term): Field = make(SearchField.Duration, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun duration(term: () -> Long): Field =
    make(SearchField.Duration, term())

  @JvmName("firstReleaseDateTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun firstReleaseDate(term: () -> Term): Field =
    make(SearchField.FirstReleaseDate, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun firstReleaseDate(term: () -> LocalDate): Field =
    make(SearchField.FirstReleaseDate, term().brainzFormat())

  @JvmName("firstReleaseDateDate")
  @OverloadResolutionByLambdaReturnType
  public inline fun firstReleaseDate(term: () -> Date): Field =
    make(SearchField.FirstReleaseDate, term().brainzFormat())

  @JvmName("formatTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun format(term: () -> Term): Field = make(SearchField.Format, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun format(term: () -> String): Field =
    make(SearchField.Format, term())

  @JvmName("isrcTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun isrc(term: () -> Term): Field = make(SearchField.Isrc, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun isrc(term: () -> String): Field = make(SearchField.Isrc, term())

  @JvmName("numberTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun number(term: () -> Term): Field = make(SearchField.Number, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun number(term: () -> String): Field = make(SearchField.Number, term())

  @JvmName("positionTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun position(term: () -> Term): Field = make(SearchField.Position, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun position(term: () -> String): Field = make(SearchField.Position, term())

  @JvmName("primaryTypeTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun primaryType(term: () -> Term): Field = make(SearchField.PrimaryType, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun primaryType(term: () -> Release.Type): Field =
    make(SearchField.PrimaryType, term().value)

  @JvmName("primaryTypeString")
  @OverloadResolutionByLambdaReturnType
  public inline fun primaryType(term: () -> String): Field = make(SearchField.PrimaryType, term())

  @JvmName("quantizedDurationTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun quantizedDuration(term: () -> Term): Field =
    make(SearchField.QuantizedDuration, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun quantizedDuration(term: () -> String): Field =
    make(SearchField.QuantizedDuration, term())

  @JvmName("recordingTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun recording(term: () -> Term): Field = make(SearchField.Recording, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun recording(term: () -> String): Field = make(SearchField.Recording, term())

  @JvmName("recordingAccentTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun recordingAccent(term: () -> Term): Field =
    make(SearchField.RecordingAccent, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun recordingAccent(term: () -> String): Field =
    make(SearchField.RecordingAccent, term())

  @JvmName("recordingIdTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun recordingId(term: () -> Term): Field = make(SearchField.RecordingId, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun recordingId(term: () -> String): Field = make(SearchField.RecordingId, term())

  @JvmName("releaseIdTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun releaseId(term: () -> Term): Field = make(SearchField.ReleaseId, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun releaseId(term: () -> RecordingMbid): Field =
    make(SearchField.ReleaseId, term())

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

  @JvmName("trackIdTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun trackId(term: () -> Term): Field = make(SearchField.TrackId, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun trackId(term: () -> TrackMbid): Field = make(SearchField.TrackId, term())

  @JvmName("trackNumberTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun trackNumber(term: () -> Term): Field = make(SearchField.TrackNumber, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun trackNumber(term: () -> Int): Field =
    make(SearchField.TrackNumber, term())

  @JvmName("tracksTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun tracks(term: () -> Term): Field = make(SearchField.Tracks, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun tracks(term: () -> Int): Field = make(SearchField.Tracks, term())

  @JvmName("tracksReleaseTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun tracksRelease(term: () -> Term): Field = make(SearchField.TracksRelease, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun tracksRelease(term: () -> Int): Field =
    make(SearchField.TracksRelease, term())

  @JvmName("videoTerm")
  @OverloadResolutionByLambdaReturnType
  public inline fun video(term: () -> Term): Field = make(SearchField.Video, term())

  @OverloadResolutionByLambdaReturnType
  public inline fun video(term: () -> Boolean): Field = make(SearchField.Video, term())

  public fun <T> make(field: SearchField, term: T): Field =
    makeAndAdd(field.value, term)
}
