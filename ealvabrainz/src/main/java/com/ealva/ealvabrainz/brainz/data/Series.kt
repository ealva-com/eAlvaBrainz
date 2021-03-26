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
 * A series is a sequence of separate release groups, releases, recordings, works or events with a
 * common theme. The theme is usually prominent in the branding of the entities in the series and
 * the individual entities will often have been given a number indicating the position in the
 * series.
 */
@JsonClass(generateAdapter = true)
public class Series(
  /** The Series MBID */
  public var id: String = "",
  /** The official name of the series. */
  public var name: String = "",
  public var disambiguation: String = "",
  /**
   * The type primarily describes what type of entity the series contains. The possible values are:
   * * Release group - A series of release groups.
   * * Release -  A series of releases.
   * * Recording - A series of recordings.
   * * Work - A series of works.
   *     * Catalogue - A series of works which form a catalogue of classical compositions.
   * * Event - A series of events.
   *     * Tour - A series of related concerts by an artist in different locations.
   *     * Festival - A recurring festival, usually happening annually in the same location.
   *     * Run - A series of performances of the same show at the same venue.
   */
  public var type: String = "",
  @field:Json(name = "type-id") public var typeId: String = "",
  /**
   * See the [page about annotations](https://musicbrainz.org/doc/Annotation) for more information.
   */
  public var annotation: String = "",
  public var relations: List<Relation> = emptyList()
) {

  @Suppress("unused")
  public enum class Include(override val value: String) : Inc {
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

  public enum class Browse(override val value: String) : Inc {
    Aliases("aliases"),
    Annotation("annotation"),
    Tags("tags"),
    UserTags("user-tags"),
    Genres("genres"),
    UserGenres("user-genres");
  }

  public enum class SearchField(public override val value: String) : EntitySearchField {
    Default(""),
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
