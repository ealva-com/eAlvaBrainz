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

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * The ISRC (International Standard Recording Code) is the international identification system for
 * sound recordings and music video recordings. Each ISRC is a unique and permanent identifier for a
 * specific recording, independent of the format on which it appears (CD, audio file, etc.), or the
 * rights owners involved. Only one ISRC should be issued for a recording, and an ISRC can never
 * represent more than one unique recording.
 *
 * [ISRC](https://isrc.ifpi.org/)
 *
 * [MusicBrainz ISRC](https://musicbrainz.org/doc/ISRC#Determining_ISRCs_of_recordings)
 */
@Parcelize
@JvmInline
public value class Isrc(public val value: String) : Parcelable {
  public companion object {
    public val UNKNOWN: Isrc = Isrc("Unknown")
  }
}

@Suppress("unused")
public fun String?.toIsrc(): Isrc =
  if (this != null) Isrc(trim()) else Isrc.UNKNOWN
