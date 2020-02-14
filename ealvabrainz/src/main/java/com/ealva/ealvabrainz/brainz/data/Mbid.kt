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
interface Mbid {
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
  val value: String
}

fun Mbid.appearsValid(): Boolean = with(value) {
  if (length != 36) return false

  val groups = value.split("-")
  if (groups.size != 5) return false

  return checkGroup(groups[0], 8) &&
    checkGroup(groups[1], 4) &&
    checkGroup(groups[2], 4) &&
    checkGroup(groups[3], 4) &&
    checkGroup(groups[4], 12)
}

/**
 * Check length of group and that is a hexadecimal string
 */
private fun checkGroup(group: String, expectedLength: Int): Boolean {
  return try {
    group.length == expectedLength && group.toLong(16) >= 0
  } catch (e: Exception) {
    false
  }
}

