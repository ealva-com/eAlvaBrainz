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

/**
 * It's expected that [location] will be the sole determinant for equality. This interface
 * is provided as common facade over the source of images.
 */
@Suppress("unused")
interface RemoteImage : Comparable<RemoteImage> {
  val location: Uri
  val sizeBucket: SizeBucket
  val sourceLogoDrawableRes: Int
  val sourceIntent: Intent
  val actualSize: Size?
}


