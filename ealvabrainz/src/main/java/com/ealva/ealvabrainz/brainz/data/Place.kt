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

import com.ealva.ealvabrainz.brainz.data.Place.Companion.NullPlace
import com.ealva.ealvabrainz.moshi.FallbackOnNull
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class Place(
  var id: String = "",
  var name: String = "",
  var type: String = "",
  var address: String = "",
  @field:FallbackOnNull var area: Area = Area.NullArea,
  @field:FallbackOnNull var coordinates: Coordinates = Coordinates.NullCoordinates,
  var disambiguation: String = "",
  @field:Json(name = "life-span") @field:FallbackOnNull var lifeSpan: LifeSpan = LifeSpan.NullLifeSpan,
  @field:Json(name = "type-id") var typeId: String = ""
) {
  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as Place

    if (id != other.id) return false

    return true
  }

  override fun hashCode() = id.hashCode()

  override fun toString() = toJson()


  companion object {
    val NullPlace = Place(id = NullObject.ID, name = NullObject.NAME)
    val fallbackMapping: Pair<String, Any> = Place::class.java.name to NullPlace
  }
}

inline val Place.isNullObject
  get() = this === NullPlace

inline class PlaceMbid(override val value: String) : Mbid

inline val Place.mbid
  get() = PlaceMbid(id)
