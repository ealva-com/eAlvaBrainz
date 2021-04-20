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

import com.ealva.ealvabrainz.log.brainzLogger
import com.ealva.ealvalog.invoke
import com.ealva.ealvalog.w

private val LOG by brainzLogger(ArtistType::class)

public enum class ArtistType(public val value: String) {
  Person("Person"),
  Group("Group"),
  Orchestra("Orchestra"),
  Choir("Choir"),

  /**
   * Example: [Sam the Eagle](https://musicbrainz.org/artist/7321c1f0-20ef-4eb9-b271-c13dcb5712a5)
   */
  Character("Character"),

  /**
   * Example: [Various Artists](https://musicbrainz.org/artist/89ad4ac3-39f7-470e-963a-56509c546377)
   */
  Other("Other"),

  /**
   * Used when an artist type string is unrecognized or as a placeholder until real type is
   * determined
   */
  Unknown("Unknown")
}

/**
 * Should probably change json adapters to do this conversion
 */
public val Artist.artistType: ArtistType
  get() = type.toArtistType()

private val typeNameToArtistMap = ArtistType.values().associateBy { it.value }

/**
 * Convert a String to an ArtistType. If the string is unrecognized, returns [ArtistType.Unknown]
 */
public fun String.toArtistType(): ArtistType {
  return typeNameToArtistMap[this] ?: mapToUnknown(this)
}

private fun mapToUnknown(typeString: String): ArtistType {
  LOG.w { it("'%s' is an Unknown ArtistType", typeString) }
  return ArtistType.Unknown
}
