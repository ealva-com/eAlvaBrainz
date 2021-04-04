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
import com.ealva.ealvabrainz.brainz.data.ReleaseGroup.Type.Unrecognized
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.Locale

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
  @field:Json(name = "user-tags") public val userTags: List<Tag> = emptyList(),
  /** Genres associated with the releases in this group */
  public val genres: List<Genre> = emptyList(),
  @field:Json(name = "user-genres") public val userGenres: List<Genre> = emptyList(),
  @field:Json(name = "primary-type") public val primaryType: String = "",
  @field:Json(name = "primary-type-id") public val primaryTypeId: String = "",
  @field:Json(name = "secondary-types") public val secondaryTypes: List<String> = emptyList(),
  @field:Json(name = "secondary-type-ids") public val secondaryTypeIds: List<String> = emptyList(),
  @field:Json(name = "type-id") public val typeId: String = "",
  @field:FallbackOnNull public val rating: Rating = Rating.NullRating,
  @field:FallbackOnNull @field:Json(name = "user-rating") public val userRating: Rating =
    Rating.NullRating,
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
    ArtistCredits("artist-credits"),

    Aliases("aliases"),
    Annotation("annotation"),
    Tags("tags"),
    UserTags("user-tags"),
    Ratings("ratings"),
    UserRatings("user-ratings"),
    Genres("genres"),
    UserGenres("user-genres");

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

  /**
   * Used for any lookup which includes a [ReleaseGroup] or [Release] to limit results to
   * the that particular type of Release.
   */
  public sealed class Type(
    override val value: String,
    public val mbid: ReleaseGroupTypeMbid,
    public val primary: Boolean = false
  ) : Piped {
    public object Album :
      Type("album", ReleaseGroupTypeMbid("f529b476-6e62-324f-b0aa-1f3e33d313fc"), primary = true)

    public object Single :
      Type("single", ReleaseGroupTypeMbid("d6038452-8ee0-3f68-affc-2de9a1ede0b9"), primary = true)

    public object Ep :
      Type("ep", ReleaseGroupTypeMbid("6d0c5bf6-7a33-3420-a519-44fc63eedebf"), primary = true)

    public object Other :
      Type("other", ReleaseGroupTypeMbid("4fc3be2b-de1e-396b-a933-beb8f1607a22"), primary = true)

    public object Broadcast :
      Type(
        "broadcast",
        ReleaseGroupTypeMbid("3b2e49e1-2875-37b8-9fa9-1f7cf3f49900"),
        primary = true
      )

    public object Compilation :
      Type("compilation", ReleaseGroupTypeMbid("dd2a21e1-0c00-3729-a7a0-de60b84eb5d1"))

    public object Soundtrack :
      Type("soundtrack", ReleaseGroupTypeMbid("22a628ad-c082-3c4f-b1b6-d41665107b88"))

    public object SpokenWord :
      Type("spokenword", ReleaseGroupTypeMbid("66b8a13e-43ff-3ac0-ac6c-73659d3817c6"))

    public object Interview :
      Type("interview", ReleaseGroupTypeMbid("12af3f5e-ce2a-3941-8b93-d482884031e5"))

    public object Audiobook :
      Type("audiobook", ReleaseGroupTypeMbid("499a387e-6195-333e-91c0-9592bfec535e"))

    public object Live : Type("live", ReleaseGroupTypeMbid("6fd474e2-6b58-3102-9d17-d6f7eb7da0a0"))
    public object Remix :
      Type("remix", ReleaseGroupTypeMbid("0c60f497-ff81-3818-befd-abfc84a4858b"))

    public object DjMix :
      Type("dj-mix", ReleaseGroupTypeMbid("0d47f47a-3fe5-3d69-ac9d-d566c23968bf"))

    public object MixTapeStreet :
      Type("mixtape/street", ReleaseGroupTypeMbid("15c1b1f5-d893-3375-a1db-e180c5ae15ed"))

    public object Demo : Type("demo", ReleaseGroupTypeMbid("81598169-0d6c-3bce-b4be-866fa658eda3"))
    public object AudioDrama :
      Type("audio drama", ReleaseGroupTypeMbid("0eb547c2-8783-43e4-8f81-751c680e7b04"))

    public class Unrecognized(value: String) : Type(value, ReleaseGroupTypeMbid(""))

    public companion object {
      public fun values(): Array<Type> = arrayOf(
        Album,
        Single,
        Ep,
        Other,
        Broadcast,
        Compilation,
        Soundtrack,
        SpokenWord,
        Interview,
        Audiobook,
        Live,
        Remix,
        DjMix,
        MixTapeStreet,
        Demo,
        AudioDrama,
      )
    }
  }

  public companion object {
    public val NullReleaseGroup: ReleaseGroup =
      ReleaseGroup(id = NullObject.ID, title = NullObject.NAME)
    public val fallbackMapping: Pair<String, Any> =
      ReleaseGroup::class.java.name to NullReleaseGroup

    /**
     * Returns one of the [Type] subtype objects or [Unrecognized]
     */
    public fun stringToType(value: String): Type =
      typeMap.computeIfAbsent(value.trim().toLowerCase(Locale.ROOT)) { key -> Unrecognized(key) }

    private val typeMap: MutableMap<String, Type> = mutableMapOf<String, Type>().apply {
      Type.values().forEach { put(it.value, it) }
      put("mixtape", Type.MixTapeStreet)
    }
  }
}

public inline val ReleaseGroup.isNullObject: Boolean
  get() = this === NullReleaseGroup

@JvmInline
public value class ReleaseGroupMbid(override val value: String) : Mbid

public inline val ReleaseGroup.mbid: ReleaseGroupMbid
  get() = ReleaseGroupMbid(id)

/**
 * Returns one of the [ReleaseGroup.Type] subtype objects, or [ReleaseGroup.Type.Unrecognized] if
 * the type is not found.
 */
public fun String.toReleaseGroupType(): ReleaseGroup.Type =
  ReleaseGroup.stringToType(this)

@JvmInline
public value class ReleaseGroupTypeMbid(override val value: String) : Mbid

public val ReleaseGroupTypeMbid.type: ReleaseGroup.Type
  get() = when (value) {
    ReleaseGroup.Type.Album.value -> ReleaseGroup.Type.Album
    ReleaseGroup.Type.Single.value -> ReleaseGroup.Type.Single
    ReleaseGroup.Type.Ep.value -> ReleaseGroup.Type.Ep
    ReleaseGroup.Type.Other.value -> ReleaseGroup.Type.Other
    ReleaseGroup.Type.Broadcast.value -> ReleaseGroup.Type.Broadcast
    ReleaseGroup.Type.Compilation.value -> ReleaseGroup.Type.Compilation
    ReleaseGroup.Type.Soundtrack.value -> ReleaseGroup.Type.Soundtrack
    ReleaseGroup.Type.SpokenWord.value -> ReleaseGroup.Type.SpokenWord
    ReleaseGroup.Type.Interview.value -> ReleaseGroup.Type.Interview
    ReleaseGroup.Type.Audiobook.value -> ReleaseGroup.Type.Audiobook
    ReleaseGroup.Type.Live.value -> ReleaseGroup.Type.Live
    ReleaseGroup.Type.Remix.value -> ReleaseGroup.Type.Remix
    ReleaseGroup.Type.DjMix.value -> ReleaseGroup.Type.DjMix
    ReleaseGroup.Type.MixTapeStreet.value -> ReleaseGroup.Type.MixTapeStreet
    ReleaseGroup.Type.Demo.value -> ReleaseGroup.Type.Demo
    ReleaseGroup.Type.AudioDrama.value -> ReleaseGroup.Type.AudioDrama
    else -> Unrecognized(value)
  }

public inline val ReleaseGroup.typeMbid: ReleaseGroupTypeMbid
  get() = ReleaseGroupTypeMbid(typeId)

public inline val ReleaseGroup.primaryReleaseGroupType: ReleaseGroup.Type
  get() = primaryType.toReleaseGroupType()

public inline val ReleaseGroup.secondaryReleaseGroupTypes: List<ReleaseGroup.Type>
  get() = secondaryTypes.map { it.toReleaseGroupType() }

public inline val ReleaseGroup.primaryTypeMbid: ReleaseGroupTypeMbid
  get() = ReleaseGroupTypeMbid(primaryTypeId)

public inline val ReleaseGroup.secondaryTypeMbids: List<ReleaseGroupTypeMbid>
  get() = secondaryTypeIds.map { ReleaseGroupTypeMbid(it) }
