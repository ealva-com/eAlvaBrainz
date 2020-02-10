/*
 * Copyright (c) 2016-2019. eAlva.com - All Rights Reserved
 *
 * Unauthorized copying of this file, in whole or in part, via any medium, is strictly prohibited
 * Proprietary and confidential
 *
 */

package com.ealva.ealvabrainz.art

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
