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

package com.ealva.ealvabrainz.brainz.data

import com.ealva.ealvabrainz.brainz.data.Alias.Companion.NullAlias
import com.ealva.ealvabrainz.brainz.data.Annotation.Companion.NullAnnotation
import com.ealva.ealvabrainz.brainz.data.Area.Companion.NullArea
import com.ealva.ealvabrainz.brainz.data.AreaRelation.Companion.NullAreaRelation
import com.ealva.ealvabrainz.brainz.data.Artist.Companion.NullArtist
import com.ealva.ealvabrainz.brainz.data.ArtistCredit.Companion.NullArtistCredit
import com.ealva.ealvabrainz.brainz.data.ArtistRelation.Companion.NullArtistRelation
import com.ealva.ealvabrainz.brainz.data.Attribute.Companion.NullAttribute
import com.ealva.ealvabrainz.brainz.data.CdStub.Companion.NullCdStub
import com.ealva.ealvabrainz.brainz.data.Coordinates.Companion.NullCoordinates
import com.ealva.ealvabrainz.brainz.data.CoverArtArchive.Companion.NullCoverArtArchive
import com.ealva.ealvabrainz.brainz.data.CoverArtImage.Companion.NullCoverArtImage
import com.ealva.ealvabrainz.brainz.data.CoverArtRelease.Companion.NullCoverArtRelease
import com.ealva.ealvabrainz.brainz.data.Disc.Companion.NullDisc
import com.ealva.ealvabrainz.brainz.data.Event.Companion.NullEvent
import com.ealva.ealvabrainz.brainz.data.EventRelation.Companion.NullEventRelation
import com.ealva.ealvabrainz.brainz.data.Genre.Companion.NullGenre
import com.ealva.ealvabrainz.brainz.data.Instrument.Companion.NullInstrument
import com.ealva.ealvabrainz.brainz.data.Label.Companion.NullLabel
import com.ealva.ealvabrainz.brainz.data.LabelInfo.Companion.NullLabelInfo
import com.ealva.ealvabrainz.brainz.data.LabelRelation.Companion.NullLabelRelation
import com.ealva.ealvabrainz.brainz.data.LifeSpan.Companion.NullLifeSpan
import com.ealva.ealvabrainz.brainz.data.Medium.Companion.NullMedium
import com.ealva.ealvabrainz.brainz.data.Packaging.Companion.NullPackaging
import com.ealva.ealvabrainz.brainz.data.Place.Companion.NullPlace
import com.ealva.ealvabrainz.brainz.data.PlaceRelation.Companion.NullPlaceRelation
import com.ealva.ealvabrainz.brainz.data.Rating.Companion.NullRating
import com.ealva.ealvabrainz.brainz.data.Recording.Companion.NullRecording
import com.ealva.ealvabrainz.brainz.data.RecordingRelation.Companion.NullRecordingRelation
import com.ealva.ealvabrainz.brainz.data.Release.Companion.NullRelease
import com.ealva.ealvabrainz.brainz.data.ReleaseEvent.Companion.NullReleaseEvent
import com.ealva.ealvabrainz.brainz.data.ReleaseGroup.Companion.NullReleaseGroup
import com.ealva.ealvabrainz.brainz.data.ReleaseGroupRelation.Companion.NullReleaseGroupRelation
import com.ealva.ealvabrainz.brainz.data.ReleaseRelation.Companion.NullReleaseRelation
import com.ealva.ealvabrainz.brainz.data.Series.Companion.NullSeries
import com.ealva.ealvabrainz.brainz.data.SeriesRelation.Companion.NullSeriesRelation
import com.ealva.ealvabrainz.brainz.data.Tag.Companion.NullTag
import com.ealva.ealvabrainz.brainz.data.Target.Companion.NullTarget
import com.ealva.ealvabrainz.brainz.data.TextRepresentation.Companion.NullTextRepresentation
import com.ealva.ealvabrainz.brainz.data.Thumbnails.Companion.NullThumbnails
import com.ealva.ealvabrainz.brainz.data.Track.Companion.NullTrack
import com.ealva.ealvabrainz.brainz.data.Url.Companion.NullUrl
import com.ealva.ealvabrainz.brainz.data.Work.Companion.NullWork
import com.ealva.ealvabrainz.brainz.data.WorkRelation.Companion.NullWorkRelation
import com.nhaarman.expect.expect
import org.junit.Test

/**
 * Test that the fallback map is properly configured
 */
internal class FallbackMapTest {
  private val expectedMap: Map<String, Any> = mapOf(
    Annotation.fallbackMapping,
    Alias.fallbackMapping,
    Area.fallbackMapping,
    AreaRelation.fallbackMapping,
    Artist.fallbackMapping,
    ArtistCredit.fallbackMapping,
    ArtistRelation.fallbackMapping,
    Attribute.fallbackMapping,
    Coordinates.fallbackMapping,
    CoverArtArchive.fallbackMapping,
    CoverArtImage.fallbackMapping,
    CoverArtRelease.fallbackMapping,
    Disc.fallbackMapping,
    CdStub.fallbackMapping,
    Event.fallbackMapping,
    EventRelation.fallbackMapping,
    Genre.fallbackMapping,
    Instrument.fallbackMapping,
    Label.fallbackMapping,
    LabelInfo.fallbackMapping,
    LabelRelation.fallbackMapping,
    LifeSpan.fallbackMapping,
    Medium.fallbackMapping,
    Packaging.fallbackMapping,
    Place.fallbackMapping,
    PlaceRelation.fallbackMapping,
    Rating.fallbackMapping,
    Recording.fallbackMapping,
    RecordingRelation.fallbackMapping,
    Release.fallbackMapping,
    ReleaseRelation.fallbackMapping,
    ReleaseEvent.fallbackMapping,
    ReleaseGroup.fallbackMapping,
    ReleaseGroupRelation.fallbackMapping,
    Series.fallbackMapping,
    SeriesRelation.fallbackMapping,
    Tag.fallbackMapping,
    Target.fallbackMapping,
    TextRepresentation.fallbackMapping,
    Thumbnails.fallbackMapping,
    Track.fallbackMapping,
    Url.fallbackMapping,
    Work.fallbackMapping,
    WorkRelation.fallbackMapping
  )

  /**
   * Test that the FallbackMap contains all fallback mappings we expect and no more
   */
  @Test
  fun `test that map contains expected entries`() {
    expect(FallbackMap.map).toBe(expectedMap)
  }

  /**
   * Test that the key/value pairs are as expected and that there are no more entries other
   * than what we expect.
   */
  @Test
  fun `test mappings`() {
    val map = FallbackMap.map.toMutableMap()
    expect(map.remove(Annotation::class.java.name)).toBe(NullAnnotation)
    expect(map.remove(Alias::class.java.name)).toBe(NullAlias)
    expect(map.remove(Area::class.java.name)).toBe(NullArea)
    expect(map.remove(AreaRelation::class.java.name)).toBe(NullAreaRelation)
    expect(map.remove(Artist::class.java.name)).toBe(NullArtist)
    expect(map.remove(ArtistRelation::class.java.name)).toBe(NullArtistRelation)
    expect(map.remove(ArtistCredit::class.java.name)).toBe(NullArtistCredit)
    expect(map.remove(Attribute::class.java.name)).toBe(NullAttribute)
    expect(map.remove(CdStub::class.java.name)).toBe(NullCdStub)
    expect(map.remove(Coordinates::class.java.name)).toBe(NullCoordinates)
    expect(map.remove(CoverArtArchive::class.java.name)).toBe(NullCoverArtArchive)
    expect(map.remove(CoverArtImage::class.java.name)).toBe(NullCoverArtImage)
    expect(map.remove(CoverArtRelease::class.java.name)).toBe(NullCoverArtRelease)
    expect(map.remove(Disc::class.java.name)).toBe(NullDisc)
    expect(map.remove(Event::class.java.name)).toBe(NullEvent)
    expect(map.remove(EventRelation::class.java.name)).toBe(NullEventRelation)
    expect(map.remove(Genre::class.java.name)).toBe(NullGenre)
    expect(map.remove(Instrument::class.java.name)).toBe(NullInstrument)
    expect(map.remove(Label::class.java.name)).toBe(NullLabel)
    expect(map.remove(LabelInfo::class.java.name)).toBe(NullLabelInfo)
    expect(map.remove(LabelRelation::class.java.name)).toBe(NullLabelRelation)
    expect(map.remove(LifeSpan::class.java.name)).toBe(NullLifeSpan)
    expect(map.remove(Medium::class.java.name)).toBe(NullMedium)
    expect(map.remove(Packaging::class.java.name)).toBe(NullPackaging)
    expect(map.remove(Place::class.java.name)).toBe(NullPlace)
    expect(map.remove(PlaceRelation::class.java.name)).toBe(NullPlaceRelation)
    expect(map.remove(Rating::class.java.name)).toBe(NullRating)
    expect(map.remove(Recording::class.java.name)).toBe(NullRecording)
    expect(map.remove(RecordingRelation::class.java.name)).toBe(NullRecordingRelation)
    expect(map.remove(Release::class.java.name)).toBe(NullRelease)
    expect(map.remove(ReleaseEvent::class.java.name)).toBe(NullReleaseEvent)
    expect(map.remove(ReleaseRelation::class.java.name)).toBe(NullReleaseRelation)
    expect(map.remove(ReleaseGroup::class.java.name)).toBe(NullReleaseGroup)
    expect(map.remove(ReleaseGroupRelation::class.java.name)).toBe(NullReleaseGroupRelation)
    expect(map.remove(Series::class.java.name)).toBe(NullSeries)
    expect(map.remove(SeriesRelation::class.java.name)).toBe(NullSeriesRelation)
    expect(map.remove(Tag::class.java.name)).toBe(NullTag)
    expect(map.remove(Target::class.java.name)).toBe(NullTarget)
    expect(map.remove(TextRepresentation::class.java.name)).toBe(NullTextRepresentation)
    expect(map.remove(Thumbnails::class.java.name)).toBe(NullThumbnails)
    expect(map.remove(Track::class.java.name)).toBe(NullTrack)
    expect(map.remove(Url::class.java.name)).toBe(NullUrl)
    expect(map.remove(Work::class.java.name)).toBe(NullWork)
    expect(map.remove(WorkRelation::class.java.name)).toBe(NullWorkRelation)
    expect(map.size).toBe(0) { map.entries.first() }
  }
}
