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
import androidx.core.net.toUri

fun String?.toUriOrEmpty(): Uri =
  if (this != null && this.isNotEmpty()) this.toUri() else Uri.EMPTY


private fun String?.toSecureUri(): Uri {
  return if (this != null && startsWith("http:"))
    "https${this.substring(4)}".toUri()
  else
    toUriOrEmpty()
}

/**
 * Equality/hashCode/compareTo is determined solely by [location]
 */
@Suppress("unused")
data class RemoteImageData(
  override val location: Uri,
  override val sizeBucket: SizeBucket,
  override val sourceLogoDrawableRes: Int,
  override val sourceIntent: Intent,
  override val actualSize: Size? = null
) : RemoteImage {
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

  companion object {
    fun fromUrl(
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
