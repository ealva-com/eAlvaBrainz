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

import com.ealva.ealvabrainz.brainz.data.Artist.Companion.NullArtist
import com.ealva.ealvabrainz.moshi.FallbackOnNull
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import timber.log.Timber

/**
 * Result from artist mbid lookup
 *
 * [https://musicbrainz.org/doc/Artist]
 */
@JsonClass(generateAdapter = true)
public class Artist(
  /** The MBID for this artist */
  public var id: String = "",
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
  public var type: String = "",
  /** The official name of an artist, be it a person or a band */
  public var name: String = "",
  /**
   * The sort name is a variant of the artist name which would be used when sorting artists by
   * name, such as in record shops or libraries. Among other things, sort names help to ensure
   * that all the artists that start with Navigation"The" don't end up up under "T".
   */
  @field:Json(name = "sort-name") public var sortName: String = "",
  public var country: String = "",
  @field:Json(name = "begin-area") @field:FallbackOnNull public var beginArea: Area = Area.NullArea,
  @field:Json(name = "end-area") @field:FallbackOnNull public var endArea: Area = Area.NullArea,
  /**
   * See the
   * [page about disambiguation comments](https://musicbrainz.org/doc/Disambiguation_Comment)
   * for more information
   */
  public var disambiguation: String = "",
  /**
   * See the [page about annotations](https://musicbrainz.org/doc/Annotation) for more information.
   */
  public var annotation: String = "",
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
  @field:Json(name = "life-span") @field:FallbackOnNull public var lifeSpan: LifeSpan =
    LifeSpan.NullLifeSpan,
  /**
   * The gender is used to explicitly state whether a person or character identifies as male,
   * female or neither. Groups do not have genders.
   */
  public var gender: String = "",
  @field:Json(name = "gender-id") public var genderId: String = "",
  @field:Json(name = "type-id") public var typeId: String = "",
  /**
   * The artist area, as the name suggests, indicates the area with which an artist is primarily
   * identified with. It is often, but not always, its birth/formation country.
   */
  @field:FallbackOnNull public var area: Area = Area.NullArea,
  public var genres: List<Genre> = emptyList(),
  /**
   * Aliases are used to store alternate names or misspellings. For more information and examples,
   * see the [page about aliases](https://musicbrainz.org/doc/Aliases).
   */
  public var aliases: List<Alias> = emptyList(),
  /**
   * An IPI (interested party information) code is an identifying number assigned by the CISAC
   * database for musical rights management. See [IPI](https://musicbrainz.org/doc/IPI) for more
   * information, including how to find these codes.
   */
  public var ipis: List<String> = emptyList(),
  /**
   * The International Standard Name Identifier for the artist. See
   * [ISNI](https://musicbrainz.org/doc/ISNI) for more information.
   */
  public var isnis: List<String> = emptyList(),

  @field:FallbackOnNull public var rating: Rating = Rating.NullRating,
  public var tags: List<Tag> = emptyList(),
  @field:Json(name = "release-groups") public var releaseGroups: List<ReleaseGroup> = emptyList(),
  public var recordings: List<Recording> = emptyList(),
  public var releases: List<Release> = emptyList(),
  public var relations: List<Relation> = emptyList(),
  /** score only used in query results */
  public var score: Int = 0
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

  public interface Lookup : Include

  @Suppress("unused")
  public enum class Subquery(override val value: String) : Lookup {
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
    VariousArtists("various-artists")
  }

  @Suppress("unused")
  public enum class Misc(override val value: String) : Lookup {
    Aliases("aliases"),
    Annotation("annotation"),
    Tags("tags"),
    Ratings("ratings"),
    Genres("genres");
    public companion object {
      /** Doesn't create a values() array and/or list every time */
      public val all: List<Misc> by lazy { values().asList() }
    }
  }

  public enum class SearchField(public val value: String) {
    /** (part of) any alias attached to the artist (diacritics are ignored) */
    Alias("alias"),
    /** (part of) any primary alias attached to the artist (diacritics are ignored) */
    PrimaryAlias("primary_alias"),
    /** (part of) the name of the artist's main associated area */
    Area("area"),
    /** the artist's MBID */
    ArtistId("arid"),
    /** the artist's name (without accented characters) */
    Artist("artist"),
    /** the artist's name (with accented characters) */
    ArtistAccent("artistaccent"),
    /** the artist's begin date */
    Begin("begin"),
    /** the artist's begin area */
    BeginArea("beginarea"),
    /** the artist's disambiguation comment */
    Comment("comment"),
    /**
     * the 2-letter code (ISO 3166-1 alpha-2) for the artist's main associated country, or “unknown”
     */
    Country("country"),
    /** the artist's end date */
    End("end"),
    /** the artist's end area */
    EndArea("endarea"),
    /** a flag indicating whether or not the artist has ended */
    Ended("ended"),
    /** the artist's gender (“male”, “female”, or “other”) */
    Gender("gender"),
    /**
     * A number identifying persons connected to ISWC registered works (authors, composers, etc.).
     */
    Ipi("ipi"),
    /**
     * an ISNI code associated with the artist
     */
    Isni("isni"),
    /** the artist's sort name */
    SortName("sortname"),
    /** a tag attached to the artist */
    Tag("tag"),
    /** the artist's type (“person”, “group”, ...) */
    Type("type")
  }

  public companion object {
    public val NullArtist: Artist = Artist(id = NullObject.ID, name = NullObject.NAME)
    public val fallbackMapping: Pair<String, Any> = Artist::class.java.name to NullArtist
  }
}

public inline val Artist.isNullObject: Boolean
  get() = this === NullArtist

public inline class ArtistMbid(override val value: String) : Mbid {
  public companion object {

    // https://musicbrainz.org/doc/Style/Unknown_and_untitled/Special_purpose_artist
    public val ANONYMOUS: ArtistMbid = ArtistMbid("f731ccc4-e22a-43af-a747-64213329e088")
    public val DATA: ArtistMbid = ArtistMbid("33cf029c-63b0-41a0-9855-be2a3665fb3b")
    public val DIALOGUE: ArtistMbid = ArtistMbid("314e1c25-dde7-4e4d-b2f4-0a7b9f7c56dc")
    public val NO_ARTIST: ArtistMbid = ArtistMbid("eec63d3c-3b81-4ad4-b1e4-7c147d4d2b61")
    public val TRADITIONAL: ArtistMbid = ArtistMbid("9be7f096-97ec-4615-8957-8d40b5dcbc41")
    public val UNKNOWN: ArtistMbid = ArtistMbid("125ec42a-7229-4250-afc5-e057484327fe")
    public val VARIOUS_ARTISTS: ArtistMbid = ArtistMbid("89ad4ac3-39f7-470e-963a-56509c546377")
    public val CHRISTMAS_MUSIC: ArtistMbid = ArtistMbid("0187fe48-c87d-4dd8-beca-9c07ef535603")
    public val DISNEY: ArtistMbid = ArtistMbid("66ea0139-149f-4a0c-8fbf-5ea9ec4a6e49")
    public val MUSICAL_THEATER: ArtistMbid = ArtistMbid("a0ef7e1d-44ff-4039-9435-7d5fefdeecc9")
    public val CLASSICAL_MUSIC: ArtistMbid = ArtistMbid("9e44f539-f3fc-4120-bce2-94c8716437fa")
    public val SOUNDTRACK: ArtistMbid = ArtistMbid("d6bd72bc-b1e2-4525-92aa-0f853cbb41bf")
    public val RELIGIOUS_MUSIC: ArtistMbid = ArtistMbid("ae636985-40e8-4010-ae02-0f35930f8017")
    public val CHURCH_CHIMES: ArtistMbid = ArtistMbid("90068d37-bae7-4292-be4a-704c145bd616")
    public val LANGUAGE_INSTRUCTION: ArtistMbid = ArtistMbid("80a8851f-444c-4539-892b-ad2a49292aa9")
    public val NATURE_SOUNDS: ArtistMbid = ArtistMbid("51118c9d-965d-4f9f-89a1-0091837ccf54")
    public val NEWS_REPORT: ArtistMbid = ArtistMbid("49e713ce-c3be-4697-8983-ee7cd0a11ea1")
  }
}

public inline val Artist.mbid: ArtistMbid
  get() = id.toArtistMbid()

@Suppress("NOTHING_TO_INLINE")
public inline fun String.toArtistMbid(): ArtistMbid {
  if (Mbid.logInvalidMbid && isInvalidMbid()) Timber.w("Invalid ArtistMbid")
  return ArtistMbid(this)
}
