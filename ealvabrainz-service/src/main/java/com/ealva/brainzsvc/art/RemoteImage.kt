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

package com.ealva.brainzsvc.art

import android.content.Intent
import android.net.Uri
import android.util.Size
import com.ealva.brainzsvc.common.NullIntent
import com.ealva.brainzsvc.net.toSecureUri

/**
 * It's expected that [location] will be the sole determinant for equality. This interface
 * is provided as common facade over the source of images.
 */
public sealed class RemoteImage : Comparable<RemoteImage> {
  public abstract val location: Uri
  public abstract val sizeBucket: SizeBucket
  public abstract val sourceLogoDrawableRes: Int
  public abstract val sourceIntent: Intent
  public abstract val actualSize: Size?
  public abstract val otherInfo: String
}

/**
 * Equality/hashCode/compareTo is determined solely by [location]
 */
public class RemoteImageData(
  override val location: Uri,
  override val sizeBucket: SizeBucket,
  override val sourceLogoDrawableRes: Int,
  override val sourceIntent: Intent,
  override val actualSize: Size? = null,
  override val otherInfo: String = location.toString()
) : RemoteImage() {
  override fun compareTo(other: RemoteImage): Int {
    return location.compareTo(other.location)
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as RemoteImageData

    if (location != other.location) return false

    return true
  }

  override fun hashCode(): Int {
    return location.hashCode()
  }

  public companion object {
    public fun fromUrl(
      url: String,
      bucket: SizeBucket,
      sourceLogoDrawableRes: Int,
      source: Intent,
      actualSize: Size? = null
    ): RemoteImageData {
      return RemoteImageData(url.toSecureUri(), bucket, sourceLogoDrawableRes, source, actualSize)
    }
  }
}

public class RemoteImageError(override val otherInfo: String) : RemoteImage() {
  override val location: Uri = Uri.EMPTY
  override val sizeBucket: SizeBucket = SizeBucket.UNKNOWN
  override val sourceLogoDrawableRes: Int = -1
  override val sourceIntent: Intent = NullIntent
  override val actualSize: Size? = null
  override fun compareTo(other: RemoteImage): Int = if (other === this) 0 else 1
}
