/*
 * Copyright (c) 2021  Eric A. Snell
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

import com.squareup.moshi.JsonClass

/**
 * A CD stub is an anonymously submitted track list that contains a disc ID, barcode, comment field,
 * and basic metadata like a release title and track names.
 *
 * [CD Stub](https://musicbrainz.org/doc/CD_Stub)
 */
@JsonClass(generateAdapter = true)
public class CdStub(
  /** The Disc ID */
  public val id: String = "",
  /** The release title */
  public val title: String = "",
  /** (part of) the artist name set on the CD stub */
  public val artist: String = "",
  public val barcode: String = "",
  /** The number of tracks in the track list */
  public val count: Int = 0,
  public val disambiguation: String = "",
  /** score ranking used in query results */
  public val score: Int = 0
) {

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as CdStub

    if (id != other.id) return false

    return true
  }

  override fun hashCode(): Int {
    return id.hashCode()
  }

  override fun toString(): String = toJson()

  public enum class SearchField(public override val value: String) : EntitySearchField {
    /** the date the CD stub was added (e.g. "2020-01-22") */
    Added("added"),

    /** (part of) the artist name set on the CD stub */
    Artist("artist"),

    /** the barcode set on the CD stub */
    Barcode("barcode"),

    /** (part of) the comment set on the CD stub */
    Comment("comment"),

    /** Default searches [Artist] and [Title] */
    Default(""),

    /** the CD stub's [Disc ID](https://musicbrainz.org/doc/Disc_ID) */
    Discid("discid"),

    /** (part of) the release title set on the CD stub */
    Title("title"),

    /** the number of tracks on the CD stub */
    TrackCount("tracks"),
  }

  public companion object {
    public val NullCdStub: CdStub = CdStub(id = NullObject.ID, title = NullObject.NAME)
    public val fallbackMapping: Pair<String, Any> = CdStub::class.java.name to NullCdStub
  }
}

public inline val CdStub.isNullObject: Boolean
  get() = this === CdStub.NullCdStub
