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

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * ```json
 * "series": {
 *     "name": "Rolling Stone: 500 Greatest Albums of All Time: 2012 edition",
 *     "id": "8668518f-4a1e-4802-8b0d-81703ced6418",
 *     "disambiguation": ""
 * },
 * ```
 */
@JsonClass(generateAdapter = true)
public class Series(
  public var id: String = "",
  public var name: String = "",
  public var disambiguation: String = "",
  public var type: String = "",
  @field:Json(name = "type-id") public var typeId: String = "",
  public var relations: List<Relation> = emptyList()
) {

  public interface Lookup : Include

  @Suppress("unused")
  public enum class Misc(override val value: String) : Lookup {
    Aliases("aliases"),
    Annotation("annotation"),
    Tags("tags"),
    Genres("genres")
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as Series

    if (id != other.id) return false

    return true
  }

  override fun hashCode(): Int {
    return id.hashCode()
  }

  @Suppress("unused")
  public enum class SearchField(public val value: String) {
    /** an alias attached to the series */
    Alias("alias"),

    /** the disambiguation comment for the series */
    Comment("comment"),

    /** the name of the series */
    Series("series"),

    /** the MBID of the series */
    SeriesId("sid"),

    /** the series' type */
    Type("type"),

    /** a tag attached to the series */
    Tag("tag"),
  }

  public companion object {
    public val NullSeries: Series = Series(NullObject.NAME, NullObject.ID)
    public val fallbackMapping: Pair<String, Any> = Series::class.java.name to NullSeries
  }
}

public inline val Series.isNullObject: Boolean
  get() = this === Series.NullSeries
