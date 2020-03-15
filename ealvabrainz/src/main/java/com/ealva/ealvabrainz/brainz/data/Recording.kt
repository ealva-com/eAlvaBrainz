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

import com.ealva.ealvabrainz.brainz.data.Recording.Companion.NullRecording
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import timber.log.Timber

/**
 * A [recording](https://musicbrainz.org/doc/Recording) is an entity in MusicBrainz which can be
 * linked to [tracks](https://musicbrainz.org/doc/Track) on
 * [releases](https://musicbrainz.org/doc/Release). Each track must always be associated with a
 * single recording, but a recording can be linked to any number of tracks.
 *
 * A recording represents distinct audio that has been used to produce at least one released track
 * through copying or [mastering](https://musicbrainz.org/doc/Mix_Terminology#mastering). A
 * recording itself is never produced solely through copying or mastering.
 *
 * Generally, the audio represented by a recording corresponds to the audio at a stage in the
 * production process before any final mastering but after any editing or
 * [mixing](https://musicbrainz.org/doc/Mix_Terminology#mixing).
 */
@JsonClass(generateAdapter = true)
class Recording(
  /** Recording MusicBrainz ID (MBID) */
  var id: String = "",
  /** The title of the recording. */
  var title: String = "",
  /** The artist(s) that the recording is primarily credited to. */
  @field:Json(name = "artist-credit") var artistCredit: List<ArtistCredit> = emptyList(),
  /**
   * The length of the recording. It's only entered manually for standalone recordings. For
   * recordings that are being used on releases, the recording length is the median length of all
   * tracks (that have a track length) associated with that recording. If there is an even number
   * of track lengths, the smaller median candidate is used.
   */
  var length: Int = 0,
  var aliases: List<Alias> = emptyList(),
  /**
   * See the [page about disambiguation comments](https://musicbrainz.org/doc/Disambiguation_Comment)
   * for more information
   */
  var disambiguation: String = "",
  /**
   * See the [page about annotations](https://musicbrainz.org/doc/Annotation) for more information.
   */
  var annotation: String = "",
  var genres: List<Genre> = emptyList(),
  /** The International Standard Recording Code assigned to the recording. */
  var isrcs: List<String> = emptyList(),
  var rating: Rating = Rating.NullRating,
  var relations: List<Relation> = emptyList(),
  var releases: List<Release> = emptyList(),
  var tags: List<Tag> = emptyList(),
  /** If this recording is video */
  var video: Boolean = false,
  /** used with queries */
  var score: Int = 0
) {

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as Recording

    if (id != other.id) return false

    return true
  }

  override fun hashCode() = id.hashCode()

  override fun toString() = toJson()

  interface Lookup : Include

  @Suppress("unused")
  enum class Subquery(override val value: String) : Lookup {
    Artists("artists"),
    Releases("releases"),
    UrlRels("url-rels"),
    /** DiscIds requires [Releases] also be specified and inc params can (currently) repeat */
    DiscIds("releases+discids"),
    Media("media"),
    Isrcs("isrcs"),
    ArtistCredits("artist-credits")
  }

  @Suppress("unused")
  enum class Misc(override val value: String) : Lookup {
    Aliases("aliases"),       // include artist, label, area or work aliases; treat these as a set, as they are not deliberately ordered
    Annotation("annotation"),
    Tags("tags"),
    Ratings("ratings"),
    Genres("genres")
  }

  /**
   * Recording relationships
   *
   * * [Recording-Recording](https://musicbrainz.org/relationships/recording-recording)
   * * [Recording-Release](https://musicbrainz.org/relationships/recording-release)
   * * [Recording-Series](https://musicbrainz.org/relationships/recording-series)
   * * [Recording-URL](https://musicbrainz.org/relationships/recording-url)
   * * [Recording-Work](https://musicbrainz.org/relationships/recording-work)
   */
  enum class Relations(override val value: String) : Lookup {
    Recording("recording-rels"),
    Release("release-rels"),
    Series("series-rels"),
    Url("url-rels"),
    Work("work-rels")
  }

  @Suppress("unused")
  enum class SearchField(val value: String) {
    /** the artist's MBID */
    ArtistId("arid"),
    /** artist name is name(s) as it appears on the recording */
    Artist("artist"),
    /** an artist on the recording, each artist added as a separate field */
    ArtistName("artistname"),
    /** the artist's disambiguation comment */
    Comment("comment"),
    /** name credit on the recording, each artist added as a separate field */
    CreditName("creditname"),
    /**
     * the 2-letter code (ISO 3166-1 alpha-2) for the artist's main associated country, or “unknown”
     */
    Country("country"),
    /** recording release date */
    Date("date"),

    /** duration of track in milliseconds */
    Duration("dur"),
    /** recording release format */
    Format("format"),
    /**
     * The International Standard Recording Code, an identification system for audio and music
     * video recordings. Includes isrcs for all recordings
     */
    Isrc("isrc"),
    /** free text track number */
    Number("number"),
    /** the medium that the recording should be found on, first medium is position 1 */
    Position("position"),
    /** primary type of the release group (album, single, ep, other) */
    PrimaryType("primarytype"),
    /** quantized duration (duration / 2000) */
    QuantizedDuration("qdur"),
    /** name of recording or a track associated with the recording */
    Recording("recording"),
    /** name of the recording with any accent characters retained */
    RecordingAccent("recordingaccent"),
    /** release id */
    ReleaseId("reid"),
    /** release name */
    ReleaseName("release"),
    /** release group id */
    ReleaseGroupId("rgid"),
    /** recording id */
    RecordingId("rid"),
    /** secondary type of the release group (audiobook, compilation, interview, live, remix soundtrack, spokenword) */
    SecondaryType("secondarytype"),
    /** Release status (official, promotion, Bootleg, Pseudo-Release) */
    Status("status"),
    /** folksonomy tag */
    Tag("tag"),
    /** track id */
    TrackId("tid"),
    /** track number on medium */
    TrackNumber("tnum"),
    /** number of tracks in the medium on release */
    Tracks("tracks"),
    /** number of tracks on release as a whole */
    TracksRelease("tracksrelease"),
    /**
     * type of the release group, old type mapping for when we did not have separate primary and
     * secondary types or use standalone for standalone recordings
     */
    Type("type"),
    /** true to only show video tracks */
    Video("video"),
  }

  companion object {
    val NullRecording = Recording(id = NullObject.ID)
    val fallbackMapping: Pair<String, Any> = Recording::class.java.name to NullRecording
  }
}

val Recording.isNullObject: Boolean
  get() = this === NullRecording

inline class RecordingMbid(override val value: String) : Mbid

inline val Recording.mbid: RecordingMbid
  get() = id.toRecordingMbid()

@Suppress("NOTHING_TO_INLINE")
inline fun String.toRecordingMbid(): RecordingMbid {
  if (Mbid.logInvalidMbid && isInvalidMbid()) Timber.w("Invalid RecordingMbid")
  return RecordingMbid(this)
}
