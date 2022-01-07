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
 * The official name of the label. May be a partial name if used in a query.
 *
 * The label name should be represented as found on media sleeves, including use of characters from
 * non latin charsets, stylized characters, etc.
 *
 * If a label is renamed a new label should be created and a label rename relationship created
 * between the two.
 *
 * If there exists multiple slightly different names for the label (eg: The Verve Music Group, Verve
 * Music Group, VMG), you should use the most commonly used name, or the one used on the label's
 * official site.
 *
 * Labels are not always named uniquely, and different labels may share the same label name. To help
 * differentiate between identically named labels, you should use a disambiguation comment and
 * possibly an annotation as well.
 */
@Parcelize
@JvmInline
public value class LabelName(public val value: String) : Parcelable {
  public companion object {
    public val UNKNOWN: LabelName = LabelName("Unknown")
  }
}

/**
 * Convert this String to an [LabelName] or [LabelName.UNKNOWN] if this is null.
 */
public inline val String?.asLabelName: LabelName
  get() = this?.let { LabelName(it.trim()) } ?: LabelName.UNKNOWN

public inline val LabelName.isBlank: Boolean get() = value.isBlank()
