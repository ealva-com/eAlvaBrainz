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

@file:Suppress("Indentation")

package com.ealva.brainzsvc.service

import com.ealva.brainzsvc.service.BrainzMessage.BrainzExceptionMessage
import com.ealva.brainzsvc.service.BrainzMessage.BrainzStatusMessage.BrainzErrorCodeMessage
import com.ealva.brainzsvc.service.BrainzMessage.BrainzStatusMessage.BrainzNullReturn
import com.ealva.ealvabrainz.brainz.MusicBrainz
import com.ealva.ealvabrainz.brainz.data.BrainzError
import com.ealva.ealvabrainz.brainz.data.CoverArtRelease
import com.ealva.ealvabrainz.brainz.data.RecordingList
import com.ealva.ealvabrainz.brainz.data.Release
import com.ealva.ealvabrainz.brainz.data.Release.Companion.NullRelease
import com.ealva.ealvabrainz.brainz.data.ReleaseGroup
import com.ealva.ealvabrainz.brainz.data.ReleaseGroupMbid
import com.ealva.ealvabrainz.brainz.data.ReleaseList
import com.ealva.ealvabrainz.brainz.data.ReleaseMbid
import com.ealva.ealvabrainz.brainz.data.joinToString
import com.ealva.ealvabrainz.common.AlbumTitle
import com.ealva.ealvabrainz.common.ArtistName
import com.ealva.ealvabrainz.common.BrainzException
import com.ealva.ealvabrainz.common.BrainzInvalidStatusException
import com.ealva.ealvabrainz.common.Limit
import com.ealva.ealvabrainz.common.Offset
import com.ealva.ealvabrainz.common.buildQueryMap
import com.ealva.ealvabrainz.common.toAlbumTitle
import com.ealva.ealvabrainz.common.toArtistName
import com.ealva.ealvabrainz.common.toRecordingTitle
import com.ealva.ealvabrainz.test.shared.MainCoroutineRule
import com.ealva.ealvabrainz.test.shared.runBlockingTest
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.get
import com.nhaarman.expect.expect
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.doThrow
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Rule
import org.junit.Test
import retrofit2.Response

public class MusicBrainzServiceTest {
  @get:Rule
  public var coroutineRule: MainCoroutineRule = MainCoroutineRule()

  private val dummyException = RuntimeException("I O, let's go!")

  @OptIn(ExperimentalCoroutinesApi::class)
  @Test
  public fun `test findRelease`(): Unit = coroutineRule.runBlockingTest {
    doTestFindRelease(null, null)
    doTestFindRelease(Limit(20), null)
    doTestFindRelease(null, Offset(10))
    doTestFindRelease(Limit(100), Offset(10))
  }

  @OptIn(ExperimentalCoroutinesApi::class)
  @Test
  public fun `test findRelease on thrown exception`(): Unit = coroutineRule.runBlockingTest {
    val artistName = ArtistName("David Bowie")
    val albumName = AlbumTitle("The Man Who Sold the World")
    val query =
      """(artist:"${artistName.value}" AND release:"${albumName.value}")"""
    val mockBrainz = mock<MusicBrainz> {
      onBlocking {
        findRelease(query)
      } doThrow BrainzException("msg", dummyException)
    }
    val service = makeServiceForTest(mockBrainz)
    expect(service.findRelease { artist(artistName) and release(albumName) })
      .toBeInstanceOf<Err<BrainzExceptionMessage>> { result ->
        expect(result.error).toBeInstanceOf<BrainzExceptionMessage> { msg ->
          expect(msg.ex).toBeInstanceOf<BrainzException> { ex ->
            expect(ex.cause).toBeTheSameAs(dummyException)
          }
        }
      }
  }

  @OptIn(ExperimentalCoroutinesApi::class)
  @Test
  public fun `test lookupRelease`(): Unit = coroutineRule.runBlockingTest {
    val mbid = ReleaseMbid("938cef50-de9a-3ced-a1fe-bdfbd3bc4315")
    val mockBrainz = mock<MusicBrainz> {
      onBlocking { lookupRelease(mbid.value) } doReturn makeSuccess(NullRelease)
    }
    val service = makeServiceForTest(mockBrainz)
    val release: BrainzResult<Release> = service.lookupRelease(mbid)
    expect(release).toBe(Ok(NullRelease))
  }

  @OptIn(ExperimentalCoroutinesApi::class)
  @Test
  public fun `test lookupRelease with includes`(): Unit = coroutineRule.runBlockingTest {
    val dummy = Release("dummy")
    val mbid = ReleaseMbid("938cef50-de9a-3ced-a1fe-bdfbd3bc4315")
    val mockBrainz = mock<MusicBrainz> {
      onBlocking { lookupRelease(mbid.value) } doReturn makeSuccess(dummy)
    }
    val service = makeServiceForTest(mockBrainz)
    val result = service.lookupRelease(mbid)
    expect(result).toBe(Ok(dummy))
    expect(result.get()).toBeTheSameAs(dummy)
  }

  @OptIn(ExperimentalCoroutinesApi::class)
  @Test
  public fun `test lookupReleaseGroup status-include mismatch`(): Unit =
    coroutineRule.runBlockingTest {
      val mbid = ReleaseGroupMbid("938cef50-de9a-3ced-a1fe-bdfbd3bc4315")
      val mockBrainz = mock<MusicBrainz>() // should not be called
      val service = makeServiceForTest(mockBrainz)
      // expect lookupReleaseGroup to throw
      expect(
        service.lookupReleaseGroup(mbid) {
          status(*Release.Status.values())
        }
      ).toBeInstanceOf<Err<BrainzExceptionMessage>> { result ->
        expect(result.error).toBeInstanceOf<BrainzExceptionMessage> { error ->
          expect(error.ex).toBeInstanceOf<BrainzInvalidStatusException>()
        }
      }
    }

  @OptIn(ExperimentalCoroutinesApi::class)
  @Test
  public fun `test lookupReleaseGroup status used`(): Unit = coroutineRule.runBlockingTest {
    val dummy = ReleaseGroup(title = "dummy")
    val mbid = ReleaseGroupMbid("938cef50-de9a-3ced-a1fe-bdfbd3bc4315")
    val allStatus = Release.Status.values()
    val status = allStatus.toSet().joinToString()
    val mockBrainz = mock<MusicBrainz> {
      onBlocking {
        lookupReleaseGroup(
          mbid.value,
          buildQueryMap {
            put("inc", "releases")
            put("status", status)
          }
        )
      } doReturn makeSuccess(dummy)
    }
    val service = makeServiceForTest(mockBrainz)
    expect(
      service.lookupReleaseGroup(mbid) {
        include(ReleaseGroup.Include.Releases)
        status(*Release.Status.values())
      }
    ).toBeInstanceOf<Ok<ReleaseGroup>> { result ->
      expect(result.value).toBeTheSameAs(dummy)
    }
  }

  @OptIn(ExperimentalCoroutinesApi::class)
  @Test
  public fun `test find recording query string`(): Unit = coroutineRule.runBlockingTest {
    val dummy = RecordingList()
    val recordingName = "Her Majesty".toRecordingTitle()
    val albumName = "Abbey Road".toAlbumTitle()
    val artistName = "The Beatles".toArtistName()
    val query =
      """(recording:"${recordingName.value}" AND artist:"${artistName.value}" """ +
        """AND release:"${albumName.value}")"""
    val mockBrainz = mock<MusicBrainz> {
      onBlocking { findRecording(query, null, null) } doReturn makeSuccess(dummy)
    }
    val service = makeServiceForTest(mockBrainz)
    expect(
      service.findRecording {
        recording(recordingName) and artist(artistName) and release(albumName)
      }
    ).toBeInstanceOf<Ok<RecordingList>> { result ->
      expect(result.value).toBeTheSameAs(dummy)
    }
  }

  @OptIn(ExperimentalCoroutinesApi::class)
  @Test
  public fun `test direct brainz call`(): Unit = coroutineRule.runBlockingTest {
    val dummy = Release(title = "dummy")
    val mbid = ReleaseMbid("938cef50-de9a-3ced-a1fe-bdfbd3bc4315")
    val mockBrainz = mock<MusicBrainz> {
      onBlocking { lookupRelease(mbid.value) } doReturn makeSuccess(dummy)
    }
    val service = makeServiceForTest(mockBrainz)
    val response = service.brainz {
      lookupRelease(mbid.value)
    }
    expect(response).toBeInstanceOf<Ok<Release>> {
      expect(it.value).toBe(dummy)
    }
  }

  @OptIn(ExperimentalCoroutinesApi::class)
  @Test
  public fun `test direct brainz call error response`(): Unit = coroutineRule.runBlockingTest {
    val mbid = ReleaseMbid("938cef50-de9a-3ced-a1fe-bdfbd3bc4315")
    val mockBrainz = mock<MusicBrainz> {
      onBlocking {
        lookupRelease(mbid.value)
      } doReturn Response.error(404, notFoundBody.toResponseBody())
    }
    val service = makeServiceForTest(mockBrainz)
    expect(
      service.brainz {
        lookupRelease(mbid.value)
      }
    ).toBeInstanceOf<Err<BrainzErrorMessage>> { result ->
      val error: BrainzError = result.error.error
      expect(error.error).toBe("404")
      expect(error.help).toBe("Not found")
    }
  }

  @OptIn(ExperimentalCoroutinesApi::class)
  @Test
  public fun `test direct brainz call exception`(): Unit = coroutineRule.runBlockingTest {
    val mbid = ReleaseMbid("938cef50-de9a-3ced-a1fe-bdfbd3bc4315")
    val dummyEx = IllegalStateException()
    val mockBrainz = mock<MusicBrainz> {
      onBlocking { lookupRelease(mbid.value) } doThrow dummyEx
    }
    val service = makeServiceForTest(mockBrainz)
    expect(
      service.brainz {
        lookupRelease(mbid.value)
      }
    ).toBeInstanceOf<Err<BrainzExceptionMessage>> { result ->
      expect(result.error).toBeInstanceOf<BrainzExceptionMessage> { msg ->
        expect(msg.ex).toBeTheSameAs(dummyEx)
      }
    }
  }

  @OptIn(ExperimentalCoroutinesApi::class)
  @Test
  public fun `test direct brainz call unknown error response`(): Unit =
    coroutineRule.runBlockingTest {
      val mbid = ReleaseMbid("938cef50-de9a-3ced-a1fe-bdfbd3bc4315")
      val mockBrainz = mock<MusicBrainz> {
        onBlocking {
          lookupRelease(mbid.value)
        } doReturn Response.error(404, "won't work".toResponseBody())
      }
      val service = makeServiceForTest(mockBrainz)
      expect(
        service.brainz { lookupRelease(mbid.value) }
      ).toBeInstanceOf<Err<BrainzErrorCodeMessage>> { result ->
        expect(result.error).toBeInstanceOf<BrainzErrorCodeMessage> { msg ->
          expect(msg.statusCode).toBe(404)
        }
      }
    }

  private suspend fun doTestFindRelease(limit: Limit?, offset: Offset?) {
    val dummy = ReleaseList()
    val artistName = ArtistName("David Bowie")
    val albumName = AlbumTitle("The Man Who Sold the World")
    val query =
      """(artist:"${artistName.value}" AND release:"${albumName.value}")"""
    val mockBrainz = mock<MusicBrainz> {
      onBlocking { findRelease(query, limit?.value, offset?.value) } doReturn makeSuccess(dummy)
    }
    val service = makeServiceForTest(mockBrainz)
    expect(
      service.findRelease(limit, offset) {
        artist(artistName) and release(albumName)
      }
    ).toBeInstanceOf<Ok<ReleaseList>> { result ->
      verify(mockBrainz, times(1)).findRelease(query, limit?.value, offset?.value)
      expect(result.value).toBe(dummy)
    }
  }

  private fun makeServiceForTest(mockBrainz: MusicBrainz): MusicBrainzService {
    return makeMusicBrainzService(
      mockBrainz,
      NullCoverArtService,
      coroutineRule.testDispatcher
    )
  }

  private fun <T> makeSuccess(t: T) = Response.success(200, t)
}

private object NullCoverArtService : CoverArtService {
  override suspend fun getReleaseArt(mbid: ReleaseMbid): Result<CoverArtRelease, BrainzMessage> {
    return Err(BrainzNullReturn(0))
  }

  override suspend fun getReleaseGroupArt(
    mbid: ReleaseGroupMbid
  ): Result<CoverArtRelease, BrainzMessage> {
    return Err(BrainzNullReturn(0))
  }
}

private const val notFoundBody =
  """
{
  "error": "404",
  "help": "Not found"
}
"""
