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

import com.ealva.ealvabrainz.brainz.data.Area.Companion.NullArea
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Areas are geographic regions or settlements. Areas are usually kept in sync with their
 * [Wikidata](https://musicbrainz.org/doc/Wikidata) entries. To request that an area be added to
 * Musicbrainz submit a bug request under the AREQ category. See the list of
 * [current AREQ issues](https://tickets.metabrainz.org/projects/AREQ/issues/AREQ-1840?filter=allopenissues%7Clist)
 * for more information.
 */
@JsonClass(generateAdapter = true)
class Area(
  /** This area's MusicBrainz ID (MBID) */
  var id: String = "",
  /** The name of the area */
  var name: String = "",
  /** Use this field to sort a list of Area */
  @field:Json(name = "sort-name") var sortName: String = "",
  /**
   * The type of area. Possible values are:
   * * **Country**
   * Country is used for areas included (or previously included) in ISO 3166-1, e.g. United States.
   * * **Subdivision**
   * Subdivision is used for the main administrative divisions of a country, e.g. California,
   * Ontario, Okinawa. These are considered when displaying the parent areas for a given area.
   * * **County**
   * County is used for smaller administrative divisions of a country which are not the main
   * administrative divisions but are also not municipalities, e.g. counties in the USA. These are
   * not considered when displaying the parent areas for a given area.
   * * **Municipality**
   * Municipality is used for small administrative divisions which, for urban municipalities, often
   * contain a single city and a few surrounding villages. Rural municipalities typically group
   * several villages together.
   * * **City**
   * City is used for settlements of any size, including towns and villages.
   * * **District**
   * District is used for a division of a large city, e.g. Queens.
   * * **Island**
   * Island is used for islands and atolls which don't form subdivisions of their own, e.g. Skye.
   * These are not considered when displaying the parent areas for a given area.
   */
  var type: String = "",
  @Json(name = "type-id")
  var typeId: String = "",
  /**
   * See the [page about disambiguation comments](https://musicbrainz.org/doc/Disambiguation_Comment)
   * for more information
   */
  var disambiguation: String = "",
  /**
   * See the [page about annotations](https://musicbrainz.org/doc/Annotation) for more information.
   */
  var annotation: String = "",
  /** The aliases are used to store alternate names or misspellings. */
  var aliases: List<Alias> = emptyList(),

  /** The ISO 3166 codes are the codes assigned by ISO to countries and subdivisions. */
  @field:Json(name = "iso-3166-1-codes") var iso31661Codes: List<String> = emptyList(),
  @field:Json(name = "iso-3166-2-codes") var iso31662Codes: List<String> = emptyList(),
  @field:Json(name = "iso-3166-3-codes") var iso31663Codes: List<String> = emptyList(),
  /**
   * The lifespan dates indicate when a certain area was founded and/or ceased to exist.
   * For example, the Soviet Union has a begin date of 1922 and an end date of 1991.
   */
  @Json(name = "life-span")
  var lifeSpan: LifeSpan = LifeSpan.NullLifeSpan,
  var score: Int = 0
) {

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as Area

    if (id != other.id) return false

    return true
  }

  override fun hashCode() = id.hashCode()

  override fun toString() = toJson()

  interface Lookup : Include

  @Suppress("unused")
  enum class Misc(override val value: String) : Lookup {
    Aliases("aliases"),
    Annotation("annotation"),
    Tags("tags"),
    Ratings("ratings"),
    Genres("genres")
  }

  @Suppress("unused")
  enum class SearchField(val value: String) {
    /** the area's MBID */
    AreaId("aid"),
    /** an alias attached to the area */
    Alias("alias"),
    /** the area's name */
    AreaName("area"),
    /** the area's begin date */
    BeginDate("begin"),
    /** the area's disambiguation comment */
    Comment("comment"),
    /** the area's end date */
    EndDate("end"),
    /** a flag indicating whether or not the area has ended */
    Ended("ended"),
    /** an ISO 3166-1/2/3 code attached to the area */
    Iso("iso"),
    /** an ISO 3166-1 code attached to the area */
    Iso1("iso1"),
    /** an ISO 3166-2 code attached to the area */
    Iso2("iso2"),
    /** an ISO 3166-3 code attached to the area */
    Iso3("iso3"),
    /** the area's sort name */
    SortName("sortname"),
    /** the area's type  */
    Type("type"),
  }

  /**
   * Area relationships
   *
   * * [Area-Area](https://musicbrainz.org/relationships/area-area)
   * * [Area-Event](https://musicbrainz.org/relationships/area-event)
   * * [Area-Instrument](https://musicbrainz.org/relationships/area-instrument)
   * * [Area-Recording](https://musicbrainz.org/relationships/area-recording)
   * * [Area-Release](https://musicbrainz.org/relationships/area-release)
   * * [Area-URL](https://musicbrainz.org/relationships/area-url)
   * * [Area-Work](https://musicbrainz.org/relationships/area-work)
   */
  @Suppress("unused")
  enum class Relations(override val value: String) : Lookup {
    Area("area-rels"),
    Event("event-rels"),
    Instrument("instrument-rels"),
    Recording("recording-rels"),
    Release("release-rels"),
    Url("url-rels"),
    Work("work-rels")
  }

  companion object {
    val NullArea = Area()
    val fallbackMapping: Pair<String, Any> = Area::class.java.name to NullArea
  }

}

inline val Area.isNullObject
  get() = this === NullArea

inline class AreaMbid(override val value: String) : Mbid

inline val Area.mbid
  get() = AreaMbid(id)

@Suppress("NOTHING_TO_INLINE")
inline fun String.toAreaMbid() = AreaMbid(this)
