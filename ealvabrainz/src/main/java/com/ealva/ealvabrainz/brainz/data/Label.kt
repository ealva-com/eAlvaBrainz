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
import kotlinx.parcelize.Parcelize

/**
 * Labels are one of the most complicated and controversial parts of the music industry. The
 * main reason for that being that the term itself is not clearly defined and refers to at least
 * two overlapping concepts: imprints, and the companies that control them. Fortunately, in many
 * cases the imprint and the company controlling it have the same name.
 */
@JsonClass(generateAdapter = true)
public class Label(
  /** The MusicBrainz ID (MBID) for this label */
  public val id: String = "",
  /**
   * The official name of the label.
   *
   * The label name should be represented as found on media sleeves, including use of characters
   * from non latin charsets, stylized characters, etc.
   *
   * If a label is renamed a new label should be created and a label rename relationship created
   * between the two.
   *
   * If there exists multiple slightly different names for the label (eg: The Verve Music Group,
   * Verve Music Group, VMG), you should use the most commonly used name, or the one used on the
   * label's official site.
   *
   * Labels are not always named uniquely, and different labels may share the same label name. To
   * help differentiate between identically named labels, you should use a disambiguation comment
   * and possibly an annotation as well.
   */
  public val name: String = "",
  @field:Json(name = "sort-name") public val sortName: String = "",
  /**: String
   * See the
   * [page about disambiguation comments](https://musicbrainz.org/doc/Disambiguation_Comment)
   * for more information
   */
  public val disambiguation: String = "",
  /**
   * The label code is the "LC" code of the label. This is only the numeric portion; any prefix
   * is not stored. eg LC-0193 is stored as "0193"
   *
   * The Label Code (LC) was introduced in 1977 by the IFPI (International Federation of Phonogram
   * and Videogram Industries) in order to unmistakably identify the different record labels (see
   * Introduction, Record labels) for rights purposes. The Label Code consists historically of 4
   * figures, presently being extended to 5 figures, preceded by LC and a dash (e.g. LC-0193 =
   * Electrola; LC-0233 = His Master's Voice). Note that the number of countries using the LC is
   * limited, and that the code given on the item is not always accurate.
   * [IASA cataloging rules](https://www.iasa-web.org/cataloguing-rules/80-introduction)
   *
   * A label code should not be confused with a release's catalog number. A catalog number
   * identifies a particular release, whereas a label code identifies an entire label.
   */
  @field:Json(name = "label-code") public val labelCode: Int = 0,
  /**
   * Aliases are used to store alternate names or misspellings. For more information and examples,
   * see the page about [aliases](https://musicbrainz.org/doc/Aliases).
   */
  public val aliases: List<Alias> = emptyList(),
  /**
   * See the [page about annotations](https://musicbrainz.org/doc/Annotation) for more information.
   */
  public val annotation: String = "",
  public val area: Area = Area.NullArea,
  /** The country of origin for the label. */
  public val country: String = "",
  /**
   * An IPI (interested party information) code is an identifying number assigned by the CISAC
   * database for musical rights management. See IPI for more information, including how to find
   * these codes.
   */
  public val ipis: List<String> = emptyList(),
  /**
   * The International Standard Name Identifier for the label. See
   * [ISNI](https://musicbrainz.org/doc/Label/Label_Code) for more information.
   */
  public val isnis: List<String> = emptyList(),
  /**
   * The exact meaning of the begin and end dates depends on the type of label. Note that it's
   * usually hard to know if an imprint has folded or is just on hold, and in generally the end
   * date should only be entered if there's a clear indication of its demise.
   *
   * * Begin date: For officially registered trademarks or companies (holdings, distributors), it's
   * the date at which it was registered. For imprints, collection names (when used as labels) and
   * subdivisions (or subsidiaries) for which there is no available creation date, it's the release
   * date of the first release ever issued under that label name. For bootleg companies (more
   * generally for obscure/dubious companies), it's also tolerable to use the release date of the
   * first release, unless more accurate data is available.
   * * End date: For companies (holdings, distributors), it's the date at which the company stopped
   * to exist (be it bankrupted or dismantled). For imprints, collection names (when used as labels)
   * and subdivisions (or subsidiaries) for which there is no available dismantling date, it's the
   * release date of the last release ever issued under that label name. For bootlegs companies (or
   * otherwise obscure/dubious companies), it's also tolerable to use the release date of the last
   * release, unless one has more accurate information.
   */
  @Json(name = "life-span") public val lifeSpan: LifeSpan = LifeSpan.NullLifeSpan,
  @field:FallbackOnNull public val rating: Rating = Rating.NullRating,
  @field:FallbackOnNull @field:Json(name = "user-rating") public val userRating: Rating =
    Rating.NullRating,
  public val releases: List<Release> = emptyList(),
  public val relations: List<Relation> = emptyList(),
  public val tags: List<Tag> = emptyList(),
  @field:Json(name = "user-tags") public val userTags: List<Tag> = emptyList(),
  public val genres: List<Genre> = emptyList(),
  @field:Json(name = "user-genres") public val userGenres: List<Genre> = emptyList(),
  /**
   * The [type](https://musicbrainz.org/doc/Label/Type) describes the main activity of the label.
   *
   * * "imprint": should be used where the label is just a logo (usually either created by a company
   * for a specific product line, or where a former company’s logo is still used on releases after
   * the company was closed or purchased, or both) example: RCA Red Seal
   * * one of the "production" types:
   *     * "original production": should be used for labels producing entirely new releases example:
   *     Riverside Records
   *     * "bootleg production": should be used for known bootlegs labels (as in "not sanctioned by
   *     the rights owner(s) of the released work") example: Charly Records
   *     * "reissue production": should be used for labels specializing in catalog reissues example:
   *     Rhino
   *  * "distributor": should be used for companies mainly distributing other labels production,
   *  often in a specific region of the world example: ZYX, which distributes in Europe most jazz
   *  records in the Concord Music Group catalog.
   * * "holding": should be used for holdings, conglomerates or other financial entities whose main
   * activity is not to produce records, but to manage a large set of recording labels owned by them
   * example: Concord Music Group
   * * "rights society": A rights society is an organization which collects royalties on behalf of
   * the artists. This type is designed to be used with the rights society relationship type rather
   * than as a normal release label. example: GEMA
   */
  public val type: String = "",
  @Json(name = "type-id")
  public val typeId: String = "",
  /** score ranking used in query results */
  public val score: Int = 0
) {
  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as Label

    if (id != other.id) return false

    return true
  }

  override fun hashCode(): Int = id.hashCode()

  override fun toString(): String = toJson()

  public enum class Include(override val value: String) : Inc {
    Releases("releases"),

    DiscIds("discids"),
    Media("media"),
    ArtistCredits("artist-credits"), // include artists credits for all releases and recordings

    Aliases("aliases"),
    Annotation("annotation"),
    Tags("tags"),
    UserTags("user-tags"),
    Ratings("ratings"),
    UserRatings("user-ratings"),
    UserGenres("user-genres"),
    Genres("genres")
  }

  public enum class Browse(override val value: String) : Inc {
    Aliases("aliases"),
    Annotation("annotation"),
    Tags("tags"),
    UserTags("user-tags"),
    Genres("genres"),
    UserGenres("user-genres"),
    Ratings("ratings"),
    UserRatings("user-ratings");
  }

  public enum class SearchField(public override val value: String) : EntitySearchField {
    /**
     * (part of) any [alias](https://musicbrainz.org/doc/Aliases) attached to the label (diacritics
     * are ignored)
     */
    Alias("alias"),

    /** (part of) the name of the label's main associated area */
    Area("area"),

    /** the label's begin date (e.g. "1980-01-22") */
    Begin("begin"),

    /**
     * the [label code](https://musicbrainz.org/doc/Label/Label_Code) for the label (only the
     * numbers, without "LC")
     */
    Code("code"),

    /** (part of) the label's disambiguation comment */
    Comment("comment"),

    /** the 2-letter code (ISO 3166-1 alpha-2) for the label's associated country */
    Country("country"),

    /** Default searches [Alias] and [Label] */
    Default(""),

    /** the label's end date (e.g. "1980-01-22") */
    End("end"),

    /** a boolean flag (true/false) indicating whether or not the label has ended (is dissolved) */
    Ended("ended"),

    /** an IPI code associated with the label */
    Ipi("ipi"),

    /** an ISNI code associated with the label */
    Isni("isni"),

    /** (part of) the label's name (diacritics are ignored) */
    Label("label"),

    /** 	(part of) the label's name (with the specified diacritics) */
    LabelAccent("labelaccent"),

    /** the label's MBID  */
    LabelId("laid"),

    /** the amount of releases related to the label */
    ReleaseCount("release_count"),

//    Labels no longer have sort names
//    SortName("sortname"),

    /** (part of) a tag attached to the label  */
    Tag("tag"),

    /** the label's [type](https://musicbrainz.org/doc/Label/Type) */
    Type("type"),
  }

  public companion object {
    public val NullLabel: Label = Label(id = NullObject.ID, name = NullObject.NAME)
    public val fallbackMapping: Pair<String, Any> = Label::class.java.name to NullLabel
  }
}

public inline val Label.isNullObject: Boolean
  get() = this === Label.NullLabel

@Parcelize
@JvmInline
public value class LabelMbid(override val value: String) : Mbid

public inline val Label.mbid: LabelMbid
  get() = LabelMbid(id)
