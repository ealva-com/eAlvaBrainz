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

import com.ealva.ealvabrainz.brainz.data.Url.Companion.NullUrl
import com.squareup.moshi.JsonClass

/**
 * A MusicBrainz URL consists of its ID and the actual Url
 *
 * Example
 * ```json
 * "url": {
 *     "id": "56da3f0f-2a88-44ab-97ad-9cf5fa1d0be6",
 *     "resource": "https://www.musik-sammler.de/album/6995/"
 *  }
 * ```
 */
@JsonClass(generateAdapter = true)
public data class Url(
  /** MBID of the Url */
  public var id: String = "",
  /** The url */
  public var resource: String = "",
  public var relations: List<Relation> = emptyList()
) {

  public enum class Include(override val value: String) : Inc {
    Aliases("aliases"),
    Annotation("annotation"),
    Tags("tags")
  }

  @Suppress("unused")
  public enum class Browse(override val value: String) : Inc {
    Annotation("annotation"),
    Tags("tags"),
    UserTags("user-tags"),
    Genres("genres"),
    UserGenres("user-genres"),
    Ratings("ratings"),
    UserRatings("user-ratings");
  }

  public companion object {
    public val NullUrl: Url = Url(id = NullObject.ID)
    public val fallbackMapping: Pair<String, Any> = Url::class.java.name to NullUrl
  }
}

public inline val Url.isNullObject: Boolean
  get() = this === NullUrl
