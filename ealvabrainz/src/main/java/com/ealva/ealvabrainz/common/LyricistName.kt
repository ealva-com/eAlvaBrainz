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
 * Name of a Lyricist artist, be it a person or a band. The may be a partial name if used in a
 * query
 */
@Parcelize
@JvmInline
public value class LyricistName(public val value: String) : Parcelable {
  public companion object {
    public val UNKNOWN: LyricistName = LyricistName("Unknown")
  }
}

/**
 * Convert this String to an [LyricistName] or [LyricistName.UNKNOWN] if this is null.
 */
public inline val String?.asLyricistName: LyricistName
  get() = this?.let { LyricistName(it.trim()) } ?: LyricistName.UNKNOWN

public inline val LyricistName.isBlank: Boolean get() = value.isBlank()
