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

package com.ealva.ealvabrainz.test.shared

import com.nhaarman.expect.ListMatcher
import com.nhaarman.expect.fail

public fun <T> ListMatcher<T>.toNotBeEmpty(message: (() -> Any?)? = null) {
  if (actual == null) {
    fail("Expected value to be empty, but the actual value was null.", message)
  }

  if (actual?.isEmpty() == true) {
    fail("Expected $actual to not be empty.", message)
  }
}

public fun <T> ListMatcher<T>.toHaveAny(
  message: (() -> Any?)? = null,
  predicate: (T) -> Boolean
) {
  if (actual == null) {
    fail("Expected value to be empty, but the actual value was null.", message)
  }

  if (actual?.any(predicate) != true) {
    fail("Expected $actual to not be empty.", message)
  }
}
