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

import com.ealva.ealvabrainz.brainz.data.Work.Companion.NullWork
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

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
@JsonClass(generateAdapter = true)
public class Work(
  /** The MusicBrainz ID (MBID) for this work */
  public var id: String = "",
  /** The canonical title of the work, expressed in the language it was originally written. */
  public var title: String = "",
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
   * The International Standard Musical Work Code assigned to the work by copyright collecting
   * agencies.
   */
  public var iswcs: List<String> = emptyList(),
  public var language: String = "",
  public var languages: List<String> = emptyList(),
  public var type: String = "",
  @field:Json(name = "type-id") public var typeId: String = "",
  public var attributes: List<Attribute> = emptyList(),
  /**
   * If a discrete work is known by name(s) or in language(s) other than its canonical name, these
   * are specified in the work’s aliases.
   */
  public var aliases: List<Alias> = emptyList(),
  public var relations: List<Relation> = emptyList(),
  public var tags: List<Tag> = emptyList()
) {
  public interface Lookup : Include

  @Suppress("unused")
  public enum class Misc(override val value: String) : Lookup {
    Aliases("aliases"),
    Annotation("annotation"),
    Tags("tags"),
    Genres("genres")
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as Work

    if (id != other.id) return false

    return true
  }

  override fun hashCode(): Int = id.hashCode()

  override fun toString(): String = toJson()

  @Suppress("unused")
  public enum class SearchField(public val value: String) {
    /** the aliases/misspellings for this work */
    Alias("alias"),

    /** artist id */
    ArtistId("arid"),

    /**
     * artist name, an artist in the context of a work is an artist-work relation such as composer
     * or lyricist
     **/
    Artist("artist"),

    /** disambiguation comment */
    Comment("comment"),

    /** ISWC of work */
    Iswc("iswc"),

    /** Lyrics language of work */
    Language("lang"),

    /** folksonomy tag */
    Tag("tag"),

    /** work type */
    Type("type"),

    /** work id */
    WorkId("wid"),

    /** name of work */
    Work("work"),

    /** name of the work with any accent characters retained */
    WorkAccent("workaccent"),
  }

  public companion object {
    public val NullWork: Work = Work(id = NullObject.ID, title = NullObject.NAME)
    public val fallbackMapping: Pair<String, Any> = Work::class.java.name to NullWork
  }
}

public inline val Work.isNullObject: Boolean
  get() = this === NullWork
