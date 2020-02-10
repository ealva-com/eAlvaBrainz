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

import com.ealva.ealvabrainz.brainz.data.Thumbnails.Companion.NullThumbnails
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Thumbnails are alternative images within a [CoverArtImage]. Thumbnails are typically reduced
 * size images of the original.
 *
 * All are optional urls. Examples are:
 * * [size250] http://coverartarchive.org/release/76df3287-6cda-33eb-8e9a-044b5e15ffdd/829521842-250.jpg
 * * [size500] http://coverartarchive.org/release/76df3287-6cda-33eb-8e9a-044b5e15ffdd/829521842-500.jpg
 * * [size1200] http://coverartarchive.org/release/76df3287-6cda-33eb-8e9a-044b5e15ffdd/829521842-1200.jpg
 * * [small] http://coverartarchive.org/release/76df3287-6cda-33eb-8e9a-044b5e15ffdd/829521842-250.jpg
 * * [large] http://coverartarchive.org/release/76df3287-6cda-33eb-8e9a-044b5e15ffdd/829521842-500.jpg
 *
 * While [small] and [large] are considered deprecated, they are often the only thumbnails.
 * Strategy should be:
 * * Check [size250] and fallback to [small]
 * * Check [size500] and fallback to [large]
 *
 * so we adhere to the documented deprecation
 */
@JsonClass(generateAdapter = true)
data class Thumbnails(
  @field:Json(name = "250") var size250: String = "",
  @field:Json(name = "500") var size500: String = "",
  @field:Json(name = "1200") var size1200: String = "",
  var small: String = "",
  var large: String = ""
) {
  companion object {
    val NullThumbnails = Thumbnails()
    val fallbackMapping: Pair<String, Any> = Thumbnails::class.java.name to NullThumbnails
  }
}

inline val Thumbnails.isNullObject
  get() = this === NullThumbnails

inline val Thumbnails.theLarge: String
  get() = if (size500.isEmpty()) large else size500

inline val Thumbnails.theSmall: String
  get() = if (size250.isEmpty()) large else size250

