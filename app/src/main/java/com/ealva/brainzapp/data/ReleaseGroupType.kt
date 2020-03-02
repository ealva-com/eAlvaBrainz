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

package com.ealva.brainzapp.data

import androidx.annotation.StringRes
import com.ealva.ealvabrainz.R
import timber.log.Timber

/**
 * The type of a release group describes what kind of release group it is. It is divided in two:
 * a release group can have a "main" type and an unspecified number of extra types.
 *
 * MusicBrainz [Release Group Type](https://musicbrainz.org/doc/Release_Group/Type)
 */
sealed class ReleaseGroupType(val name: String, @field:StringRes private val displayName: Int) {

  /**
   * Takes a [fetch] function parameter which converts a string resource constant to a String.
   * Not all types will use the fetcher; currently [Primary.Unrecognized] and
   * [Secondary.Unrecognized] both return their [name] value
   *
   * [fetch] is typically a function like Context.getString() or a lambda. We don't want to have
   * Context as a dependency
   */
  open fun getDisplayName(fetch: (stringRes: Int) -> String): String {
    return fetch(displayName)
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as ReleaseGroupType

    if (name != other.name) return false
    if (displayName != other.displayName) return false

    return true
  }

  override fun hashCode(): Int {
    var result = name.hashCode()
    result = 31 * result + displayName
    return result
  }

  /**
   * ## Primary types
   * * **Album**
   * An album, perhaps better defined as a "Long Play" (LP) release, generally consists of previously unreleased material (unless this type is combined with secondary types which change that, such as "Compilation").
   * * **Single**
   * A single has different definitions depending on the market it is released for.
   * * **EP**
   * An EP is a so-called "Extended Play" release and often contains the letters EP in the title. Generally an EP will be shorter than a full length release (an LP or "Long Play") and the tracks are usually exclusive to the EP, in other words the tracks don't come from a previously issued release. EP is fairly difficult to define; usually it should only be assumed that a release is an EP if the artist defines it as such.
   * * **Broadcast**
   * An episodic release that was originally broadcast via radio, television, or the Internet, including podcasts.
   * * **Other**
   * Any release that does not fit or can't decisively be placed in any of the categories above.
   */
  sealed class Primary(name: String, @StringRes displayName: Int) :
    ReleaseGroupType(name, displayName) {

    object Album : Primary("Album", R.string.Album)
    object Single : Primary("Single", R.string.Single)
    object EP : Primary("EP", R.string.EP)
    object Broadcast : Primary("Broadcast", R.string.Broadcast)
    object Other : Primary("Other", R.string.Other)
    class Unrecognized(name: String) : Primary(name, R.string.empty) {
      override fun getDisplayName(fetch: (stringRes: Int) -> String) = name
    }

    object Unknown : Primary("", R.string.empty)
  }

  /**
   * ## Secondary types
   * * **Compilation**
   * Compilation should be used in addition to, not instead of, other types: for example, a
   * various artists soundtrack using pre-released music should be marked as both a soundtrack
   * and a compilation. As a general rule, always select every secondary type that applies. A
   * compilation, for the purposes of the MusicBrainz database, covers the following types of
   * releases:
   *     + a collection of recordings from various old sources (not necessarily released) combined
   *     together. For example a "best of", retrospective or rarities type release.
   *     + a various artists song collection, usually based on a general theme ("Songs for Lovers"),
   *     a particular time period ("Hits of 1998"), or some other kind of grouping ("Songs From the
   *     Movies", the "Café del Mar" series, etc).
   * * **Soundtrack**
   * A soundtrack is the musical score to a movie, TV series, stage show, computer game etc. In the
   * specific cases of computer games, a game CD with audio tracks should be classified as a
   * soundtrack: the musical properties of the CD are more interesting to MusicBrainz than the data
   * properties.
   * * **Spokenword**
   * Non-music spoken word releases.
   * * **Interview**
   * An interview release contains an interview, generally with an artist.
   * * **Audiobook**
   * An audiobook is a book read by a narrator without music.
   * * **Audio drama**
   * An audio drama is an audio-only performance of a play (often, but not always, meant for radio).
   * Unlike audiobooks, it usually has multiple performers rather than a main narrator.
   * * **Live**
   * A release that was recorded live.
   * * **Remix**
   * A release that primarily contains remixed material.
   * * **DJ-mix**
   * A DJ-mix is a sequence of several recordings played one after the other, each one modified so
   * that they blend together into a continuous flow of music. A DJ mix release requires that the
   * recordings be modified in some manner, and the DJ who does this modification is usually
   * (although not always) credited in a fairly prominent way.
   * * **Mixtape/Street**
   * Promotional in nature (but not necessarily free), mixtapes and street albums are often
   * released by artists to promote new artists, or upcoming studio albums by prominent artists.
   * They are also sometimes used to keep fans' attention between studio releases and are most
   * common in rap & hip hop genres. They are often not sanctioned by the artist's label, may lack
   * proper sample or song clearances and vary widely in production and recording quality. While
   * mixtapes are generally DJ-mixed, they are distinct from commercial DJ mixes (which are usually
   * deemed compilations) and are defined by having a significant proportion of new material,
   * including original production or original vocals over top of other artists' instrumentals.
   * They are distinct from demos in that they are designed for release directly to the public and
   * fans; not to labels.
   */
  sealed class Secondary(name: String, @StringRes displayName: Int) :
    ReleaseGroupType(name, displayName) {
    object Compilation : Secondary("Compilation", R.string.Compilation)
    object Soundtrack : Secondary("Soundtrack", R.string.Soundtrack)
    object SpokenWord : Secondary("Spokenword", R.string.Spoken_word)
    object Interview : Secondary("Interview", R.string.Interview)
    object Audiobook : Secondary("Audiobook", R.string.Audiobook)
    object AudioDrama : Secondary("Audio drama", R.string.Audio_drama)
    object Live : Secondary("Live", R.string.Live)
    object Remix : Secondary("Remix", R.string.Remix)
    object DJMix : Secondary("DJ-mix", R.string.DJ_mix)
    object MixtapeStreet : Secondary("Mixtape/Street", R.string.MixtapeSlashStreet)
    object Demo: Secondary("Demo", R.string.Demo)
    class Unrecognized(name: String) : Secondary(name, R.string.empty) {
      override fun getDisplayName(fetch: (stringRes: Int) -> String) = name
    }
    object Unknown : Secondary("", R.string.empty)
  }

  companion object {
    fun mapOfPrimary(): MutableMap<String, Primary> {
      return primaryMap.toMutableMap()
    }

    fun mapOfSecondary(): MutableMap<String, Secondary> {
      return secondaryMap.toMutableMap()
    }
  }
}

private val primaryMap = mapOf(
  makePair(ReleaseGroupType.Primary.Album),
  makePair(ReleaseGroupType.Primary.Single),
  makePair(ReleaseGroupType.Primary.EP),
  makePair(ReleaseGroupType.Primary.Broadcast),
  makePair(ReleaseGroupType.Primary.Other)
)

fun String.toPrimaryReleaseGroupType(): ReleaseGroupType.Primary {
  return primaryMap[this] ?: if (this.isBlank()) ReleaseGroupType.Primary.Unknown else {
    Timber.e("Unrecognized primary ReleaseGroup type %s", this)
    ReleaseGroupType.Primary.Unrecognized(this)
  }
}

private val secondaryMap = mapOf(
  makePair(ReleaseGroupType.Secondary.Compilation),
  makePair(ReleaseGroupType.Secondary.Soundtrack),
  makePair(ReleaseGroupType.Secondary.SpokenWord),
  makePair(ReleaseGroupType.Secondary.Interview),
  makePair(ReleaseGroupType.Secondary.Audiobook),
  makePair(ReleaseGroupType.Secondary.AudioDrama),
  makePair(ReleaseGroupType.Secondary.Live),
  makePair(ReleaseGroupType.Secondary.Remix),
  makePair(ReleaseGroupType.Secondary.DJMix),
  makePair(ReleaseGroupType.Secondary.MixtapeStreet),
  makePair(ReleaseGroupType.Secondary.Demo)
)

fun String.toSecondaryReleaseGroupType(): ReleaseGroupType.Secondary {
  return secondaryMap[this] ?: if (isBlank()) ReleaseGroupType.Secondary.Unknown else {
    Timber.e("Unrecognized secondary ReleaseGroup type '%s'", this)
    ReleaseGroupType.Secondary.Unrecognized(this)
  }
}

fun List<String>.toSecondaryReleaseGroupList(): List<ReleaseGroupType.Secondary> {
  return mapTo(mutableListOf()) {
    it.toSecondaryReleaseGroupType()
  }
}

/**
 * Joins a [ReleaseGroupType.Primary] instance with a, possible empty, list of
 * [ReleaseGroupType.Secondary]. If this is [ReleaseGroupType.Primary.Unknown] then it will
 * not appear in the result. Any [ReleaseGroupType.Secondary.Unknown] in the [secondaryList]
 * will also not appear in the results.
 *
 * Examples:
 * ```kotlin
 * Album.toDisplayString(listOf(Compilation)) { context.getString(it) }  // "Album + Compilation"
 * Other.toDisplayString(listOf(Remix, DJMix)) { context.getString(it) } // "Other + Remix + DJ mix"
 * Unknown.toDisplayString(listOf(MixtapeStreet, Remix)) { context.getString(it) } = "Mixtype/Street + Remix"
 * Single.toDisplayString(listOf()) { context.getString(it) } = "Single"
 * ```
 */
fun ReleaseGroupType.Primary.toDisplayString(
  secondaryList: List<ReleaseGroupType.Secondary>,
  fetch: (stringRes: Int) -> String
): String {
  return buildString {
    val havePrimary = if (this@toDisplayString !== ReleaseGroupType.Primary.Unknown) {
      append(this@toDisplayString.getDisplayName(fetch))
      true
    } else false

    var haveSecondary = false
    secondaryList.forEach { secondary ->
      if (secondary !== ReleaseGroupType.Secondary.Unknown) {
        if (havePrimary || haveSecondary) append(" + ")
        append(secondary.getDisplayName(fetch))
        haveSecondary = true
      }
    }
  }
}

@Suppress("NOTHING_TO_INLINE")
private inline fun makePair(type: ReleaseGroupType.Primary) = Pair(type.name, type)

@Suppress("NOTHING_TO_INLINE")
private inline fun makePair(type: ReleaseGroupType.Secondary) = Pair(type.name, type)
