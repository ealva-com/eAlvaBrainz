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

import com.ealva.ealvabrainz.brainz.data.Medium.Companion.NullMedium
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * In MusicBrainz terminology, a prototypical medium is one of the physical, separate things you
 * would get when you buy something in a record store. They are the individual CDs, vinyls, etc.
 * contained within the packaging of an album (or any other type of release). Mediums are always
 * included in a release, and have a position in said release (e.g. disc 1 or disc 2). They have
 * a format, like CD, 12" vinyl or cassette (in some cases this will be unknown), and can have an
 * optional title (e.g. disc 2: The Early Years).
 *
 * Note that a side of a disc, like side A of a vinyl, is not its own medium; the whole vinyl disc
 * is. Digital (as opposed to physical) releases don't have "real" mediums, but they should be
 * entered as several mediums if they are officially divided in several "discs". Exceptions in the
 * treatment of mediums can exist: DualDiscs are usually entered as two mediums if the tracklist is
 * the same on both sides, but with different mixes.
 *
 * [Disc IDs][Disc.id] are linked to mediums.
 */
@JsonClass(generateAdapter = true)
data class Medium(
  var title: String = "",
  @field:Json(name = "format-id") var formatId: String = "",
  var format: String = "",
  @field:Json(name = "disc-count") var discCount: Int = 0,
  @field:Json(name = "track-count") var trackCount: Int = 0,
  @field:Json(name = "track-offset") var trackOffset: Int = 0,
  var discs: List<Disc> = emptyList(),
  var position: Int = 0,
  internal var tracks: List<Track> = emptyList(),
  internal var track: List<Track> = emptyList()
) {
  companion object {
    val NullMedium = Medium(title = NullObject.NAME)
    val fallbackMapping: Pair<String, Any> = Medium::class.java.name to NullMedium
  }
}

inline val Medium.isNullObject
  get() = this === NullMedium

val Medium.theTracks
get() = if (track.isNotEmpty()) track else tracks
