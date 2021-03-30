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

import com.ealva.ealvabrainz.brainz.data.CoverArtArchive.Companion.NullCoverArtArchive
import com.ealva.ealvabrainz.brainz.data.Release.Companion.NullRelease
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * A MusicBrainz release represents the unique release (i.e. issuing) of a product on a specific
 * date with specific release information such as the country, label, barcode and packaging. If you
 * walk into a store and purchase an album or single, they are each represented in MusicBrainz as
 * one release.
 *
 * Each release belongs to a release group and contains at least one medium (commonly referred to
 * as a disc when talking about a CD release). Each medium has a tracklist.
 *
 * A medium is the actual physical medium that stores the audio content. This means that each CD in
 * a multi-disc release will be entered as separate mediums within the release, and that both sides
 * of a vinyl record or cassette will exist on one medium. Mediums have a format (e.g. CD, DVD,
 * vinyl, and cassette) and can optionally also have a title. Sometimes a medium can be a side of a
 * disc. For example, the two sides of a hybrid SACD (the CD side and the SACD side) should be
 * entered as two mediums.
 *
 * Tracklists represent the set and ordering of tracks as listed on a liner, and the same tracklist
 * can appear on more than one release. For example, a boxset compilation that contains previously
 * released CDs would share the same tracklists as the separate releases.
 *
 * [MusicBrainz Release](https://musicbrainz.org/doc/Release)
 */
@JsonClass(generateAdapter = true)
public class Release(
  /** The MusicBrainz ID (MBID) */
  public val id: String = "",
  /** The title of the release. */
  public val title: String = "",
  /** The date the release was issued */
  public val date: String = "",
  @field:Json(name = "release-group") @field:FallbackOnNull
  public val releaseGroup: ReleaseGroup = ReleaseGroup.NullReleaseGroup,
  @field:Json(name = "release-events") public val releaseEvents: List<ReleaseEvent> = emptyList(),
  /** The artist(s) that the release is primarily credited to, as credited on the release. */
  @field:Json(name = "artist-credit") public val artistCredit: List<ArtistCredit> = emptyList(),
  /**
   * The status describes how "official" a release is. Possible values are:
   * * **official**
   * Any release officially sanctioned by the artist and/or their record company. Most releases will
   * fit into this category.
   * * **promotional**
   * A give-away release or a release intended to promote an upcoming official release
   * (e.g. pre-release versions, releases included with a magazine, versions supplied to radio DJs
   * for air-play).
   * * **bootleg**
   * An unofficial/underground release that was not sanctioned by the artist and/or the record
   * company. This includes unofficial live recordings and pirated releases.
   * * **pseudo-release**
   * An alternate version of a release where the titles have been changed. These don't correspond
   * to any real release and should be linked to the original release using the transliteration
   * relationship.
   */
  public val status: String = "",
  /**
   * The name of the physical packaging that accompanies the release. See the
   * [list of packaging](https://musicbrainz.org/doc/Release/Packaging) for more information.
   */
  public val packaging: String = "",
  /**
   * The ID of the physical packaging
   */
  @field:Json(name = "packaging-id") public val packagingId: String = "",
  /**
   * The barcode, if the release has one, is a machine-readable number used as stock control
   * mechanisms by retailers. The most common types found on releases are 12-digit
   * UPCs and 13-digit EANs.
   */
  public val barcode: String = "",
  @field:Json(name = "cover-art-archive") @field:FallbackOnNull
  public val coverArtArchive: CoverArtArchive = NullCoverArtArchive,
  /**
   * See the
   * [page about disambiguation comments](https://musicbrainz.org/doc/Disambiguation_Comment)
   * for more information
   */
  public val disambiguation: String = "",
  /**
   * Data quality indicates how good the data for a release is. It is not a mark of how good or
   * bad the music itself is - for that, use ratings.
   * * **High quality**
   * All available data has been added, if possible including cover art with liner info that proves
   * it.
   * * **Default quality**
   * This is the default setting - technically "unknown" if the quality has never been modified,
   * "normal" if it has.
   * * **Low quality**
   * The release needs serious fixes, or its existence is hard to prove (but it's not clearly fake).
   */
  public val quality: String = "",
  /** The country the release was issued in. */
  public val country: String = "",
  public val asin: String = "",

  public val media: List<Medium> = emptyList(),
  /**
   * The label which issued the release. There may be more than one.
   * */
  @field:Json(name = "label-info") public val labelInfo: List<LabelInfo> = emptyList(),
  @field:Json(name = "track-count") public val trackCount: Int = 0,
  @field:Json(name = "status-id") public val statusId: String = "",
  /**
   * The language and script a release's track list is written in.
   */
  @field:Json(name = "text-representation") @field:FallbackOnNull
  public val textRepresentation: TextRepresentation = TextRepresentation.NullTextRepresentation,
  public val aliases: List<Alias> = emptyList(),
  /**
   * See the [page about annotations](https://musicbrainz.org/doc/Annotation) for more information.
   */
  public val annotation: String = "",
  public val genres: List<Genre> = emptyList(),
  public val tags: List<Tag> = emptyList(),
  @field:Json(name = "primary-type-id") public val primaryTypeId: String = "",
  @field:Json(name = "secondary-types") public val secondaryTypes: List<String> = emptyList(),
  @field:Json(name = "secondary-type-ids") public val secondaryTypeIds: List<String> = emptyList(),
  @field:Json(name = "first-release-date") public val firstReleaseDate: String = "",
  @field:FallbackOnNull public val rating: Rating = Rating.NullRating,
  /**
   * Types may be
   * * "nat"
   * * "album"
   * * "single"
   * * "ep"
   * * "compilation"
   * * "soundtrack"
   * * "spokenword"
   * * "interview"
   * * "audiobook"
   * * "live"
   * * "remix"
   * * "other"
   */
  @field:Json(name = "primary-type") public val primaryType: String = "",
  public val releases: List<Release> = emptyList(),
  public val relations: List<Relation> = emptyList(),
  /** score ranking used in query results */
  public val score: Int = 0
) {

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as Release

    if (id != other.id) return false

    return true
  }

  override fun hashCode(): Int = id.hashCode()

  override fun toString(): String = toJson()

  @Suppress("unused")
  public enum class Include(override val value: String) : Inc {
    Artists("artists"),
    Collections("collections"),
    Labels("labels"),
    Recordings("recordings"),
    ReleaseGroups("release-groups"),

    /** An ID calculated from the TOC of a CD */
    DiscIds("discids"),
    Media("media"),

    /**
     * The International Standard Recording Code, an identification system for audio and music
     * video recordings. Includes isrcs for all recordings
     */
    Isrcs("isrcs"),
    ArtistCredits("artist-credits"), // include artists credits for all releases and recordings
    Aliases("aliases"),
    Annotation("annotation"),
    Tags("tags"),
    Ratings("ratings"),
    Genres("genres");

    public companion object {
      /** Doesn't create a values() array and/or list every time */
      public val all: List<Include> by lazy { values().asList() }
    }
  }

  @Suppress("unused")
  public enum class Browse(override val value: String) : Inc {
    ArtistCredits("artist-credits"),
    Labels("labels"),
    Recordings("recordings"),
    ReleaseGroups("release-groups"),
    Media("media"),
    DiscIds("discids"),
    Isrcs("isrcs"),
    Annotation("annotation"),
    Tags("tags"),
    UserTags("user-tags"),
    Genres("genres"),
    UserGenres("user-genres"),

    /**
     * Entities supporting ratings: artist, event, instrument, label, recording, release-group,
     * work, url
     */
    Ratings("ratings"),
    UserRatings("user-ratings");
  }

  public enum class SearchField(public override val value: String) : EntitySearchField {
    /** 	(part of) any alias attached to the release (diacritics are ignored) */
    Alias("alias"),

    /** the MBID of any of the release artists  */
    ArtistId("arid"),

    /**
     * (part of) the combined credited artist name for the release, including join phrases (e.g.
     * "Artist X feat.")
     */
    Artist("artist"),

    /** (part of) the name of any of the release artists */
    ArtistName("artistname"),

    /** an Amazon [ASIN](https://musicbrainz.org/doc/ASIN) for the release */
    Asin("asin"),

    /**
     * The barcode of this release which is a machine-readable number used as stock control
     * mechanisms by retailers.
     */
    Barcode("barcode"),

    /** any catalog number for this release (insensitive to case, spaces, and separators) */
    CatalogNumber("catno"),

    /** (part of) the release's disambiguation comment */
    Comment("comment"),

    /** the 2-letter code (ISO 3166-1 alpha-2) for any country the release was released in  */
    Country("country"),

    /** (part of) the credited name of any of the release artists on this particular release */
    CreditName("creditname"),

    /** a release date for the release (e.g. "1980-01-22")  */
    Date("date"),

    /** Default searches [Release] */
    Default(""),

    /** the total number of disc IDs attached to all mediums on the release  */
    DiscIdCount("discids"),

    /** the number of disc IDs attached to any one medium on the release */
    MediumDiscCount("discidsmedium"),

    /**
     * the [format](https://musicbrainz.org/doc/Release/Format) of any medium in the release
     * (insensitive to case, spaces, and separators)
     */
    Format("format"),

    /** the MBID of any of the release labels */
    LabelId("laid"),

    /** (part of) the name of any of the release labels  */
    Label("label"),

    /**
     * the [ISO 639-3](https://iso639-3.sil.org/code_tables/639/data) code for the release language
     */
    Language("lang"),

    /** number of mediums in the release */
    MediumCount("mediums"),

    /** the number of tracks on any one medium on the release */
    MediumTrackCount("tracksmedium"),

    /**
     * the [format](https://musicbrainz.org/doc/Release/Packaging) of the release (insensitive to
     * case, spaces, and separators)
     */
    Packaging("packaging"),

    /**
     * the [primary type](https://musicbrainz.org/doc/Release_Group/Type#Primary_types) of the
     * release group for this release
     */
    PrimaryType("primarytype"),

    /**
     * the listed [quality](https://musicbrainz.org/doc/Release#Data_quality) of the data for the
     * release (one of "low", "normal", "high")
     */
    Quality("quality"),

    /** the release's MBID  */
    ReleaseId("reid"),

    /** (part of) the release's title (diacritics are ignored)  */
    Release("release"),

    /** (part of) the release's title (with the specified diacritics) */
    ReleaseAccentedName("releaseaccent"),

    /** the MBID of the release group for this release */
    ReleaseGroupId("rgid"),

    /**
     * The 4 character the [ISO 15924](http://unicode.org/iso15924/iso15924-codes.html) script
     * script code (e.g. latn) used for this release
     */
    Script("script"),

    /**
     * any of the [secondary types](https://musicbrainz.org/doc/Release_Group/Type#Secondary_types)
     * of the release group for this release
     */
    SecondaryType("secondarytype"),

    /** the [status][com.ealva.ealvabrainz.brainz.data.Release.Status] of the release */
    Status("status"),

    /** (part of) a tag attached to the release */
    Tag("tag"),

    /** total number of tracks over all mediums on the release */
    TrackCount("tracks");

    override fun toString(): String = value
  }

  /**
   * Used for any lookup which includes a [ReleaseGroup] or [Release] to limit results to
   * the that particular type of Release.
   */
  @Suppress("unused")
  public sealed class Type(override val value: String) : Piped {
    public object Nat : Type("nat")
    public object Album : Type("album")
    public object Single : Type("single")
    public object Ep : Type("ep")
    public object Compilation : Type("compilation")
    public object Soundtrack : Type("soundtrack")
    public object SpokenWord : Type("spokenword")
    public object Interview : Type("interview")
    public object Audiobook : Type("audiobook")
    public object Live : Type("live")
    public object Remix : Type("remix")
    public object Other : Type("other")
    public class Unrecognized(value: String) : Type(value)

    public companion object {
      public fun values(): Array<Type> = arrayOf(
        Nat,
        Album,
        Single,
        Ep,
        Compilation,
        Soundtrack,
        SpokenWord,
        Interview,
        Audiobook,
        Live,
        Remix,
        Other
      )

      public val values: Set<Type> = values().toSet()
    }
  }

  /**
   * Used for any lookup which includes a [Release] to limit results to that particular
   * [status][Release.status]
   */
  @Suppress("unused")
  public sealed class Status(override val value: String) : Piped {
    public object Official : Status("official")
    public object Promotion : Status("promotion")
    public object Bootleg : Status("bootleg")
    public object PseudoRelease : Status("pseudo-release")

    /**
     * If an Unrecognized is found, the type should probably be added to this group.
     */
    public class Unrecognized(value: String) : Status(value)

    public companion object {
      public fun values(): Array<Status> = arrayOf(Official, Promotion, Bootleg, PseudoRelease)
      public val values: Set<Status> by lazy { values().toSet() }
    }
  }

  public companion object {
    public val NullRelease: Release = Release(id = NullObject.ID)
    public val fallbackMapping: Pair<String, Any> = Release::class.java.name to NullRelease
  }
}

public inline val Release.isNullObject: Boolean
  get() = this === NullRelease
