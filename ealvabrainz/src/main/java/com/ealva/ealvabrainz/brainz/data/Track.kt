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
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

/**
 * In MusicBrainz, a track is the way a recording is represented on a particular release (or, more
 * exactly, on a particular medium). Every track has a title and is credited to one or more artists.
 * Tracks are additionally assigned MBIDs, though they cannot be the target of Relationships or
 * other properties conventionally available to entities.
 */
@JsonClass(generateAdapter = true)
public class Track(
  public val id: String = "",
  public val title: String = "",
  public val number: String = "",
  public val position: Int = 0,
  @field:FallbackOnNull public val recording: Recording = NullRecording,
  @field:Json(name = "artist-credit") public val artistCredit: List<ArtistCredit> = emptyList(),
  public val length: Int = 0
) {
  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as Track

    if (id != other.id) return false

    return true
  }

  override fun hashCode(): Int = id.hashCode()

  override fun toString(): String = toJson()

  public companion object {
    public val NullTrack: Track = Track(id = NullObject.ID, title = NullObject.NAME)
    public val fallbackMapping: Pair<String, Any> = Track::class.java.name to NullTrack
  }
}

public val Track.isNullObject: Boolean
  get() = this === NullTrack

@Parcelize
@JvmInline
public value class TrackMbid(override val value: String) : Mbid

public inline val Track.mbid: TrackMbid
  get() = TrackMbid(id)
