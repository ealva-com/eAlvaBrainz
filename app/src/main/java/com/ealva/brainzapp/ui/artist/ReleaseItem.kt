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

@file:Suppress("Indentation", "MagicNumber", "unused")

package com.ealva.brainzapp.ui.artist

import android.content.Context
import android.graphics.Color
import android.text.SpannableStringBuilder
import com.ealva.brainzapp.ui.view.sp
import com.ealva.ealvabrainz.brainz.data.ArtistMbid
import com.ealva.ealvabrainz.brainz.data.LabelMbid
import com.ealva.ealvabrainz.brainz.data.ReleaseMbid
import com.ealva.ealvabrainz.brainz.data.toReleaseMbid
import com.ealva.ealvabrainz.common.AlbumTitle
import com.ealva.ealvabrainz.common.ArtistName
import com.ealva.ealvabrainz.common.LabelName
import com.ealva.ealvabrainz.common.toAlbumTitle
import me.gujun.android.span.span
import splitties.resources.appStyledColor

class CreditItem(
  val artistMbid: ArtistMbid,
  val artistName: ArtistName,
  val joinPhrase: String
)

@JvmName(name = "creditListToSpannable")
fun List<CreditItem>.toSpannable(clicked: (CreditItem) -> Unit): SpannableStringBuilder {
  return span {
    this@toSpannable.forEachIndexed { index, credit ->
      if (index > 0) append(" ")
      span(credit.artistName.value) {
        textDecorationLine = "underline"
        textColor = Color.BLUE
        onClick = {
          clicked(credit)
        }
      }
      if (credit.joinPhrase.isNotBlank()) {
        append(" ")
        append(credit.joinPhrase)
      }
    }
  }
}

class LabelItem(
  val labelMbid: LabelMbid,
  val name: LabelName,
  val disambiguation: String
)

@JvmName(name = "labelListToSpannable")
fun List<LabelItem>.toSpannable(
  ctx: Context,
  clicked: (LabelItem) -> Unit
): SpannableStringBuilder {
  return span {
    val disambiguationSize = ctx.sp(13)

    this@toSpannable.forEachIndexed { index, label ->
      if (index > 0) append(", ")
      span(label.name.value) {
        textDecorationLine = "underline"
        textColor = Color.BLUE
        onClick = { clicked(label) }
      }
      if (label.disambiguation.isNotBlank()) {
        span("(${label.disambiguation})") {
          textSize = disambiguationSize
          textColor = appStyledColor(android.R.attr.textColorSecondary)
          onClick = { clicked(label) }
        }
      }
    }
  }
}

class ReleaseItem private constructor(
  val id: Long,
  val mbid: ReleaseMbid,
  val title: AlbumTitle,
  val format: String,
  val tracks: String,
  val country: String,
  val date: String,
  val labels: List<LabelItem>,
  val catalogNumber: String,
  val barcode: String,
  val artistCredits: List<CreditItem>
) {

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as ReleaseItem

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
      title: AlbumTitle,
      format: String,
      tracks: String,
      country: String,
      date: String,
      labels: List<LabelItem>,
      catalogNumber: String,
      barcode: String,
      artistCredits: List<CreditItem>
    ): ReleaseItem {
      ++latestId
      return ReleaseItem(
        latestId,
        mbid,
        title,
        format,
        tracks,
        country,
        date,
        labels,
        catalogNumber,
        barcode,
        artistCredits
      )
    }

    val NullDisplayRelease: ReleaseItem =
      ReleaseItem(
        -1L,
        "".toReleaseMbid(),
        "".toAlbumTitle(),
        format = "",
        tracks = "",
        country = "",
        date = "",
        labels = emptyList(),
        catalogNumber = "",
        barcode = "",
        artistCredits = emptyList()
      )
  }
}
