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
public class Recording(
  /** Recording MusicBrainz ID (MBID) */
  public var id: String = "",
  /** The title of the recording. */
  public var title: String = "",
  /** The artist(s) that the recording is primarily credited to. */
  @field:Json(name = "artist-credit") public var artistCredit: List<ArtistCredit> = emptyList(),
  /**
   * The length of the recording. It's only entered manually for standalone recordings. For
   * recordings that are being used on releases, the recording length is the median length of all
   * tracks (that have a track length) associated with that recording. If there is an even number
   * of track lengths, the smaller median candidate is used.
   */
  public var length: Int = 0,
  @field:Json(name = "first-release-date") public var firstReleaseDate: String = "",
  public var aliases: List<Alias> = emptyList(),
  /**
   * See the
   * [page about disambiguation comments](https://musicbrainz.org/doc/Disambiguation_Comment)
   * for more information
   */
  public var disambiguation: String = "",
  /**
   * See the [page about annotations](https://musicbrainz.org/doc/Annotation) for more information.
   */
  public var annotation: String = "",
  public var genres: List<Genre> = emptyList(),
  /** The International Standard Recording Code assigned to the recording. */
  public var isrcs: List<String> = emptyList(),
  public var rating: Rating = Rating.NullRating,
  public var relations: List<Relation> = emptyList(),
  public var releases: List<Release> = emptyList(),
  public var tags: List<Tag> = emptyList(),
  /** If this recording is video */
  public var video: Boolean = false,
  /** used with queries */
  public var score: Int = 0
) {

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as Recording

    if (id != other.id) return false

    return true
  }

  override fun hashCode(): Int = id.hashCode()

  override fun toString(): String = toJson()

  public interface Lookup : Include

  @Suppress("unused")
  public enum class Subquery(override val value: String) : Lookup {
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
  public enum class Misc(override val value: String) : Lookup {
    Aliases("aliases"),
    Annotation("annotation"),
    Tags("tags"),
    Ratings("ratings"),
    Genres("genres")
  }

  @Suppress("unused")
  public enum class SearchField(public val value: String) {
    /** 	(part of) any alias attached to the recording (diacritics are ignored) */
    Alias("alias"),

    /** the MBID of any of the recording artists  */
    ArtistId("arid"),

    /** (part of) the combined credited artist name for the recording, including join phrases
     * (e.g. "Artist X feat.")
     */
    Artist("artist"),

    /** (part of) the name of any of the recording artists */
    ArtistName("artistname"),

    /** (part of) the recording's disambiguation comment  */
    Comment("comment"),

    /**
     * the 2-letter code (ISO 3166-1 alpha-2) for the country any release of this recording was
     * released in
     */
    Country("country"),

    /** (part of) the credited name of any of the recording artists on this particular recording */
    CreditName("creditname"),

    /** the release date of any release including this recording (e.g. "1980-01-22") */
    Date("date"),

    /** duration of track in milliseconds */
    Duration("dur"),

    /** the release date of the earliest release including this recording (e.g. "1980-01-22") */
    FirstReleaseDate("firstrelasedate"),

    /** recording release format */
    Format("format"),

    /**
     * The International Standard Recording Code, an identification system for audio and music
     * video recordings. Includes isrcs for all recordings
     */
    Isrc("isrc"),

    /** 	the free-text number of the track on any medium including this recording (e.g. "A4")  */
    Number("number"),

    /** the position inside its release of any medium including this recording (starts at 1)  */
    Position("position"),

    /** primary type of the release group (album, single, ep, other) */
    PrimaryType("primarytype"),

    /** the recording duration, quantized (duration in milliseconds / 2000)  */
    QuantizedDuration("qdur"),

    /**
     * (part of) the recording's name, or the name of a track connected to this recording
     * (diacritics are ignored)
     */
    Recording("recording"),

    /**
     * (part of) the recordings's name, or the name of a track connected to this recording (with the
     * specified diacritics)
     */
    RecordingAccent("recordingaccent"),

    /** (part of) the name of any release including this recording */
    Release("release"),

    /** the MBID of any release group including this recording */
    ReleaseId("reid"),

    /** the MBID of any release group including this recording */
    ReleaseGroupId("rgid"),

    /** recording id */
    RecordingId("rid"),

    /**
     * secondary type of the release group (audiobook, compilation, interview, live, remix
     * soundtrack, spokenword)
     */
    SecondaryType("secondarytype"),

    /** Release status (official, promotion, Bootleg, Pseudo-Release) */
    Status("status"),

    /** (part of) a tag attached to the recording  */
    Tag("tag"),

    /** the MBID of a track connected to this recording  */
    TrackId("tid"),

    /**
     * the position of the track on any medium including this recording (starts at 1, pre-gaps at 0)
     */
    TrackNumber("tnum"),

    /** the number of tracks on any medium including this recording */
    Tracks("tracks"),

    /** the number of tracks on any release (as a whole) including this recording */
    TracksRelease("tracksrelease"),

    /** a boolean flag (true/false) indicating whether or not the recording is a video recording */
    Video("video")
  }

  public companion object {
    public val NullRecording: Recording = Recording(id = NullObject.ID)
    public val fallbackMapping: Pair<String, Any> = Recording::class.java.name to NullRecording
  }
}

public val Recording.isNullObject: Boolean
  get() = this === NullRecording

public inline class RecordingMbid(override val value: String) : Mbid

public inline val Recording.mbid: RecordingMbid
  get() = id.toRecordingMbid()

@Suppress("NOTHING_TO_INLINE")
public inline fun String.toRecordingMbid(): RecordingMbid {
  if (Mbid.logInvalidMbid && isInvalidMbid()) Timber.w("Invalid RecordingMbid")
  return RecordingMbid(this)
}
