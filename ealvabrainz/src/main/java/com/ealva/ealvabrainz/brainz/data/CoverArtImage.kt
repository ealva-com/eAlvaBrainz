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
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
public class CoverArtImage(
  /** This is not a MusicBrainz MBID */
  public val id: Long = 0L,
  /**
   * The original image url. Has the form:
   * https://coverartarchive.org/release/76df3287-6cda-33eb-8e9a-044b5e15ffdd/829521842.jpg
   */
  public val image: String = "",
  /**
   * Examples of types are "Front", "Back", "Booklet", etc
   *
   * Sometimes Moshi is putting a null in the list, so we have the type as String? (not saying
   * Moshi is wrong - the data may contain a null)
   *
   * [https://musicbrainz.org/doc/Cover_Art/Types]
   */
  public val types: List<String?> = emptyList(),
  public val front: Boolean = false,
  public val back: Boolean = false,
  public val edit: Int = 0,
  public val comment: String = "",
  public val isApproved: Boolean = false,
  @field:FallbackOnNull public val thumbnails: Thumbnails = Thumbnails.NullThumbnails
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

public enum class CoverArtImageSize {
  Original,
  Size250,
  Size500,
  Size1200,
  Unknown
}

public inline val CoverArtImage.isNullObject: Boolean
  get() = this === NullCoverArtImage

/**
 * Currently types may contain null, need to revisit this
 */
public val CoverArtImage.imageTypes: Sequence<CoverArtImageType>
  get() = types.asSequence()
    .filterNotNull()
    .filterNot { it.isEmpty() }
    .map { it.toImageType() }

public fun String.toImageType(): CoverArtImageType =
  nameToImageTypeMap.getOrPut(this) {
    if (isNullOrEmpty()) CoverArtImageType.UNKNOWN else CoverArtImageType.Unrecognized(this)
  }

private val nameToImageTypeMap: MutableMap<String, CoverArtImageType> = mutableMapOf(
  Pair(CoverArtImageType.FRONT.name, CoverArtImageType.FRONT),
  Pair(CoverArtImageType.BACK.name, CoverArtImageType.BACK),
  Pair(CoverArtImageType.BOOKLET.name, CoverArtImageType.BOOKLET),
  Pair(CoverArtImageType.MEDIUM.name, CoverArtImageType.MEDIUM),
  Pair(CoverArtImageType.TRAY.name, CoverArtImageType.TRAY),
  Pair(CoverArtImageType.OBI.name, CoverArtImageType.OBI),
  Pair(CoverArtImageType.SPINE.name, CoverArtImageType.SPINE),
  Pair(CoverArtImageType.TRACK.name, CoverArtImageType.TRACK),
  Pair(CoverArtImageType.LINER.name, CoverArtImageType.LINER),
  Pair(CoverArtImageType.STICKER.name, CoverArtImageType.STICKER),
  Pair(CoverArtImageType.POSTER.name, CoverArtImageType.POSTER),
  Pair(CoverArtImageType.WATERMARK.name, CoverArtImageType.WATERMARK),
  Pair(CoverArtImageType.OTHER.name, CoverArtImageType.OTHER)
)

public sealed class CoverArtImageType(public val name: String) {
  // https://musicbrainz.org/doc/Cover_Art/Types
  public object FRONT : CoverArtImageType("Front")
  public object BACK : CoverArtImageType("Back")
  public object BOOKLET : CoverArtImageType("Booklet")
  public object MEDIUM : CoverArtImageType("Medium")
  public object TRAY : CoverArtImageType("Tray")
  public object OBI : CoverArtImageType("Obi")
  public object SPINE : CoverArtImageType("Spine")
  public object TRACK : CoverArtImageType("Track")
  public object LINER : CoverArtImageType("Liner")
  public object STICKER : CoverArtImageType("Sticker")
  public object POSTER : CoverArtImageType("Poster")
  public object WATERMARK : CoverArtImageType("Watermark")
  public object OTHER : CoverArtImageType("Other")
  public object UNKNOWN : CoverArtImageType("UNKNOWN")
  public class Unrecognized(name: String) : CoverArtImageType(name)
}
