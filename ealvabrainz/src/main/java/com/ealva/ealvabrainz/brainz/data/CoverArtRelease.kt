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

import com.ealva.ealvabrainz.brainz.data.CoverArtRelease.Companion.NullCoverArtRelease
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CoverArtRelease(
  /**
   * MusicBrainz page url for the release. Has the form:
   * * http://musicbrainz.org/release/76df3287-6cda-33eb-8e9a-044b5e15ffdd
   */
  var release: String = "",

  var images: List<CoverArtImage> = emptyList()
) {
  companion object {
    val NullCoverArtRelease = CoverArtRelease(release = NullObject.NAME)
    val fallbackMapping: Pair<String, Any> = CoverArtRelease::class.java.name to NullCoverArtRelease
  }
}

inline val CoverArtRelease.isNullObject
  get() = this === NullCoverArtRelease
