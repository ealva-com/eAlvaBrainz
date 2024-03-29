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

@JsonClass(generateAdapter = true)
public class Instrument(
  /** The MusicBrainz ID (MBID */
  public val id: String = "",
  /** Name of the instrument, typically the most common name in English. */
  public val name: String = "",
  /**
   * The type categorises the instrument by the way the sound is created, similar to the
   * Hornbostel-Sachs classification. The possible values are:
   * * **Wind instrument**
   * An aerophone, i.e. an instrument where the sound is created by vibrating air. The instrument
   * itself does not vibrate.
   * * **String instrument**
   * A chordophone, i.e. an instrument where the sound is created by the vibration of strings.
   * * **Percussion instrument**
   * An idiophone, i.e. an instrument where the sound is produced by the body of the instrument
   * vibrating, or a drum (most membranophones) where the sound is produced by a stretched membrane
   * which is struck or rubbed.
   * * **Electronic instrument**
   * An electrophone, i.e. an instrument where the sound is created with electricity.
   * * **Family**
   * A grouping of related but different instruments, like the different violin-like instruments.
   * * **Ensemble**
   * A standard grouping of instruments often played together, like a string quartet.
   * * **Other instrument**
   * An instrument which doesn't fit in the categories above.
   */
  public val type: String = "",
  @Json(name = "type-id") public val typeId: String = "",
  /** A brief description of the main characteristics of the instrument. */
  public val description: String = "",
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
  public val aliases: List<Alias> = emptyList(),
  @field:FallbackOnNull public val rating: Rating = Rating.NullRating,
  @field:FallbackOnNull @field:Json(name = "user-rating") public val userRating: Rating =
    Rating.NullRating,
  public val tags: List<Tag> = emptyList(),
  public val relations: List<Relation> = emptyList(),
  /** score ranking used in query results */
  public val score: Int = 0
) {

  /**
   * Only [id] used in determining equality as the mbid is unique and for this entity only. However,
   * if an object with this mbid appears in 2 different queries, it's possible their scores are
   * different.
   */
  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as Instrument

    if (id != other.id) return false

    return true
  }

  override fun hashCode(): Int {
    return id.hashCode()
  }

  public enum class Include(override val value: String) : Inc {
    Aliases("aliases"),
    Annotation("annotation"),
    Tags("tags"),
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
    /** (part of) any alias attached to the instrument (diacritics are ignored) */
    Alias("alias"),

    /** (part of) the instrument's disambiguation comment */
    Comment("comment"),

    /** Default searches [Alias], [Description], and [Instrument] */
    Default(""),

    /** (part of) the description of the instrument (in English) */
    Description("description"),

    /** the MBID of the instrument */
    InstrumentId("iid"),

    /** (part of) the instrument's name (diacritics are ignored)  */
    InstrumentName("instrument"),

    /** (part of) the instrument's name (with the specified diacritics) */
    InstrumentAccent("instrumentaccent"),

    /** (part of) a tag attached to the instrument */
    Tag("tag"),

    /** the instrument's [type](https://musicbrainz.org/doc/Instrument#Type) */
    Type("type");
  }

  public companion object {
    public val NullInstrument: Instrument = Instrument(id = NullObject.ID, name = NullObject.NAME)
    public val fallbackMapping: Pair<String, Any> = Instrument::class.java.name to NullInstrument
  }
}

public inline val Instrument.isNullObject: Boolean
  get() = this === Instrument.NullInstrument

@Parcelize
@JvmInline
public value class InstrumentMbid(override val value: String) : Mbid

public inline val Instrument.mbid: InstrumentMbid
  get() = InstrumentMbid(id)
