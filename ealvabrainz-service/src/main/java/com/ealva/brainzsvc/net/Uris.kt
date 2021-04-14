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

package com.ealva.brainzsvc.net

import android.net.Uri
import androidx.core.net.toUri

/**
 * Creates a [Uri] from the given encoded URI string or returns [Uri.EMPTY] if string is null
 * or empty
 *
 * @see String.toUri
 */
internal fun String?.toUriOrEmpty(): Uri =
  if (this != null && this.isNotEmpty()) this.toUri() else Uri.EMPTY

/**
 * First checks if this is the [Uri.EMPTY] instance and then does equality check. This is because
 * current Uri implementation doesn't check for identity first, which is our typical case when empty
 * @return true if this Uri is the [Uri.EMPTY] instance or is [this] == [Uri.EMPTY]
 */
internal fun Uri.isEmpty(): Boolean = this === Uri.EMPTY || this == Uri.EMPTY

internal fun Uri.isNotEmpty(): Boolean = !isEmpty()

private const val HTTP_PREFIX_LEN = 4

internal fun String?.toSecureUri(): Uri {
  return if (this != null && startsWith("http:"))
    "https${substring(HTTP_PREFIX_LEN)}".toUri()
  else
    toUriOrEmpty()
}
