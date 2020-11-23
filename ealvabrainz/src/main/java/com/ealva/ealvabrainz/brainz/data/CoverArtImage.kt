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

import com.ealva.ealvabrainz.brainz.data.CoverArtImage.Companion.NullCoverArtImage
import com.ealva.ealvabrainz.moshi.FallbackOnNull
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
public class CoverArtImage(
  /** This is not a MusicBrainz MBID */
  public var id: Long = 0L,
  /**
   * The original image url. Has the form:
   * https://coverartarchive.org/release/76df3287-6cda-33eb-8e9a-044b5e15ffdd/829521842.jpg
   */
  public var image: String = "",
  /**
   * Examples of types are "Front", "Back", "Booklet", etc
   *
   * Sometimes Moshi is putting a null in the list, so we have the type as String? (not saying
   * Moshi is wrong - the data may contain a null)
   *
   * [https://musicbrainz.org/doc/Cover_Art/Types]
   */
  public var types: List<String?> = emptyList(),
  public var front: Boolean = false,
  public var back: Boolean = false,
  public var edit: Int = 0,
  public var comment: String = "",
  public var isApproved: Boolean = false,
  @field:FallbackOnNull public var thumbnails: Thumbnails = Thumbnails.NullThumbnails
) {
  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as CoverArtImage

    if (id != other.id) return false

    return true
  }

  override fun hashCode(): Int = id.hashCode()

  override fun toString(): String = toJson()

  public companion object {
    public val NullCoverArtImage: CoverArtImage = CoverArtImage(id = -1)
    public val fallbackMapping: Pair<String, Any> =
      CoverArtImage::class.java.name to NullCoverArtImage
  }
}

public inline val CoverArtImage.isNullObject: Boolean
  get() = this === NullCoverArtImage

/**
 * Currently types may contain null, need to revisit this
 */
public val CoverArtImage.imageTypes: Sequence<CoverArtImageType>
  get() = types.asSequence()
    .filterNotNull()
    .map { rawType -> CoverArtImageType(rawType) }

public inline class CoverArtImageType(public val value: String) {

  @Suppress("unused")
  public companion object {
    // https://musicbrainz.org/doc/Cover_Art/Types
    public val TYPE_FRONT: CoverArtImageType = CoverArtImageType("Front")
    public val TYPE_BACK: CoverArtImageType = CoverArtImageType("Back")
    public val TYPE_BOOKLET: CoverArtImageType = CoverArtImageType("Booklet")
    public val TYPE_MEDIUM: CoverArtImageType = CoverArtImageType("Medium")
    public val TYPE_TRAY: CoverArtImageType = CoverArtImageType("Tray")
    public val TYPE_OBI: CoverArtImageType = CoverArtImageType("Obi")
    public val TYPE_SPINE: CoverArtImageType = CoverArtImageType("Spine")
    public val TYPE_TRACK: CoverArtImageType = CoverArtImageType("Track")
    public val TYPE_LINER: CoverArtImageType = CoverArtImageType("Liner")
    public val TYPE_STICKER: CoverArtImageType = CoverArtImageType("Sticker")
    public val TYPE_POSTER: CoverArtImageType = CoverArtImageType("Poster")
    public val TYPE_WATERMARK: CoverArtImageType = CoverArtImageType("Watermark")
    public val TYPE_OTHER: CoverArtImageType = CoverArtImageType("Other")
  }
}
