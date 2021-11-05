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

package com.ealva.ealvabrainz.brainz.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

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
public interface Mbid : Parcelable {
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
@Parcelize
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

public fun String.isValidMbid(): Boolean {
  return length == MBID_LENGTH &&
    DASHES.all { get(it) == '-' } &&
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
  return range.all { get(it).isHex() }
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
