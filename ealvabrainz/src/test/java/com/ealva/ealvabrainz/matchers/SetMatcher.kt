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

import com.nhaarman.expect.Matcher
import com.nhaarman.expect.fail

public fun <T> expect(actual: Set<T>?): SetMatcher<T> {
  return SetMatcher(actual)
}

public class SetMatcher<T>(override val actual: Set<T>?) : Matcher<Set<T>>(actual) {

  public fun toBeEmpty(message: (() -> Any?)? = null) {
    if (actual == null) {
      fail("Expected value to be empty, but the actual value was null.", message)
    }

    if (actual.isNotEmpty()) {
      fail("Expected $actual to be empty.", message)
    }
  }

  public fun toHaveSize(size: Int, message: (() -> Any?)? = null) {
    if (actual == null) {
      fail("Expected value to have size $size, but the actual value was null.", message)
    }

    if (actual.size != size) {
      fail("Expected $actual to have size $size, but the actual size was ${actual.size}.", message)
    }
  }

  public fun toContain(expected: T, message: (() -> Any?)? = null) {
    if (actual == null) {
      fail("Expected to contain Key:$expected, but the actual value was null.", message)
    }

    if (!actual.contains(expected)) {
      fail("Expected $actual to contain Key:$expected", message)
    }
  }
}
