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

@file:Suppress("unused")

package com.ealva.ealvabrainz.matchers

import com.nhaarman.expect.Matcher
import com.nhaarman.expect.fail

public fun <K, V> expect(actual: Map<K, V>?): MapMatcher<K, V> {
  return MapMatcher(actual)
}

public class MapMatcher<K, V>(override val actual: Map<K, V>?) : Matcher<Map<K, V>>(actual) {

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

  public fun toContainKey(expected: K, message: (() -> Any?)? = null) {
    if (actual == null) {
      fail("Expected to contain Key:$expected, but the actual value was null.", message)
    }

    if (!actual.containsKey(expected)) {
      fail("Expected $actual to contain Key:$expected", message)
    }
  }

  public fun toContainValue(expected: V, message: (() -> Any?)? = null) {
    if (actual == null) {
      fail("Expected to contain Value:$expected, but the actual value was null.", message)
    }

    if (!actual.containsValue(expected)) {
      fail("Expected $actual to contain Value:$expected", message)
    }
  }

  public fun toContainKeyValue(expected: Pair<K, V>, message: (() -> Any?)? = null) {
    if (actual == null) {
      fail("Expected to contain $expected, but the actual value was null.", message)
    }

    if (!actual.containsKey(expected.first) || actual[expected.first] != expected.second) {
      fail("Expected $actual to contain $expected", message)
    }
  }
}
