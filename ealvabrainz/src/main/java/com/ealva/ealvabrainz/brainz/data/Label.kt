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
 * Labels are one of the most complicated and controversial parts of the music industry. The
 * main reason for that being that the term itself is not clearly defined and refers to at least
 * two overlapping concepts: imprints, and the companies that control them. Fortunately, in many
 * cases the imprint and the company controlling it have the same name.
 */
@JsonClass(generateAdapter = true)
class Label(
  /** The MusicBrainz ID (MBID) for this label */
  var id: String = "",
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
  var name: String = "",
  @field:Json(name = "sort-name") var sortName: String = "",
  /**: String
   * See the [page about disambiguation comments](https://musicbrainz.org/doc/Disambiguation_Comment)
   * for more information
   */
  var disambiguation: String = "",
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
  @field:Json(name = "label-code") var labelCode: Long = 0,
  /**
   * Aliases are used to store alternate names or misspellings. For more information and examples,
   * see the page about [aliases](https://musicbrainz.org/doc/Aliases).
   */
  var aliases: List<Alias> = emptyList(),
  /**
   * See the [page about annotations](https://musicbrainz.org/doc/Annotation) for more information.
   */
  var annotation: String = "",
  var area: Area = Area.NullArea,
  /** The country of origin for the label. */
  var country: String = "",
  var genres: List<Genre> = emptyList(),
  /**
   * An IPI (interested party information) code is an identifying number assigned by the CISAC
   * database for musical rights management. See IPI for more information, including how to find
   * these codes.
   */
  var ipis: List<String> = emptyList(),
  /**
   * The International Standard Name Identifier for the label. See
   * [ISNI](https://musicbrainz.org/doc/Label/Label_Code) for more information.
   */
  var isnis: List<String> = emptyList(),
  /**
   * The exact meaning of the begin and end dates depends on the type of label. Note that it's
   * usually hard to know if an imprint has folded or is just on hold, and in generally the end
   * date should only be entered if there's a clear indication of its demise.
   *
   * * Begin date
   * For officially registered trademarks or companies (holdings, distributors), it's the date at
   * which it was registered. For imprints, collection names (when used as labels) and subdivisions
   * (or subsidiaries) for which there is no available creation date, it's the release date of the
   * first release ever issued under that label name. For bootleg companies (more generally for
   * obscure/dubious companies), it's also tolerable to use the release date of the first release,
   * unless more accurate data is available.
   * * End date
   * For companies (holdings, distributors), it's the date at which the company stopped to exist
   * (be it bankrupted or dismantled). For imprints, collection names (when used as labels) and
   * subdivisions (or subsidiaries) for which there is no available dismantling date, it's the
   * release date of the last release ever issued under that label name. For bootlegs companies
   * (or otherwise obscure/dubious companies), it's also tolerable to use the release date of the
   * last release, unless one has more accurate information.
   */
  @Json(name = "life-span") var lifeSpan: LifeSpan = LifeSpan.NullLifeSpan,
  var rating: Rating = Rating.NullRating,
  var releases: List<Release> = emptyList(),
  var tags: List<Tag> = emptyList(),
  /**
   * The [type](https://musicbrainz.org/doc/Label/Type) describes the main activity of the label.
   */
  var type: String = "",
  @Json(name = "type-id")
  var typeId: String = "",
  /** Used when querying Labels */
  var count: Int = 0
) {
  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as Label

    if (id != other.id) return false

    return true
  }

  override fun hashCode() = id.hashCode()

  override fun toString() = toJson()

  interface Lookup : Include

  @Suppress("unused")
  enum class Subquery(override val value: String) : Lookup {
    Releases("releases"),
  }

  @Suppress("unused")
  enum class Misc(override val value: String) : Lookup {
    /**
     * include artist, label, area or work aliases; treat these as a set, as they are not
     * deliberately ordered
     */
    Aliases("aliases"),
    Annotation("annotation"),
    Tags("tags"),
    Ratings("ratings"),
    Genres("genres")
  }

  /**
   * Label relatoinships
   *
   * * [Label-Label](https://musicbrainz.org/relationships/label-label)
   * * [Label-Recording](https://musicbrainz.org/relationships/label-recording)
   * * [Label-Release](https://musicbrainz.org/relationships/label-release)
   * * [Label-ReleaseGroup](https://musicbrainz.org/relationships/label-release_group)
   * * [Label-Series](https://musicbrainz.org/relationships/label-series)
   * * [Label-URL](https://musicbrainz.org/relationships/label-url)
   * * [Label-Work](https://musicbrainz.org/relationships/label-work)
   */
  enum class Relations(override val value: String) : Lookup {
    Label("label-rels"),
    Recording("recording-rels"),
    Release("release-rels"),
    ReleaseGroup("release-group-rels"),
    Series("series-rels"),
    Url("url-rels"),
    Work("work-rels")
  }

  @Suppress("unused")
  enum class SearchField(val value: String) {
   /** the aliases/misspellings for this label */
   Alias("alias"),
   /** label area */
   Area("area"),
   /** label founding date */
   Begin("begin"),
   /** label code (only the figures part, i.e. without "LC") */
   Code("code"),
   /** label comment to differentiate similar labels */
   Comment("comment"),
   /** The two letter country code of the label country */
   Country("country"),
   /** label dissolution date */
   End("end"),
   /** true if know ended even if do not know end date */
   Ended("ended"),
   /** ipi */
   Ipi("ipi"),
   /** label name */
   Label("label"),
   /** name of the label with any accent characters retained */
   LabelAccent("labelaccent"),
   /** MBID of the label */
   LabelId("laid"),
   /** label sortname */
   SortName("sortname"),
   /** label type */
   Type("type"),
   /** folksonomy tag */
   Tag("tag"),
  }

  companion object {
    val NullLabel = Label(id = NullObject.ID, name = NullObject.NAME)
    val fallbackMapping: Pair<String, Any> = Label::class.java.name to NullLabel
  }
}

inline val Label.isNullObject
  get() = this === Label.NullLabel

inline class LabelMbid(override val value: String) : Mbid

inline val Label.mbid
  get() = LabelMbid(id)

@Suppress("NOTHING_TO_INLINE")
inline fun String.toLabelMbid() = LabelMbid(this)
