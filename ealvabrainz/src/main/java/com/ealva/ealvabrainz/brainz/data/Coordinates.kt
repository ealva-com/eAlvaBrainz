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

import com.ealva.ealvabrainz.brainz.data.Coordinates.Companion.NullCoordinates
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
public data class Coordinates(
  public var latitude: Double = 0.0,
  public var longitude: Double = 0.0
) {
  public companion object {
    public val NullCoordinates: Coordinates = Coordinates()
    public val fallbackMapping: Pair<String, Any> = Coordinates::class.java.name to NullCoordinates
  }
}

public inline val Coordinates.isNullObject: Boolean
  get() = this === NullCoordinates
