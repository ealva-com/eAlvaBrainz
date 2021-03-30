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

@file:Suppress("unused")

package com.ealva.ealvabrainz.brainz.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Result from artist mbid lookup
 *
 * [https://musicbrainz.org/doc/Artist]
 */
@JsonClass(generateAdapter = true)
public class Artist(
  /** The MBID for this artist */
  public val id: String = "",
  /**
   * The type is used to state whether an artist is a person, a group, or something else.
   * * Person
   * This indicates an individual person.
   * * Group
   * This indicates a group of people that may or may not have a distinctive name.
   * * Orchestra
   * This indicates an orchestra (a large instrumental ensemble).
   * * Choir
   * This indicates a choir/chorus (a large vocal ensemble).
   * * Character
   * This indicates an individual fictional character.
   * * Other
   * Anything which does not fit into the above categories.
   *
   * Note that not every ensemble related to classical music is an orchestra or choir.
   */
  public val type: String = "",
  /** The official name of an artist, be it a person or a band */
  public val name: String = "",
  /**
   * The sort name is a variant of the artist name which would be used when sorting artists by
   * name, such as in record shops or libraries. Among other things, sort names help to ensure
   * that all the artists that start with Navigation"The" don't end up up under "T".
   */
  @field:Json(name = "sort-name") public val sortName: String = "",
  public val country: String = "",
  @field:Json(name = "begin-area") @field:FallbackOnNull public val beginArea: Area = Area.NullArea,
  @field:Json(name = "end-area") @field:FallbackOnNull public val endArea: Area = Area.NullArea,
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
  /**
   * For an Artist lifespan, begin and end dates indicate when an artist started and finished its
   * existence. Its exact meaning depends on the type of artist:
   *
   * * For a person
   * Begin date represents date of birth, and end date represents date of death.
   *
   * * For a group (or orchestra/choir)
   * Begin date represents the date when the group first formed: if a group dissolved and then
   * reunited, the date is still that of when they first formed. End date represents the date when
   * the group last dissolved: if a group dissolved and then reunited, the date is that of when
   * they last dissolved (if they are together, it should be blank!). For listing other inactivity
   * periods, just use the annotation and the "member of" relationships.
   *
   * * For a character
   * Begin date represents the date (in real life) when the character concept was created. The End
   * date should not be set, since new media featuring a character can be created at any time. In
   * particular, the Begin and End date fields should not be used to hold the fictional birth or
   * death dates of a character. (This information can be put in the annotation.)
   *
   * * For others
   * There are no clear indications about how to use dates for artists of the type Other at the
   * moment.
   */
  @field:Json(name = "life-span") @field:FallbackOnNull public val lifeSpan: LifeSpan =
    LifeSpan.NullLifeSpan,
  /**
   * The gender is used to explicitly state whether a person or character identifies as male,
   * female or neither. Groups do not have genders.
   */
  public val gender: String = "",
  @field:Json(name = "gender-id") public val genderId: String = "",
  @field:Json(name = "type-id") public val typeId: String = "",
  /**
   * The artist area, as the name suggests, indicates the area with which an artist is primarily
   * identified with. It is often, but not always, its birth/formation country.
   */
  @field:FallbackOnNull public val area: Area = Area.NullArea,
  public val genres: List<Genre> = emptyList(),
  /**
   * Aliases are used to store alternate names or misspellings. For more information and examples,
   * see the [page about aliases](https://musicbrainz.org/doc/Aliases).
   */
  public val aliases: List<Alias> = emptyList(),
  /**
   * An IPI (interested party information) code is an identifying number assigned by the CISAC
   * database for musical rights management. See [IPI](https://musicbrainz.org/doc/IPI) for more
   * information, including how to find these codes.
   */
  public val ipis: List<String> = emptyList(),
  /**
   * The International Standard Name Identifier for the artist. See
   * [ISNI](https://musicbrainz.org/doc/ISNI) for more information.
   */
  public val isnis: List<String> = emptyList(),

  @field:FallbackOnNull public val rating: Rating = Rating.NullRating,
  public val tags: List<Tag> = emptyList(),
  @field:Json(name = "release-groups") public val releaseGroups: List<ReleaseGroup> = emptyList(),
  public val recordings: List<Recording> = emptyList(),
  public val releases: List<Release> = emptyList(),
  public val relations: List<Relation> = emptyList(),
  /** score ranking used in query results */
  public val score: Int = 0
) {
  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as Artist

    if (id != other.id) return false

    return true
  }

  override fun hashCode(): Int = id.hashCode()

  override fun toString(): String = toJson()

  public enum class Include(override val value: String) : Inc {
    Recordings("recordings"),
    Releases("releases"),
    ReleaseGroups("release-groups"),
    Works("works"),

    /** An ID calculated from the TOC of a CD */
    DiscIds("discids"),
    Media("media"),

    /**
     * The International Standard Recording Code, an identification system for audio and music
     * video recordings. Includes isrcs for all recordings
     */
    Isrcs("isrcs"),
    ArtistCredits("artist-credits"), // include artists credits for all releases and recordings
    VariousArtists("various-artists"),
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
    /** (part of) any alias attached to the artist (diacritics are ignored) */
    Alias("alias"),

    /** (part of) any primary alias attached to the artist (diacritics are ignored) */
    PrimaryAlias("primary_alias"),

    /** (part of) the name of the artist's main associated area */
    Area("area"),

    /** the artist's MBID */
    ArtistId("arid"),

    /** (part of) the artist's name (diacritics are ignored) */
    Artist("artist"),

    /** (part of) the artist's name (with the specified diacritics) */
    ArtistAccent("artistaccent"),

    /** the artist's begin date (e.g. "1980-01-22")  */
    Begin("begin"),

    /** (part of) the name of the artist's begin area */
    BeginArea("beginarea"),

    /** (part of) the artist's disambiguation comment  */
    Comment("comment"),

    /**
     * the 2-letter code (ISO 3166-1 alpha-2) for the artist's main associated country, or “unknown”
     */
    Country("country"),

    /** Default searches [Alias], [Artist], and [SortName] */
    Default(""),

    /** the artist's end date (e.g. "1980-01-22")  */
    End("end"),

    /** 	(part of) the name of the artist's end area  */
    EndArea("endarea"),

    /**
     * 	a boolean flag (true/false) indicating whether or not the artist has ended (is
     * dissolved/deceased)
     */
    Ended("ended"),

    /** the artist's gender (“male”, “female”, “other” or “not applicable”)  */
    Gender("gender"),

    /** an IPI code associated with the artist */
    Ipi("ipi"),

    /** an ISNI code associated with the artist */
    Isni("isni"),

    /**
     * (part of) the artist's [sort name](https://musicbrainz.org/doc/Artist#Sort_name)
     *
     * Sort name [style](https://musicbrainz.org/doc/Style/Artist/Sort_Name)
     */
    SortName("sortname"),

    /** (part of) a tag attached to the artist */
    Tag("tag"),

    /** the artist's [type](https://musicbrainz.org/doc/Artist#Type) (“person”, “group”, etc.) */
    Type("type")
  }

  public companion object {
    public val NullArtist: Artist = Artist(id = NullObject.ID, name = NullObject.NAME)
    public val fallbackMapping: Pair<String, Any> = Artist::class.java.name to NullArtist
  }
}

public inline val Artist.isNullObject: Boolean
  get() = this === Artist.NullArtist
