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
class CoverArtImage(
  /** This is not a MusicBrainz MBID */
  var id: Long = 0L,
  /**
   * The original image url. Has the form:
   * https://coverartarchive.org/release/76df3287-6cda-33eb-8e9a-044b5e15ffdd/829521842.jpg
   */
  var image: String = "",
  /**
   * Examples of types are "Front", "Back", "Booklet", etc
   *
   * Sometimes Moshi is putting a null in the list, so we have the type as String? (not saying
   * Moshi is wrong - the data may contain a null)
   *
   * [https://musicbrainz.org/doc/Cover_Art/Types]
   */
  var types: List<String?> = emptyList(),
  var front: Boolean = false,
  var back: Boolean = false,
  var edit: Int = 0,
  var comment: String = "",
  var isApproved: Boolean = false,
  @field:FallbackOnNull var thumbnails: Thumbnails = Thumbnails.NullThumbnails
) {
  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as CoverArtImage

    if (id != other.id) return false

    return true
  }

  override fun hashCode() = id.hashCode()

  override fun toString() = toJson()

  companion object {
    val NullCoverArtImage = CoverArtImage(id = -1)
    val fallbackMapping: Pair<String, Any> = CoverArtImage::class.java.name to NullCoverArtImage
  }
}

inline val CoverArtImage.isNullObject
  get() = this === NullCoverArtImage

/**
 * Currently types may contain null, need to revisit this
 */
val CoverArtImage.imageTypes: Sequence<CoverArtImageType>
  get() = types.asSequence()
    .filterNotNull()
    .map { rawType -> CoverArtImageType(rawType) }

inline class CoverArtImageType(val value: String) {

  @Suppress("unused")
  companion object {
    // https://musicbrainz.org/doc/Cover_Art/Types
    val TYPE_FRONT = CoverArtImageType("Front")
    val TYPE_BACK = CoverArtImageType("Back")
    val TYPE_BOOKLET = CoverArtImageType("Booklet")
    val TYPE_MEDIUM = CoverArtImageType("Medium")
    val TYPE_TRAY = CoverArtImageType("Tray")
    val TYPE_OBI = CoverArtImageType("Obi")
    val TYPE_SPINE = CoverArtImageType("Spine")
    val TYPE_TRACK = CoverArtImageType("Track")
    val TYPE_LINER = CoverArtImageType("Liner")
    val TYPE_STICKER = CoverArtImageType("Sticker")
    val TYPE_POSTER = CoverArtImageType("Poster")
    val TYPE_WATERMARK = CoverArtImageType("Watermark")
    val TYPE_OTHER = CoverArtImageType("Other")
  }
}

