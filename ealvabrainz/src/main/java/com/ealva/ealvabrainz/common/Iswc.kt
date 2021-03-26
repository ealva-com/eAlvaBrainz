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

package com.ealva.ealvabrainz.common

/**
 * The International Standard Musical Work Code (ISWC) identifies a musical work as a unique
 * intangible creation. It relates to the result of an intangible creation of one or more people,
 * regardless of copyright status, distributions or agreements that cover this creation.
 *
 * [ISWC](https://www.iswc.org/)
 *
 * [ISWC Wikipedia](https://en.wikipedia.org/wiki/International_Standard_Musical_Work_Code)
 *
 * [MusicBrainz ISWC](https://musicbrainz.org/doc/ISWC)
 */
@JvmInline
public value class Iswc(public val value: String) {
  public companion object {
    public val UNKNOWN: Iswc = Iswc("Unknown")
  }
}

@Suppress("unused")
public fun String?.toIswc(): Iswc =
  if (this != null) Iswc(trim()) else Iswc.UNKNOWN
