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
import com.ealva.brainzsvc.service.BuildConfig
import com.ealva.brainzsvc.service.ContextResourceFetcher
import com.ealva.brainzsvc.service.CoverArtService
import com.ealva.brainzsvc.service.Credentials
import com.ealva.brainzsvc.service.CredentialsProvider
import com.ealva.brainzsvc.service.MusicBrainzService
import com.ealva.brainzsvc.service.Password
import com.ealva.brainzsvc.service.ResourceFetcher
import com.ealva.brainzsvc.service.UserName
import com.ealva.ealvabrainz.brainz.data.Collection
import com.ealva.ealvabrainz.brainz.data.EventCollection
import com.ealva.ealvabrainz.brainz.data.Release
import com.ealva.ealvabrainz.brainz.data.ReleaseGroup
import com.ealva.ealvabrainz.browse.ArtistBrowse
import com.ealva.ealvabrainz.browse.CollectionBrowse
import com.ealva.ealvabrainz.browse.EventBrowse
import com.ealva.ealvabrainz.browse.LabelBrowse
import com.ealva.ealvabrainz.browse.PlaceBrowse
import com.ealva.ealvabrainz.browse.RecordingBrowse
import com.ealva.ealvabrainz.browse.ReleaseBrowse
import com.ealva.ealvabrainz.browse.ReleaseGroupBrowse.BrowseOn
import com.ealva.ealvabrainz.browse.WorkBrowse
import com.ealva.ealvabrainz.brainz.data.AreaMbid
import com.ealva.ealvabrainz.brainz.data.ArtistMbid
import com.ealva.ealvabrainz.common.EditorName
import com.ealva.ealvabrainz.common.Limit
import com.ealva.ealvabrainz.common.Offset
import com.ealva.ealvabrainz.brainz.data.ReleaseGroupMbid
import com.ealva.ealvabrainz.test.shared.MainCoroutineRule
import com.ealva.ealvabrainz.test.shared.runBlockingTest
import com.ealva.ealvabrainz.test.shared.toHaveAny
import com.ealva.ealvabrainz.test.shared.toNotBeEmpty
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
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
 *
 * One or more integration tests require authentication with the MusicBrainz server. The required
 * username and password must be defined in the local.properties file in the root folder of this
 * project. This file is not committed to version control as it's contents are private (obviously).
 * If the file doesn't exist, create it. It would typically look something like:
 * ```text
 * sdk.dir=/home/user/Android/Sdk
 * BRAINZ_USERNAME="my_username"
 * BRAINZ_PASSWORD="my_password"
 * ```
 * where BRAINZ_USERNAME and BRAINZ_PASSWORD are set to your MusicBrainz.org credentials. If you
 * don't have an account, go to https://musicbrainz.org/register and create one. Not mandatory,
 * however not setting these values correctly will result in some tests failing due to
 * authentication errors.
 *
 * Applications which call functions requiring authentication must implement the CredentialsProvider
 * interface and use it when constructing a MusicBrainzService implementation.
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
      appName = BuildConfig.BRAINZ_APP_NAME,
      appVersion = BuildConfig.BRAINZ_APP_VERSION,
      contactEmail = BuildConfig.BRAINZ_CONTACT_EMAIL,
      resourceFetcher = fetcher,
      dispatcher = coroutineRule.testDispatcher
    )
    println("make musicbrainz")
    musicBrainzService = MusicBrainzService(
      ctx = appCtx,
      appName = BuildConfig.BRAINZ_APP_NAME,
      appVersion = BuildConfig.BRAINZ_APP_VERSION,
      contactEmail = BuildConfig.BRAINZ_CONTACT_EMAIL,
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
  public fun browseArtistsBeatlesRevolver(): Unit = brainz {
    browseArtists(ArtistBrowse.BrowseOn.ReleaseGroup(THE_BEATLES_REVOLVER))
      .onSuccess { browseArtistList ->
        expect(browseArtistList.artists).toHaveSize(1)
        expect(browseArtistList.artists[0].name).toBe("The Beatles")
      }
      .onFailure { fail("Brainz call failed") { it.asString(fetcher) } }
  }

  @Test
  public fun browseArtistsWithStatusBeatlesRevolver(): Unit = brainz {
    browseArtists(ArtistBrowse.BrowseOn.ReleaseGroup(THE_BEATLES_REVOLVER)) {
      status(Release.Status.Official)
    }.onSuccess {
      fail("Expected Err result")
    }.onFailure { brainzMessage ->
      expect(brainzMessage).toBeInstanceOf<BrainzErrorMessage> { errorMessage ->
        expect(errorMessage.error.error).toContain("status is not a valid parameter")
      }
    }
  }

  @Test
  /**
   * This test requires authentication as it includes Collection.Browse.UserCollections
   */
  public fun browseCollectionsForEditor(): Unit = brainz {
    browseCollections(
      CollectionBrowse.BrowseOn.Editor(EditorName(BuildConfig.BRAINZ_USERNAME))
    ) {
      include(Collection.Browse.UserCollections)
    }.onSuccess { browseList ->
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
    }.onFailure { fail("Brainz call failed") { it.asString(fetcher) } }
  }

  @Test
  public fun browseEventsForMetallica(): Unit = brainz {
    val limit = Limit(5)
    browseEvents(
      EventBrowse.BrowseOn.Artist(ArtistMbid("65f4f0c5-ef9e-490c-aee3-909e7ae6b2ab")),
      limit
    ).onSuccess { browseList ->
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
    }.onFailure { fail("Brainz call failed") { it.asString(fetcher) } }
  }

  @Test
  public fun browseLabelsForArea(): Unit = brainz {
    val limit = Limit(24)
    browseLabels(
      LabelBrowse.BrowseOn.Area(AreaMbid("8a754a16-0027-3a29-b6d7-2b40ea0481ed")),
      limit
    ).onSuccess { browseList ->
      expect(browseList.labelCount).toBeGreaterThan(15350) // last checked was 15359
      expect(browseList.labels).toHaveSize(limit.value)
      expect(browseList.labels).toHaveAny { it.name == "[Detail] Recordings" }
      expect(browseList.labels).toHaveAny { it.name == "@10footclown" }
    }.onFailure { fail("Brainz call failed") { it.asString(fetcher) } }
  }

  @Test
  public fun browsePlacesForAreaUnitedKingdom(): Unit = brainz {
    val limit = Limit(25)
    browsePlaces(
      PlaceBrowse.BrowseOn.Area(AreaMbid("8a754a16-0027-3a29-b6d7-2b40ea0481ed")),
      limit
    ).onSuccess { browseList ->
      expect(browseList.places).toHaveSize(limit.value)
      expect(browseList.placeCount).toBeGreaterThan(4200) // 4206 last count
      expect(browseList.places).toHaveAny {
        it.name == "2khz Studio" && it.type == "Studio"
      }
      expect(browseList.places).toHaveAny {
        it.name == "13th Note Club" && it.type == "Venue"
      }
    }.onFailure { fail("Brainz call failed") { it.asString(fetcher) } }
  }

  @Test
  public fun browseReleasesBeatles(): Unit = brainz {
    val limit = Limit(5)
    val offset = Offset(100)
    browseReleases(
      ReleaseBrowse.BrowseOn.Artist(THE_BEATLES_MBID),
      limit,
      offset
    ).onSuccess { browseList ->
      expect(browseList.releaseOffset).toBe(offset.value)
      expect(browseList.releases).toHaveSize(5)
    }.onFailure { fail("Brainz call failed") { it.asString(fetcher) } }
  }

  @Test
  public fun browseRecordingsOfNirvana(): Unit = brainz {
    val limit = Limit(5)
    browseRecordings(
      RecordingBrowse.BrowseOn.Artist(ArtistMbid("5b11f4ce-a62d-471e-81fc-a69a8278c7da")),
      limit
    ).onSuccess { browseList ->
      expect(browseList.recordingCount).toBeGreaterThan(13620) // 13622 last check
      expect(browseList.recordings).toHaveSize(limit.value)
      expect(browseList.recordings).toHaveAny { it.title == "(New Wave) Polly" }
    }.onFailure { fail("Brainz call failed") { it.asString(fetcher) } }
  }

  @Test
  public fun browseReleasesWithTypeBeatles(): Unit = brainz {
    val limit = Limit(5)
    val offset = Offset(10)
    browseReleases(
      ReleaseBrowse.BrowseOn.Artist(THE_BEATLES_MBID),
      limit,
      offset
    ) {
      types(ReleaseGroup.Type.Album)
    }.onSuccess { browseList ->
      expect(browseList.releaseOffset).toBe(offset.value)
      expect(browseList.releases).toHaveSize(5)
    }.onFailure { fail("Brainz call failed") { it.asString(fetcher) } }
  }

  @Test
  public fun browseReleasesWithStatusBeatles(): Unit = brainz {
    val limit = Limit(5)
    val offset = Offset(10)
    browseReleases(
      ReleaseBrowse.BrowseOn.Artist(THE_BEATLES_MBID),
      limit,
      offset
    ) {
      status(Release.Status.Official)
    }.onSuccess { browseList ->
      expect(browseList.releaseOffset).toBe(offset.value)
      expect(browseList.releases).toHaveSize(5)
    }.onFailure { fail("Brainz call failed") { it.asString(fetcher) } }
  }

  @Test
  public fun browseReleaseGroupsJethroTull(): Unit = brainz {
    val limit = Limit(5)
    browseReleaseGroups(BrowseOn.Artist(JETHRO_TULL_MBID), limit)
      .onSuccess { browseList ->
        expect(browseList.releaseGroups).toHaveSize(limit.value)
        expect(browseList.releaseGroups[0].title).toBe("Aqualung")
      }
      .onFailure { fail("Brainz call failed") { it.asString(fetcher) } }
  }

  @Test
  public fun browseReleaseGroupsWithTypeJethroTull(): Unit = brainz {
    val limit = Limit(5)
    browseReleaseGroups(BrowseOn.Artist(JETHRO_TULL_MBID), limit) {
      types(ReleaseGroup.Type.Album)
    }.onSuccess { browseList ->
      expect(browseList.releaseGroups).toHaveSize(limit.value)
    }.onFailure { fail("Brainz call failed") { it.asString(fetcher) } }
  }

  @Test
  public fun browseWorksOfTheBeatles(): Unit = brainz {
    val limit = Limit(20)
    val offset = Offset(100)
    browseWorks(
      WorkBrowse.BrowseOn.Artist(THE_BEATLES_MBID),
      limit,
      offset
    ).onSuccess { browseList ->
      expect(browseList.workCount).toBeGreaterThan(340) // 343 last checked
      expect(browseList.workOffset).toBe(offset.value)
      expect(browseList.works).toHaveSize(limit.value)
      expect(browseList.works).toHaveAny({ "Didn't have title Help!" }) { it.title == "Help!" }
    }.onFailure { fail("Brainz call failed") { it.asString(fetcher) } }
  }

  private fun brainz(block: suspend MusicBrainzService.() -> Unit) = coroutineRule.runBlockingTest {
    @Suppress("BlockingMethodInNonBlockingContext")
    runBlocking {
      musicBrainzService.block()
    }
  }
}

private val JETHRO_TULL_MBID = ArtistMbid("ece57992-dc2e-4f67-a269-fa43626c1a3d")
private val THE_BEATLES_MBID = ArtistMbid("b10bbbfc-cf9e-42e0-be17-e2c3e1d2600d")
private val THE_BEATLES_REVOLVER = ReleaseGroupMbid("72d15666-99a7-321e-b1f3-a3f8c09dff9f")
