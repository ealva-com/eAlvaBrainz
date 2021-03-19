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

import android.net.Uri
import com.ealva.brainzapp.data.ReleaseGroupType
import com.ealva.brainzapp.data.StarRating
import com.ealva.brainzapp.data.toStarRating
import com.ealva.ealvabrainz.common.AlbumTitle
import com.ealva.ealvabrainz.common.ReleaseGroupMbid
import com.ealva.ealvabrainz.common.toAlbumTitle

class ReleaseGroupItem private constructor(
  val id: Long,
  val mbid: ReleaseGroupMbid,
  val title: AlbumTitle,
  val type: ReleaseGroupType.Primary,
  val secondaryTypes: List<ReleaseGroupType.Secondary>,
  val rating: StarRating,
  @Suppress("unused") val ratingVotes: Int,
  val date: String,
  var releaseCount: Int,
  var artwork: Uri?
) {

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as ReleaseGroupItem

    if (id != other.id) return false
    return true
  }

  override fun hashCode(): Int {
    return id.hashCode()
  }

  override fun toString(): String {
    return "DisplayReleaseGroup(id=$id, releaseCount=$releaseCount)"
  }

  companion object {
    private var latestId = 0L
    fun make(
      mbid: ReleaseGroupMbid,
      title: AlbumTitle,
      type: ReleaseGroupType.Primary,
      secondaryTypes: List<ReleaseGroupType.Secondary>,
      rating: StarRating,
      ratingVotes: Int,
      date: String,
      releaseCount: Int
    ): ReleaseGroupItem {
      ++latestId
      return ReleaseGroupItem(
        latestId,
        mbid,
        title,
        type,
        secondaryTypes,
        rating,
        ratingVotes,
        date,
        releaseCount,
        null
      )
    }

    val NullDisplayReleaseGroup =
      ReleaseGroupItem(
        -1L,
        ReleaseGroupMbid(""),
        "".toAlbumTitle(),
        ReleaseGroupType.Primary.Unknown,
        emptyList(),
        0.0F.toStarRating(),
        0,
        "",
        0,
        null
      )
  }
}
