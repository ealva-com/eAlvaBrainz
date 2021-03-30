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

import com.ealva.ealvabrainz.brainz.data.Attribute.Companion.NullAttribute
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
public class Attribute(
  public val value: String = "",
  @field:Json(name = "type-id") public val typeId: String = "",
  public val type: String = ""
) {
  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as Attribute

    if (value != other.value) return false
    if (typeId != other.typeId) return false
    if (type != other.type) return false

    return true
  }

  override fun hashCode(): Int {
    var result = value.hashCode()
    result = 31 * result + typeId.hashCode()
    result = 31 * result + type.hashCode()
    return result
  }

  public companion object {
    public val NullAttribute: Attribute = Attribute(value = NullObject.NAME)
    public val fallbackMapping: Pair<String, Any> = Attribute::class.java.name to NullAttribute
  }
}

public inline val Attribute.isNullObject: Boolean
  get() = this === NullAttribute
