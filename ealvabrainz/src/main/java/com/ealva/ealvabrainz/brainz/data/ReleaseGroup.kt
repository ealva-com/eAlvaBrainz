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
public class ReleaseGroup(
  /** Release Group MusicBrainz ID (MBID) */
  public val id: String = "",
  /**
   * The title of a release group is usually very similar, if not the same, as the titles of the
   * releases contained within it.
   */
  public val title: String = "",
  public val count: Int = 0,
  /**
   * The artist of a release group is usually very similar, if not the same, as the artist of the
   * releases contained within it. Multiple artists can be linked using artist credits.
   */
  @field:Json(name = "artist-credit") public val artistCredit: List<ArtistCredit> = emptyList(),
  /** The releases in this Release Group */
  public val releases: List<Release> = emptyList(),
  /** Genres associated with the releases in this group */
  public val genres: List<Genre> = emptyList(),
  /**
   * See the
   * [page about disambiguation comments](https://musicbrainz.org/doc/Disambiguation_Comment)
   * for more information
   */
  public val disambiguation: String = "",
  /**
   * See the [page about annotations](https://musicbrainz.org/doc/Annotation) for more information.
   */
  public val annotation: String = "",
  public val tags: List<Tag> = emptyList(),
  /**
   * Types may be
   * * "nat"
   * * "album"
   * * "single"
   * * "ep"
   * * "compilation"
   * * "soundtrack"
   * * "spokenword"
   * * "interview"
   * * "audiobook"
   * * "live"
   * * "remix"
   * * "other"
   */
  @field:Json(name = "primary-type") public val primaryType: String = "",
  @field:Json(name = "primary-type-id") public val primaryTypeId: String = "",
  @field:Json(name = "type-id") public val typeId: String = "",
  @field:FallbackOnNull public val rating: Rating = Rating.NullRating,
  @field:Json(name = "secondary-type-ids") public val secondaryTypeIds: List<String> = emptyList(),
  @field:Json(name = "secondary-types") public val secondaryTypes: List<String> = emptyList(),
  @field:Json(name = "first-release-date") public val firstReleaseDate: String = "",
  public val aliases: List<Alias> = emptyList(),
  public val relations: List<Relation> = emptyList(),
  /** score ranking used in query results */
  public val score: Int = 0
) {
  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as ReleaseGroup

    if (id != other.id) return false

    return true
  }

  override fun hashCode(): Int = id.hashCode()

  override fun toString(): String = toJson()

  public enum class Include(override val value: String) : Inc {
    Artists("artists"),
    Releases("releases"),

    /** An ID calculated from the TOC of a CD */
    DiscIds("discids"),
    Media("media"),
    ArtistCredits("artist-credits"), // include artists credits for all releases and recordings
    Aliases("aliases"),
    Annotation("annotation"),
    Tags("tags"),
    Ratings("ratings"),
    Genres("genres");

    public companion object {
      /** Doesn't create a values() array and/or list every time */
      public val all: List<Include> by lazy { values().asList() }
    }
  }

  public enum class Browse(override val value: String) : Inc {
    ArtistCredits("artist-credits"),
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
     * (part of) any [alias](https://musicbrainz.org/doc/Aliases) attached to the release group
     * (diacritics are ignored)
     */
    Alias("alias"),

    /** MBID of the release group’s artist */
    ArtistId("arid"),

    /**
     * (part of) the combined credited artist name for the release group, including join phrases
     * (e.g. "Artist X feat.")
     */
    Artist("artist"),

    /** (part of) the name of any of the release group artists   */
    ArtistName("artistname"),

    /** (part of) the release group's disambiguation comment */
    Comment("comment"),

    /**
     * (part of) the credited name of any of the release group artists on this particular release
     * group
     */
    CreditName("creditname"),

    /** Default searches [ReleaseGroup] */
    Default(""),

    /** the release date of the earliest release in this release group (e.g. "1980-01-22") */
    FirstReleaseDate("firstreleasedate"),

    /**
     * [primary type](https://musicbrainz.org/doc/Release_Group/Type#Primary_types) of the release
     * group
     */
    PrimaryType("primarytype"),

    /** the MBID of any of the releases in the release group   */
    ReleaseId("reid"),

    /** (part of) the title of any of the releases in the release group */
    Release("release"),

    /**	(part of) the release group's title (diacritics are ignored) */
    ReleaseGroup("releasegroup"),

    /** (part of) the release group's title (with the specified diacritics) */
    ReleaseGroupAccentedName("releasegroupaccent"),

    /** number of releases in this release group  */
    Releases("releases"),

    /** the release group's MBID  */
    ReleaseGroupId("rgid"),

    /**
     * any of the [secondary types](https://musicbrainz.org/doc/Release_Group/Type#Secondary_types)
     * of the release group
     */
    SecondaryType("secondarytype"),

    /**
     * the [status][com.ealva.ealvabrainz.brainz.data.Release.Status] of any of the releases in the
     * release group
     */
    Status("status"),

    /** (part of) a tag attached to the release group */
    Tag("tag")
  }

  public companion object {
    public val NullReleaseGroup: ReleaseGroup =
      ReleaseGroup(id = NullObject.ID, title = NullObject.NAME)
    public val fallbackMapping: Pair<String, Any> =
      ReleaseGroup::class.java.name to NullReleaseGroup
  }
}

public inline val ReleaseGroup.isNullObject: Boolean
  get() = this === NullReleaseGroup

/**
 * Appears as primary-type-id in some places and type-id in others. Handle like this for now. Moshi
 * should have aliases, but they recommend something similar this.
 *
 * [Moshi Issue](https://github.com/square/moshi/issues/1012)
 */
public val ReleaseGroup.thePrimaryTypeId: String
  get() = if (primaryTypeId.isNotEmpty()) primaryTypeId else typeId
