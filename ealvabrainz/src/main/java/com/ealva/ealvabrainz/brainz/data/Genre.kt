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

import com.ealva.ealvabrainz.brainz.data.Genre.Companion.NullGenre
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Genre(
  /** Genre name in lowercase */
  var name: String = "",
  /** Number of votes for this genres applicability to the entity */
  var count: Int = 0
) {
  companion object {
    val NullGenre = Genre(name = NullObject.NAME)
    val fallbackMapping: Pair<String, Any> = Genre::class.java.name to NullGenre
  }
}

val Genre.isNullObject: Boolean
  get() = this === NullGenre
