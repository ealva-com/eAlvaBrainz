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
import com.ealva.brainzsvc.service.BrainzInvalidStatusException
import com.ealva.brainzsvc.service.BrainzInvalidTypeException
import com.ealva.brainzsvc.service.BrainzMessage
import com.ealva.brainzsvc.service.ContextResourceFetcher
import com.ealva.brainzsvc.service.CoverArtService
import com.ealva.brainzsvc.service.MusicBrainzService
import com.ealva.brainzsvc.service.ResourceFetcher
import com.ealva.brainzsvc.service.getErrorString
import com.ealva.ealvabrainz.brainz.data.Area
import com.ealva.ealvabrainz.brainz.data.Artist
import com.ealva.ealvabrainz.brainz.data.BrowseReleaseGroupList
import com.ealva.ealvabrainz.brainz.data.Label
import com.ealva.ealvabrainz.brainz.data.Recording
import com.ealva.ealvabrainz.brainz.data.Release
import com.ealva.ealvabrainz.brainz.data.Release.Subquery
import com.ealva.ealvabrainz.brainz.data.ReleaseGroup
import com.ealva.ealvabrainz.brainz.data.ReleaseGroupMbid
import com.ealva.ealvabrainz.brainz.data.toAreaMbid
import com.ealva.ealvabrainz.brainz.data.toArtistMbid
import com.ealva.ealvabrainz.brainz.data.toLabelMbid
import com.ealva.ealvabrainz.brainz.data.toRecordingMbid
import com.ealva.ealvabrainz.brainz.data.toReleaseGroupMbid
import com.ealva.ealvabrainz.brainz.data.toReleaseMbid
import com.ealva.ealvabrainz.test.shared.MainCoroutineRule
import com.ealva.ealvabrainz.test.shared.runBlockingTest
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.nhaarman.expect.ListMatcher
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
  public fun lookupAreaUnitedKingdom(): Unit = coroutineRule.runBlockingTest {
    withBrainz {
      when (
        val result = lookupArea(UNITED_KINGDOM) {
          misc(Area.Misc.Aliases)
        }
      ) {
        is Ok -> {
          result.value.let { area ->
            expect(area.name).toBe("United Kingdom")
            expect(area.type).toBe("Country")
            expect(area.iso31661Codes[0]).toBe("GB")
          }
        }
        is Err -> fail("Brainz call failed") { failReason(result) }
      }
    }
  }

  @Test
  public fun findReleaseJethroTullAqualung(): Unit = coroutineRule.runBlockingTest {
    withBrainz {
      when (val result = findRelease(JETHRO_TULL, AQUALUNG, 4)) {
        is Ok -> {
          result.value.let { releaseList ->
            expect(releaseList.releases).toHaveSize(4)
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
        val result = lookupRelease(THE_HAFLER_TRIO_RELEASE) {
          subquery(Subquery.ArtistCredits, Subquery.Labels, Subquery.DiscIds, Subquery.Recordings)
        }
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
  public fun lookupReleaseHaflerTrioWithStatus(): Unit = coroutineRule.runBlockingTest {
    withBrainz {
      when (
        val result = lookupRelease(THE_HAFLER_TRIO_RELEASE) {
          status(Release.Status.Official)
        }
      ) {
        is Ok -> {
          result.value.let { release ->
            expect(release.title).toBe("æ³o & h³æ")
            expect(release.releaseEvents).toHaveSize(1)
          }
        }
        is Err -> fail("Brainz call failed") { failReason(result) }
      }
    }
  }

  @Test
  public fun lookupReleaseHaflerTrioWithType(): Unit = coroutineRule.runBlockingTest {
    withBrainz {
      when (
        val result = lookupRelease(THE_HAFLER_TRIO_RELEASE) {
          types(Release.Type.Album)
        }
      ) {
        is Ok -> {
          result.value.let { release ->
            expect(release.title).toBe("æ³o & h³æ")
            expect(release.releaseEvents).toHaveSize(1)
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
  public fun lookup50CentLostTapeReleaseGroup(): Unit = coroutineRule.runBlockingTest {
    withBrainz {
      when (
        val result = lookupReleaseGroup(FITY_CENT_LOST_TAPE_GROUP_MBID) {
          subquery(ReleaseGroup.Subquery.ArtistCredits, ReleaseGroup.Subquery.Releases)
        }
      ) {
        is Ok -> result.value.let { group ->
          expect(group.title).toBe("The Lost Tape")
          expect(group.artistCredit[0].name).toBe("50 Cent")
        }
        is Err -> fail("Brainz call failed") { failReason(result) }
      }
    }
  }

  @Test
  public fun lookup50CentLostTapeReleaseGroupWithType(): Unit = coroutineRule.runBlockingTest {
    withBrainz {
      when (
        val result = lookupReleaseGroup(FITY_CENT_LOST_TAPE_GROUP_MBID) {
          types(Release.Type.Album)
        }
      ) {
        is Ok -> result.value.let { group ->
          expect(group.title).toBe("The Lost Tape")
        }
        is Err -> fail("Brainz call failed") { failReason(result) }
      }
    }
  }

  @Test
  public fun lookup50CentLostTapeReleaseGroupWithStatus(): Unit = coroutineRule.runBlockingTest {
    withBrainz {
      when (
        val result = lookupReleaseGroup(FITY_CENT_LOST_TAPE_GROUP_MBID) {
          status(Release.Status.Official)
        }
      ) {
        is Ok -> fail("Expected Err result")
        is Err -> when (val error = result.error) {
          is BrainzMessage.BrainzExceptionMessage -> {
            expect(error.ex).toBeInstanceOf<BrainzInvalidStatusException>()
          }
          else -> fail("Expected BrainzExceptionMessage")
        }
      }
    }
  }

  @Test
  public fun browseJethroTullReleaseGroups(): Unit = coroutineRule.runBlockingTest {
    withBrainz {
      val limit = 5
      when (val result = browseArtistReleaseGroups(JETHRO_TULL_MBID, limit = limit)) {
        is Ok -> {
          result.value.let { browseList: BrowseReleaseGroupList ->
            expect(browseList.releaseGroups).toHaveSize(limit)
            expect(browseList.releaseGroups[0].title).toBe("Aqualung")
          }
        }
        is Err -> fail("Brainz call failed") { failReason(result) }
      }
    }
  }

  @Test
  public fun browseJethroTullReleaseGroupsWithType(): Unit = coroutineRule.runBlockingTest {
    withBrainz {
      val limit = 5
      when (
        val result =
          browseArtistReleaseGroups(
            JETHRO_TULL_MBID,
            limit = limit,
            type = listOf(Release.Type.Album)
          )
      ) {
        is Ok -> {
          result.value.let { browseList: BrowseReleaseGroupList ->
            expect(browseList.releaseGroups).toHaveSize(limit)
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
      when (val result = browseArtistReleases(THE_BEATLES_MBID, limit = limit, offset = offset)) {
        is Ok -> {
          result.value.let { browseList ->
            expect(browseList.releaseOffset).toBe(offset)
            expect(browseList.releases).toHaveSize(5)
          }
        }
        is Err -> fail("Brainz call failed") { failReason(result) }
      }
    }
  }

  @Test
  public fun browseBeatlesReleasesWithType(): Unit = coroutineRule.runBlockingTest {
    withBrainz {
      val limit = 5
      val offset = 10
      when (
        val result = browseArtistReleases(
          THE_BEATLES_MBID,
          limit,
          offset,
          type = listOf(Release.Type.Album)
        )
      ) {
        is Ok -> {
          result.value.let { browseList ->
            expect(browseList.releaseOffset).toBe(offset)
            expect(browseList.releases).toHaveSize(5)
          }
        }
        is Err -> fail("Brainz call failed") { failReason(result) }
      }
    }
  }

  @Test
  public fun browseBeatlesReleasesWithStatus(): Unit = coroutineRule.runBlockingTest {
    withBrainz {
      val limit = 5
      val offset = 10
      when (
        val result = browseArtistReleases(
          THE_BEATLES_MBID,
          limit,
          offset,
          status = listOf(Release.Status.Official)
        )
      ) {
        is Ok -> {
          result.value.let { browseList ->
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

  @Test
  public fun lookupArtistNirvana(): Unit = coroutineRule.runBlockingTest {
    withBrainz {
      when (
        val result = lookupArtist(NIRVANA_MBID) {
          misc(Artist.Misc.Aliases)
        }
      ) {
        is Ok -> {
          result.value.let { artist ->
            expect(artist.name).toBe("Nirvana")
            expect(artist.aliases).toNotBeEmpty()
          }
        }
        is Err -> fail("Brainz call failed") { failReason(result) }
      }
    }
  }

  @Test
  public fun lookupArtistNirvanaWithType(): Unit = coroutineRule.runBlockingTest {
    withBrainz {
      when (
        val result = lookupArtist(NIRVANA_MBID) {
          types(Release.Type.Album)
        }
      ) {
        is Ok -> fail("Expected Err result")
        is Err -> {
          when (val error = result.error) {
            is BrainzMessage.BrainzExceptionMessage -> {
              expect(error.ex).toBeInstanceOf<BrainzInvalidTypeException>()
            }
            else -> fail("Expected BrainzExceptionMessage")
          }
        }
      }
    }
  }

  @Test
  public fun lookupArtistNirvanaWithStatus(): Unit = coroutineRule.runBlockingTest {
    withBrainz {
      when (
        val result = lookupArtist(NIRVANA_MBID) {
          status(Release.Status.Official)
        }
      ) {
        is Ok -> fail("Expected Err result")
        is Err -> {
          when (val error = result.error) {
            is BrainzMessage.BrainzExceptionMessage -> {
              expect(error.ex).toBeInstanceOf<BrainzInvalidStatusException>()
            }
            else -> fail("Expected BrainzExceptionMessage")
          }
        }
      }
    }
  }

  @Test
  public fun lookupWarpLabel(): Unit = coroutineRule.runBlockingTest {
    withBrainz {
      when (
        val result = lookupLabel(WARP_LABEL) {
          misc(Label.Misc.Aliases)
        }
      ) {
        is Ok -> {
          result.value.let { label ->
            expect(label.name).toBe("Warp")
            expect(label.country).toBe("GB")
            expect(label.aliases[0].name).toBe("Warp Records")
          }
        }
        is Err -> fail("Brainz call failed") { failReason(result) }
      }
    }
  }

  @Test
  public fun lookupRecordingLastAngel(): Unit = coroutineRule.runBlockingTest {
    withBrainz {
      when (
        val result = lookupRecording(LAST_ANGEL_RECORDING) {
          subquery(
            Recording.Subquery.ArtistCredits,
            Recording.Subquery.Isrcs,
            Recording.Subquery.Releases
          )
        }
      ) {
        is Ok -> {
          result.value.let { recording ->
            expect(recording.title).toBe("LAST ANGEL")
            expect(recording.firstReleaseDate).toBe("2007-11-07")
          }
        }
        is Err -> fail("Brainz call failed") { failReason(result) }
      }
    }
  }

//  @Test
//  public fun browseBeatlesRevolverGroupArtists(): Unit = coroutineRule.runBlockingTest {
//    withBrainz {
//      when (val result = browseReleaseGroupArtists(THE_BEATLES_REVOLVER)) {
//        is Ok -> {
//          result.value.let { browseArtistList ->
//            expect(browseArtistList.artists).toHaveSize(1)
//            expect(browseArtistList.artists[0].name).toBe("The Beatles")
//          }
//        }
//        is Err -> fail("Brainz call failed") { failReason(result) }
//      }
//    }
//  }
//
//  @Test
//  public fun browseBeatlesRevolverGroupArtistsWithType(): Unit = coroutineRule.runBlockingTest {
//    withBrainz {
//      when (
//        val result = browseReleaseGroupArtists(
//          THE_BEATLES_REVOLVER,
//          type = listOf(Release.Type.Album)
//        )
//      ) {
//        is Ok -> {
//          result.value.let { browseArtistList ->
//            expect(browseArtistList.artists).toHaveSize(1)
//            expect(browseArtistList.artists[0].name).toBe("The Beatles")
//          }
//        }
//        is Err -> fail("Brainz call failed") { failReason(result) }
//      }
//    }
//  }
//
//  @Test
//  public fun browseBeatlesRevolverGroupArtistsWithStatus(): Unit = coroutineRule.runBlockingTest {
//    withBrainz {
//      when (
//        val result = browseReleaseGroupArtists(
//          THE_BEATLES_REVOLVER,
//          status = listOf(Release.Status.Official)
//        )
//      ) {
//        is Ok -> {
//          result.value.let { browseArtistList ->
//            expect(browseArtistList.artists).toHaveSize(1)
//            expect(browseArtistList.artists[0].name).toBe("The Beatles")
//          }
//        }
//        is Err -> fail("Brainz call failed") { failReason(result) }
//      }
//    }
//  }

  private suspend fun withBrainz(block: suspend MusicBrainzService.() -> Unit) {
    @Suppress("BlockingMethodInNonBlockingContext")
    runBlocking {
      musicBrainzService.block()
    }
  }

  private fun failReason(result: Err<BrainzMessage>): String = result.getErrorString(fetcher)
}

private fun <T> ListMatcher<T>.toNotBeEmpty(message: (() -> Any?)? = null) {
  if (actual == null) {
    fail("Expected value to be empty, but the actual value was null.", message)
  }

  if (actual?.isEmpty() == true) {
    fail("Expected $actual to not be empty.", message)
  }
}

private val JETHRO_TULL = "Jethro Tull".toArtistName()
private val JETHRO_TULL_MBID = "ece57992-dc2e-4f67-a269-fa43626c1a3d".toArtistMbid()
private val AQUALUNG = "Aqualung".toAlbumName()
private val THE_HAFLER_TRIO_RELEASE = "59211ea4-ffd2-4ad9-9a4e-941d3148024a".toReleaseMbid()
private val LED_ZEPPELIN = "Led Zeppelin".toArtistName()
private val HOUSES_OF_THE_HOLY = "Houses of the Holy".toAlbumName()
private val FITY_CENT_LOST_TAPE_GROUP_MBID: ReleaseGroupMbid =
  "c9fdb94c-4975-4ed6-a96f-ef6d80bb7738".toReleaseGroupMbid()
private val THE_BEATLES_MBID = "b10bbbfc-cf9e-42e0-be17-e2c3e1d2600d".toArtistMbid()
private val NIRVANA_MBID = "5b11f4ce-a62d-471e-81fc-a69a8278c7da".toArtistMbid()
// private val THE_BEATLES_REVOLVER = "72d15666-99a7-321e-b1f3-a3f8c09dff9f".toReleaseGroupMbid()
private val WARP_LABEL = "46f0f4cd-8aab-4b33-b698-f459faf64190".toLabelMbid()
private val LAST_ANGEL_RECORDING = "b9ad642e-b012-41c7-b72a-42cf4911f9ff".toRecordingMbid()
private val UNITED_KINGDOM = "8a754a16-0027-3a29-b6d7-2b40ea0481ed".toAreaMbid()
