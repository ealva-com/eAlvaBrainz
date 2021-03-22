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

@JsonClass(generateAdapter = true)
public class Instrument(
  /** The MusicBrainz ID (MBID */
  public var id: String = "",
  /** Name of the instrument, typically the most common name in English. */
  public var name: String = "",
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
  public var type: String = "",
  @Json(name = "type-id") public var typeId: String = "",
  /** A brief description of the main characteristics of the instrument. */
  public var description: String = "",
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
  public var aliases: List<Alias> = emptyList(),
  public var tags: List<Tag> = emptyList(),
  public var relations: List<Relation> = emptyList(),
  /** score ranking used in query results */
  public var score: Int = 0
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

  public interface Lookup : Include

  @Suppress("unused")
  public enum class Misc(override val value: String) : Lookup {
    Aliases("aliases"),
    Annotation("annotation"),
    Tags("tags"),
    Genres("genres")
  }

  @Suppress("unused")
  public enum class SearchField(public val value: String) {
    /** an alias attached to the instrument */
    Alias("alias"),

    /** the disambiguation comment for the instrument */
    Comment("comment"),

    /** the description of the instrument */
    Description("description"),

    /** the MBID of the instrument */
    InstrumentId("iid"),

    /** the name of the instrument */
    Instrument("instrument"),

    /** the instrument's type */
    Type("type"),

    /** a tag attached to the instrument */
    Tag("tag"),
  }

  public companion object {
    public val NullInstrument: Instrument = Instrument(id = NullObject.ID, name = NullObject.NAME)
    public val fallbackMapping: Pair<String, Any> = Instrument::class.java.name to NullInstrument
  }
}

public inline val Instrument.isNullObject: Boolean
  get() = this === Instrument.NullInstrument
