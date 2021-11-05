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
 * In MusicBrainz terminology, a work is a distinct intellectual or artistic creation, which can
 * be expressed in the form of one or more audio recordings. While a work in MusicBrainz is
 * usually musical in nature, it is not necessarily so. For example, a work could be a novel,
 * play, poem or essay, later recorded as an oratory or audiobook.
 *
 * ## Distinctiveness
 * A work’s distinctiveness is based on the artists who contributed to its final output, and
 * whether a work is derived from another original work. Examples of works that are distinct:
 * * a work that is written by an individual songwriter
 * * a work that is the result of a collaboration between composer and lyricist
 * * an instrumental work where an artist later adds lyrics
 * * translation of an original work into a different language
 * * a parody of an original work with differing lyrics
 * * a medley of multiple original works
 * * a mashup of multiple original works
 *
 * ## Types of works
 * Works are represented predominantly at two levels:
 * * **Discrete works**
 * An individual song, musical number or movement. This includes recitatives, arias, choruses,
 * duos, trios, etc. In many cases, discrete works are a part of larger, aggregate works.
 * * **Aggregate works**
 * An ordered sequence of one or more songs, numbers or movements, such as: symphony, opera,
 * theatre work, concerto, and concept album. A popular music album is not considered a distinct
 * aggregate work unless it is evident that such an album was written with intent to have a
 * specifically ordered sequence of related songs (i.e. a “concept album”).
 */
@Suppress("unused")
@JsonClass(generateAdapter = true)
public class Work(
  /** The MusicBrainz ID (MBID) for this work */
  public val id: String = "",
  /** The canonical title of the work, expressed in the language it was originally written. */
  public val title: String = "",
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
   * The International Standard Musical Work Code assigned to the work by copyright collecting
   * agencies.
   */
  public val iswcs: List<String> = emptyList(),
  public val language: String = "",
  public val languages: List<String> = emptyList(),
  public val type: String = "",
  @field:Json(name = "type-id") public val typeId: String = "",
  public val attributes: List<Attribute> = emptyList(),
  /**
   * If a discrete work is known by name(s) or in language(s) other than its canonical name, these
   * are specified in the work’s aliases.
   */
  public val aliases: List<Alias> = emptyList(),
  public val relations: List<Relation> = emptyList(),
  @field:FallbackOnNull public val rating: Rating = Rating.NullRating,
  @field:FallbackOnNull @field:Json(name = "user-rating") public val userRating: Rating =
    Rating.NullRating,
  public val tags: List<Tag> = emptyList(),
  @field:Json(name = "user-tags") public val userTags: List<Tag> = emptyList(),
  public val genres: List<Genre> = emptyList(),
  @field:Json(name = "user-genres") public val userGenres: List<Genre> = emptyList(),
  /** score ranking used in query results */
  public val score: Int = 0
) {
  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as Work

    if (id != other.id) return false

    return true
  }

  override fun hashCode(): Int = id.hashCode()

  override fun toString(): String = toJson()

  public enum class Include(override val value: String) : Inc {
    Aliases("aliases"),
    Annotation("annotation"),
    Ratings("ratings"),
    UserRatings("user-ratings"),
    Tags("tags"),
    UserTags("user-rags"),
    Genres("genres"),
    UserGenres("user-genres")
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
    /** (part of) any alias attached to the work (diacritics are ignored) */
    Alias("alias"),

    /** the MBID of an artist related to the event (e.g. a composer or lyricist) */
    ArtistId("arid"),

    /**
     * (part of) the name of an artist related to the work (e.g. a composer or lyricist)
     */
    Artist("artist"),

    /** (part of) the work's disambiguation comment */
    Comment("comment"),

    /** Default searches for [Alias] and [Work] */
    Default(""),

    /** any ISWC associated to the work */
    Iswc("iswc"),

    /** the ISO 639-3 code for any of the languages of the work's lyrics */
    Language("lang"),

    /** (part of) the title of a recording related to the work  */
    Recording("recording"),

    /** the number of recordings related to the work */
    RecordingCount("recording_count"),

    /** the MBID of a recording related to the work */
    RecordingId("rid"),

    /** (part of) a tag attached to the work */
    Tag("tag"),

    /** the work's type (e.g. "opera", "song", "symphony") */
    Type("type"),

    /** the work's MBID */
    WorkId("wid"),

    /** (part of) the work's title (diacritics are ignored) */
    Work("work"),

    /** (part of) the work's title (with the specified diacritics) */
    WorkAccent("workaccent"),
  }

  public companion object {
    public val NullWork: Work = Work(id = NullObject.ID, title = NullObject.NAME)
    public val fallbackMapping: Pair<String, Any> = Work::class.java.name to NullWork
  }
}

public inline val Work.isNullObject: Boolean
  get() = this === Work.NullWork

@Parcelize
@JvmInline
public value class WorkMbid(override val value: String) : Mbid

public inline val Work.mbid: WorkMbid
  get() = WorkMbid(id)
