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
import com.ealva.brainzsvc.common.TocParam
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
import com.ealva.ealvabrainz.brainz.data.Label
import com.ealva.ealvabrainz.brainz.data.Place
import com.ealva.ealvabrainz.brainz.data.Recording
import com.ealva.ealvabrainz.brainz.data.Release
import com.ealva.ealvabrainz.brainz.data.Release.Subquery
import com.ealva.ealvabrainz.brainz.data.ReleaseGroup
import com.ealva.ealvabrainz.common.AreaMbid
import com.ealva.ealvabrainz.common.ArtistMbid
import com.ealva.ealvabrainz.common.DiscId
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
import com.ealva.ealvabrainz.common.UrlMbid
import com.ealva.ealvabrainz.common.WorkMbid
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
      dispatcher = coroutineRule.testDispatcher,
    )
    // ensure we do not exceed rate limiting as we are creating the services each time and not
    // applying rate limiting between calls in the service itself
    Thread.sleep(2000)
  }

  @Test
  public fun lookupAreaUnitedKingdom(): Unit = coroutineRule.runBlockingTest {
    withBrainz {
      val unitedKingdom = AreaMbid("8a754a16-0027-3a29-b6d7-2b40ea0481ed")
      when (
        val result = lookupArea(unitedKingdom) {
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
  public fun lookupArtistWithTypeNirvana(): Unit = coroutineRule.runBlockingTest {
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
  public fun lookupArtistWithStatusNirvana(): Unit = coroutineRule.runBlockingTest {
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
  public fun lookupEventNineInchNailsAtArenaRiga(): Unit = coroutineRule.runBlockingTest {
    withBrainz {
      val ninAtAreaRiga = EventMbid("fe39727a-3d21-4066-9345-3970cbd6cca4")
      when (val result = lookupEvent(ninAtAreaRiga)) {
        is Ok -> {
          result.value.let { event ->
            expect(event.name).toBe("Nine Inch Nails at Arena Riga")
            expect(event.time).toBe("19:00")
            expect(event.lifeSpan.begin).toBe("2014-05-06")
            expect(event.lifeSpan.end).toBe("2014-05-06")
          }
        }
        is Err -> fail("Brainz call failed") { failReason(result) }
      }
    }
  }

  @Test
  public fun lookupGenreCrustPunk(): Unit = coroutineRule.runBlockingTest {
    withBrainz {
      val crustPunk = GenreMbid("f66d7266-eb3d-4ef3-b4d8-b7cd992f918b")
      when (val result = lookupGenre(crustPunk)) {
        is Ok -> {
          result.value.let { genre ->
            expect(genre.name).toBe("crust punk")
          }
        }
        is Err -> fail("Brainz call failed") { failReason(result) }
      }
    }
  }

  @Test
  public fun lookupInstrumentKemanak(): Unit = coroutineRule.runBlockingTest {
    withBrainz {
      val kemanakInstrument = InstrumentMbid("dd430e7f-36ba-49a5-825b-80a525e69190")
      when (val result = lookupInstrument(kemanakInstrument)) {
        is Ok -> {
          result.value.let { instrument ->
            expect(instrument.name).toBe("kemanak")
            expect(instrument.type).toBe("Percussion instrument")
          }
        }
        is Err -> fail("Brainz call failed") { failReason(result) }
      }
    }
  }

  @Test
  public fun lookupLabelWarp(): Unit = coroutineRule.runBlockingTest {
    withBrainz {
      val labelMbid = LabelMbid("46f0f4cd-8aab-4b33-b698-f459faf64190")
      when (val result = lookupLabel(labelMbid) { misc(Label.Misc.Aliases) }) {
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
  public fun lookupPlaceArenaRiga(): Unit = coroutineRule.runBlockingTest {
    withBrainz {
      val arenaRiga = PlaceMbid("478558f9-a951-4067-ad91-e83f6ba63e74")
      when (val result = lookupPlace(arenaRiga) { misc(Place.Misc.Aliases) }) {
        is Ok -> {
          result.value.let { place ->
            expect(place.name).toBe("Arēna Rīga")
            expect(place.type).toBe("Indoor arena")
            expect(place.typeId).toBe("a77c11f6-82fa-3cc0-9041-ac60e5f6e024")
            expect(place.aliases[0].locale).toBe("en")
            expect(place.aliases[0].name).toBe("Arena Riga")
            expect(place.coordinates.longitude).toBe(24.121403)
            expect(place.coordinates.latitude).toBe(56.967989)
          }
        }
        is Err -> fail("Brainz call failed") { failReason(result) }
      }
    }
  }

  @Test
  public fun lookupRecordingLastAngel(): Unit = coroutineRule.runBlockingTest {
    withBrainz {
      val lastAngelRecording = RecordingMbid("b9ad642e-b012-41c7-b72a-42cf4911f9ff")
      when (
        val result = lookupRecording(lastAngelRecording) {
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

  @Test
  public fun lookupReleaseHaflerTrio(): Unit = coroutineRule.runBlockingTest {
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
  public fun lookupReleaseWithStatusHaflerTrio(): Unit = coroutineRule.runBlockingTest {
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
  public fun lookupReleaseWithTypeHaflerTrio(): Unit = coroutineRule.runBlockingTest {
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
  public fun lookupReleaseGroup50CentLostTape(): Unit = coroutineRule.runBlockingTest {
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
  public fun lookupReleaseGroupWithType50CentLostTape(): Unit = coroutineRule.runBlockingTest {
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
  public fun lookupReleaseGroupWithStatus50CentLostTape(): Unit = coroutineRule.runBlockingTest {
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
  public fun lookupSeriesMDNATour(): Unit = coroutineRule.runBlockingTest {
    withBrainz {
      val theMdnaTour = SeriesMbid("300676c6-6e63-4d4d-9084-089efcd0113f")
      when (val result = lookupSeries(theMdnaTour)) {
        is Ok -> {
          result.value.let { series ->
            expect(series.name).toBe("The MDNA Tour")
            expect(series.type).toBe("Tour")
            expect(series.typeId).toBe("8ff6df0e-3dce-3bdf-bd57-d386c51b0060")
          }
        }
        is Err -> fail("Brainz call failed") { failReason(result) }
      }
    }
  }

  @Test
  public fun lookupUrlArvoPart(): Unit = coroutineRule.runBlockingTest {
    withBrainz {
      val arvoPartUrlMbid = UrlMbid("46d8f693-52e4-4d03-936f-7ca8459019a7")
      when (val result = lookupUrl(arvoPartUrlMbid)) {
        is Ok -> {
          result.value.let { url ->
            expect(url.resource).toBe("https://www.arvopart.ee/")
          }
        }
        is Err -> fail("Brainz call failed") { failReason(result) }
      }
    }
  }

  @Test
  public fun lookupWorkHELLO(): Unit = coroutineRule.runBlockingTest {
    withBrainz {
      val helloMbid = WorkMbid("b1df2cf3-69a9-3bc0-be44-f71e79b27a22")
      when (val result = lookupWork(helloMbid)) {
        is Ok -> {
          result.value.let { work ->
            expect(work.title).toBe("HELLO! また会おうね")
            expect(work.type).toBe("Song")
          }
        }
        is Err -> fail("Brainz call failed") { failReason(result) }
      }
    }
  }

  @Test
  public fun lookupDiscIdForNevermind(): Unit = coroutineRule.runBlockingTest {
    withBrainz {
      val nevermindDiscId = DiscId("I5l9cCSFccLKFEKS.7wqSZAorPU-")
      when (val result = lookupDisc(nevermindDiscId)) {
        is Ok -> {
          result.value.let { discLookupList ->
            expect(discLookupList.id).toBe(nevermindDiscId.value)
          }
        }
        is Err -> fail("Brainz call failed") { failReason(result) }
      }
    }
  }

  @Test
  public fun lookupDiscIdFuzzy(): Unit = coroutineRule.runBlockingTest {
    withBrainz {
      when (
        val result = fuzzyTocLookup(
          TocParam(
            leadoutSectorOffset = 267257,
            firstSectorOffset = 150,
            22767, 41887, 58317, 72102, 91375, 104652, 115380, 132165, 143932, 159870, 174597
          )
        )
      ) {
        is Ok -> {
          result.value.let { browseReleaseList ->
            expect(browseReleaseList.releases).toHaveAny { it.title == "Nevermind" }
            expect(browseReleaseList.releases).toHaveAny {
              it.id == "1afdb0ff-25d1-4966-90ee-b1133f8243fb"
            }
          }
        }
        is Err -> fail("Brainz call failed") { failReason(result) }
      }
    }
  }

  @Test
  public fun lookupDiscIdFuzzyExcludeStubsAllMedia(): Unit = coroutineRule.runBlockingTest {
    withBrainz {
      when (
        val result = fuzzyTocLookup(
          TocParam(
            leadoutSectorOffset = 267257,
            firstSectorOffset = 150,
            22767, 41887, 58317, 72102, 91375, 104652, 115380, 132165, 143932, 159870, 174597
          ),
          excludeCDStubs = true,
          allMediumFormats = true
        )
      ) {
        is Ok -> {
          result.value.let { browseReleaseList ->
            expect(browseReleaseList.releases).toHaveAny { it.title == "Nevermind" }
            expect(browseReleaseList.releases).toHaveAny {
              it.id == "1afdb0ff-25d1-4966-90ee-b1133f8243fb"
            }
          }
        }
        is Err -> fail("Brainz call failed") { failReason(result) }
      }
    }
  }

  @Test
  public fun lookupIsrcLastAngel(): Unit = coroutineRule.runBlockingTest {
    withBrainz {
      val lastAngelIsrc = Isrc("JPB600760301")
      when (
        val result = lookupIsrc(lastAngelIsrc) {
          subquery(Recording.Subquery.ArtistCredits)
        }
      ) {
        is Ok -> {
          result.value.let { list ->
            expect(list.isrc).toBe(lastAngelIsrc.value)
            expect(list.recordings[0].title).toBe("LAST ANGEL")
          }
        }
        is Err -> fail("Brainz call failed") { failReason(result) }
      }
    }
  }

  @Test
  public fun lookupIswcHello(): Unit = coroutineRule.runBlockingTest {
    withBrainz {
      val helloIswc = Iswc("T-101.690.320-9")
      when (val result = lookupIswc(helloIswc)) {
        is Ok -> {
          result.value.let { list ->
            expect(list.workCount).toBe(1)
            expect(list.works[0].title).toBe("HELLO! また会おうね")
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

private val THE_HAFLER_TRIO_RELEASE = ReleaseMbid("59211ea4-ffd2-4ad9-9a4e-941d3148024a")
private val FITY_CENT_LOST_TAPE_GROUP_MBID: ReleaseGroupMbid =
  ReleaseGroupMbid("c9fdb94c-4975-4ed6-a96f-ef6d80bb7738")
private val NIRVANA_MBID = ArtistMbid("5b11f4ce-a62d-471e-81fc-a69a8278c7da")
