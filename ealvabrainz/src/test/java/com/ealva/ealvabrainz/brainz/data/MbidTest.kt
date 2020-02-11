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

package com.ealva.ealvabrainz.brainz.data

import com.nhaarman.expect.Matcher
import com.nhaarman.expect.expect
import org.junit.Assert.fail
import org.junit.Test

class MbidTest {

  @Test
  fun `test mbid appears valid`() {
    expect("ca2866c0-e204-4b0e-8fd2-00823863e2b2".toMbid()).toBeValid()

    expect("ca2866c00-e204-4b0e-8fd2-00823863e2b2".toMbid()).toBeInvalid()
    expect("ca2866c-e204-4b0e-8fd2-00823863e2b2".toMbid()).toBeInvalid()

    expect("ca2866c-e2040-4b0e-8fd2-00823863e2b2".toMbid()).toBeInvalid()
    expect("ca2866c0-e20-4b0e-8fd2-00823863e2b2".toMbid()).toBeInvalid()

    expect("ca2866c0-e204-4b0e0-8fd2-00823863e2b2".toMbid()).toBeInvalid()
    expect("ca2866c0-e204-4b0-8fd2-00823863e2b2".toMbid()).toBeInvalid()

    expect("ca2866c0-e204-4b0e-8fd20-00823863e2b2".toMbid()).toBeInvalid()
    expect("ca2866c0-e204-4b0e-8fd-00823863e2b2".toMbid()).toBeInvalid()

    expect("ca2866c0-e204-4b0e-8fd2-00823863e2b20".toMbid()).toBeInvalid()
    expect("ca2866c0-e204-4b0e-8fd2-00823863e2b".toMbid()).toBeInvalid()

    expect("ca2866c0--e204-4b0e-8fd2-00823863e2b2".toMbid()).toBeInvalid()
    expect("ca2866c0e204-4b0e-8fd2-00823863e2b2".toMbid()).toBeInvalid()
    expect("ca2866c0-e204--4b0e-8fd2-00823863e2b2".toMbid()).toBeInvalid()
    expect("ca2866c0-e2044b0e-8fd2-00823863e2b2".toMbid()).toBeInvalid()
    expect("ca2866c0-e204-4b0e--8fd2-00823863e2b2".toMbid()).toBeInvalid()
    expect("ca2866c0-e204-4b0e8fd200823863e2b2".toMbid()).toBeInvalid()
    expect("ca2866c0-e204-4b0e-8fd2--00823863e2b2".toMbid()).toBeInvalid()
    expect("ca2866c0-e204-4b0e-8fd200823863e2b2".toMbid()).toBeInvalid()

    expect("-ca2866c0-e204-4b0e-8fd2-00823863e2b2".toMbid()).toBeInvalid()
    expect("ca2866c0-e204-4b0e-8fd2-00823863e2b2-".toMbid()).toBeInvalid()

    expect("".toMbid()).toBeInvalid()
  }
}

private fun Matcher<TestMbid>.toBeValid() {
  val mbid = actual
  if (mbid == null) fail("Expected valid Mbid but was null") else {
    if (!mbid.appearsValid()) fail("Expected valid Mbid but was $mbid")
  }
}

private fun Matcher<TestMbid>.toBeInvalid() {
  val mbid = actual
  if (mbid == null) fail("Expected invalid Mbid but was null") else {
    if (mbid.appearsValid()) fail("Expected invalid Mbid but was $mbid")
  }
}

private inline class TestMbid(override val value: String) : Mbid

private fun String.toMbid() = TestMbid(this)
