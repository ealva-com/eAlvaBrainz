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

package com.ealva.ealvabrainz.moshi

import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonReader

@Suppress("unused")
internal class StringJsonAdapter {
  @FromJson
  fun fromJson(reader: JsonReader): String {
    return when (val token = reader.peek()) {
      JsonReader.Token.NULL -> {
        reader.nextNull<String>(); ""
      }
      JsonReader.Token.STRING -> reader.nextString()
      else -> {
        // Don't want to be tied to any logger here
        println("Unrecognized token ${token.name}, expecting null or string"); ""
      }
    }
  }
}
