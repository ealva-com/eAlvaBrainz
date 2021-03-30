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
public class Alias(
  public val name: String = "",
  @field:Json(name = "sort-name") public val sortName: String = "",
  public val type: String = "",
  @field:Json(name = "type-id") public val typeId: String = "",
  public val primary: Boolean = false,
  public val locale: String = "",
  public val begin: String = "",
  public val end: String = "",
  public val ended: Boolean = false,
  @field:Json(name = "begin-date") public val beginDate: String = "",
  @field:Json(name = "end-date") public val endDate: String = ""
) {
  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as Alias

    if (name != other.name) return false
    if (sortName != other.sortName) return false
    if (type != other.type) return false
    if (typeId != other.typeId) return false
    if (primary != other.primary) return false

    return true
  }

  override fun hashCode(): Int {
    var result = name.hashCode()
    result = 31 * result + sortName.hashCode()
    result = 31 * result + type.hashCode()
    result = 31 * result + typeId.hashCode()
    result = 31 * result + primary.hashCode()
    return result
  }

  override fun toString(): String = toJson()

  public companion object {
    public val NullAlias: Alias = Alias(name = NullObject.NAME)
    public val fallbackMapping: Pair<String, Any> = Alias::class.java.name to NullAlias
  }
}

public inline val Alias.isNullObject: Boolean
  get() = this === NullAlias

/** [Alias.beginDate] if not empty, else [Alias.begin] */
public val Alias.startingDate: String
  get() = if (beginDate.isNotEmpty()) beginDate else begin

/** [Alias.endDate] if not empty, else [Alias.end] */
public val Alias.endingDate: String
  get() = if (endDate.isNotEmpty()) endDate else end
