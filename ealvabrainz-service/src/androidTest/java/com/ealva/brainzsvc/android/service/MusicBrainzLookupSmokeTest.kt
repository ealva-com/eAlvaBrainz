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
import com.ealva.ealvabrainz.brainz.data.Area
import com.ealva.ealvabrainz.brainz.data.Artist
import com.ealva.ealvabrainz.brainz.data.Collection
import com.ealva.ealvabrainz.brainz.data.Label
import com.ealva.ealvabrainz.brainz.data.Place
import com.ealva.ealvabrainz.brainz.data.Recording
import com.ealva.ealvabrainz.brainz.data.Release
import com.ealva.ealvabrainz.brainz.data.Release.Include
import com.ealva.ealvabrainz.brainz.data.ReleaseGroup
import com.ealva.ealvabrainz.browse.CollectionBrowse
import com.ealva.ealvabrainz.common.AreaMbid
import com.ealva.ealvabrainz.common.ArtistMbid
import com.ealva.ealvabrainz.common.BrainzInvalidStatusException
import com.ealva.ealvabrainz.common.BrainzInvalidTypeException
import com.ealva.ealvabrainz.common.DiscId
import com.ealva.ealvabrainz.common.EditorName
import com.ealva.ealvabrainz.common.EventMbid
import com.ealva.ealvabrainz.common.GenreMbid
import com.ealva.ealvabrainz.common.InstrumentMbid
import com.ealva.ealvabrainz.common.Isrc
import com.ealva.ealvabrainz.common.Iswc
import com.ealva.ealvabrainz.common.LabelMbid
import com.ealva.ealvabrainz.common.PlaceMbid
import com.ealva.ealvabrainz.common.RecordingMbid
import com.ealva.ealvabrainz.common.ReleaseGroupMbid
import com.ealva.ealvabrainz.common.ReleaseMbid
import com.ealva.ealvabrainz.common.SeriesMbid
import com.ealva.ealvabrainz.common.TocParam
import com.ealva.ealvabrainz.common.UrlMbid
import com.ealva.ealvabrainz.common.WorkMbid
import com.ealva.ealvabrainz.common.mbid
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
public class MusicBrainzLookupSmokeTest {

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
  public fun lookupAreaUnitedKingdom(): Unit = brainz {
    lookupArea(AreaMbid("8a754a16-0027-3a29-b6d7-2b40ea0481ed")) { include(Area.Include.Aliases) }
      .onSuccess { area ->
        expect(area.name).toBe("United Kingdom")
        expect(area.type).toBe("Country")
        expect(area.iso31661Codes[0]).toBe("GB")
      }
      .onFailure { fail("Brainz call failed") { it.asString(fetcher) } }
  }

  @Test
  public fun lookupArtistNirvana(): Unit = brainz {
    lookupArtist(NIRVANA_MBID) { include(Artist.Include.Aliases) }
      .onSuccess { artist ->
        expect(artist.name).toBe("Nirvana")
        expect(artist.aliases).toNotBeEmpty()
      }
      .onFailure { fail("Brainz call failed") { it.asString(fetcher) } }
  }

  @Test
  public fun lookupArtistWithTypeNirvana(): Unit = brainz {
    lookupArtist(NIRVANA_MBID) { types(Release.Type.Album) }
      .onSuccess { fail("Expected Err result") }
      .onFailure { brainzMessage ->
        expect(brainzMessage).toBeInstanceOf<BrainzMessage.BrainzExceptionMessage> { msg ->
          expect(msg.ex).toBeInstanceOf<BrainzInvalidTypeException>()
        }
      }
  }

  @Test
  public fun lookupArtistWithStatusNirvana(): Unit = brainz {
    lookupArtist(NIRVANA_MBID) { status(Release.Status.Official) }
      .onSuccess { fail("Expected Err result") }
      .onFailure { brainzMessage ->
        expect(brainzMessage).toBeInstanceOf<BrainzMessage.BrainzExceptionMessage> { msg ->
          expect(msg.ex).toBeInstanceOf<BrainzInvalidStatusException>()
        }
      }
  }

  /**
   * This test requires authentication as it includes Collection.Browse.UserCollections. To get a
   * valid Collect MBID first browse the
   */
  @Test
  public fun lookupCollection(): Unit = brainz {
    browseCollections(CollectionBrowse.BrowseOn.Editor(EditorName(BuildConfig.BRAINZ_USERNAME))) {
      include(Collection.Browse.UserCollections)
    }.onSuccess { browseList ->
      expect(browseList.collections).toNotBeEmpty { "Expected some collections" }
      browseList.collections[0].let { foundCollection ->
        lookupCollection(foundCollection.mbid)
          .onSuccess { expect(it.editor).toBe(BuildConfig.BRAINZ_USERNAME) }
          .onFailure { fail("Brainz call failed") { it.asString(fetcher) } }
      }
    }.onFailure { fail("Brainz call failed") { it.asString(fetcher) } }
  }

  @Test
  public fun lookupEventNineInchNailsAtArenaRiga(): Unit = brainz {
    lookupEvent(EventMbid("fe39727a-3d21-4066-9345-3970cbd6cca4"))
      .onSuccess { event ->
        expect(event.name).toBe("Nine Inch Nails at Arena Riga")
        expect(event.time).toBe("19:00")
        expect(event.lifeSpan.begin).toBe("2014-05-06")
        expect(event.lifeSpan.end).toBe("2014-05-06")
      }
      .onFailure { fail("Brainz call failed") { it.asString(fetcher) } }
  }

  @Test
  public fun lookupGenreCrustPunk(): Unit = brainz {
    lookupGenre(GenreMbid("f66d7266-eb3d-4ef3-b4d8-b7cd992f918b"))
      .onSuccess { genre -> expect(genre.name).toBe("crust punk") }
      .onFailure { fail("Brainz call failed") { it.asString(fetcher) } }
  }

  @Test
  public fun lookupInstrumentKemanak(): Unit = brainz {
    lookupInstrument(InstrumentMbid("dd430e7f-36ba-49a5-825b-80a525e69190"))
      .onSuccess { instrument ->
        expect(instrument.name).toBe("kemanak")
        expect(instrument.type).toBe("Percussion instrument")
      }
      .onFailure { fail("Brainz call failed") { it.asString(fetcher) } }
  }

  @Test
  public fun lookupLabelWarp(): Unit = brainz {
    lookupLabel(LabelMbid("46f0f4cd-8aab-4b33-b698-f459faf64190")) {
      include(Label.Include.Aliases)
    }.onSuccess { label ->
      expect(label.name).toBe("Warp")
      expect(label.country).toBe("GB")
      expect(label.aliases[0].name).toBe("Warp Records")
    }.onFailure { fail("Brainz call failed") { it.asString(fetcher) } }
  }

  @Test
  public fun lookupPlaceArenaRiga(): Unit = brainz {
    lookupPlace(PlaceMbid("478558f9-a951-4067-ad91-e83f6ba63e74")) {
      include(Place.Include.Aliases)
    }.onSuccess { place ->
      expect(place.name).toBe("Arēna Rīga")
      expect(place.type).toBe("Indoor arena")
      expect(place.typeId).toBe("a77c11f6-82fa-3cc0-9041-ac60e5f6e024")
      expect(place.aliases[0].locale).toBe("en")
      expect(place.aliases[0].name).toBe("Arena Riga")
      expect(place.coordinates.longitude).toBe(24.121403)
      expect(place.coordinates.latitude).toBe(56.967989)
    }.onFailure { fail("Brainz call failed") { it.asString(fetcher) } }
  }

  @Test
  public fun lookupRecordingLastAngel(): Unit = brainz {
    val lastAngelRecording = RecordingMbid("b9ad642e-b012-41c7-b72a-42cf4911f9ff")
    lookupRecording(lastAngelRecording) {
      include(
        Recording.Include.ArtistCredits,
        Recording.Include.Isrcs,
        Recording.Include.Releases
      )
    }.onSuccess { recording ->
      expect(recording.title).toBe("LAST ANGEL")
      expect(recording.firstReleaseDate).toBe("2007-11-07")
    }.onFailure { fail("Brainz call failed") { it.asString(fetcher) } }
  }

  @Test
  public fun lookupReleaseHaflerTrio(): Unit = brainz {
    lookupRelease(THE_HAFLER_TRIO_RELEASE) {
      include(Include.ArtistCredits, Include.Labels, Include.DiscIds, Include.Recordings)
    }.onSuccess { release ->
      expect(release.title).toBe("æ³o & h³æ")
      expect(release.releaseEvents).toHaveSize(1)
      expect(release.media).toHaveSize(2)
    }.onFailure { fail("Brainz call failed") { it.asString(fetcher) } }
  }

  @Test
  public fun lookupReleaseWithStatusHaflerTrio(): Unit = brainz {
    lookupRelease(THE_HAFLER_TRIO_RELEASE) { status(Release.Status.Official) }
      .onSuccess { release ->
        expect(release.title).toBe("æ³o & h³æ")
        expect(release.releaseEvents).toHaveSize(1)
      }
      .onFailure { fail("Brainz call failed") { it.asString(fetcher) } }
  }

  @Test
  public fun lookupReleaseWithTypeHaflerTrio(): Unit = brainz {
    lookupRelease(THE_HAFLER_TRIO_RELEASE) { types(Release.Type.Album) }
      .onSuccess { release ->
        expect(release.title).toBe("æ³o & h³æ")
        expect(release.releaseEvents).toHaveSize(1)
      }
      .onFailure { fail("Brainz call failed") { it.asString(fetcher) } }
  }

  @Test
  public fun lookupReleaseGroup50CentLostTape(): Unit = brainz {
    lookupReleaseGroup(FITY_CENT_LOST_TAPE_GROUP_MBID) {
      include(ReleaseGroup.Include.ArtistCredits, ReleaseGroup.Include.Releases)
    }.onSuccess { group ->
      expect(group.title).toBe("The Lost Tape")
      expect(group.artistCredit[0].name).toBe("50 Cent")
    }.onFailure { fail("Brainz call failed") { it.asString(fetcher) } }
  }

  @Test
  public fun lookupReleaseGroupWithType50CentLostTape(): Unit = brainz {
    lookupReleaseGroup(FITY_CENT_LOST_TAPE_GROUP_MBID) { types(Release.Type.Album) }
      .onSuccess { group -> expect(group.title).toBe("The Lost Tape") }
      .onFailure { fail("Brainz call failed") { it.asString(fetcher) } }
  }

  @Test
  public fun lookupReleaseGroupWithStatus50CentLostTape(): Unit = brainz {
    lookupReleaseGroup(FITY_CENT_LOST_TAPE_GROUP_MBID) { status(Release.Status.Official) }
      .onSuccess { fail("Expected Err result") }
      .onFailure { brainzMessage ->
        expect(brainzMessage).toBeInstanceOf<BrainzMessage.BrainzExceptionMessage> { msg ->
          expect(msg.ex).toBeInstanceOf<BrainzInvalidStatusException>()
        }
      }
  }

  @Test
  public fun lookupSeriesMDNATour(): Unit = brainz {
    lookupSeries(SeriesMbid("300676c6-6e63-4d4d-9084-089efcd0113f"))
      .onSuccess { series ->
        expect(series.name).toBe("The MDNA Tour")
        expect(series.type).toBe("Tour")
        expect(series.typeId).toBe("8ff6df0e-3dce-3bdf-bd57-d386c51b0060")
      }
      .onFailure { fail("Brainz call failed") { it.asString(fetcher) } }
  }

  @Test
  public fun lookupUrlArvoPart(): Unit = brainz {
    lookupUrl(UrlMbid("46d8f693-52e4-4d03-936f-7ca8459019a7"))
      .onSuccess { url -> expect(url.resource).toBe("https://www.arvopart.ee/") }
      .onFailure { fail("Brainz call failed") { it.asString(fetcher) } }
  }

  @Test
  public fun lookupWorkHELLO(): Unit = brainz {
    val helloMbid = WorkMbid("b1df2cf3-69a9-3bc0-be44-f71e79b27a22")
    lookupWork(helloMbid)
      .onSuccess { work ->
        expect(work.title).toBe("HELLO! また会おうね")
        expect(work.type).toBe("Song")
      }
      .onFailure { fail("Brainz call failed") { it.asString(fetcher) } }
  }

  @Test
  public fun lookupDiscIdForNevermind(): Unit = brainz {
    val nevermindDiscId = DiscId("I5l9cCSFccLKFEKS.7wqSZAorPU-")
    lookupDisc(nevermindDiscId)
      .onSuccess { discLookupList -> expect(discLookupList.id).toBe(nevermindDiscId.value) }
      .onFailure { fail("Brainz call failed") { it.asString(fetcher) } }
  }

  @Test
  public fun lookupDiscIdFuzzy(): Unit = brainz {
    fuzzyTocLookup(
      TocParam(
        leadoutSectorOffset = 267257,
        firstSectorOffset = 150,
        22767, 41887, 58317, 72102, 91375, 104652, 115380, 132165, 143932, 159870, 174597
      )
    ).onSuccess { browseReleaseList ->
      expect(browseReleaseList.releases).toHaveAny { it.title == "Nevermind" }
      expect(browseReleaseList.releases).toHaveAny {
        it.id == "1afdb0ff-25d1-4966-90ee-b1133f8243fb"
      }
    }.onFailure { fail("Brainz call failed") { it.asString(fetcher) } }
  }

  @Test
  public fun lookupDiscIdFuzzyExcludeStubsAllMedia(): Unit = brainz {
    fuzzyTocLookup(
      TocParam(
        leadoutSectorOffset = 267257,
        firstSectorOffset = 150,
        22767, 41887, 58317, 72102, 91375, 104652, 115380, 132165, 143932, 159870, 174597
      ),
      excludeCDStubs = true,
      allMediumFormats = true
    ).onSuccess { browseReleaseList ->
      expect(browseReleaseList.releases).toHaveAny { it.title == "Nevermind" }
      expect(browseReleaseList.releases).toHaveAny {
        it.id == "1afdb0ff-25d1-4966-90ee-b1133f8243fb"
      }
    }.onFailure { fail("Brainz call failed") { it.asString(fetcher) } }
  }

  @Test
  public fun lookupIsrcLastAngel(): Unit = brainz {
    val lastAngelIsrc = Isrc("JPB600760301")
    lookupIsrc(lastAngelIsrc) { include(Recording.Include.ArtistCredits) }
      .onSuccess { list ->
        expect(list.isrc).toBe(lastAngelIsrc.value)
        expect(list.recordings[0].title).toBe("LAST ANGEL")
      }
      .onFailure { fail("Brainz call failed") { it.asString(fetcher) } }
  }

  @Test
  public fun lookupIswcHello(): Unit = brainz {
    val helloIswc = Iswc("T-101.690.320-9")
    lookupIswc(helloIswc)
      .onSuccess { list ->
        expect(list.workCount).toBe(1)
        expect(list.works[0].title).toBe("HELLO! また会おうね")
      }
      .onFailure { fail("Brainz call failed") { it.asString(fetcher) } }
  }

  private fun brainz(block: suspend MusicBrainzService.() -> Unit) = coroutineRule.runBlockingTest {
    @Suppress("BlockingMethodInNonBlockingContext")
    runBlocking {
      musicBrainzService.block()
    }
  }
}

private val THE_HAFLER_TRIO_RELEASE = ReleaseMbid("59211ea4-ffd2-4ad9-9a4e-941d3148024a")
private val FITY_CENT_LOST_TAPE_GROUP_MBID: ReleaseGroupMbid =
  ReleaseGroupMbid("c9fdb94c-4975-4ed6-a96f-ef6d80bb7738")
private val NIRVANA_MBID = ArtistMbid("5b11f4ce-a62d-471e-81fc-a69a8278c7da")
