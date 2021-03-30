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
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * A place is a building or outdoor area used for performing or producing music.
 */
@JsonClass(generateAdapter = true)
public class Place(
  /** MusicBrainz ID (MBID) */
  public val id: String = "",
  /** The place name is the official name of a place. */
  public val name: String = "",
  /**
   * The type categorises the place based on its primary function. The possible values are:
   * * **Studio**
   * A place designed for non-live production of music, typically a recording studio.
   * * **Venue**
   * A place that has live artistic performances as one of its primary functions, such as a
   * concert hall.
   * * **Stadium**
   * A place whose main purpose is to host outdoor sport events, typically consisting of a
   * pitch surrounded by a structure for spectators with no roof, or a roof which can be retracted.
   * * **Indoor arena**
   * A place consisting of a large enclosed area with a central event space surrounded by tiered
   * seating for spectators, which can be used for indoor sports, concerts and other entertainment
   * events.
   * * **Religious building**
   * A place mostly designed and used for religious purposes, like a church, cathedral or synagogue.
   * * **Educational institution**
   * A school, university or other similar educational institution (especially, but not only, one
   * where music is taught)
   * * **Pressing plant**
   * A place (generally a factory) at which physical media are manufactured.
   * * **Other**
   * Anything which does not fit into the above categories.
   */
  public val type: String = "",
  @field:Json(name = "type-id") public val typeId: String = "",
  /**
   * The address describes the location of the place using the standard addressing format for the
   * country it is located in.
   */
  public val address: String = "",
  /** The area links to the area, such as the city, in which the place is located. */
  @field:FallbackOnNull public val area: Area = Area.NullArea,
  /** The latitude and longitude describe the location of the place using geographic coordinates. */
  @field:FallbackOnNull public val coordinates: Coordinates = Coordinates.NullCoordinates,
  /** Begin/end information */
  @field:Json(name = "life-span") @field:FallbackOnNull public val lifeSpan: LifeSpan =
    LifeSpan.NullLifeSpan,
  /**
   * Aliases are alternate names for a place, which currently have two main functions: localised
   * names and search hints. Localised names are used to store the official names used in different
   * languages and countries. These use the locale field to identify which language or country the
   * name is for. Search hints are used to help both users and the server when searching and can be
   * a number of things including alternate names, nicknames or even misspellings.
   */
  public val aliases: List<Alias> = emptyList(),
  public val relations: List<Relation> = emptyList(),
  /**
   * See the
   * [page about disambiguation comments](https://musicbrainz.org/doc/Disambiguation_Comment)
   * for more information
   */
  public val disambiguation: String = "",
  /**
   * See the [page about annotations](https://musicbrainz.org/doc/Annotation) for more information.
   */
  public val annotation: String = ""
) {

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as Place

    if (id != other.id) return false

    return true
  }

  override fun hashCode(): Int = id.hashCode()

  override fun toString(): String = toJson()

  public enum class Include(override val value: String) : Inc {
    Aliases("aliases"),
    Annotation("annotation"),
    Tags("tags"),
    Genres("genres")
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
    /** (part of) the physical address for this place  */
    Address("address"),

    /**
     * (part of) any [alias](https://musicbrainz.org/doc/Aliases) attached to the place (diacritics
     * are ignored)
     */
    Alias("alias"),

    /** (part of) the name of the place's main associated area */
    Area("area"),

    /** the place's begin date (e.g. "1980-01-22") */
    Begin("begin"),

    /** (part of) the place's disambiguation comment */
    Comment("comment"),

    /** Default searches [Address], [Alias], [Area], and [Place] */
    Default(""),

    /** the place's end date (e.g. "1980-01-22")  */
    End("end"),

    /** a boolean flag (true/false) indicating whether or not the place has ended (is closed) */
    Ended("ended"),

    /**
     * the [(WGS 84)](https://en.wikipedia.org/wiki/World_Geodetic_System) latitude of the place's
     * coordinates (e.g. "58.388226")
     */
    Latitude("lat"),

    /**
     * the [(WGS 84)](https://en.wikipedia.org/wiki/World_Geodetic_System) longitude of the place's
     * coordinates (e.g. "58.388226")
     */
    Longitude("long"),

    /** (part of) the place's name (diacritics are ignored) */
    Place("place"),

    /** (part of) the place's name (with the specified diacritics) */
    PlaceAccent("placeaccent"),

    /** the place's MBID  */
    PlaceId("pid"),

    /** the place's [type](https://musicbrainz.org/doc/Place#Type) */
    Type("type"),
  }

  public companion object {
    public val NullPlace: Place = Place(id = NullObject.ID, name = NullObject.NAME)
    public val fallbackMapping: Pair<String, Any> = Place::class.java.name to NullPlace
  }
}

public inline val Place.isNullObject: Boolean
  get() = this === NullPlace
