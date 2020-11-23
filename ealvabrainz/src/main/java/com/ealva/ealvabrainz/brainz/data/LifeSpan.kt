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

import com.ealva.ealvabrainz.brainz.data.LifeSpan.Companion.NullLifeSpan
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
public data class LifeSpan(
  /** Date this lifespan started */
  public var begin: String = "",
  /** Date this lifespan ended. Will be empty string if [ended] = false */
  public var end: String = "",
  /** True if this Lifespan has ended (should be an [end] date */
  public var ended: Boolean = false
) {
  public companion object {
    public val NullLifeSpan: LifeSpan = LifeSpan()
    public val fallbackMapping: Pair<String, Any> = LifeSpan::class.java.name to NullLifeSpan
  }
}

public inline val LifeSpan.isNullObject: Boolean
  get() = this === NullLifeSpan
