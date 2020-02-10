/*
 * Copyright (c) 2016-2019. eAlva.com - All Rights Reserved
 *
 * Unauthorized copying of this file, in whole or in part, via any medium, is strictly prohibited
 * Proprietary and confidential
 *
 */

package com.ealva.ealvabrainz.art

import com.ealva.ealvabrainz.service.R

enum class SizeBucket(val maybeVeryLarge: Boolean, val stringRes: Int) {
  ORIGINAL(true, R.string.Original),
  EXTRA_LARGE(true, R.string.ExtraLarge),
  LARGE(false, R.string.Large),
  MEDIUM(false, R.string.Medium),
  SMALL(false, R.string.Small),
  UNKNOWN(false, R.string.Unknown)
}
