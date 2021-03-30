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
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
public class LabelInfo(
  /** This is a number assigned to the release by the label which can often be found on the spine
   * or near the barcode. There may be more than one, especially when multiple labels are involved.
   * This is not the ASIN — there is a relationship for that — nor the label code.
   */
  @field:Json(name = "catalog-number") public val catalogNumber: String = "",
  @field:FallbackOnNull public val label: Label = Label.NullLabel
) {
  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as LabelInfo

    if (catalogNumber != other.catalogNumber) return false
    if (label != other.label) return false

    return true
  }

  override fun hashCode(): Int {
    var result = catalogNumber.hashCode()
    result = 31 * result + label.hashCode()
    return result
  }

  public companion object {
    public val NullLabelInfo: LabelInfo = LabelInfo()
    public val fallbackMapping: Pair<String, Any> = LabelInfo::class.java.name to NullLabelInfo
  }
}

public val LabelInfo.isNullObject: Boolean
  get() = this === NullLabelInfo
