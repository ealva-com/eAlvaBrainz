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
 * Aliases are alternative names for all types of MusicBrainz entities. There is no limit on the
 * number of aliases set for an entity. In MusicBrainz, aliases have several uses:
 *
 * * In a **search**, when an entity's alias matches a search term (even if the entity's actual
 * name does not), the entity will be given as a result.
 * * Aliases are used to **localize** an entity's main name into a different language, especially
 * into a different script (e.g. the Russian name Чайковский is known in English as "Tchaikovsky"
 * and in German "Tschaikowski").
 * * The **change over time** of an entity's name can be documented by aliases (e.g. Carnegie Hall
 * was known as "Music Hall" from 1891 to 1893).
 * * The **legal name** of an artist who normally performs under a stage name can be documented
 * by an alias, among other methods (e.g. Lady Gaga's legal name is
 * Stefani Joanne Angelina Germanotta).
 *
 * When searching releases, the release list contains an artist alias which has "begin-date" and
 * "end-date". When looking up artist the alias has "begin" and "end". Use [startingDate] and
 * [endingDate]
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

/** [Alias.begin] if not empty, else [Alias.beginDate] */
val Alias.startingDate
  get() = if (begin.isNotEmpty()) begin else beginDate

/** [Alias.end] if not empty, else [Alias.endDate] */
val Alias.endingDate
  get() = if (end.isNotEmpty()) end else endDate
