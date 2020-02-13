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
import com.ealva.ealvabrainz.brainz.data.LabelRelation.Companion.NullLabelRelation
import com.ealva.ealvabrainz.brainz.data.PlaceRelation.Companion.NullPlaceRelation
import com.ealva.ealvabrainz.brainz.data.RecordingRelation.Companion.NullRecordingRelation
import com.ealva.ealvabrainz.brainz.data.Release.Companion.NullRelease
import com.ealva.ealvabrainz.brainz.data.ReleaseGroupRelation.Companion.NullReleaseGroupRelation
import com.ealva.ealvabrainz.brainz.data.ReleaseRelation.Companion.NullReleaseRelation
import com.ealva.ealvabrainz.brainz.data.Target.Companion.NullTarget
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
sealed class Relation(
  var type: String = "",
  @field:Json(name = "type-id")var typeId: String = "",
  @field:FallbackOnNull var target: Target = NullTarget,
  @field:Json(name = "ordering-key")var orderingKey: Int = 0,
  var direction: String = "",
  @field:Json(name = "attribute-list") var attributeList: List<Attribute> = emptyList(),
  var begin: String = "",
  var end: String = "",
  var ended: Boolean = false,
  @field:Json(name = "source-credit") var sourceCredit: String = "",
  @field:Json(name = "target-credit") var targetCredit: String = ""
)

/**
 * * [Area-Area](https://musicbrainz.org/relationships/area-area)
 * * [Area-Instrument](https://musicbrainz.org/relationships/area-instrument)
 * * [Area-Recording](https://musicbrainz.org/relationships/area-recording)
 * * [Area-Release](https://musicbrainz.org/relationships/area-release)
 * * [Area-URL](https://musicbrainz.org/relationships/area-url)
 * * [Area-Work](https://musicbrainz.org/relationships/area-work)
 */
@JsonClass(generateAdapter = true)
class AreaRelation(
  type: String = "",
  typeId: String = "",
  target: Target = NullTarget,
  orderingKey: Int = 0,
  direction: String = "",
  attributeList: List<Attribute> = emptyList(),
  begin: String = "",
  end: String = "",
  ended: Boolean = false,
  sourceCredit: String = "",
  targetCredit: String = "",
  @field:FallbackOnNull var area: Area = Area.NullArea
) : Relation(type, typeId, target, orderingKey, direction, attributeList, begin, end, ended, sourceCredit, targetCredit) {
  companion object {
    val NullAreaRelation = AreaRelation(type = NullObject.NAME)
    val fallbackMapping: Pair<String, Any> = AreaRelation::class.java.name to NullAreaRelation
  }
}

val AreaRelation.isNullObject
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
class ArtistRelation(
  type: String = "",
  typeId: String = "",
  target: Target = NullTarget,
  orderingKey: Int = 0,
  direction: String = "",
  attributeList: List<Attribute> = emptyList(),
  begin: String = "",
  end: String = "",
  ended: Boolean = false,
  sourceCredit: String = "",
  targetCredit: String = "",
  @field:FallbackOnNull var artist: Artist = Artist.NullArtist
) : Relation(type, typeId, target, orderingKey, direction, attributeList, begin, end, ended, sourceCredit, targetCredit) {
  companion object {
    val NullArtistRelation = ArtistRelation(type = NullObject.NAME)
    val fallbackMapping: Pair<String, Any> = ArtistRelation::class.java.name to NullArtistRelation
  }
}

val ArtistRelation.isNullObject
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
class EventRelation(
  type: String = "",
  typeId: String = "",
  target: Target = NullTarget,
  orderingKey: Int = 0,
  direction: String = "",
  attributeList: List<Attribute> = emptyList(),
  begin: String = "",
  end: String = "",
  ended: Boolean = false,
  sourceCredit: String = "",
  targetCredit: String = "",
  @field:FallbackOnNull var event: Event = Event.NullEvent
) : Relation(type, typeId, target, orderingKey, direction, attributeList, begin, end, ended, sourceCredit, targetCredit) {
  companion object {
    val NullEventRelation = EventRelation(type = NullObject.NAME)
    val fallbackMapping: Pair<String, Any> = EventRelation::class.java.name to NullEventRelation
  }
}

val EventRelation.isNullObject
  get() = this === NullEventRelation

/**
 * * [Label-Label](https://musicbrainz.org/relationships/label-label)
 * * [Label-Release](https://musicbrainz.org/relationships/label-release)
 * * [Label-ReleaseGroup](https://musicbrainz.org/relationships/label-release_group)
 * * [Label-Series](https://musicbrainz.org/relationships/label-series)
 * * [Label-URL](https://musicbrainz.org/relationships/label-url)
 * * [Label-Work](https://musicbrainz.org/relationships/label-work)
 */
@JsonClass(generateAdapter = true)
class LabelRelation(
  type: String = "",
  typeId: String = "",
  target: Target = NullTarget,
  orderingKey: Int = 0,
  direction: String = "",
  attributeList: List<Attribute> = emptyList(),
  begin: String = "",
  end: String = "",
  ended: Boolean = false,
  sourceCredit: String = "",
  targetCredit: String = "",
  @field:FallbackOnNull var label: Label = Label.NullLabel
) : Relation(type, typeId, target, orderingKey, direction, attributeList, begin, end, ended, sourceCredit, targetCredit) {
  companion object {
    val NullLabelRelation = LabelRelation(type = NullObject.NAME)
    val fallbackMapping: Pair<String, Any> = LabelRelation::class.java.name to NullLabelRelation
  }
}

val LabelRelation.isNullObject
  get() = this === NullLabelRelation

/**
 * * [Place-Place](https://musicbrainz.org/relationships/place-place)
 * * [Place-Recording](https://musicbrainz.org/relationships/place-recording)
 * * [Place-Release](https://musicbrainz.org/relationships/place-release)
 * * [Place-URL](https://musicbrainz.org/relationships/place-url)
 * * [Place-Work](https://musicbrainz.org/relationships/place-work)
 */
@JsonClass(generateAdapter = true)
class PlaceRelation(
  type: String = "",
  typeId: String = "",
  target: Target = NullTarget,
  orderingKey: Int = 0,
  direction: String = "",
  attributeList: List<Attribute> = emptyList(),
  begin: String = "",
  end: String = "",
  ended: Boolean = false,
  sourceCredit: String = "",
  targetCredit: String = "",
  @field:FallbackOnNull var place: Place = Place.NullPlace
) : Relation(type, typeId, target, orderingKey, direction, attributeList, begin, end, ended, sourceCredit, targetCredit) {
  companion object {
    val NullPlaceRelation = PlaceRelation(type = NullObject.NAME)
    val fallbackMapping: Pair<String, Any> = PlaceRelation::class.java.name to NullPlaceRelation
  }
}

val PlaceRelation.isNullObject
  get() = this === NullPlaceRelation

/**
 * * [Recording-Recording](https://musicbrainz.org/relationships/recording-recording)
 * * [Recording-Release](https://musicbrainz.org/relationships/recording-release)
 * * [Recording-Series](https://musicbrainz.org/relationships/recording-series)
 * * [Recording-URL](https://musicbrainz.org/relationships/recording-url)
 * * [Recording-Work](https://musicbrainz.org/relationships/recording-work)
 */
@JsonClass(generateAdapter = true)
class RecordingRelation(
  type: String = "",
  typeId: String = "",
  target: Target = NullTarget,
  orderingKey: Int = 0,
  direction: String = "",
  attributeList: List<Attribute> = emptyList(),
  begin: String = "",
  end: String = "",
  ended: Boolean = false,
  sourceCredit: String = "",
  targetCredit: String = "",
  @field:FallbackOnNull var recording: Recording= Recording.NullRecording
) : Relation(type, typeId, target, orderingKey, direction, attributeList, begin, end, ended, sourceCredit, targetCredit) {
  companion object {
    val NullRecordingRelation = RecordingRelation(type = NullObject.NAME)
    val fallbackMapping: Pair<String, Any> = RecordingRelation::class.java.name to NullRecordingRelation
  }
}

val RecordingRelation.isNullObject
  get() = this === NullRecordingRelation

/**
 * * [Release-Release](https://musicbrainz.org/relationships/release-release)
 * * [Release-Series](https://musicbrainz.org/relationships/release-series)
 * * [Release-URL](https://musicbrainz.org/relationships/release-url)
 */
@JsonClass(generateAdapter = true)
class ReleaseRelation(
  type: String = "",
  typeId: String = "",
  target: Target = NullTarget,
  orderingKey: Int = 0,
  direction: String = "",
  attributeList: List<Attribute> = emptyList(),
  begin: String = "",
  end: String = "",
  ended: Boolean = false,
  sourceCredit: String = "",
  targetCredit: String = "",
  @field:FallbackOnNull var release: Release = NullRelease
) : Relation(type, typeId, target, orderingKey, direction, attributeList, begin, end, ended, sourceCredit, targetCredit) {
  companion object {
    val NullReleaseRelation = ReleaseRelation(type = NullObject.NAME)
    val fallbackMapping: Pair<String, Any> = ReleaseRelation::class.java.name to NullReleaseRelation
  }
}

val ReleaseRelation.isNullObject
  get() = this === NullReleaseRelation

/**
 * * [ReleaseGroup-ReleaseGroup](https://musicbrainz.org/relationships/release_group-release_group)
 * * [ReleaseGroup-Series](https://musicbrainz.org/relationships/release_group-series)
 * * [ReleaseGroup-URL](https://musicbrainz.org/relationships/release_group-url)
 */
@JsonClass(generateAdapter = true)
class ReleaseGroupRelation(
  type: String = "",
  typeId: String = "",
  target: Target = NullTarget,
  orderingKey: Int = 0,
  direction: String = "",
  attributeList: List<Attribute> = emptyList(),
  begin: String = "",
  end: String = "",
  ended: Boolean = false,
  sourceCredit: String = "",
  targetCredit: String = "",
  @field:Json(name = "release-group") @field:FallbackOnNull var releaseGroup: ReleaseGroup = ReleaseGroup.NullReleaseGroup
) : Relation(type, typeId, target, orderingKey, direction, attributeList, begin, end, ended, sourceCredit, targetCredit) {
  companion object {
    val NullReleaseGroupRelation = ReleaseGroupRelation(type = NullObject.NAME)
    val fallbackMapping: Pair<String, Any> = ReleaseGroupRelation::class.java.name to NullReleaseGroupRelation
  }
}

val ReleaseGroupRelation.isNullObject
  get() = this === NullReleaseGroupRelation

/**
 * * [Work-Work](https://musicbrainz.org/relationships/work-work)
 */
@JsonClass(generateAdapter = true)
class WorkRelation(
  type: String = "",
  typeId: String = "",
  target: Target = NullTarget,
  orderingKey: Int = 0,
  direction: String = "",
  attributeList: List<Attribute> = emptyList(),
  begin: String = "",
  end: String = "",
  ended: Boolean = false,
  sourceCredit: String = "",
  targetCredit: String = "",
  @field:FallbackOnNull var work: Work = Work.NullWork
) : Relation(type, typeId, target, orderingKey, direction, attributeList, begin, end, ended, sourceCredit, targetCredit) {
  companion object {
    val NullWorkRelation = WorkRelation(type = NullObject.NAME)
    val fallbackMapping: Pair<String, Any> = WorkRelation::class.java.name to NullWorkRelation
  }
}

val WorkRelation.isNullObject
  get() = this === NullWorkRelation

// @JsonClass(generateAdapter = true)
// class InstrumentRelation(
// type: String = "",
// typeId: String = "",
// target: Target = NullTarget,
// orderingKey: Int = 0,
// direction: String = "",
// attributeList: List<Attribute> = emptyList(),
// begin: String = "",
// end: String = "",
// ended: Boolean = false,
// sourceCredit: String = "",
// targetCredit: String = "",
// @field:FallbackOnNull var instrument: Instrument = Instrument.NullInstrument
// ) : Relation(type, typeId, target, orderingKey, direction, attributeList, begin, end, ended, sourceCredit, targetCredit) {
// companion object {
// val NullInstrumentRelation = InstrumentRelation(type = NullObject.NAME)
// val fallbackMapping: Pair<String, Any> = InstrumentRelation::class.java.name to NullInstrumentRelation
// }
// }
//
// val InstrumentRelation.isNullObject
// get() = this === NullInstrumentRelation
//
// @JsonClass(generateAdapter = true)
// class SeriesRelation(
// type: String = "",
// typeId: String = "",
// target: Target = NullTarget,
// orderingKey: Int = 0,
// direction: String = "",
// attributeList: List<Attribute> = emptyList(),
// begin: String = "",
// end: String = "",
// ended: Boolean = false,
// sourceCredit: String = "",
// targetCredit: String = "",
// @field:FallbackOnNull var series: Series = Series.NullSeries
// ) : Relation(type, typeId, target, orderingKey, direction, attributeList, begin, end, ended, sourceCredit, targetCredit) {
// companion object {
// val NullSeriesRelation = SeriesRelation(type = NullObject.NAME)
// val fallbackMapping: Pair<String, Any> = SeriesRelation::class.java.name to NullSeriesRelation
// }
// }
//
// val SeriesRelation.isNullObject
// get() = this === NullSeriesRelation
