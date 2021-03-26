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

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
public class ArtistCredit(
  public var name: String = "",
  public var joinphrase: String = "",
  @field:FallbackOnNull public var artist: Artist = Artist.NullArtist
) {
  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as ArtistCredit

    if (name != other.name) return false
    if (joinphrase != other.joinphrase) return false
    if (artist != other.artist) return false

    return true
  }

  override fun hashCode(): Int {
    var result = name.hashCode()
    result = 31 * result + joinphrase.hashCode()
    result = 31 * result + artist.hashCode()
    return result
  }

  public companion object {
    public val NullArtistCredit: ArtistCredit = ArtistCredit(name = NullObject.NAME)
    public val fallbackMapping: Pair<String, Any> =
      ArtistCredit::class.java.name to NullArtistCredit
  }
}

public inline val ArtistCredit.isNullObject: Boolean
  get() = this === ArtistCredit.NullArtistCredit
