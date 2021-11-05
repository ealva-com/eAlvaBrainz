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

package com.ealva.ealvabrainz.brainz.data

import com.nhaarman.expect.Matcher
import com.nhaarman.expect.expect
import com.nhaarman.expect.fail
import kotlinx.parcelize.Parcelize
import org.junit.Test

public class MbidTest {

  @Test
  public fun `test mbid appears valid`() {
    expect(TestMbid("ca2866c0-e204-4b0e-8fd2-00823863e2b2")).toBeValid()

    expect(TestMbid("ca2866c00-e204-4b0e-8fd2-00823863e2b2")).toBeInvalid()
    expect(TestMbid("ca2866c-e204-4b0e-8fd2-00823863e2b2")).toBeInvalid()

    expect(TestMbid("ca2866c-e2040-4b0e-8fd2-00823863e2b2")).toBeInvalid()
    expect(TestMbid("ca2866c0-e20-4b0e-8fd2-00823863e2b2")).toBeInvalid()

    expect(TestMbid("ca2866c0-e204-4b0e0-8fd2-00823863e2b2")).toBeInvalid()
    expect(TestMbid("ca2866c0-e204-4b0-8fd2-00823863e2b2")).toBeInvalid()

    expect(TestMbid("ca2866c0-e204-4b0e-8fd20-00823863e2b2")).toBeInvalid()
    expect(TestMbid("ca2866c0-e204-4b0e-8fd-00823863e2b2")).toBeInvalid()

    expect(TestMbid("ca2866c0-e204-4b0e-8fd2-00823863e2b20")).toBeInvalid()
    expect(TestMbid("ca2866c0-e204-4b0e-8fd2-00823863e2b")).toBeInvalid()

    expect(TestMbid("ca2866c0--e204-4b0e-8fd2-00823863e2b2")).toBeInvalid()
    expect(TestMbid("ca2866c0e204-4b0e-8fd2-00823863e2b2")).toBeInvalid()
    expect(TestMbid("ca2866c0-e204--4b0e-8fd2-00823863e2b2")).toBeInvalid()
    expect(TestMbid("ca2866c0-e2044b0e-8fd2-00823863e2b2")).toBeInvalid()
    expect(TestMbid("ca2866c0-e204-4b0e--8fd2-00823863e2b2")).toBeInvalid()
    expect(TestMbid("ca2866c0-e204-4b0e8fd200823863e2b2")).toBeInvalid()
    expect(TestMbid("ca2866c0-e204-4b0e-8fd2--00823863e2b2")).toBeInvalid()
    expect(TestMbid("ca2866c0-e204-4b0e-8fd200823863e2b2")).toBeInvalid()

    expect(TestMbid("-ca2866c0-e204-4b0e-8fd2-00823863e2b2")).toBeInvalid()
    expect(TestMbid("ca2866c0-e204-4b0e-8fd2-00823863e2b2-")).toBeInvalid()

    expect(TestMbid("ca2866c0ae204-4b0e-8fd2-00823863e2b2")).toBeInvalid()
    expect(TestMbid("ca2866c0-e204a4b0e-8fd2-00823863e2b2")).toBeInvalid()
    expect(TestMbid("ca2866c0-e204-4b0ea8fd2-00823863e2b2")).toBeInvalid()
    expect(TestMbid("ca2866c0-e204-4b0e-8fd2a00823863e2b2")).toBeInvalid()
    expect(TestMbid("2ca2866c0-e204-4b0e-8fd2-00823863e2b2")).toBeInvalid()
    expect(TestMbid("ca2866c0-e204-4b0e-8fd2-00823863e2b2c")).toBeInvalid()

    expect(TestMbid("ca28g6c0-e204-4b0e-8fd2-00823863e2b2c")).toBeInvalid()

    expect(TestMbid("-a2866c0-e204-4b0e-8fd2-00823863e2b2")).toBeInvalid()
    expect(TestMbid("ca2866c0-e204-4b0e-8fd2-00823863e2b-")).toBeInvalid()

    expect(TestMbid("")).toBeInvalid()
  }

  @Test
  public fun `test mbid isObsolete`() {
    val validMbid = ArtistMbid.ANONYMOUS
    val equalToValid = ArtistMbid(ArtistMbid.ANONYMOUS.value)
    val invalidMbid = ArtistMbid("")
    expect(validMbid.isObsolete(null)).toBe(false)
    expect(validMbid.isObsolete(invalidMbid)).toBe(false)
    expect(validMbid.isObsolete(equalToValid)).toBe(false)
    expect(validMbid.isObsolete(ArtistMbid.DISNEY)).toBe(true)

    expect(invalidMbid.isObsolete(null)).toBe(false)
    expect(invalidMbid.isObsolete(ArtistMbid(""))).toBe(false)
    expect(invalidMbid.isObsolete(validMbid)).toBe(true)
    expect(invalidMbid.isObsolete(ArtistMbid.DISNEY)).toBe(true)
  }
}

private fun Matcher<TestMbid>.toBeValid() {
  val mbid = actual ?: fail("Expected valid Mbid but was null")
  if (mbid.isInvalid) fail("Expected valid Mbid but was $mbid")
}

private fun Matcher<TestMbid>.toBeInvalid() {
  val mbid = actual ?: fail("Expected invalid Mbid but was null")
  if (mbid.isValid) fail("Expected invalid Mbid but was $mbid")
}

@Parcelize
@JvmInline
private value class TestMbid(override val value: String) : Mbid
