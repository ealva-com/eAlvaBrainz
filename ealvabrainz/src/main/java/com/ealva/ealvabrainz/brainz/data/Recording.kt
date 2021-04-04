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
  public val id: String = "",
  /** The title of the recording. */
  public val title: String = "",
  /** The artist(s) that the recording is primarily credited to. */
  @field:Json(name = "artist-credit") public val artistCredit: List<ArtistCredit> = emptyList(),
  /**
   * The length of the recording. It's only entered manually for standalone recordings. For
   * recordings that are being used on releases, the recording length is the median length of all
   * tracks (that have a track length) associated with that recording. If there is an even number
   * of track lengths, the smaller median candidate is used.
   */
  public val length: Int = 0,
  @field:Json(name = "first-release-date") public val firstReleaseDate: String = "",
  public val aliases: List<Alias> = emptyList(),
  /**
   * See the
   * [page about disambiguation comments](https://musicbrainz.org/doc/Disambiguation_Comment)
   * for more information
   */
  public val disambiguation: String = "",
  /**
   * See the [page about annotations](https://musicbrainz.org/doc/Annotation) for more information.
   */
  public val annotation: String = "",
  /** The International Standard Recording Code assigned to the recording. */
  public val isrcs: List<String> = emptyList(),
  public val relations: List<Relation> = emptyList(),
  public val releases: List<Release> = emptyList(),
  @field:FallbackOnNull public val rating: Rating = Rating.NullRating,
  @field:FallbackOnNull @field:Json(name = "user-rating") public val userRating: Rating =
    Rating.NullRating,
  public val tags: List<Tag> = emptyList(),
  @field:Json(name = "user-tags") public val userTags: List<Tag> = emptyList(),
  public val genres: List<Genre> = emptyList(),
  @field:Json(name = "user-genres") public val userGenres: List<Genre> = emptyList(),
  /** If this recording is video */
  public val video: Boolean = false,
  /** used with queries */
  public val score: Int = 0
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

  public enum class Include(override val value: String) : Inc {
    Artists("artists"),
    Releases("releases"),
    Isrcs("isrcs"),

    /** DiscIds requires [Releases] also be specified and inc params can (currently) repeat */
    DiscIds("discids"),
    Media("media"),
    ArtistCredits("artist-credits"),

    Aliases("aliases"),
    Annotation("annotation"),
    Tags("tags"),
    UserTags("user-tags"),
    Ratings("ratings"),
    UserRatings("user-ratings"),
    Genres("genres"),
    UserGenres("user-genres")
  }

  @Suppress("unused")
  public enum class Browse(override val value: String) : Inc {
    ArtistCredits("artist-credits"),
    Isrcs("isrcs"),
    Annotation("annotation"),
    Tags("tags"),
    UserTags("user-tags"),
    Genres("genres"),
    UserGenres("user-genres"),
    Ratings("ratings"),
    UserRatings("user-ratings");
  }

  public enum class SearchField(public override val value: String) : EntitySearchField {
    /**
     * (part of) any [alias](https://musicbrainz.org/doc/Aliases) attached to the recording
     * (diacritics are ignored)
     */
    Alias("alias"),

    /** the MBID of any of the recording artists */
    ArtistId("arid"),

    /**
     * (part of) the combined credited artist name for the recording, including join phrases
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

    /** Default searches [Recording] */
    Default(""),

    /** duration of track in milliseconds */
    Duration("dur"),

    /** the release date of the earliest release including this recording (e.g. "1980-01-22") */
    FirstReleaseDate("firstreleasedate"),

    /**
     * the [format](https://musicbrainz.org/doc/Release/Format) of any medium including this
     * recording (insensitive to case, spaces, and separators)
     */
    Format("format"),

    /**
     * The International Standard Recording Code [(ISRC)](https://musicbrainz.org/doc/ISRC), an
     * identification system for audio and music video recordings. Includes isrcs for all recordings
     */
    Isrc("isrc"),

    /** the free-text number of the track on any medium including this recording (e.g. "A4")  */
    Number("number"),

    /** the position inside its release of any medium including this recording (starts at 1)  */
    Position("position"),

    /**
     * the primary [type](https://musicbrainz.org/doc/Release_Group/Type#Primary_types) of any
     * release group including this recording
     */
    PrimaryType("primarytype"),

    /** the recording duration, quantized (duration in milliseconds / 2000) */
    QuantizedDuration("qdur"),

    /**
     * (part of) the recording's title, or the name of a track connected to this recording
     * (diacritics are ignored)
     */
    Recording("recording"),

    /**
     * (part of) the recording's name, or the name of a track connected to this recording (with the
     * specified diacritics)
     */
    RecordingAccent("recordingaccent"),

    /** (part of) the name of any release including this recording */
    Release("release"),

    /** the MBID of any release group including this recording */
    ReleaseId("reid"),

    /** the MBID of any release group including this recording */
    ReleaseGroupId("rgid"),

    /** the recording's MBID  */
    RecordingId("rid"),

    /**
     * any of the [secondary types](https://musicbrainz.org/doc/Release_Group/Type#Secondary_types)
     * of any release group including this recording
     */
    SecondaryType("secondarytype"),

    /**
     * the [status](https://musicbrainz.org/doc/Release#Status) of any release including this
     * recording
     */
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
    TrackCount("tracks"),

    /** the number of tracks on any release (as a whole) including this recording */
    ReleaseTrackCount("tracksrelease"),

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

@JvmInline
public value class RecordingMbid(override val value: String) : Mbid

public inline val Recording.mbid: RecordingMbid
  get() = RecordingMbid(id)
