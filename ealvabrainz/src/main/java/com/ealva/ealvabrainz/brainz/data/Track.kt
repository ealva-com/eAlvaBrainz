/*
 * Copyright (c) 2020  Eric A. Snell
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

package com.ealva.ealvabrainz.brainz.data

import com.ealva.ealvabrainz.brainz.data.Recording.Companion.NullRecording
import com.ealva.ealvabrainz.brainz.data.Track.Companion.NullTrack
import com.ealva.ealvabrainz.moshi.FallbackOnNull
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import timber.log.Timber

/**
 * In MusicBrainz, a track is the way a recording is represented on a particular release (or, more
 * exactly, on a particular medium). Every track has a title and is credited to one or more artists.
 * Tracks are additionally assigned MBIDs, though they cannot be the target of Relationships or
 * other properties conventionally available to entities.
 */
@JsonClass(generateAdapter = true)
class Track(
  var id: String = "",
  var title: String = "",
  var number: String = "",
  var position: Int = 0,
  @field:FallbackOnNull var recording: Recording = NullRecording,
  @field:Json(name = "artist-credit") var artistCredit: List<ArtistCredit> = emptyList(),
  var length: Int = 0
) {
  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as Track

    if (id != other.id) return false

    return true
  }

  override fun hashCode() = id.hashCode()

  override fun toString() = toJson()

  companion object {
    val NullTrack = Track(id = NullObject.ID, title = NullObject.NAME)
    val fallbackMapping: Pair<String, Any> = Track::class.java.name to NullTrack
  }
}

val Track.isNullObject: Boolean
  get() = this === NullTrack

inline class TrackMbid(override val value: String) : Mbid

inline val Track.mbid: TrackMbid
  get() = TrackMbid(id)

@Suppress("NOTHING_TO_INLINE")
inline fun String.toTrackMbid(): TrackMbid {
  if (Mbid.logInvalidMbid && isInvalidMbid()) Timber.w("Invalid TrackMbid")
  return TrackMbid(this)
}

/** First [ArtistCredit] [Artist.mbid] or "" if not found */
@Suppress("unused") val Track.theArtistMbid: String
  get() = if (artistCredit.isNotEmpty()) artistCredit[0].artist.mbid.value else ""

/** First [ArtistCredit] [Artist.name] or "" if not found */
inline val Track.theArtistName: String
  get() = if (artistCredit.isNotEmpty()) artistCredit[0].artist.name else ""

/** First [ArtistCredit] [Artist.sortName] or "" if not found */
@Suppress("unused")
inline val Track.theArtistSortName: String
  get() = if (artistCredit.isNotEmpty()) artistCredit[0].artist.sortName else theArtistName

