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

@file:Suppress("MaxLineLength")

package com.ealva.ealvabrainz.brainz.data

import com.ealva.ealvabrainz.brainz.data.Area.Companion.NullArea
import com.ealva.ealvabrainz.brainz.data.Mbid.Companion.logInvalidMbid
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import timber.log.Timber

/**
 * Areas are geographic regions or settlements. Areas are usually kept in sync with their
 * [Wikidata](https://musicbrainz.org/doc/Wikidata) entries. To request that an area be added to
 * Musicbrainz submit a bug request under the AREQ category. See the list of
 * [current AREQ issues](https://tickets.metabrainz.org/projects/AREQ/issues/AREQ-1840?filter=allopenissues%7Clist)
 * for more information.
 */
@JsonClass(generateAdapter = true)
public class Area(
  /** This area's MusicBrainz ID (MBID) */
  public var id: String = "",
  /** The name of the area */
  public var name: String = "",
  /** Use this field to sort a list of Area */
  @field:Json(name = "sort-name") public var sortName: String = "",
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
  public var type: String = "",
  @Json(name = "type-id")
  public var typeId: String = "",
  /**
   * See the [page about disambiguation comments](https://musicbrainz.org/doc/Disambiguation_Comment)
   * for more information
   */
  public var disambiguation: String = "",
  /**
   * See the [page about annotations](https://musicbrainz.org/doc/Annotation) for more information.
   */
  public var annotation: String = "",
  /** The aliases are used to store alternate names or misspellings. */
  public var aliases: List<Alias> = emptyList(),

  /** The ISO 3166 codes are the codes assigned by ISO to countries and subdivisions. */
  @field:Json(name = "iso-3166-1-codes") public var iso31661Codes: List<String> = emptyList(),
  @field:Json(name = "iso-3166-2-codes") public var iso31662Codes: List<String> = emptyList(),
  @field:Json(name = "iso-3166-3-codes") public var iso31663Codes: List<String> = emptyList(),
  /**
   * The lifespan dates indicate when a certain area was founded and/or ceased to exist.
   * For example, the Soviet Union has a begin date of 1922 and an end date of 1991.
   */
  @Json(name = "life-span")
  public var lifeSpan: LifeSpan = LifeSpan.NullLifeSpan,
  public var relations: List<Relation> = emptyList(),

  public var score: Int = 0
) {

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as Area

    if (id != other.id) return false

    return true
  }

  override fun hashCode(): Int = id.hashCode()

  override fun toString(): String = toJson()

  public interface Lookup : Include

  @Suppress("unused")
  public enum class Misc(override val value: String) : Lookup {
    Aliases("aliases"),
    Annotation("annotation"),
    Tags("tags"),
    Ratings("ratings"),
    Genres("genres")
  }

  @Suppress("unused")
  public enum class SearchField(public val value: String) {
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

  public companion object {
    public val NullArea: Area = Area()
    public val fallbackMapping: Pair<String, Any> = Area::class.java.name to NullArea
  }
}

public inline val Area.isNullObject: Boolean
  get() = this === NullArea

public inline class AreaMbid(override val value: String) : Mbid

public inline val Area.mbid: AreaMbid
  get() = id.toAreaMbid()

@Suppress("NOTHING_TO_INLINE")
public inline fun String.toAreaMbid(): AreaMbid {
  if (logInvalidMbid && isInvalidMbid()) Timber.w("Invalid AreaMbid")
  return AreaMbid(this)
}

/*
Lookup Area
https://musicbrainz.org/ws/2/area/45f07934-675a-46d6-a577-6f8637a411b1?inc=aliases&fmt=json

"discids is not a valid option for the inc parameter for the area unless inc includes: releases"
"media is not a valid option for the inc parameter for the area unless inc includes: releases"
"isrcs is not a valid option for the inc parameter for the area unless inc includes: recordings"
"artist-credits is not a valid option for the inc parameter for the area unless inc includes one of: releases, works, release-groups, recordings"
"various-artists it not a valid option for area"

aliases, annotation, tags, ratings, and genres are OK

All relationships work:
 - area-rels
 - artist-rels
 - event-rels
 - instrument-rels
 - label-rels
 - place-rels
 - recording-rels
 - release-rels
 - release-group-rels
 - series-rels
 - url-rels
 - work-rels

 Browse:

  /ws/2/area              collection

  aliases, annotation, tags, ratings, and genres are OK
 */
