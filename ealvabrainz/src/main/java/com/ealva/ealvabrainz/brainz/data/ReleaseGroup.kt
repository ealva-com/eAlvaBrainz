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

import com.ealva.ealvabrainz.brainz.data.ReleaseGroup.Companion.NullReleaseGroup
import com.ealva.ealvabrainz.moshi.FallbackOnNull
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * A release group, just as the name suggests, is used to group several different [Release]s into a
 * single logical entity. Every release belongs to one, and only one release group.
 *
 * Both release groups and releases are "albums" in a general sense, but with an important
 * difference: a release is something you can buy as media such as a CD or a vinyl record,
 * while a release group embraces the overall concept of an album -- it doesn't matter how many
 * CDs or editions/versions it had.
 *
 * When an artist says "We've released our new album", they're talking about a release group.
 * When their publisher says "This new album gets released next week in Japan and next month in
 * Europe", they're referring to the different releases that belong in the above mentioned
 * release group.
 *
 * MusicBrainz automatically considers every release in the database to be part of a release group,
 * even if this group only contains the one release. When a brand new release is added to the
 * database, a new release group is automatically added as well.
 */
@JsonClass(generateAdapter = true)
class ReleaseGroup(
  /** Release Group MusicBrainz ID (MBID) */
  var id: String = "",
  /**
   * The title of a release group is usually very similar, if not the same, as the titles of the
   * releases contained within it.
   */
  var title: String = "",
  var count: Int = 0,
  /**
   * The artist of a release group is usually very similar, if not the same, as the artist of the
   * releases contained within it. Multiple artists can be linked using artist credits.
   */
  @field:Json(name = "artist-credit") var artistCredit: List<ArtistCredit> = emptyList(),
  /** The releases in this Release Group */
  var releases: List<Release> = emptyList(),
  /** Genres associated with the releases in this group */
  var genres: List<Genre> = emptyList(),
  /**
   * See the [page about disambiguation comments](https://musicbrainz.org/doc/Disambiguation_Comment)
   * for more information
   */
  var disambiguation: String = "",
  /**
   * See the [page about annotations](https://musicbrainz.org/doc/Annotation) for more information.
   */
  var annotation: String = "",
  var tags: List<Tag> = emptyList(),
  @field:Json(name = "primary-type") var primaryType: String = "",
  @field:Json(name = "primary-type-id") internal var primaryTypeId: String = "",
  @field:Json(name = "type-id") internal var typeId: String = "",
  @field:FallbackOnNull var rating: Rating = Rating.NullRating,
  @field:Json(name = "secondary-type-ids") var secondaryTypeIds: List<String> = emptyList(),
  @field:Json(name = "secondary-types") var secondaryTypes: List<String> = emptyList(),
  @field:Json(name = "first-release-date") var firstReleaseDate: String = "",
  var aliases: List<Alias> = emptyList(),
  /** Only used in search results */
  var score: Int = 0
) {
  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as ReleaseGroup

    if (id != other.id) return false

    return true
  }

  override fun hashCode() = id.hashCode()

  override fun toString() = toJson()


  interface Lookup : Include

  @Suppress("unused")
  enum class Subqueries(override val value: String) : Lookup {
    Artists("artists"),
    Releases("releases"),
    /** An ID calculated from the TOC of a CD */
    DiscIds("discids"),             // include discids for all media in the releases
    Media("media"),                 // include media for all releases, this includes the # of tracks on each medium and its format.
    ArtistCredits("artist-credits"); // include artists credits for all releases and recordings
  }

  @Suppress("unused")
  enum class Misc(override val value: String) : Lookup {
    Aliases("aliases"),       // include artist, label, area or work aliases; treat these as a set, as they are not deliberately ordered
    Annotation("annotation"),
    Tags("tags"),
    Ratings("ratings"),
    Genres("genres")
  }

  @Suppress("unused")
  enum class SearchFields(val value: String) {
    /** MBID of the release group’s artist */
    ArtistId("arid"),

    /** release group artist as it appears on the cover (Artist Credit)  */
    Artist("artist"),

    /** “real name” of any artist that is included in the release group’s artist credit  */
    ArtistName("artistname"),

    /** release group comment to differentiate similar release groups */
    Comment("comment"),

    /** name of any artist in multi-artist credits, as it appears on the cover */
    CreditName("creditname"),

    /** primary type of the release group (album, single, ep, other) */
    PrimaryType("primarytype"),

    /** MBID of the release group */
    ReleaseGroupId("rgid"),

    /** release group name */
    ReleaseGroup("releasegroup"),

    /** name of the releasegroup with any accent characters retained */
    ReleaseGroupAccentedName("releasegroupaccent"),

    /** number of releases in this release group  */
    Releases("releases"),

    /** name of a release that appears in the release group */
    Release("release"),

    /** MBID of a release that appears in the release group  */
    ReleaseId("reid"),

    /**
     * secondary type of the release group (audiobook, compilation, interview, live, remix,
     * soundtrack, spokenword)
     */
    SecondaryType("secondarytype"),

    /** status of a release that appears within the release group */
    Status("status"),

    /** a tag that appears on the release group */
    Tag("tag"),

    /**
     * type of the release group, old type mapping for when we did not have separate primary
     * and secondary types
     */
    Type("type"),
  }

  companion object {
    val NullReleaseGroup = ReleaseGroup(id = NullObject.ID, title = NullObject.NAME)
    val fallbackMapping: Pair<String, Any> = ReleaseGroup::class.java.name to NullReleaseGroup
  }
}

inline val ReleaseGroup.isNullObject
  get() = this === NullReleaseGroup

inline class ReleaseGroupMbid(override val value: String) : Mbid

inline val ReleaseGroup.mbid: ReleaseGroupMbid
  get() = ReleaseGroupMbid(id)

/**
 * Appears as primary-type-id in some places and type-id in others. Handle like this for now. Moshi
 * should have aliases, but they recommend something similar this.
 *
 * [Moshi Issue](https://github.com/square/moshi/issues/1012)
 */
val ReleaseGroup.thePrimaryTypeId: String
  get() = if (primaryTypeId.isNotEmpty()) primaryTypeId else typeId
