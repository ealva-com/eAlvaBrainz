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

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

/**
 * Areas are geographic regions or settlements. Areas are usually kept in sync with their
 * [Wikidata](https://musicbrainz.org/doc/Wikidata) entries. To request that an area be added to
 * Musicbrainz submit a bug request under the AREQ category. See the list of
 * [current AREQ issues](https://tickets.metabrainz.org/browse/AREQ)
 * for more information.
 */
@JsonClass(generateAdapter = true)
public class Area(
  /** This area's MusicBrainz ID (MBID) */
  public val id: String = "",
  /** The name of the area */
  public val name: String = "",
  /** Use this field to sort a list of Area */
  @field:Json(name = "sort-name") public val sortName: String = "",
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
  public val type: String = "",
  @Json(name = "type-id")
  public val typeId: String = "",
  /**
   * See the [page about disambiguation comments](https://musicbrainz.org/doc/Disambiguation_Comment)
   * for more information
   */
  public val disambiguation: String = "",
  /**
   * See the [page about annotations](https://musicbrainz.org/doc/Annotation) for more information.
   */
  public val annotation: String = "",
  /** The aliases are used to store alternate names or misspellings. */
  public val aliases: List<Alias> = emptyList(),

  public val tags: List<Tag> = emptyList(),
  @field:Json(name = "user-tags") public val userTags: List<Tag> = emptyList(),
  public val genres: List<Genre> = emptyList(),
  @field:Json(name = "user-genres") public val userGenres: List<Genre> = emptyList(),

  /** The ISO 3166 codes are the codes assigned by ISO to countries and subdivisions. */
  @field:Json(name = "iso-3166-1-codes") public val iso31661Codes: List<String> = emptyList(),
  @field:Json(name = "iso-3166-2-codes") public val iso31662Codes: List<String> = emptyList(),
  @field:Json(name = "iso-3166-3-codes") public val iso31663Codes: List<String> = emptyList(),
  /**
   * The lifespan dates indicate when a certain area was founded and/or ceased to exist.
   * For example, the Soviet Union has a begin date of 1922 and an end date of 1991.
   */
  @Json(name = "life-span")
  public val lifeSpan: LifeSpan = LifeSpan.NullLifeSpan,
  public val relations: List<Relation> = emptyList(),

  public val score: Int = 0
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

  public enum class Include(override val value: String) : Inc {
    Aliases("aliases"),
    Annotation("annotation"),
    Tags("tags"),
    UserTags("user-tags"),
    Ratings("ratings"),
    UserRatings("user-ratings"),
    Genres("genres"),
    UserGenres("user-genres")
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
    /** (part of) any alias attached to the artist (diacritics are ignored) */
    Alias("alias"),

    /** the area's MBID */
    AreaId("aid"),

    /** (part of) the area's name (diacritics are ignored) */
    AreaName("area"),

    /** (part of) the area's name (with the specified diacritics) */
    AreaAccent("areaaccent"),

    /** the area's begin date (e.g. "1980-01-22") */
    Begin("begin"),

    /** (part of) the area's disambiguation comment */
    Comment("comment"),

    /** Default searches the [AreaName] */
    Default(""),

    /** the area's end date (e.g. "1980-01-22") */
    End("end"),

    /**
     * A boolean flag (true/false) indicating whether or not the area has ended (is no longer
     * current)
     */
    Ended("ended"),

    /**
     * An [ISO 3166-1, 3166-2 or 3166-3](https://en.wikipedia.org/wiki/ISO_3166) code attached to
     * the area
     */
    Iso("iso"),

//    /** an ISO 3166-1 code attached to the area */
//    Iso1("iso1"),
//
//    /** an ISO 3166-2 code attached to the area */
//    Iso2("iso2"),
//
//    /** an ISO 3166-3 code attached to the area */
//    Iso3("iso3"),
//
//    Area's no longer have sort names
//    SortName("sortname"),

    /** a tag attached to the area */
    Tag("tag"),

    /** the area's [type](https://musicbrainz.org/doc/Area#Type)  */
    Type("type"),
  }

  public companion object {
    public val NullArea: Area = Area(name = NullObject.NAME)
    public val fallbackMapping: Pair<String, Any> = Area::class.java.name to NullArea
  }
}

public inline val Area.isNullObject: Boolean
  get() = this === Area.NullArea

@Parcelize
@JvmInline
public value class AreaMbid(override val value: String) : Mbid

public inline val Area.mbid: AreaMbid
  get() = AreaMbid(id)
