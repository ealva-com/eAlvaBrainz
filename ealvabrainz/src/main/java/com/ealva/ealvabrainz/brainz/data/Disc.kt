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

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Disc ID [“5MniTJ1axfp8JU.YZml7CRPArzc-”](https://musicbrainz.org/cdtoc/5MniTJ1axfp8JU.YZml7CRPArzc-)
 *
 * CD TOC details
 * ```
 * Full TOC:      1 4 61460 183 18098 32238 46648
 * Disc ID:       5MniTJ1axfp8JU.YZml7CRPArzc-
 * FreeDB:        21033104
 * Total tracks:  4
 * Total length:  13:39
 * ```
 * ```
 * Track details:
 * Track 	Start 	Length 	End
 * Time 	Sectors 	Time 	Sectors 	Time 	Sectors
 * 1 	0:02 	183 	3:59 	17915 	4:01 	18098
 * 2 	4:01 	18098 	3:09 	14140 	7:10 	32238
 * 3 	7:10 	32238 	3:12 	14410 	10:22 	46648
 * 4 	10:22 	46648 	3:17 	14812 	13:39 	61460
 * ```
 */
@JsonClass(generateAdapter = true)
data class Disc(
  /**
   * ID of the Disc. NOT a MusicBrainz ID (MBID)
   *
   * Disc ID is the code number which MusicBrainz uses to link a physical CD to a release listing.
   * It is a string of letters, like XzPS7vW.HPHsYemQh0HBUGr8vuU-.
   */
  var id: String = "",
  var sectors: Int = 0,
  var offsets: List<Int> = emptyList(),
  @field:Json(name = "offset-count") var offsetCount: Int = 0
) {
  companion object {
    val NullDisc = Disc(id = NullObject.ID)
    val fallbackMapping: Pair<String, Any> = Disc::class.java.name to NullDisc
  }
}

inline val Disc.isNullObject
  get() = this === Disc.NullDisc

inline class DiscMbid(override val value: String) : Mbid

inline val Disc.mbid
  get() = DiscMbid(id)
