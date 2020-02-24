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

/**
 * A place is a building or outdoor area used for performing or producing music.
 */
@JsonClass(generateAdapter = true)
class Place(
  /** MusicBrainz ID (MBID) */
  var id: String = "",
  /** The place name is the official name of a place. */
  var name: String = "",
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
  var type: String = "",
  @field:Json(name = "type-id") var typeId: String = "",
  /**
   * The address describes the location of the place using the standard addressing format for the
   * country it is located in.
   */
  var address: String = "",
  /** The area links to the area, such as the city, in which the place is located. */
  @field:FallbackOnNull var area: Area = Area.NullArea,
  /** The latitude and longitude describe the location of the place using geographic coordinates. */
  @field:FallbackOnNull var coordinates: Coordinates = Coordinates.NullCoordinates,
  /** Begin/end information */
  @field:Json(name = "life-span") @field:FallbackOnNull var lifeSpan: LifeSpan = LifeSpan.NullLifeSpan,
  /**
   * Aliases are alternate names for a place, which currently have two main functions: localised
   * names and search hints. Localised names are used to store the official names used in different
   * languages and countries. These use the locale field to identify which language or country the
   * name is for. Search hints are used to help both users and the server when searching and can be
   * a number of things including alternate names, nicknames or even misspellings.
   */
  var aliases: List<Alias> = emptyList(),
  /**
   * See the [page about disambiguation comments](https://musicbrainz.org/doc/Disambiguation_Comment)
   * for more information
   */
  var disambiguation: String = "",
  /**
   * See the [page about annotations](https://musicbrainz.org/doc/Annotation) for more information.
   */
  var annotation: String = ""
) {

  interface Lookup : Include

  @Suppress("unused")
  enum class Misc(override val value: String) : Lookup {
    Aliases("aliases"),
    Annotation("annotation"),
    Tags("tags"),
    Genres("genres")
  }

  /**
   * Place relationships
   *
   * * [Place-Place](https://musicbrainz.org/relationships/place-place)
   * * [Place-Recording](https://musicbrainz.org/relationships/place-recording)
   * * [Place-Release](https://musicbrainz.org/relationships/place-release)
   * * [Place-URL](https://musicbrainz.org/relationships/place-url)
   * * [Place-Work](https://musicbrainz.org/relationships/place-work)
   */
  @Suppress("unused")
  enum class Relations(override val value: String) : Lookup {
    Place("place-rels"),
    Recording("recording-rels"),
    Release("release-rels"),
    Url("url-rels"),
    Work("work-rels")
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as Place

    if (id != other.id) return false

    return true
  }

  override fun hashCode() = id.hashCode()

  override fun toString() = toJson()

  @Suppress("unused")
  enum class SearchField(val value: String) {
    /** the place ID */
    PlaceId("pid"),
    /** the address of this place */
    Address("address"),
    /** the aliases/misspellings for this area */
    Alias("alias"),
    /** area name */
    Area("area"),
    /** place begin date */
    Begin("begin"),
    /** disambiguation comment */
    Comment("comment"),
    /** place end date */
    End("end"),
    /** place ended */
    Ended("ended"),
    /** place latitude */
    Latitude("lat"),
    /** place longitude */
    Longitude("long"),
    /** the place name (without accented characters) */
    place("place"),
    /** the place name (with accented characters) */
    PlaceAccent("placeaccent"),
    /** the places type */
    Type("type"),
  }

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

@Suppress("NOTHING_TO_INLINE")
inline fun String.toPlaceMbid() = PlaceMbid(this)
