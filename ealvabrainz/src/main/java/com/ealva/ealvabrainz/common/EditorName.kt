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
 * An editor is the user that created a Collection. So, your login name for MusicBrainz
 * would be the editor name for collections
 */
@Suppress("unused")
@Parcelize
@JvmInline
public value class EditorName(public val value: String) : Parcelable {
  public companion object {
    public val UNKNOWN: EditorName = EditorName("Unknown")
  }
}

public inline val String?.asEditorName: EditorName
  get() = this?.let { EditorName(it.trim()) } ?: EditorName.UNKNOWN
