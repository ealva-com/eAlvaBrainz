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

import com.ealva.ealvabrainz.brainz.data.AreaRelation.Companion.NullAreaRelation
import com.ealva.ealvabrainz.brainz.data.ArtistRelation.Companion.NullArtistRelation
import com.ealva.ealvabrainz.brainz.data.EventRelation.Companion.NullEventRelation
import com.ealva.ealvabrainz.brainz.data.InstrumentRelation.Companion.NullInstrumentRelation
import com.ealva.ealvabrainz.brainz.data.LabelRelation.Companion.NullLabelRelation
import com.ealva.ealvabrainz.brainz.data.PlaceRelation.Companion.NullPlaceRelation
import com.ealva.ealvabrainz.brainz.data.RecordingRelation.Companion.NullRecordingRelation
import com.ealva.ealvabrainz.brainz.data.ReleaseGroupRelation.Companion.NullReleaseGroupRelation
import com.ealva.ealvabrainz.brainz.data.ReleaseRelation.Companion.NullReleaseRelation
import com.ealva.ealvabrainz.brainz.data.SeriesRelation.Companion.NullSeriesRelation
import com.ealva.ealvabrainz.brainz.data.UrlRelation.Companion.NullUrlRelation
import com.ealva.ealvabrainz.brainz.data.WorkRelation.Companion.NullWorkRelation
import com.ealva.ealvabrainz.moshi.FallbackOnNull
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * ### Relationship types
 * There are a huge number of different relationship types. The lists (organised per types of
 * entities they connect) can be checked at the
 * [relationship type table](https://musicbrainz.org/relationships).
 * ```
 * ```
 * ### Credits
 * Credits allow indicating that, for example, songwriting was credited to an artist's legal name,
 * and not his main (performance) name.
 * ```
 * ```
 * ### Attributes
 * Relationships can have attributes which modify the relationship. There is a
 * [list of all](https://musicbrainz.org/relationship-attributes)
 * attributes, but the attributes which are available, and how they should be used, depends on the
 * relationship type, so see the documentation for the relationship you want to use for more
 * information.
 * ```
 * ```
 * ### Dates
 * Some relationships have two date fields, a begin date and an end date, to store the period of
 * time during which the relationship applied. The date can be the year, the year and the month or
 * the full date. It is optional, so it can also be left blank. As with other attributes,
 * see the documentation for the relationship types you are using.
 */
public sealed class Relation(
  public var type: String = "",
  @field:Json(name = "type-id") public var typeId: String = "",
  @field:Json(name = "target-type") public var targetType: String = "",
  @field:Json(name = "ordering-key") public var orderingKey: Int = 0,
  public var direction: String = "",
  public var attributes: List<String> = emptyList(),
  @field:Json(name = "attribute-values")
  public var attributeValues: Map<String, String> = emptyMap(),
  @field:Json(name = "attribute-ids") public var attributeIds: Map<String, String> = emptyMap(),
  public var begin: String = "",
  public var end: String = "",
  public var ended: Boolean = false,
  @field:Json(name = "source-credit") public var sourceCredit: String = "",
  @field:Json(name = "target-credit") public var targetCredit: String = ""
) {
  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as Relation

    if (type != other.type) return false
    if (typeId != other.typeId) return false
    if (targetType != other.targetType) return false
    if (orderingKey != other.orderingKey) return false
    if (direction != other.direction) return false
    if (begin != other.begin) return false
    if (end != other.end) return false
    if (ended != other.ended) return false
    if (sourceCredit != other.sourceCredit) return false
    if (targetCredit != other.targetCredit) return false

    return true
  }

  override fun hashCode(): Int {
    var result = type.hashCode()
    result = 31 * result + typeId.hashCode()
    result = 31 * result + targetType.hashCode()
    result = 31 * result + orderingKey
    result = 31 * result + direction.hashCode()
    result = 31 * result + begin.hashCode()
    result = 31 * result + end.hashCode()
    result = 31 * result + ended.hashCode()
    result = 31 * result + sourceCredit.hashCode()
    result = 31 * result + targetCredit.hashCode()
    return result
  }
}

/**
 * * [Area-Area](https://musicbrainz.org/relationships/area-area)
 * * [Area-Event](https://musicbrainz.org/relationships/area-event)
 * * [Area-Instrument](https://musicbrainz.org/relationships/area-instrument)
 * * [Area-Recording](https://musicbrainz.org/relationships/area-recording)
 * * [Area-Release](https://musicbrainz.org/relationships/area-release)
 * * [Area-URL](https://musicbrainz.org/relationships/area-url)
 * * [Area-Work](https://musicbrainz.org/relationships/area-work)
 */
@JsonClass(generateAdapter = true)
public class AreaRelation(
  type: String = "",
  typeId: String = "",
  targetType: String = "",
  orderingKey: Int = 0,
  direction: String = "",
  attributes: List<String> = emptyList(),
  attributeValues: Map<String, String> = emptyMap(),
  attributeIds: Map<String, String> = emptyMap(),
  begin: String = "",
  end: String = "",
  ended: Boolean = false,
  sourceCredit: String = "",
  targetCredit: String = "",
  @field:FallbackOnNull public var area: Area = Area.NullArea
) : Relation(
  type,
  typeId,
  targetType,
  orderingKey,
  direction,
  attributes,
  attributeValues,
  attributeIds,
  begin,
  end,
  ended,
  sourceCredit,
  targetCredit
) {
  public companion object {
    public val NullAreaRelation: AreaRelation = AreaRelation(type = NullObject.NAME)
    public val fallbackMapping: Pair<String, Any> =
      AreaRelation::class.java.name to NullAreaRelation
  }
}

public val AreaRelation.isNullObject: Boolean
  get() = this === NullAreaRelation

/**
 * * [Artist-Artist](https://musicbrainz.org/relationships/artist-artist)
 * * [Artist-Event](https://musicbrainz.org/relationships/artist-event)
 * * [Artist-Instrument](https://musicbrainz.org/relationships/artist-instrument)
 * * [Artist-Label](https://musicbrainz.org/relationships/artist-label)
 * * [Artist-Place](https://musicbrainz.org/relationships/artist-place)
 * * [Artist-Recording](https://musicbrainz.org/relationships/artist-recording)
 * * [Artist-Release](https://musicbrainz.org/relationships/artist-release)
 * * [Artist-ReleaseGroup](https://musicbrainz.org/relationships/artist-release_group)
 * * [Artist-Series](https://musicbrainz.org/relationships/artist-series)
 * * [Artist-URL](https://musicbrainz.org/relationships/artist-url)
 * * [Artist-Work](https://musicbrainz.org/relationships/artist-work)
 */
@JsonClass(generateAdapter = true)
public class ArtistRelation(
  type: String = "",
  typeId: String = "",
  targetType: String = "",
  orderingKey: Int = 0,
  direction: String = "",
  attributes: List<String> = emptyList(),
  attributeValues: Map<String, String> = emptyMap(),
  attributeIds: Map<String, String> = emptyMap(),
  begin: String = "",
  end: String = "",
  ended: Boolean = false,
  sourceCredit: String = "",
  targetCredit: String = "",
  @field:FallbackOnNull public var artist: Artist = Artist.NullArtist
) : Relation(
  type,
  typeId,
  targetType,
  orderingKey,
  direction,
  attributes,
  attributeValues,
  attributeIds,
  begin,
  end,
  ended,
  sourceCredit,
  targetCredit
) {
  public companion object {
    public val NullArtistRelation: ArtistRelation = ArtistRelation(type = NullObject.NAME)
    public val fallbackMapping: Pair<String, Any> =
      ArtistRelation::class.java.name to NullArtistRelation
  }
}

public val ArtistRelation.isNullObject: Boolean
  get() = this === NullArtistRelation

/**
 * * [Event-Event](https://musicbrainz.org/relationships/event-event)
 * * [Event-Place](https://musicbrainz.org/relationships/event-place)
 * * [Event-Recording](https://musicbrainz.org/relationships/event-recording)
 * * [Event-Release](https://musicbrainz.org/relationships/event-release)
 * * [Event-ReleaseGroup](https://musicbrainz.org/relationships/event-release_group)
 * * [Event-Series](https://musicbrainz.org/relationships/event-series)
 * * [Event-URL](https://musicbrainz.org/relationships/event-url)
 * * [Event-Work](https://musicbrainz.org/relationships/event-work)
 */
@JsonClass(generateAdapter = true)
public class EventRelation(
  type: String = "",
  typeId: String = "",
  targetType: String = "",
  orderingKey: Int = 0,
  direction: String = "",
  attributes: List<String> = emptyList(),
  attributeValues: Map<String, String> = emptyMap(),
  attributeIds: Map<String, String> = emptyMap(),
  begin: String = "",
  end: String = "",
  ended: Boolean = false,
  sourceCredit: String = "",
  targetCredit: String = "",
  @field:FallbackOnNull public var event: Event = Event.NullEvent
) : Relation(
  type,
  typeId,
  targetType,
  orderingKey,
  direction,
  attributes,
  attributeValues,
  attributeIds,
  begin,
  end,
  ended,
  sourceCredit,
  targetCredit
) {
  public companion object {
    public val NullEventRelation: EventRelation = EventRelation(type = NullObject.NAME)
    public val fallbackMapping: Pair<String, Any> =
      EventRelation::class.java.name to NullEventRelation
  }
}

public val EventRelation.isNullObject: Boolean
  get() = this === NullEventRelation

/**
 * * [Instrument-Instrument](https://musicbrainz.org/relationships/instrument-instrument)
 * * [Instrument-Label](https://musicbrainz.org/relationships/instrument-label)
 * * [Instrument-URL](https://musicbrainz.org/relationships/instrument-url)
 */
@JsonClass(generateAdapter = true)
public class InstrumentRelation(
  type: String = "",
  typeId: String = "",
  targetType: String = "",
  orderingKey: Int = 0,
  direction: String = "",
  attributes: List<String> = emptyList(),
  attributeValues: Map<String, String> = emptyMap(),
  attributeIds: Map<String, String> = emptyMap(),
  begin: String = "",
  end: String = "",
  ended: Boolean = false,
  sourceCredit: String = "",
  targetCredit: String = "",
  @field:FallbackOnNull public var instrument: Instrument = Instrument.NullInstrument
) : Relation(
  type,
  typeId,
  targetType,
  orderingKey,
  direction,
  attributes,
  attributeValues,
  attributeIds,
  begin,
  end,
  ended,
  sourceCredit,
  targetCredit
) {
  public companion object {
    public val NullInstrumentRelation: InstrumentRelation =
      InstrumentRelation(type = NullObject.NAME)
    public val fallbackMapping: Pair<String, Any> =
      InstrumentRelation::class.java.name to NullInstrumentRelation
  }
}

public val InstrumentRelation.isNullObject: Boolean
  get() = this === NullInstrumentRelation

/**
 * * [Label-Label](https://musicbrainz.org/relationships/label-label)
 * * [Label-Recording](https://musicbrainz.org/relationships/label-recording)
 * * [Label-Release](https://musicbrainz.org/relationships/label-release)
 * * [Label-ReleaseGroup](https://musicbrainz.org/relationships/label-release_group)
 * * [Label-Series](https://musicbrainz.org/relationships/label-series)
 * * [Label-URL](https://musicbrainz.org/relationships/label-url)
 * * [Label-Work](https://musicbrainz.org/relationships/label-work)
 */
@JsonClass(generateAdapter = true)
public class LabelRelation(
  type: String = "",
  typeId: String = "",
  targetType: String = "",
  orderingKey: Int = 0,
  direction: String = "",
  attributes: List<String> = emptyList(),
  attributeValues: Map<String, String> = emptyMap(),
  attributeIds: Map<String, String> = emptyMap(),
  begin: String = "",
  end: String = "",
  ended: Boolean = false,
  sourceCredit: String = "",
  targetCredit: String = "",
  @field:FallbackOnNull public var label: Label = Label.NullLabel
) : Relation(
  type,
  typeId,
  targetType,
  orderingKey,
  direction,
  attributes,
  attributeValues,
  attributeIds,
  begin,
  end,
  ended,
  sourceCredit,
  targetCredit
) {
  public companion object {
    public val NullLabelRelation: LabelRelation = LabelRelation(type = NullObject.NAME)
    public val fallbackMapping: Pair<String, Any> =
      LabelRelation::class.java.name to NullLabelRelation
  }
}

public val LabelRelation.isNullObject: Boolean
  get() = this === NullLabelRelation

/**
 * * [Place-Place](https://musicbrainz.org/relationships/place-place)
 * * [Place-Recording](https://musicbrainz.org/relationships/place-recording)
 * * [Place-Release](https://musicbrainz.org/relationships/place-release)
 * * [Place-URL](https://musicbrainz.org/relationships/place-url)
 * * [Place-Work](https://musicbrainz.org/relationships/place-work)
 */
@JsonClass(generateAdapter = true)
public class PlaceRelation(
  type: String = "",
  typeId: String = "",
  targetType: String = "",
  orderingKey: Int = 0,
  direction: String = "",
  attributes: List<String> = emptyList(),
  attributeValues: Map<String, String> = emptyMap(),
  attributeIds: Map<String, String> = emptyMap(),
  begin: String = "",
  end: String = "",
  ended: Boolean = false,
  sourceCredit: String = "",
  targetCredit: String = "",
  @field:FallbackOnNull public var place: Place = Place.NullPlace
) : Relation(
  type,
  typeId,
  targetType,
  orderingKey,
  direction,
  attributes,
  attributeValues,
  attributeIds,
  begin,
  end,
  ended,
  sourceCredit,
  targetCredit
) {
  public companion object {
    public val NullPlaceRelation: PlaceRelation = PlaceRelation(type = NullObject.NAME)
    public val fallbackMapping: Pair<String, Any> =
      PlaceRelation::class.java.name to NullPlaceRelation
  }
}

public val PlaceRelation.isNullObject: Boolean
  get() = this === NullPlaceRelation

/**
 * * [Recording-Recording](https://musicbrainz.org/relationships/recording-recording)
 * * [Recording-Release](https://musicbrainz.org/relationships/recording-release)
 * * [Recording-Series](https://musicbrainz.org/relationships/recording-series)
 * * [Recording-URL](https://musicbrainz.org/relationships/recording-url)
 * * [Recording-Work](https://musicbrainz.org/relationships/recording-work)
 */
@JsonClass(generateAdapter = true)
public class RecordingRelation(
  type: String = "",
  typeId: String = "",
  targetType: String = "",
  orderingKey: Int = 0,
  direction: String = "",
  attributes: List<String> = emptyList(),
  attributeValues: Map<String, String> = emptyMap(),
  attributeIds: Map<String, String> = emptyMap(),
  begin: String = "",
  end: String = "",
  ended: Boolean = false,
  sourceCredit: String = "",
  targetCredit: String = "",
  @field:FallbackOnNull public var recording: Recording = Recording.NullRecording
) : Relation(
  type,
  typeId,
  targetType,
  orderingKey,
  direction,
  attributes,
  attributeValues,
  attributeIds,
  begin,
  end,
  ended,
  sourceCredit,
  targetCredit
) {
  public companion object {
    public val NullRecordingRelation: RecordingRelation = RecordingRelation(type = NullObject.NAME)
    public val fallbackMapping: Pair<String, Any> =
      RecordingRelation::class.java.name to NullRecordingRelation
  }
}

public val RecordingRelation.isNullObject: Boolean
  get() = this === NullRecordingRelation

/**
 * * [Release-Release](https://musicbrainz.org/relationships/release-release)
 * * [Release-Series](https://musicbrainz.org/relationships/release-series)
 * * [Release-URL](https://musicbrainz.org/relationships/release-url)
 */
@JsonClass(generateAdapter = true)
public class ReleaseRelation(
  type: String = "",
  typeId: String = "",
  targetType: String = "",
  orderingKey: Int = 0,
  direction: String = "",
  attributes: List<String> = emptyList(),
  attributeValues: Map<String, String> = emptyMap(),
  attributeIds: Map<String, String> = emptyMap(),
  begin: String = "",
  end: String = "",
  ended: Boolean = false,
  sourceCredit: String = "",
  targetCredit: String = "",
  @field:FallbackOnNull public var release: Release = Release.NullRelease
) : Relation(
  type,
  typeId,
  targetType,
  orderingKey,
  direction,
  attributes,
  attributeValues,
  attributeIds,
  begin,
  end,
  ended,
  sourceCredit,
  targetCredit
) {
  public companion object {
    public val NullReleaseRelation: ReleaseRelation = ReleaseRelation(type = NullObject.NAME)
    public val fallbackMapping: Pair<String, Any> =
      ReleaseRelation::class.java.name to NullReleaseRelation
  }
}

public val ReleaseRelation.isNullObject: Boolean
  get() = this === NullReleaseRelation

/**
 * * [ReleaseGroup-ReleaseGroup](https://musicbrainz.org/relationships/release_group-release_group)
 * * [ReleaseGroup-Series](https://musicbrainz.org/relationships/release_group-series)
 * * [ReleaseGroup-URL](https://musicbrainz.org/relationships/release_group-url)
 */
@JsonClass(generateAdapter = true)
public class ReleaseGroupRelation(
  type: String = "",
  typeId: String = "",
  targetType: String = "",
  orderingKey: Int = 0,
  direction: String = "",
  attributes: List<String> = emptyList(),
  attributeValues: Map<String, String> = emptyMap(),
  attributeIds: Map<String, String> = emptyMap(),
  begin: String = "",
  end: String = "",
  ended: Boolean = false,
  sourceCredit: String = "",
  targetCredit: String = "",
  @field:Json(name = "release_group") @field:FallbackOnNull
  public var releaseGroup: ReleaseGroup = ReleaseGroup.NullReleaseGroup
) : Relation(
  type,
  typeId,
  targetType,
  orderingKey,
  direction,
  attributes,
  attributeValues,
  attributeIds,
  begin,
  end,
  ended,
  sourceCredit,
  targetCredit
) {
  public companion object {
    public val NullReleaseGroupRelation: ReleaseGroupRelation =
      ReleaseGroupRelation(type = NullObject.NAME)
    public val fallbackMapping: Pair<String, Any> =
      ReleaseGroupRelation::class.java.name to NullReleaseGroupRelation
  }
}

public val ReleaseGroupRelation.isNullObject: Boolean
  get() = this === NullReleaseGroupRelation

/**
 * * [Series-Series](https://musicbrainz.org/relationships/series-series)
 * * [Series-URL](https://musicbrainz.org/relationships/series-url)
 * * [Series-Work](https://musicbrainz.org/relationships/series-work)
 */
@JsonClass(generateAdapter = true)
public class SeriesRelation(
  type: String = "",
  typeId: String = "",
  targetType: String = "",
  orderingKey: Int = 0,
  direction: String = "",
  attributes: List<String> = emptyList(),
  attributeValues: Map<String, String> = emptyMap(),
  attributeIds: Map<String, String> = emptyMap(),
  begin: String = "",
  end: String = "",
  ended: Boolean = false,
  sourceCredit: String = "",
  targetCredit: String = "",
  @field:FallbackOnNull public var series: Series = Series.NullSeries
) : Relation(
  type,
  typeId,
  targetType,
  orderingKey,
  direction,
  attributes,
  attributeValues,
  attributeIds,
  begin,
  end,
  ended,
  sourceCredit,
  targetCredit
) {
  public companion object {
    public val NullSeriesRelation: SeriesRelation = SeriesRelation(type = NullObject.NAME)
    public val fallbackMapping: Pair<String, Any> =
      SeriesRelation::class.java.name to NullSeriesRelation
  }
}

public val SeriesRelation.isNullObject: Boolean
  get() = this === NullSeriesRelation

/**
 * * [Url-Work](https://musicbrainz.org/relationships/url-work)
 */
@JsonClass(generateAdapter = true)
public class UrlRelation(
  type: String = "",
  typeId: String = "",
  targetType: String = "",
  orderingKey: Int = 0,
  direction: String = "",
  attributes: List<String> = emptyList(),
  attributeValues: Map<String, String> = emptyMap(),
  attributeIds: Map<String, String> = emptyMap(),
  begin: String = "",
  end: String = "",
  ended: Boolean = false,
  sourceCredit: String = "",
  targetCredit: String = "",
  @field:FallbackOnNull public var url: Url = Url.NullUrl
) : Relation(
  type,
  typeId,
  targetType,
  orderingKey,
  direction,
  attributes,
  attributeValues,
  attributeIds,
  begin,
  end,
  ended,
  sourceCredit,
  targetCredit
) {
  public companion object {
    public val NullUrlRelation: UrlRelation = UrlRelation(type = NullObject.NAME)
    public val fallbackMapping: Pair<String, Any> = UrlRelation::class.java.name to NullUrlRelation
  }
}

public val UrlRelation.isNullObject: Boolean
  get() = this === NullUrlRelation

/**
 * * [Work-Work](https://musicbrainz.org/relationships/work-work)
 */
@JsonClass(generateAdapter = true)
public class WorkRelation(
  type: String = "",
  typeId: String = "",
  targetType: String = "",
  orderingKey: Int = 0,
  direction: String = "",
  attributes: List<String> = emptyList(),
  attributeValues: Map<String, String> = emptyMap(),
  attributeIds: Map<String, String> = emptyMap(),
  begin: String = "",
  end: String = "",
  ended: Boolean = false,
  sourceCredit: String = "",
  targetCredit: String = "",
  @field:FallbackOnNull public var work: Work = Work.NullWork
) : Relation(
  type,
  typeId,
  targetType,
  orderingKey,
  direction,
  attributes,
  attributeValues,
  attributeIds,
  begin,
  end,
  ended,
  sourceCredit,
  targetCredit
) {
  public companion object {
    public val NullWorkRelation: WorkRelation = WorkRelation(type = NullObject.NAME)
    public val fallbackMapping: Pair<String, Any> =
      WorkRelation::class.java.name to NullWorkRelation
  }
}

public val WorkRelation.isNullObject: Boolean
  get() = this === NullWorkRelation
