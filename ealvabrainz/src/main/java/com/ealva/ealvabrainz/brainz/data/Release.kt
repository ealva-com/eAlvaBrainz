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
import com.ealva.ealvabrainz.moshi.FallbackOnNull
import com.squareup.moshi.Json
//import com.squareup.moshi.JsonClass

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
//@JsonClass(generateAdapter = true)  Only generate when changed - we use custom for "packaging"
data class Release(
  /** The MusicBrainz ID (MBID) */
  var id: String = "",
  /** The title of the release. */
  var title: String = "",
  /** The date the release was issued */
  var date: String = "",
  @field:Json(name = "release-group") @field:FallbackOnNull
  var releaseGroup: ReleaseGroup = ReleaseGroup.NullReleaseGroup,
  @field:Json(name = "release-events") var releaseEvents: List<ReleaseEvent> = emptyList(),
  /** The artist(s) that the release is primarily credited to, as credited on the release. */
  @field:Json(name = "artist-credit") var artistCredit: List<ArtistCredit> = emptyList(),
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
  var status: String = "",
  /**
   * The name of the physical packaging that accompanies the release. See the
   * [list of packaging](https://musicbrainz.org/doc/Release/Packaging) for more information.
   */
  var packaging: String = "",
  /**
   * The ID of the physical packaging
   */
  @field:Json(name = "packaging-id") var packagingId: String = "",
  /**
   * The barcode, if the release has one. The most common types found on releases are 12-digit
   * UPCs and 13-digit EANs.
   */
  var barcode: String = "",
  @field:Json(name = "cover-art-archive") @field:FallbackOnNull
  var coverArtArchive: CoverArtArchive = NullCoverArtArchive,
  /**
   * See the [page about disambiguation comments](https://musicbrainz.org/doc/Disambiguation_Comment)
   * for more information
   */
  var disambiguation: String = "",
  /**
   * Data quality indicates how good the data for a release is. It is not a mark of how good or
   * bad the music itself is - for that, use ratings.
   * * **High quality**
   * All available data has been added, if possible including cover art with liner info that proves it.
   * * **Default quality**
   * This is the default setting - technically "unknown" if the quality has never been modified,
   * "normal" if it has.
   * * **Low quality**
   * The release needs serious fixes, or its existence is hard to prove (but it's not clearly fake).
   */
  var quality: String = "",
  /** The country the release was issued in. */
  var country: String = "",
  var asin: String = "",

  var media: List<Medium> = emptyList(),
  /**
   * The label which issued the release. There may be more than one.
   * */
  @field:Json(name = "label-info") var labelInfo: List<LabelInfo> = emptyList(),
  @field:Json(name = "track-count") var trackCount: Int = 0,
  @field:Json(name = "status-id") var statusId: String = "",
  /**
   * The language and script a release's track list is written in.
   */
  @field:Json(name = "text-representation") @field:FallbackOnNull
  var textRepresentation: TextRepresentation = TextRepresentation.NullTextRepresentation,
  var aliases: List<Alias> = emptyList(),
  /**
   * See the [page about annotations](https://musicbrainz.org/doc/Annotation) for more information.
   */
  var annotation: String = "",
  var genres: List<Genre> = emptyList(),
  var tags: List<Tag> = emptyList(),
  @field:Json(name = "primary-type-id") var primaryTypeId: String = "",
  @field:Json(name = "secondary-types") var secondaryTypes: List<String> = emptyList(),
  @field:Json(name = "secondary-type-ids") var secondaryTypeIds: List<String> = emptyList(),
  @field:Json(name = "first-release-date") var firstReleaseDate: String = "",
  @field:FallbackOnNull var rating: Rating = Rating.NullRating,
  @field:Json(name = "primary-type") var primaryType: String = "",
  var releases: List<Release> = emptyList(),
  /** Only relevant if returned from query */
  var score: Int = 0
) {

  override fun toString(): String {
    return toJSon()
  }

  interface Lookup : Include

  @Suppress("unused")
  enum class Subqueries(override val value: String) : Lookup {
    Artists("artists"),
    Collections("collections"),
    Labels("labels"),
    Recordings("recordings"),
    ReleaseGroups("release-groups"),
    DiscIds("discids"),              // include discids for all media in the releases
    Media("media"),                  // include media for all releases, this includes the # of tracks on each medium and its format.
    Isrcs("isrcs"),                  // include isrcs for all recordings
    ArtistCredits("artist-credits"); // include artists credits for all releases and recordings
  }

  @Suppress("unused")
  enum class Misc(override val value: String) : Lookup {
    Aliases("aliases"),       // include artist, label, area or work aliases; treat these as a set, as they are not deliberately ordered
    Annotation("annotation"),
    Tags("tags"),
    Ratings("ratings"),
    Genres("genres")
  }

  enum class SearchFields(val value: String) {
    /** artist MusicBrainz id (MBID) */
    ArtistId("arid"),

    /** complete artist name(s) as it appears on the release */
    Artist("artist"),

    /** an artist on the release, each artist added as a separate field */
    ArtistName("artistname"),

    /** the Amazon ASIN for this release */
    Asin("asin"),

    /** The barcode of this release */
    Barcode("barcode"),

    /** The catalog number for this release, can have multiples when major using an imprint */
    CatalogueNumber("catno"),

    /** Disambiguation comment */
    Comment("comment"),

    /** The two letter country code for the release country */
    Country("country"),

    /** name credit on the release, each artist added as a separate field */
    CreditName("creditname"),

    /** The release date (format: YYYY-MM-DD) */
    Date("date"),

    /** total number of cd ids over all mediums for the release */
    DiscIdCount("discids"),

    /** number of cd ids for the release on a medium in the release */
    MediumDiscCount("discidsmedium"),

    /** release format */
    Format("format"),

    /**
     * The label MusicBrainz id (MBID) for this release, a release can have multiples when
     * major using an imprint
     */
    LabelId("laid"),

    /** The name of the label for this release, can have multiples when major using an imprint */
    Label("label"),

    /**
     * The language for this release. Use the three character ISO 639-3 codes to search for a
     * specific language. (e.g. lang:eng)
     */
    Language("lang"),

    /** number of mediums in the release */
    MediumCount("mediums"),

    /** primary type of the release group (album, single, ep, other) */
    PrimaryType("primarytype"),

    /** The release contains recordings with these puids */
    Puid("puid"),

    /** The quality of the release (low, normal, high) */
    Quality("quality"),

    /** release MusicBrainz id (MBID) */
    ReleaseId("reid"),

    /** release name */
    Release("release"),

    /** name of the release with any accent characters retained */
    ReleaseAccentedName("releaseaccent"),

    /** release group MusicBrainz id (MBID) */
    ReleaseGroupId("rgid"),

    /** The 4 character script code (e.g. latn) used for this release */
    Script("script"),

    /**
     * secondary type of the release group (audiobook, compilation, interview, live, remix,
     * soundtrack, spokenword)
     */
    SecondaryType("secondarytype"),

    /** release status (e.g official) */
    Status("status"),

    /** a tag that appears on the release */
    Tag("tag"),

    /** total number of tracks over all mediums on the release */
    TrackCount("tracks"),

    /** number of tracks on a medium in the release */
    MediumTrackCount("tracksmedium"),

    /** type of the release group, old type mapping for when we did not have separate primary
     * and secondary types
     */
    Type("type"),
  }

  companion object {
    val NullRelease = Release(id = NullObject.ID)
    val fallbackMapping: Pair<String, Any> = Release::class.java.name to NullRelease

  }
}

inline val Release.isNullObject
  get() = this === NullRelease

inline class ReleaseMbid(override val value: String) : Mbid

inline val Release.mbid
  get() = ReleaseMbid(id)

@Suppress("NOTHING_TO_INLINE")
inline fun String.toReleaseMbid(): ReleaseMbid {
  return ReleaseMbid(this)
}

/** Assumes artist credit not present is exceptional */
inline val Release.theArtistMbid: String
  get() {
    return try {
      artistCredit[0].artist.id
    } catch (e: Exception) {
      ""
    }
  }

/** Assumes artist credit not present is exceptional */
inline val Release.theArtistName: String
  get() {
    return try {
      artistCredit[0].artist.name
    } catch (e: Exception) {
      ""
    }
  }

/** Assumes artist credit not present is exceptional */
inline val Release.theArtistSortName: String
  get() {
    return try {
      artistCredit[0].artist.theSortName
    } catch (e: Exception) {
      ""
    }
  }

