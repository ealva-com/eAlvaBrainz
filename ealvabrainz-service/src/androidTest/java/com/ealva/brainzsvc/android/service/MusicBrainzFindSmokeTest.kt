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
import com.ealva.brainzsvc.service.getErrorString
import com.ealva.ealvabrainz.brainz.data.Release
import com.ealva.ealvabrainz.common.AlbumTitle
import com.ealva.ealvabrainz.common.AreaName
import com.ealva.ealvabrainz.common.ArtistMbid
import com.ealva.ealvabrainz.common.ArtistName
import com.ealva.ealvabrainz.common.LabelName
import com.ealva.ealvabrainz.common.Limit
import com.ealva.ealvabrainz.common.ReleaseMbid
import com.ealva.ealvabrainz.common.WorkName
import com.ealva.ealvabrainz.common.toAlbumTitle
import com.ealva.ealvabrainz.common.toArtistName
import com.ealva.ealvabrainz.lucene.Term
import com.ealva.ealvabrainz.lucene.inclusive
import com.ealva.ealvabrainz.lucene.or
import com.ealva.ealvabrainz.test.shared.MainCoroutineRule
import com.ealva.ealvabrainz.test.shared.runBlockingTest
import com.ealva.ealvabrainz.test.shared.toHaveAny
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
public class MusicBrainzFindSmokeTest {

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
  public fun findAnnotation(): Unit = coroutineRule.runBlockingTest {
    withBrainz {
      when (
        val result = findAnnotation {
          entity { ReleaseMbid("bdb24cb5-404b-4f60-bba4-7b730325ae47") }
        }
      ) {
        is Ok -> result.value.let { annotationList ->
          expect(annotationList.count).toBeGreaterThan(0)
          expect(annotationList.annotations).toHaveAny { it.type == "release" }
          expect(annotationList.annotations).toHaveAny { it.name == "Pieds nus sur la braise" }
        }
        is Err -> fail("Brainz call failed") { failReason(result) }
      }
    }
  }

  @Test
  public fun findAreaIledeFrance(): Unit = coroutineRule.runBlockingTest {
    withBrainz {
      when (val result = findArea { default { AreaName("Île-de-France") } }) {
        is Ok -> result.value.let { areaList ->
          expect(areaList.count).toBeGreaterThan(0)
          expect(areaList.areas).toHaveAny { it.name == "Île-de-France" }
        }
        is Err -> fail("Brainz call failed") { failReason(result) }
      }
    }
  }

  @Test
  public fun findArtistJethroTullSearch(): Unit = coroutineRule.runBlockingTest {
    withBrainz {
      when (val result = findArtist(limit = Limit(4)) { artist { JETHRO_TULL } }) {
        is Ok -> result.value.let { artistList ->
          expect(artistList.count).toBeGreaterThan(0)
          result.value.artists.let { artists ->
            expect(artists).toHaveSize(1)
            val artist = artists[0]
            expect(ArtistMbid(artist.id)).toBe(JETHRO_TULL_MBID)
          }
        }
        is Err -> fail("Brainz call failed") { failReason(result) }
      }
    }
  }

  @Test
  public fun findCdStubWithTitleDoo(): Unit = coroutineRule.runBlockingTest {
    withBrainz {
      when (val result = findCdStub { title { AlbumTitle("Doo") } }) {
        is Ok -> result.value.let { stubList ->
          expect(stubList.count).toBeGreaterThan(50) // 56 last check
          expect(stubList.cdStubs).toHaveAny { it.title == "Doo- Be - Doo" }
          expect(stubList.cdStubs).toHaveAny { it.title == "Doo-lang Doo-lang" }
          expect(stubList.cdStubs).toHaveAny { it.title == "Doo-Bop" }
        }
        is Err -> fail("Brainz call failed") { failReason(result) }
      }
    }
  }

  @Test
  public fun findEvent(): Unit = coroutineRule.runBlockingTest {
    withBrainz {
      when (val result = findEvent { default { "unique" } }) {
        is Ok -> result.value.let { eventList ->
          expect(eventList.count).toBeGreaterThan(6) // 8 last checked
          expect(eventList.events).toHaveAny {
            it.name == "Dominique A at Le Lieu Unique, April 2012"
          }
          expect(eventList.events).toHaveAny {
            it.name == "Joung & Junique at Maybe's"
          }
        }
        is Err -> fail("Brainz call failed") { failReason(result) }
      }
    }
  }

  @Test
  public fun findInstrument(): Unit = coroutineRule.runBlockingTest {
    withBrainz {
      when (val result = findInstrument { default { "Nose" } }) {
        is Ok -> result.value.let { instrumentList ->
          expect(instrumentList.count).toBeGreaterThan(3)
          expect(instrumentList.instruments).toHaveAny { it.name == "nose whistle" }
          expect(instrumentList.instruments).toHaveAny { it.name == "nose flute" }
        }
        is Err -> fail("Brainz call failed") { failReason(result) }
      }
    }
  }

  @Test
  public fun findLabelDevilsRecords(): Unit = coroutineRule.runBlockingTest {
    withBrainz {
      val labelName = LabelName("Devil's Records")
      when (val result = findLabel(limit = Limit(4)) { default { labelName } }) {
        is Ok -> result.value.let { labelList ->
          expect(labelList.count).toBeGreaterThan(27)
          expect(labelList.labels[0].name).toBe(labelName.value)
        }
        is Err -> fail("Brainz call failed") { failReason(result) }
      }
    }
  }

  @Test
  public fun findPlaceChipping(): Unit = coroutineRule.runBlockingTest {
    withBrainz {
      when (val result = findPlace { default { "chipping" } }) {
        is Ok -> result.value.let { placeList ->
          expect(placeList.count).toBeGreaterThan(0)
          expect(placeList.places).toHaveAny { it.name == "Chipping Norton Recording Studios" }
        }
        is Err -> fail("Brainz call failed") { failReason(result) }
      }
    }
  }

  @Test
  public fun findRecording(): Unit = coroutineRule.runBlockingTest {
    withBrainz {
      when (val result = findRecording { isrc { "GBAHT1600302" } }) {
        is Ok -> result.value.let { recordingList ->
          expect(recordingList.count).toBeGreaterThan(0)
          expect(recordingList.recordings).toHaveAny { it.title == "Blow Your Mind (Mwah)" }
        }
      }
    }
  }

  @Test
  public fun findReleaseJethroTullAqualung(): Unit = coroutineRule.runBlockingTest {
    withBrainz {
      val aqualung = "Aqualung".toAlbumTitle()
      when (
        val result = findRelease(Limit(4)) { artist { JETHRO_TULL } and release { aqualung } }
      ) {
        is Ok -> result.value.let { releaseList ->
          expect(releaseList.releases).toHaveSize(4)
        }
        is Err -> fail("Brainz call failed") { failReason(result) }
      }
    }
  }

  @Test
  public fun findReleaseJethroTullNotFound(): Unit = coroutineRule.runBlockingTest {
    withBrainz {
      when (
        val result = findRelease(Limit(4)) {
          artist { JETHRO_TULL } and release { "not found".toAlbumTitle() }
        }
      ) {
        is Ok -> result.value.let { releaseList ->
          expect(releaseList.count).toBe(0) // expect nothing found
        }
        is Err -> fail("Brainz call failed") { failReason(result) }
      }
    }
  }

  @Test
  public fun findBeatlesAlbums67Through69(): Unit = coroutineRule.runBlockingTest {
    withBrainz {
      when (
        val result = findReleaseGroup {

          artist { ArtistName("The Beatles") } and
            firstReleaseDate { Term("1967") inclusive Term("1969") } and
            primaryType { Release.Type.Album } and
            !secondaryType { Term(Release.Type.Compilation) or Term(Release.Type.Interview) } and
            status { Release.Status.Official }
        }
      ) {
        is Ok -> result.value.let { releaseList ->
          expect(releaseList.count).toBe(5)
          expect(releaseList.releaseGroups).toHaveAny { it.title == "Magical Mystery Tour" }
          expect(releaseList.releaseGroups).toHaveAny {
            it.title == "Sgt. Pepper’s Lonely Hearts Club Band"
          }
          expect(releaseList.releaseGroups).toHaveAny { it.title == "Yellow Submarine" }
          expect(releaseList.releaseGroups).toHaveAny { it.title == "Abbey Road" }
          expect(releaseList.releaseGroups).toHaveAny { it.title == "The Beatles" }
        }
        is Err -> fail("Brainz call failed") { failReason(result) }
      }
    }
  }

  @Test
  public fun findReleaseGroupZeppelinHousesOfTheHoly(): Unit = coroutineRule.runBlockingTest {
    withBrainz {
      val ledZeppelin = "Led Zeppelin".toArtistName()
      val housesOfTheHoly = "Houses of the Holy".toAlbumTitle()
      when (
        val result = findReleaseGroup {
          artist { ledZeppelin } and releaseGroup { housesOfTheHoly }
        }
      ) {
        is Ok -> result.value.let { groupList ->
          expect(groupList.count).toBe(2)
          expect(groupList.releaseGroups).toHaveSize(2)
          expect(groupList.releaseGroups[0].title).toBe(housesOfTheHoly.value)
          expect(groupList.releaseGroups[0].artistCredit[0].name).toBe(ledZeppelin.value)
        }
        is Err -> fail("Brainz call failed") { failReason(result) }
      }
    }
  }

  @Test
  public fun findSeries(): Unit = coroutineRule.runBlockingTest {
    withBrainz {
      when (val result = findSeries { default { "Studio Brussel" } }) {
        is Ok -> result.value.let { seriesList ->
          expect(seriesList.count).toBeGreaterThan(1)
          expect(seriesList.series).toHaveAny { it.name == "Studio Brussel: De Maxx" }
          expect(seriesList.series).toHaveAny { it.name == "Studio Brussel: Life Is Music" }
        }
        is Err -> fail("Brainz call failed") { failReason(result) }
      }
    }
  }

  @Test
  public fun findTagShoegazeAndIndie(): Unit = coroutineRule.runBlockingTest {
    withBrainz {
      when (val result = findTag { default { "shoegaze" } }) {
        is Ok -> result.value.let { tagList ->
          expect(tagList.count).toBeGreaterThan(25) // 28 last checked
          expect(tagList.tags).toHaveSize(25)
          expect(tagList.tags).toHaveAny {
            it.score == 100 && it.name == "rock shoegaze"
          }
          expect(tagList.tags).toHaveAny {
            it.score == 100 && it.name == "indie shoegaze"
          }
        }
        is Err -> fail("Brainz call failed") { failReason(result) }
      }

      when (val result = findTag { default { "indie" } }) {
        is Ok -> result.value.let { tagList ->
          expect(tagList.tags).toHaveAny { it.name.contains("indie") }
        }
        is Err -> fail("Brainz call failed") { failReason(result) }
      }
    }
  }

  @Test
  public fun findWorkFrozenFred(): Unit = coroutineRule.runBlockingTest {
    withBrainz {
      val michielPetersMbid = ArtistMbid("4c006444-ccbf-425e-b3e7-03a98bab5997")
      when (
        val result = findWork(limit = Limit(1)) {
          work { WorkName("Frozen") } and artistId { michielPetersMbid }
        }
      ) {
        is Ok -> result.value.let { workList ->
          expect(workList.works).toHaveSize(1)
          expect(workList.works[0].id).toBe("10c1a66a-8166-32ec-a00f-540f111ce7a3")
          expect(workList.works[0].title).toBe("Frozen Fred")
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

private val JETHRO_TULL = ArtistName("Jethro Tull")
private val JETHRO_TULL_MBID = ArtistMbid("ece57992-dc2e-4f67-a269-fa43626c1a3d")
