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

@file:Suppress("MagicNumber")

package com.ealva.ealvabrainz.common

import com.ealva.ealvabrainz.brainz.data.Annotation
import com.ealva.ealvabrainz.brainz.data.Area
import com.ealva.ealvabrainz.brainz.data.Artist
import com.ealva.ealvabrainz.brainz.data.Event
import com.ealva.ealvabrainz.brainz.data.Genre
import com.ealva.ealvabrainz.brainz.data.Instrument
import com.ealva.ealvabrainz.brainz.data.Label
import com.ealva.ealvabrainz.brainz.data.Packaging
import com.ealva.ealvabrainz.brainz.data.Place
import com.ealva.ealvabrainz.brainz.data.Recording
import com.ealva.ealvabrainz.brainz.data.Release
import com.ealva.ealvabrainz.brainz.data.ReleaseGroup
import com.ealva.ealvabrainz.brainz.data.Series
import com.ealva.ealvabrainz.brainz.data.Track
import com.ealva.ealvabrainz.brainz.data.Url
import com.ealva.ealvabrainz.brainz.data.Work

/**
 * One of MusicBrainz' aims is to be the universal lingua franca for music by providing a reliable
 * and unambiguous form of music identification; this music identification is performed through the
 * use of MusicBrainz Identifiers (MBIDs).
 *
 * In a nutshell, an MBID is a 36 character Universally Unique Identifier that is permanently
 * assigned to each entity in the database, i.e. artists, release groups, releases, recordings,
 * works, labels, areas, places and URLs. MBIDs are also assigned to Tracks, though tracks do not
 * share many other properties of entities. For example, the artist Queen has an artist MBID of
 * 0383dadf-2a4e-4d10-a46a-e9e041da8eb3, and their song Bohemian Rhapsody has a recording MBID
 * of b1a9c0e9-d987-4042-ae91-78d6a3267d69.
 *
 * An entity can have more than one MBID. When an entity is merged into another, its MBIDs redirect
 * to the other entity.
 */
public interface Mbid {
  /**
   * In its canonical textual representation, the 16 octets of a
   * [UUID](https://en.wikipedia.org/wiki/Universally_unique_identifier) are represented as 32
   * hexadecimal (base-16) digits, displayed in 5 groups separated by hyphens, in the form
   * 8-4-4-4-12 for a total of 36 characters (32 hexadecimal characters and 4 hyphens). For example:
   *
   * * 123e4567-e89b-12d3-a456-426655440000
   * * xxxxxxxx-xxxx-Mxxx-Nxxx-xxxxxxxxxxxx
   *
   * The 4 bit M and the 1 to 3 bit N fields code the format of the UUID itself. The 4 bits of digit
   * M are the UUID version, and the 1 to 3 most significant bits of digit N code the UUID variant.
   * (See below.) In the example, M is 1, and N is a (10xx2), meaning that this is a version-1,
   * variant-1 UUID; that is, a time-based DCE/RFC 4122 UUID.
   *
   * The canonical 8-4-4-4-12 format string is based on the record layout for the 16 bytes of the
   * UUID
   */
  public val value: String
}

/**
 * MBID of an unknown type of MusicBrainz entity. As an example of when this might happen, when
 * an annotation has a "type" and "entity" ID and the "type" is unrecognized. See the Annotation
 * class in brainz.data
 */
@JvmInline
public value class UnknownEntityMbid(override val value: String) : Mbid

public inline val Mbid.isValid: Boolean
  get() = value.isValidMbid()

public inline val Mbid.isInvalid: Boolean
  get() = !isValid

/**
 * Determines if this is superseded by [newValue]. If this is invalid or, [newValue] is valid
 * and != this, then this should be replaced by [newValue]. This may occur when reading a type of
 * MBID from a file tag that has change since last parse or if reading a "more accurate" MBID from
 * MusicBrainz
 */
public fun <T : Mbid> T.isObsolete(newValue: T?): Boolean {
  return (newValue != null && newValue.isValid && (this.isInvalid || newValue != this))
}

@JvmInline
public value class AreaMbid(override val value: String) : Mbid

public inline val Area.mbid: AreaMbid
  get() = AreaMbid(id)

@JvmInline
public value class ArtistMbid(override val value: String) : Mbid {
  @Suppress("unused")
  public companion object {

    // https://musicbrainz.org/doc/Style/Unknown_and_untitled/Special_purpose_artist
    public val ANONYMOUS: ArtistMbid = ArtistMbid("f731ccc4-e22a-43af-a747-64213329e088")
    public val DATA: ArtistMbid = ArtistMbid("33cf029c-63b0-41a0-9855-be2a3665fb3b")
    public val DIALOGUE: ArtistMbid = ArtistMbid("314e1c25-dde7-4e4d-b2f4-0a7b9f7c56dc")
    public val NO_ARTIST: ArtistMbid = ArtistMbid("eec63d3c-3b81-4ad4-b1e4-7c147d4d2b61")
    public val TRADITIONAL: ArtistMbid = ArtistMbid("9be7f096-97ec-4615-8957-8d40b5dcbc41")
    public val UNKNOWN: ArtistMbid = ArtistMbid("125ec42a-7229-4250-afc5-e057484327fe")
    public val VARIOUS_ARTISTS: ArtistMbid = ArtistMbid("89ad4ac3-39f7-470e-963a-56509c546377")
    public val CHRISTMAS_MUSIC: ArtistMbid = ArtistMbid("0187fe48-c87d-4dd8-beca-9c07ef535603")
    public val DISNEY: ArtistMbid = ArtistMbid("66ea0139-149f-4a0c-8fbf-5ea9ec4a6e49")
    public val MUSICAL_THEATER: ArtistMbid = ArtistMbid("a0ef7e1d-44ff-4039-9435-7d5fefdeecc9")
    public val CLASSICAL_MUSIC: ArtistMbid = ArtistMbid("9e44f539-f3fc-4120-bce2-94c8716437fa")
    public val SOUNDTRACK: ArtistMbid = ArtistMbid("d6bd72bc-b1e2-4525-92aa-0f853cbb41bf")
    public val RELIGIOUS_MUSIC: ArtistMbid = ArtistMbid("ae636985-40e8-4010-ae02-0f35930f8017")
    public val CHURCH_CHIMES: ArtistMbid = ArtistMbid("90068d37-bae7-4292-be4a-704c145bd616")
    public val LANGUAGE_INSTRUCTION: ArtistMbid = ArtistMbid("80a8851f-444c-4539-892b-ad2a49292aa9")
    public val NATURE_SOUNDS: ArtistMbid = ArtistMbid("51118c9d-965d-4f9f-89a1-0091837ccf54")
    public val NEWS_REPORT: ArtistMbid = ArtistMbid("49e713ce-c3be-4697-8983-ee7cd0a11ea1")
  }
}

public inline val Artist.mbid: ArtistMbid
  get() = ArtistMbid(id)

@JvmInline
public value class CollectionMbid(override val value: String) : Mbid

public inline val com.ealva.ealvabrainz.brainz.data.Collection.mbid: CollectionMbid
  get() = CollectionMbid(id)

@JvmInline
public value class EventMbid(override val value: String) : Mbid

public inline val Event.mbid: EventMbid
  get() = EventMbid(id)

@JvmInline
public value class GenreMbid(override val value: String) : Mbid

public inline val Genre.mbid: GenreMbid
  get() = GenreMbid(id)

@JvmInline
public value class InstrumentMbid(override val value: String) : Mbid

public inline val Instrument.mbid: InstrumentMbid
  get() = InstrumentMbid(id)

@JvmInline
public value class LabelMbid(override val value: String) : Mbid

public inline val Label.mbid: LabelMbid
  get() = LabelMbid(id)

@JvmInline
public value class PackagingMbid(override val value: String) : Mbid

public inline val Packaging.mbid: PackagingMbid
  get() = PackagingMbid(id)

@JvmInline
public value class PlaceMbid(override val value: String) : Mbid

public inline val Place.mbid: PlaceMbid
  get() = PlaceMbid(id)

@JvmInline
public value class RecordingMbid(override val value: String) : Mbid

public inline val Recording.mbid: RecordingMbid
  get() = RecordingMbid(id)

@JvmInline
public value class ReleaseMbid(override val value: String) : Mbid

public inline val Release.mbid: ReleaseMbid
  get() = ReleaseMbid(id)

@JvmInline
public value class ReleaseGroupMbid(override val value: String) : Mbid

public inline val ReleaseGroup.mbid: ReleaseGroupMbid
  get() = ReleaseGroupMbid(id)

@JvmInline
public value class SeriesMbid(override val value: String) : Mbid

public inline val Series.mbid: SeriesMbid
  get() = SeriesMbid(id)

@JvmInline
public value class TrackMbid(override val value: String) : Mbid

public inline val Track.mbid: TrackMbid
  get() = TrackMbid(id)

@JvmInline
public value class UrlMbid(override val value: String) : Mbid

public inline val Url.mbid: UrlMbid
  get() = UrlMbid(id)

@JvmInline
public value class WorkMbid(override val value: String) : Mbid

public inline val Work.mbid: WorkMbid
  get() = WorkMbid(id)

public fun String.isValidMbid(): Boolean {
  return length == MBID_LENGTH &&
    DASHES.all { this[it] == '-' } &&
    RANGES.all { rangeIsHex(it) }
}

private const val MBID_LENGTH = 36
private val DASHES: IntArray = intArrayOf(8, 13, 18, 23)
private val FIRST_GROUP: IntRange = 0..7
private val SECOND_GROUP: IntRange = 9..12
private val THIRD_GROUP: IntRange = 14..17
private val FOURTH_GROUP: IntRange = 19..22
private val FIFTH_GROUP: IntRange = 24..35

private val RANGES = arrayOf(FIRST_GROUP, SECOND_GROUP, THIRD_GROUP, FOURTH_GROUP, FIFTH_GROUP)

public fun String.rangeIsHex(range: IntRange): Boolean {
  return range.all { this[it].isHex() }
}

private fun Char.isHex(): Boolean = when (this) {
  '0' -> true
  '1' -> true
  '2' -> true
  '3' -> true
  '4' -> true
  '5' -> true
  '6' -> true
  '7' -> true
  '8' -> true
  '9' -> true
  'a' -> true
  'b' -> true
  'c' -> true
  'd' -> true
  'e' -> true
  'f' -> true
  'A' -> true
  'B' -> true
  'C' -> true
  'D' -> true
  'E' -> true
  'F' -> true
  else -> false
}

public inline val Annotation.areaMbid: AreaMbid
  get() = AreaMbid(entity)
public inline val Annotation.artistMbid: ArtistMbid
  get() = ArtistMbid(entity)
public inline val Annotation.eventMbid: EventMbid
  get() = EventMbid(entity)
public inline val Annotation.labelMbid: LabelMbid
  get() = LabelMbid(entity)
public inline val Annotation.placeMbid: PlaceMbid
  get() = PlaceMbid(entity)
public inline val Annotation.recordingMbid: RecordingMbid
  get() = RecordingMbid(entity)
public inline val Annotation.releaseMbid: ReleaseMbid
  get() = ReleaseMbid(entity)
public inline val Annotation.releaseGroupMbid: ReleaseGroupMbid
  get() = ReleaseGroupMbid(entity)
public inline val Annotation.seriesMbid: SeriesMbid
  get() = SeriesMbid(entity)
public inline val Annotation.workMbid: WorkMbid
  get() = WorkMbid(entity)

@Suppress("unused")
public fun Annotation.entityMbid(): Mbid = when (type) {
  "area" -> areaMbid
  "artist" -> artistMbid
  "event" -> eventMbid
  "label" -> labelMbid
  "place" -> placeMbid
  "recording" -> recordingMbid
  "release" -> releaseMbid
  "release-group" -> releaseGroupMbid
  "series" -> seriesMbid
  "work" -> workMbid
  else -> UnknownEntityMbid(entity)
}
