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

import com.ealva.ealvabrainz.brainz.data.LabelInfo.Companion.NullLabelInfo
import com.ealva.ealvabrainz.moshi.FallbackOnNull
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LabelInfo(
  /** This is a number assigned to the release by the label which can often be found on the spine
   * or near the barcode. There may be more than one, especially when multiple labels are involved.
   * This is not the ASIN — there is a relationship for that — nor the label code.
   */
  @field:Json(name = "catalog-number") var catalogNumber: String = "",
  @field:FallbackOnNull var label: Label = Label.NullLabel
) {
  companion object {
    val NullLabelInfo = LabelInfo()
    val fallbackMapping: Pair<String, Any> = LabelInfo::class.java.name to NullLabelInfo
  }
}

val LabelInfo.isNullObject
  get() = this === NullLabelInfo
