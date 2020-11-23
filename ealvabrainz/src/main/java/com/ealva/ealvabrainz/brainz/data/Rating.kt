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

import com.ealva.ealvabrainz.brainz.data.Rating.Companion.NullRating
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * The rating system allows users to rate MusicBrainz entities (artists, labels, releases and
 * tracks).
 */
@JsonClass(generateAdapter = true)
public data class Rating(
  /** Ratings are assigned from 1-5 and then averaged to produce this value. */
  public var value: Float = 0.0F,
  /** The number of times this entity has been rated  */
  @field:Json(name = "votes-count") public var votesCount: Int = 0
) {
  public companion object {
    public val NullRating: Rating = Rating()
    public val fallbackMapping: Pair<String, Any> = Rating::class.java.name to NullRating
  }
}

public inline val Rating.isNullObject: Boolean
  get() = this === NullRating
