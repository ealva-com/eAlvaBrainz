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

package com.ealva.ealvabrainz.service

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ealva.ealvabrainz.MainCoroutineRule
import com.ealva.ealvabrainz.brainz.MusicBrainz
import com.ealva.ealvabrainz.brainz.data.CoverArtRelease
import com.ealva.ealvabrainz.brainz.data.Release
import com.ealva.ealvabrainz.brainz.data.Release.Companion.NullRelease
import com.ealva.ealvabrainz.brainz.data.ReleaseList
import com.ealva.ealvabrainz.brainz.data.toReleaseMbid
import com.ealva.ealvabrainz.common.AlbumName
import com.ealva.ealvabrainz.common.ArtistName
import com.ealva.ealvabrainz.runBlockingTest
import com.nhaarman.expect.expect
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.doThrow
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Response
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class MusicBrainzServiceTest {
  @get:Rule var coroutineRule = MainCoroutineRule()

  private val context = ApplicationProvider.getApplicationContext<Context>()

  @UseExperimental(ExperimentalCoroutinesApi::class)
  @Test
  fun `test findRelease`() = coroutineRule.runBlockingTest {
    doTestFindRelease(null, null)
    doTestFindRelease(20, null)
    doTestFindRelease(null, 10)
    doTestFindRelease(100, 10)
  }

  @UseExperimental(ExperimentalCoroutinesApi::class)
  @Test(expected = MusicBrainzException::class)
  fun `test findRelease on thrown IOException`() = coroutineRule.runBlockingTest {
    val artistName = ArtistName("David Bowie")
    val albumName = AlbumName("The Man Who Sold the World")
    val query = """artist:"${artistName.value}" AND release:"${albumName.value}""""
    val mockBrainz = mock<MusicBrainz> {
      onBlocking {
        findRelease(query)
      } doThrow MusicBrainzException("msg", IOException("I O, let's go!"))
    }
    val service = makeServiceForTest(mockBrainz)
    service.findRelease(artistName, albumName)
  }

  @UseExperimental(ExperimentalCoroutinesApi::class)
  @Test
  fun `test lookupRelease`() = coroutineRule.runBlockingTest {
    val mbid = "938cef50-de9a-3ced-a1fe-bdfbd3bc4315".toReleaseMbid()
    val mockBrainz = mock<MusicBrainz> {
      onBlocking { lookupRelease(mbid.value, null) } doReturn makeSuccess(NullRelease)
    }
    val service = makeServiceForTest(mockBrainz)
    val release = service.lookupRelease(mbid)
    expect(release).toNotBeNull()
  }

  @UseExperimental(ExperimentalCoroutinesApi::class)
  @Test
  fun `test lookupRelease with includes`() = coroutineRule.runBlockingTest {
    val mbid = "938cef50-de9a-3ced-a1fe-bdfbd3bc4315".toReleaseMbid()
    val mockBrainz = mock<MusicBrainz> {
      onBlocking { lookupRelease(mbid.value, null) } doReturn makeSuccess(NullRelease)
    }
    val service = makeServiceForTest(mockBrainz)
    val release = service.lookupRelease(mbid, listOf())
    expect(release).toNotBeNull()
  }

  @UseExperimental(ExperimentalCoroutinesApi::class)
  @Test
  fun `test direct brainz call`() = coroutineRule.runBlockingTest {
    val mbid = "938cef50-de9a-3ced-a1fe-bdfbd3bc4315".toReleaseMbid()
    val mockBrainz = mock<MusicBrainz> {
      onBlocking { lookupRelease(mbid.value, null) } doReturn makeSuccess(NullRelease)
    }
    val service = makeServiceForTest(mockBrainz)
    val response = service.brainz { brainz ->
      brainz.lookupRelease(mbid.value, null)
    }
    expect(response).toBeInstanceOf<MusicBrainzResult.Success<Release>> {
      expect(it.result).toBe(NullRelease)
    }
  }

  private suspend fun doTestFindRelease(limit: Int?, offset: Int?) {
    val list = ReleaseList()
    val artistName = ArtistName("David Bowie")
    val albumName = AlbumName("The Man Who Sold the World")
    val query = """artist:"${artistName.value}" AND release:"${albumName.value}""""
    val mockBrainz = mock<MusicBrainz> {
      onBlocking {
        findRelease(query, limit, offset)
      } doReturn makeSuccess(list)
    }
    val service = makeServiceForTest(mockBrainz)
    val rc: ReleaseList? = service.findRelease(artistName, albumName, limit, offset)
    verify(mockBrainz, times(1)).findRelease(query, limit, offset)
    expect(rc).toBe(list)
  }

  private fun makeServiceForTest(mockBrainz: MusicBrainz): MusicBrainzService {
    return MusicBrainzService.make(
      context,
      mockBrainz,
      NullCoverArtService,
      coroutineRule.testDispatcher
    )
  }

  private fun <T> makeSuccess(t: T) = Response.success(200, t)
}

object NullCoverArtService : CoverArtService {
  override suspend fun getCoverArtRelease(
    entity: CoverArtService.Entity,
    mbid: String
  ): CoverArtRelease? {
    return null
  }
}
