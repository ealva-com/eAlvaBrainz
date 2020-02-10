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
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Recording(
  var id: String = "",
  var title: String = "",
  var disambiguation: String = "",
  @field:Json(name = "artist-credit") var artistCredit: List<ArtistCredit> = emptyList(),
  var length: Int = 0,
  var aliases: List<Alias> = emptyList(),
  var annotation: String = "",
  var genres: List<Genre> = emptyList(),
  var isrcs: List<String> = emptyList(),
  var rating: Rating = Rating.NullRating,
//  var relations: List<> = emptyList(),
  var releases: List<Release> = emptyList(),
  var tags: List<Tag> = emptyList(),
  var video: Boolean = false
) {
  companion object {
    val NullRecording = Recording(id = NullObject.ID)
    val fallbackMapping: Pair<String, Any> = Recording::class.java.name to NullRecording
  }
}

val Recording.isNullObject
  get() = this === NullRecording

inline class RecordingMbid(override val value: String) : Mbid

inline val Recording.mbid
  get() = RecordingMbid(id)

