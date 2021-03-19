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

package com.ealva.ealvabrainz.common

import com.nhaarman.expect.Matcher
import com.nhaarman.expect.expect
import com.nhaarman.expect.fail
import org.junit.Test

public class MbidTest {

  @Test
  public fun `test mbid appears valid`() {
    expect(TestMbid("ca2866c0-e204-4b0e-8fd2-00823863e2b2")).toBeValid()
    expect(TestMbid("ece57992-dc2e-4f67-a269-fa43626c1a3d")).toBeValid()
    expect(TestMbid("59211ea4-ffd2-4ad9-9a4e-941d3148024a")).toBeValid()
    expect(TestMbid("c9fdb94c-4975-4ed6-a96f-ef6d80bb7738")).toBeValid()
    expect(TestMbid("b10bbbfc-cf9e-42e0-be17-e2c3e1d2600d")).toBeValid()
    expect(TestMbid("5b11f4ce-a62d-471e-81fc-a69a8278c7da")).toBeValid()
    expect(TestMbid("72d15666-99a7-321e-b1f3-a3f8c09dff9f")).toBeValid()
    expect(TestMbid("46f0f4cd-8aab-4b33-b698-f459faf64190")).toBeValid()
    expect(TestMbid("b9ad642e-b012-41c7-b72a-42cf4911f9ff")).toBeValid()
    expect(TestMbid("8a754a16-0027-3a29-b6d7-2b40ea0481ed")).toBeValid()
    expect(TestMbid("f66d7266-eb3d-4ef3-b4d8-b7cd992f918b")).toBeValid()
    expect(TestMbid("fe39727a-3d21-4066-9345-3970cbd6cca4")).toBeValid()
    expect(TestMbid("dd430e7f-36ba-49a5-825b-80a525e69190")).toBeValid()
    expect(TestMbid("b1df2cf3-69a9-3bc0-be44-f71e79b27a22")).toBeValid()
    expect(TestMbid("46d8f693-52e4-4d03-936f-7ca8459019a7")).toBeValid()
    expect(TestMbid("300676c6-6e63-4d4d-9084-089efcd0113f")).toBeValid()
    expect(TestMbid("478558f9-a951-4067-ad91-e83f6ba63e74")).toBeValid()

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

@JvmInline
private value class TestMbid(override val value: String) : Mbid
