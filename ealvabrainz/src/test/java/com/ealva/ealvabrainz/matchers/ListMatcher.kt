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

package com.ealva.ealvabrainz.matchers

import com.nhaarman.expect.ListMatcher
import com.nhaarman.expect.fail

public fun <T> ListMatcher<T>.toHaveOnlyOne(
  message: (() -> Any?)? = null,
  predicate: (T) -> Boolean
) {
  actual?.let {
    if (it.filter(predicate).size != 1) fail("Expected one instance. ", message)
  } ?: fail("Expected value to be empty, but the actual value was null.", message)
}
