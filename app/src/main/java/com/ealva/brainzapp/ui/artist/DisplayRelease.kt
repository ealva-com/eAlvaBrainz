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

package com.ealva.brainzapp.ui.artist

import com.ealva.brainzsvc.common.ReleaseName
import com.ealva.brainzsvc.common.toReleaseName
import com.ealva.ealvabrainz.brainz.data.ReleaseMbid
import com.ealva.ealvabrainz.brainz.data.toReleaseMbid

class DisplayRelease private constructor(
  val id: Long,
  val mbid: ReleaseMbid,
  val name: ReleaseName
) {

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as DisplayRelease

    if (id != other.id) return false

    return true
  }

  override fun hashCode(): Int {
    return id.hashCode()
  }

  override fun toString(): String {
    return "DisplayRelease(id=$id)"
  }

  companion object {
    private var latestId = 0L
    fun make(
      mbid: ReleaseMbid,
      name: ReleaseName
    ): DisplayRelease {
      ++latestId
      return DisplayRelease(
        latestId,
        mbid,
        name
      )
    }

    val NullDisplayRelease =
      DisplayRelease(
        -1L,
        "".toReleaseMbid(),
        "".toReleaseName()
      )
  }
}
