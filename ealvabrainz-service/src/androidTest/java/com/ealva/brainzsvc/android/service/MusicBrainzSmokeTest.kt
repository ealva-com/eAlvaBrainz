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

package com.ealva.brainzsvc.android.service

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ealva.brainzsvc.common.toAlbumName
import com.ealva.brainzsvc.common.toArtistName
import com.ealva.brainzsvc.service.BrainzMessage
import com.ealva.brainzsvc.service.ContextResourceFetcher
import com.ealva.brainzsvc.service.CoverArtService
import com.ealva.brainzsvc.service.MusicBrainzService
import com.ealva.brainzsvc.service.ResourceFetcher
import com.ealva.brainzsvc.service.getErrorString
import com.ealva.ealvabrainz.brainz.data.BrowseReleaseGroupList
import com.ealva.ealvabrainz.brainz.data.Release.Subquery
import com.ealva.ealvabrainz.brainz.data.ReleaseGroup
import com.ealva.ealvabrainz.brainz.data.ReleaseGroupMbid
import com.ealva.ealvabrainz.brainz.data.toArtistMbid
import com.ealva.ealvabrainz.brainz.data.toReleaseGroupMbid
import com.ealva.ealvabrainz.brainz.data.toReleaseMbid
import com.ealva.ealvabrainz.test.shared.MainCoroutineRule
import com.ealva.ealvabrainz.test.shared.runBlockingTest
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.nhaarman.expect.expect
import com.nhaarman.expect.fail
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

private const val appName = "eAlvaBrainz_test"
private const val appVersion = "0.1"
private const val contactEmail = "musicbrainz@ealva.com"

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
public class MusicBrainzSmokeTest {

  @get:Rule
  public var coroutineRule: MainCoroutineRule = MainCoroutineRule()

  private lateinit var appCtx: Context
  private lateinit var coverArtService: CoverArtService
  private lateinit var musicBrainzService: MusicBrainzService
  private lateinit var fetcher: ResourceFetcher

  @Before
  public fun setup() {
    appCtx = ApplicationProvider.getApplicationContext()
    fetcher = ContextResourceFetcher(appCtx)
    println("make CoverArt")
    coverArtService = CoverArtService(
      ctx = appCtx,
      appName = appName,
      appVersion = appVersion,
      contactEmail = contactEmail,
      resourceFetcher = fetcher,
      dispatcher = coroutineRule.testDispatcher
    )
    println("make musicbrainz")
    musicBrainzService = MusicBrainzService(
      ctx = appCtx,
      appName = appName,
      appVersion = appVersion,
      contact = contactEmail,
      coverArt = coverArtService,
      dispatcher = coroutineRule.testDispatcher,
    )
    // ensure we do not exceed rate limiting as we are creating the services each time and not
    // applying rate limiting between calls in the service itself
    Thread.sleep(2000)
  }

  @Test
  public fun findReleaseJethroTullAqualung(): Unit = coroutineRule.runBlockingTest {
    withBrainz {
      when (val result = findRelease(JETHRO_TULL, AQUALUNG, 4)) {
        is Ok -> {
          result.value.let { releaseList ->
            expect(releaseList.count).toBeGreaterThan(40) // 42 last I checked
            releaseList.releases.let { releases ->
              expect(releases).toHaveSize(4)
            }
          }
        }
        is Err -> fail("Brainz call failed") { failReason(result) }
      }
    }
  }

  @Test
  public fun findReleaseJethroTullNotFound(): Unit = coroutineRule.runBlockingTest {
    withBrainz {
      when (val result = findRelease(JETHRO_TULL, "not found".toAlbumName(), 4)) {
        is Ok -> {
          result.value.let { releaseList ->
            expect(releaseList.count).toBe(0) // expect nothing found
          }
        }
        is Err -> fail("Brainz call failed") { failReason(result) }
      }
    }
  }

  @Test
  public fun lookupReleaseHaflerTrioRelease(): Unit = coroutineRule.runBlockingTest {
    withBrainz {
      when (
        val result = lookupRelease(
          THE_HAFLER_TRIO_RELEASE,
          listOf(Subquery.ArtistCredits, Subquery.Labels, Subquery.DiscIds, Subquery.Recordings)
        )
      ) {
        is Ok -> {
          result.value.let { release ->
            expect(release.title).toBe("æ³o & h³æ")
            expect(release.releaseEvents).toHaveSize(1)
            expect(release.media).toHaveSize(2)
          }
        }
        is Err -> fail("Brainz call failed") { failReason(result) }
      }
    }
  }

  @Test
  public fun findReleaseGroupZeppelinHousesOfTheHoly(): Unit = coroutineRule.runBlockingTest {
    withBrainz {
      when (val result = findReleaseGroup(LED_ZEPPELIN, HOUSES_OF_THE_HOLY)) {
        is Ok -> {
          result.value.let { groupList ->
            expect(groupList.count).toBe(2)
            expect(groupList.releaseGroups).toHaveSize(2)
            expect(groupList.releaseGroups[0].title).toBe(HOUSES_OF_THE_HOLY.value)
            expect(groupList.releaseGroups[0].artistCredit[0].name).toBe(LED_ZEPPELIN.value)
          }
        }
        is Err -> fail("Brainz call failed") { failReason(result) }
      }
    }
  }

  @Test
  public fun find50CentLostTapeReleaseGroup(): Unit = coroutineRule.runBlockingTest {
    withBrainz {
      when (
        val result = lookupReleaseGroup(
          FITY_CENT_LOST_TAPE_GROUP,
          listOf(ReleaseGroup.Subquery.ArtistCredits, ReleaseGroup.Subquery.Releases)
        )
      ) {
        is Ok -> {
          result.value.let { group ->
            expect(group.title).toBe("The Lost Tape")
            expect(group.artistCredit[0].name).toBe("50 Cent")
          }
        }
        is Err -> fail("Brainz call failed") { failReason(result) }
      }
    }
  }

  @Test
  public fun browseJethroTullReleaseGroups(): Unit = coroutineRule.runBlockingTest {
    withBrainz {
      val limit = 5
      when (val result = browseReleaseGroups(JETHRO_TULL_MBID, limit = limit)) {
        is Ok -> {
          result.value.let { browseList: BrowseReleaseGroupList ->
            expect(browseList.releaseGroupCount).toBeGreaterThan(140) // 141 last I checked
            expect(browseList.releaseGroups).toHaveSize(limit)
            expect(browseList.releaseGroups[0].title).toBe("Aqualung")
          }
        }
        is Err -> fail("Brainz call failed") { failReason(result) }
      }
    }
  }

  @Test
  public fun browseBeatlesReleases(): Unit = coroutineRule.runBlockingTest {
    withBrainz {
      val limit = 5
      val offset = 100
      when (val result = browseReleases(THE_BEATLES, limit = limit, offset = offset)) {
        is Ok -> {
          result.value.let { browseList ->
            expect(browseList.releaseCount).toBeGreaterThan(2480) // 2490 last count
            expect(browseList.releaseOffset).toBe(offset)
            expect(browseList.releases).toHaveSize(5)
          }
        }
        is Err -> fail("Brainz call failed") { failReason(result) }
      }
    }
  }

  @Test
  public fun findArtistJethroTull(): Unit = coroutineRule.runBlockingTest {
    withBrainz {
      when (val result = findArtist(JETHRO_TULL, 4)) {
        is Ok -> {
          result.value.let { artistList ->
            expect(artistList.count).toBeGreaterThan(0)
            result.value.artists.let { artists ->
              expect(artists).toHaveSize(1)
              val artist = artists[0]
              expect(artist.id.toArtistMbid()).toBe(JETHRO_TULL_MBID)
            }
          }
        }
        is Err -> fail("Brainz call failed") { failReason(result) }
      }
    }
  }

  private suspend fun withBrainz(block: suspend MusicBrainzService.() -> Unit) {
    @Suppress("BlockingMethodInNonBlockingContext")
    runBlocking {
      musicBrainzService.block()
    }
  }

  private fun failReason(result: Err<BrainzMessage>): String = result.getErrorString(fetcher)
}

private val JETHRO_TULL = "Jethro Tull".toArtistName()
private val JETHRO_TULL_MBID = "ece57992-dc2e-4f67-a269-fa43626c1a3d".toArtistMbid()
private val AQUALUNG = "Aqualung".toAlbumName()
private val THIS_WAS = "This Was".toAlbumName()
private val THE_HAFLER_TRIO_RELEASE = "59211ea4-ffd2-4ad9-9a4e-941d3148024a".toReleaseMbid()
private val LED_ZEPPELIN = "Led Zeppelin".toArtistName()
private val HOUSES_OF_THE_HOLY = "Houses of the Holy".toAlbumName()
private val FITY_CENT_LOST_TAPE_GROUP: ReleaseGroupMbid =
  "c9fdb94c-4975-4ed6-a96f-ef6d80bb7738".toReleaseGroupMbid()
private val THE_BEATLES = "b10bbbfc-cf9e-42e0-be17-e2c3e1d2600d".toArtistMbid()
