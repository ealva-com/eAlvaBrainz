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

internal object FallbackMap {
  internal val map = mapOf(
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
    Event.fallbackMapping,
    EventRelation.fallbackMapping,
    Genre.fallbackMapping,
    Instrument.fallbackMapping,
    Label.fallbackMapping,
    LabelRelation.fallbackMapping,
    LabelInfo.fallbackMapping,
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

  fun get(key: String): Any {
    return map[key] ?: throw IllegalStateException("Fallback map not configured for $key")
  }
}
