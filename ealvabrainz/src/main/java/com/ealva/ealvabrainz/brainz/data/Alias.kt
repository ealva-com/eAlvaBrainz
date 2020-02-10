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

import com.ealva.ealvabrainz.brainz.data.Alias.Companion.NullAlias
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * When searching releases, the release list contains an artist alias which has "begin-date" and
 * "end-date". When looking up artist the alias has "begin" and "end".
 */
@JsonClass(generateAdapter = true)
data class Alias(
  var name: String = "",
  @field:Json(name = "sort-name") var sortName: String = "",
  var type: String = "",
  @field:Json(name = "type-id") var typeId: String = "",
  var primary: Boolean = false,
  var locale: String = "",
  var begin: String = "",
  var end: String = "",
  var ended: Boolean = false,
  @field:Json(name = "begin-date") var beginDate: String = "",
  @field:Json(name = "end-date") var endDate: String = ""
) {
  override fun toString(): String {
    return toJSon()
  }

  companion object {
    val NullAlias = Alias(name = NullObject.NAME)
    val fallbackMapping: Pair<String, Any> = Alias::class.java.name to NullAlias
  }
}

inline val Alias.isNullObject
  get() = this === NullAlias
