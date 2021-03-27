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
import androidx.test.filters.LargeTest
import com.ealva.brainzsvc.service.BrainzErrorMessage
import com.ealva.brainzsvc.service.BrainzMessage
import com.ealva.brainzsvc.service.BuildConfig
import com.ealva.brainzsvc.service.ContextResourceFetcher
import com.ealva.brainzsvc.service.CoverArtService
import com.ealva.brainzsvc.service.Credentials
import com.ealva.brainzsvc.service.CredentialsProvider
import com.ealva.brainzsvc.service.MusicBrainzService
import com.ealva.brainzsvc.service.Password
import com.ealva.brainzsvc.service.ResourceFetcher
import com.ealva.brainzsvc.service.UserName
import com.ealva.ealvabrainz.browse.ArtistBrowse
import com.ealva.ealvabrainz.browse.CollectionBrowse
import com.ealva.ealvabrainz.browse.EventBrowse
import com.ealva.ealvabrainz.browse.LabelBrowse
import com.ealva.ealvabrainz.browse.PlaceBrowse
import com.ealva.ealvabrainz.browse.RecordingBrowse
import com.ealva.ealvabrainz.browse.ReleaseBrowse
import com.ealva.ealvabrainz.browse.ReleaseGroupBrowse.BrowseOn
import com.ealva.ealvabrainz.browse.WorkBrowse
import com.ealva.brainzsvc.service.getErrorString
import com.ealva.ealvabrainz.brainz.data.BrowseReleaseGroupList
import com.ealva.ealvabrainz.brainz.data.Collection
import com.ealva.ealvabrainz.brainz.data.EventCollection
import com.ealva.ealvabrainz.brainz.data.Release
import com.ealva.ealvabrainz.common.AreaMbid
import com.ealva.ealvabrainz.common.ArtistMbid
import com.ealva.ealvabrainz.common.EditorName
import com.ealva.ealvabrainz.common.Limit
import com.ealva.ealvabrainz.common.Offset
import com.ealva.ealvabrainz.common.ReleaseGroupMbid
import com.ealva.ealvabrainz.test.shared.MainCoroutineRule
import com.ealva.ealvabrainz.test.shared.runBlockingTest
import com.ealva.ealvabrainz.test.shared.toHaveAny
import com.ealva.ealvabrainz.test.shared.toNotBeEmpty
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

/**
 * An integration test with MusicBrainz servers. This test suite purposefully sleeps 2 seconds
 * between calls to stay far away from rate limiting and not tax the MusicBrainz FREE (to us, an
 * open source library) services. Given that, please don't run this test often. Preferably, do all
 * your work, do unit tests, and save this test suite for a (hopefully) single run before committing
 * code.
 */
@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
@LargeTest
@Suppress("LargeClass")
public class MusicBrainzBrowseSmokeTest {

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
      credentialsProvider = object : CredentialsProvider {
        override val credentials: Credentials =
          Credentials(UserName(BuildConfig.BRAINZ_USERNAME), Password(BuildConfig.BRAINZ_PASSWORD))
      },
      dispatcher = coroutineRule.testDispatcher,
    )
    // ensure we do not exceed rate limiting as we are creating the services each time and not
    // applying rate limiting between calls in the service itself
    Thread.sleep(2000)
  }

  @Test
  public fun browseArtistsBeatlesRevolver(): Unit = coroutineRule.runBlockingTest {
    withBrainz {
      when (val result = browseArtists(ArtistBrowse.BrowseOn.ReleaseGroup(THE_BEATLES_REVOLVER))) {
        is Ok -> result.value.let { browseArtistList ->
          expect(browseArtistList.artists).toHaveSize(1)
          expect(browseArtistList.artists[0].name).toBe("The Beatles")
        }
        is Err -> fail("Brainz call failed") { failReason(result) }
      }
    }
  }

  @Test
  public fun browseArtistsWithStatusBeatlesRevolver(): Unit = coroutineRule.runBlockingTest {
    withBrainz {
      when (
        val result = browseArtists(ArtistBrowse.BrowseOn.ReleaseGroup(THE_BEATLES_REVOLVER)) {
          status(Release.Status.Official)
        }
      ) {
        is Ok -> fail("Expected an error from the server")
        is Err -> {
          expect(result.error).toBeInstanceOf<BrainzErrorMessage> { errorMessage ->
            expect(errorMessage.error.error).toContain("status is not a valid parameter")
          }
        }
      }
    }
  }

  @Test
  public fun browseCollectionsForEditor(): Unit = coroutineRule.runBlockingTest {
    withBrainz {
      when (
        val result = browseCollections(
          CollectionBrowse.BrowseOn.Editor(EditorName(BuildConfig.BRAINZ_USERNAME))
        ) {
          include(Collection.Browse.UserCollections)
        }
      ) {
        is Ok -> result.value.let { browseList ->
          expect(browseList.collections).toNotBeEmpty { "Expected some collections" }
          // Test for both default collections each MusicBrainz user has
          expect(browseList.collections).toHaveAny(
            { " Didn't contain Maybe attending of type EventCollection" }
          ) { collection ->
            collection.name == "Maybe attending" && collection is EventCollection
          }
          expect(browseList.collections).toHaveAny(
            { " Didn't contain Attending of type EventCollection" }
          ) { collection ->
            collection.name == "Attending" && collection is EventCollection
          }
        }
        is Err -> fail("Brainz call failed") { failReason(result) }
      }
    }
  }

  @Test
  public fun browseEventsForMetallica(): Unit = coroutineRule.runBlockingTest {
    withBrainz {
      val metallicaMbid = ArtistMbid("65f4f0c5-ef9e-490c-aee3-909e7ae6b2ab")
      val limit = Limit(5)
      when (
        val result = browseEvents(
          EventBrowse.BrowseOn.Artist(metallicaMbid),
          limit
        )
      ) {
        is Ok -> result.value.let { browseList ->
          expect(browseList.eventCount).toBeGreaterThan(39) // 40 last checked
          expect(browseList.events).toHaveSize(limit.value)
          expect(browseList.events).toHaveAny({ "Metallica at UIC Pavilion" }) {
            it.name == "Metallica at UIC Pavilion"
          }
          expect(browseList.events).toHaveAny(
            { " Expected to contain Monsters of Rock Pforzheim 1987 Festival " }
          ) {
            it.type == "Festival" && it.name == "Monsters of Rock Pforzheim 1987"
          }
        }
        is Err -> fail("Brainz call failed") { failReason(result) }
      }
    }
  }

  @Test
  public fun browseLabelsForArea(): Unit = coroutineRule.runBlockingTest {
    withBrainz {
      val unitedKingdom = AreaMbid("8a754a16-0027-3a29-b6d7-2b40ea0481ed")
      val limit = Limit(24)
      when (
        val result = browseLabels(LabelBrowse.BrowseOn.Area(unitedKingdom), limit)
      ) {
        is Ok -> result.value.let { browseList ->
          expect(browseList.labelCount).toBeGreaterThan(15350) // last checked was 15359
          expect(browseList.labels).toHaveSize(limit.value)
          expect(browseList.labels).toHaveAny { it.name == "[Detail] Recordings" }
          expect(browseList.labels).toHaveAny { it.name == "@10footclown" }
        }
        is Err -> fail("Brainz call failed") { failReason(result) }
      }
    }
  }

  @Test
  public fun browsePlacesForAreaUnitedKingdom(): Unit = coroutineRule.runBlockingTest {
    withBrainz {
      val unitedKingdom = AreaMbid("8a754a16-0027-3a29-b6d7-2b40ea0481ed")
      val limit = Limit(25)
      when (
        val result = browsePlaces(PlaceBrowse.BrowseOn.Area(unitedKingdom), limit)
      ) {
        is Ok -> result.value.let { browseList ->
          expect(browseList.places).toHaveSize(limit.value)
          expect(browseList.placeCount).toBeGreaterThan(4200) // 4206 last count
          expect(browseList.places).toHaveAny {
            it.name == "2khz Studio" && it.type == "Studio"
          }
          expect(browseList.places).toHaveAny {
            it.name == "13th Note Club" && it.type == "Venue"
          }
        }
        is Err -> fail("Brainz call failed") { failReason(result) }
      }
    }
  }

  @Test
  public fun browseReleasesBeatles(): Unit = coroutineRule.runBlockingTest {
    withBrainz {
      val limit = Limit(5)
      val offset = Offset(100)
      when (
        val result = browseReleases(
          ReleaseBrowse.BrowseOn.Artist(THE_BEATLES_MBID),
          limit,
          offset
        )
      ) {
        is Ok -> result.value.let { browseList ->
          expect(browseList.releaseOffset).toBe(offset.value)
          expect(browseList.releases).toHaveSize(5)
        }
        is Err -> fail("Brainz call failed") { failReason(result) }
      }
    }
  }

  @Test
  public fun browseRecordingsOfNirvana(): Unit = coroutineRule.runBlockingTest {
    withBrainz {
      val nirvana = ArtistMbid("5b11f4ce-a62d-471e-81fc-a69a8278c7da")
      val limit = Limit(5)
      when (val result = browseRecordings(RecordingBrowse.BrowseOn.Artist(nirvana), limit)) {
        is Ok -> result.value.let { browseList ->
          expect(browseList.recordingCount).toBeGreaterThan(13620) // 13622 last check
          expect(browseList.recordings).toHaveSize(limit.value)
          expect(browseList.recordings).toHaveAny { it.title == "(New Wave) Polly" }
        }
        is Err -> fail("Brainz call failed") { failReason(result) }
      }
    }
  }

  @Test
  public fun browseReleasesWithTypeBeatles(): Unit = coroutineRule.runBlockingTest {
    withBrainz {
      val limit = Limit(5)
      val offset = Offset(10)
      when (
        val result = browseReleases(
          ReleaseBrowse.BrowseOn.Artist(THE_BEATLES_MBID),
          limit,
          offset
        ) {
          types(Release.Type.Album)
        }
      ) {
        is Ok -> result.value.let { browseList ->
          expect(browseList.releaseOffset).toBe(offset.value)
          expect(browseList.releases).toHaveSize(5)
        }
        is Err -> fail("Brainz call failed") { failReason(result) }
      }
    }
  }

  @Test
  public fun browseReleasesWithStatusBeatles(): Unit = coroutineRule.runBlockingTest {
    withBrainz {
      val limit = Limit(5)
      val offset = Offset(10)
      when (
        val result = browseReleases(
          ReleaseBrowse.BrowseOn.Artist(THE_BEATLES_MBID),
          limit,
          offset
        ) {
          status(Release.Status.Official)
        }
      ) {
        is Ok -> result.value.let { browseList ->
          expect(browseList.releaseOffset).toBe(offset.value)
          expect(browseList.releases).toHaveSize(5)
        }
        is Err -> fail("Brainz call failed") { failReason(result) }
      }
    }
  }

  @Test
  public fun browseReleaseGroupsJethroTull(): Unit = coroutineRule.runBlockingTest {
    withBrainz {
      val limit = Limit(5)
      when (val result = browseReleaseGroups(BrowseOn.Artist(JETHRO_TULL_MBID), limit)) {
        is Ok -> result.value.let { browseList: BrowseReleaseGroupList ->
          expect(browseList.releaseGroups).toHaveSize(limit.value)
          expect(browseList.releaseGroups[0].title).toBe("Aqualung")
        }
        is Err -> fail("Brainz call failed") { failReason(result) }
      }
    }
  }

  @Test
  public fun browseReleaseGroupsWithTypeJethroTull(): Unit = coroutineRule.runBlockingTest {
    withBrainz {
      val limit = Limit(5)
      when (
        val result = browseReleaseGroups(BrowseOn.Artist(JETHRO_TULL_MBID), limit = limit) {
          types(Release.Type.Album)
        }
      ) {
        is Ok -> result.value.let { browseList: BrowseReleaseGroupList ->
          expect(browseList.releaseGroups).toHaveSize(limit.value)
        }
        is Err -> fail("Brainz call failed") { failReason(result) }
      }
    }
  }

  @Test
  public fun browseWorksOfTheBeatles(): Unit = coroutineRule.runBlockingTest {
    withBrainz {
      val limit = Limit(5)
      val offset = Offset(100)
      when (
        val result = browseWorks(
          WorkBrowse.BrowseOn.Artist(THE_BEATLES_MBID),
          limit,
          offset
        )
      ) {
        is Ok -> result.value.let { browseList ->
          expect(browseList.workCount).toBeGreaterThan(340) // 343 last checked
          expect(browseList.workOffset).toBe(offset.value)
          expect(browseList.works).toHaveSize(limit.value)
          expect(browseList.works).toHaveAny { it.title == "Her Majesty" }
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

private val JETHRO_TULL_MBID = ArtistMbid("ece57992-dc2e-4f67-a269-fa43626c1a3d")
private val THE_BEATLES_MBID = ArtistMbid("b10bbbfc-cf9e-42e0-be17-e2c3e1d2600d")
private val THE_BEATLES_REVOLVER = ReleaseGroupMbid("72d15666-99a7-321e-b1f3-a3f8c09dff9f")
